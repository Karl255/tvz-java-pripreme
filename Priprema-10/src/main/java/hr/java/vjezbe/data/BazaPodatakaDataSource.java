package hr.java.vjezbe.data;

import hr.java.vjezbe.entitet.*;
import hr.java.vjezbe.iznimke.DataSourceException;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

public class BazaPodatakaDataSource implements DataSource, Closeable {
	private final Connection connection;

	public BazaPodatakaDataSource(Properties prop) throws DataSourceException, IOException {
		String dbUrl = prop.getProperty("url");
		String dbUsername = prop.getProperty("username");
		String dbPassoword = prop.getProperty("password");

		try {
			connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassoword);
		} catch (SQLException e) {
			throw new DataSourceException(e);
		}
	}

	@Override
	public void close() {
		try {
			connection.close();
		} catch (SQLException ignored) {
		}
	}

	private <T> List<T> selectGeneric(String query, ResultMapper<T> resultMapper) throws DataSourceException {
		return selectGeneric(query, null, resultMapper);
	}

	private <T> List<T> selectGeneric(String query, PreparedStatementModifier psm, ResultMapper<T> resultReader) throws DataSourceException {
		try (var s = connection.prepareStatement(query)) {
			if (psm != null) {
				psm.modify(s);
			}

			s.execute();
			var items = new ArrayList<T>();

			try (var results = s.getResultSet()) {
				while (results.next()) {
					items.add(resultReader.map(results));
				}
			}

			return items;
		} catch (SQLException e) {
			throw new DataSourceException(e);
		}
	}

	private long insertGeneric(String query, PreparedStatementModifier psm) throws DataSourceException {
		try (var s = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			psm.modify(s);
			s.execute();

			var keys = s.getGeneratedKeys();
			keys.next();
			return keys.getLong(1);
		} catch (SQLException e) {
			throw new DataSourceException(e);
		}
	}

	private static <T extends Entitet> T getEntity(List<T> entities, long id) {
		return entities.stream()
			.filter(e -> e.getId() == id)
			.findFirst()
			.orElseThrow(() -> new RuntimeException(String.format("Entity with id %d wasn't found.", id)));
	}

	private static <T extends Entitet> List<T> getEntities(List<T> entities, List<Long> ids) {
		var values = new ArrayList<T>();

		for (var id : ids) {
			entities.stream().filter(e -> e.getId() == id).findFirst().ifPresent(values::add);
		}

		return values;
	}

	// public data access methods

	@Override
	public List<Student> readStudenti() throws DataSourceException {
		return selectGeneric("SELECT * FROM STUDENT;", BazaPodatakaDataSource::mapResultToStudent);
	}

	@Override
	public void createStudent(Student student) throws DataSourceException {
		insertGeneric("INSERT INTO STUDENT(ime, prezime, jmbag, datum_rodjenja) VALUES (?, ?, ?, ?)", s -> {
			s.setString(1, student.getIme());
			s.setString(2, student.getPrezime());
			s.setString(3, student.getJmbag());
			s.setDate(4, Date.valueOf(student.getDatumRodjenja()));
		});
	}

	@Override
	public List<Student> readStudentiKojiImajuRodjendan() throws DataSourceException {
		var today = LocalDate.now();
		
		return selectGeneric(
			"SELECT * FROM STUDENT WHERE DAY(datum_rodjenja) = ? AND MONTH(datum_rodjenja) = ?;",
			ps -> {
				ps.setInt(1, today.getDayOfMonth());
				ps.setInt(2, today.getMonth().getValue());
			},
			BazaPodatakaDataSource::mapResultToStudent);
	}

	@Override
	public List<Student> readStudentiWhere(Student criteria) throws DataSourceException {
		String query = "SELECT * FROM STUDENT WHERE 1 = 1";
		var params = new ArrayList<>();
		
		if (criteria.getId() != null) {
			query += " AND id = ?";
			params.add(criteria.getId());
		}
		
		if (criteria.getIme() != null) {
			query += " AND ime LIKE ?";
			params.add(criteria.getIme());
		}
		
		if (criteria.getPrezime() != null) {
			query += " AND prezime LIKE ?";
			params.add(criteria.getPrezime());
		}
		
		if (criteria.getJmbag() != null) {
			query += " AND jmbag LIKE ?";
			params.add(criteria.getJmbag());
		}
		
		if (criteria.getDatumRodjenja() != null) {
			query += " AND datum_rodjenja = ?";
			params.add(criteria.getDatumRodjenja());
		}
		
		return selectGeneric(
			query,
			ps -> {
				for (int i = 0; i < params.size(); i++) {
					ps.setObject(i + 1, params.get(i));
				}
			},
			BazaPodatakaDataSource::mapResultToStudent);
	}

	@Override
	public List<Profesor> readProfesori() throws DataSourceException {
		return selectGeneric("SELECT * FROM PROFESOR;", BazaPodatakaDataSource::mapResultToProfesor);
	}

	@Override
	public void createProfesor(Profesor profesor) throws DataSourceException {
		insertGeneric("INSERT INTO PROFESOR(ime, prezime, sifra, titula) VALUES (?, ?, ?, ?)", s -> {
			s.setString(1, profesor.getIme());
			s.setString(2, profesor.getPrezime());
			s.setString(3, profesor.getSifra());
			s.setString(4, profesor.getTitula());
		});
	}

	@Override
	public List<Predmet> readPredmeti() throws DataSourceException {
		final var STUDENTI_IDS_FOR_PREDMET = "SELECT * FROM PREDMET_STUDENT WHERE predmet_id = ?;";

		var profesori = readProfesori();
		var studenti = readStudenti();

		return selectGeneric("SELECT * FROM PREDMET;", r -> {
			var predmetId = r.getLong("id");

			var studentiIds = selectGeneric(
				STUDENTI_IDS_FOR_PREDMET,
				statement -> statement.setLong(1, predmetId),
				r2 -> r2.getLong("student_id")
			);

			return new Predmet(
				predmetId,
				r.getString("sifra"),
				r.getString("naziv"),
				r.getInt("broj_ects_bodova"),
				getEntity(profesori, r.getLong("nositelj_id")),
				new HashSet<>(getEntities(studenti, studentiIds))
			);
		});
	}

	@Override
	public void createPredmet(Predmet predmet) throws DataSourceException {
		var predmetId = insertGeneric("INSERT INTO PREDMET(sifra, naziv, broj_ects_bodova, nositelj_id) VALUES (?, ?, ?, ?)", s -> {
			s.setString(1, predmet.getSifra());
			s.setString(2, predmet.getNaziv());
			s.setInt(3, predmet.getBrojEctsBodova());
			s.setLong(4, predmet.getNositelj().getId());
		});

		for (var student : predmet.getStudenti()) {
			insertGeneric("INSERT INTO PREDMET_STUDENT(predmet_id, student_id) VALUES (?, ?)", s -> {
				s.setLong(1, predmetId);
				s.setLong(2, student.getId());
			});
		}
	}

	@Override
	public List<Ispit> readIspiti() throws DataSourceException {
		var predmeti = readPredmeti();
		var studenti = readStudenti();

		return selectGeneric("SELECT * FROM ISPIT;", r -> new Ispit(
			r.getLong("id"),
			getEntity(predmeti, r.getLong("predmet_id")),
			getEntity(studenti, r.getLong("student_id")),
			Ocjena.parseOcjena(r.getInt("ocjena")),
			r.getTimestamp("datum_i_vrijeme").toLocalDateTime()
		));
	}

	@Override
	public void createIspit(Ispit ispit) throws DataSourceException {
		insertGeneric("INSERT INTO ISPIT(predmet_id, student_id, ocjena, datum_i_vrijeme) VALUES (?, ?, ?, ?)", s -> {
			s.setLong(1, ispit.getPredmet().getId());
			s.setLong(2, ispit.getStudent().getId());
			s.setInt(3, ispit.getOcjena().getNumerickaVrijednost());
			s.setTimestamp(4, Timestamp.valueOf(ispit.getDatumIVrijeme()));
		});
	}

	@Override
	public List<Ispit> readIspitiWhere(Ispit criteria) throws DataSourceException {
		String query = "SELECT * FROM ISPIT WHERE 1 = 1";
		var params = new ArrayList<>();

		if (criteria.getId() != null) {
			query += " AND id = ?";
			params.add(criteria.getId());
		}

		if (criteria.getPredmet() != null) {
			query += " AND predmet_id = ?";
			params.add(criteria.getPredmet().getId());
		}

		if (criteria.getStudent() != null) {
			query += " AND student_id = ?";
			params.add(criteria.getStudent().getId());
		}

		if (criteria.getOcjena() != null) {
			query += " AND ocjena = ?";
			params.add(criteria.getOcjena().getNumerickaVrijednost());
		}

		if (criteria.getDatumIVrijeme() != null) {
			query += " AND datum_i_vrijeme = ?";
			params.add(criteria.getDatumIVrijeme());
		}
		
		var predmeti = readPredmeti();
		var studenti = readStudenti();

		return selectGeneric(
			query,
			ps -> {
				for (int i = 0; i < params.size(); i++) {
					ps.setObject(i + 1, params.get(i));
				}
			},
			r -> new Ispit(
				r.getLong("id"),
				getEntity(predmeti, r.getLong("predmet_id")),
				getEntity(studenti, r.getLong("student_id")),
				Ocjena.parseOcjena(r.getInt("ocjena")),
				r.getTimestamp("datum_i_vrijeme").toLocalDateTime()
			)
		);
	}

	@Override
	public List<ObrazovnaUstanova> readObrazovneUstanove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void createObrazovnaUstanova(ObrazovnaUstanova ustanova) {
		throw new UnsupportedOperationException();
	}

	// result mapping
	
	private static Student mapResultToStudent(ResultSet r) throws SQLException {
		return new Student(
			r.getLong("id"),
			r.getString("ime"),
			r.getString("prezime"),
			r.getString("jmbag"),
			r.getDate("datum_rodjenja").toLocalDate()
		);
	}

	private static Profesor mapResultToProfesor(ResultSet r) throws SQLException {
		return new Profesor(
			r.getLong("id"),
			r.getString("sifra"),
			r.getString("ime"),
			r.getString("prezime"),
			r.getString("titula")
		);
	}
}

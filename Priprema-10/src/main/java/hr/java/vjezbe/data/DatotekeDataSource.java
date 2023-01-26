package hr.java.vjezbe.data;

/*
import hr.java.vjezbe.entitet.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public final class DatotekeDataSource implements DataSource {
	private static final Path PROFESORI_FILE = Path.of("dat/profesori.txt");
	private static final Path STUDENTI_FILE = Path.of("dat/studenti.txt");
	private static final Path PREDMETI_FILE = Path.of("dat/predmeti.txt");
	private static final Path ISPITI_FILE = Path.of("dat/ispiti.txt");
	private static final Path OBRAZOVNE_USTANOVE_FILE = Path.of("dat/obrazovneUstanove.txt");

	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("d.M.yyyy.");
	private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("d.M.yyyy. H:mm");

	private List<Profesor> loadProfesori() {
		var filename = PROFESORI_FILE;
		var profesori = new ArrayList<Profesor>();

		try (Scanner scanner = new Scanner(filename)) {
			while (scanner.hasNextLine()) {
				var fields = scanner.nextLine().split(";");
				
				long id = Long.parseLong(fields[0]);
				var sifra = fields[1];
				var ime = fields[2];
				var prezime = fields[3];
				var titula = fields[4];

				var profesor = new Profesor(id, sifra, ime, prezime, titula);
				profesori.add(profesor);
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException("File not found: " + filename);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return profesori;
	}
	
	private void writeProfesori(List<Profesor> profesori) {
		var out = profesori.stream()
			.map(p -> "%d;%s;%s;%s;%s".formatted(
				p.getId(),
				p.getSifra(),
				p.getIme(),
				p.getPrezime(),
				p.getTitula()
			)).collect(Collectors.joining("\n"));

		try {
			Files.writeString(PROFESORI_FILE, out);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private List<Student> loadStudenti() {
		var filename = STUDENTI_FILE;
		var studenti = new ArrayList<Student>();

		try (Scanner scanner = new Scanner(filename)) {
			while (scanner.hasNextLine()) {
				var fields = scanner.nextLine().split(";");
				
				long id = Long.parseLong(fields[0]);
				var ime = fields[1];
				var prezime = fields[2];
				var jmbag = fields[3];
				var datumRodjenja = LocalDate.parse(fields[4], DATE_FORMAT);

				var student = new Student(id, ime, prezime, jmbag, datumRodjenja);
				studenti.add(student);
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException("File not found: " + filename);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return studenti;
	}

	private void writeStudenti(List<Student> studenti) {
		var out = studenti.stream()
			.map(s -> "%d;%s;%s;%s;%s".formatted(
				s.getId(),
				s.getIme(),
				s.getPrezime(),
				s.getJmbag(),
				s.getDatumRodjenja().format(DATE_FORMAT)
			)).collect(Collectors.joining("\n"));

		try {
			Files.writeString(STUDENTI_FILE, out);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private List<Predmet> loadPredmeti(List<Profesor> profesori, List<Student> sviStudenti) {
		var filename = PREDMETI_FILE;
		var predmeti = new ArrayList<Predmet>();

		try (Scanner scanner = new Scanner(filename)) {
			while (scanner.hasNextLine()) {
				var fields = scanner.nextLine().split(";");
				
				long id = Long.parseLong(fields[0]);
				var sifra = fields[1];
				var naziv = fields[2];
				var brojEctsBodova = Integer.parseInt(fields[3]);
				var nositelj = getEntity(profesori, Long.parseLong(fields[4]));
				var studenti = getEntities(sviStudenti, parseIds(fields[5]));

				var predmet = new Predmet(id, sifra, naziv, brojEctsBodova, nositelj, new HashSet<>(studenti));
				predmeti.add(predmet);
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException("File not found: " + filename);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return predmeti;
	}

	private void writePredmeti(List<Predmet> predmeti) {
		var out = predmeti.stream()
			.map(p -> "%d;%s;%s;%d;%d;%s".formatted(
				p.getId(),
				p.getSifra(),
				p.getNaziv(),
				p.getBrojEctsBodova(),
				p.getNositelj().getId(),
				p.getStudenti().stream().map(student -> String.valueOf(student.getId())).collect(Collectors.joining(" "))
			)).collect(Collectors.joining("\n"));

		try {
			Files.writeString(PREDMETI_FILE, out);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private List<Ispit> loadIspiti(List<Student> studenti, List<Predmet> predmeti) {
		var filename = ISPITI_FILE;
		var ispiti = new ArrayList<Ispit>();

		try (Scanner scanner = new Scanner(filename)) {
			while (scanner.hasNextLine()) {
				var fields = scanner.nextLine().split(";");
				
				long id = Long.parseLong(fields[0]);
				var predmet = getEntity(predmeti, Long.parseLong(fields[1]));
				var student = getEntity(studenti, Long.parseLong(fields[2]));
				var ocjena = Ocjena.parseOcjena(fields[3]);
				var datumIVrijeme = LocalDateTime.parse(fields[4], DATE_TIME_FORMAT);

				var ispit = new Ispit(id, predmet, student, ocjena, datumIVrijeme);
				ispiti.add(ispit);
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException("File not found: " + filename);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return ispiti;
	}

	private void writeIspiti(List<Ispit> ispiti) {
		var out = ispiti.stream()
			.map(i -> "%d;%d;%d;%d;%s".formatted(
				i.getId(),
				i.getPredmet().getId(),
				i.getStudent().getId(),
				i.getOcjena().getNumerickaVrijednost(),
				i.getDatumIVrijeme().format(DATE_TIME_FORMAT)
			)).collect(Collectors.joining("\n"));

		try {
			Files.writeString(ISPITI_FILE, out);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private List<ObrazovnaUstanova> loadObrazovneUstanove(List<Profesor> sviProfesori, List<Predmet> sviPredmeti, List<Ispit> sviIspiti) {
		var filename = OBRAZOVNE_USTANOVE_FILE;
		var obrazovneUstanove = new ArrayList<ObrazovnaUstanova>();

		try (Scanner scanner = new Scanner(filename)) {
			while (scanner.hasNextLine()) {
				var fields = scanner.nextLine().split(";");
				
				long id = Long.parseLong(fields[0]);
				var naziv = fields[1];
				var vrstaObrazovneUstanove = Integer.parseInt(fields[2]);
				var predmeti = getEntities(sviPredmeti, parseIds(fields[3]));
				var profesori = getEntities(sviProfesori, parseIds(fields[4]));
				var ispiti = getEntities(sviIspiti, parseIds(fields[5]));

				var obrazovnaUstanova = switch (vrstaObrazovneUstanove) {
					case 1 -> new VeleucilisteJave(id, naziv, predmeti, profesori, ispiti);
					case 2 -> new FakultetRacunarstva(id, naziv, predmeti, profesori, ispiti);
					default -> {
						throw new RuntimeException("Nedozvoljena vrijednost za vrstu obrazovne ustanove: " + vrstaObrazovneUstanove);
					}
				};

				obrazovneUstanove.add(obrazovnaUstanova);
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException("File not found: " + filename);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return obrazovneUstanove;
	}

	private void writeObrazovneUstanove(List<ObrazovnaUstanova> ustanove) {
		var s = ustanove.stream()
			.map(u -> "%d;%s;%d;%s;%s,%s".formatted(
				u.getId(),
				u.getNaziv(),
				getVrstaObrazovneUstanove(u),
				u.getPredmeti().stream().map(predmet -> String.valueOf(predmet.getId())).collect(Collectors.joining(" ")),
				u.getProfesori().stream().map(profesor -> String.valueOf(profesor.getId())).collect(Collectors.joining(" ")),
				u.getIspiti().stream().map(ispit -> String.valueOf(ispit.getId())).collect(Collectors.joining(" "))
			)).collect(Collectors.joining("\n"));

		try {
			Files.writeString(OBRAZOVNE_USTANOVE_FILE, s);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private int getVrstaObrazovneUstanove(ObrazovnaUstanova ustanova) {
		if (ustanova instanceof  VeleucilisteJave) {
			return 1;
		} else if (ustanova instanceof FakultetRacunarstva) {
			return 2;
		} else {
			throw new RuntimeException("Nepoznata vrsta ustanove: " + ustanova.getClass());
		}
	}

	// interface implementation

	@Override
	public List<Profesor> readProfesori() {
		return loadProfesori();
	}

	@Override
	public void createProfesor(Profesor profesor) {
		var profesori = new ArrayList<>(readProfesori());
		profesor.setId(profesori.stream().mapToLong(Profesor::getId).max().orElse(0) + 1);
		profesori.add(profesor);
		
		writeProfesori(profesori);
	}

	@Override
	public List<Student> readStudenti() {
		return loadStudenti();
	}

	@Override
	public void createStudent(Student student) {
		var studenti = new ArrayList<>(readStudenti());
		student.setId(studenti.stream().mapToLong(Student::getId).max().orElse(0) + 1);
		studenti.add(student);

		writeStudenti(studenti);
	}

	@Override
	public List<Predmet> readPredmeti() {
		return loadPredmeti(loadProfesori(), loadStudenti());
	}

	@Override
	public void createPredmet(Predmet predmet) {
		var predmeti = new ArrayList<>(readPredmeti());
		predmet.setId(predmeti.stream().mapToLong(Predmet::getId).max().orElse(0) + 1);
		predmeti.add(predmet);

		writePredmeti(predmeti);
	}

	@Override
	public List<Ispit> readIspiti() {
		var studenti = loadStudenti();
		return loadIspiti(studenti, loadPredmeti(loadProfesori(), studenti));
	}

	@Override
	public void createIspit(Ispit ispit) {
		var ispiti = new ArrayList<>(readIspiti());
		ispit.setId(ispiti.stream().mapToLong(Ispit::getId).max().orElse(0) + 1);
		ispiti.add(ispit);

		writeIspiti(ispiti);
	}

	@Override
	public List<ObrazovnaUstanova> readObrazovneUstanove() {
		var profesori = loadProfesori();
		var studenti = loadStudenti();
		var predmeti = loadPredmeti(profesori, studenti);
		var ispiti = loadIspiti(studenti, predmeti);

		return loadObrazovneUstanove(profesori, predmeti, ispiti);
	}

	@Override
	public void createObrazovnaUstanova(ObrazovnaUstanova ustanova) {
		var ustanove = new ArrayList<>(readObrazovneUstanove());
		ustanova.setId(ustanove.stream().mapToLong(ObrazovnaUstanova::getId).max().orElse(0) + 1);
		ustanove.add(ustanova);

		writeObrazovneUstanove(ustanove);
	}

	// utility

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

	private static List<Long> parseIds(String ids) {
		return Arrays.stream(ids.split(" ")).map(Long::parseLong).collect(Collectors.toList());
	}
}
*/

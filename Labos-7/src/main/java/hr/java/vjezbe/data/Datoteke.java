package hr.java.vjezbe.data;

import hr.java.vjezbe.entitet.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public final class Datoteke {
	private static final String PROFESORI_FILE = "dat\\profesori.txt";
	private static final String STUDENTI_FILE = "dat\\studenti.txt";
	private static final String PREDMETI_FILE = "dat\\predmeti.txt";
	private static final String ISPITI_FILE = "dat\\ispiti.txt";
	private static final String OBRAZOVNE_USTANOVE_FILE = "dat\\obrazovneUstanove.txt";

	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("d.M.yyyy.");
	private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("d.M.yyyy. H:mm");

	static List<Profesor> loadProfesori() {
		String filename = PROFESORI_FILE;
		var profesori = new ArrayList<Profesor>();

		try (Scanner scanner = new Scanner(new File(filename))) {
			while (scanner.hasNextLine()) {
				long id = scanner.nextLong();
				scanner.nextLine();

				var sifra = scanner.nextLine();
				var ime = scanner.nextLine();
				var prezime = scanner.nextLine();
				var titula = scanner.nextLine();

				var profesor = new Profesor(id, sifra, ime, prezime, titula);
				profesori.add(profesor);
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(String.format("File not found: %s%n", filename));
		}

		return profesori;
	}

	static List<Student> loadStudenti() {
		String filename = STUDENTI_FILE;
		var studenti = new ArrayList<Student>();

		try (Scanner scanner = new Scanner(new File(filename))) {
			while (scanner.hasNextLine()) {
				long id = scanner.nextLong();
				scanner.nextLine();

				var ime = scanner.nextLine();
				var prezime = scanner.nextLine();
				var jmbag = scanner.nextLine();
				var datumRodjenja = LocalDate.parse(scanner.nextLine(), DATE_FORMAT);

				var student = new Student(id, ime, prezime, jmbag, datumRodjenja);
				studenti.add(student);
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(String.format("File not found: %s%n", filename));
		}

		return studenti;
	}

	static List<Predmet> loadPredmeti(List<Profesor> profesori, List<Student> sviStudenti) {
		String filename = PREDMETI_FILE;
		var predmeti = new ArrayList<Predmet>();

		try (Scanner scanner = new Scanner(new File(filename))) {
			while (scanner.hasNextLine()) {
				long id = scanner.nextLong();
				scanner.nextLine();

				var sifra = scanner.nextLine();
				var naziv = scanner.nextLine();

				var brojEctsBodova = scanner.nextInt();
				scanner.nextLine();

				var nositelj = getEntity(profesori, scanner.nextLong());
				scanner.nextLine();

				var studenti = getEntities(sviStudenti, parseIds(scanner.nextLine()));

				var predmet = new Predmet(id, sifra, naziv, brojEctsBodova, nositelj, new HashSet<>(studenti));
				predmeti.add(predmet);
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(String.format("File not found: %s%n", filename));
		}

		return predmeti;
	}

	static List<Ispit> loadIspiti(List<Student> studenti, List<Predmet> predmeti) {
		String filename = ISPITI_FILE;
		var ispiti = new ArrayList<Ispit>();

		try (Scanner scanner = new Scanner(new File(filename))) {
			while (scanner.hasNextLine()) {
				long id = scanner.nextLong();
				scanner.nextLine();

				var predmet = getEntity(predmeti, scanner.nextLong());
				scanner.nextLine();

				var student = getEntity(studenti, scanner.nextLong());
				scanner.nextLine();

				var ocjena = Ocjena.parseOcjena(scanner.nextInt());
				scanner.nextLine();

				var datumIVrijeme = LocalDateTime.parse(scanner.nextLine(), DATE_TIME_FORMAT);
				var zgrada = scanner.nextLine();
				var dvorana = scanner.nextLine();

				var ispit = new Ispit(id, predmet, student, ocjena, datumIVrijeme, new Dvorana(zgrada, dvorana));
				ispiti.add(ispit);
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(String.format("File not found: %s%n", filename));
		}

		return ispiti;
	}

	static List<ObrazovnaUstanova> loadObrazovneUstanove(List<Profesor> sviProfesori, List<Predmet> sviPredmeti, List<Ispit> sviIspiti) {
		String filename = OBRAZOVNE_USTANOVE_FILE;
		var obrazovneUstanove = new ArrayList<ObrazovnaUstanova>();

		try (Scanner scanner = new Scanner(new File(filename))) {
			while (scanner.hasNextLine()) {
				long id = scanner.nextLong();
				scanner.nextLine();

				var naziv = scanner.nextLine();
				var vrstaObrazovneUstanove = scanner.nextInt();
				scanner.nextLine();

				var predmeti = getEntities(sviPredmeti, parseIds(scanner.nextLine()));
				var profesori = getEntities(sviProfesori, parseIds(scanner.nextLine()));
				var ispiti = getEntities(sviIspiti, parseIds(scanner.nextLine()));

				var obrazovnaUstanova = switch (vrstaObrazovneUstanove) {
					case 1 -> new VeleucilisteJave(id, naziv, predmeti, profesori, ispiti);
					case 2 -> new FakultetRacunarstva(id, naziv, predmeti, profesori, ispiti);
					default -> {
						String message = String.format("Nedozvoljena vrijednost za obrazovnu ustanovu: %d", vrstaObrazovneUstanove);
						throw new RuntimeException(message);
					}
				};

				obrazovneUstanove.add(obrazovnaUstanova);
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(String.format("File not found: %s%n", filename));
		}

		return obrazovneUstanove;
	}

	// utility

	static <T extends Entitet> T getEntity(List<T> entities, long id) {
		return entities.stream()
			.filter(e -> e.getId() == id)
			.findFirst()
			.orElseThrow(() -> new RuntimeException(String.format("Entity with id %d wasn't found.", id)));
	}

	static <T extends Entitet> List<T> getEntities(List<T> entities, List<Long> ids) {
		var values = new ArrayList<T>();

		for (var id : ids) {
			entities.stream().filter(e -> e.getId() == id).findFirst().ifPresent(values::add);
		}

		return values;
	}

	static List<Long> parseIds(String ids) {
		return Arrays.stream(ids.split(" ")).map(Long::parseLong).collect(Collectors.toList());
	}
}

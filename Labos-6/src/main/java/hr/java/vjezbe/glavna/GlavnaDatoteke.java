package hr.java.vjezbe.glavna;

import hr.java.vjezbe.entitet.*;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class GlavnaDatoteke {
	private static final String PROFESORI_FILE = "dat\\profesori.txt";
	private static final String STUDENTI_FILE = "dat\\studenti.txt";
	private static final String PREDMETI_FILE = "dat\\predmeti.txt";
	private static final String ISPITI_FILE = "dat\\ispiti.txt";
	private static final String OBRAZOVNE_USTANOVE_FILE = "dat\\obrazovneUstanove.txt";
	
	private static final String SERIALIZED_OUTPUT_FILE = "dat\\obrazovne-ustanove.dat";

	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("d.M.yyyy.");
	private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("d.M.yyyy. H:mm");

	public static void main(String[] args) {
		var profesori = getProfesori(PROFESORI_FILE);
		var studenti = getStudenti(STUDENTI_FILE);
		var predmeti = getPredmeti(PREDMETI_FILE, profesori, studenti);
		var ispiti = getIspiti(ISPITI_FILE, studenti, predmeti);
		var obrazovneUstanove = getObrazovnaUstanove(OBRAZOVNE_USTANOVE_FILE, profesori, predmeti, ispiti);

		for (var obrazovnaUstanova : obrazovneUstanove.values()) {
			System.out.printf("Obrazovna ustanova '%s':%n", obrazovnaUstanova.getNaziv());

			for (var nositeljIPredmeti : dobijNositelje(predmeti.values()).entrySet()) {
				System.out.printf("Profesor %s %s predanje sljedeće predmete:%n", nositeljIPredmeti.getKey().getIme(), nositeljIPredmeti.getKey().getPrezime());

				for (var predmet : nositeljIPredmeti.getValue()) {
					System.out.printf("  %s%n", predmet.getNaziv());
				}
			}

			for (var predmet : obrazovnaUstanova.getPredmeti()) {
				System.out.printf("Studenti upisani na predmet '%s':%n", predmet.getNaziv());

				int i = 1;
				for (var student : predmet.getStudenti()) {
					System.out.printf("  %d. %s %s%n", i++, student.getIme(), student.getPrezime());
				}
			}

			var izvrsni = obrazovnaUstanova.ispitiSOcjenomIzvrstan();

			if (izvrsni.size() > 0) {
				System.out.println("Studenti koji su na ispitu ostvarili ocjenu 'izvrstan':");

				for (var ispit : izvrsni) {
					System.out.printf("  %s %s na predmetu '%s'%n", ispit.getStudent().getIme(), ispit.getStudent().getPrezime(), ispit.getPredmet().getNaziv());
				}
			} else {
				System.out.println("Nitko nije dobio ocjenu 5!");
			}

			for (var konacnaOcjena : dobijKonacneOcjeneStudenata(obrazovnaUstanova.getIspiti()).entrySet()) {
				System.out.printf("Konačna ocjena studija studenta %s %s je %.1f.%n", konacnaOcjena.getKey().getIme(), konacnaOcjena.getKey().getPrezime(), konacnaOcjena.getValue());
			}
			
			Student najboljiStudent = null;
			int godina = LocalDate.now().getYear();
			
			if (obrazovnaUstanova instanceof VeleucilisteJave veleucilisteJave) {
				najboljiStudent = veleucilisteJave.odrediNajuspjesnijegStudentaNaGodini(godina);
			}
			
			if (obrazovnaUstanova instanceof FakultetRacunarstva fakultetRacunarstva) {
				najboljiStudent = fakultetRacunarstva.odrediNajuspjesnijegStudentaNaGodini(godina);
			}
			
			if (najboljiStudent != null) {
				System.out.printf("Najbolji student %d. godine je %s %s, JMBAG: %s%n", godina, najboljiStudent.getIme(), najboljiStudent.getPrezime(), najboljiStudent.getJmbag());
			}

			System.out.println();
		}

		serializeToFile(SERIALIZED_OUTPUT_FILE, obrazovneUstanove);
	}

	// data loading

	static Map<Long, Profesor> getProfesori(String filename) {
		HashMap<Long, Profesor> profesori = new HashMap<>();

		try (Scanner scanner = new Scanner(new File(filename))) {
			while (scanner.hasNextLine()) {
				long id = scanner.nextLong();
				scanner.nextLine();

				var sifra = scanner.nextLine();
				var ime = scanner.nextLine();
				var prezime = scanner.nextLine();
				var titula = scanner.nextLine();

				var profesor = new Profesor(id, sifra, ime, prezime, titula);
				profesori.put(profesor.getId(), profesor);
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(String.format("File not found: %s%n", filename));
		}

		return profesori;
	}

	static Map<Long, Student> getStudenti(String filename) {
		HashMap<Long, Student> studenti = new HashMap<>();

		try (Scanner scanner = new Scanner(new File(filename))) {
			while (scanner.hasNextLine()) {
				long id = scanner.nextLong();
				scanner.nextLine();

				var ime = scanner.nextLine();
				var prezime = scanner.nextLine();
				var jmbag = scanner.nextLine();
				var datumRodjenja = LocalDate.parse(scanner.nextLine(), DATE_FORMAT);

				var student = new Student(id, ime, prezime, jmbag, datumRodjenja);
				studenti.put(student.getId(), student);
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(String.format("File not found: %s%n", filename));
		}

		return studenti;
	}

	static Map<Long, Predmet> getPredmeti(String filename, Map<Long, Profesor> profesori, Map<Long, Student> sviStudenti) {
		HashMap<Long, Predmet> predmeti = new HashMap<>();

		try (Scanner scanner = new Scanner(new File(filename))) {
			while (scanner.hasNextLine()) {
				long id = scanner.nextLong();
				scanner.nextLine();

				var sifra = scanner.nextLine();
				var naziv = scanner.nextLine();

				var brojEctsBodova = scanner.nextInt();
				scanner.nextLine();

				var nositelj = profesori.get(scanner.nextLong());
				scanner.nextLine();

				var studenti = mapGetMany(sviStudenti, parseIds(scanner.nextLine()));

				var predmet = new Predmet(id, sifra, naziv, brojEctsBodova, nositelj, new HashSet<>(studenti));
				predmeti.put(predmet.getId(), predmet);
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(String.format("File not found: %s%n", filename));
		}

		return predmeti;
	}

	static Map<Long, Ispit> getIspiti(String filename, Map<Long, Student> studenti, Map<Long, Predmet> predmeti) {
		HashMap<Long, Ispit> ispiti = new HashMap<>();

		try (Scanner scanner = new Scanner(new File(filename))) {
			while (scanner.hasNextLine()) {
				long id = scanner.nextLong();
				scanner.nextLine();

				var predmet = predmeti.get(scanner.nextLong());
				scanner.nextLine();

				var student = studenti.get(scanner.nextLong());
				scanner.nextLine();

				var ocjena = Ocjena.parseOcjena(scanner.nextInt());
				scanner.nextLine();

				var datumIVrijeme = LocalDateTime.parse(scanner.nextLine(), DATE_TIME_FORMAT);
				var zgrada = scanner.nextLine();
				var dvorana = scanner.nextLine();

				var ispit = new Ispit(id, predmet, student, ocjena, datumIVrijeme, new Dvorana(zgrada, dvorana));
				ispiti.put(ispit.getId(), ispit);
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(String.format("File not found: %s%n", filename));
		}

		return ispiti;
	}

	static Map<Long, ObrazovnaUstanova> getObrazovnaUstanove(String filename, Map<Long, Profesor> sviProfesori, Map<Long, Predmet> sviPredmeti, Map<Long, Ispit> sviIspiti) {
		HashMap<Long, ObrazovnaUstanova> obrazovneUstanove = new HashMap<>();

		try (Scanner scanner = new Scanner(new File(filename))) {
			while (scanner.hasNextLine()) {
				long id = scanner.nextLong();
				scanner.nextLine();

				var naziv = scanner.nextLine();
				var vrstaObrazovneUstanove = scanner.nextInt();
				scanner.nextLine();
				
				var predmeti = mapGetMany(sviPredmeti, parseIds(scanner.nextLine()));
				var profesori = mapGetMany(sviProfesori, parseIds(scanner.nextLine()));
				var ispiti = mapGetMany(sviIspiti, parseIds(scanner.nextLine()));

				var obrazovnaUstanova = switch (vrstaObrazovneUstanove) {
					case 1 -> new VeleucilisteJave(id, naziv, predmeti, profesori, ispiti);
					case 2 -> new FakultetRacunarstva(id, naziv, predmeti, profesori, ispiti);
					default -> {
						String message = String.format("Nedozvoljena vrijednost za obrazovnu ustanovu: %d", vrstaObrazovneUstanove);
						throw new RuntimeException(message);
					}
				};

				obrazovneUstanove.put(obrazovnaUstanova.getId(), obrazovnaUstanova);
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(String.format("File not found: %s%n", filename));
		}

		return obrazovneUstanove;

	}

	// data storing - serializing
	
	static void serializeToFile(String filename, Map<Long, ObrazovnaUstanova> obrazovneUstanove) {
		try (var out = new ObjectOutputStream(new FileOutputStream(filename))) {
			try {
				out.writeObject(obrazovneUstanove);
			} catch (NotSerializableException e) {
				System.out.println(e.getMessage());
				throw new RuntimeException(e.getMessage(), e);
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(String.format("File not found: %s%n", filename));
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	// specific utility

	static Map<Profesor, List<Predmet>> dobijNositelje(Collection<Predmet> predmeti) {
		var nositelji = new HashMap<Profesor, List<Predmet>>();

		for (var predmet : predmeti) {
			var nositelj = predmet.getNositelj();

			if (!nositelji.containsKey(nositelj)) {
				nositelji.put(nositelj, new ArrayList<>());
			}

			nositelji.get(nositelj).add(predmet);
		}

		return nositelji;
	}

	static Map<Student, BigDecimal> dobijKonacneOcjeneStudenata(List<Ispit> ispiti) {
		var ispitiStudenata = new HashMap<Student, List<Ispit>>();

		for (var ispit : ispiti) {
			var student = ispit.getStudent();

			if (!ispitiStudenata.containsKey(student)) {
				ispitiStudenata.put(student, new ArrayList<>());
			}

			ispitiStudenata.get(student).add(ispit);
		}

		return ispitiStudenata.entrySet().stream().map(entry -> {
			BigDecimal sum = BigDecimal.ZERO;
			int count = 0;
			boolean imaNegativna = false;

			for (var ispit : entry.getValue()) {
				sum = sum.add(new BigDecimal(ispit.getOcjena().getNumerickaVrijednost()));
				count++;
			}

			return new AbstractMap.SimpleEntry<>(entry.getKey(), imaNegativna ? BigDecimal.ONE : sum.divide(new BigDecimal(count)));
		}).collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
	}

	// generic utility

	static <TKey, TValue> List<TValue> mapGetMany(Map<TKey, TValue> map, List<TKey> keys) {
		var values = new ArrayList<TValue>();

		for (var key : keys) {
			values.add(map.get(key));
		}

		return values;
	}

	static <T> List<Long> parseIds(String ids) {
		return Arrays.stream(ids.split(" ")).map(Long::parseLong).collect(Collectors.toList());
	}
}

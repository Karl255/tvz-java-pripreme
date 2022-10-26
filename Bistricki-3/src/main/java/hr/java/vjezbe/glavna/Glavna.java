package hr.java.vjezbe.glavna;

import hr.java.vjezbe.entitet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Glavna {
	// TODO: read these at runtime
	private static final int BROJ_OBRAZOVNIH_USTANOVA = 2;
	private static final int BROJ_STUDENATA = 2;
	private static final int BROJ_PROFESORA = 2;
	private static final int BROJ_PREDMETA = 2;
	private static final int BROJ_ISPITA = 2;

	private static final Logger logger = LoggerFactory.getLogger(Glavna.class);

	private static final Predicate<Integer> jeLiOcjena = x -> x >= 1 && x <= 5;
	private static final Function<Integer, Predicate<Integer>> jeLiIspravanIndeksOd1 = count -> (i -> i >= 1 && i <= count);

	public static void main(String[] args) {
		var obrazovneUstanove = new ObrazovnaUstanova[BROJ_OBRAZOVNIH_USTANOVA];

		var scanner = new Scanner(System.in);

		for (int i = 0; i < obrazovneUstanove.length; i++) {
			try {
				obrazovneUstanove[i] = nextObrazovnaUstanova(scanner);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				System.exit(1);
			}
		}

		for (var ustanova : obrazovneUstanove) {
			System.out.printf("%nUstanova '%s'%n", ustanova.getNaziv());

			if (ustanova instanceof Visokoskolska visokoskolska) {
				for (var s : ustanova.dobijSveStudenteKojiProlaze()) {
					
					
					System.out.printf("Ocjene završnog rada koje je ostvario student %s %s:%n", s.getIme(), s.getPrezime());

					System.out.print("  ocjena završnog rada (1-5): ");
					int ocjenaZavrsnogRada = SafeScanning.ensureNextInt(scanner, jeLiOcjena);

					System.out.print("  ocjena obrane završnog rada (1-5): ");
					int ocjenaObraneZavrsnogRada = SafeScanning.ensureNextInt(scanner, jeLiOcjena);

					var konacnaOcjena = visokoskolska.izracunajKonacnuOcjenuStudijaZaStudenta(visokoskolska.filtrirajPoStudentu(ustanova.getIspiti(), s), ocjenaZavrsnogRada, ocjenaObraneZavrsnogRada);
					System.out.printf("Konačna ocjena studenta %s %s je %.2f%n", s.getIme(), s.getPrezime(), konacnaOcjena);
				}

				var najboljiStudent = ustanova.odrediNajuspjesnijegStudentaNaGodini(2022);
				System.out.printf("Najuspješniji student u 2022. godini je %s %s%n", najboljiStudent.getIme(), najboljiStudent.getPrezime());

				if (ustanova instanceof Diplomski diplomski) {
					Student s = diplomski.odrediStudentaZaRektorovuNagradu();
					System.out.printf("Rektorovu nagradu osvaja student %s %s%n", s.getIme(), s.getPrezime());
				}
			}
		}

		scanner.close();
	}

	static ObrazovnaUstanova nextObrazovnaUstanova(Scanner scanner) {
		var studenti = new Student[BROJ_STUDENATA];
		var profesori = new Profesor[BROJ_PROFESORA];
		var predmeti = new Predmet[BROJ_PREDMETA];
		var ispiti = new Ispit[BROJ_ISPITA];

		System.out.printf("%nOdaberite ustanovu:%n  1. Veleučilište Jave%n  2. Fakultet Računarstva%n");
		int odabranaObrazovnaUstanova = SafeScanning.ensureNextInt(scanner, jeLiIspravanIndeksOd1.apply(2));
		System.out.print("Naziv ustanove: ");
		var naziv = scanner.nextLine();

		for (int i = 0; i < studenti.length; i++) {
			System.out.printf("Unesite podatke za %d. studenta:%n", i + 1);
			var student = nextStudent(scanner);
			studenti[i] = student;
		}

		for (int i = 0; i < profesori.length; i++) {
			System.out.printf("Unesite podatke za %d. profesora:%n", i + 1);
			var profesor = nextProfesor(scanner);
			profesori[i] = profesor;
		}

		for (int i = 0; i < predmeti.length; i++) {
			System.out.printf("Unesite podatke za %d. predmet:%n", i + 1);
			var predmet = nextPredmet(scanner, profesori, studenti);
			predmeti[i] = predmet;
		}

		for (int i = 0; i < ispiti.length; i++) {
			System.out.printf("Unesite podatke za %d. ispit:%n", i + 1);
			var ispit = nextIspit(scanner, predmeti);
			ispiti[i] = ispit;
		}

		System.out.println("Studenti koji su ostvarili ocjenu 5:");
		int n = 0;
		for (Ispit ispit : ispiti) {
			if (ispit.getOcjena() == 5) {
				System.out.printf("%s %s, %s%n", ispit.getStudent().getIme(), ispit.getStudent().getPrezime(), ispit.getPredmet().getNaziv());
				n++;
			}
		}

		if (n == 0) {
			System.out.println("Nitko nije zaslužio ocjenu 5!");
		}

		logger.info(String.format("Odabrana ustanova: %s%n", odabranaObrazovnaUstanova));
		
		return switch (odabranaObrazovnaUstanova) {
			case 1 -> new VeleucilisteJave(naziv, predmeti, profesori, ispiti);
			case 2 -> new FakultetRacunarstva(naziv, predmeti, profesori, ispiti);
			default -> {
				String message = "Kritična greška: dosegnut nedostižan dio koda!";
				logger.error(message);
				throw new RuntimeException(message);
			}
		};
	}

	static Profesor nextProfesor(Scanner scanner) {
		var builder = new Profesor.ProfesorBuilder();

		System.out.print("  šifra: ");
		builder.setSifra(scanner.nextLine());

		System.out.print("  ime: ");
		builder.setIme(scanner.nextLine());

		System.out.print("  prezime: ");
		builder.setPrezime(scanner.nextLine());

		System.out.print("  titula: ");
		builder.setTitula(scanner.nextLine());

		return builder.build();
	}

	static Student nextStudent(Scanner scanner) {
		System.out.print("  ime: ");
		var ime = scanner.nextLine();

		System.out.print("  prezime: ");
		var prezime = scanner.nextLine();

		System.out.print("  JMBAG: ");
		var jmbag = scanner.nextLine();

		System.out.print("  datum rođenja (d.M.yyyy.): ");
		var datumRodjenja = SafeScanning.ensureNextDate(scanner, DateTimeFormatter.ofPattern(("d.M.yyyy.")));

		return new Student(ime, prezime, jmbag, datumRodjenja);
	}

	static Predmet nextPredmet(Scanner scanner, Profesor[] profesori, Student[] sviStudenti) {
		System.out.print("  šifra: ");
		var sifra = scanner.nextLine();

		System.out.print("  naziv: ");
		var naziv = scanner.nextLine();

		System.out.print("  broj ECTS bodova: ");
		int brojEctsBodova = SafeScanning.ensureNextInt(scanner, x -> x > 0);

		var nositelj = odaberiProfesora(scanner, profesori);
		var upisaniStudenti = odaberiUpisaneStudente(scanner, sviStudenti);

		return new Predmet(sifra, naziv, brojEctsBodova, nositelj, upisaniStudenti);
	}

	static Ispit nextIspit(Scanner scanner, Predmet[] predmeti) {
		var predmet = odaberiPredmet(scanner, predmeti);
		var student = odaberiStudenta(scanner, predmet.getStudenti());

		System.out.print("  ocjena (1-5): ");
		int ocjena = SafeScanning.ensureNextInt(scanner, jeLiOcjena);

		System.out.print("  datum i vrijeme pisanja (d.M.yyyy. HH:mm): ");
		var datumIVrijeme = SafeScanning.ensureNextDateTime(scanner, DateTimeFormatter.ofPattern("d.M.yyyy. HH:mm"));

		System.out.print("  zgrada pisanja: ");
		var zgrada = scanner.nextLine();

		System.out.print("  naziv dvorane pisanja: ");
		var dvorana = scanner.nextLine();

		return new Ispit(predmet, student, ocjena, datumIVrijeme, new Dvorana(dvorana, zgrada));
	}

	static Profesor odaberiProfesora(Scanner scanner, Profesor[] profesori) {
		for (int i = 0; i < profesori.length; i++) {
			System.out.printf("  %d. %s %s%n", i + 1, profesori[i].getIme(), profesori[i].getPrezime());
		}

		System.out.print("  izaberite profesora: ");
		int index = SafeScanning.ensureNextInt(scanner, jeLiIspravanIndeksOd1.apply(profesori.length)) - 1;

		return profesori[index];
	}

	static Predmet odaberiPredmet(Scanner scanner, Predmet[] predmeti) {
		for (int i = 0; i < predmeti.length; i++) {
			System.out.printf("  %d. %s%n", i + 1, predmeti[i].getNaziv());
		}

		System.out.print("  izaberite predmet: ");
		int index = SafeScanning.ensureNextInt(scanner, jeLiIspravanIndeksOd1.apply(predmeti.length)) - 1;

		return predmeti[index];
	}

	static Student odaberiStudenta(Scanner scanner, Student[] studenti) {
		for (int i = 0; i < studenti.length; i++) {
			System.out.printf("  %d. %s %s%n", i + 1, studenti[i].getIme(), studenti[i].getPrezime());
		}

		System.out.print("  izaberite studenta: ");
		int index = SafeScanning.ensureNextInt(scanner, jeLiIspravanIndeksOd1.apply(studenti.length)) - 1;

		return studenti[index];
	}

	static Student[] odaberiUpisaneStudente(Scanner scanner, Student[] sviStudenti) {
		for (int i = 0; i < sviStudenti.length; i++) {
			System.out.printf("  %d. %s %s%n", i + 1, sviStudenti[i].getIme(), sviStudenti[i].getPrezime());
		}

		System.out.print("  izaberite studente (brojevi razdvojeni razmakom, bez točke): ");
		
		var input = SafeScanning.ensureNextString(scanner, s ->
			Pattern.matches("(\\d+ )*\\d+", s)
			&& Arrays.stream(s.split(" "))
				.mapToInt(Integer::parseInt)
				.allMatch(n -> n >= 1 && n <= sviStudenti.length)
		);
		
		var indecies = Arrays.stream(input.split(" ")).mapToInt(s -> Integer.parseInt(s) - 1).toArray();
		var upisaniStudenti = new Student[indecies.length];
		int i = 0;

		for (int j = 0; j < indecies.length; j++) {
			upisaniStudenti[i++] = sviStudenti[j];
		}

		return upisaniStudenti;
	}
}

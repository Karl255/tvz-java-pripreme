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

/**
 * Glavna klasa s metodom main i pomoćnim metodama.
 */
public class Glavna {
	private static final int MIN_OBRAZOVNIH_USTANOVA = 2;
	private static final int MIN_STUDENATA = 2;
	private static final int MIN_PROFESORA = 2;
	private static final int MIN_PREDMETA = 2;
	private static final int MIN_ISPITA = 2;

	private static final Logger logger = LoggerFactory.getLogger(Glavna.class);

	private static final Predicate<Integer> jeLiOcjena = x -> x >= 1 && x <= 5;
	private static final Function<Integer, Predicate<Integer>> jeLiIspravanIndeksOd1 = count -> (i -> i >= 1 && i <= count);

	/**
	 * Početak programa.
	 * @param args Argumenti iz komandne linije.
	 */
	public static void main(String[] args) {
		var scanner = new Scanner(System.in);
		System.out.print("Koliko obrazovnih ustanova želite unijeti? (najmanje 2) ");
		int brojObrazovnihUstanova = SafeScanning.ensureNextInt(scanner, x -> x >= MIN_OBRAZOVNIH_USTANOVA);
		var obrazovneUstanove = new ObrazovnaUstanova[brojObrazovnihUstanova];

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

	/**
	 * Učitava novu obrazovnu ustanovu preko danog scannera.
	 * @param scanner Scanner s kojim se čitaju podatci.
	 * @return Novi objekt s podacima učitanih koristeći dani scanner.
	 */
	static ObrazovnaUstanova nextObrazovnaUstanova(Scanner scanner) {
		System.out.printf("%nOdaberite ustanovu:%n  1. Veleučilište Jave%n  2. Fakultet Računarstva%n");
		int odabranaObrazovnaUstanova = SafeScanning.ensureNextInt(scanner, jeLiIspravanIndeksOd1.apply(2));
		System.out.print("Naziv ustanove: ");
		var naziv = scanner.nextLine();

		System.out.print("Koliko studenata želite unijeti? (najmanje 2) ");
		int brojStudenata = SafeScanning.ensureNextInt(scanner, x -> x >= MIN_STUDENATA);
		var studenti = new Student[brojStudenata];

		for (int i = 0; i < studenti.length; i++) {
			System.out.printf("Unesite podatke za %d. studenta:%n", i + 1);
			var student = nextStudent(scanner);
			studenti[i] = student;
		}

		System.out.print("Koliko profesora želite unijeti? (najmanje 2) ");
		int brojProfesora = SafeScanning.ensureNextInt(scanner, x -> x >= MIN_PROFESORA);
		var profesori = new Profesor[brojProfesora];

		for (int i = 0; i < profesori.length; i++) {
			System.out.printf("Unesite podatke za %d. profesora:%n", i + 1);
			var profesor = nextProfesor(scanner);
			profesori[i] = profesor;
		}

		System.out.print("Koliko predmeta želite unijeti? (najmanje 2) ");
		int brojPredmeta = SafeScanning.ensureNextInt(scanner, x -> x >= MIN_PREDMETA);
		var predmeti = new Predmet[brojPredmeta];

		for (int i = 0; i < predmeti.length; i++) {
			System.out.printf("Unesite podatke za %d. predmet:%n", i + 1);
			var predmet = nextPredmet(scanner, profesori, studenti);
			predmeti[i] = predmet;
		}

		System.out.print("Koliko ispita želite unijeti? (najmanje 2) ");
		int brojIspita = SafeScanning.ensureNextInt(scanner, x -> x >= MIN_ISPITA);
		var ispiti = new Ispit[brojIspita];

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

	/**
	 * Učitava novog profesora preko danog scannera.
	 * @param scanner Scanner s kojim se čitaju podatci.
	 * @return Novi objekt s podacima učitanih koristeći dani scanner.
	 */
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

	/**
	 * Učitava novog studenta preko danog scannera.
	 * @param scanner Scanner s kojim se čitaju podatci.
	 * @return Novi objekt s podacima učitanih koristeći dani scanner.
	 */
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

	/**
	 * Učitava novi predmet preko danog scannera te danih profesora i studenata.
	 * @param scanner Scanner s kojim se čitaju podatci.
	 * @param profesori Popis dostupnih profesora za nositelja predmeta.
	 * @param sviStudenti Popis dostupnih studenata koji mogu upisati predmet. 
	 * @return Novi objekt s podacima učitanih koristeći dani scanner.
	 */
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

	/**
	 * Učitava novi ispit preko danog scannera te danih predmeta i njihovih studenata.
	 * @param scanner Scanner s kojim se čitaju podatci.
	 * @param predmeti Popis predmeta koji se polažu.
	 * @return Novi objekt s podacima učitanih koristeći dani scanner.
	 */
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

	/**
	 * Pita korisnika za odabir jednog od danih profesora.
	 * @param scanner Scanner s kojim se čita korisnikov odabir.
	 * @param profesori Popis profesora koji se mogu odabrati.
	 * @return Odabran profesor.
	 */
	static Profesor odaberiProfesora(Scanner scanner, Profesor[] profesori) {
		for (int i = 0; i < profesori.length; i++) {
			System.out.printf("  %d. %s %s%n", i + 1, profesori[i].getIme(), profesori[i].getPrezime());
		}

		System.out.print("  izaberite profesora: ");
		int index = SafeScanning.ensureNextInt(scanner, jeLiIspravanIndeksOd1.apply(profesori.length)) - 1;

		return profesori[index];
	}

	/**
	 * Pita korisnika za odabir jednog od danih predmeta.
	 * @param scanner Scanner s kojim se čita korisnikov odabir.
	 * @param predmeti Popis predmeta koji se mogu odabrati.
	 * @return Odabran predmet.
	 */
	static Predmet odaberiPredmet(Scanner scanner, Predmet[] predmeti) {
		for (int i = 0; i < predmeti.length; i++) {
			System.out.printf("  %d. %s%n", i + 1, predmeti[i].getNaziv());
		}

		System.out.print("  izaberite predmet: ");
		int index = SafeScanning.ensureNextInt(scanner, jeLiIspravanIndeksOd1.apply(predmeti.length)) - 1;

		return predmeti[index];
	}

	/**
	 * Pita korisnika za odabir jednog od danih studenata.
	 * @param scanner Scanner s kojim se čita korisnikov odabir.
	 * @param studenti Popis studenata koji se mogu odabrati.
	 * @return Odabran student.
	 */
	static Student odaberiStudenta(Scanner scanner, Student[] studenti) {
		for (int i = 0; i < studenti.length; i++) {
			System.out.printf("  %d. %s %s%n", i + 1, studenti[i].getIme(), studenti[i].getPrezime());
		}

		System.out.print("  izaberite studenta: ");
		int index = SafeScanning.ensureNextInt(scanner, jeLiIspravanIndeksOd1.apply(studenti.length)) - 1;

		return studenti[index];
	}

	/**
	 * Pita korisnika za odabir barem jednog studenata.
	 * @param scanner Scanner s kojim se čita korisnikov odabir.
	 * @param sviStudenti Popis studenata koji se mogu odabrati.
	 * @return Odabrani studenti.
	 */
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

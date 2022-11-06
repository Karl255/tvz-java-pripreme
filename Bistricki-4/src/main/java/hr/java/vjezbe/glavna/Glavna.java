package hr.java.vjezbe.glavna;

import hr.java.vjezbe.entitet.*;
import hr.java.vjezbe.sortiranje.StudentSorter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.util.*;
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
	//private static final int MIN_PREDMETA = 3;
	private static final int MIN_ISPITA = 2;

	private static final Logger logger = LoggerFactory.getLogger(Glavna.class);

	private static final Function<Integer, Predicate<Integer>> jeLiIspravanIndeksOd1 = count -> (i -> i >= 1 && i <= count);

	/**
	 * Početak programa. Ako je dan jedan ili više argumenata prvi argument će se tretirati kao naziv datoteke čiji sadržaj će zmijeniti korisnikov unos. 
	 * @param args Argumenti iz komandne linije.
	 */
	public static void main(String[] args) {
		var scanner = args.length > 0
			? new Scanner(new UserInputSimulator(args[0]))
			: new Scanner(System.in);
		
		System.out.printf("Koliko obrazovnih ustanova želite unijeti? (najmanje %d) ", MIN_OBRAZOVNIH_USTANOVA);
		int brojObrazovnihUstanova = SafeScanning.ensureNextInt(scanner, x -> x >= MIN_OBRAZOVNIH_USTANOVA);
		List<ObrazovnaUstanova> obrazovneUstanove = new ArrayList<>();

		for (int i = 0; i < brojObrazovnihUstanova; i++) {
			try {
				obrazovneUstanove.add(nextObrazovnaUstanova(scanner));
			} catch (Exception e) {
				System.out.println(e.getMessage());
				System.exit(1);
			}
		}

		for (var ustanova : obrazovneUstanove) {
			System.out.printf("%nUstanova '%s'%n", ustanova.getNaziv());

			for (var p : ustanova.getPredmeti()) {
				ispisiStudenteNaPredmetu(p);
			}
			
			ispisiIspiteSOcjenomIzvrstan(ustanova);

			if (ustanova instanceof VeleucilisteJave veleucilisteJave) {
				odrediKonacneOcjeneStudenata(scanner, veleucilisteJave);
			}
			
			if (ustanova instanceof FakultetRacunarstva fakultetRacunarstva) {
				odrediKonacneOcjeneStudenata(scanner, fakultetRacunarstva);
			}

			var najboljiStudent = ustanova.odrediNajuspjesnijegStudentaNaGodini(2022);
			System.out.printf("Najuspješniji student u 2022. godini je %s %s%n", najboljiStudent.getIme(), najboljiStudent.getPrezime());
			
			if (ustanova instanceof Diplomski diplomski) {
				Student s = diplomski.odrediStudentaZaRektorovuNagradu();
				System.out.printf("Rektorovu nagradu osvaja student %s %s%n", s.getIme(), s.getPrezime());
			}
		}

		scanner.close();
	}

	/**
	 * Ispisuje ispite koji su ocjenjeni s ocjenom 5.
	 * @param ustanova Ustanova na kojoj su pisani ispiti.
	 */
	private static void ispisiIspiteSOcjenomIzvrstan(ObrazovnaUstanova ustanova) {
		System.out.println("Ispiti s ocjenom 5:");
		
		var ispiti = ustanova.ispitiSOcjenomIzvrstan();

		if (ispiti.size() == 0) {
			System.out.println("Nitko nije dobio ocjenu 5!");
		} else {
			for (var ispit : ispiti) {
				System.out.printf("%s %s, %s%n", ispit.getStudent().getIme(), ispit.getStudent().getPrezime(), ispit.getPredmet().getNaziv());
			}
		}
	}

	/**
	 * Od korisnika zatraži upis ocjene završnog rada te izračuna konačnu ocjenu.
	 * @param scanner Scanner s kojim se čitaju podatci.
	 * @param ustanova Visokoskolska obrazovna ustanova na kojoj studiraju studenti.
	 */
	private static <T extends ObrazovnaUstanova & Visokoskolska> void odrediKonacneOcjeneStudenata(Scanner scanner, T ustanova) {
		for (var s : ustanova.dobijSveStudenteKojiProlaze()) {
			System.out.printf("Ocjene završnog rada koje je ostvario student %s %s:%n", s.getIme(), s.getPrezime());

			System.out.print("  ocjena završnog rada (1-5): ");
			var ocjenaZavrsnogRada = SafeScanning.ensureNextOcjena(scanner);

			System.out.print("  ocjena obrane završnog rada (1-5): ");
			var ocjenaObraneZavrsnogRada = SafeScanning.ensureNextOcjena(scanner);

			var konacnaOcjena = ustanova.izracunajKonacnuOcjenuStudijaZaStudenta(ustanova.filtrirajPoStudentu(ustanova.getIspiti(), s), ocjenaZavrsnogRada, ocjenaObraneZavrsnogRada);
			System.out.printf("Konačna ocjena studenta %s %s je %.2f%n", s.getIme(), s.getPrezime(), konacnaOcjena);
		}
	}
	
	private static void ispisiStudenteNaPredmetu(Predmet p) {
		System.out.printf("Studenti upisani na predmet %s (%s):%n", p.getNaziv(), p.getSifra());

		for (var s : p.getStudenti() .stream().sorted(new StudentSorter()).toList()) {
			System.out.printf("%s %s %s%n", s.getJmbag(), s.getPrezime(), s.getIme());
		}
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

		System.out.printf("Koliko studenata želite unijeti? (najmanje %d) ", MIN_STUDENATA);
		int brojStudenata = SafeScanning.ensureNextInt(scanner, x -> x >= MIN_STUDENATA);
		List<Student> studenti = new ArrayList<>();

		for (int i = 0; i < brojStudenata; i++) {
			System.out.printf("Unesite podatke za %d. studenta:%n", i + 1);
			studenti.add(nextStudent(scanner));
		}

		System.out.printf("Koliko profesora želite unijeti? (najmanje %d) ", MIN_PROFESORA);
		int brojProfesora = SafeScanning.ensureNextInt(scanner, x -> x >= MIN_PROFESORA);
		List<Profesor> profesori = new ArrayList<>();

		for (int i = 0; i < brojProfesora; i++) {
			System.out.printf("Unesite podatke za %d. profesora:%n", i + 1);
			profesori.add(nextProfesor(scanner));
		}

		System.out.printf("Koliko predmeta želite unijeti? (najmanje %d) ", brojProfesora + 1);
		int brojPredmeta = SafeScanning.ensureNextInt(scanner, x -> x >= brojProfesora + 1);
		var predmeti = nextPredmeti(scanner, brojPredmeta, studenti, profesori);

		System.out.printf("Koliko ispita želite unijeti? (najmanje %d) ", MIN_ISPITA);
		int brojIspita = SafeScanning.ensureNextInt(scanner, x -> x >= MIN_ISPITA);
		List<Ispit> ispiti = new ArrayList<>();

		for (int i = 0; i < brojIspita; i++) {
			System.out.printf("Unesite podatke za %d. ispit:%n", i + 1);
			ispiti.add(nextIspit(scanner, predmeti));
		}

		logger.info(String.format("Odabrana ustanova: %s%n", odabranaObrazovnaUstanova));

		return switch (odabranaObrazovnaUstanova) {
			case 1 -> new VeleucilisteJave(naziv, predmeti, profesori, ispiti);
			case 2 -> new FakultetRacunarstva(naziv, predmeti, profesori, ispiti);
			default -> {
				String message = "Kritična greška: dosegnut nedostižan dio koda pri odabiru obrazovne ustanove!";
				logger.error(message);
				throw new RuntimeException(message);
			}
		};
	}

	private static void ispisiPredmeteNositelja(Map<Profesor, List<Predmet>> nositelji) {
		nositelji.forEach((nositelj, predmeti) -> {
			System.out.printf("%s %s je nositelj predmeta:%n", nositelj.getIme(), nositelj.getPrezime());
			for (var p : predmeti) {
				System.out.printf("  %s %s%n", p.getSifra(), p.getNaziv());
			}
		});
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
	 * Učitava sve predmete za obrazovnu ustanovu. Osigurava da su zadovoljeni kriteriji: svaki profesor mora biti nositelj barem jednog predmeta te barem jedan profesor more biti nostelj više od jednog predmeta.
	 * @param scanner Scanner s kojim se čitaju podatci.
	 * @param brojPredmeta Broj predmeta koji se mora upisati.
	 * @param studenti Popis dostupnih studenata koji mogu upisati predmet. 
	 * @param profesori Popis profesora dostupni za nositelja predmeta.
	 * @return Popis predmeta s podacima učinati koristeći dani scanner.
	 */
	private static List<Predmet> nextPredmeti(Scanner scanner, int brojPredmeta, List<Student> studenti, List<Profesor> profesori) {
		while (true) {
			List<Predmet> predmeti = new ArrayList<>();
			Map<Profesor, List<Predmet>> nositelji = new HashMap<>();

			for (int i = 0; i < brojPredmeta; i++) {
				System.out.printf("Unesite podatke za %d. predmet:%n", i + 1);
				var predmet = nextPredmet(scanner, profesori, studenti);
				predmeti.add(predmet);
				var nositelj = predmet.getNositelj();

				if (nositelji.containsKey(nositelj)) {
					nositelji.get(nositelj).add(predmet);
				} else {
					nositelji.put(nositelj, new ArrayList<>() {{ add(predmet); }});
				}
			}
			
			if (profesori.stream().allMatch(nositelji::containsKey) && profesori.stream().anyMatch(p -> {
				var l = nositelji.get(p);
				return l != null && l.size() > 1;
			})) {
				ispisiPredmeteNositelja(nositelji);
				return predmeti;
			} else {
				System.out.println("Greška pri unosu! Svaki profesor mora biti nositelj barem jednog predmeta i barem jedan mora biti nositelj više od jednog predmeta!");
			}
		}
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
	 * @param profesori Popis profesora dostupni za nositelja predmeta.
	 * @param sviStudenti Popis dostupnih studenata koji mogu upisati predmet. 
	 * @return Novi objekt s podacima učitanih koristeći dani scanner.
	 */
	static Predmet nextPredmet(Scanner scanner, List<Profesor> profesori, List<Student> sviStudenti) {
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
	static Ispit nextIspit(Scanner scanner, List<Predmet> predmeti) {
		var predmet = odaberiPredmet(scanner, predmeti);
		var student = odaberiStudenta(scanner, predmet.getStudenti());

		System.out.print("  ocjena (1-5): ");
		Ocjena ocjena = SafeScanning.ensureNextOcjena(scanner);

		System.out.print("  datum i vrijeme pisanja (d.M.yyyy. H:mm): ");
		var datumIVrijeme = SafeScanning.ensureNextDateTime(scanner, DateTimeFormatter.ofPattern("d.M.yyyy. H:mm"));

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
	static Profesor odaberiProfesora(Scanner scanner, List<Profesor> profesori) {
		for (int i = 0; i < profesori.size(); i++) {
			System.out.printf("  %d. %s %s%n", i + 1, profesori.get(i).getIme(), profesori.get(i).getPrezime());
		}

		System.out.print("  izaberite profesora: ");
		int index = SafeScanning.ensureNextInt(scanner, jeLiIspravanIndeksOd1.apply(profesori.size())) - 1;

		return profesori.get(index);
	}

	/**
	 * Pita korisnika za odabir jednog od danih predmeta.
	 * @param scanner Scanner s kojim se čita korisnikov odabir.
	 * @param predmeti Popis predmeta koji se mogu odabrati.
	 * @return Odabran predmet.
	 */
	static Predmet odaberiPredmet(Scanner scanner, List<Predmet> predmeti) {
		for (int i = 0; i < predmeti.size(); i++) {
			System.out.printf("  %d. %s%n", i + 1, predmeti.get(i).getNaziv());
		}

		System.out.print("  izaberite predmet: ");
		int index = SafeScanning.ensureNextInt(scanner, jeLiIspravanIndeksOd1.apply(predmeti.size())) - 1;

		return predmeti.get(index);
	}

	/**
	 * Pita korisnika za odabir jednog od danih studenata.
	 * @param scanner Scanner s kojim se čita korisnikov odabir.
	 * @param studenti Popis studenata koji se mogu odabrati.
	 * @return Odabran student.
	 */
	static Student odaberiStudenta(Scanner scanner, Set<Student> studenti) {
		var s = studenti.toArray(new Student[0]);
		
		for (int i = 0; i < studenti.size(); i++) {
			System.out.printf("  %d. %s %s%n", i + 1, s[i].getIme(), s[i].getPrezime());
		}

		System.out.print("  izaberite studenta: ");
		int index = SafeScanning.ensureNextInt(scanner, jeLiIspravanIndeksOd1.apply(studenti.size())) - 1;

		return s[index];
	}

	/**
	 * Pita korisnika za odabir barem jednog studenata.
	 * @param scanner Scanner s kojim se čita korisnikov odabir.
	 * @param sviStudenti Popis studenata koji se mogu odabrati.
	 * @return Odabrani studenti.
	 */
	static Set<Student> odaberiUpisaneStudente(Scanner scanner, List<Student> sviStudenti) {
		for (int i = 0; i < sviStudenti.size(); i++) {
			System.out.printf("  %d. %s %s%n", i + 1, sviStudenti.get(i).getIme(), sviStudenti.get(i).getPrezime());
		}

		System.out.print("  izaberite studente (brojevi razdvojeni razmakom, bez točke, min. 2 studenta): ");
		
		var input = SafeScanning.ensureNextString(scanner, s ->
			Pattern.matches("(\\d+ )+\\d+", s) // min. 2 broja odvojeni s razmakom
			&& Arrays.stream(s.split(" "))
				.mapToInt(Integer::parseInt)
				.allMatch(n -> n >= 1 && n <= sviStudenti.size())
		);
		
		var indecies = Arrays.stream(input.split(" ")).mapToInt(s -> Integer.parseInt(s) - 1).toArray();
		Set<Student> upisaniStudenti = new HashSet<>();
		int i = 0;

		for (int j = 0; j < indecies.length; j++) {
			upisaniStudenti.add(sviStudenti.get(j));
		}

		return upisaniStudenti;
	}
}

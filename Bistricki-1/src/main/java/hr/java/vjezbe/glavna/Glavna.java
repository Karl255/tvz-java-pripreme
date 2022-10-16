package hr.java.vjezbe.glavna;

import hr.java.vjezbe.entitet.Ispit;
import hr.java.vjezbe.entitet.Predmet;
import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.entitet.Student;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Scanner;

public class Glavna {
	private static final int BROJ_STUDENATA = 2;
	private static final int BROJ_PROFESORA = 2;
	private static final int BROJ_PREDMETA = 3;
	private static final int BROJ_ISPITA = 3;

	public static void main(String[] args) {
		Student[] studenti = new Student[BROJ_STUDENATA];
		Profesor[] profesori = new Profesor[BROJ_PROFESORA];
		Predmet[] predmeti = new Predmet[BROJ_PREDMETA];
		Ispit[] ispiti = new Ispit[BROJ_ISPITA];

		Scanner scanner = new Scanner(System.in);

		for (int i = 0; i < studenti.length; i++) {
			System.out.printf("Unesite podatke za %d. studenta:%n", i + 1);
			Student student = nextStudent(scanner);
			studenti[i] = student;
		}

		for (int i = 0; i < profesori.length; i++) {
			System.out.printf("Unesite podatke za %d. profesora:%n", i + 1);
			Profesor profesor = nextProfesor(scanner);
			profesori[i] = profesor;
		}

		for (int i = 0; i < predmeti.length; i++) {
			System.out.printf("Unesite podatke za %d. predmet:%n", i + 1);
			Predmet predmet = nextPredmet(scanner, profesori, studenti);
			predmeti[i] = predmet;
		}

		for (int i = 0; i < ispiti.length; i++) {
			System.out.printf("Unesite podatke za %d. ispit:%n", i + 1);
			Ispit ispit = nextIspit(scanner, predmeti);
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
			System.out.println("nitko nije zaslužio ocjenu 5!");
		}

		scanner.close();
	}

	static Profesor nextProfesor(Scanner scanner) {
		System.out.print("  šifra: ");
		String sifra = scanner.nextLine();

		System.out.print("  ime: ");
		String ime = scanner.nextLine();

		System.out.print("  prezime: ");
		String prezime = scanner.nextLine();

		System.out.print("  titula: ");
		String titula = scanner.nextLine();

		return new Profesor(sifra, ime, prezime, titula);
	}

	static Student nextStudent(Scanner scanner) {
		System.out.print("  ime: ");
		String ime = scanner.nextLine();

		System.out.print("  prezime: ");
		String prezime = scanner.nextLine();

		System.out.print("  JMBAG: ");
		String jmbag = scanner.nextLine();

		System.out.print("  datum rođenja (dd.MM.yyyy.): ");
		LocalDate datumRodjenja = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("dd.MM.yyyy."));

		return new Student(ime, prezime, jmbag, datumRodjenja);
	}

	static Predmet nextPredmet(Scanner scanner, Profesor[] profesori, Student[] sviStudenti) {
		System.out.print("  šifra: ");
		String sifra = scanner.nextLine();

		System.out.print("  naziv: ");
		String naziv = scanner.nextLine();

		System.out.print("  broj ECTS bodova: ");
		int brojEctsBodova = scanner.nextInt();
		scanner.nextLine();

		Profesor nositelj = odaberiProfesora(scanner, profesori);
		Student[] upisaniStudenti = odaberiUpisaneStudente(scanner, sviStudenti);

		return new Predmet(sifra, naziv, brojEctsBodova, nositelj, upisaniStudenti);
	}

	static Ispit nextIspit(Scanner scanner, Predmet[] predmeti) {
		Predmet predmet = odaberiPredmet(scanner, predmeti);
		Student student = odaberiStudenta(scanner, predmet.getStudenti());

		System.out.print("  ocjena (1-5): ");
		int ocjena = scanner.nextInt();
		scanner.nextLine();

		System.out.print("  datum i vrijeme pisanja (dd.MM.yyyy. HH:mm): ");
		LocalDateTime datumIVrijeme = LocalDateTime.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm"));

		return new Ispit(predmet, student, ocjena, datumIVrijeme);
	}

	static Profesor odaberiProfesora(Scanner scanner, Profesor[] profesori) {
		for (int i = 0; i < profesori.length; i++) {
			System.out.printf("  %d. %s %s%n", i + 1, profesori[i].getIme(), profesori[i].getPrezime());
		}

		System.out.print("  izaberite profesora: ");
		int index = scanner.nextInt() - 1;
		scanner.nextLine();

		return profesori[index];
	}

	static Predmet odaberiPredmet(Scanner scanner, Predmet[] predmeti) {
		for (int i = 0; i < predmeti.length; i++) {
			System.out.printf("  %d. %s%n", i + 1, predmeti[i].getNaziv());
		}

		System.out.print("  izaberite predmet: ");
		int index = scanner.nextInt() - 1;
		scanner.nextLine();

		return predmeti[index];
	}

	static Student odaberiStudenta(Scanner scanner, Student[] studenti) {
		for (int i = 0; i < studenti.length; i++) {
			System.out.printf("  %d. %s %s%n", i + 1, studenti[i].getIme(), studenti[i].getPrezime());
		}

		System.out.print("  izaberite studenta: ");
		int index = scanner.nextInt() - 1;
		scanner.nextLine();

		return studenti[index];
	}

	static Student[] odaberiUpisaneStudente(Scanner scanner, Student[] sviStudenti) {
		for (int i = 0; i < sviStudenti.length; i++) {
			System.out.printf("  %d. %s %s%n", i + 1, sviStudenti[i].getIme(), sviStudenti[i].getPrezime());
		}

		System.out.print("  izaberite studente (brojevi razdvojeni razmakom, bez točke): ");
		int[] indecies = Arrays.stream(scanner.nextLine().split(" ")).mapToInt(s -> Integer.parseInt(s) - 1).toArray();
		Student[] upisaniStudenti = new Student[indecies.length];
		int i = 0;

		for (int j = 0; j < indecies.length; j++) {
			upisaniStudenti[i++] = sviStudenti[j];
		}

		return upisaniStudenti;
	}
}

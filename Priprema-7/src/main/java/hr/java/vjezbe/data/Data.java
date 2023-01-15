package hr.java.vjezbe.data;

import hr.java.vjezbe.entitet.*;

import java.util.List;

public class Data {
	private static List<Profesor> profesori = Datoteke.loadProfesori();
	private static List<Student> studenti = Datoteke.loadStudenti();
	private static List<Predmet> predmeti = Datoteke.loadPredmeti(profesori, studenti);
	private static List<Ispit> ispiti = Datoteke.loadIspiti(studenti, predmeti);
	private static List<ObrazovnaUstanova> obrazovneUstanove = Datoteke.loadObrazovneUstanove(profesori, predmeti, ispiti);

	public static List<Profesor> getProfesori() {
		return profesori;
	}

	public static List<Student> getStudenti() {
		return studenti;
	}

	public static List<Predmet> getPredmeti() {
		return predmeti;
	}

	public static List<Ispit> getIspiti() {
		return ispiti;
	}

	public static List<ObrazovnaUstanova> getObrazovneUstanove() {
		return obrazovneUstanove;
	}
}

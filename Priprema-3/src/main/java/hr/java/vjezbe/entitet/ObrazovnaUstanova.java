package hr.java.vjezbe.entitet;

import java.util.HashSet;
import java.util.List;

/**
 * Bazna klasa za obrazovne ustanove. 
 */
public abstract class ObrazovnaUstanova {
	private String naziv;
	private Predmet[] predmeti;
	private Profesor[] profesori;
	private Ispit[] ispiti;

	public ObrazovnaUstanova(String naziv, Predmet[] predmeti, Profesor[] profesori, Ispit[] ispiti) {
		this.naziv = naziv;
		this.predmeti = predmeti;
		this.profesori = profesori;
		this.ispiti = ispiti;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public Predmet[] getPredmeti() {
		return predmeti;
	}

	public void setPredmeti(Predmet[] predmeti) {
		this.predmeti = predmeti;
	}

	public Profesor[] getProfesori() {
		return profesori;
	}

	public void setProfesori(Profesor[] profesori) {
		this.profesori = profesori;
	}

	public Ispit[] getIspiti() {
		return ispiti;
	}

	public void setIspiti(Ispit[] ispiti) {
		this.ispiti = ispiti;
	}

	/**
	 * Pronalazi najuspješnijeg studenta na godini.
	 * @param godina Godina za pretraživanje.
	 * @return Najuspješniji student.
	 */
	public abstract Student odrediNajuspjesnijegStudentaNaGodini(int godina);

	/**
	 * Vraća popis studenata dobiven iz popisa ispita. Isti student se neće ponavljati.
	 * @return Popis studenata.
	 */
	public Student[] dobijSveStudente() {
		HashSet<Student> studenti = new HashSet<>();

		for (var i : getIspiti()) {
			studenti.add(i.getStudent());
		}

		return studenti.toArray(new Student[0]);
	}

	/**
	 * Vraća popis studenata dobiven iz popisa ispita koji nemaju negativnu ocjenu. Isti student se neće ponavljati.
	 * @return Popis studenata koji nemaju negativnu ocjenu.
	 */
	public Student[] dobijSveStudenteKojiProlaze() {
		HashSet<Student> studenti = new HashSet<>(List.of(dobijSveStudente()));
		
		for (var i : ispiti) {
			if (i.getOcjena() == 1) {
				studenti.remove(i.getStudent());
			}
		}
		
		return studenti.toArray(new Student[0]);
	}
}

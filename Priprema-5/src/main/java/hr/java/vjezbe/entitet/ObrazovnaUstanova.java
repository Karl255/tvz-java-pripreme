package hr.java.vjezbe.entitet;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Bazna klasa za obrazovne ustanove. 
 */
public abstract class ObrazovnaUstanova {
	protected String naziv;
	protected List<Predmet> predmeti;
	protected List<Profesor> profesori;
	protected List<Ispit> ispiti;

	public ObrazovnaUstanova(String naziv, List<Predmet> predmeti, List<Profesor> profesori, List<Ispit> ispiti) {
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

	public List<Predmet> getPredmeti() {
		return predmeti;
	}

	public void setPredmeti(List<Predmet> predmeti) {
		this.predmeti = predmeti;
	}

	public List<Profesor> getProfesori() {
		return profesori;
	}

	public void setProfesori(List<Profesor> profesori) {
		this.profesori = profesori;
	}

	public List<Ispit> getIspiti() {
		return ispiti;
	}

	public void setIspiti(List<Ispit> ispiti) {
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
	public List<Student> dobijSveStudente() {
		Set<Student> studenti = new HashSet<>();

		for (var i : getIspiti()) {
			studenti.add(i.getStudent());
		}

		return studenti.stream().toList();
	}

	/**
	 * Vraća popis studenata dobiven iz popisa ispita koji nemaju negativnu ocjenu. Isti student se neće ponavljati.
	 * @return Popis studenata koji nemaju negativnu ocjenu.
	 */
	public List<Student> dobijSveStudenteKojiProlaze() {
		Set<Student> studenti = new HashSet<>(dobijSveStudente());
		
		for (var i : ispiti) {
			if (i.getOcjena().getNumerickaVrijednost() == 1) {
				studenti.remove(i.getStudent());
			}
		}
		
		return studenti.stream().toList();
	}
	
	public List<Ispit> ispitiSOcjenomIzvrstan() {
		return ispiti.stream().filter(i -> i.getOcjena() == Ocjena.IZVRSTAN).collect(Collectors.toList());
	}
}

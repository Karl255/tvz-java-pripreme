package hr.java.vjezbe.entitet;

import java.time.LocalDateTime;

/**
 * Ispit proveden u tercijarnoj obrazovnoj ustanovi.
 */
public final class Ispit implements Online {
	private Predmet predmet;
	private Student student;
	private Ocjena ocjena;
	private LocalDateTime datumIVrijeme;
	private Dvorana dvorana;
	private String nazivOnlineSoftvera = null;

	public Ispit(Predmet predmet, Student student, Ocjena ocjena, LocalDateTime datumIVrijeme, Dvorana dvorana) {
		this.predmet = predmet;
		this.student = student;
		this.ocjena = ocjena;
		this.datumIVrijeme = datumIVrijeme;
		this.dvorana = dvorana;
	}

	public Predmet getPredmet() {
		return predmet;
	}

	public void setPredmet(Predmet predmet) {
		this.predmet = predmet;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Ocjena getOcjena() {
		return ocjena;
	}

	public void setOcjena(Ocjena ocjena) {
		this.ocjena = ocjena;
	}

	public LocalDateTime getDatumIVrijeme() {
		return datumIVrijeme;
	}

	public void setDatumIVrijeme(LocalDateTime datumIVrijeme) {
		this.datumIVrijeme = datumIVrijeme;
	}

	public Dvorana getDvorana() {
		return dvorana;
	}

	public Ispit setDvorana(Dvorana dvorana) {
		this.dvorana = dvorana;
		return this;
	}

	/**
	 * Postavlja naziv online softvera ako se ispit održao online.
	 * @param nazivOnlineSoftvera Naziv korištenog softvera.
	 */
	@Override
	public void setNazivOnlineSoftvera(String nazivOnlineSoftvera) {
		this.nazivOnlineSoftvera = nazivOnlineSoftvera;
	}

	public String getNazivOnlineSoftvera() {
		return nazivOnlineSoftvera;
	}
}

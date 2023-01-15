package hr.java.vjezbe.entitet;

import java.util.Set;

/**
 * Predmet koji se izvodi u tercijarnoj obrazovnoj ustanovi.
 */
public class Predmet extends Entitet {
	private String sifra;
	private String naziv;
	private int brojEctsBodova;
	private Profesor nositelj;
	private Set<Student> studenti;

	public Predmet(long id, String sifra, String naziv, int brojEctsBodova, Profesor nositelj, Set<Student> studenti) {
		super(id);
		this.sifra = sifra;
		this.naziv = naziv;
		this.brojEctsBodova = brojEctsBodova;
		this.nositelj = nositelj;
		this.studenti = studenti;
	}

	public String getSifra() {
		return sifra;
	}

	public void setSifra(String sifra) {
		this.sifra = sifra;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public int getBrojEctsBodova() {
		return brojEctsBodova;
	}

	public void setBrojEctsBodova(int brojEctsBodova) {
		this.brojEctsBodova = brojEctsBodova;
	}

	public Profesor getNositelj() {
		return nositelj;
	}

	public void setNositelj(Profesor nositelj) {
		this.nositelj = nositelj;
	}

	public Set<Student> getStudenti() {
		return studenti;
	}

	public void setStudenti(Set<Student> studenti) {
		this.studenti = studenti;
	}
}

package hr.java.vjezbe.entitet;

public class Profesor extends Osoba {
	private String sifra;
	private String titula;

	private Profesor(String sifra, String ime, String prezime, String titula) {
		super(ime, prezime);
		this.sifra = sifra;
		this.titula = titula;
	}

	public String getSifra() {
		return sifra;
	}

	public void setSifra(String sifra) {
		this.sifra = sifra;
	}

	public String getTitula() {
		return titula;
	}

	public void setTitula(String titula) {
		this.titula = titula;
	}
	
	public static class ProfesorBuilder {
		private String sifra;
		private String ime;
		private String prezime;
		private String titula;

		public ProfesorBuilder setSifra(String sifra) {
			this.sifra = sifra;
			return this;
		}

		public ProfesorBuilder setIme(String ime) {
			this.ime = ime;
			return this;
		}

		public ProfesorBuilder setPrezime(String prezime) {
			this.prezime = prezime;
			return this;
		}

		public ProfesorBuilder setTitula(String titula) {
			this.titula = titula;
			return this;
		}
		
		public Profesor build() {
			return new Profesor(sifra, ime, prezime, titula);
		}
	}
}

package hr.java.vjezbe.entitet;

/**
 * Profesor na tercijarnoj obrazovnoj ustanovi.
 */
public class Profesor extends Osoba {
	private String sifra;
	private String titula;

	public Profesor(long id, String sifra, String ime, String prezime, String titula) {
		super(id, ime, prezime);
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

	@Override
	public String toString() {
		return titula + ' ' + ime + ' ' + prezime;
	}

	/**
	 * Klasa za generiranje objekata tipa {@link Profesor}
	 */
	public static class ProfesorBuilder {
		private long id;
		private String sifra;
		private String ime;
		private String prezime;
		private String titula;

		public ProfesorBuilder(long id) {
			this.id = id;
		}

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

		/**
		 * Generira objekt tipa {@link Profesor}.
		 * @return Generiran objekt tipa {@link Profesor}.
		 */
		public Profesor build() {
			return new Profesor(id, sifra, ime, prezime, titula);
		}
	}
}

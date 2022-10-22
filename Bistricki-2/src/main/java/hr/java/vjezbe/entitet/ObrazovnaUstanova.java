package hr.java.vjezbe.entitet;

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
	
	public abstract Student odrediNajuspjesnijegStudentaNaGodini(int godina);
}

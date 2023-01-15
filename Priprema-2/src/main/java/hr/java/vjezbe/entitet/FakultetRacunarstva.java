package hr.java.vjezbe.entitet;

import java.math.BigDecimal;
import java.util.HashSet;

public class FakultetRacunarstva extends ObrazovnaUstanova implements Diplomski {
	public FakultetRacunarstva(String naziv, Predmet[] predmeti, Profesor[] profesori, Ispit[] ispiti) {
		super(naziv, predmeti, profesori, ispiti);
	}

	@Override
	public BigDecimal izracunajKonacnuOcjenuStudijaZaStudenta(Ispit[] ispiti, int ocjenaDiplomskogRada, int ocjenaObraneDiplomskogRada) {
		return odrediProsjekOcjenaNaIspitima(ispiti)
			.multiply(new BigDecimal(3))
			.add(new BigDecimal(ocjenaDiplomskogRada))
			.add(new BigDecimal(ocjenaObraneDiplomskogRada))
			.divide(new BigDecimal(5));
	}

	@Override
	public Student odrediNajuspjesnijegStudentaNaGodini(int godina) {
		Ispit[] ovogodisnjiIspiti = filtrirajOvogodisnjeIspite(getIspiti(), godina);

		HashSet<Student> studenti = new HashSet<>();
		Student najboljiStudent = null;
		int najboljiBrojIzvrsnihOcjena = 0;

		for (var i : ovogodisnjiIspiti) {
			studenti.add(i.getStudent());
		}

		for (var s : studenti) {
			int brojIzvrsnihOcjena = odrediBrojIzvrsnihOcjena(filtrirajPoStudentu(ovogodisnjiIspiti, s));

			if (brojIzvrsnihOcjena > najboljiBrojIzvrsnihOcjena) {
				najboljiStudent = s;
				najboljiBrojIzvrsnihOcjena = brojIzvrsnihOcjena;
			}
		}

		return najboljiStudent;
	}
	
	private int odrediBrojIzvrsnihOcjena(Ispit[] ispiti) {
		int count = 0;
		
		for (var i : ispiti) {
			if (i.getOcjena() == 5) {
				count++;
			}
		}
		
		return count;
	}

	@Override
	public Student odrediStudentaZaRektorovuNagradu() {
		HashSet<Student> studenti = new HashSet<>();

		for (var i : getIspiti()) {
			studenti.add(i.getStudent());
		}
		
		Student najboljiStudent = null;
		BigDecimal najboljiProsjek = new BigDecimal(0);

		for (var s : studenti) {
			BigDecimal prosjek = odrediProsjekOcjenaNaIspitima(filtrirajPoStudentu(getIspiti(), s));

			if (prosjek.compareTo(najboljiProsjek) >= 0 || najboljiStudent == null || prosjek.equals(najboljiProsjek) && s.getDatumRodjenja().compareTo(najboljiStudent.getDatumRodjenja()) > 0) {
				najboljiStudent = s;
				najboljiProsjek = prosjek;
			}
		}

		return najboljiStudent;
	}
}

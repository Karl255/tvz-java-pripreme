package hr.java.vjezbe.entitet;

import java.math.BigDecimal;
import java.util.HashSet;

public class VeleucilisteJave extends ObrazovnaUstanova implements Visokoskolska {
	public VeleucilisteJave(String naziv, Predmet[] predmeti, Profesor[] profesori, Ispit[] ispiti) {
		super(naziv, predmeti, profesori, ispiti);
	}

	@Override
	public BigDecimal izracunajKonacnuOcjenuStudijaZaStudenta(Ispit[] ispiti, int ocjenaZavrsnogRada, int ocjenaObraneZavrsnogRada) {
		return odrediProsjekOcjenaNaIspitima(ispiti)
			.multiply(new BigDecimal(2))
			.add(new BigDecimal(ocjenaZavrsnogRada))
			.add(new BigDecimal(ocjenaObraneZavrsnogRada))
			.divide(new BigDecimal(4));
	}

	@Override
	public Student odrediNajuspjesnijegStudentaNaGodini(int godina) {
		Ispit[] ovogodisnjiIspiti = filtrirajOvogodisnjeIspite(getIspiti(), godina);
		HashSet<Student> studenti = new HashSet<>();
		
		for (var i : ovogodisnjiIspiti) {
			studenti.add(i.getStudent());
		}
		
		Student najboljiStudent = null;
		BigDecimal najboljiProsjek = new BigDecimal(0);
		
		for (var s : studenti) {
			BigDecimal prosjek = odrediProsjekOcjenaNaIspitima(filtrirajPoStudentu(ovogodisnjiIspiti, s));
			
			if (prosjek.compareTo(najboljiProsjek) >= 0) {
				najboljiStudent = s;
				najboljiProsjek = prosjek;
			}
		}
		
		return najboljiStudent;
	}
}

package hr.java.vjezbe.entitet;

import hr.java.vjezbe.glavna.Glavna;
import hr.java.vjezbe.iznimke.NemoguceOdreditiProsjekStudentaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashSet;

/**
 * Visokoškolska obrazovna ustanova za Javu.
 */
public class VeleucilisteJave extends ObrazovnaUstanova implements Visokoskolska {
	private static final Logger logger = LoggerFactory.getLogger(Glavna.class);
	
	public VeleucilisteJave(String naziv, Predmet[] predmeti, Profesor[] profesori, Ispit[] ispiti) {
		super(naziv, predmeti, profesori, ispiti);
	}

	@Override
	public BigDecimal izracunajKonacnuOcjenuStudijaZaStudenta(Ispit[] ispiti, int ocjenaZavrsnogRada, int ocjenaObraneZavrsnogRada) {
		try {
			return odrediProsjekOcjenaNaIspitima(ispiti)
				.multiply(BigDecimal.TWO)
				.add(new BigDecimal(ocjenaZavrsnogRada))
				.add(new BigDecimal(ocjenaObraneZavrsnogRada))
				.divide(new BigDecimal(4));
		} catch (NemoguceOdreditiProsjekStudentaException e) {
			Student s = ispiti[0].getStudent();
			logger.warn(String.format("Student %s %s zbog negativne ocjene na jednom od ispita ima prosjek 'nedovoljan (1)'!", s.getIme(), s.getPrezime()), e);
			return BigDecimal.ONE;
		}
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
			BigDecimal prosjek;
			try {
				prosjek = odrediProsjekOcjenaNaIspitima(filtrirajPoStudentu(ovogodisnjiIspiti, s));
			} catch (NemoguceOdreditiProsjekStudentaException e) {
				logger.info(String.format("Student %s %s zbog negativne ocjene na jednom od ispita ima prosjek 'nedovoljan (1)'!", s.getIme(), s.getPrezime()), e);
				continue;
			}
			
			if (prosjek.compareTo(najboljiProsjek) >= 0) {
				najboljiStudent = s;
				najboljiProsjek = prosjek;
			}
		}
		
		return najboljiStudent;
	}
}

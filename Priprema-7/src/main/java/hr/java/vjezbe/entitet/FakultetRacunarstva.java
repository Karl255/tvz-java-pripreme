package hr.java.vjezbe.entitet;

import hr.java.vjezbe.iznimke.NemoguceOdreditiProsjekStudentaException;
import hr.java.vjezbe.iznimke.PostojiViseNajmladjihStudenataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

/**
 * Diplomska obrazovna ustanova za računarstvo.
 */
public class FakultetRacunarstva extends ObrazovnaUstanova implements Diplomski {
	private static final Logger logger = LoggerFactory.getLogger(FakultetRacunarstva.class);
	
	public FakultetRacunarstva(long id, String naziv, List<Predmet> predmeti, List<Profesor> profesori, List<Ispit> ispiti) {
		super(id, naziv, predmeti, profesori, ispiti);
	}

	@Override
	public BigDecimal izracunajKonacnuOcjenuStudijaZaStudenta(List<Ispit> ispiti, Ocjena ocjenaDiplomskogRada, Ocjena ocjenaObraneDiplomskogRada) {
		try {
			return odrediProsjekOcjenaNaIspitima(ispiti)
				.multiply(new BigDecimal(3))
				.add(ocjenaDiplomskogRada.toBigDecimal())
				.add(ocjenaObraneDiplomskogRada.toBigDecimal())
				.divide(new BigDecimal(5));
		} catch (NemoguceOdreditiProsjekStudentaException e) {
			Student s = ispiti.get(0).getStudent();
			logger.warn(String.format("Student %s %s zbog negativne ocjene na jednom od ispita ima prosjek 'nedovoljan (1)'!", s.getIme(), s.getPrezime()), e);
			return BigDecimal.ONE;
		}
	}

	@Override
	public Student odrediNajuspjesnijegStudentaNaGodini(int godina) {
		List<Ispit> ovogodisnjiIspiti = filtrirajOvogodisnjeIspite(ispiti, godina);

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

	/**
	 * Određuje broj izvrsnih ocjena (5) na ispitima. 
	 * @param ispiti Ispiti iz kojih će se izvlačiti ocjene.
	 * @return Količina izvrsnih ocjena (5). 
	 */
	private int odrediBrojIzvrsnihOcjena(List<Ispit> ispiti) {
		int count = 0;
		
		for (var i : ispiti) {
			if (i.getOcjena().getNumerickaVrijednost() == 5) {
				count++;
			}
		}
		
		return count;
	}

	@Override
	public Student odrediStudentaZaRektorovuNagradu() {
		HashSet<Student> studenti = new HashSet<>();

		for (var i : ispiti) {
			studenti.add(i.getStudent());
		}
		
		Student najboljiStudent = null;
		BigDecimal najboljiProsjek = new BigDecimal(0);

		for (var s : studenti) {
			BigDecimal prosjek;
			try {
				prosjek = odrediProsjekOcjenaNaIspitima(filtrirajPoStudentu(ispiti, s));
			} catch (NemoguceOdreditiProsjekStudentaException e) {
				logger.info(String.format("Student %s %s zbog negativne ocjene na jednom od ispita ima prosjek 'nedovoljan (1)'!", s.getIme(), s.getPrezime()), e);
				continue;
			}

			int usporedbaDobi = najboljiStudent != null
				? s.getDatumRodjenja().compareTo(najboljiStudent.getDatumRodjenja())
				: 1;
			
			if (prosjek.compareTo(najboljiProsjek) >= 0 || prosjek.equals(najboljiProsjek) && usporedbaDobi > 0) {
				najboljiStudent = s;
				najboljiProsjek = prosjek;
			} else if (usporedbaDobi == 0) {
				String najmladjiStudenti = String.format("%s %s, %s %s",
					najboljiStudent.getIme(),
					najboljiStudent.getPrezime(),
					s.getIme(),
					s.getPrezime());

				System.out.println("Pronađeno je više najmlađih studenata: " + najmladjiStudenti);
				logger.error("Pronađeno je više najmlađih studenata: " + najmladjiStudenti);
				
				throw new PostojiViseNajmladjihStudenataException(najmladjiStudenti);
			}
		}

		return najboljiStudent;
	}
}

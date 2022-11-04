package hr.java.vjezbe.entitet;

import hr.java.vjezbe.iznimke.NemoguceOdreditiProsjekStudentaException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Interface za smjerove na visokoškolskoj razini.
 */
public interface Visokoskolska {
	/**
	 * Računa konačnu ocjenu iz danih ispita. Nije dozvoljena negativna ocjena.
	 * @param ispiti Ispiti iz kojih se uzimaju ocjene. Očekuje se filtriran popis ispita za jednog studenta.
	 * @param ocjenaZavrsnogRada Ocjena dobivena za završni rad.
	 * @param ocjenaObraneZavrsnogRada Ocjena dobivena za obranu završnog rada.
	 * @return Konačna ocjena studenta.
	 */
	BigDecimal izracunajKonacnuOcjenuStudijaZaStudenta(List<Ispit> ispiti, Ocjena ocjenaZavrsnogRada, Ocjena ocjenaObraneZavrsnogRada);

	/**
	 * Računa prosječnu ocjenu iz danih ispita. Nije dozvoljena negativna ocjena.
	 * @param ispiti Ispiti iz kojih se uzimaju ocjene. Očekuje se filtriran popis ispita za jednog studenta.
	 * @return Prosječna ocjena studenta na ispitima.
	 * @throws NemoguceOdreditiProsjekStudentaException Ako se pronađe ispit s negativnom ocjenom.
	 */
	default BigDecimal odrediProsjekOcjenaNaIspitima(List<Ispit> ispiti) throws NemoguceOdreditiProsjekStudentaException {
		BigDecimal sum = new BigDecimal(0);
		int count = 0;

		for (var i : ispiti) {
			if (i.getOcjena().getNumerickaVrijednost() > 1) {
				sum = sum.add(i.getOcjena().toBigDecimal());
				count++;
			} else {
				throw new NemoguceOdreditiProsjekStudentaException(String.format("Student %s %s je ocjenjen negativom ocjenom iz predmeta %s (%s)!",
					i.getStudent().getIme(),
					i.getStudent().getPrezime(),
					i.getPredmet().getNaziv(),
					i.getPredmet().getSifra()));
			}
		}

		return sum.divide(new BigDecimal(count));
	}

	/**
	 * Izbacuje ispite s negativnom ocjenom.
	 * @param ispiti Ispiti iz kojih se uzimaju ocjene.
	 * @return Filtrirani ispiti.
	 */
	private List<Ispit> filtrirajPolozeneIspite(List<Ispit> ispiti) {
		ArrayList<Ispit> filtrirano = new ArrayList<>();

		for (var i : ispiti) {
			if (i.getOcjena().getNumerickaVrijednost() > 1) {
				filtrirano.add(i);
			}
		}

		return filtrirano;
	}

	/**
	 * Vraća sve ispite koje je pisao određen student.
	 * @param ispiti Svi ispiti.
	 * @param student Student prema kojem se filtriraju ispiti. 
	 * @return Filtrirani ispiti.
	 */
	default List<Ispit> filtrirajPoStudentu(List<Ispit> ispiti, Student student) {
		ArrayList<Ispit> filtrirano = new ArrayList<>();

		for (var i : ispiti) {
			if (i.getStudent().equals(student)) {
				filtrirano.add(i);
			}
		}

		return filtrirano;
	}

	/**
	 * Vraća sve ispite koji su pisani određene godine.
	 * @param ispiti Svi ispiti.
	 * @param godina Godina prema kojoj se filtriraju ispiti.
	 * @return Filtrirani ispiti.
	 */
	default List<Ispit> filtrirajOvogodisnjeIspite(List<Ispit> ispiti, int godina) {
		ArrayList<Ispit> ovogodisnjiIspitiList = new ArrayList<>();

		for (var i : ispiti) {
			if (i.getDatumIVrijeme().getYear() == godina) {
				ovogodisnjiIspitiList.add(i);
			}
		}

		return ovogodisnjiIspitiList;
	}
}

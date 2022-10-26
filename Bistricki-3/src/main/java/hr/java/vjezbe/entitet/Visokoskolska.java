package hr.java.vjezbe.entitet;

import hr.java.vjezbe.iznimke.NemoguceOdreditiProsjekStudentaException;

import java.math.BigDecimal;
import java.util.ArrayList;

public interface Visokoskolska {
	 BigDecimal izracunajKonacnuOcjenuStudijaZaStudenta(Ispit[] ispiti, int ocjenaZavrsnogRada, int ocjenaObraneZavrsnogRada);
	 
	 default BigDecimal odrediProsjekOcjenaNaIspitima(Ispit[] ispiti) throws NemoguceOdreditiProsjekStudentaException {
		 BigDecimal sum = new BigDecimal(0);
		 int count = 0;
		 
		 for (var i : ispiti) {
			 if (i.getOcjena() > 1) {
				 sum = sum.add(new BigDecimal(i.getOcjena()));
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
	 
	 private Ispit[] filtrirajPolozeneIspite(Ispit[] ispiti) {
		 ArrayList<Ispit> filtrirano = new ArrayList<>();
		 
		 for (var i : ispiti) {
			 if (i.getOcjena() > 1) {
				 filtrirano.add(i);
			 }
		 }
		 
		 return filtrirano.toArray(new Ispit[0]);
	 }
	 
	 default Ispit[] filtrirajPoStudentu(Ispit[] ispiti, Student student) {
		 ArrayList<Ispit> filtrirano = new ArrayList<>();

		 for (var i : ispiti) {
			 if (i.getStudent().equals(student)) {
				 filtrirano.add(i);
			 }
		 }

		 return filtrirano.toArray(new Ispit[0]);
	 }
	 
	 default Ispit[] filtrirajOvogodisnjeIspite(Ispit[] ispiti, int godina) {
		 ArrayList<Ispit> ovogodisnjiIspitiList = new ArrayList<>();

		 for (var i : ispiti) {
			 if (i.getDatumIVrijeme().getYear() == godina) {
				 ovogodisnjiIspitiList.add(i);
			 }
		 }

		 return ovogodisnjiIspitiList.toArray(new Ispit[0]);
	 }
}

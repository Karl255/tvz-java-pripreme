package hr.java.vjezbe.entitet;

/**
 * Interface za smjerove na diplomskoj razini.
 */
public interface Diplomski extends Visokoskolska {
	/**
	 * Pronalazi najboljeg studenta za rektorovu nagradu.
	 * @return Najbolji student izabran za rektorovu nagradu.
	 */
	Student odrediStudentaZaRektorovuNagradu();
}

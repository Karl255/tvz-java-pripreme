package hr.java.vjezbe.glavna;

import hr.java.vjezbe.entitet.Ocjena;
import hr.java.vjezbe.iznimke.InputPredicateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.function.Predicate;

/**
 * Pomoćne metode za siguran i garantiran upis točnih podataka.
 */
final class SafeScanning {
	private static final Logger logger = LoggerFactory.getLogger(Glavna.class);

	static LocalDate ensureNextDate(Scanner scanner, DateTimeFormatter formatter) {
		LocalDate value = null;
		boolean valid;
		
		do {
			try {
				var s = scanner.nextLine();
				value = LocalDate.parse(s, formatter);
				valid = true;
			} catch (DateTimeParseException e) {
				System.out.println("  Unesen datum je u krivom obliku! Pokušaj ponovno:");
				logger.error("Unesen datum je u krivom obliku!", e);
				valid = false;
			}
		} while (!valid);
		
		return value;
	}

	static LocalDateTime ensureNextDateTime(Scanner scanner, DateTimeFormatter formatter) {
		LocalDateTime value = null;
		boolean valid;

		do {
			try {
				var s = scanner.nextLine();
				value = LocalDateTime.parse(s, formatter);
				valid = true;
			} catch (DateTimeParseException e) {
				System.out.println("  Unesen datum i vrijeme je u krivom obliku! Pokušaj ponovno:");
				logger.error("Unesen datum i vrijeme je u krivom obliku!", e);
				valid = false;
			}
		} while (!valid);

		return value;
	}

	static int ensureNextInt(Scanner scanner, Predicate<Integer> predicate) {
		int value = 0;
		boolean valid;

		do {
			try {
				value = scanner.nextInt();

				if (predicate != null && !predicate.test(value)) {
					throw new InputPredicateException("Unesen broj ne odgovara danom predikatu!");
				}

				valid = true;
			} catch (InputMismatchException e) {
				System.out.println("  Unos treba biti broj! Pokušaj ponovno:");
				logger.error("Nije unesen broj!", e);
				valid = false;
			} catch (InputPredicateException e) {
				System.out.println("  Unesena je nedozvoljena vrijednost! Pokušaj ponovno:");
				logger.error(e.getMessage(), e);
				valid = false;
			} finally {
				scanner.nextLine();
			}
		} while (!valid);

		return value;
	}
	
	static Ocjena ensureNextOcjena(Scanner scanner) {
		int ocjena = ensureNextInt(scanner, x -> x >= 1 && x <= 5);
		
		return switch (ocjena) {
			case 1 -> Ocjena.NEDOVOLJAN;
			case 2 -> Ocjena.DOVOLJAN;
			case 3 -> Ocjena.DOBAR;
			case 4 -> Ocjena.VRLO_DOBAR;
			case 5 -> Ocjena.IZVRSTAN;
			default -> {
				String message = "Kritična greška: dosegnut nedostižan dio koda pri upisu ocjene!";
				logger.error(message);
				throw new RuntimeException(message);
			}
		};
	}
	
	static String ensureNextString(Scanner scanner, Predicate<String> predicate) {
		String value = "";
		boolean valid;

		do {
			try {
				value = scanner.nextLine();

				if (predicate != null && !predicate.test(value)) {
					throw new InputPredicateException("Unos ne odgovara danom predikatu!");
				}

				valid = true;
			} catch (InputPredicateException e) {
				System.out.println("  Unesena je nedozvoljena vrijednost! Pokušaj ponovno:");
				logger.error(e.getMessage(), e);
				valid = false;
			}
		} while (!valid);

		return value;
	}
}

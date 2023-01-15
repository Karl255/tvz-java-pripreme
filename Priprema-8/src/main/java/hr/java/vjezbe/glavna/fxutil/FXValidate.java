package hr.java.vjezbe.glavna.fxutil;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.function.Predicate;

public class FXValidate {
	public static boolean isValidInt(String input) {
		try {
			int x = Integer.parseInt(input);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	public static boolean isValidInt(String input, Predicate<Integer> predicate) {
		try {
			return predicate.test(Integer.parseInt(input));
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	public static boolean isValidTime(String input, DateTimeFormatter format) {
		try {
			var time = LocalTime.parse(input, format);
			return true;
		} catch (DateTimeParseException e) {
			return false;
		}
	}
}

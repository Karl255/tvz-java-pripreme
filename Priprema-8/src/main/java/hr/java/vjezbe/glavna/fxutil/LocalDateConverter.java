package hr.java.vjezbe.glavna.fxutil;

import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateConverter extends StringConverter<LocalDate> {
	private final DateTimeFormatter format;

	public LocalDateConverter(DateTimeFormatter format) {
		this.format = format;
	}

	@Override
	public String toString(LocalDate localDate) {
		return localDate == null ? null : localDate.format(format);
	}

	@Override
	public LocalDate fromString(String s) {
		try {
			return LocalDate.parse(s, format);
		} catch (RuntimeException e) {
			return null;
		}
	}
}

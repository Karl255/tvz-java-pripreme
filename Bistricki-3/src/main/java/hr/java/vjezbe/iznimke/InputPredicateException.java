package hr.java.vjezbe.iznimke;

public class InputPredicateException extends Exception {
	public InputPredicateException() { }

	public InputPredicateException(String message) {
		super(message);
	}

	public InputPredicateException(String message, Throwable cause) {
		super(message, cause);
	}

	public InputPredicateException(Throwable cause) {
		super(cause);
	}

	public InputPredicateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}

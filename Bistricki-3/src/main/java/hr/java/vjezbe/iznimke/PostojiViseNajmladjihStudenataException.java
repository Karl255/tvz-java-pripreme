package hr.java.vjezbe.iznimke;

public class PostojiViseNajmladjihStudenataException extends RuntimeException {
	public PostojiViseNajmladjihStudenataException() { }

	public PostojiViseNajmladjihStudenataException(String message) {
		super(message);
	}

	public PostojiViseNajmladjihStudenataException(String message, Throwable cause) {
		super(message, cause);
	}

	public PostojiViseNajmladjihStudenataException(Throwable cause) {
		super(cause);
	}

	public PostojiViseNajmladjihStudenataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}

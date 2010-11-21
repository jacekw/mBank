package pl.jw.mbank.common.exception;
public class MBankSfiException extends RuntimeException {

	public MBankSfiException() {

	}

	public MBankSfiException(String message, Throwable cause) {
		super(message, cause);
	}

	public MBankSfiException(String message) {
		super(message);
	}

	public MBankSfiException(Throwable cause) {
		super(cause);
	}
}

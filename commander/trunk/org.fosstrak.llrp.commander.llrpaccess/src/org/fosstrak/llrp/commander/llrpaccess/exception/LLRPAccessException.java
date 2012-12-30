package org.fosstrak.llrp.commander.llrpaccess.exception;

/**
 * custom exception of the LLRP access layer.
 * 
 * @author swieland
 *
 */
public class LLRPAccessException extends Exception {

	private static final long serialVersionUID = -6904907023004877040L;

	public LLRPAccessException() {
		super();
	}

	public LLRPAccessException(String message) {
		super(message);
	}

	public LLRPAccessException(Throwable cause) {
		super(cause);
	}

	public LLRPAccessException(String message, Throwable cause) {
		super(message, cause);
	}

}

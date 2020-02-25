package com.zenvia.challenge.caixa.arch.exceptions.handlers;

public class BusinessException extends RuntimeException {
	
	private static final long serialVersionUID = -58113886014038265L;
	
	private String message;

	public BusinessException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}

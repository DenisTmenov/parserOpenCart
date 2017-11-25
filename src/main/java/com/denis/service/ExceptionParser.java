package com.denis.service;

public class ExceptionParser extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ExceptionParser(String message, Throwable cause) {
		super(message, cause);
	}
}

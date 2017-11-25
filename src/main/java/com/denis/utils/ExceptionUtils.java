package com.denis.utils;

public class ExceptionUtils extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ExceptionUtils(String message, Throwable cause) {
		super(message, cause);
	}

	public ExceptionUtils(String message) {
		super(message);
	}
}

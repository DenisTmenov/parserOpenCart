package com.denis.dao.impl;

public class ExceptionDAO extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ExceptionDAO(String message, Throwable cause) {
		super(message, cause);
	}
}

package com.denis.dto;

public class ExceptionDto extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ExceptionDto(String message, Throwable cause) {
		super(message, cause);
	}
}

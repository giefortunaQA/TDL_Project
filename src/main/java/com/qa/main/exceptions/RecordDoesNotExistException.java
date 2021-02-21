package com.qa.main.exceptions;

public class RecordDoesNotExistException extends Exception{

	private static final long serialVersionUID = -395287318175997167L;

	public RecordDoesNotExistException(String message) {
		super(message);
	}
}

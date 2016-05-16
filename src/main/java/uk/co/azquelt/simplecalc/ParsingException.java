package uk.co.azquelt.simplecalc;

import java.util.List;

import org.parboiled.errors.ParseError;

@SuppressWarnings("serial")
public class ParsingException extends Exception {

	private final List<ParseError> parseErrors;

	public ParsingException(List<ParseError> parseErrors) {
		this.parseErrors = parseErrors;
	}

	public List<ParseError> getParseErrors() {
		return parseErrors;
	}
}

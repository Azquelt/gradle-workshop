package uk.co.azquelt.simplecalc;

import org.parboiled.Parboiled;
import org.parboiled.errors.ParseError;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParsingResult;

import uk.co.azquelt.simplecalc.elements.Element;

public class Main {

	public static void main(String[] args) {
		String expression = ArgumentHandler.connectArguments(args);
		
		try {
			int result = evaluateExpression(expression);
			System.out.println(result);
			
		} catch (ParsingException ex) {
			for (ParseError e : ex.getParseErrors()) {
				System.out.println(getParseErrorMessage(e));
				printErrorLocation(expression, e);
			}
			System.exit(2);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private static String getParseErrorMessage(ParseError e) {
		if (e.getErrorMessage() != null) {
			return "Parsing error: " + e.getErrorMessage();
		} else {
			return "Parsing error:";
		}
	}
	
	private static void printErrorLocation(String expression, ParseError e) {
		System.out.println(expression);
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (i = 0; i < e.getStartIndex(); i++) {
			sb.append(' ');
		}
		for (; i < e.getEndIndex(); i++) {
			sb.append('^');
		}
		System.out.println(sb.toString());
	}
	
	public static int evaluateExpression(String expression) throws ParsingException {
		SimpleCalcParser parser = Parboiled.createParser(SimpleCalcParser.class);
		
		ReportingParseRunner<Element> runner = new ReportingParseRunner<>(parser.Input());
		ParsingResult<Element> parseResult = runner.run(expression);
		
		if (parseResult.hasErrors()) {
			throw new ParsingException(parseResult.parseErrors);
		}
		
		Element rootElement = parseResult.resultValue;

		return rootElement.evaluate();
	}
}
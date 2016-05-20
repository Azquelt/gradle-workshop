package uk.co.azquelt.simplecalc.test;

import org.parboiled.Parboiled;
import org.parboiled.parserunners.BasicParseRunner;
import org.parboiled.support.ParsingResult;

import uk.co.azquelt.simplecalc.SimpleCalcParser;
import uk.co.azquelt.simplecalc.elements.AbstractElement;
import uk.co.azquelt.simplecalc.elements.AddElement;
import uk.co.azquelt.simplecalc.elements.Element;
import uk.co.azquelt.simplecalc.elements.NumberElement;
import uk.co.azquelt.simplecalc.elements.SubtractElement;

public class ElementUtils {

	public static Element parse(String input) {
		SimpleCalcParser parser = Parboiled.createParser(SimpleCalcParser.class);
		BasicParseRunner<AbstractElement> runner = new BasicParseRunner<>(parser.Expression());
		ParsingResult<AbstractElement> parsingResult = runner.run(input);
		return parsingResult.resultValue;
	}

	public static Element add(Element left, Element right) {
		return new AddElement(left, right);
	}

	public static Element subtract(Element left, Element right) {
		return new SubtractElement(left, right);
	}

	public static Element num(int num) {
		return new NumberElement(num);
	}

}

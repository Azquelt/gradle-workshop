package uk.co.azquelt.simplecalc;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.support.Var;

import uk.co.azquelt.simplecalc.elements.AbstractElement;
import uk.co.azquelt.simplecalc.elements.AddElement;
import uk.co.azquelt.simplecalc.elements.Element;
import uk.co.azquelt.simplecalc.elements.NumberElement;
import uk.co.azquelt.simplecalc.elements.SubtractElement;

@BuildParseTree
public class SimpleCalcParser extends BaseParser<Element>{

	public Rule Input() {
		return Sequence(
				Expression(),
				EOI);
	}
	
	public Rule Expression() {
		Var<Character> op = new Var<>();
		return Sequence(
				Number(),
				ZeroOrMore(
						AnyOf("+-"),
						op.set(matchedChar()),
						Number(),
						swap() && push(createOpElement(op.get(), pop(), pop()))
						)
				);
	}
	
	Rule Number() {
		return Sequence(
				OneOrMore(CharRange('0', '9')),
				push(new NumberElement(Integer.parseInt(match())))
				);
	}
	
	AbstractElement createOpElement(char op, Element left, Element right) {
		if (op == '+') {
			return new AddElement(left, right);
		} else {
			return new SubtractElement(left, right);
		}
	}
}

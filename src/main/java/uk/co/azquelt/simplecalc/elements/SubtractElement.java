package uk.co.azquelt.simplecalc.elements;

public class SubtractElement extends AbstractPairElement {

	public SubtractElement(Element left, Element right) {
		super(left, right);
	}

	@Override
	public int evaluate() {
		return left.evaluate() - right.evaluate();
	}

}

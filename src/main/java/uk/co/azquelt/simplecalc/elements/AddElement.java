package uk.co.azquelt.simplecalc.elements;

public class AddElement extends AbstractPairElement {

	public AddElement(Element left, Element right) {
		super(left, right);
	}
	
	@Override
	public int evaluate() {
		return left.evaluate() + right.evaluate();
	}

}

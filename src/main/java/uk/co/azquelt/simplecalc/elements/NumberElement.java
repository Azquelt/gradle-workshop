package uk.co.azquelt.simplecalc.elements;

public class NumberElement extends AbstractElement {
	
	private final int value;
	
	public NumberElement(int value) {
		this.value = value;
	}

	@Override
	public int evaluate() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + value;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NumberElement other = (NumberElement) obj;
		if (value != other.value)
			return false;
		return true;
	}

}

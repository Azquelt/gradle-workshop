package uk.co.azquelt.simplecalc.elements;

public abstract class AbstractPairElement extends AbstractElement {

	protected final Element left;
	protected final Element right;
	
	public AbstractPairElement(Element left, Element right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public abstract int evaluate();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (getClass().hashCode());
		result = prime * result + ((left == null) ? 0 : left.hashCode());
		result = prime * result + ((right == null) ? 0 : right.hashCode());
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
		AbstractPairElement other = (AbstractPairElement) obj;
		if (left == null) {
			if (other.left != null)
				return false;
		} else if (!left.equals(other.left))
			return false;
		if (right == null) {
			if (other.right != null)
				return false;
		} else if (!right.equals(other.right))
			return false;
		return true;
	}

}

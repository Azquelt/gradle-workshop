package uk.co.azquelt.simplecalc.elements;

public abstract class AbstractElement implements Element {
	
	/* (non-Javadoc)
	 * @see uk.co.azquelt.simplecalc.elements.Element#evaluate()
	 */
	@Override
	public abstract int evaluate();

	/* (non-Javadoc)
	 * @see uk.co.azquelt.simplecalc.elements.Element#equals(java.lang.Object)
	 */
	@Override
	public abstract boolean equals(Object o);

	/* (non-Javadoc)
	 * @see uk.co.azquelt.simplecalc.elements.Element#hashCode()
	 */
	@Override
	public abstract int hashCode();

}

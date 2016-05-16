package uk.co.azquelt.simplecalc.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static uk.co.azquelt.simplecalc.test.ElementUtils.add;
import static uk.co.azquelt.simplecalc.test.ElementUtils.num;
import static uk.co.azquelt.simplecalc.test.ElementUtils.subtract;

import org.junit.Test;

public class ElementTest {

	@Test
	public void testNumber() {
		assertThat(num(3).evaluate(), is(3));
	}
	
	@Test
	public void testAdd() {
		assertThat(add(num(3), num(4)).evaluate(), is(7));
	}
	
	@Test
	public void testSubtract() {
		assertThat(subtract(num(5), num(2)).evaluate(), is(3));
	}
}

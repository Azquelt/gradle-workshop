package uk.co.azquelt.simplecalc.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static uk.co.azquelt.simplecalc.test.ElementUtils.add;
import static uk.co.azquelt.simplecalc.test.ElementUtils.num;
import static uk.co.azquelt.simplecalc.test.ElementUtils.parse;
import static uk.co.azquelt.simplecalc.test.ElementUtils.subtract;

import org.junit.Test;

public class ParserTest {
	
	@Test
	public void testNumber() {
		assertThat(parse("3"), is(num(3)));
	}
	
	@Test
	public void testAddition() {
		assertThat(parse("3+4"), is(add(num(3), num(4))));
	}
	
	@Test
	public void testSubtraction() {
		assertThat(parse("3-4"), is(subtract(num(3), num(4))));
	}
	
	@Test
	public void testMultiAddition() {
		assertThat(parse("1+2+3"),is(
				add(
						add(num(1), num(2)),
						num(3)
				)
			));
	}
	
	@Test
	public void testMixAddSubtract() {
		assertThat(parse("1+2-3"),is(
				subtract(
						add(num(1), num(2)),
						num(3)
				)
			));
	}

}

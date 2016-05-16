package uk.co.azquelt.simplecalc.itest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import uk.co.azquelt.simplecalc.itest.RunApplication.Result;

public class BasicTests {
	
	@Test
	public void testNumber() {
		Result result = RunApplication.run("3");
		assertThat(result.getReturnCode(), is(0));
		assertThat(result.getOutput().trim(), is("3"));
	}
	
	@Test
	public void testAddition() {
		Result result = RunApplication.run("9+2");
		assertThat(result.getReturnCode(), is(0));
		assertThat(result.getOutput().trim(), is("11"));
	}
	
	@Test
	public void testMultiArg() {
		Result result = RunApplication.run("9", "+", "2");
		assertThat(result.getReturnCode(), is(0));
		assertThat(result.getOutput().trim(), is("11"));
	}

}

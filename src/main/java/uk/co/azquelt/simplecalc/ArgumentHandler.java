package uk.co.azquelt.simplecalc;

public class ArgumentHandler {
	
	public static String connectArguments(String[] args) {
		StringBuilder sb = new StringBuilder();
		for (String arg : args) {
			sb.append(arg);
		}
		return sb.toString();
	}

}

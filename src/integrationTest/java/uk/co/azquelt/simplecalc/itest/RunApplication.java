package uk.co.azquelt.simplecalc.itest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RunApplication {
	
	private static final String APP_NAME = "simple-calculator";
	private static final File APP_DIRECTORY = new File("build/install/simple-calculator/bin");

	public static class Result {
		private String output;
		private String errorOutput;
		private int returnCode;
		public String getOutput() {
			return output;
		}
		public String getErrorOutput() {
			return errorOutput;
		}
		public int getReturnCode() {
			return returnCode;
		}
	};
	
	public static Result run(String... args) {
		try {
			return doRun(args);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	private static Result doRun(String[] args) throws Exception {
		List<String> command = new ArrayList<>();
		command.add(new File(APP_DIRECTORY, APP_NAME).getAbsolutePath());
		command.addAll(Arrays.asList(args));
		
		ProcessBuilder pb = new ProcessBuilder(command);
		pb.environment().put("JAVA_HOME", System.getProperty("java.home"));
		
		Process process = pb.start();
		waitForProcess(process);
		Result result = new Result();
		result.output = readStream(process.getInputStream());
		result.errorOutput = readStream(process.getErrorStream());
		result.returnCode = process.exitValue();
		
		return result;
	}
	
	private static void waitForProcess(Process process) {
		try {
			process.waitFor();
		} catch (InterruptedException ex){}
	}
	
	private static String readStream(InputStream in) throws IOException {
		Reader reader = new InputStreamReader(in);
		char[] buf = new char[1024];
		StringBuilder sb = new StringBuilder();
		for (int i; (i = reader.read(buf)) > -1; ) {
			sb.append(buf, 0, i);
		}
		return sb.toString();
	}

}

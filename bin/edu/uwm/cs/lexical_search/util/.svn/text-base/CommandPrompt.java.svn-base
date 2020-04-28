package edu.uwm.cs.lexical_search.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandPrompt {
	
	private int exitValue;
	private Runtime rt;
	
	public CommandPrompt() {
		rt = Runtime.getRuntime();
	}
	
	//One shot command execution
	public String commandLine (String Command) throws IOException, InterruptedException
	{
		String message = "";
		
        Process pr = rt.exec("cmd /c " + Command);
        BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));

        String line = null;

        while((line = input.readLine()) != null) {
        	message += line + "\n";
        }

        exitValue = pr.waitFor();
        message += "Exited with error code " + exitValue;
        pr.destroy();
	
		return message;
	}
	
	public int getExitValue() {
		return exitValue;
	}
}

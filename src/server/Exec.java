package server;

import java.io.*;

public class Exec{
	public String executeLinuxCmd(String cmd) {
	   //System.out.println("执行命令[ " + cmd + "]");
	   Runtime run = Runtime.getRuntime();
	   try {
	       Process process = run.exec(cmd);  
	       String line;
	       BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	       StringBuffer out = new StringBuffer();
	       while ((line = stdoutReader.readLine()) != null ) {
		    out.append("\n"+line);
		}
		try {
		    process.waitFor();
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		process.destroy();
		return out.toString();
	    } catch (IOException e) {
		 e.printStackTrace();
	    }
		return null;
	 }
}

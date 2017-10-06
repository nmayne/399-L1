/*
 * https://docs.oracle.com/javase/tutorial/networking/sockets/index.html
 * Socket approach thank to Jayden Chan from class.
 * 
 */


package L1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class RemoteControl {

	public static void main(String[] args) {
		 System.out.println("Begin Control: a:left, w:forward, s:reverse, d:right, x:halt.");
	        
	        try (
		            Socket robotSocket = new Socket("10.0.1.1", 6003); // connect to Robot server
		            PrintWriter out =
		                new PrintWriter(robotSocket.getOutputStream(), true);
		            BufferedReader in =
		                new BufferedReader(
		                    new InputStreamReader(robotSocket.getInputStream()));
		            BufferedReader stdIn =
		                new BufferedReader(
		                    new InputStreamReader(System.in))
		        ) {
		            String userInput;
		            System.out.println("USER INPUT:");
		            while ((userInput = stdIn.readLine()) != null) { // get command from user
		                out.println(userInput);	// send command to robot
		            }
		        } catch (UnknownHostException e) {
		            System.err.println("Unknown Robot: 10.0.1.1");
		            System.exit(1);
		        } catch (IOException e) {
		            System.err.println("Robot not ready...");
		            System.exit(1);
		        } 
	}

}

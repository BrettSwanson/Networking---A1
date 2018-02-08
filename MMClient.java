/** Client program for the MM app
 *
 *  @author YOUR FULL NAME GOES HERE
 *
 *  @version CS 391 - Spring 2018 - A1
 **/

import java.io.*;
import java.net.*;

public class MMClient {

    static String hostName = "localhost";  // name of server machine
    static int portNumber = 5555;          // port on which server listens
    static Socket socket = null;           // socket to server
    static DataInputStream in = null;      // input stream from server
    static DataOutputStream out = null;    // output stream to server
    static BufferedReader console = null;  // keyboard input stream


    public static void main(String[] args) {
        String query, reply;
        try {
        	socket = new Socket(hostName, portNumber);
        	System.out.println("Connected to server: " + socket);
        	openStreams();
        	while(true) {
        		reply = in.readUTF();
        		System.out.println(reply);
        		if (reply.equals("    Thank you for playing!")) {
        		    break;
        		}
        		else {
	        		query = console.readLine();
	        		while (query.length() != 4) {
	        			if (query.equalsIgnoreCase("y") ||
	        			    query.equalsIgnoreCase("n")) {
	        				break;
	        			}
	        			System.out.println("    Your guess must be " +
                                "exactly 4 characters long...\n" +
                                "    Guess Again!");
	        			query = console.readLine();
	        		}
        		}
        		out.writeUTF(query.toUpperCase());
        	}
        	close();
        } catch (UnknownHostException e) {
        	System.err.println("Unknown host: " + hostName);
        	System.exit(1);
        } catch (IOException e) {
        	System.err.println("I/O error when connecting to " +
                    hostName);
        	System.exit(1);
        }

    }// main method

    /* open the necessary I/O streams and initializes the in, out, and console
       static variables; this method does not catch any exceptions.
     */
    static void openStreams() throws IOException {
    	in = new DataInputStream(socket.getInputStream());
    	out = new DataOutputStream(socket.getOutputStream());
    	console = new BufferedReader(
    	        new InputStreamReader(System.in)
        );
    }// openStreams method

    /* close ALL open I/O streams and sockets
     */
    static void close() {
    	try {
    		if (console != null) {console.close(); }
    		if (in != null) {in.close(); }
    		if (out != null) {out.close(); }
    		if (socket != null) {socket.close(); }
    	} catch (IOException e) {
    		System.err.println("Error in close(): " + e.getMessage());
    	}

    }// close method
}// MMClient class

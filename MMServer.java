/** Server program for the MM app
 *
 *  @author YOUR FULL NAME GOES HERE
 *
 *  @version CS 391 - Spring 2018 - A1
 **/

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.Arrays;

public class MMServer {

  static ServerSocket serverSocket = null;
  static int portNumber = 5555;
  static Socket clientSocket = null;
  static DataInputStream in = null;
  static DataOutputStream out = null;

  public static void main(String[] args) {
      String request, reply;
      MM mm = null;
      try {
    	  serverSocket = new ServerSocket(portNumber);
    	  System.out.println("Server started: " + serverSocket);
    	  System.out.println("Waiting for a client...");
    	  clientSocket = serverSocket.accept();
    	  System.out.println("Connection established: " +
                  clientSocket);
    	  openStreams();
    	  out.writeUTF("    Type your next guess: \n");
    	  mm = new MM();
    	  while (true) {
    		  request = in.readUTF();
    		  reply = mm.getReply(request);
    		  out.writeUTF(reply);
    		  if (reply.equals(MM.thankYou)) { break; }
    	  }
    	  close();
      } catch (IOException e) {
    	  System.out.println("Server encountered an IO error." +
                  " Shutting  down...");
      }
  }


  static void openStreams() throws IOException {

      in = new DataInputStream(clientSocket.getInputStream());
      out = new DataOutputStream(clientSocket.getOutputStream());

  }


  static void close() {

     try {
    	 if (in != null) {in.close(); }
    	 if (out != null) {out.close();}
    	 if (clientSocket != null) {clientSocket.close(); }
     } catch (IOException e) {
    	 System.err.println("Error in close(): " + e.getMessage());
     }

  }

}


class MM {
  private static final int PLAY = 0;
  private static final int GAMEOVER = 1;
  private int state;
  private String answer;
  private ArrayList<String> trace;
  static String youWin = "    You win!";
  static String nextGuess = "    Type your next guess:";
  static String playAgain = "    Another game? (Y/N)";
  static String thankYou = "    Thank you for playing!";


  MM() {

	  state = PLAY;
	  trace = new ArrayList<String>();
	  answer = pickRandomAnswer();

  }

  private String pickRandomAnswer() {
	  String randomAnswer = "";
      char[] letters = {'A','B','C','D','E','F'};
	  Random rand = new Random();
      for (int i = 0; i < 4; i++) {
    	  int letterSelect = rand.nextInt(6);
    	  randomAnswer += letters[letterSelect];
      }
      System.out.println(randomAnswer);
      return randomAnswer;
  }

private String getFeedback(String guess) {

	  String feedback = "";
	  if (guess.equals(answer)) {
		  feedback = "BBBB";
	  }
	  else  {
		  ArrayList<Integer> nums = new ArrayList<Integer>();
		  char[] copy = answer.toCharArray();
		  for (int i = 0; i < 4; i++) {
			  if (guess.charAt(i) == answer.charAt(i)) {
				  feedback += "B";
				  copy[i] = 'Z';
			  }
			  else {
				  nums.add(i);
			  }
		  }
		  for (int i = 0; i < nums.size(); i++) {
			  for (int j = 0; j < copy.length; j++) {
				  if (guess.charAt(nums.get(i)) == copy[j]) {
					  feedback += "W";
					  break;
				  }
			  }
		  }
	  }
	  char[] sortArray = feedback.toCharArray();
	  Arrays.sort(sortArray);
	  feedback = String.valueOf(sortArray);
      return feedback;
}

  public String getReply(String query) {
	  String reply = null;
	  String line = "==================== \n";
	  switch(state) {
	  case PLAY:
		  if (query.equals("")) {
			  reply = nextGuess;
		  }
		  else {
			  if (getFeedback(query).equals("BBBB")) {
				  reply = youWin + "\n" + playAgain;
				  state = GAMEOVER;
				  break;
			  }
			  trace.add(query);
			  reply = "    " + line;
			  reply += "    Previous Guesses: \n    ";
			  for (int i = 0; i < trace.size(); i++) {
				  reply += trace.get(i) + "  " +
                          getFeedback(trace.get(i)) + "\n    ";
			  }
			  reply += line;
			  reply += nextGuess + "\n";
		  }
		  break;
	  case GAMEOVER:
		  if (query.equalsIgnoreCase("y")) {
			  answer = pickRandomAnswer();
			  state = PLAY;
			  reply =nextGuess;
			  trace = new ArrayList<String>();
		  }
		  else {
			  reply = thankYou;
		  }
		  break;
	  }
      return reply;
  }

}

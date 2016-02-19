import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.text.DefaultCaret;
import javax.swing.JOptionPane; 

public class listenToServer extends Thread{
    //listens for the servers messages and prints them to the client gui notifications textarea
    BufferedReader in;
    PrintWriter out;
    JTextArea notifications;
    mapGUI mapPanel;
    JFrame clientFrame;

    public listenToServer(BufferedReader in, JTextArea notifications, mapGUI mapPanel, JFrame clientFrame){
	//constructor initialises values
	super("listenToServer");
	this.in = in;
	this.notifications = notifications;
	this.mapPanel = mapPanel;
	this.clientFrame = clientFrame; 
    }

    public void run(){
	String serverInput;
	//listens to the clients input stream and deals with the reply
	try{
	    while((serverInput = in.readLine()) != null){
		//sends a look reply to the mapPanel to update
		if ((serverInput.length() > 9) && (serverInput.substring(0,9).equals("LOOKREPLY"))){
		    String lookreply = serverInput.substring(9);
		    mapPanel.revalidate();
		    mapPanel.repaint();
		    mapPanel.setMap(lookreply);
		}
		//opens a dialog box when the game has been won with the relevant lose/win message and awesome gif
		else if ((serverInput.length()> 8) && (serverInput.substring(0,9).equals("GAME OVER"))){
		    if(serverInput.substring(9).equals("YOU WIN")){
			//the gif used for icon here was found on the internet and is not my own.
			final ImageIcon icon = new ImageIcon("Snape.gif");
			JOptionPane.showMessageDialog(clientFrame, "GAME OVER.\n" + serverInput.substring(9) + "\nProgram Will Now Quit.", "You Won", JOptionPane.INFORMATION_MESSAGE, icon);

			}
			else{
			    //the gif used for icon here was found on the internet and is not my own.
			    final ImageIcon icon = new ImageIcon("Dumbledore.gif");
			    JOptionPane.showMessageDialog(clientFrame, "GAME OVER.\n" + serverInput.substring(9) + "\nProgram Will Now Quit.", "You Lost", JOptionPane.INFORMATION_MESSAGE, icon);
			}
		    System.exit(0);
		}
		//otherwise adds reply to notifications text area.
		else{
		    notifications.append(serverInput + "\n");
		    notifications.setCaretPosition(notifications.getDocument().getLength());
		}
			
	    }
	    //if the server disconnects, the code will reach here and the game will quit
	    JOptionPane.showMessageDialog(clientFrame, "Server Disconnected. Program will now quit");
	    System.exit(0);
	}
	catch (IOException ioe){
	    System.exit(0);
	}
    }    
}
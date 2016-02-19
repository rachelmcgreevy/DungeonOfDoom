import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;

public class clientThread extends Thread{
    /*this class creates a new listener for an individual client, listening to the 
      socket on the server side. It then starts a new game with the client, and then
      will listen to the client and send the commands into playgame, then send the 
      corresponding reply back to the client.
    */

    private Socket socket = null;
    public String clientMessage, serverMessage;
    public DODGame game;
    public BufferedReader input;
    public PrintWriter output;
    public Player player;
    public int ID;
    mapGUI gameMap;


    public clientThread(Socket socket, int ID, DODGame game, mapGUI gameMap){
	//sets the socket, game & ID & the i/o streams
	super("clientThread");
	this.socket = socket;
	this.ID = ID;
	this.game = game;
	this.gameMap = gameMap;


	try {
	    input = new BufferedReader(new InputStreamReader(
							  socket.getInputStream()));
	    output = new PrintWriter(socket.getOutputStream(), true);
	} catch (IOException e) {
	    System.out.println(e);
	}
    }

    public void run(){	
	//creates a new playgame with the main game passed in
	PlayGame pg = new PlayGame(game);
	//creates a new player in playgame using the players ID, then sets this class's player value
	setClientPlayer(pg.createNewPlayer(ID));
	//sets the players startlocation then paints the map in the gameMap panel of the server's map
    	game.setRandomStartLocation(player);
	gameMap.setMap(game.getDungeonMap());
	String message;
	try{
	    /*while the client is connected, reads in a line from the input stream and sends message
	      to play game, then prints corresponding message. repaints gameMap panel sends a look to
	      all clients to update their map panels
	     */
	    while ((message = input.readLine()) != null){
		String response = pg.playerCommand(message);
		output.println(response);
		gameMap.revalidate();
		gameMap.repaint();
		gameMap.setMap(game.getDungeonMap());
		game.allLook();
	    }	
	}
	catch (IOException e) {
	    System.exit(0);
	}
    }

    public BufferedReader getInputStream(){
	//returns the servers input stream
	return input;
    }
    
    public PrintWriter getOutputStream(){
	//returns the servers output stream
	return output;
    }

    public void setClientPlayer(Player player){
	//sets the clients player with the player passed in 
	this.player = player;
    }

    public Player getClientPlayer(){
	//returns the clients instantiation of player
	return player;
    }
}
import java.util.Random;
import java.util.Vector;
import java.io.*;
import java.net.*;
import javax.swing.*; 

// Imports all of the static variables from DODMap (i.e. The constants for map objects)


/**
 * This class controls the game logic and other such magic.
 */
public class DODGame {
		
    //public Player player;
	
    private DODMap dodMap;

    private Vector<clientThread> clients;
    private JFrame serverFrame;
    
    
    /**
     * Default Constructor.
     * 
     */
    public DODGame(Vector c, JFrame serverFrame, String mapFilePath){
	clients = c;
	this.serverFrame = serverFrame;
	if (mapFilePath == null){
	    dodMap = new DODMap();
	}
	else {
	    dodMap = new DODMap(mapFilePath);
	}
    }
 
    /**
     sends a lookreply to all clients in the clients list
     */
    public void allLook(){	    
	for(int a = 0; a < clients.size(); a++){
	    clientThread c = clients.get(a);
	    Player p = c.getClientPlayer();
	    String look = clientLook(p);
	    PrintWriter out = c.getOutputStream();
	    out.println(look);
	}
    }

    
    
    /** 
     * Once a player has performed an action the game needs to move onto the next turn 
     * to do this the game needs to check for a win
     * 
     */
    private void advanceTurn(Player player) {
    	// Check if the player has won
    	if ((player.getGold() >= dodMap.getGoal()) && 
    		(dodMap.getMap()[player.getY()][player.getX()] == DODMap.EXIT)) 
    	{
	    //sends a win/lose message to all clients
	    for(int a = 0; a < clients.size(); a++){
		clientThread c = clients.get(a);
		Player p = c.getClientPlayer();
		PrintWriter out = c.getOutputStream();
		if (p == player){
		    out.println("GAME OVERYOU WIN");
		}
		else{
		    out.println("GAME OVERYOU LOSE");
		}
	    }
	    //opens a dialogs box to say the game has been won, then the server will shut on exit
	    JOptionPane.showMessageDialog(serverFrame, "GAME OVER.\nProgram Will Now Quit.");
	    System.exit(0);
	}
    }
    
    
    /**
     * Puts the player in a randomised start location.
     */
    public void setRandomStartLocation(Player player)
    {
    	boolean wall = true;
    	while(wall) {
	    // Generate a random location
	    Random random = new Random();
	    int randomY = random.nextInt(dodMap.getMapHeight());
	    int randomX = random.nextInt(dodMap.getMapWidth());
	    
	    boolean playerOnSquare = false;
	    // checks the squares current players are on and compares to generated squares
	    for(int a = 0; a < clients.size(); a++){
		clientThread c = clients.get(a);
		Player p = c.getClientPlayer();
		if (player != p){
			if((randomX == p.getX()) && (randomY == p.getY())){
			    playerOnSquare = true;
			}
		}
	    }
	    
	    if((dodMap.getMap()[randomY][randomX] != DODMap.WALL) && (playerOnSquare == false)){
		// If it's not a wall or player then we can put them there
		player.setLocation(randomX, randomY);
		wall = false;
	    }
    	}	    	
    }
    
    
    
    /***GAME COMMANDS AND LOGIC***/
    

    /**
     *  Handles the client message HELLO
     *  
     */
    public String clientHello(String newName, Player player) 
    {
    	// Change the player name and then say hello to them
		player.setName(newName);
		return "HELLO " + newName + "\nGOAL " + dodMap.getGoal();
    }

    public String clientQuit(Player player) {
	//sets the player as dead, and removes them from the client list
	player.setDead(true);	
	for(int a = 0; a < clients.size(); a++){
	    clientThread c = clients.get(a);
	    Player p = c.getClientPlayer();
	    if (p == player){
		clients.remove(a);
		break;
	    }
	}
	return "";
    } 
    
    /**
     * Handles the client message LOOK
     * Shows the portion of the map that the player can currently see.
     * 
     * @return The part of the map that the player can currently see.
     */
    public String clientLook(Player player) 
    {
    	String message = "LOOKREPLY";
    	
    	// Work out how far the player can see
	    int distance = 3;
	    
	    // Iterate through the rows.
	    for (int i = -distance; i <= distance; ++i) 
	    {
	    	String line = "";
			
	    	// Iterate through the columns.
			for (int j = -distance; j <= distance; ++j) 
			{

			    char content = '?';
			    
			    // Work out which location is next.
			    int targetX = player.getX() + j;
			    int targetY = player.getY() + i;

			    // Work out what is in the square field of vision.
			    if (player.getLantern() == 0 && (Math.abs(j) == 3 || Math.abs(i) == 3)){
				content = 'X';
			    }
			    else if (Math.abs(i) + Math.abs(j) > distance + player.getLantern()) 
			    {
				// It's outside the FoV so we don't know what it is.
				content = 'X';
			    } 
			    else if ((targetX < 0) || (targetX >= dodMap.getMapWidth()) ||
				         (targetY < 0) || (targetY >= dodMap.getMapHeight())) 
			    {	
			    	// It's outside the map, so just call it a wall.
			    	content = '#';
			    } 
			    else 
			    {
				        
				switch (dodMap.getMap()[targetY][targetX]){
				case DODMap.EMPTY: 		content = '.'; break;
				case DODMap.HEALTH: 	content = 'H'; break;
				case DODMap.LANTERN: 	content = 'L'; break;
				case DODMap.SWORD: 		content = 'S'; break;
				case DODMap.ARMOUR: 	content = 'A'; break;
				case DODMap.EXIT: 		content = 'E'; break;
				case DODMap.WALL: 		content = '#'; break;
				case DODMap.GOLD:		content = 'G'; break;
				}

				// if the current square is where a player is, changes content
				clientThread c;
				Player p;
				    
				for(int a = 0; a < clients.size(); a++){
				    c = clients.get(a);
				    p = c.getClientPlayer();
				    if ((targetX == p.getX()) && (targetY == p.getY())){
					content = 'P';
				    }
				}
				
			    
				
			    }
			    // Add to the line
			    line += content;
			}
			
			// Send a line of the look message
			message += line;
	    }
	    
	    advanceTurn(player);
	    
	    return message;
    }

    public String getDungeonMap(){
	//returns the whole dungeon map for the server
	String message = "";
	//Iterate through the rows
	for (int i = 0; i < dodMap.getMapHeight(); ++i) {
	       	String line = "";
			
	    	// Iterate through the columns.
		for (int j = 0; j < dodMap.getMapWidth(); ++j) {

		    char content = '?';
			    
		    // Work out which location is next.
		    int targetX = j;
		    int targetY = i;
		            
		    switch (dodMap.getMap()[targetY][targetX]){
		    case DODMap.EMPTY: 		content = '.'; break;
		    case DODMap.HEALTH: 	content = 'H'; break;
		    case DODMap.LANTERN: 	content = 'L'; break;
		    case DODMap.SWORD: 		content = 'S'; break;
		    case DODMap.ARMOUR: 	content = 'A'; break;
		    case DODMap.EXIT: 		content = 'E'; break;
		    case DODMap.WALL: 		content = '#'; break;
		    case DODMap.GOLD:		content = 'G'; break;
		    }
		    
		    //checks if player is on this square
		    clientThread c;
		    Player p; 

		    for(int a = 0; a < clients.size(); a++){
			c = clients.get(a);
			p = c.getClientPlayer();
			if ((targetX == p.getX()) && (targetY == p.getY())){
			    content = 'P';
			}
		    }
		    
		    // Add to the line
		    line += content;
		}
		
		// Send a line of the look message
	    	message += line;
	}
	
	return message;	
	
    }
    
    
    /**
     * Returns the current message to the client. Note that this becomes important when using
     * multiple clients across a network.
     * 
     * @param message 	The message to be shouted
     * @return			The message
     */
    public String clientShout(String message, Player player) 
    {	
	PrintWriter output = null;
	for (int i = 0; i < clients.size(); i++) {
	    //gets the i'th client from the list of clients, assigns the output stream and sends the message to this stream
	    clientThread a = clients.get(i);
	    Player p = a.getClientPlayer();
	    if (p != player){
		output = a.getOutputStream();
		output.println(message);
	    }
	}
	return message;
    }
    
    
    /**
     * Handles the client message PICKUP.
     * Generally it decrements AP, and gives the player the item that they picked up
     * Also removes the item from the map
     * 
     * @return A message indicating the success or failure of the action of picking up.
     */
    public String clientPickup(Player player) 
    {
	String failMessage = "FAIL: ";

	// Check that there is something to pick up
	switch (dodMap.getMap()[player.getY()][player.getX()]) {
	case DODMap.EXIT : // Can't pick up the exit

	case DODMap.EMPTY : // Nothing to pick up
	    failMessage += "Nothing to pick up";
	    break;
			
	case DODMap.HEALTH :
	    
	    // Remove from the map
	    dodMap.setMapCell(player.getY(), player.getX(), DODMap.EMPTY);
	    
	    // Add one to health...
	    player.incrementHealth();
			
	    // ... notify the client ...
	    advanceTurn(player);
	    return "SUCCESS:  +1 HP \n Total HP = " + player.getHp();
	    
	case DODMap.LANTERN :
	    
	    // Can pick up if we don't have one
	    if (player.getLantern() == 0) 
		{		    
		    // Remove from the map
		    dodMap.setMapCell(player.getY(), player.getX(), DODMap.EMPTY);
		    
		    // ... give them a lantern ...
		    player.setLantern(1);
		    advanceTurn(player);
		    return "SUCCESS: Got Lantern";
		} 
	    else 
		{
		    failMessage += "Already have a lantern";
		}
	    break;
	    
	case DODMap.SWORD :
	    
	    // Does almost exactly the same thing as picking up a lantern		
	    if (player.getSword() == 0) 
		{		    
		    dodMap.setMapCell(player.getY(), player.getX(), DODMap.EMPTY);
		    
		    player.setSword(1);
		    advanceTurn(player);
		    return "SUCCESS: Got Sword";
		} 
	    else 
		{
		    failMessage += "Already have a sword";
		}
	    break;
	    
	case DODMap.ARMOUR :
	    
	    // Similar again
	    if (player.getArmour() == 0) 
		{
		    
		    dodMap.setMapCell(player.getY(), player.getX(), DODMap.EMPTY);
		    
		    player.setArmour(1);
		    advanceTurn(player);
		    return "SUCCESS: Got Armour";
		} 
	    else 
		{
		    failMessage += "Already have armour";
		}
	    break;
	    
	case DODMap.GOLD:
	    	    
	    // Remove from the map
	    dodMap.setMapCell(player.getY(), player.getX(), DODMap.EMPTY);
	    
	    // Add to the amount of treasure
	    player.addGold(1); 
	    
	    advanceTurn(player);
	    return "SUCCESS: + 1 Gold. Total Gold = " + player.getGold();   
	    
	default:
	    
	    // This shouldn't happen
	    System.err.println("Pickup at strange map location : [" + player.getY() + "][" + player.getX() + "] = " + dodMap.getMap()[player.getY()][player.getX()]);
	    System.exit(1);
	}

	// Fail unless the process explicitly succeeded
	advanceTurn(player);
	return failMessage;
    }
    
    
    /**
     * Handles the client message MOVE
     * 
     * Move the player in the specified direction - assuming there isn't a wall in the way
     * 
     * @param direction The direction (NESW) to move the player
     * @return			An indicator of the success or failure of the movement.
     */
    public String clientMove(char direction, Player player) 
    {
	String failMessage = "FAIL \n";
	
	// Work out where the move would take the player
	int targetX = player.getX();
	int targetY = player.getY();
	
	switch (direction) {
	case 'N' : --targetY; break;
	case 'S' : ++targetY; break;
	case 'E' : ++targetX; break;
	case 'W' : --targetX; break;
	    
	default :  // Shouldn't happen
	    System.err.println("Internal error in connection base.");
	    System.err.println("'" + direction + "' is not a direction.");
	    System.exit(1);
	}
	
	// Ensure that the movement is within the bounds of the map
	if ((targetX >= 0) && (targetX < dodMap.getMapWidth()) && (targetY >= 0) && (targetY < dodMap.getMapHeight())) {
	    
	    // The move must not be into a wall or player
	    // checks a player is not already on the square
	    boolean playerOnSquare = false;

	    for(int a = 0; a < clients.size(); a++){
		clientThread c = clients.get(a);
		Player p = c.getClientPlayer();
		if ((targetX == p.getX()) && (targetY == p.getY())){
		    playerOnSquare = true;
		}
	    }
	    

	    if ((dodMap.getMap()[targetY][targetX] != DODMap.WALL) && (playerOnSquare == false)) 
		{
		    // Move the player
		    player.setX(targetX);
		    player.setY(targetY);
		    
		    // Notify the client of the success
		    advanceTurn(player);
		    return "SUCCESS";
		} 
	    else 
		{
		    failMessage += "Can't move into a wall/player";
		}
	} 
	else 
	    {
		// Needs to be the same as above or otherwise people will know where
		// the edges of the dungeon are.
		failMessage += "Can't move into a wall";			
	    }
	
	// Fail unless there is an explicit reason why we succeed
	advanceTurn(player);
	return failMessage;
    }
        
    /**
     * Passes the goal back (for the bot)
     * 
     * @return The current goal
     `    */
    public int getGoal(){
	//returns gold number needed to win
    	return dodMap.getGoal();
    }

    public int getMapHeight(){
	//returns map's height
	return dodMap.getMapHeight();
    }

    public int getMapWidth(){
	//returns map's width
	return dodMap.getMapWidth();
    }

}
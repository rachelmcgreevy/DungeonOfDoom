/**
 * Class representing a player
 * 
 * Note: Player variables should be kept as a part of the server functionality in 
 * your coursework implementation, they are the game's internal representation of
 * a player, not used by the client side code.
 */
public class Player 
{	
	// Unless they say otherwise, we'll call all of the clients Dave
	private String name = "Dave";
	
	// Location on the map
	private int x = -1;
	private int y = -1;
	
	// Objects (1 if they have them, 0 if they don't)
	// Note that these are ints because of the AP calculation
	private int lantern = 0;
	private int sword = 0;
	private int armour = 0;
	
	// How much gold they have
	private int gold = 0;
	
	// If they are dead
	private boolean dead = false;
	
	// Player attribute value things
	private int hp = 3;
	
	
	/**
	 * Default constructor for players
	 */
	public Player()
	{
		// Doesn't need to do anything because everything is already initialised to default values
	}
	
	
	/**
	 * Constructor for player with lots of arguments
	 * 
	 * @param hp		How much HP they start with
	 * @param lantern	Whether they have a lantern
	 * @param sword		Whether they have a sword
	 * @param armour	Whether they have armour
	 * @param gold 		How much gold they start with
	 * @param dead		Whether they are dead
	 */
	public Player(int hp, int lantern, int sword, int armour, int gold, boolean dead)
	{
		this.hp = hp;
        
		// Initialise their location to something stupid
		this.x = -1;
		this.y = -1;
		
		this.sword = sword;
        this.armour = armour;
        this.gold = gold;
        this.dead = dead;
	}

	
	/**
	 * Returns the name of the player
	 * 
	 * @return The name of the player
	 */
	public String getName() 
	{
		return name;
	}

	
	/**
	 * Sets the name of the player
	 * 
	 * @param name The new name of the player
	 */
	public void setName(String name) 
	{
		this.name = name;
	}
	
	
	/**
	 * Sets the location of the player
	 * 
	 * @param x	The new x-coordinate of the player
	 * @param y The new y-coordinate of the player
	 */
	public void setLocation(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	
	/**
	 * Returns the x-coordinate of the player's location
	 * 
	 * @return The x-coordinate of the player's location
	 */
	public int getX() 
	{
		return x;
	}


	/**
	 * Sets the x-coordinate of the player's location
	 * 
	 * @param x The new x-coordinate of the player's location
	 */
	public void setX(int x) 
	{
		this.x = x;
	}

	
	/**
	 * Returns the y-coordinate of the player's location
	 * 
	 * @return The y-coordinate of the player's location
	 */
	public int getY() 
	{
		return y;
	}

	
	/**
	 * Sets the y-coordinate of the player's location
	 * 
	 * @param y The new y-coordinate of the player's location
	 */
	public void setY(int y) 
	{
		this.y = y;
	}

	
	/**
	 * Returns the status of the player's lantern
	 *  
	 * @return 1 if they have a lantern, 0 otherwise
	 */
	public int getLantern() 
	{
		return lantern;
	}

	
	/**
	 * Sets the status of the player's lantern
	 * 
	 * @param lantern The new value of the lantern
	 */
	public void setLantern(int lantern) 
	{
		this.lantern = lantern;
	}
	
	
	/**
	 * Returns whether the player has a lantern or not
	 * 
	 * @return Whether the player has a lantern or not
	 */
	public boolean hasLantern()
	{
		return this.lantern == 1;
	}

	
	/**
	 * Returns the value of the player's sword
	 * 
	 * @return 1 if they have a sword, 0 otherwise
	 */
	public int getSword() 
	{
		return sword;
	}

	
	/**
	 * Sets the value of the player's sword
	 * 
	 * @param sword The new value of the player's sword
	 */
	public void setSword(int sword) 
	{
		this.sword = sword;
	}
	
	
	/**
	 * Returns whether the player has a sword or not
	 * 
	 * @return Whether the player has a sword
	 */
	public boolean hasSword()
	{
		return this.sword == 1;
	}

	
	/**
	 * Returns the value of the player's armour
	 * 
	 * @return 1 if they have armour, 0 otherwise
	 */
	public int getArmour() 
	{
		return armour;
	}

	
	/**
	 * Sets the value of the player's armour
	 * 
	 * @param armour The new value of the player's armour
	 */
	public void setArmour(int armour) 
	{
		this.armour = armour;
	}
	
	
	/**
	 * Returns whether the player has armour or not
	 * 
	 * @return Whether the player has armour
	 */
	public boolean hasArmour()
	{
		return this.armour == 1;
	}

	
	/**
	 * Returns the amount of gold the player has
	 * 
	 * @return The amount of gold the player has
	 */
	public int getGold() 
	{
		return gold;
	}

	
	/**
	 * Sets the amount of gold the player has
	 * 
	 * @param gold The new amount of gold the player has
	 */
	public void setGold(int gold) 
	{
		this.gold = gold;
	}
	
	
	/**
	 * Adds more gold to the amount of gold the player already has
	 * 
	 * @param gold The amount of gold to add
	 */
	public void addGold(int gold)
	{
		this.gold += gold;
	}

	
	/**
	 * Returns the player's liveness
	 * 
	 * @return true if the player is dead, false otherwise
	 */
	public boolean isDead() 
	{
		return dead;
	}

	
	/**
	 * Sets the player's liveness
	 * 
	 * @param dead true if the player is dead, false otherwise
	 */
	public void setDead(boolean dead) 
	{
		this.dead = dead;
	}

	
	/**
	 * Returns the amount of HP the player has
	 * 
	 * @return The amount of HP the player has
	 */
	public int getHp() 
	{
		return hp;
	}

	
	/**
	 * Sets the amount of HP the player has
	 * 
	 * @param hp The new amount of HP the player has
	 */
	public void setHp(int hp) 
	{
		this.hp = hp;
	}
	
	
	/**
	 * Adds one to the player's HP
	 */
	public void incrementHealth()
	{
		this.hp++;
	}

}
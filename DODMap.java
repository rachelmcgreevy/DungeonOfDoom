import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class containing the map used by the game engine. Allows for reading in ASCII art maps from
 * a file, or just using the default map.
 * 
 * You are not responsible for making this more robust - you are only required to perform error checking
 * on the extra code that you write to network the client and server.
 */
public class DODMap {   
    // Each of these is just the value of the ASCII character in the ASCII art map
    public static final char GOLD     =  'G';
    public static final char EMPTY    =  '.';
    public static final char HEALTH   =  'H';
    public static final char LANTERN  =  'L';
    public static final char SWORD    =  'S';
    public static final char ARMOUR   =  'A';
    public static final char EXIT     =  'E';
    public static final char WALL     =  '#';
	
	// Dimensions of the map
    private int mapWidth = 20;
    private int mapHeight = 20;
    
    // Map object (initialised to some default)
    private char map[][] = new char[][]
	    { 
	        {WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL},
	    	{WALL, EMPTY, EMPTY, EMPTY, HEALTH, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, WALL, EMPTY, EMPTY, EMPTY, HEALTH, EMPTY, EMPTY, EMPTY, EMPTY, WALL},
	    	{WALL, EMPTY, WALL, WALL, WALL, EMPTY, WALL, GOLD, WALL, WALL, WALL, EMPTY, WALL, WALL, WALL, WALL, WALL, GOLD, EMPTY, WALL},
	    	{WALL, EMPTY, WALL, EMPTY, EMPTY, EMPTY, WALL, EMPTY, WALL, WALL, WALL, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, WALL, WALL, EMPTY, WALL},
            {WALL, EMPTY, LANTERN, EMPTY, SWORD, EMPTY, WALL, EMPTY, WALL, WALL, WALL, EMPTY, EMPTY, WALL, EMPTY, EMPTY, WALL, EMPTY, EMPTY, WALL},
            {WALL, EMPTY, WALL, WALL, WALL, EMPTY, WALL, EMPTY, WALL, EMPTY, EMPTY, EMPTY, WALL, EMPTY, WALL, EMPTY, WALL, EMPTY, WALL, WALL},
            {WALL, EMPTY, EMPTY, ARMOUR, EMPTY, EMPTY, WALL, EMPTY, EMPTY, WALL, WALL, EMPTY, WALL, WALL, WALL, EMPTY, WALL, EMPTY, EMPTY, WALL},
	    	{WALL, EMPTY, WALL, WALL, WALL, WALL, WALL, GOLD, WALL, WALL, WALL, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, WALL, WALL, EMPTY, WALL},
            {WALL, EMPTY, WALL, WALL, WALL, EMPTY, WALL, EMPTY, EMPTY, EMPTY, WALL, EMPTY, WALL, WALL, WALL, EMPTY, WALL, EMPTY, EMPTY, WALL},
            {WALL, EMPTY, EMPTY, EMPTY, HEALTH, EMPTY, EMPTY, EMPTY, EMPTY, WALL, WALL, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, WALL, WALL, EMPTY, WALL},
            {WALL, EMPTY, WALL, WALL, WALL, EMPTY, WALL, EMPTY, WALL, WALL, WALL, EMPTY, WALL, WALL, WALL, EMPTY, WALL, WALL, EMPTY, WALL},
            {WALL, WALL, WALL, WALL, WALL, EMPTY, WALL, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, WALL, WALL, WALL, EMPTY, WALL, WALL, EMPTY, WALL},
            {WALL, EMPTY, LANTERN, EMPTY, SWORD, EMPTY, WALL, EMPTY, WALL, EMPTY, WALL, EMPTY, WALL, WALL, WALL, EMPTY, WALL, EMPTY, EMPTY, WALL},
            {WALL, EMPTY, WALL, WALL, WALL, EMPTY, WALL, EMPTY, WALL, EMPTY, WALL, EMPTY, WALL, WALL, EMPTY, EMPTY, WALL, EMPTY, EMPTY, WALL},
            {WALL, EMPTY, WALL, ARMOUR, EMPTY, EMPTY, WALL, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, WALL, EMPTY, WALL, EMPTY, WALL, WALL, EMPTY, WALL},
            {WALL, EMPTY, WALL, EMPTY, WALL, EMPTY, WALL, EMPTY, WALL, EMPTY, WALL, EMPTY, WALL, WALL, WALL, EMPTY, WALL, EMPTY, EMPTY, WALL},
            {WALL, EMPTY, EMPTY, EMPTY, WALL, EMPTY, WALL, GOLD, WALL, EMPTY, WALL, EMPTY, EMPTY, EMPTY, WALL, EMPTY, WALL, WALL, EMPTY, WALL},
            {WALL, EMPTY, WALL, WALL, WALL, EMPTY, EMPTY, GOLD, WALL, HEALTH, WALL, EMPTY, WALL, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, WALL},
            {WALL, EMPTY, WALL, WALL, WALL, EMPTY, EMPTY, EMPTY, WALL, EMPTY, WALL, EMPTY, WALL, EMPTY, WALL, EMPTY, WALL, WALL, EMPTY, WALL},
            {WALL, WALL, WALL, WALL, WALL, EXIT, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL}
	    };
    
    // Other info about the map
    private int goal = 2;
    private String name = "Default Dungeon of Doooom";
    
    
    /**
     * Creates a default map
     */
    public DODMap()
    {	
    	// We don't need to do anything here because the default stuff has already been set.
    }
    
    
    /**
     * Creates a map from the file specified. Note that this is not robust...
     * 
     * @param filename The name of the file to load the map from
     */
    public DODMap(String filename)
    {
    	ArrayList<String> lines = readFile(filename);
    	
    	// Good programmers always check this...
    	if(lines == null) {
	    // The default stuff is already set, so just use that
	    return;
	}
    	
    	// The first line should always be the name of the map.
    	String first = lines.get(0);
    	String tempName = "";
    	if(first.substring(0, first.indexOf(" ")).equals("name")) {
    		tempName = first.substring(first.indexOf(" ") + 1);
	    }
    	
    	// The second line should be the goal.
    	String second = lines.get(1);
    	int tempGoal = 0;
    	if(second.substring(0, second.indexOf(" ")).equals("win"))
    	{
    		tempGoal = Integer.parseInt(second.substring(second.indexOf(" ") + 1));
    	}
    	
    	// Read the rest of the map
    	int tempMapWidth = lines.get(2).length();
    	int tempMapHeight = lines.size() - 2;
    	
    	// Reset map so that it is the right size
    	char[][] tempMap = new char[tempMapHeight][tempMapWidth];
    	
    	int goldCount = 0;
    	
    	for(int i = 0; i < lines.size() - 2; i++)
    	{
	    String line = lines.get(i + 2);
    		
	    // Make sure the input map is sensible
	    if(!line.matches("[AEGHLS.#]*"))
    		{
		    return;
    		}
	    
	    for(int j = 0; j < line.length(); j++)
    		{
		    // Just use the character representation in the input file.
		    tempMap[i][j] = line.charAt(j);
		    
		    if(line.charAt(j) == 'G')
			goldCount++;
    		}
    	}
    	
    	if(goldCount < tempGoal){
	}
	else{
	    // Set the class variables to be the temporary ones
	    map = tempMap;
	    mapHeight = tempMapHeight;
	    mapWidth = tempMapWidth;
	    name = tempName;
    	}
    }
    
    
    /**
     * Reads in a file and returns an arraylist of Strings.
     * This makes life slightly easier in working out how big the 2D char array needs to be
     * (as well as the name, goal, etc)
     * 
     * @param filename 	The name of the file to read the map from
     * @return			An ArrayList of lines in the file
     */
    private ArrayList<String> readFile(String filename)
    {
    	// So that we have a reference to it in the 
    	BufferedReader reader;
    	
    	try 
	    {
    		// Try to open the file
		reader = new BufferedReader(new FileReader(filename));
	    }
    	catch (FileNotFoundException e) 
	    {
		return null;
	    }
    	
    	ArrayList<String> lines = new ArrayList<String>();
    	
    	try
	    {
    		// Try to read a line of the file at a time, and then store it
    		String line = "";
	    	while((line = reader.readLine()) != null)
		    {
	    		// Don't bother with blank lines
	    		if(!line.equals("")) {
			    lines.add(line);
	    		}
		    }
	    }
    	catch(IOException e)
	    {
		return null;
	    }
    	
    	return lines;
    }
    
    
    /**
     * Returns the width of the map
     * 
     * @return The width of the map
     */
    public int getMapWidth() 
    {
	return mapWidth;
    }
    
    
    /**
     * Set the width of the map
     * 
     * @param mapWidth The new width of the map
     */
    public void setMapWidth(int mapWidth) 
    {
	this.mapWidth = mapWidth;
    }
    
    
    /**
     * Returns the height of the map
     * 
     * @return The height of the map
     */
    public int getMapHeight() 
    {
	return mapHeight;
    }
    
    
	/**
	 * Set the height of the map
	 * 
	 * @param mapHeight The new height of the map
	 */
    public void setMapHeight(int mapHeight) 
    {
	this.mapHeight = mapHeight;
    }
    
    
    /**
     * Returns the map
     * 
     * @return The map
     */
    public char[][] getMap() 
    {
	return map;
    }
    
    
    /**
     * Overwrite the entire map
     * 
     * @param map The new map to use
     */
    public void setMap(char[][] map) 
    {
	this.map = map;
    }
    
    
    /**
     * Sets the contents of a particular cell in the map
     * 
     * @param y 		The x-coordinate of the cell to be changed
     * @param x			The y-coordinate of the cell to be changed
     * @param contents	The new contents of the cell
     */
    public void setMapCell(int y, int x, char contents)
    {
	this.map[y][x] = contents;
    }
    
    
    /**
     * Returns the amount of gold required to win on this map
     * 
     * @return The amount of gold required to win on this map
	 */
    public int getGoal() 
    {
	return goal;
    }
    
    
    /**
     * Sets the amount of gold required to win on this map
     * 
     * @param goal The new amount of gold required to win on this map
     */
    public void setGoal(int goal) 
    {
	this.goal = goal;
    }
    
    
    /**
     * Returns the name of the map
     * 
     * @return The name of the map
     */
    public String getName() 
    {
	return name;
    }
    
    
    /**
     * Sets the new name of this map
     * 
     * @param name The new name of this map
     */
    public void setName(String name) 
    {
	this.name = name;
    }
}

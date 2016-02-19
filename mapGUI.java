import javax.swing.*;
import javax.swing.ImageIcon;
import java.awt.*;

public class mapGUI extends JPanel {
    /* initialises a an empty map and paints to the panel, then creates a new
       map grid based on the passed in look message when method setMap is called
     */
    
    public int rowNum;
    public int columnNum;

    public static final int cellSize = 30;

    private ImageIcon[][] mapGrid;

    //the pictures used for all of the imageicons here were found on the internet and modified, and are not my own.
    ImageIcon PLAYER = new ImageIcon(this.getClass().getResource("Player.jpg"));
    ImageIcon FLOOR = new ImageIcon(this.getClass().getResource("Floor.jpg"));
    ImageIcon WALL = new ImageIcon(this.getClass().getResource("Wall.jpg"));
    ImageIcon GOLD = new ImageIcon(this.getClass().getResource("Gold.jpg"));
    ImageIcon ARMOUR = new ImageIcon(this.getClass().getResource("Armour.jpg"));
    ImageIcon LANTERN = new ImageIcon(this.getClass().getResource("Lantern.jpg"));
    ImageIcon SWORD = new ImageIcon(this.getClass().getResource("Sword.jpg"));
    ImageIcon EMPTY = new ImageIcon(this.getClass().getResource("Empty.jpg"));
    ImageIcon EXIT = new ImageIcon(this.getClass().getResource("Exit.jpg"));
    ImageIcon HEALTH = new ImageIcon(this.getClass().getResource("Health.jpg"));

    //creates a list of image icons
    public ImageIcon[] icon= {
	EMPTY,
        PLAYER,
	FLOOR,
	WALL,
	GOLD,
	ARMOUR,
	LANTERN,
	SWORD,
	EXIT,
	HEALTH
    };

    public mapGUI(int rowNum, int columnNum){
	//goes through imageicon 2d array and sets as the EMPTY icon
	this.rowNum = rowNum;
	this.columnNum = columnNum;
        this.mapGrid = new ImageIcon[rowNum][columnNum];
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < columnNum; j++) {
                this.mapGrid[i][j] = icon[0];
            }
        }
	int Width = columnNum * cellSize;
        int Height =  rowNum * cellSize;
        setPreferredSize(new Dimension(Width, Height));
	setVisible(true);
    }
    
    @Override
    public void paintComponent(Graphics g) {
	//paints the image icons onto the mapGUI panel
	super.paintComponent(g);
	ImageIcon cell;
	g.clearRect(0, 0, getWidth(), getHeight());
	int Width = getWidth() / columnNum;
	int Height = getHeight() / rowNum;
	for (int i = 0; i < rowNum; i++) {
	    for (int j = 0; j < columnNum; j++) {
		// 
		int x = i * Width;
		int y = j * Height;
		cell = mapGrid[i][j];
		if(cell != null){
		    cell.paintIcon(this, g, y, x);
		}
	    }
	}
    }

    public void setMap(String dungeon){
	//uses the lookreply message that is passed in and sets the mapGrid
	//icons to the corresponding lookreply letters
        this.mapGrid = new ImageIcon[rowNum][columnNum];
	int count = 0;
	ImageIcon cellType;
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < columnNum; j++) {
		try{
		    if (dungeon.charAt(count) == 'X'){
			cellType = icon[0];
		    }
		    else if (dungeon.charAt(count) == 'P'){
			cellType = icon[1];
		    }
		    else if (dungeon.charAt(count) == '.'){
			cellType = icon[2];
		    }
		    else if (dungeon.charAt(count) == '#'){
			cellType = icon[3];
		    }
		    else if (dungeon.charAt(count) == 'G'){
			cellType = icon[4];
		    }
		    else if (dungeon.charAt(count) == 'A'){
			cellType = icon[5];
		    }
		    else if (dungeon.charAt(count) == 'L'){
			cellType = icon[6];
		    }
		    else if (dungeon.charAt(count) == 'S'){
			cellType = icon[7];
		    }
		    else if (dungeon.charAt(count) == 'E'){
			cellType = icon[8];
		    }
		    else if (dungeon.charAt(count) == 'H'){
			cellType = icon[9];
		    }
		    else{
			cellType = icon[0];
		    }
		    
		}
		catch(Exception e){
		    cellType = icon[0];
		}
                this.mapGrid[i][j] = cellType;
		count += 1;
            }
        }

	int Width = columnNum * cellSize;
        int Height =  rowNum * cellSize;
        setPreferredSize(new Dimension(Width, Height));
	setVisible(true);
    }
  
}


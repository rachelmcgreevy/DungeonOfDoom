import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.text.DefaultCaret;

public class clientGUI extends JFrame{

    /*
      This class creates the main client window, with a panel for game chat/notifications
      buttons for moving, picking up and exiting, and a main graphics panel.
    */

    PrintWriter out;
    BufferedReader in;
    mapGUI mapPanel;
    JTextField chatLine;
    String ID;

    public clientGUI(PrintWriter out, BufferedReader in, String ID){
	/* 
	This constructor creates the main client GUI window
	inherits from JFrame, sets parameter values  and sets 
	the frame's title, size, re-sizability & location.
	*/

	super("Dungeon of Doom");
	this.out = out;
	this.in = in;
	this.ID = ID;
	setSize(800,300);
	setVisible(true);
	setResizable(false);
	setLocationRelativeTo(null);

	/*
	  creates the map panel, which will display the lookreply to the user via 
	  image icons. will always be 7x7, lookreply in DODGame is modified to 
	  calculate look with or without lantern on 7x7 grid
	*/
	
	mapPanel = new mapGUI(7,7);

	/* creates a gridlayout for the move buttons, enabling the NESW buttons and disabling 
	   the red. A new listener is added and a assigned to the button to check when it is 
	   pressed. The button is then added to the control panel.
	*/

	GridLayout directionGrid = new GridLayout(3,2);
	
	JPanel controlPanel = new JPanel();
	controlPanel.setLayout(directionGrid);
	
	String directions = "x^x<x>xvx";
	ActionListener buttonlistener  = new buttonListener();

	for (int i = 0; i < directions.length(); i++){
	    String d = directions.substring(i,i+1);
	    JButton dbutton = new JButton(d);
	    if(d.equals("x")){
		dbutton.setEnabled(false);
	    }
	    else{
		dbutton.addActionListener(buttonlistener);
	    }
	    controlPanel.add(dbutton);
	}

	/* A list of buttons needed is created, then for each item in the list, a command button
	   is created, and an action listener is assigned to it, then the button is added to the
	   command panel
	*/


	String[] cList = new String[] {"PICKUP","QUIT"};
	JPanel commandPanel = new JPanel();

	for (String command : cList){
		JButton commandButton = new JButton(command);
		commandButton.addActionListener(buttonlistener);
		commandPanel.add(commandButton);
	}

	/* A text area for chat messages/game notifications is created, and a vertical
	   scroll bar is added and will always show. A textbox and send button are also
	   added to send shout messages which will show in the chat text area. All are 
	   then added to a chatPanel
	 */

	JTextArea chatText = new JTextArea(10,35);
	chatText.setEditable(false);
	JScrollPane sPane = new JScrollPane (chatText);
	sPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

	chatLine = new JTextField(25);

	JButton sendChat = new JButton("SEND");
	sendChat.addActionListener(buttonlistener);

	JPanel chatPanel = new JPanel();

	BorderLayout b1 = new BorderLayout();
	chatPanel.setLayout(b1);
	
	chatPanel.add(sPane, BorderLayout.NORTH);
	chatPanel.add(chatLine);
	chatPanel.add(sendChat, BorderLayout.EAST);

	//panel of all panels is created, then added to the main frame
	JPanel DODPanel = new JPanel();

	BorderLayout b2 = new BorderLayout();
	DODPanel.setLayout(b2);
	DODPanel.add(mapPanel, BorderLayout.EAST);
	DODPanel.add(commandPanel, BorderLayout.SOUTH);
	DODPanel.add(controlPanel);
	DODPanel.add(chatPanel, BorderLayout.WEST);

	add(DODPanel);
	//frame packed to smallest size needed
	pack();
	//disables x button so player must use quit to disconnect from game
	setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	//starts new listentoserver thread to listen to the input stream of the socket
	new listenToServer(in, chatText, mapPanel, this).start();
	//prints look to the output stream which goes on to initialise the map in the clients frame
	out.println("LOOK");
		
    }

    public class buttonListener implements ActionListener{
	/* When any button  being listened to is pressed, this class checks which button has
	   been pressed, and sends the relevant command to the output stream for the server
	   to recieve. Look is called at the end to update clients map graphic.
	*/
	public void actionPerformed(ActionEvent event){
	    String com = event.getActionCommand();
	    if (com.equals("^")){
		out.println("MOVE N");
	    }
	    else if (com.equals(">")){
		out.println("MOVE E");
	    }
	    
	    else if (com.equals("<")){
		out.println("MOVE W");
	    }
	    
	    else if (com.equals("v")){
		out.println("MOVE S");
	    }
	    
	    else if (com.equals("PICKUP")){
		out.println("PICKUP");
	    }
	    else if (com.equals("SEND")){
		out.println("SHOUT "+ ID + ": " + chatLine.getText());
		chatLine.setText("");
	    }
	    else {
		out.println("QUIT");
		System.exit(0);
	    }
	    out.println("LOOK");
	    
	}
	
    }
    
}


    
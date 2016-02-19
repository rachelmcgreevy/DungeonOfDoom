import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.awt.event.*;

public class myServer{

    int port;
    String mapFilePath;
     
    public InetAddress inetAddress;
    public String ipAddress;
    DODGame g;
    mapGUI gameMap;
    public JTextField portNum;
    public JTextField mapName;
    JFrame connect;
    JFrame server;
    listenForClients listenforclients;

    //a vector of clientThreads denoting each client joining is created
    Vector<clientThread> clients = new Vector<clientThread>();
    int playerCount;

    ServerSocket ss = null;
    Socket client = null;
	
    public myServer(){
	//constructor initialises ip address and calls startupServer method
	
	playerCount = 0;
	
	try{   
	    //the ip address of the local host is found and converted into a string
	    inetAddress = InetAddress.getLocalHost();
	    ipAddress = inetAddress.getHostAddress();
	   
	    startUpServer();
	}
	catch (UnknownHostException uhe){
	    System.out.println(uhe);
	}
	
    }


    public void startUpServer(){
	//creates new frame for user to enter port number & map name for server
	connect = new JFrame("Start Server");
	connect.setSize(250,100);
	connect.setResizable(false);
	connect.setLocationRelativeTo(null);
		
	portNum = new JTextField(5);
	portNum.setHorizontalAlignment(JTextField.LEFT);

	mapName = new JTextField(15);
	mapName.setHorizontalAlignment(JTextField.LEFT);

	JPanel textFieldPanel = new JPanel();
	BorderLayout b1 = new BorderLayout();
	textFieldPanel.setLayout(b1);
	textFieldPanel.add(portNum, BorderLayout.NORTH);
	textFieldPanel.add(mapName, BorderLayout.SOUTH);

	JLabel portLabel = new JLabel("Port Number: ");
	JLabel mapLabel = new JLabel("Map Name: ");

	JPanel labelPanel = new JPanel();
	BorderLayout b2 = new BorderLayout();
	labelPanel.setLayout(b2);
	labelPanel.add(portLabel, BorderLayout.NORTH);
	labelPanel.add(mapLabel, BorderLayout.SOUTH);

	ActionListener connectlistener = new connectListener();

	JButton goButton = new JButton("Go");
	goButton.addActionListener(connectlistener);

	JPanel GUIPanel = new JPanel();
	BorderLayout b3 = new BorderLayout();
	GUIPanel.setLayout(b3);
	GUIPanel.add(textFieldPanel, BorderLayout.EAST);
	GUIPanel.add(labelPanel, BorderLayout.WEST);
	GUIPanel.add(goButton, BorderLayout.SOUTH);

	connect.add(GUIPanel);
	connect.pack();
	connect.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	connect.setVisible(true);
    }
    

    public class connectListener implements ActionListener{
	//when the go button is pressed this class is called
	public void actionPerformed(ActionEvent event){
	    //port and mapfilePath is defined
	    port = Integer.parseInt(portNum.getText());
	    mapFilePath = mapName.getText(); 
	    //connect frame closed
	    connect.dispose();
	    //new game called 
	    g = new DODGame(clients, server, mapFilePath);
	    //main servergui method is called
	    serverGUI();
	    //new client connection listening thread is called and started
	    listenforclients = new listenForClients();
	    listenforclients.start();
	}
    }

    public void serverGUI(){

	server = new JFrame("Server");
	server.setSize(1600,1600);
	server.setResizable(false);
	server.setLocationRelativeTo(null);
	//the ip address and port of the server is printed for client to use to connect to it
	JLabel ipLabel = new JLabel("IP Address: " + ipAddress);
	JLabel portLabel = new JLabel("Port Number: " + Integer.toString(port));
	
	JPanel labelPanel = new JPanel();
	BorderLayout b4 = new BorderLayout();
	labelPanel.setLayout(b4);
	labelPanel.add(ipLabel, BorderLayout.NORTH);
	labelPanel.add(portLabel, BorderLayout.SOUTH);
	
	//a map of the whole dungeon is printed onto a panel using mapGUI class
	gameMap = new mapGUI(g.getMapHeight(), g.getMapWidth());
	gameMap.revalidate();
	gameMap.repaint();
	gameMap.setMap(g.getDungeonMap());

	//radio buttons for listening are created, grouped and added to a panel
	ActionListener l = new clientListener();

	JRadioButton listening = new JRadioButton("Listen For Clients");
        listening.setActionCommand("listening");
	listening.addActionListener(l);
        listening.setSelected(true);
	JRadioButton notListening = new JRadioButton("Don't Listen For Clients");
        notListening.setActionCommand("notListening");
	listening.addActionListener(l);

        ButtonGroup group = new ButtonGroup();
        group.add(listening);
        group.add(notListening);

	JPanel radioPanel = new JPanel(new GridLayout(0,1));
	radioPanel.add(listening);
	radioPanel.add(notListening);
	
	//main server panel is created and the map & labels are added
	JPanel serverPanel = new JPanel();
	BorderLayout b5 = new BorderLayout();
	serverPanel.setLayout(b5);
	serverPanel.add(gameMap, BorderLayout.CENTER);
	serverPanel.add(labelPanel, BorderLayout.SOUTH);

	//a new frame is made for turning listening for clients on/off
	JFrame clientlisten = new JFrame("Listening");
	clientlisten.add(radioPanel);
	clientlisten.pack();
	clientlisten.setVisible(true);
	
	//serverPanel added to main server frame
	server.add(serverPanel);
	server.pack();
	server.setVisible(true);
	server.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
    }

        public class clientListener implements ActionListener{
	/* suspends and resumes the listenforclients thread when one of the two
	   readio buttons has been pressed. Stops new clients connecting.
	*/
	public void actionPerformed(ActionEvent event){
	    String com = event.getActionCommand();
	    if (com.equals("listening")){
		listenforclients.resume();
	    }
	    else if (com.equals("notListening")){
		listenforclients.suspend();
	    }
	}
	
    }

    public class listenForClients extends Thread{
	
	//a new connection listener thread is created, loading in the current vector of clients connected
	//to the server and the game being used, and is then started
	public void run(){
	    try{

		ss = new ServerSocket(port);
		//waits for a client to connect to the server, adds the client to the vector of connections and starts a thread for the client
		while(true){
		    try{
			client = ss.accept();
			playerCount = playerCount + 1;
			clientThread connection = new clientThread(client, playerCount, g, gameMap);
			clients.add(connection);
			connection.start();
		    }
		    catch (IOException e){
			System.out.println(e);
		    }
		}
	    }
	    catch (IOException e){
		System.out.println(e);
	    }
		
	}
    }
    

    public static void main(String[] args){
	//the main creates new myServer class, must be called to start up server 
		new myServer();
    }
    

}



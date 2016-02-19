import java.io.*;
import java.net.*;
import java.lang.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;
import java.awt.event.*;

public class myClient extends JFrame{
    /* the starting client class. asks the user for server's ipaddress, port number and
       a name to identify themselves by, then tries to connect to the server and start
       the main client gui.
    */

    Socket s = null;
    public PrintWriter out = null;
    public BufferedReader in = null; 
    String ipAddress = null;
    int port = 0;
    public JTextField ipAdd;
    public JTextField portNum;
    public JTextField clientID;

    //initialises game for user
    public myClient(){
	super("Connect to Server");
	connectGUI();
    }

    public void connectGUI(){
	//creates textboxes/labels for user to enter ip address, port & name into
	setSize(250,100);
	setResizable(false);
	setLocationRelativeTo(null);
	
	ipAdd = new JTextField(15);
	ipAdd.setHorizontalAlignment(JTextField.LEFT);
	
	portNum = new JTextField(5);
	portNum.setHorizontalAlignment(JTextField.LEFT);

	clientID = new JTextField(20);
	clientID.setHorizontalAlignment(JTextField.LEFT);

	JPanel textFieldPanel = new JPanel();
	BorderLayout b1 = new BorderLayout();
	textFieldPanel.setLayout(b1);
	textFieldPanel.add(ipAdd, BorderLayout.NORTH);
	textFieldPanel.add(portNum);
	textFieldPanel.add(clientID, BorderLayout.SOUTH);

	JLabel ipLabel = new JLabel("IP Address: ");
	JLabel portLabel = new JLabel("Port Number: ");
	JLabel nameLabel = new JLabel("ID Name: ");

	JPanel labelPanel = new JPanel();
	BorderLayout b2 = new BorderLayout();
	labelPanel.setLayout(b2);
	labelPanel.add(ipLabel, BorderLayout.NORTH);
	labelPanel.add(portLabel);
	labelPanel.add(nameLabel, BorderLayout.SOUTH);

	ActionListener listener = new nextListener();

	JButton nextButton = new JButton("Next");
	nextButton.addActionListener(listener);

	JPanel GUIPanel = new JPanel();
	BorderLayout b3 = new BorderLayout();
	GUIPanel.setLayout(b3);
	GUIPanel.add(textFieldPanel, BorderLayout.EAST);
	GUIPanel.add(labelPanel, BorderLayout.WEST);
	GUIPanel.add(nextButton, BorderLayout.SOUTH);

	add(GUIPanel);
	pack();
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public class nextListener implements ActionListener{
	public void actionPerformed(ActionEvent event){
	    try{	    
		//creates a new socket using the entered ipaddress & port
		s = new Socket(ipAdd.getText(), Integer.parseInt(portNum.getText()));
		//gets the output - printwriter  & input - bufferedreader from the socket
		out = new PrintWriter(s.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		//closes current frame and creates a new clientgui class using user's entries
		dispose();
		clientGUI c = new clientGUI(out, in, clientID.getText());
		c.setVisible(true);
	    }
	    catch (UnknownHostException e){
		System.out.println(e);
	    }
	    catch (IOException ioe){
		System.out.println(ioe);
	    }
	}
    }
    
    public static void main(String[] args){
	//client main method, creates new myclient class
	new myClient().setVisible(true);
    }
}
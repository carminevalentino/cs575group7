package dragonbids.server;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTextArea;

import dragonbids.api.*;
import javax.swing.JCheckBox;

public class ServerWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JButton btnConnect;
	private JTextArea textArea;
	private RMIRegisterServer rmiRegister = new RMIRegisterServer();
	private DragonBidsServer dragonBidsServer;
	private boolean serverStarted = false;
	private int portNumber = 1099; // Default Port Number RMI Registry Runs on
	private JCheckBox chckbxPersistData;
	
	private static boolean isNumeric(String str)
	{
	  return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerWindow frame = new ServerWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the frame.
	 */
	public ServerWindow() {
		setTitle("DragonBids Server");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 6, 588, 224);
		contentPane.add(scrollPane);

		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);

		MessageConsole mc = new MessageConsole(textArea);
		mc.redirectOut();
		mc.redirectErr(Color.RED, null);
		mc.setMessageLines(100);

		textField = new JTextField();
		textField.setText("1099");
		textField.setBounds(49, 246, 86, 26);
		contentPane.add(textField);
		textField.setColumns(10);

		JLabel lblPort = new JLabel("Port:");
		lblPort.setBounds(16, 251, 61, 16);
		contentPane.add(lblPort);

		btnConnect = new JButton("Start Server");
		
		btnConnect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (isNumeric(textField.getText())) // Convert the string in the text box on the gui to an int if possible
				{
					portNumber = Integer.parseInt(textField.getText());
				}
				
				if (false == serverStarted) // If we don't have a running server at the moment
				{
					dragonBidsServer = new DragonBidsServer(chckbxPersistData.isSelected()); //Instantiate Server Object in Constructor
					startServer();
				}
				else
				{
					stopServer();
				}

			}
		});
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnConnect.setBounds(477, 246, 117, 29);
		contentPane.add(btnConnect);
		
		chckbxPersistData = new JCheckBox("Persist Data");
		chckbxPersistData.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(chckbxPersistData.isSelected())
				{
					System.out.println("All Data will be loaded from disk, and written once Server is started...");
					System.out.println("You cannot change this once the Server is started...");
				}
			}
		});
		chckbxPersistData.setBounds(358, 247, 107, 23);
		contentPane.add(chckbxPersistData);
		
	}
	
	private void startServer()
	{
		if (true == rmiRegister.createRegister(portNumber)) { // If we are able to start the RMI Registry Server
			System.out.println("RMI Registry Created...");
			if ( true == dragonBidsServer.bindServerToRegister(portNumber)) // If we are able to bind our DragonBidsServer_I Interface to the Registry
			{
				serverStarted = true; // Set this flag so we can flip the button's responsibility to destroying the server
				btnConnect.setText("Stop Server");
			}
			else { // Fatal error, cannot create RMI registry
				System.err.println("Unable to Create RMI Registry...");
				System.err.println("Try Switching the Port Number ...");
			}
		}
	}
	
	private void stopServer()
	{
		if (true == dragonBidsServer.unbindServerFromRegister())
		{
			System.out.println("DragonBids Server Unbound from Register...");
			
			if (true == rmiRegister.destroyRegister()) // We have a running RMI Register, so lets destroy it
			{
				serverStarted = false;
				btnConnect.setText("Start Server");
				System.out.println("RMI Register Destroyed...");
				System.exit(NORMAL); //Noticed that the RMI Register won't fully terminate until application exits
			}
			else
			{
				System.err.println("Cannot Destroy RMI Register...");
			}
		}
		else
		{
			System.err.println("Cannot Unbind DragonBids Server from Register...");
		}
	}
}
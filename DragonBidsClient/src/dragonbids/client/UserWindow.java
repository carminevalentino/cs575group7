package dragonbids.client;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.event.ChangeEvent;
import java.awt.Color;
import java.awt.Font;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;
import java.awt.Toolkit;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import dragonbids.api.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class UserWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField usernameInput;
	private boolean waitingForServer = true;
	private Registry registry;
	private DragonBidsServer_I stub;
	private JLabel lblOnline;
	private JLabel lblServerTime;
	private SimpleDateFormat time;
	private JPanel pAccount;
	private JDatePickerImpl sellDuration;
	private JTextField sellTitle;
	private JTextArea sellDescription;
	private JTextField buyTitle;
	private JTextField buyCurrentPrice;
	private JTextField buyTimeLeft;
	private JTextField buyPendingBidPrice;
	private JTextField buySellerUname;
	private JTextField buyBuyerUname;
	private JTextArea buyDescription;
	Vector<ListingSkeleton> listingVector = new Vector<ListingSkeleton>();
	Iterator<ListingSkeleton> it;
	private boolean isLoggedIn = false;
	private int activeAuctionId = -1; //This determines which Listing is loaded in the Listing Details Window
	private String userToContact;
	
	private String activeUser = "";
	private JButton btnModifyItem;
	private JButton btnContactBuyers;
	private JButton btnRemoveListing;
	private JButton btnPlaceBid;
	private JTextField MsgMessage;
	private JTextArea MsgUsrTxtArea;
	
	/**
	 * Launch the application.
	 */
 	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserWindow frame = new UserWindow();
					frame.setVisible(true);
					// Wait for Server
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public UserWindow() {
		
		//TODO: We would Construct our Single Instances of Client Classes here
		/*
		 * new CreateListing() //Strategy Pattern to make extensible for future types of listings
		 * new CreateUser() //Will take username and send it to the server, then the client co
		 * new ClientCommunication() //Will construct itself with username, and bind username to itself so that the server can call methods defined in the client communication interface inherited. (Singleton, one get instance)
		 * new PurchaseListings //Could also implement strategy here
		 */
		
		// ###########################
		// Begin GUI Construction
		setIconImage(Toolkit.getDefaultToolkit().getImage("/Users/carminevalentino/Dropbox/Software/AuctionServerApp/img/icon.png"));
		setResizable(false);
		setTitle("DragonBids");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		DefaultListModel<ListingSkeleton> listingsList = new DefaultListModel<ListingSkeleton>();
		JList<ListingSkeleton> list = new JList<ListingSkeleton>(listingsList);
		DefaultListModel<String> MsgUsrListString = new DefaultListModel<String>();
		JList<String> MsgUserList = new JList<String>(MsgUsrListString);
		MsgUserList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				userToContact = MsgUsrListString.getElementAt(MsgUserList.getSelectedIndex()).toString();
			}
		});
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
			    JTabbedPane sourceTabbedPane = (JTabbedPane) e.getSource();
			    int tabIndex = sourceTabbedPane.getSelectedIndex();
			    
			    switch(tabIndex) { //Client Window Tab Activation
			    case 0 : //Account Tab
			    	break;
			    	
			    case 1: //Listing Detail Tab
			    	deactivateSellerFeature();
			    	it = listingVector.iterator();
			    	while(it.hasNext())
			    	{
			    		ListingSkeleton thisListing = it.next();
			    		if(activeAuctionId == thisListing.listingId)
			    		{
			    			buyTitle.setText(thisListing.auctionTile);
			    			buyDescription.setText(thisListing.auctionDescription);
			    			buyCurrentPrice.setText(Long.toString(thisListing.currentPrice));
			    			buySellerUname.setText(thisListing.sellerUsername);
			    			buyBuyerUname.setText(thisListing.buyerUsername);
			    			LocalDateTime timeRemaining = LocalDateTime.of(thisListing.auctionCompletionDateTime.toLocalDate(),thisListing.auctionCompletionDateTime.toLocalTime());
			    			buyTimeLeft.setText(timeRemaining.toString());
			    			if(thisListing.sellerUsername.equals(activeUser))
			    			{
			    				activateSellerFeature();
			    			}
			    			else
			    			{
			    				deactivateSellerFeature();
			    			}
			    			//Populate Listing Details Window with this ListingSkeleton
			    		}
			    	}
			    	break;
			    	
			    case 2: //Sell Tab
			    	break;
			    	
			    case 3: //Browse Tab
			    	try
			    	{
			    		listingsList.removeAllElements(); // Clear the Table Out Before Re-populating
			    		listingVector = stub.getListings();
			    		it = listingVector.iterator();
			    		while(it.hasNext())
			    		{
			    			listingsList.addElement(it.next()); // Add Li
			    		}
			    	}
			    	catch (Exception getListingException)
			    	{
			    		// Error
			    	}
			    	break;
			    	
			    case 4: //Messages Tab
			    	try {
			    		MsgUsrListString.removeAllElements();
			    		Vector<String> usersList = new Vector<String>();
			    		usersList = stub.getUsers();
						Iterator<String> userIt = usersList.iterator();
						while(userIt.hasNext())
						{
							MsgUsrListString.addElement(userIt.next());
						}
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			    	break;
			    }
			}
		});
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		pAccount = new JPanel();
		pAccount.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		tabbedPane.addTab("Account", null, pAccount, null);
		tabbedPane.setEnabledAt(0, true);
		pAccount.setLayout(null);
		
		usernameInput = new JTextField();
		usernameInput.setBounds(253, 86, 229, 26);
		pAccount.add(usernameInput);
		usernameInput.setColumns(10);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
		lblUsername.setBounds(329, 58, 124, 16);
		pAccount.add(lblUsername);
		
		JLabel lblServerIs = new JLabel("Server is:");
		lblServerIs.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
		lblServerIs.setBounds(309, 167, 71, 16);
		pAccount.add(lblServerIs);
		
		lblOnline = new JLabel("Offline");
		lblOnline.setFont(new Font("Helvetica Neue", Font.BOLD, 14));
		lblOnline.setForeground(Color.RED);
		lblOnline.setBounds(377, 167, 79, 16);
		pAccount.add(lblOnline);
		
		JLabel lblLoggedInUser = new JLabel("New label");
		lblLoggedInUser.setVisible(false);
		lblLoggedInUser.setHorizontalAlignment(SwingConstants.CENTER);
		lblLoggedInUser.setFont(new Font("Helvetica Neue", Font.PLAIN, 20));
		lblLoggedInUser.setBounds(220, 113, 300, 29);
		pAccount.add(lblLoggedInUser);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				/*
				 * Try to create the user with the server
				 * If user exists server will set the local boolean to true
				 * Otherwise, the remote method will return true, and user will be created
				 */
				if(btnLogin.getText().equals("Logout"))
				{
					System.exit(NORMAL);
				}
				try{
					if(stub.createUser((String)usernameInput.getText()))
					{
						System.out.println("Created User " + usernameInput.getText() + "...");
						Communication.getCommunication((String)usernameInput.getText());
						usernameInput.setVisible(false);
						lblUsername.setVisible(false);
						btnLogin.setText("Logout");
						lblLoggedInUser.setText((String)usernameInput.getText());
						lblLoggedInUser.setVisible(true);
						isLoggedIn = true;
						activeUser = (String)usernameInput.getText();
					}
					else
					{
						System.out.println("Welcome Back, " + usernameInput.getText() + "...");
						Communication.getCommunication((String)usernameInput.getText());
						usernameInput.setVisible(false);
						lblUsername.setVisible(false);
						btnLogin.setText("Logout");
						lblLoggedInUser.setText((String)usernameInput.getText());
						lblLoggedInUser.setVisible(true);
						isLoggedIn = true;
						activeUser = (String)usernameInput.getText();
					}
				}
				catch (RemoteException e2)
				{
					System.out.println("There was an error. Check that the server is Online ...");
					System.out.println("We Recommend Reloading the application ...");
				}
				
			}
		});
		btnLogin.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		btnLogin.setBounds(309, 209, 117, 29);
		pAccount.add(btnLogin);
		
		lblServerTime = new JLabel("New label");
		lblServerTime.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
		lblServerTime.setBounds(351, 154, 145, 15);
		
        Calendar cal = Calendar.getInstance();
		
		pAccount.add(lblServerTime);
		
		//current time
        time = new SimpleDateFormat("HH:mm:ss");
        System.out.println( time.format(cal.getTime()) );
		lblServerTime.setText(time.format(cal.getTime()));
		
		JLabel label = new JLabel("Time:");
		label.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
		label.setBounds(309, 153, 71, 16);
		pAccount.add(label);
		
		
		JPanel pListingDetails = new JPanel();
		pListingDetails.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		tabbedPane.addTab("Listing Details", null, pListingDetails, null);
		pListingDetails.setLayout(null);
		
		JLabel lblNewLabel_5 = new JLabel("Title:");
		lblNewLabel_5.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_5.setBounds(6, 36, 80, 16);
		pListingDetails.add(lblNewLabel_5);
		
		JLabel lblNewLabel_6 = new JLabel("Price:");
		lblNewLabel_6.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_6.setBounds(6, 83, 80, 16);
		pListingDetails.add(lblNewLabel_6);
		
		buyTitle = new JTextField();
		buyTitle.setEditable(false);
		buyTitle.setBounds(98, 31, 445, 26);
		pListingDetails.add(buyTitle);
		buyTitle.setColumns(10);
		
		buyCurrentPrice = new JTextField();
		buyCurrentPrice.setEditable(false);
		buyCurrentPrice.setBounds(98, 78, 130, 26);
		pListingDetails.add(buyCurrentPrice);
		buyCurrentPrice.setColumns(10);
		
		JLabel lblNewLabel_7 = new JLabel("Time Left:");
		lblNewLabel_7.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_7.setBounds(6, 130, 80, 16);
		pListingDetails.add(lblNewLabel_7);
		
		buyTimeLeft = new JTextField();
		buyTimeLeft.setEditable(false);
		buyTimeLeft.setBounds(98, 125, 130, 26);
		pListingDetails.add(buyTimeLeft);
		buyTimeLeft.setColumns(10);
		
		JLabel lblNewLabel_8 = new JLabel("Description:");
		lblNewLabel_8.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_8.setBounds(6, 169, 80, 16);
		pListingDetails.add(lblNewLabel_8);
		
		buyDescription = new JTextArea();
		buyDescription.setLineWrap(true);
		buyDescription.setWrapStyleWord(true);
		buyDescription.setBounds(98, 169, 445, 106);
		pListingDetails.add(buyDescription);
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setForeground(Color.BLACK);
		separator.setBounds(555, 6, 10, 292);
		pListingDetails.add(separator);
		
		buyPendingBidPrice = new JTextField();
		buyPendingBidPrice.setBounds(577, 230, 117, 26);
		pListingDetails.add(buyPendingBidPrice);
		buyPendingBidPrice.setColumns(10);
		
		btnPlaceBid = new JButton("Bid");
		btnPlaceBid.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// If my value in the text box is expressed in cents
				try{
					modifyListing();
				}
				catch (Exception ex)
				{
					//
				}
				
			}
		});
		btnPlaceBid.setBounds(577, 269, 117, 29);
		pListingDetails.add(btnPlaceBid);
		
		JLabel lblSeller = new JLabel("Seller:");
		lblSeller.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSeller.setBounds(290, 83, 80, 16);
		pListingDetails.add(lblSeller);
		
		JLabel lblHighBidder = new JLabel("Buyer:");
		lblHighBidder.setHorizontalAlignment(SwingConstants.RIGHT);
		lblHighBidder.setBounds(290, 130, 80, 16);
		pListingDetails.add(lblHighBidder);
		
		buySellerUname = new JTextField();
		buySellerUname.setBounds(382, 78, 130, 26);
		pListingDetails.add(buySellerUname);
		buySellerUname.setColumns(10);
		
		buyBuyerUname = new JTextField();
		buyBuyerUname.setBounds(382, 125, 130, 26);
		pListingDetails.add(buyBuyerUname);
		buyBuyerUname.setColumns(10);
		
		JButton btnContactSeller = new JButton("Contact Seller");
		btnContactSeller.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		btnContactSeller.setBounds(577, 125, 117, 29);
		pListingDetails.add(btnContactSeller);
		
		btnContactBuyers = new JButton("Contact Buyers");
		btnContactBuyers.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		btnContactBuyers.setForeground(Color.RED);
		btnContactBuyers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnContactBuyers.setBounds(577, 59, 136, 29);
		pListingDetails.add(btnContactBuyers);
		
		btnRemoveListing = new JButton("Remove Item");
		btnRemoveListing.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					removeListing();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "Listing not found on server");
				}
			}
		});
		btnRemoveListing.setForeground(Color.RED);
		btnRemoveListing.setBounds(577, 31, 117, 29);
		pListingDetails.add(btnRemoveListing);
		
		btnModifyItem = new JButton("Modify Item");
		btnModifyItem.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					modifyListing();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,  "Listing not found on server");
				}
				
			}
		});
		btnModifyItem.setForeground(Color.RED);
		btnModifyItem.setBounds(577, 6, 117, 29);
		pListingDetails.add(btnModifyItem);
		
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
	    		try {
					ListingSkeleton thisListing = stub.getListing(activeAuctionId);
	    			buyTitle.setText(thisListing.auctionTile);
	    			buyDescription.setText(thisListing.auctionDescription);
	    			buyCurrentPrice.setText(Long.toString(thisListing.currentPrice));
	    			buySellerUname.setText(thisListing.sellerUsername);
	    			buyBuyerUname.setText(thisListing.buyerUsername);
	    			LocalDateTime timeRemaining = LocalDateTime.of(thisListing.auctionCompletionDateTime.toLocalDate(),thisListing.auctionCompletionDateTime.toLocalTime());
	    			buyTimeLeft.setText(timeRemaining.toString());
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnRefresh.setBounds(577, 151, 117, 29);
		pListingDetails.add(btnRefresh);
		
		JPanel pCreateAuction = new JPanel();
		pCreateAuction.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		tabbedPane.addTab("Sell", null, pCreateAuction, null);
		pCreateAuction.setLayout(null);
		
		JButton btnListItem = new JButton("List Item");
		btnListItem.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				createAuction();
			}
		});
		btnListItem.setBounds(495, 269, 117, 29);
		pCreateAuction.add(btnListItem);
		
		JLabel lblNewLabel_1 = new JLabel("Title:");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_1.setBounds(6, 25, 80, 16);
		pCreateAuction.add(lblNewLabel_1);
		
		JLabel lblNewLabel_3 = new JLabel("Expiration:");
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_3.setBounds(6, 70, 80, 16);
		pCreateAuction.add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("Description:");
		lblNewLabel_4.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_4.setBounds(6, 116, 80, 16);
		pCreateAuction.add(lblNewLabel_4);
		
		sellDescription = new JTextArea();
		sellDescription.setLineWrap(true);
		sellDescription.setWrapStyleWord(true);
		sellDescription.setBounds(122, 116, 445, 106);
		pCreateAuction.add(sellDescription);
		
		
		UtilDateModel model = new UtilDateModel();
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		
		sellDuration = datePicker;
		sellDuration.setBounds(122, 65, 130, 26);
		pCreateAuction.add(sellDuration);
		//sellDuration.setColumns(10);
		
		sellTitle = new JTextField();
		sellTitle.setBounds(122, 20, 445, 26);
		pCreateAuction.add(sellTitle);
		sellTitle.setColumns(10);
		
		//Begin Auction Listing Browser
		JScrollPane pBrowseListings = new JScrollPane();
		pBrowseListings.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		tabbedPane.addTab("Browse", null, pBrowseListings, null);
		pBrowseListings.setLayout(null);
		
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (2 == e.getClickCount())
				{
					// Double Clicked Auction
					activeAuctionId = listingsList.getElementAt(list.getSelectedIndex()).listingId;
					tabbedPane.setSelectedIndex(1); // Select Listing Detail Pane
				}
			}
		});
		list.setVisibleRowCount(18);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setBounds(6, 6, 707, 292);
		pBrowseListings.add(list);
		
		JPanel pMessages = new JPanel();
		tabbedPane.addTab("Messages", null, pMessages, null);
		pMessages.setLayout(null);
		
		MsgUserList.setBounds(6, 6, 209, 292);
		pMessages.add(MsgUserList);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(227, 6, 1, 292);
		pMessages.add(separator_1);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setOrientation(SwingConstants.VERTICAL);
		separator_2.setForeground(Color.BLACK);
		separator_2.setBounds(215, 0, 13, 304);
		pMessages.add(separator_2);
		
		MsgMessage = new JTextField();
		MsgMessage.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER && "" != userToContact)
				{
					//TODO: Send Message
					try {
						NotificationObserver_I stub = (NotificationObserver_I)registry.lookup(userToContact);
						stub.notifyClient(activeUser + "<" + System.currentTimeMillis() + ">" + ": " + MsgMessage.getText());
						MsgUsrTxtArea.setText("Message Sent to " + userToContact);
						MsgMessage.setText("");
					} catch (RemoteException | NotBoundException e1) {
						// TODO Auto-generated catch block
						System.out.println(userToContact + " is not Online.");
					}
					
				}
			}
		});
		MsgMessage.setBounds(300, 261, 413, 26);
		pMessages.add(MsgMessage);
		MsgMessage.setColumns(10);
		
		JLabel lblMessage = new JLabel("Message:");
		lblMessage.setBounds(227, 266, 61, 16);
		pMessages.add(lblMessage);
		
		MsgUsrTxtArea = new JTextArea();
		MsgUsrTxtArea.setBounds(227, 6, 486, 243);
		pMessages.add(MsgUsrTxtArea);
//		for (int i = 0; i < 100; ++i)
//		{
//			listingsList.addElement("Auction - Index " + i);
//		}
		// End Auction Listing Browser
		
		JPanel notificationPanel = new JPanel();
		contentPane.add(notificationPanel, BorderLayout.SOUTH);
		notificationPanel.setLayout(new BorderLayout(0, 0));
		
		JTextArea notificationPanelBox = new JTextArea();
		notificationPanelBox.setEditable(false);
		notificationPanelBox.setColumns(1);
		notificationPanelBox.setRows(6);
		notificationPanel.add(new JScrollPane(notificationPanelBox), BorderLayout.NORTH);
		
		MessageConsole mc = new MessageConsole(notificationPanelBox);
		
		JLabel lblNewLabel = new JLabel("Notifications");
		lblNewLabel.setFont(new Font("Helvetica Neue", Font.BOLD, 14));
		notificationPanel.add(lblNewLabel, BorderLayout.SOUTH);
		mc.redirectOut();
		mc.redirectErr(Color.RED, null);
		mc.setMessageLines(100);
		
		// ###########################
		// Begin GUI Construction
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				connectToServer();
			}
		});
		
	}
	
	private void connectToServer()
	{
		while(waitingForServer)
		{
	        try {
	            registry = LocateRegistry.getRegistry(); //Works only on localhosts, meaning we have to run the server from the same machine/subnet
	            stub = (DragonBidsServer_I) registry.lookup("DragonBids"); //This is a Key to Retrieve the Object from the Registry; Registry is a String:Object pair
	            System.out.println("Connected to Server...");
	            lblOnline.setText("Online");
	            lblOnline.setForeground(Color.GREEN);
	            Calendar cal = Calendar.getInstance();
	            lblServerTime.setText(time.format(cal.getTime()));
	            waitingForServer = false;
	        } catch (Exception e1) {
	        }
		}
	}
	
	private void createAuction() // Methods which invoke the remote objects need to throw a Remote Exception, and calls sh
	{
		//TODO: This would check to ensure that we have made a connect, and have a bound object
		if (!waitingForServer)
		{
			// Call to CreateListing class
				// The Create Listing Class would get the remote object stub (stub)
					// The Listing Class would ultimately invoke the server like this:
				try{
					String Title=sellTitle.getText();
					String Description= sellDescription.getText();
					Date Duration= (Date) sellDuration.getModel().getValue();					
					LocalDateTime exp = LocalDateTime.ofInstant(Duration.toInstant(), ZoneId.systemDefault());
					if(Title.length()<3){
						JOptionPane.showMessageDialog(null,"You need to input a Title");
						return;
					}
					if(Description.length()<3){
						JOptionPane.showMessageDialog(null,"You need to input a Description");
						return;
					}
					if (exp.isBefore(LocalDateTime.now()))
							{
								JOptionPane.showMessageDialog(null, "Please choose an expiration date in the future.");
								return;
							}
					
//					if(Duration.length()<10){
//						JOptionPane.showMessageDialog(null,"You need to input a Duration");
//						return;
//					}
//					LocalDateTime completeDateTime;
//					try {
//						DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
//						completeDateTime = LocalDateTime.ofInstant(Duration.toInstant(), ZoneId.systemDefault());
//					}
//					catch (Exception e)
//					{
//						JOptionPane.showMessageDialog(null,"Invalid DateTime Format MM/dd/yyyy HH:mm");
//						return;
//					}
					ListingSkeleton listing= new ListingSkeleton();
					listing.auctionDescription = Description;
					listing.auctionTile = Title;
					listing.sellerUsername = usernameInput.getText();
					listing.auctionCompletionDateTime = exp;
					if(stub.createListing(listing)) {
						//TODO: Redirect to another tab

						System.out.println("Server Created New Listing: " + Title);
						JOptionPane.showMessageDialog(null, "Done!");

					}
				}
				
				catch (RemoteException e)
				{
					// Failed to invoke the server
				}
		}
		
	}

	private void modifyListing() throws RemoteException
	{
		if (!waitingForServer)
		{
			try
			{
				ListingSkeleton modListing = new ListingSkeleton();
				modListing.auctionTile = buyTitle.getText();
				modListing.auctionDescription = buyDescription.getText();
				modListing.listingId = activeAuctionId;
				try{
				modListing.proposedPrice = Long.parseLong(buyPendingBidPrice.getText());
				modListing.buyerUsername = activeUser;
				}
				catch (Exception e)
				{
					// need a better way to handle
				}
			//	modListing.extendAuctionMinutes = [] // consider changing this to "proposedEndTime" 
				
//** DEBUG: Uncomment to Test modifying listing 1 on server
 //modListing.auctionTile = "Title Changed!";
 //modListing.auctionDescription = "A better description!";
 //modListing.listingId = 1;
//** END DEBUG */
			
				stub.modifyListing(modListing);
			}
			catch (RemoteException e)
			{
				// failed to invoke on server
			}
			
		}
	}
	
	private void removeListing() throws RemoteException
	{
		try
		{
			stub.removeListing(activeAuctionId);
		}
		catch (RemoteException e)
		{
			//failed to invoke on server
		}
	}

	private void activateSellerFeature()
	{
		btnModifyItem.setVisible(true);;
		btnContactBuyers.setVisible(true);
		btnRemoveListing.setVisible(true);
		btnPlaceBid.setVisible(false);
		buyPendingBidPrice.setVisible(false);
		buyTitle.setEditable(true);
		buyDescription.setEditable(true);
	}
	
	private void deactivateSellerFeature()
	{
		btnModifyItem.setVisible(false);;
		btnContactBuyers.setVisible(false);
		btnRemoveListing.setVisible(false);
		btnPlaceBid.setVisible(true);
		buyPendingBidPrice.setVisible(true);
		buyTitle.setEditable(false);
		buyDescription.setEditable(false);
	}
	
    final class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String datePattern = "MM/dd/yyyy";
        private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

    @Override
     public Object stringToValue(String text) throws ParseException {
     return dateFormatter.parseObject(text);
      }
    @Override
        public String valueToString(Object value) throws ParseException {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }
            return "";
        }
    }
}

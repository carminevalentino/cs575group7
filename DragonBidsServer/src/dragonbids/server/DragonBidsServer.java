package dragonbids.server;

import dragonbids.api.*;
import java.io.*;
import dragonbids.structures.listings.Listing;
import dragonbids.structures.listings.AuctionFactory;

import dragonbids.structures.listings.*;
import dragonbids.structures.listings.ListingHandlers.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.Vector;
import java.util.HashMap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.Collections;

public class DragonBidsServer implements DragonBidsServer_I {

	private Registry registry;
	private String dragonBidsServer = "DragonBids";
	private Vector<User> activeUsers; // Vector of User Classes Held by the
										// server
	protected HashMap<Integer, Listing> activeListings; // collection of active
														// listings held on
														// server
	private int lastAuctionUID;
	private AuctionFactory auctionFactory;
	private boolean isDataPersisted;

	public DragonBidsServer(boolean _isDataPersisted) {
		isDataPersisted = _isDataPersisted;
		lastAuctionUID = 0;
		activeUsers = new Vector<User>();
		activeListings = new HashMap<Integer, Listing>();
		startupProcess();
		auctionFactory = new AuctionFactory();
	}

	public boolean bindServerToRegister(int port) {
		boolean bindSuccess = false;
		try {
			DragonBidsServer obj = new DragonBidsServer(isDataPersisted);
			DragonBidsServer_I stub = (DragonBidsServer_I) UnicastRemoteObject.exportObject(obj, 0);

			// Bind the remote object's stub in the registry
			registry = LocateRegistry.getRegistry(port);
			registry.bind(dragonBidsServer, stub);
			System.out.println("DragonBids Server Online ...");
			bindSuccess = true;
		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
			bindSuccess = false;
		}
		return bindSuccess;
	}

	public boolean unbindServerFromRegister() {
		boolean unbindSuccess = false;
		try {
			registry.unbind(dragonBidsServer);
			unbindSuccess = true;
		} catch (AccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return unbindSuccess;
	}

	@Override
	public boolean createUser(String username) throws RemoteException {
		Iterator<User> it = activeUsers.iterator();
		while (it.hasNext()) {
			if (it.next().getUsername().equals(username.toString())) // We found
																		// the
																		// user
																		// already,
																		// so
																		// Let
																		// Client
																		// Login
			{
				System.out.println("User " + username + ", has connected to server ...");
				return false;
			}
		}

		try {
			activeUsers.add(new User(username)); // Create new user, and add to
													// our vector
			writeUsers();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean createListing(ListingSkeleton arg0) throws RemoteException {
		lastAuctionUID += 1;
		// TODO Add duration to listing
		Listing newListing = null;
		
		switch(arg0.listingTypes)
		{
		case AUCTION:
			newListing = auctionFactory.getListing("AUCTION", lastAuctionUID, arg0.sellerUsername, arg0.auctionTile,
					arg0.auctionDescription, arg0.auctionCompletionDateTime);
			
			break;
		case DEFAULT:
			// Do Nothing
			break;
			
			//Switch off of additional listing types in the future and call to the relational factory
		}
		
		if (null != newListing) {
			activeListings.put(lastAuctionUID, newListing);
			System.out.println("Listing Created: " + newListing.getTitle());
			System.out.println(activeListings.toString());
			writeListings();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Vector<ListingSkeleton> getListings() throws RemoteException {
		Vector<ListingSkeleton> listingSkeletonVector = new Vector<ListingSkeleton>();
		for (Listing listing : activeListings.values()) {

			listingSkeletonVector.add(listing.extractSkeleton());

		}
		return listingSkeletonVector;
	}
	
	@Override
	public ListingSkeleton getListing(int listingId) throws RemoteException
	{
		Listing listing = activeListings.get(listingId);
		return listing.extractSkeleton();
	}

	@Override
	public Vector<String> getUsers() throws RemoteException {
		Vector<String> userVector = new Vector<String>();
		Iterator<User> it = activeUsers.iterator();
		while(it.hasNext())
		{
			userVector.add(it.next().getUsername().toString());
		}
		
		return userVector;
	}

	@Override
	public boolean modifyListing(ListingSkeleton arg0) throws RemoteException {
		// THOUGHTS: create singletons for a Handler of each of each ListingType
		// handler has one method: modify(Listing, ListingSkeleton) that
		// updates the Listing to the spec described by ListingSkeleton
		// >>rolls bid placement into the modify(Listing, ListingSkeleton)
		// method of Handler
		// >>allows listing to be responsible for defining how to place bid, etc
		// ** IS A STRATEGY PATTERN **

		// TODO Auto-generated method stub
		// TODO finish modification of existing listing object
		Listing listingToMod = getListing(arg0);

		if (listingToMod instanceof Auction) {
			AuctionHandler hndl = new AuctionHandler();
			System.out.println("Modified Listing: " + listingToMod.getTitle());
			System.out.println(hndl.modify(listingToMod, arg0));
			writeListings();
		}

		return false;
	}

	@Override
	public boolean placeBid(ListingSkeleton arg0) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean removeListing(int listingId) throws RemoteException {
		// TODO Auto-generated method stub
		// TODO implement observer notification so that bidders know auction is
		// canceled
		if (activeListings.containsKey(listingId)) {
			Listing lst = activeListings.remove(listingId); // dummy assingment
															// in case we decide
															// to do something
															// with the removed
															// listing
			// lst.notifyObservers(new ListingRemovedNotification());
			System.out.println("Removed Listing: " + lst.getTitle());
			writeListings();
			return true;
		}

		return false;
	}

	private Listing getListing(ListingSkeleton skeleton) {
		return activeListings.get(skeleton.listingId);
	}

	private final void writeListings() {
		if (isDataPersisted) {
			try {

				File listings = new File(System.getProperty("user.dir") + "/activeListings.ser");
				FileOutputStream fout = new FileOutputStream(listings);
				ObjectOutputStream oos = new ObjectOutputStream(fout);

				HashMap<Integer, Listing> tmp = new HashMap<Integer, Listing>();

				tmp.putAll(activeListings);

				oos.writeObject(activeListings);
				oos.close();
				fout.close();
				System.out.println("Listings written to disk.");
			} catch (IOException e) {
				System.out.println("ERROR: Listing file not written.");
			}
		}
	}

	private final void writeUsers() {
		if (isDataPersisted) {
			try {

				File users = new File(System.getProperty("user.dir") + "/activeUsers.ser");
				FileOutputStream fout = new FileOutputStream(users);
				ObjectOutputStream oos = new ObjectOutputStream(fout);
				oos.writeObject(activeUsers);
				oos.close();
				fout.close();
				System.out.println("Active users written to disk. ");

			} catch (IOException e) {
				System.out.println("ERROR: User file not written.");
			}
		}
	}

	private final void startupProcess() {
		if (isDataPersisted) {
			try {
				FileInputStream fin;
				ObjectInputStream ois;

				// read active listings
				fin = new FileInputStream(System.getProperty("user.dir") + "/activeListings.ser");
				ois = new ObjectInputStream(fin);
				activeListings = (HashMap<Integer, Listing>) ois.readObject();
				lastAuctionUID = Collections.max(activeListings.keySet());
				ois.close();
				fin.close();

				// read active users
				fin = new FileInputStream(System.getProperty("user.dir") + "/activeUsers.ser");
				ois = new ObjectInputStream(fin);
				Vector<User> tempActiveUsers = (Vector<User>) ois.readObject();
				ois.close();
				fin.close();

				// Need to instantiate Users that are being loaded
				Iterator<User> it = tempActiveUsers.iterator();
				while (it.hasNext()) {
					activeUsers.add(new User(it.next().getUsername()));
				}

			} catch (Exception e) {
				System.out.println("No server files loaded.");
			}
		}
	}

}
package dragonbids.server;

import dragonbids.api.*;
import dragonbids.structures.listings.Listing;
import dragonbids.structures.listings.ListingFactory;

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

public class DragonBidsServer implements DragonBidsServer_I {
	
	private Registry registry;
	private String dragonBidsServer = "DragonBids";
	private Vector<User> activeUsers = new Vector<User>(); //Vector of User Classes Held by the server
	private HashMap<Integer, Listing> activeListings = new HashMap<Integer, Listing>(); //collection of active listings held on server
    private int lastAuctionUID=0;
	public boolean bindServerToRegister(int port)
	{
		boolean bindSuccess = false;
        try {
        	DragonBidsServer obj = new DragonBidsServer();
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
	
	public boolean unbindServerFromRegister()
	{
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
		// TODO Auto-generated method stub
		Iterator<User> it = activeUsers.iterator();
		while(it.hasNext())
		{
			if (it.next().getUsername().equals(username.toString())) // We found the user already, so Let Client Login
			{
				System.out.println("User " + username + ", has connected to server ...");
				return false;
			}
		}

		try {
			activeUsers.add(new User(username)); // Create new user, and add to our vector
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	@Override
	public boolean createListing(ListingSkeleton arg0) throws RemoteException {


		ListingFactory factory=new ListingFactory();
		lastAuctionUID+=1;
		//Todo: Add duration to listing
		Listing newAuction=factory.getListing("AUCTION",lastAuctionUID,arg0.sellerUsername,arg0.auctionTile,arg0.auctionDescription);
		activeListings.put(lastAuctionUID,newAuction);
		return true;
		//Todo : When should this return false ?
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
	public Vector<String> getUsers() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean modifyListing(ListingSkeleton arg0) throws RemoteException {
		//THOUGHTS: create singletons for a Handler of each of each ListingType
		//			handler has one method: modify(Listing, ListingSkeleton) that
		//			updates the Listing to the spec described by ListingSkeleton
		//          >>rolls bid placement into the modify() method of Handler
		//          >>allows listing to be responsible for defining how to place bid, etc
		//          ** IS A STRATEGY PATTERN **
		
		// TODO Auto-generated method stub
		// TODO finish modification of existing listing object
		Listing listingToMod = getListing(arg0);
		
		if (listingToMod instanceof Auction)
		{
			AuctionHandler hndl = new AuctionHandler((Auction) listingToMod);
			hndl.modify(listingToMod, arg0);
		}
		
		
		return false;
	}

	@Override
	public boolean placeBid(ListingSkeleton arg0) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	
	public boolean remoteListing(int listingId) throws RemoteException {
		// TODO Auto-generated method stub
		// TODO implement observer notification so that bidders know auction is canceled
		Listing lst = activeListings.remove(listingId);
		// lst.notifyObservers(new ListingRemovedNotification());
		return false;
	}
	
	private Listing getListing(ListingSkeleton skeleton){
		return activeListings.get(skeleton.listingId);
	}

}
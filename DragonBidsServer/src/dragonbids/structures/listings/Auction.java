package dragonbids.structures.listings;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDateTime;
import java.util.LinkedList;

import dragonbids.api.NotificationObserver_I;

/**
 * Write a description of class Auction here.
 * 
 * @author Lew
 * @version latest: 12-5-2016
 */
public class Auction extends Listing
{

	private static final long serialVersionUID = -6365418462569296789L;
	private BidHistory bidHistory;
	private LinkedList<NotificationObserver_I> observers = new LinkedList<NotificationObserver_I>();
	private Registry registry;
    
    /**
     */
    public Auction(int listingUID, String creatorID, String title, String description, LocalDateTime expiration)
    {
        super(listingUID, creatorID, title, description, new Object(), 0, expiration);
        this.bidHistory = new BidHistory();
        try
        {
        	registry = LocateRegistry.getRegistry();
        	observers.addFirst((NotificationObserver_I)registry.lookup(this.creatorID.toString()));
        } catch (NotBoundException | RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Fatal Error: Getting Client Notification Object from Java RMI...");
		}
        bidHistory.addBid(creatorID, currentPrice);
        
    }
    
    public void addBid(String userid, long bid)
    {
        //long temp = currentPrice;
        bidHistory.addBid(userid, bid);
        this.currentPrice = bid;
        this.buyerID = userid;
        
        try
        {
        registry = LocateRegistry.getRegistry();
        
        // Notify the Seller of a Bid
        observers.getFirst().notifyClient("A new Bid of " + this.currentPrice + " Has Been Placed on your Listing " + this.title + "...");
        // If this is not the first bid placed
        if(observers.size() > 1)
        {
        	observers.getLast().notifyClient("Sorry, you have been Outbid on item: " + this.title + "...");
        	// Remove Previous High Bidder from Notification LinkedList
        	observers.removeLast();
        }
        
        // Add Current Bidder to End of Notification LinkedList
        observers.addLast((NotificationObserver_I)registry.lookup(userid.toString()));
        observers.getLast().notifyClient("Great! you are currently the high bidder...");
        
        }
        catch(RemoteException | NotBoundException e)
        {
        	System.out.println(e.toString());
        }
    }
    
    public Bid getHighBid()
    {
        return bidHistory.getHighBid();
    }
    
    public Bid getStartingBid()
    { 
        return bidHistory.getFirstBid();
    }
    
    public BidHistory getBidHistory()
    {
        return bidHistory;
    }
}

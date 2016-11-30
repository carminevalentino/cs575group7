package dragonbids.structures.listings;
import java.util.*;
import java.io.Serializable;


/**
 * Write a description of class BidHistory here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class BidHistory implements Serializable
{

	private static final long serialVersionUID = -7082260414627474298L;
	LinkedList<Bid> bids;
    

    /**
     * Constructor for objects of class BidHistory
     */
    public BidHistory()
    {
        bids = new LinkedList<Bid>();
    }

    public void addBid(String userid, long bid)
    {
        bids.add(new Bid(userid, bid));
    }
    
    public Bid getHighBid()
    {
        // select bid having max timestamp
        return bids.getLast();
    }
    
    public Bid getFirstBid()
    {
        // select bid having min timestamp
        return bids.getFirst();
    }
    
//    private void writeObject(ObjectOutputStream aOutputStream) throws IOException 
//    {
//    	
//    }
  
}

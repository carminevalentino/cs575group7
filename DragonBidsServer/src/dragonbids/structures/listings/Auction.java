package dragonbids.structures.listings;


/**
 * Write a description of class Auction here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Auction extends Listing
{

	private static final long serialVersionUID = -6365418462569296789L;
	private BidHistory bidHistory;
    
    /**
     */
    public Auction(int listingUID, String creatorID, String title, String description)
    {
        super(listingUID, creatorID, title, description, new Object(), 0, 180);
        this.bidHistory = new BidHistory();
        bidHistory.addBid(creatorID, currentPrice);
        //addObserver(new AuctionObserver());
    }
    
    public void addBid(String userid, long bid)
    {
        long temp = currentPrice;
        bidHistory.addBid(userid, bid);
        this.currentPrice = bid;
        //setChanged();
        //notifyObservers(new BidChangedNotification(userid, listingID, bid, temp));
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

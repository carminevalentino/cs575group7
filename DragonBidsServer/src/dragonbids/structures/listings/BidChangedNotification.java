package dragonbids.structures.listings;


/**
 * Write a description of class BidChangedNotification here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class BidChangedNotification implements Notification
{
    int listingID;
    String userID;
    long newPrice;
    long oldPrice;

    /**
     * Constructor for objects of class BidChangedNotification
     */
    public BidChangedNotification(String userid, int listingID, long oldPrice, long newPrice)
    {
        this.listingID = listingID = listingID;
        this.userID = userid;
        this.newPrice = newPrice;
        this.oldPrice = oldPrice;
    }
    
    public String toString()
    {
        return "Listing " + listingID + " bid changed to " + newPrice + " by user " + userID;
    }
}

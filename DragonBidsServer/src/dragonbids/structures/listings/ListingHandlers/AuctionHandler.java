package dragonbids.structures.listings.ListingHandlers;
import dragonbids.structures.listings.*;
import dragonbids.api.*;



/**
 */
public class AuctionHandler implements ListingHandler

{
	
	//deprecated
	
//    private Auction auction;
//    
//    public AuctionHandler(Auction auctionToMod)
//    {
//    	this.auction = auctionToMod;
//    }  
//
//    public boolean modify(ListingSkeleton skele)
//    {
//    	
//    	//TODO modify duration
//    	modifyTitle(skele.auctionTile);
//    	modifyDesc(skele.auctionDescription);
//    	
//    	return true;
//    }
//    
// 
//    public boolean placeBid(String userid, long newBid)
//    {   
//        if (newBid <= auction.getCurrentPrice())
//        {
//            //fail to bid
//            return false;
//        }
//        else
//        {
//            auction.addBid(userid, newBid);
//            return true;
//        }
//    }
//        
//    public boolean modifyDesc(String newDesc)
//    {
//        auction.setDesc(newDesc);
//        return true;
//    }
//    
//    public boolean modifyTitle(String newTitle)
//    {
//        auction.setTitle(newTitle);
//        return true;
//    }
//    
//    public boolean extendListing(long minutes)
//    {
//        auction.setExpiration(auction.getExpiration().plusMinutes(minutes));
//        return true;
//    }
//    
////    public boolean modifyPhoto(Object photo)
////    {
////        auction.setPhoto(photo);
////        return true;
////    }
    

    public AuctionHandler() {}
    

    
    public String modify(Listing lst, ListingSkeleton skele)
    {
    	Auction auctionToMod = (Auction) lst;
    	String modifiedString = new String();
    	
    	// check each "modifiable" property and update it if it has changed
    	
    	
    	//check description, title for changes and update as necessary
    	if (!(auctionToMod.getDesc().equals(skele.auctionDescription)))
    	{
    		lst.setDesc(skele.auctionDescription);
    		modifiedString += "description changed to " + skele.auctionDescription + "; ";
    	}
    	
    	if (!(auctionToMod.getTitle().equals(skele.auctionTile)))
    	{
    		lst.setTitle(skele.auctionTile);
    		modifiedString += "title changed to " + skele.auctionTile + "; ";
    	}
    	
    	// if there is a proposed price change from someone not the seller
    	if (!(skele.proposedPrice == 0) && !(skele.buyerUsername.equals("") && !(skele.sellerUsername.equals(lst.getCreator()))))
    	{
    		if ((placeBid(auctionToMod, skele.buyerUsername, skele.proposedPrice)))
    		{
    			modifiedString += "Bid Successful: " + skele.buyerUsername + " for " + skele.proposedPrice;
    		}
    	}
    			
    	return modifiedString;
    }
    
    private boolean placeBid(Auction auction, String userid, long newBid)
    {   
        if (newBid <= auction.getHighBid().getBidPrice())
        {
            //fail to bid
            return false;
        }
        else
        {
            auction.addBid(userid, newBid);
            return true;
        }
    }
        
}
package dragonbids.structures.listings.ListingHandlers;

import dragonbids.structures.listings.*;
import dragonbids.api.*;

/**
 */
public class AuctionHandler implements ListingHandler

{
	public AuctionHandler() {
	}

	public String modify(Listing lst, ListingSkeleton skele) {
		Auction auctionToMod = (Auction) lst;
		String modifiedString = new String();
		long proPrice = skele.proposedPrice.movePointRight(2).longValue();

		// check each "modifiable" property and update it if it has changed

		// check description, title for changes and update as necessary
		if (!(auctionToMod.getDesc().equals(skele.auctionDescription))) {
			lst.setDesc(skele.auctionDescription);
			modifiedString += "description changed to " + skele.auctionDescription + "; ";
		}

		if (!(auctionToMod.getTitle().equals(skele.auctionTile))) {
			lst.setTitle(skele.auctionTile);
			modifiedString += "title changed to " + skele.auctionTile + "; ";
		}

		// if there is a proposed price change from someone not the seller
		if (!(proPrice == 0) && !(skele.buyerUsername.equals("") && !(skele.sellerUsername.equals(lst.getCreator())))) {
			if ((placeBid(auctionToMod, skele.buyerUsername, proPrice)))
				modifiedString += "Bid Successful: " + skele.buyerUsername + " for " + skele.proposedPrice;
		}
		return modifiedString;
	}



	private boolean placeBid(Auction auction, String userid, long newBid) {
		if (newBid <= auction.getHighBid().getBidPrice()) {
			// fail to bid
			return false;
		} else {
			auction.addBid(userid, newBid);
			return true;
		}
	}

}
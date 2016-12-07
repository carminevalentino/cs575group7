package dragonbids.structures.listings;

import java.time.LocalDateTime;

/**
 * Created by amir on 11/20/16.
 * edited by lew on   12/05/16
 */
public class ListingFactory {

    public ListingFactory() {
    }

    public Listing getListing(String Type, int listingUID, String creatorID, String title, String description, LocalDateTime expiration) {
//    	if (Type.equalsIgnoreCase("AUCTION"))
//    	{
//    		return new Auction(listingUID, creatorID, title, description);
//    	}
//    	return null;
       return Type == null?null:(Type.equalsIgnoreCase("AUCTION")?new Auction(listingUID, creatorID, title, description, expiration):null);
    }
}
package dragonbids.structures.listings;

/**
 * Created by amir on 11/20/16.
 */
public class ListingFactory {

    public ListingFactory() {
    }

    public Listing getListing(String Type, int listingUID, String creatorID, String title, String description) {
//    	if (Type.equalsIgnoreCase("AUCTION"))
//    	{
//    		return new Auction(listingUID, creatorID, title, description);
//    	}
//    	return null;
       return Type == null?null:(Type.equalsIgnoreCase("AUCTION")?new Auction(listingUID, creatorID, title, description):null);
    }
}
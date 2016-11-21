package dragonbids.structures.listings;

/**
 * Created by amir on 11/20/16.
 */
public class ListingFactory {
    private int x = 0;

    public ListingFactory() {
    }

    public Listing getListing(String Type, int listingUID, String creatorID, String title, String description) {
        return Type == null?null:(Type.equalsIgnoreCase("AUCTION")?new Auction(listingUID, creatorID, title, description):null);
    }
}
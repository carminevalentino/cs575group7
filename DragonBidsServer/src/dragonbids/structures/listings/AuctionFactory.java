package dragonbids.structures.listings;

import java.time.LocalDateTime;

/**
 * Created by amir on 11/20/16.
 * edited by lew on   12/05/16
 */
public class AuctionFactory implements ListingFactory_I {

    public AuctionFactory() {
    }

    @Override
    public Listing getListing(String Type, int listingUID, String creatorID, String title, String description, LocalDateTime expiration) {
       return new Auction(listingUID, creatorID, title, description, expiration);
    }
}
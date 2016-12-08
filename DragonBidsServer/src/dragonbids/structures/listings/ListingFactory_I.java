package dragonbids.structures.listings;

import java.time.LocalDateTime;

public interface ListingFactory_I {
	public Listing getListing(String Type, int listingUID, String creatorID, String title, String description, LocalDateTime expiration);
}

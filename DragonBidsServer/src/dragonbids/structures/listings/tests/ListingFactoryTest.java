package dragonbids.structures.listings.tests;

import dragonbids.structures.listings.ListingFactory_I;
import dragonbids.structures.listings.AuctionFactory;
import dragonbids.structures.listings.Listing;
import dragonbids.structures.listings.Auction;

import org.junit.Test;

import java.time.LocalDateTime;
import static org.junit.Assert.*;

/**
 * Created by amir on 11/30/16.
 */
public class ListingFactoryTest {


    @Test
    public void getListingAuctionTest() {
        ListingFactory_I LF = new AuctionFactory();
        Listing res = LF.getListing("AUCTION", 100, "User", "test", "test", LocalDateTime.now());
        assertEquals(res.getClass(), Auction.class);
    }


}
package dragonbids.structures.listings.test;

import dragonbids.structures.listings.ListingFactory;
import dragonbids.structures.listings.Listing;
import dragonbids.structures.listings.Auction;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by amir on 11/30/16.
 */
public class ListingFactoryTest {
    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void getListingAuctionTest() throws Exception {
        ListingFactory LF = new ListingFactory();
        Listing res = LF.getListing("AUCTION", 100, "User", "test", "test");
        assertEquals(res.getClass(), Auction.class);
    }

    @Test
    public void getListingNullTest() throws Exception {
        ListingFactory LF = new ListingFactory();
        Listing res = LF.getListing("Null", 100, "User", "test", "test");
        assertEquals(res, null);

    }
}
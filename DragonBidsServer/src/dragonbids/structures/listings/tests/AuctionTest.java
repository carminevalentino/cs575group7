package dragonbids.structures.listings.tests;

import dragonbids.structures.listings.Bid;
import dragonbids.structures.listings.BidHistory;
import org.junit.Test;
import  dragonbids.structures.listings.Auction;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * Created by amir on 12/7/16.
 */
public class AuctionTest {
    @Test
    public void addBid() throws Exception {
        Auction a= new Auction(0,"seller","test","test", LocalDateTime.MAX);

    }

    @Test
    public void getHighBid() throws Exception {

    }

    @Test
    public void getStartingBid() throws Exception {

    }

    @Test
    public void getBidHistory() throws Exception {

    }

}
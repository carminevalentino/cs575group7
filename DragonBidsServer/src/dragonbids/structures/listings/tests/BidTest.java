package dragonbids.structures.listings.tests;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by amir on 12/7/16.
 */
import dragonbids.structures.listings.Bid;

public class BidTest {
    @Test
    public void getBidder() throws Exception {
        Bid a= new Bid("user",1000);
        assertEquals(a.getBidder(),"user");
    }

    @Test
    public void getBidPrice() throws Exception {
        Bid a= new Bid("user",1000);
        assertEquals(a.getBidPrice(),1000);

    }

    @Test
    public void compareTo() throws Exception {
        Bid a= new Bid("user",1000);
        Bid b= new Bid("user",500);
        assertEquals(a.compareTo(b),500);
    }



}
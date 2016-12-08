package dragonbids.structures.listings.tests;

import dragonbids.structures.listings.Bid;
import dragonbids.structures.listings.BidHistory;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * Created by amir on 12/7/16.
 */
public class BidHistoryTest {
    @Before
    public void setUp() throws Exception {


    }

    @Test
    public void addBid() throws Exception {
        BidHistory a=new BidHistory();
        a.addBid("user0",0);
        //Using Reflection to access private fields.
        Field field =a.getClass().getDeclaredField("bids");
        field.setAccessible(true);
        LinkedList<Bid>b=(LinkedList<Bid>)field.get(a);
        assertEquals(1,b.size());

    }

    @Test
    public void addBidMulti() throws Exception {
        BidHistory a=new BidHistory();
        a.addBid("user0",0);
        a.addBid("user0",10);
        a.addBid("user0",20);
        a.addBid("user0",30);
        Field field =a.getClass().getDeclaredField("bids");
        field.setAccessible(true);
        LinkedList<Bid>b=(LinkedList<Bid>)field.get(a);
        assertEquals(4,b.size());

    }


    @Test
    public void getHighBid() throws Exception {
        BidHistory a=new BidHistory();
        a.addBid("user0",0);
        a.addBid("user10",10);
        a.addBid("user20",20);
        a.addBid("user30",30);
        Bid b= a.getHighBid();
        Bid c= new Bid("user30",30);
        assertEquals(b.getBidder(),c.getBidder());
        assertEquals(b.getBidPrice(),c.getBidPrice());
    }

    @Test
    public void getFirstBid() throws Exception {
        BidHistory a=new BidHistory();
        a.addBid("user0",0);
        a.addBid("user10",10);
        a.addBid("user20",20);
        a.addBid("user30",30);
        Bid b= a.getFirstBid();
        Bid c= new Bid("user0",0);
        assertEquals(b.getBidder(),c.getBidder());
        assertEquals(b.getBidPrice(),c.getBidPrice());
    }

}
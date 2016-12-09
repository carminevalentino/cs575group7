package dragonbids.structures.listings.tests;

import org.junit.Test;
import dragonbids.structures.listings.Listing;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

/**
 * Created by amir on 12/7/16.
 */
public class ListingTest {




    @Test
    public void getDesc() throws Exception {
        Listing a= new Listing(0, "test", "test", "test", null, 0, LocalDateTime.now());
        assertEquals(a.getDesc(),"test");

    }

    @Test
    public void getTitle() throws Exception {
        Listing a= new Listing(0, "test", "test", "test", null, 0, LocalDateTime.now());
        assertEquals(a.getTitle(),"test");
    }

    @Test
    public void getCurrentPrice() throws Exception {
        Listing a= new Listing(0, "test", "test", "test", null, 0, LocalDateTime.now());
        assertEquals(a.getCurrentPrice(),0);
    }

    @Test
    public void getExpiration() throws Exception {
        Listing a= new Listing(0, "test", "test", "test", null, 0, LocalDateTime.MAX);
        assertEquals(a.getExpiration(),LocalDateTime.MAX);

    }

    @Test
    public void getListingID() throws Exception {
        Listing a= new Listing(0, "test", "test", "test", null, 0, LocalDateTime.now());
        assertEquals(a.getListingID(),0);
    }

    @Test
    public void getCreator() throws Exception {
        Listing a= new Listing(0, "user", "test", "test", null, 0, LocalDateTime.now());
        assertEquals(a.getCreator(),"user");
    }


    @Test
    public void setExpiration() throws Exception {
        Listing a= new Listing(0, "test", "test", "test", null, 0, LocalDateTime.now());
        a.setExpiration(LocalDateTime.MAX);
        assertEquals(a.getExpiration(),LocalDateTime.MAX);
    }

    @Test
    public void setStartingPrice() throws Exception {
        Listing a= new Listing(0, "test", "test", "test", null, 0, LocalDateTime.now());
        a.setStartingPrice(20000);
        assertEquals(a.getCurrentPrice(),20000);
    }

    @Test
    public void setTitle() throws Exception {
        Listing a= new Listing(0, "test", "test", "test", null, 0, LocalDateTime.now());
        a.setTitle("test2");
        assertEquals(a.getTitle(),"test2");
    }

    @Test
    public void setDesc() throws Exception {
        Listing a= new Listing(0, "test", "test", "test", null, 0, LocalDateTime.now());
        a.setDesc("test2");
        assertEquals(a.getDesc(),"test2");
    }

    @Test
    public void timeLeft() throws Exception {
        Listing a= new Listing(0, "test", "test", "test", null, 0, LocalDateTime.now());
        assertEquals(a.timeLeft(),0);

    }


}
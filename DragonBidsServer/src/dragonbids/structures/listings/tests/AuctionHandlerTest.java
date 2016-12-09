package dragonbids.structures.listings.tests;

import dragonbids.api.ListingSkeleton;
import dragonbids.structures.listings.Auction;
import dragonbids.structures.listings.ListingHandlers.AuctionHandler;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

/**
 * Created by amir on 12/7/16.
 */
public class AuctionHandlerTest {
    // not working !
     ListingSkeleton buildSkeleton(String sellerUserName,
                                          String auctionTitle,
                                          String auctionDescription,
                                          String buyerUsername,
                                          BigDecimal currenPrice,
                                          int listingId,
                                          LocalDateTime auctionCompletionDateTime,
                                          BigDecimal proposedPrice
                                          ) throws Exception
    {
        ListingSkeleton t=new ListingSkeleton();
        t.sellerUsername=sellerUserName;
        t.auctionTile=auctionTitle;
        t.auctionDescription=auctionDescription;
        t.buyerUsername=buyerUsername;
        t.currentPrice=currenPrice;
        t.listingId=listingId;
        t.auctionCompletionDateTime= auctionCompletionDateTime;
        t.proposedPrice=proposedPrice;
        return t;
    }
    @Test
    public void modifyNoChange() throws Exception {

        AuctionHandler a=new AuctionHandler();
        ListingSkeleton modified=buildSkeleton("seller","test","test","buyer",new BigDecimal(10.10),0,LocalDateTime.MAX,new BigDecimal(0));
        Auction original= new Auction(0,"seller","test","test",LocalDateTime.MAX);
        String res=a.modify(original,modified);
        assertEquals(res,"");
    }

    @Test
    public void modifyTitle() throws Exception {

        AuctionHandler a=new AuctionHandler();
        ListingSkeleton modified=buildSkeleton("seller","test2","test","buyer",new BigDecimal(10.10),0,LocalDateTime.MAX,new BigDecimal(0));
        Auction original= new Auction(0,"seller","test","test",LocalDateTime.MAX);
        String res=a.modify(original,modified);
        assertEquals(res.toString(),"title changed to test2; ");

    }
    @Test
    public void modifyDesc() throws Exception {

        AuctionHandler a=new AuctionHandler();
        ListingSkeleton modified=buildSkeleton("seller","test","test2","buyer",new BigDecimal(10.10),0,LocalDateTime.MAX,new BigDecimal(0));
        Auction original= new Auction(0,"seller","test","test",LocalDateTime.MAX);
        String res=a.modify(original,modified);
        assertEquals(res.toString(),"description changed to test2; ");

    }

    @Test
    public void modifyBid() throws Exception {
        //not working
        /*
        AuctionHandler a=new AuctionHandler();
        ListingSkeleton modified=buildSkeleton("seller","test","test2","buyer",1000,0,LocalDateTime.MAX,10);
        Auction original= new Auction(0,"seller","test","test",LocalDateTime.MAX);
        String res=a.modify(original,modified);
        assertEquals(res.toString(),"description changed to test2; ");
        */

    }

}
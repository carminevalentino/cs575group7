package dragonbids.server.tests;

import dragonbids.api.ListingSkeleton;
import dragonbids.server.DragonBidsServer;
import dragonbids.server.RMIRegisterServer;
import org.codehaus.groovy.runtime.StringGroovyMethods;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Vector;

import static org.junit.Assert.*;

/**
 * Created by amir on 12/8/16.
 */
public class DragonBidsServerTest {
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
        t.listingTypes= ListingSkeleton.ListingTypes.AUCTION;
        return t;
    }
    @Test
    public void bindServerToRegisterSucc() throws Exception {
        DragonBidsServer s=new DragonBidsServer(false);
        RMIRegisterServer rmiRegister = new RMIRegisterServer();
        rmiRegister.createRegister(1099);
        assertEquals(s.bindServerToRegister(1099),true);
    }
    @Test
    public void bindServerToRegisterFail() throws Exception {
        DragonBidsServer s=new DragonBidsServer(false);
        RMIRegisterServer rmiRegister = new RMIRegisterServer();
        rmiRegister.createRegister(1099);
        s.bindServerToRegister(1099);
        assertEquals(s.bindServerToRegister(1099),false);
    }

    @Test
    public void unbindServerFromRegisterSucc() throws Exception {
        DragonBidsServer s=new DragonBidsServer(false);
        RMIRegisterServer rmiRegister = new RMIRegisterServer();
        rmiRegister.createRegister(1099);
        s.bindServerToRegister(1099);
        assertEquals(s.unbindServerFromRegister(),true);

    }

    @Test(expected = NullPointerException.class)
    public void unbindServerFromRegisterFail() throws Exception {
        DragonBidsServer s=new DragonBidsServer(false);
        RMIRegisterServer rmiRegister = new RMIRegisterServer();
        rmiRegister.createRegister(1099);
         s.unbindServerFromRegister();
    }

    @Test
    public void createUser() throws Exception {
            DragonBidsServer s=new DragonBidsServer(false);
            s.createUser("test");
            Vector<String> l=s.getUsers();
            assertEquals(l.lastElement(),"test");
    }

    @Test
    public void createListing() throws Exception {
        DragonBidsServer s=new DragonBidsServer(false);
        ListingSkeleton sk=buildSkeleton("seller","test","test2","buyer",new BigDecimal(10.10),0,LocalDateTime.MAX,new BigDecimal(0));
        s.createListing(sk);
        assertEquals(s.getListings().size(),1);

    }

    @Test
    public void getListings() throws Exception {
        DragonBidsServer s=new DragonBidsServer(false);
        ListingSkeleton sk1=buildSkeleton("seller","test1","test2","buyer",new BigDecimal(10.10),0,LocalDateTime.MAX,new BigDecimal(0));
        ListingSkeleton sk2=buildSkeleton("seller","test2","test2","buyer",new BigDecimal(10.10),0,LocalDateTime.MAX,new BigDecimal(0));
        ListingSkeleton sk3=buildSkeleton("seller","test3","test2","buyer",new BigDecimal(10.10),0,LocalDateTime.MAX,new BigDecimal(0));

        s.createListing(sk1);
        s.createListing(sk2);
        s.createListing(sk3);

        assertEquals(s.getListings().size(),3);
    }

    @Test
    public void getListing() throws Exception {
        DragonBidsServer s=new DragonBidsServer(false);
        ListingSkeleton sk1=buildSkeleton("seller","test1","test2","buyer",new BigDecimal(10.10),0,LocalDateTime.MAX,new BigDecimal(0));
        ListingSkeleton sk2=buildSkeleton("seller","test2","test2","buyer",new BigDecimal(10.10),0,LocalDateTime.MAX,new BigDecimal(0));
        ListingSkeleton sk3=buildSkeleton("seller","test3","test2","buyer",new BigDecimal(10.10),0,LocalDateTime.MAX,new BigDecimal(0));
        s.createListing(sk1);
        s.createListing(sk2);
        s.createListing(sk3);
        assertEquals(s.getListing(1).auctionTile,sk1.auctionTile);
    }

    @Test
    public void getUsers() throws Exception {
        DragonBidsServer s=new DragonBidsServer(false);
        s.createUser("test");
        s.createUser("test1");
        s.createUser("test2");
        Vector<String> l=s.getUsers();
        assertEquals(l.size(),3);
    }

    @Test
    public void modifyListing() throws Exception {
        DragonBidsServer s=new DragonBidsServer(false);
        ListingSkeleton sk=buildSkeleton("seller","test2","test2","buyer",new BigDecimal(10.10),0,LocalDateTime.MAX,new BigDecimal(0));
        s.createListing(sk);
        sk.listingId=1;
        sk.auctionTile="New Title";
        s.modifyListing(sk);
        assertEquals(s.getListing(1).auctionTile,"New Title");
    }

    @Test
    public void placeBid() throws Exception {
    }

    @Test
    public void removeListing() throws Exception {
    }

}
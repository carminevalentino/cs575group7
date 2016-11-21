package dragonbids.structures.listings;
import java.util.*;
import java.time.*;
/**
 * Write a description of class AuctionObserver here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class AuctionObserver implements Observer
{
    public void update(Observable a, Object arg)
    {
        if (arg instanceof BidChangedNotification)
        {
            System.out.println("New bid registered");
            System.out.println(arg.toString());
        }
    
        if (arg instanceof LocalDateTime)
        {
            System.out.println(a.toString() + " expiration changed to " + arg.toString());
        }
    }
}

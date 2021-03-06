package DragonBidsStructures.listings;
import java.sql.Timestamp;
import java.util.*;
import java.time.*;
import DragonBidsStructures.listings.ListingHandlers.*;

/**
 * @author Lew Cannalongo
 * @version 11-11-16
 */
public abstract class Listing extends Observable
{
    String title;
    String description;
    int listingID;
    long currentPrice;
    LocalDateTime createDate;
    LocalDateTime expirationDate;
    String creatorID;
    Object photo;
    
    // using a variety of setProperty() methods allows a proxy to easily manipulate a Listing

    public Listing(int listingUID, String creatorID, String title, String description, Object photo, int startingPrice, int duration)
    {
        this.createDate = new Timestamp(new java.util.Date().getTime()).toLocalDateTime();
        this.listingID = listingUID;
        this.creatorID = creatorID;
        
        setExpiration(createDate.plusMinutes(duration));
        
        setStartingPrice(startingPrice);
        setTitle(title);
        setDesc(description);
        setPhoto(photo);
        

    }
    
    public long timeLeft()
    {
        return Duration.between(LocalDateTime.now(), expirationDate).toMinutes();
    }
    
    public LocalDateTime getExpiration()
    {
        return expirationDate;
    }
    
    public void setExpiration(LocalDateTime endingTime)
    {
        expirationDate = endingTime;
        setChanged();
        notifyObservers(endingTime);
    }
    
    public void setStartingPrice(long price)
    {
        currentPrice = price;
    }
    
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    public void setDesc(String desc)
    {
        this.description = desc;
    }
    
    public void setPhoto(Object photo)
    {
        this.photo = photo;
    }
    
    public String toString()
    {
        return this.getClass() + " // Listing ID: " + listingID;
    }
}
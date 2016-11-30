package dragonbids.structures.listings;
import java.time.*;
import java.io.*;
import java.sql.Timestamp;

/**
 */
public class Bid implements Comparable<Bid>, Serializable
{
	private static final long serialVersionUID = 2256540585786924469L;
	private String userid;
    long bidPrice;
    LocalDateTime datePlaced;
    
    public Bid(String userid, long bidPrice)
    {
        this.userid = userid;
        this.bidPrice = bidPrice;
        datePlaced = new Timestamp(new java.util.Date().getTime()).toLocalDateTime();
    }
    
    public String getBidder()
    {
        return userid;
    }
    
    public long  getBidPrice()
    {
        return bidPrice;
    }
    
    public int compareTo(Bid b)
    {
        return  (int) (this.getBidPrice() - b.getBidPrice());
    }
    
    public String toString()
    {
        return "UserID: " + userid + " Bid Price: " + bidPrice;
    }
    }

package dragonbids.api;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.time.LocalDateTime;

public class ListingSkeleton implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int listingId;
	public String sellerUsername;
	public String buyerUsername;
	public String auctionTile;
	public String auctionDescription;
	public LocalDateTime auctionCompletionDateTime;
	public int extendAuctionMinutes;
	public long currentPrice;
	public long proposedPrice;
	
	public enum ListingTypes
	{
		DEFAULT,
		AUCTION
		// Other listing types could be added in the future
	}
	
	public ListingTypes listingTypes;
	
	public ListingSkeleton() throws RemoteException
	{
		this.listingId = -1;
		this.sellerUsername = "";
		this.buyerUsername = "";
		this.auctionTile = "";
		this.auctionDescription = "";
		this.extendAuctionMinutes = -1;
		this.currentPrice = -1;
		this.proposedPrice = -1;
		this.auctionCompletionDateTime = null;
		this.listingTypes = ListingTypes.DEFAULT;
	}
	
	@Override
	public String toString()
	{
		return this.auctionTile;
	}
}

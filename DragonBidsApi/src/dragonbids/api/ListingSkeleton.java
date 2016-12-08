package dragonbids.api;

import java.io.Serializable;
import java.math.BigDecimal;
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
	public BigDecimal currentPrice;
	public BigDecimal proposedPrice;
	
	public ListingSkeleton() throws RemoteException
	{
		this.listingId = -1;
		this.sellerUsername = "";
		this.buyerUsername = "";
		this.auctionTile = "";
		this.auctionDescription = "";
		this.extendAuctionMinutes = -1;
		this.currentPrice = new BigDecimal(-1);
		this.proposedPrice = new BigDecimal(-1);
		this.auctionCompletionDateTime = null;
	}
	
	@Override
	public String toString()
	{
		return this.auctionTile;
	}
}

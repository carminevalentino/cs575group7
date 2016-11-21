package dragonbids.api;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;

public class ListingSkeleton extends UnicastRemoteObject{
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
	}
	
	@Override
	public String toString()
	{
		return this.auctionTile;
	}
}

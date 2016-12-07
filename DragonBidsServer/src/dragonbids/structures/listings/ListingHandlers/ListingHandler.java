package dragonbids.structures.listings.ListingHandlers;
import dragonbids.api.*;
import dragonbids.structures.listings.*;
/**
 */
public interface ListingHandler
{
	public abstract String modify(Listing lst, ListingSkeleton skele);
	
//	public abstract boolean modify(ListingSkeleton skele);  //candidate for deprecation
//    public abstract boolean modifyDesc(String newDesc); //candidate for deprecation
//    public abstract boolean modifyTitle(String newTitle); //candidate for deprecation
//    public abstract boolean extendListing(long minutes); //candidate for deprecation
////    public abstract boolean modifyPhoto(Object photo); //candidate for deprecation

}
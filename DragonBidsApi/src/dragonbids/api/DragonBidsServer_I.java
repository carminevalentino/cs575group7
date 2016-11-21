package dragonbids.api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

// This will declare methods which the client can remotely call on the server
// The instance of the server will inherit from this interface
public interface DragonBidsServer_I extends Remote {
	
	public boolean createListing(ListingSkeleton listing) throws RemoteException;
	public Vector<ListingSkeleton> getListings() throws RemoteException;
	public boolean modifyListing(ListingSkeleton listing) throws RemoteException;
	public boolean placeBid(ListingSkeleton listing) throws RemoteException;
	public boolean createUser(String username) throws RemoteException;
	public boolean removeListing(int listingId) throws RemoteException;
	public Vector<String> getUsers() throws RemoteException;
}

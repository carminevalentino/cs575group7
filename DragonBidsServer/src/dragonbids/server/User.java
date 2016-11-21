package dragonbids.server;

public class User {
	private String username;
	//private Vector<Listings> itemsForSale = new Vector<Listings>();
	/*
	 * Conceivably, the attributes of a user can be extended by this class
	 */
	public User(String _username)
	{
		username = _username;
		System.out.println("User " + username + ", has been created ...");
	}
	
	public String getUsername()
	{
		return this.username;
	}
	
}

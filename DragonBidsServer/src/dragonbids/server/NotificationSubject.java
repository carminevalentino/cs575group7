/*
 * The purpose of this class is to allow User (Objects) to be registered
 * As Observers to the Object which instantiates a particular Subject to
 * be observed.
 * 
 */
package dragonbids.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Iterator;
import java.util.Vector;

import dragonbids.api.NotificationObserver_I;

public class NotificationSubject {

	private Vector<User> usersToBeNotified = new Vector<User>();
	public void attach(User user) {
		usersToBeNotified.add(user);
	}

	public void detatch(User user) {
		usersToBeNotified.remove(user);
	}

	// Notify all Users Registered
	public void notifyObservers(String notification) {
		String username = "";
		Iterator<User> it = usersToBeNotified.iterator();
		while(it.hasNext())
		{
			username = it.next().getUsername().toString();
			try{
				Registry registry = LocateRegistry.getRegistry(); //Works only on localhosts, meaning we have to run the server from the same machine/subnet;
				NotificationObserver_I stub = (NotificationObserver_I) registry.lookup(username); //This is a Key to Retrieve the Object from the Registry; Registry is a String:Object pair;
				stub.notifyClient(notification); //Notify Client
			}
			catch (Exception e)
			{
			}
		}
	}
	
	// Notify a Specific User Registered
	public void notifyObservers(User user, String notification)
	{
		String username = user.getUsername().toString();
		Iterator<User> it = usersToBeNotified.iterator();
		while(it.hasNext())
		{
			if (it.next().getUsername().equals(username))
			{
				try{
					Registry registry = LocateRegistry.getRegistry(); //Works only on localhosts, meaning we have to run the server from the same machine/subnet;
					NotificationObserver_I stub = (NotificationObserver_I) registry.lookup(username); //This is a Key to Retrieve the Object from the Registry; Registry is a String:Object pair;
					stub.notifyClient(notification); //Notify Client
				}
				catch (Exception e)
				{
					System.out.println("Error: Tried to notify user " + username + ", but user was not currently registered");
				}
			}
			else
			{
				System.out.println("Error: Tried to notify user " + username + ", but user was not currently registered");
		}
		}
	}
}

package dragonbids.client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import dragonbids.api.*;

public class Communication implements NotificationObserver_I {
	private static Communication instance = null;
	protected Communication(String username){
        try {
        	NotificationObserver_I stub = (NotificationObserver_I) UnicastRemoteObject.exportObject(this, 0);
        	Registry registry = LocateRegistry.getRegistry();
        	
        	registry.rebind(username, stub);
        	System.out.println("Created Notification Observer for User: " + username + "...");
            
        } catch (Exception e) {
            System.err.println("Error Creating Notification Observer for User : " + username + " " +  e.toString());
            e.printStackTrace();
        }
	};
	
	public static Communication getCommunication(String username)
	{
	      if(instance == null) {
		         instance = new Communication(username);
		      }
		      return instance;
	}

	@Override
	public void notifyClient(String notification) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println(notification);
		
	}
	
}

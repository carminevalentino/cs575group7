
package dragonbids.server;

import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIRegisterServer {
	Registry currentRegistry;
	
	public boolean createRegister()
	{
		boolean registerCreated = false;
		try {
			currentRegistry = java.rmi.registry.LocateRegistry.createRegistry(1099);
			System.out.println("RMI Register Initialized at Port " + 1099 + "...");
		    registerCreated = true;
		} catch (Exception e) {
		    System.out.println("Exception starting RMI registry:");
		    e.printStackTrace();
		    registerCreated = false;
		}
		return registerCreated;
	}
	
	public boolean createRegister(int port)
	{
		boolean registerCreated = false;
		try {
			currentRegistry = java.rmi.registry.LocateRegistry.createRegistry(port);
			System.out.println("RMI Register Initialized at Port " + port + "...");
		    registerCreated = true;
		} catch (Exception e) {
		    System.out.println("Exception starting RMI registry:");
		    e.printStackTrace();
		    registerCreated = false;
		}
		return registerCreated;
	}
	
	public boolean destroyRegister()
	{
		boolean registerDestroyed = false;
		
		if (currentRegistry != null) {
			try{
				UnicastRemoteObject.unexportObject(currentRegistry, true);
				registerDestroyed = true;
			}
			catch (Exception e)
			{
				System.out.println("Exception stopping RMI registry:");
				registerDestroyed = false;
			}
		}
		return registerDestroyed;
	}
}

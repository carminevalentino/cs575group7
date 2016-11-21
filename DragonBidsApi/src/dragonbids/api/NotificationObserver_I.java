package dragonbids.api;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NotificationObserver_I extends Remote {
	public void notifyClient(String notification) throws RemoteException;
}

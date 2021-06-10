package sk.tondrus.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ObchodDataListener extends Remote {

    public void NotifyUser() throws RemoteException;
}

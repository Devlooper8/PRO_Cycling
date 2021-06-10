package sk.tondrus.RMI;

import sk.tondrus.Exceptions.InvalidUserException;
import sk.tondrus.Exceptions.UnautorizedAccess;
import sk.tondrus.Exceptions.UserAlreadyExist;
import sk.tondrus.Exceptions.WrongPasswordException;
import sk.tondrus.databaza.databazaEntities.Objednavka;
import sk.tondrus.databaza.databazaEntities.Parts;
import sk.tondrus.databaza.databazaEntities.Product;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public interface Obchod extends Remote {
    public int login(String Email, String Password) throws RemoteException, InvalidUserException, WrongPasswordException;

    public int register(String Email, String Password) throws RemoteException, SQLException, UserAlreadyExist;

    public void logout(int userId) throws RemoteException;

    public List<Product> loadKatalog(int userId, int typBicykla) throws RemoteException, SQLException, UnautorizedAccess;

    public List<Parts> loadParts(int userId, int productID) throws RemoteException, SQLException, UnautorizedAccess;

    public Product getProductDetail(int userId, int productID) throws RemoteException, SQLException, UnautorizedAccess;

    public int objednaj(int userId, Objednavka objednavka) throws RemoteException, SQLException, UnautorizedAccess;

    public List<Objednavka> UserLoadObjednavky(int userId) throws RemoteException, SQLException, UnautorizedAccess;

    public void UserDeleteObjednavky(int userId, int idObjednavky) throws RemoteException, SQLException, UnautorizedAccess;

    public void addListener(ObchodDataListener obchodDataListener) throws RemoteException;

    public void removeListener(ObchodDataListener obchodDataListener) throws RemoteException;
}

package sk.tondrus;

import org.apache.log4j.Logger;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import sk.tondrus.Exceptions.InvalidUserException;
import sk.tondrus.Exceptions.UnautorizedAccess;
import sk.tondrus.Exceptions.UserAlreadyExist;
import sk.tondrus.Exceptions.WrongPasswordException;
import sk.tondrus.RMI.Obchod;
import sk.tondrus.RMI.ObchodDataListener;
import sk.tondrus.databaza.*;
import sk.tondrus.databaza.databazaEntities.*;
import sk.tondrus.multiThreading.Robotnik;
import sk.tondrus.multiThreading.Skladnik;
import sk.tondrus.multiThreading.Udalost;
import sk.tondrus.multiThreading.Zamestnanec;
import sk.tondrus.view.MainScreenFabrika;

import java.rmi.AccessException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author Tomáš
 * Hlavna trieda ktora mi spusta aplikaciu na strane servera
 */
public class AppFabrika extends SingleFrameApplication implements Obchod {
    private static Logger logger = Logger.getLogger(AppFabrika.class);
    private static final String ServerName = "Fabrika";
    private Registry registry;
    private Database database; //pouzita agregacia
    private HashSet<Integer> loggedUsers=new HashSet<>();
    private HashSet<Integer> orders= new HashSet<>();
    private List<ObchodDataListener> obchodDataListeners= new ArrayList<>();//kompozicia
    private Udalost udalost = new Udalost();
    private List<Zamestnanec> zamestnanci = new ArrayList<>();//kompozicia
    private int SystemID=-125;
    private MainScreenFabrika mainFrame;


    /**
     * vytvori a zobrazi hlavne okno aplikacie
     */
    @Override
    protected void startup() {
         mainFrame = new MainScreenFabrika();
         mainFrame.setTitle("AppFabrika");
        setMainFrame(mainFrame);
        try {
            addListener(mainFrame);
        } catch (RemoteException exception) {
            exception.printStackTrace();
        }
        mainFrame.setName("Okno");
        show(mainFrame);
    }

    private void checkUser(int userId) throws UnautorizedAccess {
        if(userId==SystemID)
            return;
        if (!loggedUsers.contains(userId)){
            throw new UnautorizedAccess("Neautorizovany pristup");
        }
    }


    /**
     * nastavi potrebne premenne, databazu a robotnikov
     * @param args args
     */
    @Override
    protected void initialize(String[] args) {
        database = new Database("jdbc:sqlite:C:\\Users\\Tomáš\\IdeaProjects\\PRO_Cycling\\Databases\\PRO_Cycling.db");
        try {
            registry = ensureRegistry(1099);
            Obchod obchod = (Obchod) UnicastRemoteObject.exportObject(AppFabrika.this, 0);
            registry.rebind(ServerName, obchod);
            logger.info("Server ready");
            zamestnanci.add(new Skladnik("Fero",udalost, Udalost.PRISLA_OBJEDNAVKA));
            zamestnanci.add(new Robotnik("Filip",udalost,Udalost.VYROBIT));
            zamestnanci.add(new Robotnik("Peto",udalost,Udalost.VYROBIT));
            zamestnanci.add(new Robotnik("Adam",udalost,Udalost.VYROBIT));

            for (Zamestnanec zamestnanec : zamestnanci){
                zamestnanec.start();
            }

            udalost.zobudZamestnancov(Udalost.PRISLA_OBJEDNAVKA);
        } catch (Exception e) {
            System.err.println("Server exception" + e.getMessage());
            exit();
        }

    }

    /**
     * vypne aplikaciu
     */
    @Override
    protected void shutdown() {
        // The default shutdown saves session window state.
        super.shutdown();
        // Now perform any other shutdown tasks you need.
        database.disconnect();
        for (Zamestnanec zamestnanec : zamestnanci){
            zamestnanec.zastav();
        }
        try {
            destroyRegistry();
        } catch (NoSuchObjectException e) {
            e.printStackTrace();
        }
    }

    /**
     * ziska databazu
     * @return databaza
     */
    public Database getDatabase() {
        return database;
    }

    public static void main(String[] args) {
        Application.launch(AppFabrika.class, args);
    }




    /* LOGIN SCREEN FUNCTIONS*/

    /**
     * Ziska parametre z databazy porovna ci sa zhoduju s parametrami zo vstupu a  prihlasi pouzivatela
     * @param Email email ktory bol nacitany zo vstupu
     * @param Password heslo ktore bolo nacitane zo vstupu
     * @return vrati ID prihlaseneho pouzivatela
     * @throws RemoteException
     * @throws InvalidUserException
     * @throws WrongPasswordException
     */
    @Override
    public int login(String Email, String Password) throws RemoteException, InvalidUserException, WrongPasswordException {
        UserFactory factory = new UserFactory();
        java.util.List<User> users = database.select(factory, "SELECT * FROM USERS WHERE EMAIL LIKE '" + Email+"'");
        if (users.size() == 0)
            throw new InvalidUserException();
        users = database.select(factory, "SELECT * FROM USERS WHERE EMAIL LIKE '" + Email
                + "' AND PASSWORD LIKE '" + Password + "'");
        if (users.size() == 0)
            throw new WrongPasswordException();
        loggedUsers.add(users.get(0).getID());
        return users.get(0).getID();
    }

    /**
     * Zapise parametre do databazy a prihlasi pouzivatela
     * @param Email email ktory bol nacitany zo vstupu
     * @param Password heslo ktore bolo nacitane zo vstupu
     * @return vrati id pouzivatela
     * @throws RemoteException
     * @throws SQLException
     * @throws UserAlreadyExist
     */
    @Override
    public int register(String Email, String Password) throws RemoteException, SQLException, UserAlreadyExist {
        UserFactory factory = new UserFactory();
        java.util.List<User> users = database.select(factory, "SELECT * FROM USERS WHERE EMAIL LIKE '" + Email+"'");
        if (users.size() > 0)
            throw new UserAlreadyExist();
        int id= database.insert(factory,new User(Email,Password));
        loggedUsers.add(id);
        return id;

    }




    /*MAIN SCREEN USER ODHLASIT FUNCTION*/

    /**
     * odhlasi pouzivatela
     * @param userId podla id odhlasi pouzivatela
     */
    @Override
    public void logout(int userId) {
        loggedUsers.remove(userId);
    }






    /*KATALOG FUNCTIONS*/

    /**
     * Nacita katalog podla typu bicykla
     * @param userId id pouzivatela
     * @param TypBicykla typ bicykla ktory sa ma zobrazit
     * @return
     * @throws RemoteException
     * @throws SQLException
     * @throws UnautorizedAccess
     */
    @Override
    public List<Product> loadKatalog(int userId, int TypBicykla) throws RemoteException, SQLException, UnautorizedAccess {
        checkUser(userId);
        ProductFactory productFactory = new ProductFactory();
        String sql="SELECT * FROM Katalog";

           if(TypBicykla>-1)
                sql+= " WHERE TypBicykla= "+TypBicykla;

        String[] polozky={ Product.ID,Product.CENA,Product.NAHLAD,Product.POPIS};
        java.util.List<Product> products = database.select(productFactory,sql,polozky);
        return products;
    }


    /**
     * Nacita detail produktu ktory bol v katalogu
     * @param userId id pouzivatela
     * @param productID id produktu ktory sa ma zobrazit
     * @return vracia produkt
     * @throws RemoteException
     * @throws SQLException
     * @throws UnautorizedAccess
     */
    @Override
    public Product getProductDetail(int userId, int productID) throws RemoteException, SQLException, UnautorizedAccess {
        checkUser(userId);
        ProductFactory productFactory = new ProductFactory();
        String[] polozky={ Product.ID,Product.CENA,Product.OBRAZOK,Product.POPIS,Product.TYPBICYKLA};
        java.util.List<Product> products = database.select(productFactory, "SELECT * FROM Katalog WHERE ID="+productID,polozky);
        List<Parts> parts= loadParts(userId,productID);
        Product product=products.get(0);
        product.setParts(parts);
        return product;
    }

    /**
     * Nacita casti bicykla ktore obsahuje produkt
     * @param userId id pouzivatela
     * @param productID id produktu podla ktoreho sa nacitaju casti bicykla
     * @return list suciastok
     * @throws RemoteException
     * @throws SQLException
     * @throws UnautorizedAccess
     */
    @Override
    public List<Parts> loadParts(int userId,int productID) throws RemoteException, SQLException, UnautorizedAccess {
        checkUser(userId);
        PartsFactory partsFactory = new PartsFactory();
        String sql="SELECT PRODUKT_ID,NAZOV,TYP FROM Kusovnik INNER JOIN PARTS_VARIANTS ON VARIANT_ID= PARTS_VARIANTS.ID INNER JOIN PARTS_TYPE T ON PARTS_TYPE= T.ID WHERE PRODUKT_ID = "+productID;
        java.util.List<Parts> parts = database.select(partsFactory,sql);
        return parts;
    }

    /**
     * Vytvori objednavku a vlozi ju do databazy
     * @param userId id pouzivatela
     * @param objednavka objekt ktory sa ma vlozit do databazy
     * @return vracia id objednavky
     * @throws RemoteException
     * @throws SQLException
     * @throws UnautorizedAccess
     */
    @Override
    public int objednaj(int userId, Objednavka objednavka)throws  RemoteException, SQLException, UnautorizedAccess {
        checkUser(userId);
        ObjednavkaFactory objednavkaFactory= new ObjednavkaFactory();
        objednavka.setStav(Objednavka.VYTVORENA);
        int order_id= database.insert(objednavkaFactory,objednavka);
        orders.add(order_id);
        ((ObchodDataListener)getMainFrame()).NotifyUser();
        udalost.zobudZamestnancov(Udalost.PRISLA_OBJEDNAVKA,1);
        return order_id;

    }

    /**
     * Nacita objednavky ktore boli vytvorene a cakaju na spracovanie
     * @return list objednavok
     * @throws RemoteException
     * @throws SQLException
     */
    public List<Objednavka> LoadObjednavkySkladnik() throws RemoteException,SQLException{
        ObjednavkaFactory objednavkaFactory = new ObjednavkaFactory();
        String[] polozky={ Objednavka.ORDER_ID,Objednavka.ID_BICYKLA};
        java.util.List<Objednavka> objednavky = database.select(objednavkaFactory, "SELECT ORDER_ID,ID_BICYKLA,DATUM,Cena, Popis FROM ORDERS INNER JOIN Katalog ON ID_BICYKLA= Katalog.ID WHERE STAV = "+Objednavka.VYTVORENA,polozky);
        return objednavky;
    }

    /**
     * nacita vsetky suciastky ktore sa v sklade nachadzaju
     * @return list suciastok
     * @throws RemoteException
     * @throws SQLException
     */
    public List<Sklad> LoadSklad()throws RemoteException, SQLException{
        SkladFactory skladFactory= new SkladFactory();
    java.util.List<Sklad>skladList;
    String[] polozky= {Sklad.NUM_OF,Sklad.PART_ID,Sklad.NAZOV};
    skladList= database.select(skladFactory,"SELECT ID, NAZOV,VOLUME FROM PARTS_VARIANTS");
    return skladList;
    }

    /**
     * Naskladni potrebny pocet suciastok aby sa mohli vyrobit dalsie bicykle
     * @throws RemoteException
     * @throws SQLException
     */
    public void Naskladni() throws RemoteException,SQLException{
        SkladFactory skladFactory = new SkladFactory();
        database.updateSklad(skladFactory);
    }

    /**
     * Odcita z databazy diely ktore treba na vyrobenie bicykla z danej objednavky
     * @param objednavka z objednavky sa zisti ake diely treba odcitat z databazy
     * @throws RemoteException
     * @throws SQLException
     */
    public void SkladObjednavka(Objednavka objednavka) throws  RemoteException, SQLException{
        SkladFactory skladFactory= new SkladFactory();
        database.updateSkladObjednavka(skladFactory, objednavka);
    }

    /**
     * Aktualizuje stav objednavky na VYRABA SA a vrati ju
     * @return objednavka
     * @throws RemoteException
     * @throws SQLException
     */
    public synchronized Objednavka PriradObjednavku()throws RemoteException,SQLException{
        ObjednavkaFactory objednavkaFactory = new ObjednavkaFactory();
        String[] polozky={ Objednavka.ORDER_ID,Objednavka.ID_BICYKLA};
        java.util.List<Objednavka> objednavka= database.select(objednavkaFactory,"SELECT ORDER_ID,ID_BICYKLA,DATUM,Cena, Popis FROM ORDERS INNER JOIN Katalog ON ID_BICYKLA= Katalog.ID WHERE STAV = "+Objednavka.SPRACOVANA+" Limit 1",polozky);
            if(objednavka.size()==0)
                return null;
        String[] vlastnosti={Objednavka.STAV};
        Object[]values={Objednavka.VYRABA_SA};
        database.update(objednavkaFactory, vlastnosti, values, objednavka.get(0).getOrderID());
        NotifyUserDataChanged();
    return objednavka.get(0);
    }

    /* USER TABULKA OBJEDNAVOK*/

    /**
     * Nacita vsetky objednavky ktore dany pouzivatel urobil
     * @param userId id pouzivatela podla ktoreho sa nacitaju objednavky z databazy
     * @return vrati list objednavok
     * @throws RemoteException
     * @throws SQLException
     * @throws UnautorizedAccess
     */
    @Override
    public List<Objednavka> UserLoadObjednavky(int userId) throws RemoteException, SQLException, UnautorizedAccess {
        checkUser(userId);
        ObjednavkaFactory objednavkaFactory = new ObjednavkaFactory();
        String[] polozky={ Objednavka.ORDER_ID,Objednavka.CENA,Objednavka.POPIS,Objednavka.STAV };
        java.util.List<Objednavka> objednavky = database.select(objednavkaFactory, "SELECT ORDER_ID,ID_BICYKLA,DATUM,Cena, Popis, STAV FROM ORDERS INNER JOIN Katalog ON ID_BICYKLA= Katalog.ID WHERE USER_ID= "+userId,polozky);
        return objednavky;
    }

    /**
     * Kym nebola objednavka spracovana je mozne aby pouzivatel odstranil objednavku
     * @param userId id pouzivatela podla ktoreho sa odstrani objednavka z databazy
     * @param idObjednavky id objednavky podla ktoreho sa odstrani objednavka
     * @throws RemoteException
     * @throws SQLException
     * @throws UnautorizedAccess
     */
    @Override
    public void UserDeleteObjednavky(int userId, int idObjednavky) throws RemoteException, SQLException, UnautorizedAccess {
        checkUser(userId);
        ObjednavkaFactory objednavkaFactory = new ObjednavkaFactory();
        java.util.List<Objednavka> objednavky = database.select(objednavkaFactory, "SELECT STAV FROM ORDERS WHERE ORDER_ID=" + idObjednavky, new String[]{Objednavka.STAV});
        if (objednavky.size() == 0) {
            throw new RemoteException();
        } else if (objednavky.get(0).getStav() > Objednavka.VYTVORENA) {
            throw new SQLException();
        } else {
            database.delete(objednavkaFactory, idObjednavky);
            ((ObchodDataListener)getMainFrame()).NotifyUser();
        }

    }

    /*RMI CALLBACK LISTENER FUNCTIONS*/

    /**
     * prida listener aby mohli aplikacie medzi sebou komunikovat
     * @param obchodDataListener vstupny listener ktory bude komunikovat
     * @throws RemoteException
     */
    @Override
    public void addListener(ObchodDataListener obchodDataListener)throws RemoteException{
        obchodDataListeners.add(obchodDataListener);
    }

    /**
     *odstrani listener pri ukonceni aplikacie
     * @param obchodDataListener vstupny listener ktory bude komunikovat
     * @throws RemoteException
     */
    @Override
    public void removeListener(ObchodDataListener obchodDataListener)throws RemoteException {
        obchodDataListeners.remove(obchodDataListener);
    }




    /* FABRIKA TABULKA OBJEDNAVOK FUNCTIONS*/

    /**
     * nacita vsetky objednavky ktore boli urobene
     * @return vrati list objednavok
     * @throws RemoteException
     * @throws SQLException
     */
    public List<Objednavka> FabrikaLoadObjednavky()throws RemoteException, SQLException{
        ObjednavkaFactory objednavkaFactory = new ObjednavkaFactory();
        String[] polozky={ Objednavka.ORDER_ID,Objednavka.CENA,Objednavka.POPIS, Objednavka.USER_ID,Objednavka.STAV};
        java.util.List<Objednavka> objednavky= database.select(objednavkaFactory,"SELECT ORDER_ID, ID_BICYKLA,DATUM,USER_ID, Cena, Popis, STAV FROM ORDERS INNER JOIN Katalog ON ID_BICYKLA= Katalog.ID ORDER BY DATUM ASC",polozky);
        return objednavky;
    }

    /**
     * nastavi stav objednavky na SPRACOVANA
     * @param IdObjednavky id objednavky podla ktoreho sa nastavi stav konkretnej objednavke
     * @throws RemoteException
     * @throws SQLException
     */
    public void ZacniVyrobu(int IdObjednavky)throws RemoteException,SQLException{
        ObjednavkaFactory objednavkaFactory = new ObjednavkaFactory();
        String[] vlastnosti={Objednavka.STAV};
        Object[]values={Objednavka.SPRACOVANA};
        database.update(objednavkaFactory,vlastnosti,values,IdObjednavky);
        NotifyUserDataChanged();
    }


    /**
     * oznami aplikacii Fabrika aj User ze sa zmenili data v tabulke a maju si aktualizovat udaje na obrazovke
     */
    public void NotifyUserDataChanged(){
        for(ObchodDataListener obchodDataListener: obchodDataListeners){
            try {
            //    logger.debug("Notify data");
                obchodDataListener.NotifyUser();

            } catch (RemoteException exception) {
                exception.printStackTrace();
            }
        }
    }


    /*FABRIKA RMI COMMUNICATION*/

    /**
     * nastavi register na komunikaciu medzi aplikaciami
     * @param port port na ktorom dane aplikacie komunikuju
     * @return vracia register
     * @throws RemoteException
     */
    private Registry ensureRegistry(int port) throws RemoteException {
        Registry registry = LocateRegistry.getRegistry(port);
        if (exists(registry))
            System.out.println("Located an RMI registry at port " + port);
        else {
            System.out.println("Sarting RMI registry on 'localhost:' " + port + "'");
            registry = LocateRegistry.createRegistry(port);
        }
        return registry;
    }

    /**
     * najde existujuce spojenie a vrati false
     * @param reg vstupny register
     * @return vracia false alebo true podla toho ci sa uspesne naslo spojenie so serverom;
     */
    private boolean exists(Registry reg) {
        if (reg == null)
            return false;
        try {
            reg.lookup(ServerName);
            return true;
        } catch (NotBoundException e) {
            System.out.println(ServerName + " not bound");
        } catch (RemoteException e) {
            System.out.println("Failed to communicate with registry");
            reg = null;
        }
        return false;
    }

    /**
     * ukonci spojenie medzi aplikaciami
     * @throws NoSuchObjectException
     */
    private void destroyRegistry() throws NoSuchObjectException {
        if (registry != null)
            try {
                System.out.println("Unbinding from registry...");
                registry.unbind(ServerName);
                UnicastRemoteObject.unexportObject(registry, true);
            } catch (NotBoundException e) {
                System.out.println("Server '" + ServerName + "' was already unbound");
            } catch (AccessException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
    }

}
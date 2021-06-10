package sk.tondrus;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import sk.tondrus.RMI.Obchod;
import sk.tondrus.RMI.ObchodDataListener;
import sk.tondrus.databaza.databazaEntities.Objednavka;
import sk.tondrus.view.LoginScreen;
import sk.tondrus.view.MainScreenZakaznik;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class AppZakaznik extends SingleFrameApplication implements ObchodDataListener {
    private Obchod obchod;
    private String host = "localhost";
    private Registry registry;
    private int userID = 1;
    private Objednavka objednavka;
    private int stav=0;
    private MainScreenZakaznik mainFrame;
    private ObchodDataListener dataListener;

    public Objednavka getObjednavka() {
        return objednavka;
    }

    public void setObjednavka(Objednavka objednavka) {
        this.objednavka = objednavka;
    }

    /**
     * vrati obchod
     * @return obchod
     */
    public Obchod getObchod() {
        return obchod;
    }

    public void setObchod(Obchod obchod) {
        this.obchod = obchod;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Registry getRegistry() {
        return registry;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    /**
     * vrati id pouzivatela
     * @return userID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * nastavi id pouzivatela
     * @param userID id pouzivatela
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getStav() {
        return stav;
    }

    /**
     * nastavi stav podla toho ci sa pouzivatel odhlasil alebo vypol aplikaciu
     * @param stav premenna ktora urcuje ci sa pouzivatel odhlasil alebo vypol aplikaciu
     */
    public void setStav(int stav) {
        this.stav = stav;
    }

    /**
     * vytvori a zobrazi hlavne okno aplikacie
     */
    @Override
    protected void startup() {
        LoginScreen loginScreen = null;
        loginScreen = new LoginScreen();
        loginScreen.setVisible(true);
        setUserID(loginScreen.getUserID());
        if (loginScreen.getUserID() == 0) {
            exit();
        } else {

            // Som prihásený
            mainFrame = new MainScreenZakaznik();
            mainFrame.addWindowListener(new Windowdestroy());
            mainFrame.setTitle("AppZákazník");
            mainFrame.setName("Okno");
            //setMainFrame(mainFrame);
            show(mainFrame);
        }
    }

    /**
     * nastavi potrebne premenne, komunikaciu RMI
     * @param args args
     */
    protected void initialize(String[] args) {
        try {
            registry = LocateRegistry.getRegistry(host);
            obchod = (Obchod) registry.lookup("Fabrika");
            dataListener=(ObchodDataListener) UnicastRemoteObject.exportObject( this, 0);
            obchod.addListener(dataListener);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * vypne aplikaciu
     */
    @Override
    protected void shutdown() {
        // The default shutdown saves session window state.
        super.shutdown();
        try {
            obchod.removeListener(this);
            if(dataListener !=null)
            UnicastRemoteObject.unexportObject(this,true);
            obchod.logout(userID);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    /**
     * informuje pouzivatela ze sa zmenili data v databaze a mal by prekreslit data na obrazovke
     * @throws RemoteException
     */
    @Override
    public void NotifyUser() {

        if(mainFrame!=null){
            try {
                mainFrame.NotifyUser();
            } catch (RemoteException exception) {
                exception.printStackTrace();
            }
        }
    }



    private class Windowdestroy extends WindowAdapter {
        public void windowClosed(WindowEvent e) {
            mainFrame=null;
            if (stav == 1) {
                stav=0;
                startup();
            }
            else
                shutdown();
        }
    }

    public static void main(String[] args) {
        Application.launch(AppZakaznik.class, args);
    }
}

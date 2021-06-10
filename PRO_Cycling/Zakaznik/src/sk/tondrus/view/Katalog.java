package sk.tondrus.view;

import org.jdesktop.application.Application;
import sk.tondrus.AppZakaznik;
import sk.tondrus.Exceptions.UnautorizedAccess;
import sk.tondrus.databaza.databazaEntities.Product;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.sql.SQLException;
/*
 * Created by JFormDesigner on Fri Apr 09 22:01:42 CEST 2021
 */


/**
 * @author Tomas Ondrus
 */
public class Katalog extends JPanel {

    public Katalog() {
        initComponents();
    }

    /**
     * vypise katalog z databazy podla typu bicykla
     * @param TypBicykla urcuje ktory typ sa ma vypisat
     * @throws RemoteException
     * @throws SQLException
     * @throws UnautorizedAccess
     */
    public void showKatalog(int TypBicykla) throws RemoteException, SQLException, UnautorizedAccess {
        AppZakaznik appZakaznik = (AppZakaznik) Application.getInstance();
        java.util.List<Product> productList = appZakaznik.getObchod().loadKatalog(appZakaznik.getUserID(),TypBicykla);

        for (Product product : productList) {

            ProduktInfo Pi = new ProduktInfo(product);
            obsah.add(Pi);
        }
        //throw new UnautorizedAccess("test");
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        obsah = new JPanel();

        //======== this ========
        setLayout(new BorderLayout());

        //======== obsah ========
        {
            obsah.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        }
        add(obsah, BorderLayout.CENTER);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel obsah;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}

package sk.tondrus.multiThreading;

import org.apache.log4j.Logger;
import org.jdesktop.application.Application;
import sk.tondrus.AppFabrika;
import sk.tondrus.Exceptions.UnautorizedAccess;
import sk.tondrus.databaza.ObjednavkaFactory;
import sk.tondrus.databaza.databazaEntities.*;

import java.rmi.RemoteException;
import java.sql.SQLException;

/**
 * trieda je odvodena od triedy zamestnanec, sluzi na vyrobu bicyklov podla objednavok
 */
public class Robotnik extends Zamestnanec {
    private static Logger logger = Logger.getLogger(Skladnik.class);
    private long delay = 1 * 20 * 1000; //2*60*1000

    public Robotnik(String meno, Udalost udalost, int cinnost) {
        super(meno, udalost, cinnost);
    }

    /**
     * fukncia nacita objednavku nasledne podla objednavky ziska produkt ktory obsahuje diely a vytvori bicykel podla typu ktory je  databaze
     */
    @Override
    protected void pracuj() {
        logger.debug("Robotnik zacina robit");
        Product bicykel;
        AppFabrika appFabrika = (AppFabrika) Application.getInstance();
        try {
            do {
                Objednavka objednavka = appFabrika.PriradObjednavku();

                if (objednavka == null)
                    return;

                Product product = appFabrika.getProductDetail(-125, objednavka.getIDBicykla()); //ziska produkt v tomto pripade bicykel a podla typu vytvori dany bicykel
                switch (product.getTypBicykla()) {
                    case 0:
                        bicykel = new RoadBike(product);
                        break;
                    case 1:
                        bicykel = new MTBike(product);
                        break;
                    case 2:
                        bicykel = new DownHillBike(product);
                        break;
                }
                long startTime = System.currentTimeMillis();
                while ((System.currentTimeMillis() - startTime) < delay && !koniec) {
                    Thread.sleep(100);
                }
//TODO update database
                logger.debug("updatujem databazu");
                String[] vlastnosti = {Objednavka.STAV};
                Object[] values = {Objednavka.DOKONCENA};
                ObjednavkaFactory objednavkaFactory = new ObjednavkaFactory();
                appFabrika.getDatabase().update(objednavkaFactory, vlastnosti, values, objednavka.getOrderID());
                logger.debug("prekreslujem tabulku");
                appFabrika.NotifyUserDataChanged();
            } while (true && !koniec);
        } catch (RemoteException exception) {
            exception.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (UnautorizedAccess unautorizedAccess) {
            unautorizedAccess.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * zavola rodicovsku metodu
     */
    @Override
    protected void dokonci() {
        super.dokonci();
    }

}

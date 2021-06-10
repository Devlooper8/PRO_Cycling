package sk.tondrus.multiThreading;

import org.apache.log4j.Logger;
import org.jdesktop.application.Application;
import sk.tondrus.AppFabrika;
import sk.tondrus.databaza.ObjednavkaFactory;
import sk.tondrus.databaza.databazaEntities.Objednavka;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public class Skladnik extends Zamestnanec {
    private static Logger logger = Logger.getLogger(Skladnik.class);

    /**
     *
     * @param meno meno skladnika
     * @param udalost udalost pri ktorej zacina pracovat
     * @param cinnost cinnost ktoru zacne vykonavat
     */
    public Skladnik(String meno, Udalost udalost, int cinnost) {
        super(meno, udalost, cinnost);
    }

    /**
     * zavola rodicovsku metodu
     */
    @Override
    protected void dokonci() {
        super.dokonci();
    }

    /**
     * zacina pracovat, priradi robotnikom objednavky, skontroluje ci sa v sklade nachadza dostatok dielov ak nie tak ich naskladni
     */
    @Override
    protected void pracuj() {
        try {
            logger.debug("Skladnik pracuje");
            AppFabrika appFabrika = (AppFabrika) Application.getInstance();
            ObjednavkaFactory objednavkaFactory = new ObjednavkaFactory();
            String[] vlastnosti = {Objednavka.STAV};
            Object[] values = {Objednavka.SPRACOVANA};

            List<Objednavka> objednavky = appFabrika.LoadObjednavkySkladnik();
            if (objednavky.size() == 0) {
                logger.debug("Ziadna objednavka");
                return;
            }

            for (Objednavka objednavka : objednavky) {
                appFabrika.Naskladni();
                appFabrika.getDatabase().update(objednavkaFactory, vlastnosti, values, objednavka.getOrderID());
                appFabrika.SkladObjednavka(objednavka);
            }
            udalost.zobudZamestnancov(Udalost.VYROBIT);
            //TODO fixnut prekreslovanie tabulky
            appFabrika.NotifyUserDataChanged();
        } catch (RemoteException exception) {
            exception.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}

package sk.tondrus.multiThreading;

import java.util.HashMap;
import java.util.Vector;

public class Udalost {
    public static int PRISLA_OBJEDNAVKA = 1, VYROBIT = 2;
    private HashMap<Integer, Vector<Zamestnanec>> mapaUdalosti = new HashMap<>();

    /**
     * vlozi zamestnanca medzi ostatnych a nastavi mu udalost pri ktorej ma zacat pracovat
     * @param udalost udalost udava ktory zamestanec ma robit a pri akej udalosti ma zacat pracovat
     * @param zamestnanec vstupny objekt zamestnanec
     */
    public synchronized void zaregistrujZamestnanca(int udalost, Zamestnanec zamestnanec) {
        Vector<Zamestnanec> zamestnanci = mapaUdalosti.get(udalost);
        if (zamestnanci == null) {
            zamestnanci = new Vector<>();
            mapaUdalosti.put(udalost, zamestnanci);
        }
        zamestnanci.add(zamestnanec);
    }

    /**
     * odstrani zamestnantca zo zoznamu
     * @param udalost udalost udava ktory zamestanec ma robit a pri akej udalosti ma zacat pracovat
     * @param zamestnanec vstupny objekt zamestnanec
     */
    public void odregistrujZamestnanca(int udalost, Zamestnanec zamestnanec) {
        Vector<Zamestnanec> zamestnanci = mapaUdalosti.get(udalost);
        if (zamestnanci == null) return;
        zamestnanci.remove(zamestnanec);
    }

    /**
     * zobudi zamestnancov aby zacali pracovat
     * @param udalost podla udalosti zobudi len tych zamestnancov ktorym bola pred tym priradena
     */
    public void zobudZamestnancov(int udalost) {
        Vector<Zamestnanec> zamestnanci = mapaUdalosti.get(udalost);
        if (zamestnanci == null) return;
        for (Zamestnanec zamestnanec : zamestnanci) {
            zamestnanec.vstavaj();
        }
    }

    /**
     zobudi zamestnancov aby zacali pracovat
     * @param udalost podla udalosti zobudi len tych zamestnancov ktorym bola pred tym priradena
     * @param cas udava ako dlho maju zamestnanci cakat pred tym ako zacnu pracovat
     */
    public void zobudZamestnancov(int udalost, int cas) {
        Thread waitThread = new Thread(() -> {
            try {
                Thread.sleep(cas * 60 * 1000);
                zobudZamestnancov(udalost);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        waitThread.start();
    }
}
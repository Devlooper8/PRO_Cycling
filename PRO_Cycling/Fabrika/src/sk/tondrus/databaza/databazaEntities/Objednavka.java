package sk.tondrus.databaza.databazaEntities;

import java.io.Serializable;
import java.util.Date;

/**
 * Pouziva sa na vytvorenie ObjednavkaFactory
 * @author Tomáš
 */
public class Objednavka implements Serializable {
    public static final String ORDER_ID = "ORDER_ID";
    public static final String ID_BICYKLA = "ID_BICYKLA";
    public static final String DATE = "DATUM";
    public static final String USER_ID = "USER_ID";
    public static final String CENA = "Cena";
    public static final String POPIS = "POPIS";
    public static final String STAV = "STAV";
    public static final int VYTVORENA = 0, SPRACOVANA = 1, VYRABA_SA = 2, DOKONCENA = 3, EXPEDOVANA = 4;

    private int orderID;
    private int IDBicykla;
    private Date date;
    private int userID;
    private String popis;
    private float cena;
    private int stav;

    /**
     *  defaultny konstruktor
     */
    public Objednavka() {

    }

    /**
     * konstruktor s 2 parametrami
     * @param IdBicykla nacita sa z databazy id bicykla
     * @param userId nacita sa id aktualne prihlaseneho pouzivatela
     */
    public Objednavka(int IdBicykla, int userId) {
        IDBicykla = IdBicykla;
        userID = userId;
    }

    /**
     * OrderID getter
     * @return vrati cislo objednavky
     */
    public int getOrderID() {
        return orderID;
    }

    /**
     * OrderID setter
     * @param orderID cislo objednavky ktore bolo nacitane z databazy
     */
    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    /**
     * IDBicykla getter
     * @return vrati cislo bicykla
     */
    public int getIDBicykla() {
        return IDBicykla;
    }

    /**
     * IDBicykla setter
     * @param IDBicykla cislo bicykla ktore bolo nacitane z databazy
     */
    public void setIDBicykla(int IDBicykla) {
        this.IDBicykla = IDBicykla;
    }

    /**
     * Date getter
     * @return vrati datum vytvorenia objednavky
     */
    public Date getDate() {
        return date;
    }

    /**
     * Date setter
     * @param date datum ktory bol nacitany z databazy
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * UserId getter
     * @return vrati id pouzivatela ktory vytvoril danu objednavku
     */
    public int getUserId() {
        return userID;
    }

    /**
     * UserId setter
     * @param userId cislo uzivatela ktore bolo nacitane z databazy
     */
    public void setUserId(int userId) {
        this.userID = userId;
    }

    /**
     * Popis getter
     * @return vrati popis bicykla
     */
    public String getPopis() {
        return popis;
    }

    /**
     * Popis setter
     * @param popis popis objednavky ktory bol nacitany z databazy
     */
    public void setPopis(String popis) {
        this.popis = popis;
    }

    /**
     * Cena getter
     * @return vrati cenu bicykla
     */
    public float getCena() {
        return cena;
    }

    /**
     * Cena setter
     * @param cena cena objednavky ktora bola nacitana z databazy
     */
    public void setCena(float cena) {
        this.cena = cena;
    }

    /**
     * Stav getter
     * @return vrati stav objednavky
     */
    public int getStav() {
        return stav;
    }

    /**
     * Stav setter
     * @param stav stav objednavky ktory bol nacitany z databazy
     */
    public void setStav(int stav) {
        this.stav = stav;
    }


}

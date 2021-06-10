package sk.tondrus.databaza.databazaEntities;

/**
 * @author Tomáš
 * Do instancie tejto triedy sa mi uklada aktualny stav zasob v sklade
 */
public class Sklad {
    public static final String PART_ID = "ID";
    public static final String NUM_OF = "NUM_OF";
    public static final String NAZOV = "NAZOV";
    public static final String VOLUME = "VOLUME";
    private int partID;
    private String nazov;
    private int pocetKusov;

    /**
     * defaultny konstruktor
     */
    public Sklad() {
    }

    /**
     * PartID getter
     * @return vrati id dielu v sklade
     */
    public int getPartID() {
        return partID;
    }

    /**
     * PartID setter
     * @param partID ID dielu ktory bol nacitany z databazy
     */
    public void setPartID(int partID) {
        this.partID = partID;
    }

    /**
     * PocetKusov getter
     * @return vrati pocetKusov daneho dielu v sklade
     */
    public int getPocetKusov() {
        return pocetKusov;
    }

    /**
     * PocetKusov setter
     * @param pocetKusov pocet kusov daneho dielu ktory bol nacitany z databazy
     */
    public void setPocetKusov(int pocetKusov) {
        this.pocetKusov = pocetKusov;
    }

    /**
     * Nazov getter
     * @return vrati nazov dielu v sklade
     */
    public String getNazov() {
        return nazov;
    }

    /**
     * Nazov setter
     * @param nazov nazov dielu ktory bol nacitany z databazy
     */
    public void setNazov(String nazov) {
        this.nazov = nazov;
    }
}

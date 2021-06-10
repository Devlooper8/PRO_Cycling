package sk.tondrus.databaza.databazaEntities;

import java.io.Serializable;
import java.util.List;

/**
 * @author Tomáš
 * Do objektu tejto triedy sa ukladaju informacie o danom produkte ktore sa nacitaju z databazy
 */

public class Product implements Serializable {
    public static final String ID = "ID";
    public static final String OBRAZOK = "Obrazok";
    public static final String NAHLAD = "Nahlad";
    public static final String POPIS = "Popis";
    public static final String CENA = "Cena";
    public static final String TYPBICYKLA = "TypBicykla";
    protected List<Parts> parts;
    private int id;
    private byte[] obrazok;
    private byte[] nahlad;
    private String popis;
    private float cena;
    private int TypBicykla;

    /**
     * defaultny konstruktor
     */
    public Product() {
    }

    /**
     * Parts getter
     * @return vrati list dielov v produkte
     */
    public List<Parts> getParts() {
        return parts;
    }

    /**
     * Parts setter
     * @param parts list dielov ktory bol nacitany z databazy
     */
    public void setParts(List<Parts> parts) {
        this.parts = parts;
    }

    /**
     * Id getter
     * @return vrati id produktu
     */
    public int getId() {
        return id;
    }

    /**
     * Id setter
     * @param id id produktu ktory bol nacitany z databazy
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Popis getter
     * @return vrati popis produktu
     */
    public String getPopis() {
        return popis;
    }

    /**
     * Popis setter
     * @param popis popis produktu ktory bol nacitany z databazy
     */
    public void setPopis(String popis) {
        this.popis = popis;
    }

    /**
     * Cena getter
     * @return vrati cenu produktu
     */
    public float getCena() {
        return cena;
    }

    /**
     * Cena setter
     * @param cena cena produktu ktora bola nacitana z databazy
     */
    public void setCena(float cena) {
        this.cena = cena;
    }

    /**
     * Obrazok getter
     * @return vrati obrazok produktu reprezentovany ako pole bajtov
     */
    public byte[] getObrazok() {
        return obrazok;
    }

    /**
     * Obrazok setter
     * @param obrazok obrazok produktu reprezentovany ako pole bajtov ktory bol nacitany z databazy
     */
    public void setObrazok(byte[] obrazok) {
        this.obrazok = obrazok;
    }

    /**
     * Nahlad getter
     * @return vrati nahlad produktu reprezentovany ako pole bajtov
     */
    public byte[] getNahlad() {
        return nahlad;
    }

    /**
     * Nahlad setter
     * @param nahlad nahlad produktu reprezentovany ako pole bajtov ktory bol nacitany z databazy
     */
    public void setNahlad(byte[] nahlad) {
        this.nahlad = nahlad;
    }

    /**
     * TypBicykla getter
     * @return vrati typ bicykla v produkte
     */
    public int getTypBicykla() {
        return TypBicykla;
    }

    /**
     * TypBicykla setter
     * @param typBicykla typ bicykla ktory bol nacitany z databazy
     */
    public void setTypBicykla(int typBicykla) {
        TypBicykla = typBicykla;
    }


}


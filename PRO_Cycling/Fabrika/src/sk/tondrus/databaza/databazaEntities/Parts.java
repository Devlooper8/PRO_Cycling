package sk.tondrus.databaza.databazaEntities;

import java.io.Serializable;

/**
 * Instancia tento triedy sa pouziva na reprezentaciu dielov ktore dany bicykel obsahuje
 * @author Tomáš
 */
public class Parts implements Serializable {

    private String partType;
    private String partVariant;

    /**
     * defaultny konstruktor
     */
    public Parts() {

    }

    /**
     * PartType getter
     * @return vrati typ dielu
     */
    public String getPartType() {
        return partType;
    }

    /**
     * PartType setter
     * @param partType typ dielu ktory bol nacitany z databazy
     */
    public void setPartType(String partType) {
        this.partType = partType;
    }

    /**
     * PartVariant getter
     * @return vrati variant dielu
     */
    public String getPartVariant() {
        return partVariant;
    }

    /**
     * PartVariant setter
     * @param partVariant variant dielu ktory bol nacitany z databazy
     */
    public void setPartVariant(String partVariant) {
        this.partVariant = partVariant;
    }

}

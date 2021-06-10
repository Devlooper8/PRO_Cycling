package sk.tondrus.databaza.databazaEntities;

/**
 * Do instancie tejto triedy ulozim diely ktore sa nacitaju z databazy
 * Tato trieda dedi od triedy produkt
 * @author Tomáš
 */
public class RoadBike extends Product {


    /**
     * konstruktor
     * @param product produkt ktory bol nacitany z databazy
     */
    public RoadBike(Product product) {
        this.setParts(product.getParts());
    }


}

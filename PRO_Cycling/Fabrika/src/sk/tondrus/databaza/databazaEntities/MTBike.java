package sk.tondrus.databaza.databazaEntities;

/**
 * Do instancie tejto triedy ulozim diely ktore sa nacitaju z databazy
 * Tato trieda dedi od triedy RoadBike ktora dedi od triedy produkt
 * @author Tomáš
 */
public class MTBike extends RoadBike {
    /**
     * konstruktor
     * @param product produkt ktory sa ziska z databazy
     * produkt sa pouzije v rodicovskom konstruktore
     */
    public MTBike(Product product) {
        super(product);

    }
}

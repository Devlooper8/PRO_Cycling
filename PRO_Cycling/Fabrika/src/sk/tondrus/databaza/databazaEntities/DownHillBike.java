package sk.tondrus.databaza.databazaEntities;

public class DownHillBike extends MTBike{
    /**
     * konstruktor
     *
     * @param product produkt ktory sa ziska z databazy
     *                produkt sa pouzije v rodicovskom konstruktore
     */
    public DownHillBike(Product product) {
        super(product);
    }
}

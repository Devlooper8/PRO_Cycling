package sk.tondrus.databaza;

import sk.tondrus.databaza.databazaEntities.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ProductFactory sluzi na to aby databaza vedela ako spracovat objekt Product
 */
public class ProductFactory implements Factory<Product> {

    /**
     *
     * @param rs vytvoreny resultSet z sql query
     * @param propNames nazvy stlpcov v tabulke
     * @return vracia typ T v zavislosti od pouzitej factory
     * @throws SQLException
     */
    @Override
    public Product load(ResultSet rs, String[] propNames) {
        Product product = new Product();
        if (propNames.length == 0) {
            propNames = new String[]{
                    Product.ID,
                    Product.OBRAZOK,
                    Product.NAHLAD,
                    Product.POPIS,
                    Product.CENA

            };
        }
        try {
            for (String propName : propNames) {
                switch (propName) {
                    case Product.ID:
                        product.setId((rs.getInt(Product.ID)));
                        break;
                    case Product.OBRAZOK:
                        product.setObrazok(rs.getBytes(Product.OBRAZOK));
                        break;
                    case Product.NAHLAD:
                        product.setNahlad(rs.getBytes(Product.NAHLAD));
                        break;
                    case Product.POPIS:
                        product.setPopis(rs.getString(Product.POPIS));
                        break;
                    case Product.CENA:
                        product.setCena(rs.getFloat(Product.CENA));
                        break;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return product;
    }


    /**
     *
     * @param connection vstupne spojenie s databazou
     * @param source vstupny objekt ktory sa vlozi do tabulky
     * @return vracia PreparedStatement
     * @throws SQLException
     */
    @Override
    public PreparedStatement create(Connection connection, Product source) throws SQLException {
        return null;
    }

    /**
     *
     * @param connection vstupne spojenie s databazou
     * @param Id urcuje riadok ktory sa ma vymazat
     * @return vracia PreparedStatement
     * @throws SQLException
     */
    @Override
    public PreparedStatement delete(Connection connection, int Id) throws SQLException {
        return null;
    }
}

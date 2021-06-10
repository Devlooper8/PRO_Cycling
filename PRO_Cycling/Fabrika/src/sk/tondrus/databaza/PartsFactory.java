package sk.tondrus.databaza;

import sk.tondrus.databaza.databazaEntities.Parts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * PartsFactory sluzi na to aby databaza vedela ako spracovat objekt Parts
 */
public class PartsFactory implements Factory {
    public static String TYPDIELU = "TYP";
    public static String VARIANTDIELU = "NAZOV";

    /**
     *
     * @param rs vytvoreny resultSet z sql query
     * @param propNames nazvy stlpcov v tabulke
     * @return vracia typ T v zavislosti od pouzitej factory
     * @throws SQLException
     */
    @Override
    public Parts load(ResultSet rs, String[] propNames) {
        Parts parts = new Parts();

        try {


            parts.setPartType(rs.getString(TYPDIELU));

            parts.setPartVariant(rs.getString(VARIANTDIELU));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parts;
    }

    /**
     *
     * @param connection vstupne spojenie s databazou
     * @param source vstupny objekt ktory sa vlozi do tabulky
     * @return vracia PreparedStatement
     * @throws SQLException
     */
    @Override
    public PreparedStatement create(Connection connection, Object source) throws SQLException {
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

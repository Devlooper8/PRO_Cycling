package sk.tondrus.databaza;

import sk.tondrus.databaza.databazaEntities.Objednavka;
import sk.tondrus.databaza.databazaEntities.Sklad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * SkladFactory sluzi na to aby databaza vedela ako spracovat objekt Sklad
 */
public class SkladFactory implements Factory {
    /**
     *
     * @param rs vytvoreny resultSet z sql query
     * @param propNames nazvy stlpcov v tabulke
     * @return vracia typ T v zavislosti od pouzitej factory
     * @throws SQLException
     */
    @Override
    public Sklad load(ResultSet rs, String[] propNames) throws SQLException {
        Sklad sklad = new Sklad();
        if (propNames.length == 0) {
            propNames = new String[]{
                    Sklad.VOLUME,
                    Sklad.PART_ID,
                    Sklad.NAZOV

            };
        }
        try {
            for (String propName : propNames) {
                switch (propName) {
                    case Sklad.PART_ID:
                        sklad.setPartID(rs.getInt(Sklad.PART_ID));
                        break;
                    case Sklad.VOLUME:
                        sklad.setPocetKusov(rs.getInt(Sklad.VOLUME));
                        break;
                    case Sklad.NAZOV:
                        sklad.setNazov(rs.getString(Sklad.NAZOV));
                        break;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sklad;
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

    /**
     *
     * @param connection vstupne spojenie s databazou
     * @return vracia PreparedStatement
     * @throws SQLException
     */
    @Override
    public PreparedStatement updateSklad(Connection connection) throws SQLException {
        String sql = "UPDATE PARTS_VARIANTS SET VOLUME= MAX_VOLUME WHERE ID IN (SELECT PARTS_VARIANTS.ID FROM PARTS_VARIANTS INNER JOIN Kusovnik ON VARIANT_ID=PARTS_VARIANTS.ID WHERE (VOLUME-Kusovnik.NUM_OF<PARTS_VARIANTS.MIN_VOLUME))";
        PreparedStatement psmt = connection.prepareStatement(sql);

        return psmt;
    }

    /**
     *
     * @param connection vstupne spojenie s databazou
     * @param objednavka objednavka podla ktorej sa odpocitaju diely zo skladu a aktualizuje sa tabulka v databaze
     * @return vracia PreparedStatement
     * @throws SQLException
     */
    @Override
    public PreparedStatement updateSkladObjednavka(Connection connection, Objednavka objednavka) throws SQLException {
        String sql = "UPDATE PARTS_VARIANTS  SET VOLUME =VOLUME -(SELECT NUM_OF FROM Kusovnik INNER JOIN ORDERS ON PRODUKT_ID= ID_BICYKLA INNER JOIN PARTS_VARIANTS pv ON pv.ID= VARIANT_ID WHERE ORDER_ID =" + objednavka.getOrderID() + " AND PARTS_VARIANTS.ID= Kusovnik.VARIANT_ID) WHERE PARTS_VARIANTS.ID IN( SELECT VARIANT_ID FROM ORDERS INNER JOIN Kusovnik ON PRODUKT_ID= ID_BICYKLA INNER JOIN PARTS_VARIANTS ON PARTS_VARIANTS.ID= VARIANT_ID WHERE ORDER_ID =" + objednavka.getOrderID() + ")";
        PreparedStatement psmt = connection.prepareStatement(sql);

        return psmt;
    }

    ;
}

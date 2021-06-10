package sk.tondrus.databaza;

import sk.tondrus.databaza.databazaEntities.Objednavka;

import java.sql.*;

/**
 * ObjednavkaFactory sluzi na to aby databaza vedela ako spracovat objekt Objednavka
 */
public class ObjednavkaFactory implements Factory<Objednavka> {
    /**
     *
     * @param rs vytvoreny resultSet z sql query
     * @param propNames nazvy stlpcov v tabulke
     * @return vracia typ T v zavislosti od pouzitej factory
     * @throws SQLException
     */
    @Override
    public Objednavka load(ResultSet rs, String[] propNames) throws SQLException {
        Objednavka objednavka = new Objednavka();

        try {
            for (String propName : propNames) {
                switch (propName) {
                    case Objednavka.ORDER_ID:
                        objednavka.setOrderID(rs.getInt(Objednavka.ORDER_ID));
                        break;
                    case Objednavka.ID_BICYKLA:
                        objednavka.setIDBicykla(rs.getInt(Objednavka.ID_BICYKLA));
                        break;
                    case Objednavka.DATE:
                        objednavka.setDate(rs.getDate(Objednavka.DATE));
                        break;
                    case Objednavka.POPIS:
                        objednavka.setPopis(rs.getString(Objednavka.POPIS));
                        break;
                    case Objednavka.CENA:
                        objednavka.setCena(rs.getFloat(Objednavka.CENA));
                        break;
                    case Objednavka.USER_ID:
                        objednavka.setUserId(rs.getInt(Objednavka.USER_ID));
                        break;
                    case Objednavka.STAV:
                        objednavka.setStav(rs.getInt(Objednavka.STAV));
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objednavka;
    }

    /**
     *
     * @param connection vstupne spojenie s databazou
     * @param objednavka vstupny objekt ktory sa vlozi do tabulky
     * @return vracia PreparedStatement
     * @throws SQLException
     */
    @Override
    public PreparedStatement create(Connection connection, Objednavka objednavka) throws SQLException {
        String sql = "INSERT INTO ORDERS(ID_BICYKLA,DATUM,USER_ID) VALUES(?,?,?)";
        PreparedStatement psmt = connection.prepareStatement(sql, new String[]{Objednavka.ORDER_ID});
        psmt.setInt(1, objednavka.getIDBicykla());
        psmt.setDate(2, new Date(System.currentTimeMillis()));
        psmt.setInt(3, objednavka.getUserId());

        return psmt;
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
        String sql = "DELETE FROM ORDERS WHERE ORDER_ID =" + Id;
        PreparedStatement psmt = connection.prepareStatement(sql, new String[]{Objednavka.ORDER_ID});
        return psmt;
    }

    /**
     *
     * @param connection vstupne spojenie s databazou
     * @param vlastnosti stlpce v tabulke ktore sa maju aktualizovat
     * @param values hodnoty ktore sa do tychto stplcov maju ulozit
     * @param id urcuje riadok ktory sa ma aktualizovat
     * @return deaultne vracia null inak vracia PreparedStatement
     * @throws SQLException
     */
    public PreparedStatement update(Connection connection, String[] vlastnosti, Object[] values, int id) throws SQLException {
        String sql = " UPDATE ORDERS SET ";
        for (int i = 0; i < vlastnosti.length; i++) {
            sql += vlastnosti[i] + " = ?";
            if (i < vlastnosti.length - 1) sql += ",";
        }

        sql += " WHERE ORDER_ID= " + id;
        PreparedStatement psmt = connection.prepareStatement(sql);
        for (int i = 0; i < values.length; i++) {
            switch (vlastnosti[i]) {
                case "STAV":
                    psmt.setInt(i + 1, (Integer) values[i]);
                    break;
            }
        }
        return psmt;
    }
}

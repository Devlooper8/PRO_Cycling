package sk.tondrus.databaza;

import sk.tondrus.databaza.databazaEntities.Objednavka;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface Factory<T> {
    /**
     *
     * @param rs vytvoreny resultSet z sql query
     * @param propNames nazvy stlpcov v tabulke
     * @return vracia typ T v zavislosti od pouzitej factory
     * @throws SQLException
     */
    T load(ResultSet rs, String[] propNames) throws SQLException;

    /**
     *
     * @param connection vstupne spojenie s databazou
     * @param source vstupny objekt ktory sa vlozi do tabulky
     * @return vracia PreparedStatement
     * @throws SQLException
     */
    PreparedStatement create(Connection connection, T source) throws SQLException;

    /**
     *
     * @param connection vstupne spojenie s databazou
     * @param Id urcuje riadok ktory sa ma vymazat
     * @return vracia PreparedStatement
     * @throws SQLException
     */
    PreparedStatement delete(Connection connection, int Id) throws SQLException;

    /**
     *
     * @param connection vstupne spojenie s databazou
     * @param vlastnosti stlpce v tabulke ktore sa maju aktualizovat
     * @param values hodnoty ktore sa do tychto stplcov maju ulozit
     * @param id urcuje riadok ktory sa ma aktualizovat
     * @return deaultne vracia null inak vracia PreparedStatement
     * @throws SQLException
     */
    default PreparedStatement update(Connection connection, String[] vlastnosti, Object[] values, int id) throws SQLException {
        return null;
    }

    /**
     * funkcia ktora mi zisti ci treba naslkladnit nejake diely a nasledne aktualizuje tabulku
     * @param connection vstupne spojenie s databazou
     * @return vracia PreparedStatement
     * @throws SQLException
     */
    default PreparedStatement updateSklad(Connection connection) throws SQLException {
        return null;
    }

    /**
     *
     * @param mainconn vstupne spojenie s databazou
     * @param objednavka objednavka podla ktorej sa urcuje ktore diely sa maju z tabulky odcitat a nasledne sa aktualizuje tabulka
     * @return vracia PreparedStatement defaultne je null
     * @throws SQLException
     */
    default PreparedStatement updateSkladObjednavka(Connection mainconn, Objednavka objednavka) throws SQLException {
        return null;
    }


}


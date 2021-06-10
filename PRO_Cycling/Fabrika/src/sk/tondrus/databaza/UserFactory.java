package sk.tondrus.databaza;

import sk.tondrus.databaza.databazaEntities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * UserFactory sluzi na to aby databaza vedela ako spracovat objekt User
 */
public class UserFactory implements Factory<User> {

    /**
     *
     * @param rs vytvoreny resultSet z sql query
     * @param propNames nazvy stlpcov v tabulke
     * @return vracia typ T v zavislosti od pouzitej factory
     * @throws SQLException
     */
    @Override
    public User load(ResultSet rs, String[] propNames) {
        User user = new User();
        if (propNames.length == 0) {
            propNames = new String[]{
                    User.ID,
                    User.EMAIL,
                    User.PASSWORD
            };
        }
        try {
            for (String propName : propNames) {
                if (propName.equals(User.ID)) user.setID(rs.getInt(User.ID));
                else if (propName.equals(User.EMAIL)) user.setEmail(rs.getString(User.EMAIL));
                else if (propName.equals(User.PASSWORD)) user.setPassword(rs.getString(User.PASSWORD));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }

    /**
     *
     * @param connection vstupne spojenie s databazou
     * @param user vstupny objekt ktory sa vlozi do tabulky
     * @return vracia PreparedStatement
     * @throws SQLException
     */
    @Override
    public PreparedStatement create(Connection connection, User user) throws SQLException {
        String sql = "INSERT INTO USERS(EMAIL,PASSWORD) VALUES(?,?)";
        PreparedStatement psmt = connection.prepareStatement(sql, new String[]{User.ID});
        psmt.setString(1, user.getEmail());
        psmt.setString(2, user.getPassword());

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
        String sql = "DELETE FROM USERS WHERE Id = ?";
        PreparedStatement psmt = connection.prepareStatement(sql, new String[]{User.ID});
        return psmt;
    }

}

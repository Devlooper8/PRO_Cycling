package sk.tondrus.databaza;

import sk.tondrus.databaza.databazaEntities.Objednavka;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tomáš
 * Trieda ktora mi zabezpecuje komunikaciu s databazou SQLITE
 */

public class Database {
    private String path;
    private Connection Mainconn = null;

    /**
     * defaultny konstruktor
     */
    public Database() {

    }

    /**
     *  konstruktor s jednym parametrom
     * @param path cesta na ktorej sa nachadza moja databaza
     */
    public Database(String path) {
        this.path = path;
        connect();
    }

    /**
     * Funkcia mi zabezpeci vytvorenie spojenia s databazou,
     * ak bolo vytvorene vrati Connection ak nebolo najprv ho vytvori a potom ho vrati
     * @return Connection
     */
    public Connection connect() {

        if (Mainconn != null)
            return Mainconn;
        try {
            Mainconn = DriverManager.getConnection(path);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Mainconn;
    }

    /**
     * funkcia mi aktualizuje tabulku v databaze
     * @param factory vstupny objekt factory na ktorom sa aplikuje polymorfizmus a aktualizuje sa tabulka
     * @param vlastnosti specifikujem ktore stlpce v tabulke sa maju aktualizovat
     * @param values specifikujem akou hodnotou nahradim povodnu hodnotu v tabulke
     * @param id podla id sa urci riadok ktory treba aktualizovat
     */
    public void update(Factory factory, String[] vlastnosti, Object[] values, int id) {
        PreparedStatement pstmt = null;
        try {
            pstmt = factory.update(Mainconn, vlastnosti, values, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (Exception error) {

            }
        }
    }

    /**
     * funkcia ktora mi aktualizuje sklad / naskladni potrebne suciastky
     * @param factory vstupna factory v tomto pripade bude skladFactory
     */
    public void updateSklad(Factory factory) {
        PreparedStatement pstmt = null;
        try {
            pstmt = factory.updateSklad(Mainconn);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (Exception error) {

            }
        }
    }

    /**
     * Tato fukncia mi odpocita zo skladu potrebne suciastky na vytvorenie bicykla ktory bol v objednavke
     * @param factory vstupna factory bude skladFactory
     * @param objednavka v objednavke su udane ake casti bicykla sa maju pouzit
     */
    public void updateSkladObjednavka(Factory factory, Objednavka objednavka) {
        PreparedStatement pstmt = null;
        try {
            pstmt = factory.updateSkladObjednavka(Mainconn, objednavka);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (Exception error) {

            }
        }
    }

    /**
     * funkcia odstrani zaznam z databazy
     * @param factory vstupna factory urcuje ako sa operacia odstranenia zaznamu spracuje
     * @param Id id urcuje zaznam ktory je potrebne odstranit
     */
    public void delete(Factory factory, int Id) {
        PreparedStatement pstmt = null;
        try {
            pstmt = factory.delete(Mainconn, Id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (Exception error) {

            }
        }
    }

    /**
     * funkcia vlozi do tabulky zaznam
     * @param factory urcuje ako sa dana funkcia bude spravat
     * @param source vstupny objekt ktory sa vlozi do tabulky v databaze
     * @param <T> urcuje typ vstupneho objektu
     * @return vracia prvy stlpec/ id  pod ktorym bol objekt vlozeny
     * @throws SQLException
     */
    public <T> int insert(Factory<T> factory, T source) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            pstmt = factory.create(Mainconn, source);
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs != null && rs.next()) {
                return rs.getInt(1);
            } else {
                throw new SQLException("chýba ID");
            }
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (Exception error) {

            }
        }
    }

    /**
     * ak chcem vytiahnut vsetko z tabulku pouzijem tuto funkciu
     * @param factory urcuje ako sa dana funkcia bude spravat
     * @param sql query ktora sa pouzije na urcenie tabulky v databaze a ktore hodnoty treba vybrat
     * @param <T> je to typ factory ktory sa do funkcie zavola
     * @return navratova hodnota je List typu <T>
     */
    public <T> List<T> select(Factory<T> factory, String sql) {
        return select(factory, sql, new String[]{});
    }

    /**
     * ak chcem vytiahnut len urcite informacie z tabulky pouzijem tuto funkciu
     * @param factory urcuje ako sa dana funkcia bude spravat
     * @param sql query ktora sa pouzije na urcenie tabulky v databaze a ktore hodnoty treba vybrat
     * @param propNames udava hodnoty ktore sa maju vytiahnut z tabulky
     * @param <T> je to typ factory ktory sa do funkcie zavola
     * @return navratova hodnota je List typu <T>
     */
    public <T> List<T> select(Factory<T> factory, String sql, String[] propNames) {
        List<T> result = new ArrayList<T>();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = Mainconn.createStatement();
            rs = stmt.executeQuery(sql);

            // loop through the result set
            while (rs.next()) {
                result.add(factory.load(rs, propNames));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (Exception error) {

            }
        }
        return result;
    }

    /**
     * funkcia zatvori spojenie s databazou
     */
    public void disconnect() {
        if (Mainconn != null) {
            try {
                Mainconn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}

package sk.tondrus.databaza.databazaEntities;

import java.io.Serializable;

/**
 * @author Tomáš
 * Do objektu tejto triedy sa mi ukladaju informacie o pouzivatelovi ktory sa bud prihlasi alebo zaregistruje
 */

public class User implements Serializable {
    public static String ID = "ID";
    public static String EMAIL = "EMAIL";
    public static String PASSWORD = "PASSWORD";
    private int Id;
    private String Email;
    private String Password;

    /**
     * defaultny konstruktor
     */
    public User() {
    }

    /**
     * konstruktor s 2 parametrami
     * @param email email ktory sa nacita z databazy alebo zo vstupu podla toho ci sa pouzivatel ide prihlasit alebo registrovat
     * @param password heslo ktore sa nacita z databazy alebo zo vstupu podla toho ci sa pouzivatel ide prihlasit alebo registrovat
     */
    public User(String email, String password) {
        this.Email = email;
        this.Password = password;
    }

    /**
     * ID getter
     * @return vrati ID pouzivatela v databaze
     */
    public int getID() {
        return Id;
    }

    /**
     * ID setter
     * @param ID ID uzivatela ktore bol nacitane z databazy
     */
    public void setID(int ID) {
        this.Id = ID;
    }

    /**
     * Email getter
     * @return vrati email uzivatela v databaze
     */
    public String getEmail() {
        return Email;
    }

    /**
     * Email setter
     * @param email email uzivatela ktory bol nacitany z databazy
     */
    public void setEmail(String email) {
        Email = email;
    }

    /**
     * Password getter
     * @return vrati heslo uzivatela v databaze
     */
    public String getPassword() {
        return Password;
    }

    /**
     * Password setter
     * @param password heslo uzivatela ktore bolo nacitane z databazy
     */
    public void setPassword(String password) {
        Password = password;
    }
}

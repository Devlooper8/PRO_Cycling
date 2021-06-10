package sk.tondrus.Exceptions;

/**
 * vynimka ktora sa vytvori ked sa pokusi neovereny pouzivatel pouzivat aplikaciu
 */
public class UnautorizedAccess extends Exception {
    public UnautorizedAccess(String ErrorMessage) {
        super(ErrorMessage);
    }
}

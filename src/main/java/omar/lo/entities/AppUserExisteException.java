package omar.lo.entities;

public class AppUserExisteException extends RuntimeException {
    public AppUserExisteException(String username) {
        super(username + " existe déjà!!");
    }
}

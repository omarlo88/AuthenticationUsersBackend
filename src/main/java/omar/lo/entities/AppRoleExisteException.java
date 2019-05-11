package omar.lo.entities;

public class AppRoleExisteException extends RuntimeException {
    public AppRoleExisteException(String roleName) {
        super(roleName + " existe déjà!!");
    }
}

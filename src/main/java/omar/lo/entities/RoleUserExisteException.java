package omar.lo.entities;

public class RoleUserExisteException extends RuntimeException {
    public RoleUserExisteException(String roleName, String username) {
        super("Cet utilisateur: " + username + " a déjà ce rôle: " + roleName);
    }
}

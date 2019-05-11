package omar.lo.entities;

public class RoleUserNotFoundException extends RuntimeException {
    public RoleUserNotFoundException(String roleName, String username) {
        super("Cet utilisateur: " + username + " n'a pas ce rôle: " + roleName);
    }
}

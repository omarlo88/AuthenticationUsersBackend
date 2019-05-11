package omar.lo.entities;

public class AppRoleNotFoundException extends RuntimeException {
    public AppRoleNotFoundException(Long id) {
        super("Rôle introuvable " + id);
    }
}

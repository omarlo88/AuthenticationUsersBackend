package omar.lo.entities;

public class AppUserNotFoundException extends RuntimeException {
    public AppUserNotFoundException(Long id){
        super("User introuvable " + id);
    }
}

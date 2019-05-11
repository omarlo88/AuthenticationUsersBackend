package omar.lo.entities;

public class ConfirmationPasswordException extends RuntimeException {
    public ConfirmationPasswordException() {
        super("Veuillez confirmer votre password!!");
    }
}

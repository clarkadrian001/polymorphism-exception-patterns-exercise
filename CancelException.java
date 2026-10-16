public class CancelException extends RuntimeException {
    public CancelException() {
        super("Cancelled.");
    }
}
public class Fragile extends Box {
    public static final double FRAGILE_MAX_WEIGHT = 30.0;
    private static final double HANDLING_FEE = 100.0;
    private final String handlingNote;
    public Fragile(String trackingNumber, String receiverName, double weight, int length, int width, int height, String handlingNote) throws InvalidParcelException {
        super(trackingNumber, receiverName, weight, length, width, height);
        
        if (handlingNote == null || handlingNote.trim().isEmpty()) {
            throw new InvalidParcelException("Handling note cannot be empty.");
        }
        
        this.handlingNote = handlingNote.trim();
    }

    @Override
    public String getParcelType() {
        return "Fragile";
    }

    @Override
    public double getMaxWeight() {
        return FRAGILE_MAX_WEIGHT;
    }
    
    @Override
    public double calculateBaseFee() {
        return super.calculateBaseFee() + HANDLING_FEE;
    }

    public String getHandlingNote() {
        return handlingNote;
    }
}
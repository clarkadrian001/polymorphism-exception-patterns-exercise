public class Document extends Parcel {
    public static final double DOCUMENT_MAX_WEIGHT = 0.5;
    public static final int MIN_PAGES = 1;
    public static final int MAX_PAGES = 100;
    private static final double FLAT_FEE = 50.0;
    private final int numberOfPages;
    public Document(String trackingNumber, String receiverName, double weight, int numberOfPages) throws InvalidParcelException {
        super(trackingNumber, receiverName, weight);
        
        if (numberOfPages < MIN_PAGES || numberOfPages > MAX_PAGES) {
            throw new InvalidParcelException("Number of pages must be from " + MIN_PAGES + " to " + MAX_PAGES + ".");
        }
        
        this.numberOfPages = numberOfPages;
    }

    @Override
    public String getParcelType() {
        return "Document";
    }

    @Override
    public double getMaxWeight() {
        return DOCUMENT_MAX_WEIGHT;
    }

    @Override
    public double calculateBaseFee() {
        return FLAT_FEE;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }
}
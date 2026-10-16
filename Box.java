public class Box extends Parcel {
    public static final double BOX_MAX_WEIGHT = 50.0;
    private static final double VOLUMETRIC_DIVISOR = 3500.0;
    private static final double RATE_PER_KG = 40.0;
    private final int length;
    private final int width;
    private final int height;
    public Box(String trackingNumber, String receiverName, double weight, int length, int width, int height) throws InvalidParcelException {
        super(trackingNumber, receiverName, weight);

        if (length <= 0 || width <= 0 || height <= 0) {
            throw new InvalidParcelException("Length, width, and height must all be greater than 0.");
        }
        this.length = length;
        this.width = width;
        this.height = height;
    }

    @Override
    public String getParcelType() {
        return "Box";
    }

    @Override
    public double getMaxWeight() {
        return BOX_MAX_WEIGHT;
    }

    public double calculateVolumetricWeight() {
        return ((double) length * width * height) / VOLUMETRIC_DIVISOR;
    }

    @Override
    public double calculateChargeableWeight() {
        return Math.ceil(Math.max(getWeight(), calculateVolumetricWeight()));
    }

    @Override
    public double calculateBaseFee() {
        return RATE_PER_KG * calculateChargeableWeight();
    }

    public int getLength() { return length; }
    public int getWidth()  { return width; }
    public int getHeight() { return height; }
}
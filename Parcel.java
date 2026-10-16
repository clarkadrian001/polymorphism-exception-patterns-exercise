public abstract class Parcel {
    private final String trackingNumber;
    private String receiverName;
    private final double weight;
    private DeliveryService deliveryService;
    private ParcelStatus status;
    public Parcel(String trackingNumber, String receiverName, double weight) throws InvalidParcelException {
        if (!isValidTrackingNumber(trackingNumber)) {
            throw new InvalidParcelException(
                "Invalid tracking number! Use ABC followed by exactly four digits (example: ABC0001).");
        }

        if (receiverName == null || receiverName.trim().isEmpty()) {
            throw new InvalidParcelException("Receiver name cannot be empty.");
        }

        if (Double.isNaN(weight) || weight <= 0) {
            throw new InvalidParcelException("Weight must be greater than 0 kg.");
        }

        if (weight > getMaxWeight()) {
            throw new InvalidParcelException(String.format(
                "%s weight must not exceed %.1f kg.", getParcelType(), getMaxWeight()));
        }

        this.trackingNumber = trackingNumber;
        this.receiverName = receiverName.trim();
        this.weight = weight;
        this.deliveryService = DeliveryService.STANDARD;
        this.status = ParcelStatus.PENDING;
    }

    public static boolean isValidTrackingNumber(String trackingNumber) {
        return trackingNumber != null && trackingNumber.matches("ABC\\d{4}");
    }

    public abstract String getParcelType();
    public abstract double getMaxWeight();
    public abstract double calculateBaseFee();
    public double calculateChargeableWeight() {
        return weight;
    }

    public double getDeliveryFee() {
        return deliveryService.getStrategy().calculateDeliveryFee(calculateBaseFee());
    }

    public void changeDeliveryService(DeliveryService newService) throws DeliveryServiceNotAllowedException {
        DeliveryStrategy strategy = newService.getStrategy();

        if (!strategy.isAvailableFor(calculateChargeableWeight())) {
            throw new DeliveryServiceNotAllowedException(strategy.getRestrictionMessage());
        }

        this.deliveryService = newService;
    }

    public boolean markAsDelivered() {
        if (status == ParcelStatus.DELIVERED) {
            return false;
        }

        status = ParcelStatus.DELIVERED;
        return true;
    }

    public String getTrackingNumber() { return trackingNumber; }
    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) throws InvalidParcelException {
        if (receiverName == null || receiverName.trim().isEmpty()) {
            throw new InvalidParcelException("Receiver name cannot be empty.");
        }
        
        this.receiverName = receiverName.trim();
    }

    public double getWeight() { return weight; }
    public DeliveryService getDeliveryService() { return deliveryService; }
    public ParcelStatus getStatus() { return status; }
}
public interface DeliveryStrategy {
    double calculateDeliveryFee(double baseFee);
    boolean isAvailableFor(double chargeableWeight);
    String getRestrictionMessage();
}
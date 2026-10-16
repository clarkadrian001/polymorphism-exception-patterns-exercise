public class StandardDelivery implements DeliveryStrategy {

    @Override
    public double calculateDeliveryFee(double baseFee) {
        return baseFee;
    }

    @Override
    public boolean isAvailableFor(double chargeableWeight) {
        return true;
    }

    @Override
    public String getRestrictionMessage() {
        return "";
    }
}
public class ExpressDelivery implements DeliveryStrategy {
    @Override
    public double calculateDeliveryFee(double baseFee) {
        return baseFee * 1.5;
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
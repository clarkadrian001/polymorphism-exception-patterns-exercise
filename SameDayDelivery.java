public class SameDayDelivery implements DeliveryStrategy {
    private static final double MAX_CHARGEABLE_WEIGHT = 10.0;
    private static final double SURCHARGE = 100.0;

    @Override
    public double calculateDeliveryFee(double baseFee) {
        return (baseFee * 2) + SURCHARGE;
    }

    @Override
    public boolean isAvailableFor(double chargeableWeight) {
        return chargeableWeight <= MAX_CHARGEABLE_WEIGHT;
    }

    @Override
    public String getRestrictionMessage() {
        return "Same Day delivery is only for parcels up to 10 kg!";
    }
}
public enum DeliveryService {
    STANDARD("Standard", new StandardDelivery()),
    EXPRESS("Express", new ExpressDelivery()),
    SAME_DAY("Same Day", new SameDayDelivery());

    private final String displayName;
    private final DeliveryStrategy strategy;

    DeliveryService(String displayName, DeliveryStrategy strategy) {
        this.displayName = displayName;
        this.strategy = strategy;
    }

    public DeliveryStrategy getStrategy() {
        return strategy;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
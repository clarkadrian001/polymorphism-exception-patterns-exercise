import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParcelManager {
    private static final ParcelManager instance = new ParcelManager();
    private final List<Parcel> parcels = new ArrayList<>();
    private ParcelManager() {
    }

    public static ParcelManager getInstance() {
        return instance;
    }

    public Parcel findParcel(String trackingNumber) {
        for (Parcel parcel : parcels) {
            if (parcel.getTrackingNumber().equals(trackingNumber)) {
                return parcel;
            }
        }
        return null;
    }

    public boolean trackingNumberExists(String trackingNumber) {
        return findParcel(trackingNumber) != null;
    }

    public void addParcel(Parcel parcel) throws DuplicateTrackingNumberException {
        if (trackingNumberExists(parcel.getTrackingNumber())) {
            throw new DuplicateTrackingNumberException("Tracking number " + parcel.getTrackingNumber() + " already exists.");
        }

        parcels.add(parcel);
    }

    public List<Parcel> getAllParcels() {
        return Collections.unmodifiableList(parcels);
    }
}
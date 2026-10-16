import java.util.List;
import java.util.NoSuchElementException;

public class Main {
    private static final ParcelManager manager = ParcelManager.getInstance();
    public static void main(String[] args) {
        boolean running = true;

        displayTitle();

        try {
            while (running) {
                displayMenu();
                int choice = InputHelper.readMainMenuChoice("Enter choice: ", 5);
                try {
                    switch (choice) {
                        case 1:
                            addParcel();
                            break;
                        case 2:
                            viewAllParcels();
                            break;
                        case 3:
                            changeDeliveryService();
                            break;
                        case 4:
                            markAsDelivered();
                            break;
                        case 5:
                            running = false;
                            System.out.println();
                            System.out.println("================ EXIT ================");
                            System.out.println("Exiting program. Goodbye!");
                            break;
                        case 0:
                            break;
                    }
                } catch (CancelException e) {
                    System.out.println("Cancelled. Returning to main menu.");
                    System.out.println();
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("\nNo more input available. Program ended.");
        }
    }

    private static void displayTitle() {
        System.out.println("=======================================");
        System.out.println("       PARCEL DELIVERY SYSTEM          ");
        System.out.println("=======================================");
    }

    private static void displayMenu() {
        System.out.println("Menu");
        System.out.println("1 - Add Parcel");
        System.out.println("2 - View All Parcels");
        System.out.println("3 - Change Delivery Service");
        System.out.println("4 - Mark as Delivered");
        System.out.println("5 - Exit");
        System.out.println();
        System.out.println("Note: Press 0 to go back to the main menu anytime.");
    }

    private static void addParcel() {
        int bannerWidth = Design.printHeaderBanner("ADD PARCEL");
        Design.printParcelTypeList(bannerWidth);

        int type = InputHelper.readSubMenuChoice("Enter Parcel Type: ", 1, 3);

        String trackingNumber = readNewTrackingNumber();
        String receiverName = InputHelper.readNonEmptyText("Enter Receiver Name: ", "Receiver name");

        try {
            Parcel parcel;
            if (type == 1) {
                parcel = createDocument(trackingNumber, receiverName);
            } else if (type == 2) {
                parcel = createBox(trackingNumber, receiverName);
            } else {
                parcel = createFragile(trackingNumber, receiverName);
            }

            System.out.println("Chargeable weight: " + Design.formatWeight(parcel.calculateChargeableWeight()));
            chooseDeliveryServiceFor(parcel);
            manager.addParcel(parcel);

            System.out.println("Parcel added successfully! Delivery fee: " + Design.formatMoney(parcel.getDeliveryFee()));
            Design.printTable(manager.getAllParcels(), "ADDED PARCELS");

        } catch (InvalidParcelException | DuplicateTrackingNumberException e) {
            System.out.println("Parcel was not added: " + e.getMessage());
            System.out.println();
        }
    }

    private static String readNewTrackingNumber() {
        boolean isValidInput = false;

        while (!isValidInput) {
            String trackingNumber = InputHelper.readLineOrCancel("Enter Tracking Number (ABC + 4 digits): ");
            if (!Parcel.isValidTrackingNumber(trackingNumber)) {
                System.out.println("Invalid format! Use ABC followed by exactly four digits (example: ABC0001).");
            } else if (manager.trackingNumberExists(trackingNumber)) {
                System.out.println("Duplicate tracking number! Please use a different one.");
            } else {
                isValidInput = true;
                return trackingNumber;
            }
        }
        return null;
    }

    private static Parcel createDocument(String trackingNumber, String receiverName) throws InvalidParcelException {
        double weight = readWeight(Document.DOCUMENT_MAX_WEIGHT, "Document");
        int pages = InputHelper.readIntInRange("Enter Number of Pages (1-100): ", Document.MIN_PAGES, Document.MAX_PAGES);

        return new Document(trackingNumber, receiverName, weight, pages);
    }

    private static Parcel createBox(String trackingNumber, String receiverName) throws InvalidParcelException {
        double weight = readWeight(Box.BOX_MAX_WEIGHT, "Box");
        int length = InputHelper.readPositiveInt("Enter Length (cm): ", "Length");
        int width = InputHelper.readPositiveInt("Enter Width (cm): ", "Width");
        int height = InputHelper.readPositiveInt("Enter Height (cm): ", "Height");

        return new Box(trackingNumber, receiverName, weight, length, width, height);
    }

    private static Parcel createFragile(String trackingNumber, String receiverName) throws InvalidParcelException {
        double weight = readWeight(Fragile.FRAGILE_MAX_WEIGHT, "Fragile");
        int length = InputHelper.readPositiveInt("Enter Length (cm): ", "Length");
        int width = InputHelper.readPositiveInt("Enter Width (cm): ", "Width");
        int height = InputHelper.readPositiveInt("Enter Height (cm): ", "Height");

        String note = InputHelper.readNonEmptyText("Enter Handling Note: ", "Handling note");

        return new Fragile(trackingNumber, receiverName, weight, length, width, height, note);
    }

    private static double readWeight(double maxWeight, String typeName) {
        boolean isValidInput = false;

        while (!isValidInput) {
            double weight = InputHelper.readDouble("Enter Actual Weight (kg): ");
            if (weight <= 0) {
                System.out.println("Weight must be greater than 0 kg.");
            } else if (weight > maxWeight) {
                System.out.println(typeName + " weight must not exceed " + maxWeight + " kg.");
            } else {
                isValidInput = true;
                return weight;
            }
        }
        return 0.0;
    }

    private static void chooseDeliveryServiceFor(Parcel parcel) {
        boolean isValidInput = false;

        while (!isValidInput) {
            DeliveryService selected = readDeliveryService();
            try {
                parcel.changeDeliveryService(selected);
                isValidInput = true;
            } catch (DeliveryServiceNotAllowedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static DeliveryService readDeliveryService() {
        DeliveryService[] services = DeliveryService.values();

        for (int i = 0; i < services.length; i++) {
            System.out.println((i + 1) + " - " + services[i]);
        }

        int choice = InputHelper.readSubMenuChoice("Enter Delivery Service: ", 1, services.length);

        return services[choice - 1];
    }

    private static void viewAllParcels() {
        List<Parcel> parcels = manager.getAllParcels();

        if (parcels.isEmpty()) {
            Design.printEmptyMessage("There are no parcels to view, please add a parcel first");
            return;
        }

        int tableWidth = Design.computeTableBorderLength(parcels);
        Design.printHeaderBanner("VIEW ALL PARCELS", tableWidth);

        Design.printTable(parcels);
    }

    private static boolean areAllParcelsDelivered() {
        for (Parcel parcel : manager.getAllParcels()) {
            if (parcel.getStatus() == ParcelStatus.PENDING) {
                return false;
            }
        }

        return true;
    }

    private static Parcel readExistingParcel() {
        boolean isValidInput = false;

        while (!isValidInput) {
            String trackingNumber = InputHelper.readLineOrCancel("Enter Tracking Number: ");
            Parcel parcel = manager.findParcel(trackingNumber);

            if (parcel != null) {
                isValidInput = true;
                return parcel;
            }

            System.out.println("Parcel not found!");
        }
        return null;
    }

    private static void changeDeliveryService() {
        if (manager.getAllParcels().isEmpty()) {
            Design.printEmptyMessage("Please add a parcel first");
            return;
        }

        if (areAllParcelsDelivered()) {
            Design.printEmptyMessage("All parcels are already delivered, there is nothing to change anymore.");
            return;
        }

        Design.printHeaderBanner("CHANGE DELIVERY SERVICE");

        Parcel parcel = null;

        boolean isValidInput = false;
        while (!isValidInput) {
            parcel = readExistingParcel();

            if (parcel.getStatus() != ParcelStatus.DELIVERED) {
                isValidInput = true;
            } else {
                System.out.println("Parcel already delivered!");
            }
        }

        Design.printParcelDetails(parcel, "PARCEL DETAILS");

        boolean isValidChoice = false;
        while (!isValidChoice) {
            DeliveryService selected = readDeliveryService();

            try {
                parcel.changeDeliveryService(selected);
                System.out.println("Delivery service changed! New fee: " + Design.formatMoney(parcel.getDeliveryFee()));
                System.out.println();
                return;
            } catch (DeliveryServiceNotAllowedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void markAsDelivered() {
        if (manager.getAllParcels().isEmpty()) {
            Design.printEmptyMessage("Please add a parcel first");
            return;
        }

        if (areAllParcelsDelivered()) {
            Design.printEmptyMessage("All parcels are already delivered, there is nothing to mark yet.");
            return;
        }

        Design.printHeaderBanner("MARK AS DELIVERED");

        boolean isValidInput = false;
        while (!isValidInput) {
            Parcel parcel = readExistingParcel();

            if (parcel.markAsDelivered()) {
                System.out.println("Parcel delivered successfully!");
                isValidInput = true;
            } else {
                System.out.println("Parcel was already delivered.");
            }
        }

        System.out.println();
    }
}
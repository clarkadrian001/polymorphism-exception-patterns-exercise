import java.util.List;

public class Design {
    private static final String[] PARCEL_TYPES = {"Document", "Box", "Fragile"};
    public static String formatMoney(double amount) {
        return "Php " + String.format("%,.2f", amount);
    }

    public static String formatWeight(double weight) {
        if (weight == Math.floor(weight)) {
            return (long) weight + " kg";
        }

        return String.format("%.2f kg", weight);
    }

    public static String[] getTableHeaders() {
        return new String[]{"Tracking No.", "Type", "Receiver", "Chargeable Weight", "Service", "Delivery Fee", "Status"};
    }

    public static List<String[]> buildTableRows(List<Parcel> list) {
        List<String[]> rows = new java.util.ArrayList<>();

        for (Parcel p : list) {
            rows.add(new String[]{
                p.getTrackingNumber(),
                p.getParcelType(),
                p.getReceiverName(),
                formatWeight(p.calculateChargeableWeight()),
                p.getDeliveryService().toString(),
                formatMoney(p.getDeliveryFee()),
                p.getStatus().toString()
            });
        }

        return rows;
    }

    public static int[] computeColumnWidths(String[] headers, List<String[]> rows) {
        int[] columnWidth = new int[headers.length];

        for (int c = 0; c < headers.length; c++) {
            columnWidth[c] = headers[c].length();
        }

        for (String[] row : rows) {
            for (int c = 0; c < headers.length; c++) {
                columnWidth[c] = Math.max(columnWidth[c], row[c].length());
            }
        }

        return columnWidth;
    }

    public static int computeTableBorderLength(List<Parcel> list) {
        String[] headers = getTableHeaders();
        List<String[]> rows = buildTableRows(list);
        int[] columnWidth = computeColumnWidths(headers, rows);

        return buildBorderLine(columnWidth).length();
    }

    public static void printTable(List<Parcel> list) {
        printTable(list, "PARCELS");
    }

    public static void printTable(List<Parcel> list, String title) {
        if (list.isEmpty()) {
            System.out.println("No parcels to display.");
            System.out.println();

            return;
        }

        String[] headers = getTableHeaders();
        List<String[]> rows = buildTableRows(list);
        int[] columnWidth = computeColumnWidths(headers, rows);

        String border = buildBorderLine(columnWidth);
        int innerWidth = border.length() - 2;

        String titleBorder = "-".repeat(innerWidth + 2);
        String titleRow = centerText(title, innerWidth + 2);
        System.out.println(titleBorder);
        System.out.println(titleRow);
        System.out.println(border);
        System.out.println(buildRowLine(headers, columnWidth));
        System.out.println(border);

        for (String[] row : rows) {
            System.out.println(buildRowLine(row, columnWidth));
        }

        System.out.println(border);
        System.out.println();
    }

    public static String buildBorderLine(int[] columnWidth) {
        StringBuilder sb = new StringBuilder();
        sb.append("-");

        for (int width : columnWidth) {
            sb.append("-".repeat(width + 2)).append("-");
        }

        return sb.toString();
    }

    public static String buildRowLine(String[] values, int[] columnWidth) {
        StringBuilder sb = new StringBuilder();
        sb.append(" ");

        for (int c = 0; c < values.length; c++) {
            sb.append(" ").append(String.format("%-" + columnWidth[c] + "s", values[c])).append("  ");
        }

        return sb.toString();
    }

    public static void printParcelTypeList(int width) {
        System.out.println(buildFilledBanner("PARCEL TYPE LIST", width, '-'));

        for (int i = 0; i < PARCEL_TYPES.length; i++) {
            System.out.println((i + 1) + " - " + PARCEL_TYPES[i]);
        }

        System.out.println("-".repeat(width));
    }

    public static String centerText(String text, int width) {
        if (text.length() >= width) {
            return text.substring(0, width);
        }

        int totalPadding = width - text.length();
        int left = totalPadding / 2;
        int right = totalPadding - left;

        return " ".repeat(left) + text + " ".repeat(right);
    }

    public static String buildFilledBanner(String title, int totalWidth, char fillChar) {
        String labelWithSpaces = " " + title + " ";

        if (totalWidth <= labelWithSpaces.length()) {
            return labelWithSpaces;
        }

        int totalPadding = totalWidth - labelWithSpaces.length();
        int left = totalPadding / 2;
        int right = totalPadding - left;
        String fill = String.valueOf(fillChar);

        return fill.repeat(left) + labelWithSpaces + fill.repeat(right);
    }

    public static int printHeaderBanner(String title) {
        System.out.println();
        String banner = "================ " + title + " ================";
        System.out.println(banner);

        return banner.length();
    }

    public static int printHeaderBanner(String title, int targetWidth) {
        System.out.println();
        String banner = buildFilledBanner(title, targetWidth, '=');
        System.out.println(banner);

        return banner.length();
    }

    public static void printParcelDetails(Parcel parcel, String title) {
        String[] labels = {"Tracking No.", "Type", "Receiver", "Actual Weight", "Chargeable Weight", "Service", "Delivery Fee", "Status"};
        String[] values = {
            parcel.getTrackingNumber(),
            parcel.getParcelType(),
            parcel.getReceiverName(),
            formatWeight(parcel.getWeight()),
            formatWeight(parcel.calculateChargeableWeight()),
            parcel.getDeliveryService().toString(),
            formatMoney(parcel.getDeliveryFee()),
            parcel.getStatus().toString()
        };

        int maxLabelLength = 0;

        for (String label : labels) {
            maxLabelLength = Math.max(maxLabelLength, label.length());
        }

        String[] lines = new String[labels.length];
        int maxLineLength = 0;

        for (int i = 0; i < labels.length; i++) {
            lines[i] = String.format("%-" + maxLabelLength + "s : %s", labels[i], values[i]);
            maxLineLength = Math.max(maxLineLength, lines[i].length());
        }

        String defaultBanner = "================ " + title + " ================";
        int sharedWidth = Math.max(defaultBanner.length(), maxLineLength);

        System.out.println(buildFilledBanner(title, sharedWidth, '='));

        String detailsBorder = "-".repeat(sharedWidth);
        System.out.println(detailsBorder);

        for (String line : lines) {
            System.out.println(line);
        }

        System.out.println(detailsBorder);
        System.out.println();
    }

    public static void printEmptyMessage(String message) {
        System.out.println();
        System.out.println(message);
        System.out.println();
    }
}
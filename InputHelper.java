import java.util.Scanner;

public class InputHelper {
    private static final Scanner scanner = new Scanner(System.in);
    private InputHelper() {
    }

    public static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static String readLineOrCancel(String prompt) {
        String text = readLine(prompt);
        if (text.equals("0")) {
            throw new CancelException();
        }
        return text;
    }

    private static boolean isSingleDigit(String text) {
        return text.length() == 1 && Character.isDigit(text.charAt(0));
    }

    public static int readMainMenuChoice(String prompt, int max) {
        while (true) {
            String text = readLine(prompt);

            if (!isSingleDigit(text)) {
                System.out.println("Invalid input! Please enter a single digit with no spaces or extra characters.");
                continue;
            }

            int choice = Character.getNumericValue(text.charAt(0));

            if (choice <= max) {
                return choice;
            }

            System.out.println("Please enter a number between 1 and " + max + ".");
        }
    }

    public static int readSubMenuChoice(String prompt, int min, int max) {
        while (true) {
            String text = readLineOrCancel(prompt);

            if (!isSingleDigit(text)) {
                System.out.println("Invalid input! Please enter a single digit with no spaces or extra characters.");
                continue;
            }

            int choice = Character.getNumericValue(text.charAt(0));

            if (choice >= min && choice <= max) {
                return choice;
            }

            System.out.println("Please enter a number between " + min + " and " + max + ".");
        }
    }

    public static String readNonEmptyText(String prompt, String fieldName) {
        while (true) {
            String text = readLineOrCancel(prompt);
            if (!text.isEmpty()) {
                return text;
            }
            System.out.println(fieldName + " cannot be empty. Please try again.");
        }
    }

    public static int readIntInRange(String prompt, int min, int max) {
        while (true) {
            String text = readLineOrCancel(prompt);
            try {
                int number = Integer.parseInt(text);
                if (number >= min && number <= max) {
                    return number;
                }
                System.out.println("Please enter a number between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a whole number.");
            }
        }
    }

    public static int readPositiveInt(String prompt, String fieldName) {
        while (true) {
            String text = readLineOrCancel(prompt);
            try {
                int number = Integer.parseInt(text);
                if (number > 0) {
                    return number;
                }
                System.out.println(fieldName + " must be greater than 0.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! " + fieldName + " must be a whole number.");
            }
        }
    }

    public static double readDouble(String prompt) {
        while (true) {
            String text = readLineOrCancel(prompt);
            try {
                double number = Double.parseDouble(text);
                if (Double.isNaN(number) || Double.isInfinite(number)) {
                    System.out.println("Invalid input! Please enter a normal decimal number.");
                } else {
                    return number;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number (example: 2.5).");
            }
        }
    }
}
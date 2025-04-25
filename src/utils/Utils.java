package utils;

import java.util.Scanner;

public class Utils {

    public static int validaInt(String missatge, String missatgeError) {
        Scanner scanner = new Scanner(System.in);
        int number = 0;
        boolean isValid = false;

        while (!isValid) {
            System.out.print(missatge);
            if (scanner.hasNextInt()) {
                number = scanner.nextInt();
                isValid = true;
            } else {
                System.out.println(missatgeError);
                scanner.next(); // Clear the invalid input
            }
        }
        return number;
    }

    public static double validaDouble(String missatge, String missatgeError) {
        Scanner scanner = new Scanner(System.in);
        double number = 0;
        boolean isValid = false;

        while (!isValid) {
            System.out.print(missatge);
            if (scanner.hasNextDouble()) {
                number = scanner.nextDouble();
                isValid = true;
            } else {
                System.out.println(missatgeError);
                scanner.next(); // Clear the invalid input
            }
        }
        return number;
    }

    public static String validaString(String missatge, String missatgeError) {
        Scanner scanner = new Scanner(System.in);
        String input = "";
        boolean isValid = false;

        while (!isValid) {
            System.out.print(missatge);
            input = scanner.nextLine();
            if (!input.isEmpty()) {
                isValid = true;
            } else {
                System.out.println(missatgeError);
            }
        }
        return input;
    }

    public static String validaStringMin(String missatge, String missatgeError, int min) {
        Scanner scanner = new Scanner(System.in);
        String input = "";
        boolean isValid = false;

        while (!isValid) {
            System.out.print(missatge);
            input = scanner.nextLine();
            if (input.length() >= min) {
                isValid = true;
            } else {
                System.out.println(missatgeError);
            }
        }
        return input;
    }

    //* valida si la resposta és si o no */
    public static boolean validaSN(String missatge, String missatgeError) {
        Scanner scanner = new Scanner(System.in);
        boolean input = false;
        boolean isValid = false;

        while (!isValid) {
            System.out.print(missatge);
            if (scanner.hasNext()) {
                input = scanner.next().equalsIgnoreCase("s");
                isValid = true;
            } else {
                System.out.println(missatgeError);
                scanner.next(); // Clear the invalid input
            }
        }
        return input;
    }
}

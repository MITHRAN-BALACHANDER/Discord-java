package utils;

import java.util.Scanner;

/**
 * ConsoleUtil class for console input/output helpers
 * Provides formatted output and input utilities
 */
public class ConsoleUtil {
    private static final Scanner scanner = new Scanner(System.in);
    
    // ANSI Color codes for console output
    public static final String RESET = "\033[0m";
    public static final String RED = "\033[31m";
    public static final String GREEN = "\033[32m";
    public static final String YELLOW = "\033[33m";
    public static final String BLUE = "\033[34m";
    public static final String PURPLE = "\033[35m";
    public static final String CYAN = "\033[36m";
    public static final String WHITE = "\033[37m";
    public static final String BOLD = "\033[1m";
    
    /**
     * Print a header with formatting
     */
    public static void printHeader(String text) {
        System.out.println("\n" + BOLD + BLUE + "=".repeat(text.length() + 4) + RESET);
        System.out.println(BOLD + BLUE + "  " + text + "  " + RESET);
        System.out.println(BOLD + BLUE + "=".repeat(text.length() + 4) + RESET);
    }
    
    /**
     * Print success message in green
     */
    public static void printSuccess(String message) {
        System.out.println(GREEN + "✓ " + message + RESET);
    }
    
    /**
     * Print error message in red
     */
    public static void printError(String message) {
        System.out.println(RED + "✗ " + message + RESET);
    }
    
    /**
     * Print warning message in yellow
     */
    public static void printWarning(String message) {
        System.out.println(YELLOW + "⚠ " + message + RESET);
    }
    
    /**
     * Print info message in cyan
     */
    public static void printInfo(String message) {
        System.out.println(CYAN + "ℹ " + message + RESET);
    }
    
    /**
     * Print menu options
     */
    public static void printMenu(String title, String[] options) {
        printHeader(title);
        for (int i = 0; i < options.length; i++) {
            System.out.println(BOLD + (i + 1) + ". " + RESET + options[i]);
        }
        System.out.println(BOLD + "0. " + RESET + "Exit/Back");
        System.out.print("\nEnter your choice: ");
    }
    
    /**
     * Get user input with prompt
     */
    public static String getInput(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine().trim();
    }
    
    /**
     * Get password input (note: console password masking is limited in Java)
     */
    public static String getPassword(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine().trim();
    }
    
    /**
     * Get integer input with validation
     */
    public static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + ": ");
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                printError("Please enter a valid number.");
            }
        }
    }
    
    /**
     * Get integer input with range validation
     */
    public static int getIntInput(String prompt, int min, int max) {
        while (true) {
            int value = getIntInput(prompt);
            if (value >= min && value <= max) {
                return value;
            } else {
                printError("Please enter a number between " + min + " and " + max + ".");
            }
        }
    }
    
    /**
     * Wait for user to press Enter
     */
    public static void waitForEnter() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Clear console (works on most terminals)
     */
    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[2J\033[H");
            }
        } catch (Exception e) {
            // If clearing fails, just print some newlines
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
    
    /**
     * Print a divider line
     */
    public static void printDivider() {
        System.out.println("-".repeat(50));
    }
    
    /**
     * Print application banner
     */
    public static void printBanner() {
        String banner = 
            "╔══════════════════════════════════════════════════╗\n" +
            "║                 DISCORD CLONE                    ║\n" +
            "║              Java Console Edition                ║\n" +
            "║                                                  ║\n" +
            "║    Real-time group communication platform       ║\n" +
            "╚══════════════════════════════════════════════════╝";
        
        System.out.println(PURPLE + banner + RESET);
    }
    
    /**
     * Format text with color
     */
    public static String colorText(String text, String color) {
        return color + text + RESET;
    }
    
    /**
     * Get confirmation from user
     */
    public static boolean getConfirmation(String prompt) {
        while (true) {
            String input = getInput(prompt + " (y/n)").toLowerCase();
            if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            } else {
                printError("Please enter 'y' or 'n'.");
            }
        }
    }
}

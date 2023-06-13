package WELCOME;

import java.util.*;

import FinancialInfo.FinancialInfo;

import java.io.*;

public class WELCOME {
    FinancialInfo fInfo = new FinancialInfo();
    public static final String ANSI_RESET = "\u001B[0m";

    public static final String BG = "\u001B[40m";
    public static final String Green = "\u001B[32m";
    public static final String Cyan = "\u001B[36m";
    public static final String Black = "\u001B[30m";
    public static final String Red = "\u001B[31m";

    public static final String BLUE = "\u001B[34m";
    Scanner scn = new Scanner(System.in);

    public void loginPage(String loginName, Object password) {
        String newline = "";
        long startTime = 0;
        int count = 0;
        ArrayList<String> dataArrayList = new ArrayList<>();

        if (loginName == null || password == null) {
            System.out.println("Invalid login name or password");
            return;
        }

        String username = "Login Name: ";
        int screenWidth = 80;
        int leftPadding = (screenWidth - username.length()) / 5;

        System.out.print(String.format("%" + leftPadding + "s%s", "", username));
        String login = scn.nextLine();

        String passwordText = "Password: ";
        System.out.print(String.format("%" + leftPadding + "s%s", "", passwordText));
        scn.nextLine();

        String passwordString = ((String) password).trim();
        boolean found = false;

        try (BufferedReader bReader = new BufferedReader(new FileReader("newfile.txt"))) {
            String line = bReader.readLine();

            while (line != null) {
                if (line.contains(login) && line.contains(passwordString)) {
                    String name = Black + "\nHi, " + login + ANSI_RESET;
                    System.out.println(
                            "\n" + BG + "||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||"
                                    + ANSI_RESET);
                    int ndashboardWidth = 80;
                    int ndashboardLeftPadding = (ndashboardWidth - name.length());
                    System.out.printf("%n%" + ndashboardLeftPadding + "s%s%n%n", "", name);
                    String welcomeText = Black + "Welcome to your dashboard. Your current financial records are below:"
                            + ANSI_RESET;
                    int dashboardWidth = 80;
                    int dashboardLeftPadding = (dashboardWidth - welcomeText.length()) / 2;

                    System.out.printf("%n%" + dashboardLeftPadding + "s%s%n%n", "", welcomeText);

                    BufferedReader readbudget = new BufferedReader(new FileReader("budget.txt"));
                    String budget = readbudget.readLine();

                    boolean budgetfound = false;
                    while (budget != null) {

                        budgetfound = true;

                        Double actualbudgetvalue = fInfo.actualBudget();
                        System.out.println(
                                BLUE + "Target Budget: $" + ANSI_RESET + Black + budget + ANSI_RESET + BLUE
                                        + "\t\t\tBudget Status: $" + ANSI_RESET
                                        + Black + actualbudgetvalue + ANSI_RESET);

                        budget = readbudget.readLine();
                    }
                    readbudget.close();

                    if (!budgetfound) {
                        System.out.println("No budget found.");
                    }
                    System.out.println(Cyan + "\n\n\t\tTransactions Section\t\t" + ANSI_RESET);
                    try (BufferedReader records = new BufferedReader(new FileReader("financial_records.txt"))) {
                        newline = records.readLine();
                        startTime = System.currentTimeMillis(); // Record the start time
                        while (newline != null) {
                            dataArrayList.add(newline);
                            count++;

                            /*
                             * System.out.println(newline);
                             */
                            newline = records.readLine();
                        }

                        fInfo.avetlExpense();

                        /* fInfo.totalExpenses(); */
                        System.out.println(
                                "\n" + BG + "||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||"
                                        + ANSI_RESET);
                    }

                    found = true;

                }
                line = bReader.readLine();

            }
            for (String entry : dataArrayList) {
                System.out.println(entry);
            }
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            double elapsedTimeSeconds = elapsedTime / 1000.0;
            if (newline != null) {
                System.out.println("\n\t\t\tTime taken for the Retrieval of " + count + " transactions: "
                        + elapsedTimeSeconds + " seconds\n\t\t\t");

            }
            if (newline == null) {
                return;
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
            return;
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return;
        }

        if (!found) {
            System.out.println("Invalid login name or password");
        }

    }

}

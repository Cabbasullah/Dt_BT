package FinancialInfo;

import java.util.*;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import FinancialRecord.FinancialRecord;

import SearchContent.SearchContent;

public class FinancialInfo {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String BG = "\u001B[47m";
    public static final String BCyan = "\u001B[46m";
    public static final String Cyan = "\u001B[36m";
    public static final String Black = "\u001B[30m";
    public static final String Green = "\u001B[32m";
    public static final String BLUE = "\u001B[34m";
    public static final String Blue = "\u001B[32m";
    public static final String Red = "\u001B[31m";

    public double actualBudget() {
        double[] expenseResult = totalExpenses();
        double totalExpensel = expenseResult[0];
        double targetBudget = 0;
        double budgetStatus = 0;

        try (BufferedReader bReader = new BufferedReader(new FileReader("budget.txt"))) {
            String line = bReader.readLine();
            boolean lineempty = true;
            if (line != null) {
                lineempty = false;
                targetBudget = Double.parseDouble(line.trim());
                if (targetBudget > totalExpensel) {
                    budgetStatus = targetBudget - totalExpensel;
                } else if (targetBudget < totalExpensel) {
                    budgetStatus = targetBudget - totalExpensel;
                    Double bstatusdifference = targetBudget - budgetStatus;
                    System.out.println(
                            Red + "\t\t\t\tWarning!! Your Expenses Have Exceeded Your Target Budget by "
                                    + bstatusdifference
                                    + ANSI_RESET
                                    + "\t\t\t");
                    budgetStatus = 0.0;

                } else if (targetBudget == totalExpensel) {
                    System.out.println("\t\t\t\tYour Expenses hit the Target Budget\t\t\t");
                }
                lineempty = false;

            }
            line = bReader.readLine();

            if (lineempty) {
                updateTargetBudget();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return budgetStatus;
    }

    public double[] totalExpenses() {
        double total = 0.0;
        int count = 0;

        try {
            BufferedReader bReader = new BufferedReader(new FileReader("financial_records.txt"));
            String line = bReader.readLine();

            while (line != null) {
                String[] tokens = line.split(" : ");

                if (tokens.length >= 2) {
                    try {
                        String value = tokens[0].trim();
                        double amount = Double.parseDouble(value);
                        total = total + amount;
                        count++;

                    } catch (NumberFormatException e) {

                    }

                }

                line = bReader.readLine();
            }

            bReader.close();

        } catch (IOException e) {
            e.getMessage();
        }

        double average = total / count;
        String formatedavg = String.format("%.2f", average);
        System.out.println(
                String.format(
                        BLUE + "\nTotal Expenses: $" + ANSI_RESET + total + BLUE
                                + "\t\tAverage Expenses: $" + ANSI_RESET + formatedavg + "\n\n"));

        return new double[] { total, average };
    }

    public double updateTargetBudget() {
        Scanner scn = new Scanner(System.in);
        double updatedBudget = 0.0;
        try {
            BufferedReader bReader = new BufferedReader(new FileReader("budget.txt"));
            ArrayList<String> lines = new ArrayList<>();
            String line = bReader.readLine();
            boolean fileIsEmpty = true;

            while (line != null) {
                if (!line.isEmpty()) {
                    fileIsEmpty = false;
                    System.out.print("Current target budget: " + line);
                    System.out.print("\nEnter new target budget: ");
                    String newBudgetStr = scn.nextLine();
                    double newBudget = Double.parseDouble(newBudgetStr);
                    updatedBudget = newBudget;
                    line = newBudgetStr;
                }
                lines.add(line);
                line = bReader.readLine();
            }
            System.out.println("Successfully Updated your target budget.");

            bReader.close();

            if (fileIsEmpty) {
                BufferedWriter tempfile = new BufferedWriter(new FileWriter("budget.txt", true));
                System.out.print("Enter target budget: ");
                String newBudgetStr = scn.nextLine();
                double newBudget = Double.parseDouble(newBudgetStr);
                tempfile.write(String.valueOf(newBudget));
                tempfile.newLine();
                tempfile.close();
                updatedBudget = newBudget;
                System.out.println("Successfully Added Target Budget!");

            } else {
                BufferedWriter writer = new BufferedWriter(new FileWriter("budget.txt"));
                for (String updatedLine : lines) {
                    writer.write(updatedLine);
                    writer.newLine();
                }
                writer.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return updatedBudget;
    }

    public void avetlExpense() {
        File filename = new File("financial_records.txt");

        HashMap<String, Double> totalexpense = new HashMap<>();
        double total = 0.0;
        HashMap<String, Integer> numberexpense = new HashMap<>();
        int count = 0;

        try (BufferedReader expensreader = new BufferedReader(new FileReader(filename))) {

            String line = expensreader.readLine();
            String current = null;
            while (line != null) {
                if (line.trim().isEmpty()) {
                    line = expensreader.readLine();
                    continue;
                }
                if (line.trim().endsWith(":")) {
                    current = line.trim().replaceAll(":", "");
                    total = 0.0;
                    count = 0;
                    totalexpense.put(current, 0.0);
                    numberexpense.put(current, 0);

                } else {
                    try {
                        String[] elements = line.split(":");
                        String value = elements[0];
                        double amount = Double.parseDouble(value);
                        total += amount;
                        count++;
                        totalexpense.put(current, totalexpense.get(current) + amount);
                        numberexpense.put(current, numberexpense.get(current) + 1);
                    } catch (NumberFormatException e) {
                        System.out.print("Parsing Error" + e.getMessage());
                    }
                }
                line = expensreader.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        for (HashMap.Entry<String, Double> mapexpense : totalexpense.entrySet()) {
            total = mapexpense.getValue();
            String categoryname = mapexpense.getKey();
            count = numberexpense.get(categoryname);

            double average = total / count;
            System.out.println();
            System.out.println(BLUE + "=================================================" + ANSI_RESET);
            System.out.println(categoryname);
            System.out.println("Total expense: $" + total);
            System.out.println("Average expenses: $" + average);
            System.out.println(BLUE + "=================================================" + ANSI_RESET);
            System.out.println();

        }
    }

    public ArrayList<FinancialRecord> newlist = new ArrayList<>();
    public HashMap<String, ArrayList<FinancialRecord>> expensesByCategory = new HashMap<>();

    public FinancialInfo() {

        newlist = new ArrayList<>();
        expensesByCategory = new HashMap<>();
    }

    public void addMethod() {
        Scanner scn = new Scanner(System.in);
        System.out.println("\n" + Cyan + "=====================" + ANSI_RESET + "" + Cyan
                + "\n\nTRANSACTIONS ENTRY\n\n" + ANSI_RESET + Cyan + "=====================" + ANSI_RESET
                + "\n");

        System.out.println(Black + "Number of Transactions: " + ANSI_RESET);
        int n = scn.nextInt();
        scn.nextLine();

        long startTime = System.currentTimeMillis(); // Record the start time
        HashSet<String> checkCateg = new HashSet<>();
        for (int i = 0; i < n; i++) {
            System.out.println(Black + "Enter the amount " + (i + 1) + ": " + ANSI_RESET);
            double amount = scn.nextDouble();
            System.out.println(Black + "Enter the date (MM/dd/yyyy): " + ANSI_RESET);
            String dateString = scn.next();
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            Date date;
            try {
                date = format.parse(dateString);
            } catch (Exception e) {
                System.out.println("Invalid date format");
                return;
            }
            scn.nextLine(); // consume newline character
            System.out.println(Black + "Enter the description: " + ANSI_RESET);
            String desc = scn.nextLine();
            System.out.println(Black + "Enter the category: " + ANSI_RESET);
            String catg = scn.nextLine();
            FinancialRecord expense = new FinancialRecord(amount, date, desc, catg);
            newlist.add(expense);
        }

        for (FinancialRecord expense : newlist) {
            ArrayList<FinancialRecord> categoryExpensesList = expensesByCategory.get(expense.getCategory());
            if (categoryExpensesList == null) {
                categoryExpensesList = new ArrayList<>();
                expensesByCategory.put(expense.getCategory(), categoryExpensesList);
            }
            categoryExpensesList.add(expense);
        }

        System.out.println("\nList of contents \n");
        for (Map.Entry<String, ArrayList<FinancialRecord>> entry : expensesByCategory.entrySet()) {
            ArrayList<FinancialRecord> categoryExpensesList = entry.getValue();
            for (FinancialRecord expense : categoryExpensesList) {
                System.out.println(expense);
                System.out.println();
            }
            System.out.println();
        }

        try {
            File newfile = new File("financial_records.txt");
            BufferedWriter bwriter = new BufferedWriter(new FileWriter(newfile, true));

            for (String category : expensesByCategory.keySet()) {
                ArrayList<FinancialRecord> categoryExpenses = expensesByCategory.get(category);

                boolean categ = false;
                try (BufferedReader bReader = new BufferedReader(new FileReader(newfile))) {
                    String line = bReader.readLine();
                    while (line != null) {
                        if (line.contains(category)) {
                            categ = true;
                            break;
                        }
                        line = bReader.readLine();
                    }
                }

                if (!checkCateg.contains(category) && !categ) {
                    bwriter.write(category + ":");
                    bwriter.newLine();
                    checkCateg.add(category);
                    for (FinancialRecord expense : categoryExpenses) {
                        bwriter.write(
                                "  " + expense.getAmount() + " : " + expense.getDate() + " : "
                                        + expense.getDescrip()
                                        + "\n");
                        bwriter.newLine();
                    }
                }
            }

            bwriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        double elapsedTimeSeconds = elapsedTime / 1000.0;

        System.out.println(
                "\n\t\t\tTime taken for the addition operation of " + n + " transactions: " + elapsedTimeSeconds
                        + " seconds\t\t");

    }

    public void Dashboard() {
        int count = 0;
        long startTime = 0;
        ArrayList<String> dataArrayList = new ArrayList<>();

        try (BufferedReader bReader = new BufferedReader(new FileReader("newfile.txt"))) {
            String line = bReader.readLine();

            while (line != null) {

                System.out.println(
                        "\n" + BG + "||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||"
                                + ANSI_RESET);
                /* int ndashboardWidth = 80; */

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

                    Double actualbudgetvalue = actualBudget();
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
                startTime = System.currentTimeMillis(); // Record the start time
                try (BufferedReader records = new BufferedReader(new FileReader("financial_records.txt"))) {
                    String line1 = records.readLine();

                    while (line1 != null) {

                        dataArrayList.add(line1);
                        count++;

                        line1 = records.readLine();
                    }

                    avetlExpense();

                    /* fInfo.totalExpenses(); */
                    System.out.println(
                            "\n" + BG + "||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||"
                                    + ANSI_RESET);
                }

                line = bReader.readLine();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String entry : dataArrayList) {
            System.out.println(entry);
        }
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        double elapsedTimeSeconds = elapsedTime / 1000.0;

        System.out.println("\n\t\t\tTime taken for the Retrieval of " + count + " transactions: "
                + elapsedTimeSeconds + " seconds\n\t\t\t");
    }

    public void addFinancialInfo() {
        Scanner scn = new Scanner(System.in);
        char Options;
        do {

            System.out.println(Blue
                    + "=================\nOPTIONS\na. Exit \nb. Add more transactions \nc. Update More Transactions \nd. Delete Records \ne. Search Content\nf. Update Target Budget\n g. Dashboard h. Retrieve \n=================");
            Options = scn.next().charAt(0);
            scn.nextLine();
            if (Options == 'a') {
                System.exit(0);
                return;
            }

            if (Options == 'b') {
                addMethod();

            }

            else if (Options == 'c') {
                System.out.println("\n" + Cyan + "=====================" + ANSI_RESET + "" + Cyan
                        + "\n\nTRANSACTIONS UPDATE\n\n" + ANSI_RESET + Cyan + "=====================" + ANSI_RESET
                        + "\n");
                System.out.println("Number of Transactions to Update: ");
                int n = scn.nextInt();
                scn.nextLine();

                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                long startTime = System.currentTimeMillis(); // Record the start time

                for (int i = 0; i < n; i++) {
                    System.out.println("Update by category: ");
                    String category = scn.nextLine();
                    ArrayList<FinancialRecord> expenses = expensesByCategory.get(category);
                    if (expenses != null) {
                        System.out.println("Expenses found for the category '" + category + "':");
                        for (FinancialRecord expense : expenses) {
                            System.out.println(expense);
                        }

                        ArrayList<FinancialRecord> updatedExpenses = new ArrayList<>();

                        System.out.print(Black + "Enter new amount: " + ANSI_RESET);
                        double amount = scn.nextDouble();
                        System.out.print(Black + "Enter new date (MM/dd/yyyy): " + ANSI_RESET);
                        String dateString = scn.next();
                        Date date;
                        try {
                            date = format.parse(dateString);
                        } catch (ParseException e) {
                            System.out.println("Invalid date format");
                            return;
                        }
                        scn.nextLine(); // consume newline character
                        System.out.print(Black + "Enter new description: " + ANSI_RESET);
                        String desc = scn.nextLine();
                        System.out.print(Black + "Enter new category: " + ANSI_RESET);
                        String catg = scn.nextLine();
                        FinancialRecord updatedExpense = new FinancialRecord(amount, date, desc, catg);
                        updatedExpenses.add(updatedExpense);

                        expensesByCategory.put(category, updatedExpenses);

                        System.out.println("\nUpdated contents\n");
                        for (Map.Entry<String, ArrayList<FinancialRecord>> entry : expensesByCategory.entrySet()) {

                            ArrayList<FinancialRecord> categoryExpensesList = entry.getValue();

                            for (FinancialRecord expen : categoryExpensesList) {
                                System.out.println(expen.getCategory() + ":");
                                System.out.println(
                                        "  " + expen.getAmount() + " : " + format.format(expen.getDate()) + " : "
                                                + expen.getDescrip());
                            }
                            System.out.println();
                        }
                        boolean categoryFound = false;
                        try {
                            File inputFile = new File("financial_records.txt");
                            File tempFile = new File("temp.txt");
                            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

                            String line;

                            while ((line = reader.readLine()) != null) {
                                if (line.startsWith(category.trim() + ":")) {
                                    categoryFound = true;
                                    for (FinancialRecord exp : updatedExpenses) {
                                        writer.write(exp.getCategory() + ":");
                                        writer.newLine();
                                        writer.write("  " + exp.getAmount() + " : " + format.format(exp.getDate())
                                                + " : " + exp.getDescrip());
                                        writer.newLine();
                                    }
                                    while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {

                                    }
                                } else {
                                    writer.write(line);
                                    writer.newLine();
                                }
                            }

                            reader.close();
                            writer.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        File inputFile = new File("financial_records.txt");
                        File tempFile = new File("temp.txt");
                        if (!categoryFound) {
                            System.out.println("Category not found in the file.");
                            tempFile.delete();
                            return;
                        }
                        if (categoryFound) {
                            if (inputFile.delete()) {
                                tempFile.renameTo(inputFile);
                            } else {
                                System.out.println("Error deleting the original file");
                            }
                            System.out.println("\nFile updated successfully.");

                        }

                    } else {
                        System.out.println("No expenses found for the category '" + category + "'.");
                    }
                }
                long endTime = System.currentTimeMillis();
                long elapsedTime = endTime - startTime;
                double elapsedTimeSeconds = elapsedTime / 1000.0;

                System.out.println("Time taken for the update operation of " + n + " transactions: "
                        + elapsedTimeSeconds + " seconds");

            }

            else if (Options == 'd') {

                System.out.println("\n" + Cyan + "========================" + ANSI_RESET + "" + Cyan
                        + "\n\nDELETE CATEGORIES\n\n" + ANSI_RESET + Cyan + "========================" + ANSI_RESET
                        + "\n");

                System.out.println(Black + "Enter the number of categories to delete: " + ANSI_RESET);
                int numCategories = scn.nextInt();
                scn.nextLine(); // consume newline character

                long startTime = System.currentTimeMillis(); // Record the start time

                for (int i = 0; i < numCategories; i++) {
                    System.out.println(Black + "Enter the category name to delete: " + ANSI_RESET);
                    String categoryName = scn.nextLine();

                    ArrayList<FinancialRecord> categoryExpensesList = expensesByCategory.get(categoryName);
                    if (categoryExpensesList != null) {
                        expensesByCategory.remove(categoryName);
                        System.out.println("Successfully Deleted " + categoryName + " Transactions");
                    }
                    deleteme(categoryName);

                }

                System.out.println("\nList of updated contents \n");
                for (Map.Entry<String, ArrayList<FinancialRecord>> entry : expensesByCategory.entrySet()) {
                    ArrayList<FinancialRecord> categoryExpensesList = entry.getValue();
                    for (FinancialRecord expense : categoryExpensesList) {
                        System.out.println(expense);
                        System.out.println();
                    }
                    System.out.println(); // Add an empty line between categories
                }
                long endTime = System.currentTimeMillis();
                long elapsedTime = endTime - startTime;
                double elapsedTimeSeconds = elapsedTime / 1000.0;

                System.out.println("Time taken for the deletion operation of " + numCategories + " transcations:"
                        + elapsedTimeSeconds + " seconds");

            }
            if (Options == 'f') {
                updateTargetBudget();

            }

            if (Options == 'e') {
                SearchContent scontent = new SearchContent();
                scontent.searchedRecord("");
            }

            if (Options == 'a') {
                System.exit(0);
            }
            if (Options == 'g') {
                Dashboard();
            }

        } while (Options != 'a');

    }

    public void deleteme(String categoryName) {

        try {
            File originalFile = new File("financial_records.txt");
            File temporaryFile = new File("temporary.txt");

            BufferedReader reader = new BufferedReader(new FileReader(originalFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(temporaryFile));

            String line;
            boolean deleted = false;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(categoryName)) {
                    System.out.println("Successfully Removed");
                    deleted = true;
                    // Skip the associated content line
                    reader.readLine();
                    // Skip the date line
                    reader.readLine();
                    reader.readLine();
                } else {
                    writer.write(line);
                    writer.newLine();
                }
            }

            reader.close();
            writer.close();

            if (!deleted) {
                System.out.println("Category name not found");
                temporaryFile.delete();
            } else {
                originalFile.delete();
                temporaryFile.renameTo(originalFile);
                System.out.println("File successfully Removed");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

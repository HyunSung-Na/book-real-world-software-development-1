package com.iteratrlearning.shu_book.chapter_02;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class BankStatementAnalyzerSimple {

    private static final String RESOURCES = "src/main/resources/";

    public static void main(final String[] args) throws IOException {
            final BankStatementCSVParser bankStatementCSVParser = new BankStatementCSVParser();

            final Path path = Paths.get(RESOURCES + "bank-data-simple.csv");
            final List<String> lines = Files.readAllLines(path);

            final List<BankTransaction> bankTransactions
                    = bankStatementCSVParser.parseLinesFromCSV(lines);

        System.out.println("The total for all transactions is " + calculateTotalAmount(bankTransactions));
        System.out.println("Transactions in January " + selectInMonth(bankTransactions, Month.JANUARY));
    }

    public static List<BankTransaction> selectInMonth(List<BankTransaction> bankTransactions, Month month) {
        final List<BankTransaction> bankTransactionsInMonth = new ArrayList<>();

        for (final BankTransaction bankTransaction : bankTransactions) {
            if (bankTransaction.getDate().getMonth() == month) {
                bankTransactionsInMonth.add(bankTransaction);
            }
        }

        return bankTransactionsInMonth;
    }

    public static double calculateTotalAmount(List<BankTransaction> bankTransactions) {
        double total = 0d;
        for (final BankTransaction bankTransaction : bankTransactions) {
            total += bankTransaction.getAmount();
        }

        return total;
    }
}


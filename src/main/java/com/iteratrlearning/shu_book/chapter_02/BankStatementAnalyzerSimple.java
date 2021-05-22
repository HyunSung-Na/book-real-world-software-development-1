package com.iteratrlearning.shu_book.chapter_02;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Month;
import java.util.List;

public class BankStatementAnalyzerSimple {

    private static final String RESOURCES = "src/main/resources/";

    public static void main(final String[] args) throws IOException {
            final BankStatementCSVParser bankStatementCSVParser = new BankStatementCSVParser();
            final Path path = Paths.get(RESOURCES + "bank-data-simple.csv");
            final List<String> lines = Files.readAllLines(path);

            final List<BankTransaction> bankTransactions
                    = bankStatementCSVParser.parseLinesFromCSV(lines);
            final BankStatementProcessor bankStatementProcessor = new BankStatementProcessor(bankTransactions);

            collectSummary(bankStatementProcessor);
    }

    private static void collectSummary(final BankStatementProcessor bankStatementProcessor) {

        System.out.println("The total for all transactions is "
                + bankStatementProcessor.calculateTotalAmount());
        System.out.println("The total for transactions in January is "
                + bankStatementProcessor.calculateTotalInMonth(Month.JANUARY));
        System.out.println("The total for transactions in February is "
                + bankStatementProcessor.calculateTotalInMonth(Month.FEBRUARY));
        System.out.println("The total salary received is "
                + bankStatementProcessor.calculateTotalForCategory("Salary"));
    }
}


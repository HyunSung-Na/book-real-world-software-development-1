package com.iteratrlearning.shu_book.chapter_02;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class BankStatementAnalyzerSimple {

    private static final String RESOURCES = "src/main/resources/";

    public static void main(final String[] args) throws IOException {
            final Path path = Paths.get(RESOURCES + "bank-data-simple.csv");
            final List<String> lines = Files.readAllLines(path);
            double total = 0d;

            for(final String line: lines) {
                String[] columns = line.split(",");
                double amount = Double.parseDouble(columns[1]);
                total += amount;
            }

            System.out.println("The total for all transactions is " + total);
    }
}


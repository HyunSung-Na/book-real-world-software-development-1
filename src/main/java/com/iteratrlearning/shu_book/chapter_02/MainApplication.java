package com.iteratrlearning.shu_book.chapter_02;

public class MainApplication {

    public static void main(String[] args) throws Exception {

        final BankStatementAnalyzerSimple bankStatementAnalyzer
                = new BankStatementAnalyzerSimple();

        final BankStatementParser bankStatementParser
                = new BankStatementCSVParser();

        bankStatementAnalyzer.analyze("bank-data-simple.csv", bankStatementParser);

    }
}

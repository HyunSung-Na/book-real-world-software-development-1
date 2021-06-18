package com.iteratrlearning.shu_book.chapter_05;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Inspector {

    private final List<ConditionAction> conditionalActionList;

    public Inspector(final ConditionAction... conditionalActions) {
        this.conditionalActionList = Arrays.asList(conditionalActions);
    }

    public List<Report> inspect(final Facts facts) {
        final List<Report> reportList = new ArrayList<>();
        for (ConditionAction conditionAction : conditionalActionList) {
            final boolean conditionResult = conditionAction.evaluate(facts);
            final Report report = new Report(conditionAction, facts, conditionResult);
            reportList.add(report);
        }
        return reportList;
    }
}

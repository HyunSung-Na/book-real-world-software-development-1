package com.iteratrlearning.shu_book.chapter_05;

import java.util.StringJoiner;

public class Report {

    private final ConditionAction conditionAction;
    private final Facts facts;
    private final boolean isPositive;

    public Report(ConditionAction conditionAction, Facts facts, boolean isPositive) {
        this.conditionAction = conditionAction;
        this.facts = facts;
        this.isPositive = isPositive;
    }

    public ConditionAction getConditionAction() {
        return conditionAction;
    }

    public Facts getFacts() {
        return facts;
    }

    public boolean isPositive() {
        return isPositive;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Report.class.getSimpleName() + "[", "]")
                .add("conditionAction=" + conditionAction)
                .add("facts=" + facts)
                .add("isPositive=" + isPositive)
                .toString();
    }
}

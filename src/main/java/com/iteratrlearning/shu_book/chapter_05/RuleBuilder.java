package com.iteratrlearning.shu_book.chapter_05;

public class RuleBuilder {
    private ConditionAction condition;

    private RuleBuilder(ConditionAction condition) {
        this.condition = condition;
    }

    public static RuleBuilder when(ConditionAction condition) {
        return new RuleBuilder(condition);
    }

    public Rule then(Action action) {
        return new DefaultRule(condition, action);
    }
}
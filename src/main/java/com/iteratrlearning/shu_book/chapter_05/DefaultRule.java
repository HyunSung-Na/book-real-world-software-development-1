package com.iteratrlearning.shu_book.chapter_05;

public class DefaultRule implements Rule{

    private final ConditionAction conditionAction;
    private final Action action;

    public DefaultRule(ConditionAction conditionAction, Action action) {
        this.conditionAction = conditionAction;
        this.action = action;
    }

    @Override
    public void perform(Facts facts) {
        if (conditionAction.evaluate(facts)) {
            action.perform(facts);
        }
    }
}

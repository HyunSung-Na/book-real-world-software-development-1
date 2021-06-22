package com.iteratrlearning.shu_book.chapter_05;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class BusinessRuleEngineTest {

    @Test
    public void shouldHaveNoRulesInitially() {
        final Facts facts = new Facts();
        final BusinessRuleEngine businessRuleEngine = new BusinessRuleEngine(facts);

        assertEquals(0, businessRuleEngine.count());
    }

    @Test
    public void shouldTwoActions() {
        final Facts fact = new Facts();
        final BusinessRuleEngine businessRuleEngine = new BusinessRuleEngine(fact);
        final ConditionAction conditionAction = (Facts facts) ->
                "CEO".equals(facts.getFacts("jobTitle"));
        final Action action = (Facts facts) -> {
            var name = facts.getFacts("name");
        };

        final Rule rule = new DefaultRule(conditionAction, action);

        final Rule ruleSendEmailToSalesWhenCEO = RuleBuilder
                .when(facts -> "CEO".equals(facts.getFacts("jobTitle")))
                .then(facts -> {
                   var name = facts.getFacts("name");
                });

        businessRuleEngine.addAction(facts -> {
            final String jobTitle = facts.getFacts("jobTitle");
            if ("CEO".equals(jobTitle)) {
                final String name = facts.getFacts("name");
                System.out.println(name);
            }
        });
        businessRuleEngine.addAction(facts -> {
            var dealStage = Stage.valueOf(facts.getFacts("stage"));
            var amount = Double.parseDouble(facts.getFacts("amount"));
            var forecastedAmount = amount * switch (dealStage) {
                case LEAD -> 0.2;
                case EVALUATING -> 0.5;
                case INTERESTED -> 0.8;
                case CLOSED -> 1;
            };

            facts.addFact("forecastedAmount", String.valueOf(forecastedAmount));
        });

        assertEquals(2, businessRuleEngine.count());
    }

    @Test
    public void shouldExecuteOneAction() {
        // given
        final Rule mockRule = mock(Rule.class);
        final Facts mockFacts = mock(Facts.class);
        final BusinessRuleEngine businessRuleEngine = new BusinessRuleEngine(mockFacts);

        // when
        businessRuleEngine.addAction(mockRule);
        businessRuleEngine.run();

        // then
        verify(mockRule).perform(mockFacts);
    }

    @Test
    public void shouldPerformAnActionWithFacts() {
        final Rule mockRule = mock(Rule.class);
        final Facts mockFacts = mock(Facts.class);

        final BusinessRuleEngine businessRuleEngine = new BusinessRuleEngine(mockFacts);

        businessRuleEngine.addAction(mockRule);
        businessRuleEngine.run();

        verify(mockRule).perform(mockFacts);
    }
}

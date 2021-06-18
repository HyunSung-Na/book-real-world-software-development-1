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

        businessRuleEngine.addAction(facts -> {
            final String jobTitle = facts.getFacts("jobTitle");
            if ("CEO".equals(jobTitle)) {
                final String name = facts.getFacts("name");
                System.out.println(name);
            }
        });
        businessRuleEngine.addAction(facts -> {
            double forecastedAmount = 0.0;
            Stage dealStage = Stage.valueOf(facts.getFacts("stage"));
            double amount = Double.parseDouble(facts.getFacts("amount"));

            if (dealStage == Stage.LEAD) {
                forecastedAmount = amount * 0.2;
            } else if (dealStage == Stage.EVALUATING) {
                forecastedAmount = amount * 0.5;
            } else if (dealStage == Stage.INTERESTED) {
                forecastedAmount = amount * 0.8;
            } else if (dealStage == Stage.CLOSED) {
                forecastedAmount = amount;
            }
            facts.addFact("forecastedAmount", String.valueOf(forecastedAmount));
        });

        assertEquals(2, businessRuleEngine.count());
    }

    @Test
    public void shouldExecuteOneAction() {
        // given
        final Action mockAction = mock(Action.class);
        final Facts mockFacts = mock(Facts.class);
        final BusinessRuleEngine businessRuleEngine = new BusinessRuleEngine(mockFacts);

        // when
        businessRuleEngine.addAction(mockAction);
        businessRuleEngine.run();

        // then
        verify(mockAction).perform(mockFacts);
    }

    @Test
    public void shouldPerformAnActionWithFacts() {
        final Action mockAction = mock(Action.class);
        final Facts mockFacts = mock(Facts.class);

        final BusinessRuleEngine businessRuleEngine = new BusinessRuleEngine(mockFacts);

        businessRuleEngine.addAction(mockAction);
        businessRuleEngine.run();

        verify(mockAction).perform(mockFacts);
    }
}

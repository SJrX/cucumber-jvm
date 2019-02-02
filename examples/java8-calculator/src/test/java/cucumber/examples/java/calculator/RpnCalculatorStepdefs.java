package cucumber.examples.java.calculator;

import cucumber.api.PickleStepTestStep;
import cucumber.api.Scenario;
import cucumber.api.java.BeforeStep;
import io.cucumber.datatable.DataTable;
import cucumber.api.java8.En;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class RpnCalculatorStepdefs implements En {
    private RpnCalculator calc;

    private int stepIndex = 0;
    private String lastKeyword = null;

    public RpnCalculatorStepdefs() {
        Given("a calculator I just turned on", () -> {
            calc = new RpnCalculator();
        });

        When("I add {int} and {int}", (Integer arg1, Integer arg2) -> {
            calc.push(arg1);
            calc.push(arg2);
            calc.push("+");
        });


        When("I press (.+)", (String what) -> calc.push(what));

        Then("the result is {double}", (Integer expected) -> assertEquals(expected, calc.value()));

        Then("the result is {int}", (Integer expected) -> assertEquals(expected.doubleValue(), calc.value()));


        Before(new String[]{"not @foo"}, (Scenario scenario) -> {
            scenario.write("Runs before scenarios *not* tagged with @foo");
        });

        After((Scenario scenario) -> {
            // result.write("HELLLLOO");
        });


        Given("the previous entries:", (DataTable dataTable) -> {
            List<Entry> entries = dataTable.asList(Entry.class);
            for (Entry entry : entries) {
                calc.push(entry.first);
                calc.push(entry.second);
                calc.push(entry.operation);
            }
        });
        
        BeforeStep(scenario -> {
                PickleStepTestStep nextStep = scenario.getTestCase().getScenarioTestSteps().get(stepIndex++);
        
                String keyword = nextStep.getStepDefinitionMatch().getStepDefinition().getKeyword();
        
                if(lastKeyword != null)
                {
                    switch(lastKeyword){
                        case "Given":
                            break;
                        case "When":
                            if (!keyword.equals("Given")) {
                                break;
                            }
                        case "Then":
                            if(keyword.equals("Then")) {
                                break;
                            }
                            throw new IllegalStateException("You must use the Given / When / Then in order when specifying tests, you cannot use a "
                                                            + keyword + " after you have already used " + lastKeyword + " see :" +
                                                            nextStep.getStepLocation());
                    }
                }
        
                lastKeyword = keyword;
            }
        );

    }

    static final class Entry {
        private final Integer first;
        private final Integer second;
        private final String operation;

        Entry(Integer first, Integer second, String operation) {
            this.first = first;
            this.second = second;
            this.operation = operation;
        }
    }
}

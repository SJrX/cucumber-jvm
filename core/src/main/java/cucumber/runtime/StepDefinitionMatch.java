package cucumber.runtime;

import cucumber.api.Scenario;

public interface StepDefinitionMatch extends cucumber.api.runtime.StepDefinitionMatch {
    void runStep(Scenario scenario) throws Throwable;

    void dryRunStep(Scenario scenario) throws Throwable;

}

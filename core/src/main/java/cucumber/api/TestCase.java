package cucumber.api;

import gherkin.pickles.PickleTag;

import java.util.List;

public interface TestCase {
    int getLine();

    String getName();

    String getScenarioDesignation();

    List<PickleTag> getTags();

    List<TestStep> getTestSteps();

    List<PickleStepTestStep> getScenarioTestSteps();

    String getUri();
}

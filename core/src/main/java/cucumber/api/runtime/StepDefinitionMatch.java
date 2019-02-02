package cucumber.api.runtime;

/**
 * Represents a match of a step definition in a feature file to a location in source code.
 */
public interface StepDefinitionMatch {

    String getCodeLocation();
    
    /**
     * @return the step definition if defined, <code>null</code> if there is no step associated with match
     */
    StepDefinition getStepDefinition();
}

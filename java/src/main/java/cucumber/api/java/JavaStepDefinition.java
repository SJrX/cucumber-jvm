package cucumber.api.java;

import cucumber.api.runtime.StepDefinition;

import java.lang.reflect.Method;

/**
 * Step Definition subtype for when the Step is defined as a Java Method.
 *
 * We deliberately don't expose the reflection method on the StepDefinition interface in case future implementations do not use methods.
 */
public interface JavaStepDefinition extends StepDefinition {
    
    /**
     * @return the method that will be executed for this step definition.
     */
    Method getStepMethod();

}

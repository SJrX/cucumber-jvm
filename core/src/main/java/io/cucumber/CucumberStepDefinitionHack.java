package io.cucumber.hack;

import cucumber.api.Scenario;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CucumberStepDefinitionHack {
  
  private int step = 0;

  
  public void incrementStep() {
    this.step++;
  }
  
  public String getNextStepAnnotationName(Scenario scenario)
  {
    Annotation annotation = getStepAnnotation(scenario, false);
  
    if(annotation == null)
    {
      return null;
    }
    
    return annotation.annotationType().getSimpleName();
  }
  
  
  public String getLastStepAnnotationName(Scenario scenario)
  {
    Annotation annotation = getStepAnnotation(scenario, true);
  
    if(annotation == null)
    {
      return null;
    }
    
    return annotation.annotationType().getSimpleName();
  }
  
  @SuppressWarnings("unchecked")
  private Annotation getStepAnnotation(Scenario scenario, boolean nextStep)
  {
    Object testCase = getFieldValue(scenario, "testCase");
    List<Object> testSteps = (List<Object>) getFieldValue(testCase, "testSteps");
    
    //List<Object> stepResults = (List<Object>) getFieldValue(scenario, "stepResults");
  
  
    int index = step - (nextStep?1:0);
    
    if ((index < 0) || (index >= testSteps.size()))
    {
      return null;
    }
    
    Object lastTestStep = testSteps.get(index);
    
    Object stepDefinitionMatch = getFieldValue(lastTestStep, "stepDefinitionMatch");
    
    Object stepDefinition = getFieldValue(stepDefinitionMatch, "stepDefinition");
    
    Method m = (Method) getFieldValue(stepDefinition, "method");
    
    Annotation[] annotations =  m.getAnnotations();
    
    List<Annotation> cucumberAnnotations = Arrays.stream(annotations).filter(annotation -> annotation.annotationType().getCanonicalName().startsWith("cucumber.api.java.en")).collect(
      Collectors.toList());
    
    switch(cucumberAnnotations.size())
    {
      case 1:
        return cucumberAnnotations.get(0);
      case 0:
      default:
        throw new IllegalStateException("Method " + m.getDeclaringClass().getCanonicalName() + "::" + m.getName() + " should only have one method that is in cucumber.api.java.en but it has " + cucumberAnnotations.size() );
    }
  }
  
  private static Object getFieldValue(Object o, String name)
  {
    try {
      Class<?> cls = o.getClass();

      while(cls != Object.class)
      {
        try {
  
          Field f = cls.getDeclaredField(name);
          
          f.setAccessible(true);
          return f.get(o);
        } catch(NoSuchFieldException e)
        {
          cls = cls.getSuperclass();
        }
        
      }
      
      throw new IllegalArgumentException("No fields name " + name + " in " + o.getClass() + " or any super class");
      
    } catch (IllegalAccessException e) {
      throw new IllegalArgumentException(e);
    }
  }
}

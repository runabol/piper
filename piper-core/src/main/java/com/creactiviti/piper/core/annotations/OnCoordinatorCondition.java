package com.creactiviti.piper.core.annotations;

import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * A Condition that evaluates if piper.roles property 
 * contains the value <code>coordinator</code>.
 *
 * @author Arik Cohen
 * @see ConditionalOnCoordinator
 */
public class OnCoordinatorCondition extends SpringBootCondition {

  @Override
  public ConditionOutcome getMatchOutcome(ConditionContext aContext, AnnotatedTypeMetadata aMetadata) {
    String property = aContext.getEnvironment().getProperty("piper.coordinator.enabled");
    boolean result = Boolean.valueOf(property);
    return new ConditionOutcome(result,ConditionMessage.forCondition(ConditionalOnCoordinator.class, "(" + getClass().getName() + ")").resultedIn(result));
  }

}

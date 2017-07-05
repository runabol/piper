package com.creactiviti.piper.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Conditional;

/**
 * Configuration annotation for a conditional element that depends on 
 * the property <code>piper.roles</code> containing the value 
 * <code>coordinator</code>
 * 
 * @author Arik Cohen
 * @since Apr 4, 2017
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Conditional(OnCoordinatorCondition.class)
public @interface ConditionalOnCoordinator {
  
}

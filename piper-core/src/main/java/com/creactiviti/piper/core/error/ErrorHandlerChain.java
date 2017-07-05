
package com.creactiviti.piper.core.error;

import java.lang.reflect.Method;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

/**
 * 
 * @author Arik Cohen
 * @since Apr 10, 2017
 */
public class ErrorHandlerChain implements ErrorHandler<Errorable> {

  private final List<ErrorHandler> handlers;

  public ErrorHandlerChain(List<ErrorHandler> aHandlers) {
    Assert.notNull(aHandlers,"list of handlers must not be null");
    handlers = aHandlers;
  }
  
  @Override
  public void handle(Errorable aErrorable) {
    for(ErrorHandler handler : handlers) {
      Method method = BeanUtils.findDeclaredMethodWithMinimalParameters(handler.getClass(), "handle");
      if(method.getParameters()[0].getType().isAssignableFrom(aErrorable.getClass())) {
        handler.handle(aErrorable);
      }
    }
  }

}

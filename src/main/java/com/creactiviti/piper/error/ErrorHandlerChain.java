/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Apr 2017
 */
package com.creactiviti.piper.error;

import java.util.List;

import org.springframework.util.Assert;

/**
 * 
 * @author Arik Cohen
 * @since Apr 10, 2017
 */
public class ErrorHandlerChain implements ErrorHandler {

  private final List<ErrorHandler> handlers;

  public ErrorHandlerChain(List<ErrorHandler> aHandlers) {
    Assert.notNull(aHandlers,"list of handlers must not be null");
    handlers = aHandlers;
  }
  
  @Override
  public void handle(Errorable aErrorable) {
    for(ErrorHandler handler : handlers) {
      handler.handle(aErrorable);
    }
  }

}

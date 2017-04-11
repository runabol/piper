/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Apr 2017
 */
package com.creactiviti.piper.error;

import java.util.ArrayList;
import java.util.List;

public class ErrorHandlerChain implements ErrorHandler {

  private final List<ErrorHandler> handlers = new ArrayList<>();

  public ErrorHandlerChain(List<ErrorHandler> aHandlers) {
    handlers.addAll(aHandlers);
  }
  
  @Override
  public void handle(Errorable aError) {
    for(ErrorHandler handler : handlers) {
      handler.handle(aError);
    }
  }

}

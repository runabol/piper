/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Apr 2017
 */
package com.creactiviti.piper.error;

/**
 * 
 * @author Arik Cohen
 * @since Apr 10, 2017
 */
public abstract class AbstractErrorHandler implements ErrorHandler {

  @Override
  public void handle(Errorable aErrorable) {
    if(canHandle(aErrorable)) {
      handleInternal(aErrorable);
    }
  }
  
  protected abstract void handleInternal (Errorable aErrorable);
  
  protected abstract boolean canHandle (Errorable aErrorable);
  
}

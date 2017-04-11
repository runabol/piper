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
 * @since Apt 10, 2017
 */
public interface ErrorHandler {

  void handle (Errorable aErrorable);
  
}

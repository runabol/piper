/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Apr 2017
 */
package com.creactiviti.piper.error;

/**
 * An interface which marks an object as being
 * able to provide {@link Error} status about 
 * itself.
 * 
 * @author Arik Cohen
 * @since Apr 10, 2017
 */
public interface Errorable {

  
  /**
   * Returns the error associated with the object.
   */
  Error getError ();

}

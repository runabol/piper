/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.cache;


/**
 * Allows a class that holds (typically temporary, cache, etc.) state
 * to be cleared on demand.
 * 
 * @author Arik Cohen
 * @since Mar 28, 2017
 */
public interface Clearable {

  void clear ();
  
}

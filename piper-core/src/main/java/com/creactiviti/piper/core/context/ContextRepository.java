/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.context;

import java.util.List;


/**
 * <p>Stores context information for a job or task
 * objects.</p>
 * 
 * <p>{@link Context} instances are used to evaluate
 * pipeline tasks before they are executed.</p>
 * 
 * @author Arik Cohen
 * @since Mar 2017
 */
public interface ContextRepository<T extends Context> {

  T push (String aStackId, T aContext);
  
  T peek (String aStackId);
  
  List<T> getStack (String aStackId);
  
}

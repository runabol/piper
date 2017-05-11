/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.context;

import java.util.List;

public interface ContextRepository<T extends Context> {

  void push (String aStackId, T aContext);
  
  //T pop (String aStackId);
  
  T peek (String aStackId);
  
  List<T> getStack (String aStackId);
  
  int stackSize (String aStackId);
  
}

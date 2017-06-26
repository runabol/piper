/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core;

import java.util.List;

public interface Page<T> {
  
  List<T> getItems ();
  
  int getSize ();
  
  int getNumber ();
  
  int getTotalItems ();
  
  int getTotalPages ();
  
}

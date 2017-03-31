/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.context;

import java.util.HashMap;
import java.util.Map;

public class SimpleContextRepository implements ContextRepository<SimpleContext> {

  private Map<String, SimpleContext> contexts = new HashMap<String, SimpleContext> ();
  
  @Override
  public SimpleContext getForJobId (String aJobId) {
    return contexts.get(aJobId);
  }

  @Override
  public SimpleContext save (SimpleContext aContext) {
    contexts.put(aContext.getJobId(), aContext);
    return aContext;
  }

}

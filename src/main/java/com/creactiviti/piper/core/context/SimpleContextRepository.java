package com.creactiviti.piper.core.context;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
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

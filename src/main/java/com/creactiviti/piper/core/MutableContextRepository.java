package com.creactiviti.piper.core;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class MutableContextRepository implements ContextRepository<MutableContext> {

  private Map<String, MutableContext> contexts = new HashMap<String, MutableContext> ();
  
  @Override
  public MutableContext findForJobId (String aJobId) {
    return contexts.get(aJobId);
  }

  @Override
  public MutableContext save (MutableContext aContext) {
    contexts.put(aContext.getJobId(), aContext);
    return aContext;
  }

}

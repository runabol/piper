/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.context;

import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.Assert;

public class InMemoryContextRepository implements ContextRepository<Context> {

  private Map<String, Stack<Context>> contexts =  new ConcurrentHashMap<>();

  @Override
  public synchronized void push(String aJobId, Context aContext) {
    Stack<Context> stack = contexts.get(aJobId);
    if(stack == null) {
      stack = new Stack<Context>();
      contexts.put(aJobId, stack);
    }
    stack.push(aContext);
  }

  @Override
  public synchronized Context pop (String aJobId) {
    Stack<Context> stack = contexts.get(aJobId);
    Assert.isTrue(stack!=null&&!stack.empty(),"No context found for job id: " + aJobId);
    return stack.pop();
  }
  
  @Override
  public Context peek(String aJobId) {
    Stack<Context> stack = contexts.get(aJobId);
    Assert.isTrue(stack!=null&&!stack.empty(),"No context found for job id: " + aJobId);
    return stack.peek();
  }

}

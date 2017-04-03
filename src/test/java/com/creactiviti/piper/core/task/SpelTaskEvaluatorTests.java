/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.task;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

import com.creactiviti.piper.core.MapObject;
import com.creactiviti.piper.core.context.MapContext;
import com.creactiviti.piper.core.job.MutableJobTask;

public class SpelTaskEvaluatorTests {

  @Test
  public void test1 () {
    SpelTaskEvaluator evaluator = new SpelTaskEvaluator();
    JobTask jt = new MutableJobTask(Collections.EMPTY_MAP);
    JobTask evaluated = evaluator.evaluate(jt, new MapContext(Collections.EMPTY_MAP));
    Assert.assertEquals(evaluated.asMap(),jt.asMap());
  }
  
  @Test
  public void test2 () {
    SpelTaskEvaluator evaluator = new SpelTaskEvaluator();
    JobTask jt = new MutableJobTask(Collections.singletonMap("hello", "world"));
    JobTask evaluated = evaluator.evaluate(jt, new MapContext(Collections.EMPTY_MAP));
    Assert.assertEquals(evaluated.asMap(),jt.asMap());
  }
  
  @Test
  public void test3 () {
    SpelTaskEvaluator evaluator = new SpelTaskEvaluator();
    JobTask jt = new MutableJobTask(Collections.singletonMap("hello", "${name}"));
    JobTask evaluated = evaluator.evaluate(jt, new MapContext(Collections.singletonMap("name", "arik")));
    Assert.assertEquals("arik",evaluated.getString("hello"));
  }
  
  @Test
  public void test4 () {
    SpelTaskEvaluator evaluator = new SpelTaskEvaluator();
    JobTask jt = new MutableJobTask(Collections.singletonMap("hello", "${firstName} ${lastName}"));
    MapContext ctx = new MapContext();
    ctx.put("firstName", "Arik");
    ctx.put("lastName", "Cohen");
    JobTask evaluated = evaluator.evaluate(jt, ctx);
    Assert.assertEquals("Arik Cohen",evaluated.getString("hello"));
  }
  
  @Test
  public void test5 () {
    SpelTaskEvaluator evaluator = new SpelTaskEvaluator();
    JobTask jt = new MutableJobTask(Collections.singletonMap("hello", "${T(java.lang.Integer).valueOf(number)}"));
    MapContext ctx = new MapContext();
    ctx.put("number", "5");
    JobTask evaluated = evaluator.evaluate(jt, ctx);
    Assert.assertEquals(Integer.valueOf(5),((Integer)evaluated.get("hello")));
  }
  
  @Test
  public void test6 () {
    SpelTaskEvaluator evaluator = new SpelTaskEvaluator();
    JobTask jt = new MutableJobTask(Collections.singletonMap("list", Arrays.asList("${firstName}","${lastName}")));
    MapContext ctx = new MapContext();
    ctx.put("firstName", "Arik");
    ctx.put("lastName", "Cohen");
    JobTask evaluated = evaluator.evaluate(jt, ctx);
    Assert.assertEquals(Arrays.asList("Arik","Cohen"),evaluated.getList("list", String.class));
  }
  
  @Test
  public void test7 () {
    SpelTaskEvaluator evaluator = new SpelTaskEvaluator();
    JobTask jt = new MutableJobTask(Collections.singletonMap("map", Collections.singletonMap("hello", "${firstName}")));
    MapContext ctx = new MapContext();
    ctx.put("firstName", "Arik");
    JobTask evaluated = evaluator.evaluate(jt, ctx);
    Assert.assertEquals(MapObject.of(Collections.singletonMap("hello", "Arik")),evaluated.getMap("map"));
  }
  
  @Test
  public void test8 () {
    SpelTaskEvaluator evaluator = new SpelTaskEvaluator();
    JobTask jt = new MutableJobTask(Collections.singletonMap("mult","${n1*n2}"));
    MapContext ctx = new MapContext();
    ctx.put("n1", 5);
    ctx.put("n2", 3);
    JobTask evaluated = evaluator.evaluate(jt, ctx);
    Assert.assertEquals(Integer.valueOf(15),evaluated.getInteger("mult"));
  }
  
}

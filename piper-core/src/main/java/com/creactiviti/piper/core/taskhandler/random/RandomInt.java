
package com.creactiviti.piper.core.taskhandler.random;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Component;

import com.creactiviti.piper.core.task.Task;
import com.creactiviti.piper.core.task.TaskHandler;


/**
 * a {@link TaskHandler} implementaion which generates a 
 * random integer.
 * 
 * @author Arik Cohen
 * @since Mar 30, 2017
 */
@Component
public class RandomInt implements TaskHandler<Object> {

  @Override
  public Object handle(Task aTask) throws Exception {
    int startInclusive = aTask.getInteger("startInclusive", 0);
    int endInclusive = aTask.getInteger("endInclusive", 100);
    return RandomUtils.nextInt(startInclusive, endInclusive);
  }

}

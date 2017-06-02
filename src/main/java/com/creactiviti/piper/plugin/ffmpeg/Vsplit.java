/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, June 2017
 */
package com.creactiviti.piper.plugin.ffmpeg;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.creactiviti.piper.core.task.Task;
import com.creactiviti.piper.core.task.TaskHandler;
import com.google.common.collect.ImmutableMap;

@Component
public class Vsplit implements TaskHandler<List<Map<String,Object>>> {

  private static final int ONE_SECOND = 1000;
  private final Vduration vduration = new Vduration();
  private final Framerate framerate = new Framerate();
  
  private static final int MIN_CHUNK_SIZE = 5;
  
  @Override
  public List<Map<String, Object>> handle (Task aTask) throws Exception {
    List<Map<String,Object>> chunks = new ArrayList<> ();
    Double duration = vduration.handle(aTask);
    Double frate = framerate.handle(aTask);
    Assert.notNull(duration,"could not determine duration");
    Assert.notNull(frate,"could not determine Frame Rate");
    double frateCeil = Math.ceil(frate);
    double timeUnit = frateCeil/frate;
    double chunkSize = chunkSizeInSeconds(aTask.getRequiredString("chunkSize"))*timeUnit;
    double start = 0;
    double end = 0;
    while(start<duration) {
      if(duration-start<chunkSize) {
        end=duration-start;
      }
      else {
        end=chunkSize;
      }
      // if the next chunk is going to be 
      // too small then we just append it
      // to this chunk.
      if(duration-(start+end) < MIN_CHUNK_SIZE) {
        end=duration-start;
      }
      chunks.add(ImmutableMap.of("start", start, "end", end));
      start = start+end;
    }
    return chunks;
  }
  
  private long chunkSizeInSeconds (String aChunkSizeExpression) {
    long chunkSizeInSeconds = Duration.parse("PT"+aChunkSizeExpression).toMillis()/ONE_SECOND;
    return chunkSizeInSeconds>MIN_CHUNK_SIZE?chunkSizeInSeconds:MIN_CHUNK_SIZE;
  }

}


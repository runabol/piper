/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, June 2017
 */
package com.creactiviti.piper.plugin.ffmpeg;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.creactiviti.piper.core.task.Task;
import com.creactiviti.piper.core.task.TaskHandler;

@Component
public class Vduration implements TaskHandler<Double> {

  private Ffprobe ffprobe = new Ffprobe();
  
  @Override
  public Double handle (Task aTask) throws Exception {
    Map<String, Object> ffprobeResult = ffprobe.handle(aTask);
    List<Map<String,Object>> videos = (List<Map<String, Object>>) ffprobeResult.get("video");
    if(videos!=null && videos.size() > 0) {
      Map<String, Object> video = videos.get(0);
      Object duration = video.get("duration");
      if(duration instanceof String) {
        return Double.valueOf((String)duration);
      }
      return (Double) duration;
    }
    return null;
  }

}


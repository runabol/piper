/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, June 2017
 */
package com.creactiviti.piper.plugin.ffmpeg;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.creactiviti.piper.core.task.Task;
import com.creactiviti.piper.core.task.TaskHandler;

/**
 * 
 * @author Arik Cohen
 * @since Jun 2, 2017
 */
@Component
public class Framerate implements TaskHandler<Double> {

  private final Mediainfo mediainfo = new Mediainfo();
  
  @Override
  public Double handle (Task aTask) throws Exception {
    Map<String, Object> mediainfoResult = mediainfo.handle(aTask);
    String frameRateStr = (String) mediainfoResult.get("video_frame_rate");
    Assert.notNull(frameRateStr, "can not determine framerate");
    return Double.valueOf(frameRateStr.replaceAll("[^0-9\\.]", ""));
  }

}


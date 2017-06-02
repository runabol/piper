/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, June 2017
 */
package com.creactiviti.piper.plugin.ffmpeg;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.creactiviti.piper.core.task.Task;
import com.creactiviti.piper.core.task.TaskHandler;

@Component
public class Dar implements TaskHandler<String> {

  private final Mediainfo mediainfo = new Mediainfo();
  
  @Override
  public String handle (Task aTask) throws Exception {
    Map<String, Object> mediainfoResult = mediainfo.handle(aTask);
    return (String) mediainfoResult.get("video_display_aspect_ratio");
  }

}


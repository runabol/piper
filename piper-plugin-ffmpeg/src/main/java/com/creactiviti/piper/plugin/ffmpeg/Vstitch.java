/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, June 2017
 */
package com.creactiviti.piper.plugin.ffmpeg;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import com.creactiviti.piper.core.job.SimpleTaskExecution;
import com.creactiviti.piper.core.task.Task;
import com.creactiviti.piper.core.task.TaskHandler;

/**
 * a {@link TaskHandler} implementation which "stitches" together a 
 * collection of <code>chunks</code> into a single file specified by 
 * the <code>output</code> property.
 * 
 * @author Arik Cohen
 * @since Jun 4, 2017
 */
@Component
public class Vstitch implements TaskHandler<Object> {

  private final Ffmpeg ffmpeg = new Ffmpeg();
  
  @Override
  public Object handle (Task aTask) throws Exception {
    List<String> chunks = aTask.getList("chunks",String.class);
    File tempFile = File.createTempFile("_chunks", ".txt");
    try {
      try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFile, true), "UTF-8"))) {
        for(String chunk : chunks) {
          writer.append(String.format("file '%s'", chunk))
                .append("\n");
        }
      }
      SimpleTaskExecution ffmpegTask = SimpleTaskExecution.create();
      List<String> options = Arrays.asList(
        "-y",
        "-f","concat",
        "-safe","0",
        "-i",tempFile.getAbsolutePath(),
        "-c","copy",
        aTask.getRequiredString("output")
      );
      ffmpegTask.set("options", options);
      ffmpeg.handle(ffmpegTask);
    }
    finally {
      FileUtils.deleteQuietly(tempFile);
    }
    return null;
  }

}


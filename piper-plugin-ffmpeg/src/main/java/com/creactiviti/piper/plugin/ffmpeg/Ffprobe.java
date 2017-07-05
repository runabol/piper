
package com.creactiviti.piper.plugin.ffmpeg;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.creactiviti.piper.core.task.Task;
import com.creactiviti.piper.core.task.TaskHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Executes an ffprobe command on a given input. 
 * 
 * @author Arik Cohen
 * @since May 23, 2017
 */
@Component
public class Ffprobe implements TaskHandler<Map<String,Object>> {

  private final Logger log = LoggerFactory.getLogger(getClass());
  private final ObjectMapper json = new ObjectMapper();
  
  @Override
  public Map<String,Object> handle(Task aTask) throws Exception {
    CommandLine cmd = new CommandLine ("ffprobe");
    cmd.addArgument("-v")
       .addArgument("quiet")
       .addArgument("-print_format")
       .addArgument("json")
       .addArgument("-show_error")
       .addArgument("-show_format")
       .addArgument("-show_streams")
       .addArgument(aTask.getRequiredString("input"));
    log.debug("{}",cmd);
    DefaultExecutor exec = new DefaultExecutor();
    File tempFile = File.createTempFile("log", null);
    try (PrintStream stream = new PrintStream(tempFile);) {
      exec.setStreamHandler(new PumpStreamHandler(stream));
      exec.execute(cmd);
      return parse(FileUtils.readFileToString(tempFile));
    }
    catch (ExecuteException e) {
      throw new ExecuteException(e.getMessage(),e.getExitValue(), new RuntimeException(FileUtils.readFileToString(tempFile)));
    }
    finally {
      FileUtils.deleteQuietly(tempFile);
    }
  }
  
  private Map<String,Object> parse (String aJson) throws Exception {
    Map<String,Object> result = json.readValue(aJson,Map.class);
    ArrayList<Object> video = new ArrayList<>();
    ArrayList<Object> audio = new ArrayList<>();
    result.put("video", video);
    result.put("audio", audio);
    List<Map<String,Object>> streams = (List<Map<String, Object>>) result.get("streams");
    for(int i=0; streams!=null&&i<streams.size(); i++) {
     Map<String, Object> stream = streams.get(i);
     if("video".equals(stream.get("codec_type"))) {
       video.add(stream);
     }
     else if("audio".equals(stream.get("codec_type"))) {
       audio.add(stream);
     }
    }
    return result;
  }

}
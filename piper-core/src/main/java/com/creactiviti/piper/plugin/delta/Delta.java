package com.creactiviti.piper.plugin.delta;

import java.io.File;
import java.io.PrintStream;

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
import com.creactiviti.piper.core.uuid.UUIDGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class Delta implements TaskHandler<Object> {

  private static final String BASE_PATH = "/var/www/delta";
  
  private final ObjectMapper json = new ObjectMapper();
  
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  @Override
  public Object handle (Task aTask) throws Exception {
    File inputFile = new File(String.format("%s/tmp/%s",BASE_PATH,UUIDGenerator.generate()));
    File outputFile = File.createTempFile("output", null);
    try (PrintStream stream = new PrintStream(outputFile);) {
      FileUtils.writeStringToFile(inputFile, json.writeValueAsString(aTask));
      CommandLine cmd = new CommandLine ("php");
      cmd.addArgument(String.format("%s/utility_scripts/_autoTask.php", BASE_PATH))
         .addArgument(String.format("input=%s",inputFile.getAbsolutePath()));
      logger.debug("{}",cmd);
      DefaultExecutor exec = new DefaultExecutor();
      exec.setStreamHandler(new PumpStreamHandler(stream));
      exec.execute(cmd);
      return FileUtils.readFileToString(outputFile);
    }
    catch (ExecuteException e) {
      throw new ExecuteException(e.getMessage(),e.getExitValue(), new RuntimeException(FileUtils.readFileToString(outputFile)));
    }
    finally {
      FileUtils.deleteQuietly(outputFile);
      FileUtils.deleteQuietly(inputFile);
    }
  }
  
}

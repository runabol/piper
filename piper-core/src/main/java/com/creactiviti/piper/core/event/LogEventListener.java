package com.creactiviti.piper.core.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Arik Cohen
 * @since Jul 5, 2017
 */
public class LogEventListener implements EventListener {
  
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void onApplicationEvent (PiperEvent aEvent) {
    logger.debug("{}",aEvent);
  }

}

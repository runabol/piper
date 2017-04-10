package com.creactiviti.piper.error;

import java.util.Map;

import com.creactiviti.piper.core.MapObject;

public class ErrorObject extends MapObject implements Error {

  public ErrorObject () {
  }
  
  public ErrorObject (Map<String,Object> aSource) {
    super(aSource);
  }
  
  public ErrorObject (String aMessage, String[] aStackTrace) {
    set("message", aMessage);
    set("stackTrace", aStackTrace);
  } 
  
  @Override
  public String getMessage() {
    return getString("message");
  }

  @Override
  public String[] getStackTrace() {
    return getArray("stackTrace", String.class);
  }

}

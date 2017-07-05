
package com.creactiviti.piper.core.context;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.creactiviti.piper.core.MapObject;

public class MapContext extends MapObject implements Context {

  public MapContext () {
    super(new HashMap<>());
  }
  
  public MapContext (String aKey, Object aValue) {
    this(Collections.singletonMap(aKey, aValue));
  }
  
  public MapContext (Map<String, Object> aSource) {
    super(aSource);
  }
  
  public MapContext (Context aSource) {
    super(aSource.asMap());
  }
  
  
}

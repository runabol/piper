
package com.creactiviti.piper.core.uuid;

import java.util.UUID;

public final class UUIDGenerator {

  private UUIDGenerator () {}
  
  public static String generate () {
    return UUID.randomUUID().toString().replaceAll("-", "");
  }
  
}

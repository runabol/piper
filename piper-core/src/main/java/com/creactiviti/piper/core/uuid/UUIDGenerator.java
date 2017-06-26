/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.uuid;

import java.util.UUID;

public final class UUIDGenerator {

  private UUIDGenerator () {}
  
  public static String generate () {
    return UUID.randomUUID().toString().replaceAll("-", "");
  }
  
}

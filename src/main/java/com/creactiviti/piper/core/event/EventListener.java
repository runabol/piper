/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Jun 2017
 */
package com.creactiviti.piper.core.event;


/**
 * 
 * @author Arik Cohen
 * @since Jun 9, 2017
 */
public interface EventListener {

  void onApplicationEvent (PiperEvent aEvent);
  
}

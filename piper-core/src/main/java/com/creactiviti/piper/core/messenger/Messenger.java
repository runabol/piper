/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.messenger;


/**
 * <p>Abstraction for sending messages between the various componentes of the application. 
 * Implementations are responsible for the guranteed delivery of the message.</p>
 * 
 * @author Arik Cohen
 * @since Jun 18, 2016
 */
public interface Messenger {

  /**
   * 
   * @param aRoutingKey
   *          a string representaiton used for routing the message 
   *          to the appropriate destination.
   * @param aMessage
   *          The message to send.
   */
  void send (String aRoutingKey, Object aMessage);
  
}

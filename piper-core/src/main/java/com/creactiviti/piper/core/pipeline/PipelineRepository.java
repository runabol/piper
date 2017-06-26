/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.pipeline;

import java.util.List;


/**
 * 
 * @author Arik Cohen
 * @since Jun 18, 2016
 */
public interface PipelineRepository {
  
  Pipeline findOne (String aId);
  
  List<Pipeline> findAll ();

}

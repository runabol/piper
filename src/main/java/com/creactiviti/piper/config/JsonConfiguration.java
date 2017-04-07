/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.config;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.creactiviti.piper.json.ExceptionSerializer;

@Configuration
public class JsonConfiguration implements Jackson2ObjectMapperBuilderCustomizer {

  @Override
  public void customize(Jackson2ObjectMapperBuilder aJacksonObjectMapperBuilder) {
    aJacksonObjectMapperBuilder.serializerByType(Throwable.class, new ExceptionSerializer());
  }

}

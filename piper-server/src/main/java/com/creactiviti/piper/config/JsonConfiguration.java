
package com.creactiviti.piper.config;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JsonConfiguration implements Jackson2ObjectMapperBuilderCustomizer {

  @Override
  public void customize(Jackson2ObjectMapperBuilder aJacksonObjectMapperBuilder) {
    //aJacksonObjectMapperBuilder.serializerByType(Throwable.class, new ExceptionSerializer());
    //aJacksonObjectMapperBuilder.deserializerByType(JobTaskException.class, new JobTaskExceptionDeserializer());
  }

}

/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Apr 2017
 */
package com.creactiviti.piper.core.context;

import java.util.Date;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.creactiviti.piper.json.JsonHelper;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Arik Cohe
 * @since Apt 7, 2017
 */
@Component
public class JdbcContextRepository implements ContextRepository<Context> {

  private JdbcTemplate jdbc;
  private ObjectMapper objectMapper = new ObjectMapper();
  
  @Override
  public void push(String aJobId, Context aContext) {
    jdbc.update("insert into job_context (id,job_id,data,creation_date) values (?,?,?,?)",aContext.getId(),aJobId,JsonHelper.writeValueAsString(objectMapper, aContext), new Date());
  }

  @Override
  public synchronized Context pop(String aJobId) {
    Context context = peek(aJobId);
    jdbc.update("delete from job_context where id = ?",context.getId());
    return context;
  }

  @Override
  public Context peek(String aJobId) {
    MapContext context = jdbc.queryForObject("select id,data from job_context where job_id = ? order by creation_date desc limit 1",new Object[]{aJobId},(rs,i)->{
      String data = rs.getString(2);
      return new MapContext(JsonHelper.readValue(objectMapper, data, Map.class));
    });
    return context;
  }

  public void setJdbcTemplate (JdbcTemplate aJdbcTemplate) {
    jdbc = aJdbcTemplate;
  }
  
  public void setObjectMapper(ObjectMapper aObjectMapper) {
    objectMapper = aObjectMapper;
  }

}

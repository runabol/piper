/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Apr 2017
 */
package com.creactiviti.piper.core.context;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
  @Transactional
  public Context pop(String aJobId) {
    Context context = peek(aJobId);
    jdbc.update("delete from job_context where id = ?",context.getId());
    return context;
  }

  @Override
  public Context peek (String aJobId) {
    String sql = "select id,data from job_context where job_id = ? order by creation_date desc limit 1";
    return jdbc.queryForObject(sql,new Object[]{aJobId},this::contextRowMapper);
  }
  
  private Context contextRowMapper (ResultSet aResultSet, int aIndex) throws SQLException {
    String data = aResultSet.getString(2);
    return new MapContext(JsonHelper.readValue(objectMapper, data, Map.class));    
  }

  public void setJdbcTemplate (JdbcTemplate aJdbcTemplate) {
    jdbc = aJdbcTemplate;
  }
  
  public void setObjectMapper(ObjectMapper aObjectMapper) {
    objectMapper = aObjectMapper;
  }

}

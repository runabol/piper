/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.job;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.creactiviti.piper.core.Page;
import com.creactiviti.piper.core.ResultPage;
import com.creactiviti.piper.core.task.JobTask;
import com.creactiviti.piper.json.JsonHelper;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JdbcJobRepository implements JobRepository {

  private NamedParameterJdbcOperations jdbc;
  private ObjectMapper json = new ObjectMapper();
  
  public static final int DEFAULT_PAGE_SIZE = 25;
  
  @Override
  public Job findOne(String aId) {
    List<Job> query = jdbc.query("select * from job where id = :id", Collections.singletonMap("id", aId),this::jobRowMappper);
    if(query.size() == 1) {
      return query.get(0);
    }
    return null;
  }
  
  @Override
  public Job findJobByTaskId(String aTaskId) {
    Map<String, String> params = Collections.singletonMap("id", aTaskId);
    return jdbc.queryForObject("select * from job j where j.id = (select job_id from job_task jt where jt.id=:id)", params, this::jobRowMappper);
  }

  @Override
  public Page<Job> findAll(int aPageNumber) {
    Integer totalItems = jdbc.getJdbcOperations().queryForObject("select count(*) from job",Integer.class);
    int offset = (aPageNumber-1) * DEFAULT_PAGE_SIZE;
    int limit = offset + DEFAULT_PAGE_SIZE;
    List<Job> items = jdbc.query(String.format("select * from job order by creation_date desc offset %s limit %s",offset,limit),this::jobRowMappper);
    ResultPage<Job> resultPage = new ResultPage<>(Job.class);
    resultPage.setItems(items);
    resultPage.setNumber(items.size()>0?aPageNumber:0);
    resultPage.setTotalItems(totalItems);
    resultPage.setTotalPages(items.size()>0?totalItems/DEFAULT_PAGE_SIZE+1:0);
    return resultPage;
  }
      
  @Override
  public void update (Job aJob) {
    MapSqlParameterSource sqlParameterSource = createSqlParameterSource(aJob);
    jdbc.update("update job set data=:data,status=:status where id = :id ", sqlParameterSource);
  }

  @Override
  public void create (Job aJob) {
    MapSqlParameterSource sqlParameterSource = createSqlParameterSource(aJob);
    jdbc.update("insert into job (id,creation_date,data,status) values (:id,:creationDate,:data,:status)", sqlParameterSource);
  }

  private MapSqlParameterSource createSqlParameterSource(Job aJob) {
    MutableJob job = new MutableJob(aJob);
    job.remove("execution"); // don't want to store the execution as part of the job's data
    MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
    sqlParameterSource.addValue("id", job.getId());
    sqlParameterSource.addValue("creationDate", job.getCreationDate());
    sqlParameterSource.addValue("data", writeValueAsJsonString(job));
    sqlParameterSource.addValue("status", job.getStatus().toString());
    return sqlParameterSource;
  }
  
  @Override
  public void create(JobTask aJobTask) {
    SqlParameterSource sqlParameterSource = createSqlParameterSource(aJobTask);
    jdbc.update("insert into job_task (id,job_id,data,status) values (:id,:jobId,:data,:status)", sqlParameterSource);
  }
  
  @Override
  public void update(JobTask aJobTask) {
    SqlParameterSource sqlParameterSource = createSqlParameterSource(aJobTask);
    jdbc.update("update job_task set data=:data,status=:status where id = :id ", sqlParameterSource);
  }

  private SqlParameterSource createSqlParameterSource(JobTask aJobTask) {
    MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
    sqlParameterSource.addValue("id", aJobTask.getId());
    sqlParameterSource.addValue("jobId", aJobTask.getJobId());
    sqlParameterSource.addValue("data", writeValueAsJsonString(aJobTask));
    sqlParameterSource.addValue("status", aJobTask.getStatus().toString());
    return sqlParameterSource;
  }
    
  public void setJdbcOperations (NamedParameterJdbcOperations aJdbcOperations) {
    jdbc = aJdbcOperations;
  }
  
  public void setJson(ObjectMapper aJson) {
    json = aJson;
  }
  
  private JobTask jobTaskRowMappper (ResultSet aRs, int aIndex) throws SQLException {
    MutableJobTask t = new MutableJobTask(readValueFromString(aRs.getString("data")));
    return t;
  }
    
  private Job jobRowMappper (ResultSet aRs, int aIndex) throws SQLException {
    Map<String, Object> map = readValueFromString(aRs.getString("data"));
    map.put("execution", getExecution(aRs.getString("id")));
    return new MutableJob(map);
  }
  
  private Map<String,Object> readValueFromString (String aValue) {
    return JsonHelper.readValue(json, aValue, Map.class);
  }

  private String writeValueAsJsonString (Object aValue) {
    return JsonHelper.writeValueAsString(json, aValue);
  }
  
  private List<JobTask> getExecution(String aJobId) {
    return jdbc.query("select * From job_task where job_id = :jobId ", Collections.singletonMap("jobId", aJobId),this::jobTaskRowMappper);
  }

}

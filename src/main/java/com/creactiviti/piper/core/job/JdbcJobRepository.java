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
import org.springframework.util.Assert;

import com.creactiviti.piper.core.Page;
import com.creactiviti.piper.core.ResultPage;
import com.creactiviti.piper.core.task.JobTask;
import com.creactiviti.piper.core.task.JobTaskRepository;
import com.creactiviti.piper.json.JsonHelper;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JdbcJobRepository implements JobRepository {

  private NamedParameterJdbcOperations jdbc;
  private ObjectMapper json = new ObjectMapper();
  private JobTaskRepository jobTaskRepository;
  
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
    List<Job> list = jdbc.query("select * from job j where j.id = (select job_id from job_task jt where jt.id=:id)", params, this::jobRowMappper);
    Assert.isTrue(list.size() < 2, "expecting 1 result, got: " + list.size());
    return list.size() == 1 ? list.get(0) : null;
  }

  @Override
  public Page<Job> findAll(int aPageNumber) {
    Integer totalItems = jdbc.getJdbcOperations().queryForObject("select count(*) from job",Integer.class);
    int offset = (aPageNumber-1) * DEFAULT_PAGE_SIZE;
    int limit = DEFAULT_PAGE_SIZE;
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
    jdbc.update("update job set data=:data,status=:status,completion_date=:completionDate where id = :id ", sqlParameterSource);
  }

  @Override
  public void create (Job aJob) {
    MapSqlParameterSource sqlParameterSource = createSqlParameterSource(aJob);
    jdbc.update("insert into job (id,creation_date,data,status) values (:id,:creationDate,:data,:status)", sqlParameterSource);
  }

  private MapSqlParameterSource createSqlParameterSource(Job aJob) {
    MutableJob job = new MutableJob(aJob);
    job.remove("execution"); // don't want to store the execution as part of the job's data
    Assert.notNull(aJob, "job must not be null");
    Assert.notNull(aJob.getId(), "job status must not be null");
    Assert.notNull(aJob.getCreationDate(), "job creationDate must not be null");
    Assert.notNull(aJob.getStatus(), "job status must not be null");
    MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
    sqlParameterSource.addValue("id", job.getId());
    sqlParameterSource.addValue("creationDate", job.getCreationDate());
    sqlParameterSource.addValue("data", writeValueAsJsonString(job));
    sqlParameterSource.addValue("status", job.getStatus().toString());
    sqlParameterSource.addValue("completionDate", job.getCompletionDate());
    return sqlParameterSource;
  }
  
  public void setJobTaskRepository(JobTaskRepository aJobTaskRepository) {
    jobTaskRepository = aJobTaskRepository;
  }
    
  public void setJdbcOperations (NamedParameterJdbcOperations aJdbcOperations) {
    jdbc = aJdbcOperations;
  }
  
  public void setObjectMapper(ObjectMapper aObjectMapper) {
    json = aObjectMapper;
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
    return jobTaskRepository.getExecution(aJobId);
  }

  @Override
  public int countRunningJobs() {
    return jdbc.queryForObject("select count(*) from job where status='STARTED'", Collections.EMPTY_MAP, Integer.class);
  }

  @Override
  public int countCompletedJobsToday() {
    return jdbc.queryForObject("select count(*) from job where status='COMPLETED' and completion_date >= current_date", Collections.EMPTY_MAP, Integer.class);
  }

  @Override
  public int countCompletedJobsYesterday() {
    return jdbc.queryForObject("select count(*) from job where status='COMPLETED' and completion_date >= current_date-1 and completion_date < current_date-1 ", Collections.EMPTY_MAP, Integer.class);
  }

}

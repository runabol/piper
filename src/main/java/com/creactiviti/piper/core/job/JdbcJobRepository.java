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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.util.Assert;

import com.creactiviti.piper.core.Page;
import com.creactiviti.piper.core.ResultPage;
import com.creactiviti.piper.core.task.TaskExecution;
import com.creactiviti.piper.core.task.TaskExecutionRepository;

public class JdbcJobRepository implements JobRepository {

  private NamedParameterJdbcOperations jdbc;
  private TaskExecutionRepository jobTaskRepository;
  
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
    List<Job> list = jdbc.query("select * from job j where j.id = (select job_id from task_execution jt where jt.id=:id)", params, this::jobRowMappper);
    Assert.isTrue(list.size() < 2, "expecting 1 result, got: " + list.size());
    return list.size() == 1 ? list.get(0) : null;
  }

  @Override
  public Page<Job> findAll(int aPageNumber) {
    Integer totalItems = jdbc.getJdbcOperations().queryForObject("select count(*) from job",Integer.class);
    int offset = (aPageNumber-1) * DEFAULT_PAGE_SIZE;
    int limit = DEFAULT_PAGE_SIZE;
    List<Job> items = jdbc.query(String.format("select * from job order by create_time desc offset %s limit %s",offset,limit),this::jobRowMappper);
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
    jdbc.update("update job set status=:status,start_time=:startTime,end_time=:endTime,current_task=:currentTask,pipeline_id=:pipelineId,name=:name where id = :id ", sqlParameterSource);
  }

  @Override
  public void create (Job aJob) {
    MapSqlParameterSource sqlParameterSource = createSqlParameterSource(aJob);
    jdbc.update("insert into job (id,create_time,start_time,status,current_task,pipeline_id,name) values (:id,:createTime,:startTime,:status,:currentTask,:pipelineId,:name)", sqlParameterSource);
  }

  private MapSqlParameterSource createSqlParameterSource(Job aJob) {
    SimpleJob job = new SimpleJob(aJob);
    Assert.notNull(aJob, "job must not be null");
    Assert.notNull(aJob.getId(), "job status must not be null");
    Assert.notNull(aJob.getCreateTime(), "job createTime must not be null");
    Assert.notNull(aJob.getStatus(), "job status must not be null");
    MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
    sqlParameterSource.addValue("id", job.getId());
    sqlParameterSource.addValue("status", job.getStatus().toString());
    sqlParameterSource.addValue("currentTask", job.getCurrentTask());
    sqlParameterSource.addValue("pipelineId", job.getPipelineId());
    sqlParameterSource.addValue("name", job.getName());
    sqlParameterSource.addValue("createTime", job.getCreateTime());
    sqlParameterSource.addValue("startTime", job.getStartTime());
    sqlParameterSource.addValue("endTime", job.getEndTime());
    return sqlParameterSource;
  }
  
  public void setJobTaskRepository(TaskExecutionRepository aJobTaskRepository) {
    jobTaskRepository = aJobTaskRepository;
  }
    
  public void setJdbcOperations (NamedParameterJdbcOperations aJdbcOperations) {
    jdbc = aJdbcOperations;
  }

  private Job jobRowMappper (ResultSet aRs, int aIndex) throws SQLException {
    Map<String, Object> map = new HashMap<>();
    map.put("id", aRs.getString("id"));
    map.put("status", aRs.getString("status"));
    map.put("currentTask", aRs.getInt("current_task"));
    map.put("pipelineId", aRs.getString("pipeline_id"));
    map.put("name", aRs.getString("name"));
    map.put("createTime", aRs.getDate("create_time"));
    map.put("startTime", aRs.getDate("start_time"));
    map.put("endTime", aRs.getDate("end_time"));
    map.put("execution", getExecution(aRs.getString("id")));
    return new SimpleJob(map);
  }
  
  private List<TaskExecution> getExecution(String aJobId) {
    return jobTaskRepository.getExecution(aJobId);
  }

  @Override
  public int countRunningJobs() {
    return jdbc.queryForObject("select count(*) from job where status='STARTED'", Collections.EMPTY_MAP, Integer.class);
  }

  @Override
  public int countCompletedJobsToday() {
    return jdbc.queryForObject("select count(*) from job where status='COMPLETED' and end_time >= current_date", Collections.EMPTY_MAP, Integer.class);
  }

  @Override
  public int countCompletedJobsYesterday() {
    return jdbc.queryForObject("select count(*) from job where status='COMPLETED' and end_time >= current_date-1 and end_time < current_date-1 ", Collections.EMPTY_MAP, Integer.class);
  }

}

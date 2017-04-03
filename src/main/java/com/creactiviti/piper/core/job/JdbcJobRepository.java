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

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.creactiviti.piper.core.task.JobTask;

public class JdbcJobRepository implements JobRepository {

  private NamedParameterJdbcOperations jdbc;
  
  @Override
  public Job findOne(String aId) {
    List<Job> query = jdbc.query("select * from job where id = :id", Collections.singletonMap("id", aId),this::jobRowMappper);
    if(query.size() == 1) {
      return query.get(0);
    }
    return null;
  }
  
  @Override
  public Job save(Job aJob) {
    Job existingJob = findOne(aJob.getId());
    if(existingJob == null) {
      createJob(aJob);
    }
    else {
      updateJob(aJob);      
    }
    return aJob;
  }

  private void updateJob(Job aJob) {
    SqlParameterSource jobParameterSource = new BeanPropertySqlParameterSource(aJob);
    jdbc.update("update job set status=:status", jobParameterSource);
    insertJobTasks(aJob);
  }

  private void createJob(Job aJob) {
    SqlParameterSource jobParameterSource = new BeanPropertySqlParameterSource(aJob);
    jdbc.update("insert into job (id,name,pipeline_id,status,creation_date) values (:id,:name,:pipelineId,:status,:creationDate)", jobParameterSource);
    insertJobTasks(aJob);
  }

  private void insertJobTasks(Job aJob) {
    List<JobTask> tasks = aJob.getExecution();
    for(JobTask t : tasks) {
      SqlParameterSource taskParameterSource = new BeanPropertySqlParameterSource(t);
      jdbc.update("insert into job_task (id,job_id,type,status,creation_date) values (:id,:jobId,:type,:status,:creationDate)", taskParameterSource);  
    }
  }

  @Override
  public Job findJobByTaskId(String aTaskId) {
    Map<String, String> params = Collections.singletonMap("id", aTaskId);
    return jdbc.queryForObject("select * from job j where j.id = (select job_id from job_task jt where jt.id=:id)", params, this::jobRowMappper);
  }

  @Override
  public List<Job> findAll() {
    return jdbc.query("select * from job order by id desc",this::jobRowMappper);
  }
  
  public void setJdbcOperations (NamedParameterJdbcOperations aJdbcOperations) {
    jdbc = aJdbcOperations;
  }
  
  private Job jobRowMappper (ResultSet aRs, int aIndex) throws SQLException {
    MutableJob j = new MutableJob();
    j.setPipelineId(aRs.getString("pipeline_id"));
    j.setId(aRs.getString("id"));
    return j;    
  }

}

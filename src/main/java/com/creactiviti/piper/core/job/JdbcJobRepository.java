/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core.job;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import com.creactiviti.piper.core.task.JobTask;
import com.creactiviti.piper.core.task.TaskStatus;

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
    BeanPropertySqlParameterSource jobParameterSource = new BeanPropertySqlParameterSource(aJob);
    jobParameterSource.registerSqlType("status", Types.VARCHAR);
    jdbc.update("update job set status=:status where id = :id ", jobParameterSource);
    insertOrUpdateJobTasks(aJob);
  }

  private void createJob(Job aJob) {
    BeanPropertySqlParameterSource jobParameterSource = new BeanPropertySqlParameterSource(aJob);
    jobParameterSource.registerSqlType("status", Types.VARCHAR);
    jdbc.update("insert into job (id,name,pipeline_id,status,creation_date) values (:id,:name,:pipelineId,:status,:creationDate)", jobParameterSource);
    insertOrUpdateJobTasks(aJob);
  }

  private void insertOrUpdateJobTasks(Job aJob) {
    List<JobTask> tasks = aJob.getExecution();
    for(JobTask t : tasks) {
      BeanPropertySqlParameterSource taskParameterSource = new BeanPropertySqlParameterSource(t);
      taskParameterSource.registerSqlType("status", Types.VARCHAR);
      if(findJobTaskById(t.getId())==null) {
        jdbc.update("insert into job_task (id,job_id,type,status,creation_date) values (:id,:jobId,:type,:status,:creationDate)", taskParameterSource);
      }
      else {
        jdbc.update("update job_task set status=:status,creation_date=:creationDate where id = :id ", taskParameterSource);
      }
    }
  }
  
  private JobTask findJobTaskById (String aJobTaskId) {
    List<JobTask> query = jdbc.query("select * from job_task where id = :id", Collections.singletonMap("id", aJobTaskId),this::jobTaskRowMappper);
    if(query.size() == 0) {
      return null;
    }
    return query.get(0);
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
  
  private JobTask jobTaskRowMappper (ResultSet aRs, int aIndex) throws SQLException {
    MutableJobTask t = new MutableJobTask();
    t.setId(aRs.getString("id"));
    t.setStatus(TaskStatus.valueOf(aRs.getString("status")));
    t.setCreationDate(aRs.getDate("creation_date"));
    return t;
  }
  
  private List<JobTask> findJobTasksByJobId (String aJobId) {
    return jdbc.query("select * From job_task where job_id = :jobId ", Collections.singletonMap("jobId", aJobId),this::jobTaskRowMappper);
  }
  
  private Job jobRowMappper (ResultSet aRs, int aIndex) throws SQLException {
    MutableJob j = new MutableJob();
    j.setPipelineId(aRs.getString("pipeline_id"));
    j.setId(aRs.getString("id"));
    j.setExecution(findJobTasksByJobId(j.getId()));
    j.setStatus(JobStatus.valueOf(aRs.getString("status")));
    return j;    
  }

}

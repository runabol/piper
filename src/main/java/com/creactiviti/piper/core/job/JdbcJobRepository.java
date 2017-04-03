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
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import com.creactiviti.piper.core.task.JobTask;
import com.creactiviti.piper.core.task.TaskStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;

public class JdbcJobRepository implements JobRepository {

  private NamedParameterJdbcOperations jdbc;
  private ObjectMapper json = new ObjectMapper();
  
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
  
  private void updateJob(Job aJob) {
    BeanPropertySqlParameterSource jobParameterSource = new BeanPropertySqlParameterSource(aJob);
    jobParameterSource.registerSqlType("status", Types.VARCHAR);
    jdbc.update("update job set status=:status,completion_date=:completionDate,start_date=:startDate,failed_date=:failedDate where id = :id ", jobParameterSource);
    insertOrUpdateJobTasks(aJob);
  }

  private void createJob(Job aJob) {
    BeanPropertySqlParameterSource jobParameterSource = new BeanPropertySqlParameterSource(aJob);
    jobParameterSource.registerSqlType("status", Types.VARCHAR);
    jdbc.update("insert into job (id,name,pipeline_id,status,creation_date,start_date) values (:id,:name,:pipelineId,:status,:creationDate,:startDate)", jobParameterSource);
    insertOrUpdateJobTasks(aJob);
  }

  private void insertOrUpdateJobTasks(Job aJob) {
    List<JobTask> tasks = aJob.getExecution();
    for(JobTask t : tasks) {      
      MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
      sqlParameterSource.addValue("id", t.getId());
      sqlParameterSource.addValue("name", t.getName());
      sqlParameterSource.addValue("label", t.getLabel());
      sqlParameterSource.addValue("node", t.getNode());
      sqlParameterSource.addValue("jobId", t.getJobId());
      sqlParameterSource.addValue("type", t.getType());
      sqlParameterSource.addValue("status", t.getStatus(), Types.VARCHAR);
      sqlParameterSource.addValue("creationDate", t.getCreationDate());
      sqlParameterSource.addValue("completionDate", t.getCompletionDate());
      sqlParameterSource.addValue("output", wrapValueAsJsonString("output", t.getOutput()));
      if(findJobTaskById(t.getId())==null) {
        jdbc.update("insert into job_task (id,job_id,name,label,type,node,status,creation_date) values (:id,:jobId,:name,:label,:type,:node,:status,:creationDate)", sqlParameterSource);
      }
      else {
        jdbc.update("update job_task set status=:status,completion_date=:completionDate,output=:output where id = :id ", sqlParameterSource);
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
  
  private JobTask jobTaskRowMappper (ResultSet aRs, int aIndex) throws SQLException {
    MutableJobTask t = new MutableJobTask();
    t.setId(aRs.getString("id"));
    t.setStatus(TaskStatus.valueOf(aRs.getString("status")));
    t.setCreationDate(aRs.getTimestamp("creation_date"));
    t.setCompletionDate(aRs.getTimestamp("completion_date"));
    t.setJobId(aRs.getString("job_id"));
    if(aRs.getString("output")!=null) {
      t.setOutput(unwrapValueFromJsonString("output",aRs.getString("output")));
    }
    t.setType(aRs.getString("type"));
    if(aRs.getString("node")!=null) {
      t.setNode(aRs.getString("node"));
    }
    if(aRs.getString("name")!=null) {
      t.setName(aRs.getString("name"));
    }
    if(aRs.getString("label")!=null) {
      t.setLabel(aRs.getString("label"));
    }
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
    j.setCreationDate(aRs.getTimestamp("creation_date"));
    j.setCompletionDate(aRs.getTimestamp("completion_date"));
    j.setStartDate(aRs.getTimestamp("start_date"));
    return j;    
  }
  
  private Object unwrapValueFromJsonString (String aKey, String aValue) {
    if(aValue == null) {
      return null;
    }
    try {
      return json.readValue(aValue, Map.class).get(aKey);
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }

  private String wrapValueAsJsonString (String aKey, Object aValue) {
    if(aValue == null) {
      return null;
    }
    try {
      return json.writeValueAsString(Collections.singletonMap(aKey,aValue));
    } catch (JsonProcessingException e) {
      throw Throwables.propagate(e);
    }
  }

}

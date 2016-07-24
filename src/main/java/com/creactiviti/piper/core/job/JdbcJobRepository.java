package com.creactiviti.piper.core.job;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.creactiviti.piper.core.task.JobTask;

public class JdbcJobRepository implements JobRepository {

  @Autowired private NamedParameterJdbcOperations jdbc;
  
  @Override
  public Job findOne(String aId) {
    jdbc.query("select * from job where job_id = :id", Collections.singletonMap("id", aId),(rs,i) ->{
        return null;
    });
    throw new UnsupportedOperationException();
  }

  @Override
  public Job save(Job aJob) {
    SqlParameterSource jobParameterSource = new BeanPropertySqlParameterSource(aJob);
    jdbc.update("insert into job (job_id,status,creation_date) values (:id,:status,:creationDate)", jobParameterSource);
    List<JobTask> tasks = aJob.getTasks();
    for(JobTask t : tasks) {
      Map<String, Object> taskParams = new HashMap<String, Object> ();
      taskParams.put("id", t.getId());
      taskParams.put("jobId", aJob.getId());
      taskParams.put("status",t.getStatus());
      taskParams.put("creationDate", t.getCreationDate());
      jdbc.update("insert into job_task (job_task_id,job_id,status,creation_date) values (:id,:jobId,:status,:creationDate)", taskParams);  
    }
    return aJob;
  }

  @Override
  public Job findJobByTaskId(String aTaskId) {
    return null;
  }

  @Override
  public JobTask nextTask(Job aJob) {
    return null;
  }

}

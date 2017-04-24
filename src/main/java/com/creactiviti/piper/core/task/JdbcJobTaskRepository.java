package com.creactiviti.piper.core.task;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.transaction.annotation.Transactional;

import com.creactiviti.piper.core.job.MutableJobTask;
import com.creactiviti.piper.json.JsonHelper;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JdbcJobTaskRepository implements JobTaskRepository {

  private NamedParameterJdbcOperations jdbc;
  private ObjectMapper json = new ObjectMapper();
  
  @Override
  public JobTask findOne(String aJobTaskId) {
    List<JobTask> query = jdbc.query("select * from job_task where id = :id", Collections.singletonMap("id", aJobTaskId),this::jobTaskRowMappper);
    if(query.size() == 1) {
      return query.get(0);
    }
    return null;
  }

  @Override
  public void create(JobTask aJobTask) {
    SqlParameterSource sqlParameterSource = createSqlParameterSource(aJobTask);
    jdbc.update("insert into job_task (id,job_id,data,status,creation_date) values (:id,:jobId,:data,:status,:creationDate)", sqlParameterSource);
  }
  
  @Override
  @Transactional
  public void update(JobTask aJobTask) {
    MutableJobTask mjobTask = MutableJobTask.createForUpdate(aJobTask);
    JobTask jobTask = jdbc.queryForObject("select * from job_task where id = :id for update", Collections.singletonMap("id", aJobTask.getId()),this::jobTaskRowMappper);
    if(jobTask.getStatus() == TaskStatus.COMPLETED) {
      mjobTask.setStatus(TaskStatus.COMPLETED);
    }
    SqlParameterSource sqlParameterSource = createSqlParameterSource(mjobTask);
    jdbc.update("update job_task set data=:data,status=:status where id = :id ", sqlParameterSource);
  }
  
  @Override
  public List<JobTask> getExecution(String aJobId) {
    return jdbc.query("select * From job_task where job_id = :jobId order by creation_date asc", Collections.singletonMap("jobId", aJobId),this::jobTaskRowMappper);
  }
  
  private JobTask jobTaskRowMappper (ResultSet aRs, int aIndex) throws SQLException {
    return MutableJobTask.createFromMap(readValueFromString(aRs.getString("data")));
  }

  private SqlParameterSource createSqlParameterSource(JobTask aJobTask) {
    MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
    sqlParameterSource.addValue("id", aJobTask.getId());
    sqlParameterSource.addValue("jobId", aJobTask.getJobId());
    sqlParameterSource.addValue("data", writeValueAsJsonString(aJobTask));
    sqlParameterSource.addValue("status", aJobTask.getStatus().toString());
    sqlParameterSource.addValue("creationDate", aJobTask.getCreationDate());
    return sqlParameterSource;
  }
  
  public void setJdbcOperations (NamedParameterJdbcOperations aJdbcOperations) {
    jdbc = aJdbcOperations;
  }
  
  public void setObjectMapper (ObjectMapper aJson) {
    json = aJson;
  }
  
  private Map<String,Object> readValueFromString (String aValue) {
    return JsonHelper.readValue(json, aValue, Map.class);
  }

  private String writeValueAsJsonString (Object aValue) {
    return JsonHelper.writeValueAsString(json, aValue);
  }
  

}

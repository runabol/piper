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
import org.springframework.util.Assert;

import com.creactiviti.piper.core.job.SimpleTaskExecution;
import com.creactiviti.piper.json.JsonHelper;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JdbcTaskExecutionRepository implements TaskExecutionRepository {

  private NamedParameterJdbcOperations jdbc;
  private ObjectMapper json = new ObjectMapper();
  
  @Override
  public TaskExecution findOne(String aJobTaskId) {
    List<TaskExecution> query = jdbc.query("select * from job_task where id = :id", Collections.singletonMap("id", aJobTaskId),this::jobTaskRowMappper);
    if(query.size() == 1) {
      return query.get(0);
    }
    return null;
  }

  @Override
  public void create(TaskExecution aJobTask) {
    SqlParameterSource sqlParameterSource = createSqlParameterSource(aJobTask);
    jdbc.update("insert into job_task (id,parent_id,job_id,data,status,create_time) values (:id,:parentId,:jobId,:data,:status,:createTime)", sqlParameterSource);
  }
  
  @Override
  @Transactional
  public TaskExecution merge (TaskExecution aJobTask) {
    SimpleTaskExecution mjobTask = SimpleTaskExecution.createForUpdate(aJobTask);
    TaskExecution currentTask = jdbc.queryForObject("select * from job_task where id = :id for update", Collections.singletonMap("id", aJobTask.getId()),this::jobTaskRowMappper);
    if(currentTask.getStatus().value() > aJobTask.getStatus().value()) { 
      return currentTask;
    }
    SqlParameterSource sqlParameterSource = createSqlParameterSource(mjobTask);
    jdbc.update("update job_task set data=:data,status=:status where id = :id ", sqlParameterSource);
    return aJobTask;
  }
  
  @Override
  public List<TaskExecution> getExecution (String aJobId) {
    return jdbc.query("select * From job_task where job_id = :jobId order by create_time asc", Collections.singletonMap("jobId", aJobId),this::jobTaskRowMappper);
  }
  
  @Override
  @Transactional
  public long completeSubTask (TaskExecution aJobSubTask) {
    Assert.notNull(aJobSubTask.getParentId(), "parentId can't be null");
    TaskExecution parentTask = jdbc.queryForObject("select * from job_task where id = :id for update", Collections.singletonMap("id", aJobSubTask.getParentId()),this::jobTaskRowMappper);
    SimpleTaskExecution mparentTask = SimpleTaskExecution.createForUpdate(parentTask);
    List<Object> list = parentTask.getList("list", Object.class);
    long increment = mparentTask.increment("iterations");
    merge(aJobSubTask);
    merge(mparentTask);
    return (list.size()-increment);
  }
  
  private TaskExecution jobTaskRowMappper (ResultSet aRs, int aIndex) throws SQLException {
    return SimpleTaskExecution.createFromMap(readValueFromString(aRs.getString("data")));
  }

  private SqlParameterSource createSqlParameterSource(TaskExecution aJobTask) {
    MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
    sqlParameterSource.addValue("id", aJobTask.getId());
    sqlParameterSource.addValue("parentId", aJobTask.getParentId());
    sqlParameterSource.addValue("jobId", aJobTask.getJobId());
    sqlParameterSource.addValue("data", writeValueAsJsonString(aJobTask));
    sqlParameterSource.addValue("status", aJobTask.getStatus().toString());
    sqlParameterSource.addValue("createTime", aJobTask.getCreateTime());
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

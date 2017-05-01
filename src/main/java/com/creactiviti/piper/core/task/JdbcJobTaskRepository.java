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
    jdbc.update("insert into job_task (id,parent_id,job_id,data,status,create_time) values (:id,:parentId,:jobId,:data,:status,:createTime)", sqlParameterSource);
  }
  
  @Override
  @Transactional
  public void update (JobTask aJobTask) {
    MutableJobTask mjobTask = MutableJobTask.createForUpdate(aJobTask);
    JobTask currentTask = jdbc.queryForObject("select * from job_task where id = :id for update", Collections.singletonMap("id", aJobTask.getId()),this::jobTaskRowMappper);
    if(currentTask.getStatus().value() > aJobTask.getStatus().value()) { 
      return;
    }
    SqlParameterSource sqlParameterSource = createSqlParameterSource(mjobTask);
    jdbc.update("update job_task set data=:data,status=:status where id = :id ", sqlParameterSource);
  }
  
  @Override
  public List<JobTask> getTasks (String aJobId) {
    return jdbc.query("select * From job_task where job_id = :jobId order by create_time asc", Collections.singletonMap("jobId", aJobId),this::jobTaskRowMappper);
  }
  
  @Override
  @Transactional
  public long completeSubTask (JobTask aJobSubTask) {
    Assert.notNull(aJobSubTask.getParentId(), "parentId can't be null");
    JobTask parentTask = jdbc.queryForObject("select * from job_task where id = :id for update", Collections.singletonMap("id", aJobSubTask.getParentId()),this::jobTaskRowMappper);
    MutableJobTask mparentTask = MutableJobTask.createForUpdate(parentTask);
    List<Object> list = parentTask.getList("list", Object.class);
    long increment = mparentTask.increment("iterations");
    update(aJobSubTask);
    update(mparentTask);
    return (list.size()-increment);
  }
  
  private JobTask jobTaskRowMappper (ResultSet aRs, int aIndex) throws SQLException {
    return MutableJobTask.createFromMap(readValueFromString(aRs.getString("data")));
  }

  private SqlParameterSource createSqlParameterSource(JobTask aJobTask) {
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

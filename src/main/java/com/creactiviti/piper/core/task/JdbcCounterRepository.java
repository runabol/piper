package com.creactiviti.piper.core.task;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.transaction.annotation.Transactional;

public class JdbcCounterRepository implements CounterRepository {

  private final JdbcOperations jdbc;
  
  
  public JdbcCounterRepository (JdbcOperations aJdbcOperations) {
    jdbc = aJdbcOperations;
  }
  
  @Override
  @Transactional
  public void set (String aCounterName, long aValue) {
    try {
      jdbc.queryForObject("select value from counter where id = ? for update", Long.class, aCounterName);
      jdbc.update("update counter set value = ? where id = ?",aValue,aCounterName);
    }
    catch (EmptyResultDataAccessException e) {
      jdbc.update("insert into counter (id,value) values (?,?)",aCounterName,aValue);
    }
  }
  
  @Override
  @Transactional
  public long decrement(String aCounterName) {
    Long value = jdbc.queryForObject("select value from counter where id = ? for update", Long.class, aCounterName);
    value = value - 1;
    jdbc.update("update counter set value = ? where id = ?",value,aCounterName);
    return value;
  }
  

  @Override
  public void delete (String aCounterName) {
    jdbc.update("delete from counter where id = ?",aCounterName);
  }

}

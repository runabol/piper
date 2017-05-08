package com.creactiviti.piper.core.job;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.creactiviti.piper.core.Page;
import com.creactiviti.piper.core.task.JdbcJobTaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class JdbcJobRepositoryTests {

  @Autowired
  private DataSource dataSource; 
  
  @Test
  public void test1 () {
    JdbcJobTaskRepository taskRepository = new JdbcJobTaskRepository();
    taskRepository.setJdbcOperations(new NamedParameterJdbcTemplate(dataSource));
    taskRepository.setObjectMapper(createObjectMapper());
    
    JdbcJobRepository jobRepository = new JdbcJobRepository();
    jobRepository.setJdbcOperations(new NamedParameterJdbcTemplate(dataSource));
    jobRepository.setJobTaskRepository(taskRepository);
    
    MutableJob job = new MutableJob();
    job.setPipelineId("demo:1234");
    job.setId("1");
    job.setCreateTime(new Date());
    job.setStatus(JobStatus.CREATED);
    jobRepository.create(job);
    
    Page<Job> all = jobRepository.findAll(1);
    Assert.assertEquals(1,all.getSize());
    Assert.assertEquals("1",all.getItems().get(0).getId());
    
    Job one = jobRepository.findOne("1");
    Assert.assertNotNull(one);
  }
  
  @Test
  public void test2 () {
    JdbcJobTaskRepository taskRepository = new JdbcJobTaskRepository();
    taskRepository.setJdbcOperations(new NamedParameterJdbcTemplate(dataSource));
    taskRepository.setObjectMapper(createObjectMapper());
    
    JdbcJobRepository jobRepository = new JdbcJobRepository();
    jobRepository.setJdbcOperations(new NamedParameterJdbcTemplate(dataSource));
    jobRepository.setJobTaskRepository(taskRepository);
    
    MutableJob job = new MutableJob();
    job.setId("2");
    job.setPipelineId("demo:1234");
    job.setCreateTime(new Date());
    job.setStatus(JobStatus.CREATED);
    jobRepository.create(job);
    
    Job one = jobRepository.findOne("2");
    
    MutableJob mjob = new MutableJob(one);
    mjob.setStatus(JobStatus.FAILED);
    
    // test immutability
    Assert.assertNotEquals(mjob.getStatus().toString(),one.getStatus().toString());  
    
    jobRepository.update(mjob);
    one = jobRepository.findOne("2");
    Assert.assertEquals("FAILED",one.getStatus().toString());  
  }
  
  private ObjectMapper createObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ"));
    return objectMapper;
  }
  
}

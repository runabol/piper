package com.creactiviti.piper.core.job;

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


@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class JdbcJobRepositoryTests {

  @Autowired
  private DataSource dataSource; 
  
  @Test
  public void test1 () {
    JdbcJobRepository jobRepository = new JdbcJobRepository();
    jobRepository.setJdbcOperations(new NamedParameterJdbcTemplate(dataSource));
    
    MutableJob job = new MutableJob();
    job.setId("1");
    job.setCreationDate(new Date());
    job.setStatus(JobStatus.CREATED);
    jobRepository.create(job);
    
    Page<Job> all = jobRepository.findAll(1);
    Assert.assertEquals(1,all.getSize());
    Assert.assertEquals("1",all.getItems().get(0).getId());
    
    Job one = jobRepository.findOne("1");
    Assert.assertNotNull(one);
  }
  
}

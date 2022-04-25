
package com.creactiviti.piper.core.pipeline;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@SpringBootTest
public class JdbcPipelineRepositoryTests {

  @Autowired
  private DataSource dataSource;

  @Test
  public void testEmpty () {
    JdbcPipelineRepository jdbcPipelineRepository = new JdbcPipelineRepository();
    jdbcPipelineRepository.setJdbc(new NamedParameterJdbcTemplate(dataSource));
    List<Pipeline> findAll = jdbcPipelineRepository.findAll();
    Assertions.assertEquals(0, findAll.size());
  }

  @Test
  public void testCron () {
    JdbcPipelineRepository jdbcPipelineRepository = new JdbcPipelineRepository();
    jdbcPipelineRepository.setJdbc(new NamedParameterJdbcTemplate(dataSource));

    String pipelineId = "cronDemo";

    jdbcPipelineRepository.create(new SimplePipeline(Map.of("id", pipelineId, "label", "label1", "tasks",
            Collections.singleton(Map.of("foo", "bar")), "inputs", Collections.emptyList(),
            "outputs", Collections.emptyList(), "retry", 1)));

    List<Pipeline> findAll = jdbcPipelineRepository.findAll();
    Assertions.assertEquals(1, findAll.size());

    Pipeline cronDemo = jdbcPipelineRepository.findOne(pipelineId);
    Assertions.assertEquals(pipelineId, cronDemo.getId());
  }
}

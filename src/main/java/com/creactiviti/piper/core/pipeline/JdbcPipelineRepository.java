package com.creactiviti.piper.core.pipeline;

import com.creactiviti.piper.core.DSL;
import com.creactiviti.piper.core.job.Job;
import com.creactiviti.piper.core.job.SimpleJob;
import com.creactiviti.piper.core.json.Json;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.util.Assert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class JdbcPipelineRepository implements PipelineRepository {

    private NamedParameterJdbcOperations jdbc;

    private final ObjectMapper json = new ObjectMapper();

    @Override
    public Pipeline findOne(String aId) {
        List<Pipeline> query = jdbc.query("select * from pipeline where id = :id", Collections.singletonMap("id", aId), this::jobRowMappper);
        Assert.isTrue(query.size() == 1, "expected 1 result. got " + query.size());
        return query.get(0);
    }

    @Override
    public List<Pipeline> findAll() {
        return jdbc.query("select * from pipeline", this::jobRowMappper);
    }

    private Pipeline jobRowMappper (ResultSet aRs, int aIndex) throws SQLException {
        Map<String, Object> map = new HashMap<>();
        map.put("id", aRs.getString("id"));
        map.put("label", aRs.getString("label"));
        map.put("inputs", Json.deserialize(json,aRs.getString("inputs"), List.class));
        map.put("outputs", Json.deserialize(json,aRs.getString("outputs"), List.class));
        map.put("tasks", Json.deserialize(json,aRs.getString("tasks"), List.class));
        map.put("retry", aRs.getInt("retry"));
        return new SimplePipeline(map);
    }

    @Override
    public void create(Pipeline aPipeline) {
        MapSqlParameterSource sqlParameterSource = createSqlParameterSource(aPipeline);
        jdbc.update("insert into pipeline (id,label,inputs,outputs,tasks,retry) values (:id,:label,:inputs,:outputs,:tasks,:retry)", sqlParameterSource);
    }

    private MapSqlParameterSource createSqlParameterSource(Pipeline aPipeline) {
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("id", aPipeline.getId());
        sqlParameterSource.addValue("label", aPipeline.getLabel());
        sqlParameterSource.addValue("inputs", Json.serialize(json, aPipeline.getInputs()));
        sqlParameterSource.addValue("outputs", Json.serialize(json, aPipeline.getOutputs()));
        sqlParameterSource.addValue("tasks", Json.serialize(json, aPipeline.getTasks()));
        sqlParameterSource.addValue("retry", aPipeline.getRetry());
        return sqlParameterSource;
    }

    public void setJdbc(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }
}

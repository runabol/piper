package com.creactiviti.piper;

import java.util.HashMap;

import org.springframework.boot.context.properties.ConfigurationProperties;

@SuppressWarnings("serial")
@ConfigurationProperties("piper.worker")
public class WorkerProperties extends HashMap<String, Object> {

    
}
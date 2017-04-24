/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Apr 2017
 */
package com.creactiviti.piper.core;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.creactiviti.piper.core.context.Context;
import com.creactiviti.piper.core.context.ContextRepository;
import com.creactiviti.piper.core.context.MapContext;
import com.creactiviti.piper.core.job.Job;
import com.creactiviti.piper.core.job.JobRepository;
import com.creactiviti.piper.core.job.JobStatus;
import com.creactiviti.piper.core.job.MutableJob;
import com.creactiviti.piper.core.job.MutableJobTask;
import com.creactiviti.piper.core.pipeline.Pipeline;
import com.creactiviti.piper.core.pipeline.PipelineRepository;
import com.creactiviti.piper.core.task.JobTask;
import com.creactiviti.piper.core.task.JobTaskRepository;
import com.creactiviti.piper.core.task.PipelineTask;
import com.creactiviti.piper.core.task.SpelTaskEvaluator;
import com.creactiviti.piper.core.task.TaskDispatcher;
import com.creactiviti.piper.core.task.TaskEvaluator;
import com.creactiviti.piper.core.task.TaskStatus;
import com.creactiviti.piper.core.uuid.UUIDGenerator;


/**
 * 
 * @author Arik Cohen
 * @since Apr 24, 2017
 */
@Component
public class DefaultTaskCompletionHandler implements TaskCompletionHandler {

  private Logger log = LoggerFactory.getLogger(getClass());
  
  private JobRepository jobRepository;
  private PipelineRepository pipelineRepository;
  private JobTaskRepository jobTaskRepository;
  private ContextRepository contextRepository;
  private TaskDispatcher taskDispatcher;
  private TaskEvaluator taskEvaluator = new SpelTaskEvaluator();
  
  @Override
  public void handle(JobTask aTask) {
    log.debug("Completing task {}", aTask.getId());
    MutableJobTask task = MutableJobTask.createForUpdate(aTask);
    task.setStatus(TaskStatus.COMPLETED);
    task.setError(null);
    Job job = jobRepository.findJobByTaskId (aTask.getId());
    if(job!=null) {
      Pipeline pipeline = pipelineRepository.findOne(job.getPipelineId());
      MutableJob mjob = new MutableJob (job);
      mjob.setCurrentTask(mjob.getCurrentTask()+1);
      jobTaskRepository.update(task);
      jobRepository.update(mjob);
      if(task.getOutput() != null && task.getName() != null) {
        Context context = contextRepository.pop(job.getId());
        MapContext newContext = new MapContext(context.asMap());
        newContext.setId(UUIDGenerator.generate());
        newContext.put(task.getName(), task.getOutput());
        contextRepository.push(job.getId(), newContext);
      }
      execute(mjob,pipeline);
    }
    else {
      log.error("Unknown job: {}",aTask.getJobId());
    }
  }
  
  private void execute (MutableJob aJob, Pipeline aPipeline) {
    if(aJob.getStatus() != JobStatus.STARTED) {
      return;
    }
    else if(hasMoreTasks(aJob, aPipeline)) {
      executeNextTask (aJob, aPipeline);
    }
    else {
      complete(aJob);
    }
  }

  private boolean hasMoreTasks (Job aJob, Pipeline aPipeline) {
    return aJob.getCurrentTask()+1 < aPipeline.getTasks().size();
  }

  private JobTask nextTask(Job aJob, Pipeline aPipeline) {
    PipelineTask task = aPipeline.getTasks().get(aJob.getCurrentTask()+1);
    MutableJobTask mt = MutableJobTask.createFrom (task);
    mt.setJobId(aJob.getId());
    jobTaskRepository.create(mt);
    return mt;
  }

  private void executeNextTask (MutableJob aJob, Pipeline aPipeline) {
    JobTask nextTask = nextTask(aJob, aPipeline); 
    jobRepository.update(aJob);
    Context context = contextRepository.peek(aJob.getId());
    JobTask evaluatedTask = taskEvaluator.evaluate(nextTask,context);
    taskDispatcher.dispatch(evaluatedTask);
  }
  
  private void complete (MutableJob aJob) {
    contextRepository.pop(aJob.getId());
    MutableJob job = new MutableJob((Job)aJob);
    job.setStatus(JobStatus.COMPLETED);
    job.setCompletionDate(new Date ());
    jobRepository.update(job);
    log.debug("Job {} completed successfully",aJob.getId());
  }
  
  @Autowired
  public void setJobRepository(JobRepository aJobRepository) {
    jobRepository = aJobRepository;
  }
  
  @Autowired
  public void setPipelineRepository(PipelineRepository aPipelineRepository) {
    pipelineRepository = aPipelineRepository;
  }

  @Autowired
  public void setJobTaskRepository(JobTaskRepository aJobTaskRepository) {
    jobTaskRepository = aJobTaskRepository;
  }
  
  @Autowired
  public void setContextRepository(ContextRepository aContextRepository) {
    contextRepository = aContextRepository;
  }
  
  @Autowired
  public void setTaskDispatcher(TaskDispatcher aTaskDispatcher) {
    taskDispatcher = aTaskDispatcher;
  }
  
  public void setTaskEvaluator(TaskEvaluator aTaskEvaluator) {
    taskEvaluator = aTaskEvaluator;
  }

}

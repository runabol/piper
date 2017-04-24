/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Apr 2017
 */
package com.creactiviti.piper.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.creactiviti.piper.core.context.Context;
import com.creactiviti.piper.core.context.ContextRepository;
import com.creactiviti.piper.core.job.Job;
import com.creactiviti.piper.core.job.JobRepository;
import com.creactiviti.piper.core.job.JobStatus;
import com.creactiviti.piper.core.job.MutableJobTask;
import com.creactiviti.piper.core.pipeline.Pipeline;
import com.creactiviti.piper.core.pipeline.PipelineRepository;
import com.creactiviti.piper.core.task.JobTask;
import com.creactiviti.piper.core.task.JobTaskRepository;
import com.creactiviti.piper.core.task.PipelineTask;
import com.creactiviti.piper.core.task.SpelTaskEvaluator;
import com.creactiviti.piper.core.task.TaskDispatcher;
import com.creactiviti.piper.core.task.TaskEvaluator;

/**
 * 
 * @author Arik Cohen
 * @since Apr 24, 2017
 */
@Component
public class DefaultJobExecutor implements JobExecutor {

  private PipelineRepository pipelineRepository;
  private JobTaskRepository jobTaskRepository;
  private JobRepository jobRepository;
  private ContextRepository contextRepository;
  private TaskDispatcher taskDispatcher;
  private TaskEvaluator taskEvaluator = new SpelTaskEvaluator();
  
  @Override
  public void execute (Job aJob) {
    Pipeline pipeline = pipelineRepository.findOne(aJob.getPipelineId());
    if(aJob.getStatus() != JobStatus.STARTED) {
      return;
    }
    else if(hasMoreTasks(aJob, pipeline)) {
      executeNextTask (aJob, pipeline);
    }
    else {
      throw new IllegalStateException("no tasks to execute!");
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

  private void executeNextTask (Job aJob, Pipeline aPipeline) {
    JobTask nextTask = nextTask(aJob, aPipeline); 
    jobRepository.update(aJob);
    Context context = contextRepository.peek(aJob.getId());
    JobTask evaluatedTask = taskEvaluator.evaluate(nextTask,context);
    taskDispatcher.dispatch(evaluatedTask);
  }
  
  @Autowired
  public void setPipelineRepository(PipelineRepository aPipelineRepository) {
    pipelineRepository = aPipelineRepository;
  }
  
  @Autowired
  public void setJobRepository(JobRepository aJobRepository) {
    jobRepository = aJobRepository;
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

}

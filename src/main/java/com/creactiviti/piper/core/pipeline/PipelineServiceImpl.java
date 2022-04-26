package com.creactiviti.piper.core.pipeline;

import com.creactiviti.piper.core.schedule.PipelineJob;
import com.creactiviti.piper.core.schedule.QuartzService;
import com.creactiviti.piper.core.task.EbotTaskHandler;
import com.creactiviti.piper.core.task.PipelineTask;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class PipelineServiceImpl implements PipelineService {

    private final PipelineRepository pipelineRepository;
    private final QuartzService quartzService;
    private final Map<String, EbotTaskHandler<Object>> ebotTaskHandlerMap;

    public PipelineServiceImpl(PipelineRepository pipelineRepository, QuartzService quartzService, Map<String, EbotTaskHandler<Object>> ebotTaskHandlerMap, PipelineJob pipelineJob) {
        this.pipelineRepository = pipelineRepository;
        this.quartzService = quartzService;
        this.ebotTaskHandlerMap = ebotTaskHandlerMap;
    }

    @Override
    public void save(Pipeline pipeline) {
        saveCronIfExist(pipeline);
        pipelineRepository.create(pipeline);
    }

    void saveCronIfExist(Pipeline pipeline) {
        List<PipelineTask> tasks = pipeline.getTasks();
        if (tasks.size() <= 0) {
            return;
        }
        PipelineTask pipelineTask = tasks.get(0);
        String type = pipelineTask.getType();
        EbotTaskHandler<Object> handler = ebotTaskHandlerMap.get(type);
        if (handler != null) {
            String cron = pipelineTask.getRequiredString("cron");
            quartzService.addJob(PipelineJob.class, "pipelineTask", "pipelineTask", cron,
                    Collections.singletonMap("pipelineId", pipeline.getId()));
        }
    }
}

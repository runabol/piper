package com.creactiviti.piper.core.pipeline;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.creactiviti.piper.core.Task;
import com.creactiviti.piper.core.task.MutableTask;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.base.Throwables;
import com.google.common.io.Files;

public class GitPipelineRepository implements PipelineRepository  {

  private String url;
  private String searchPath;
  private final Logger logger = LoggerFactory.getLogger(getClass());
  
  
  @Override
  public List<Pipeline> findAll () {
    List<GitResource> resources = getResources(searchPath);
    return resources.stream()
                    .map(r -> read(r))
                    .collect(Collectors.toList());
  }

  private Pipeline read (GitResource aResource) {
    try (InputStream in = aResource.getInputStream()) {
      String yaml = IOUtils.toString(in);
      ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
      Map<String,Object> yamlMap = mapper.readValue(yaml, Map.class);
      List<Map<String,Object>> rawTasks = (List<Map<String, Object>>) yamlMap.get("tasks");
      List<Task> tasks = rawTasks.stream().map(rt -> new MutableTask(rt)).collect(Collectors.toList());
      String id = aResource.getId();
      String name = (String)yamlMap.get("name");
      return new SimplePipeline(id, name, tasks);
    }
    catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  private List<GitResource> getResources (String aLocation) {
    try {
      File tempDir = Files.createTempDir();
      Git git = Git.cloneRepository()
                   .setURI(url)
                   .setDirectory(tempDir)
                   .call();
      
      Repository repo = git.getRepository();
      
      List<GitResource> resources = new ArrayList<>();
      
      final ObjectId id = repo.resolve(Constants.HEAD);
      try (ObjectReader reader = repo.newObjectReader(); RevWalk walk = new RevWalk(reader); TreeWalk treeWalk = new TreeWalk(repo,reader);) {
        RevCommit commit = walk.parseCommit(id);
        RevTree tree = commit.getTree();
        treeWalk.addTree(tree);
        treeWalk.setRecursive(true);
        while (treeWalk.next()) {
          String pathString = treeWalk.getPathString();
          if(pathString.startsWith(searchPath)) {
            ObjectId objectId = treeWalk.getObjectId(0);
            logger.debug("Loading {} [{}]",pathString,objectId.name());
            File file = readBlob(repo, objectId.name());
            String rid = pathString.substring(0, pathString.indexOf('.'))+"/"+objectId.name();
            resources.add(new GitResource(rid, file));
          }
        }
        return resources;
      }
    }
    catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }
  
  private File readBlob (Repository aRepo, String aBlobId) throws Exception {
    try (ObjectReader reader = aRepo.newObjectReader()) {
      byte[] data = reader.open(aRepo.resolve(aBlobId)).getBytes();
      File tempFile = File.createTempFile(aBlobId, null);
      FileUtils.writeStringToFile(tempFile, new String(data, "utf-8"));
      return tempFile;
    }
  }

  @Override
  public Pipeline findOne (String aId) {
    return null;
  }

  public void setUrl(String aUrl) {
    url = aUrl;
  }
  
  public void setSearchPath(String aSearchPath) {
    searchPath = aSearchPath;
  }

}

package com.creactiviti.piper.git;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.AbbreviatedObjectId;
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
import org.springframework.util.Assert;

import com.creactiviti.piper.core.pipeline.GitResource;
import com.google.common.base.Throwables;
import com.google.common.io.Files;

public class JGitTemplate implements GitOperations {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public List<GitResource> getHeadFiles (String aUrl, String... aSearchPaths) {
    Repository repo = getRepository(aUrl);
    List<String> searchPaths = Arrays.asList(aSearchPaths);
    List<GitResource> resources = new ArrayList<>();
    try (ObjectReader reader = repo.newObjectReader(); RevWalk walk = new RevWalk(reader); TreeWalk treeWalk = new TreeWalk(repo,reader);) {
      final ObjectId id = repo.resolve(Constants.HEAD);
      RevCommit commit = walk.parseCommit(id);
      RevTree tree = commit.getTree();
      treeWalk.addTree(tree);
      treeWalk.setRecursive(true);
      while (treeWalk.next()) {
        String path = treeWalk.getPathString();        
        if(searchPaths.stream().anyMatch((sp)->path.startsWith(getSearchPath(sp)))) {
          ObjectId objectId = treeWalk.getObjectId(0);
          logger.debug("Loading {} [{}]",path,objectId.name());
          resources.add(readBlob(repo, path.substring(0, path.indexOf('.')), objectId.name()));
        }
      }
      return resources;
    }
    catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }

  private String getSearchPath (String aSearchPath) {
    Assert.notNull(aSearchPath,"search path can't be null");
    return aSearchPath.endsWith("/")?aSearchPath:aSearchPath+"/";
  }

  private Repository getRepository(String url) {
    try {
      File tempDir = Files.createTempDir();
      Git git = Git.cloneRepository()
          .setURI(url)
          .setDirectory(tempDir)
          .call();
      return (git.getRepository());
    }
    catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }

  @Override
  public GitResource getFile(String aUrl, String aFileId) {
    try {
      Repository repository = getRepository(aUrl);
      String path = aFileId.substring(0,aFileId.lastIndexOf('/'));
      String blobId = aFileId.substring(aFileId.lastIndexOf('/')+1);
      return readBlob(repository,path,blobId);
    }
    catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }

  private GitResource readBlob (Repository aRepo, String aPath, String aBlobId) throws Exception {
    try (ObjectReader reader = aRepo.newObjectReader()) {
      ObjectId objectId = aRepo.resolve(aBlobId);
      byte[] data = reader.open(objectId).getBytes();
      AbbreviatedObjectId abbreviated = reader.abbreviate(objectId);
      return new GitResource(aPath+"/"+abbreviated.name(), data);
    }
  }

}

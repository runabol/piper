package com.creactiviti.piper.core.pipeline;

import java.io.File;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;

import com.google.common.io.Files;

public class JGitTest {


  public static void main(String[] args) throws Exception {
    File tempDir = Files.createTempDir();
    
    Git.cloneRepository()
       .setURI("git@github.com:creactiviti/piper-pipelines.git")
       .setDirectory(tempDir)
       .call();

    Git git = Git.open(tempDir);
    JGitTest t = new JGitTest();
    Repository repository = git.getRepository();
    Map<String, Ref> allRefs = repository.getAllRefs();
    System.out.println(allRefs);
    System.out.println("-------------------------------------------------------------------");
    System.out.println( t.readBlobLatest(repository, "demo/hello.yaml") );
    FileUtils.deleteDirectory(tempDir);
  }
  
  private String readBlobLatest (Repository aRepo, String aPath) throws Exception {
    final ObjectId id = aRepo.resolve(Constants.HEAD);
    try (ObjectReader reader = aRepo.newObjectReader(); RevWalk walk = new RevWalk(reader)) {
      // Get the commit object for that revision
      RevCommit commit = walk.parseCommit(id);
      // Get the revision's file tree
      RevTree tree = commit.getTree();
      // .. and narrow it down to the single file's path
      TreeWalk treewalk = TreeWalk.forPath(reader, aPath, tree);
      if (treewalk != null) {
        // use the blob id to read the file's data
        ObjectId objectId = treewalk.getObjectId(0);
        return readBlob(aRepo, objectId.name());
      } else {
        throw new IllegalArgumentException("Path not found: " + aPath);
      }
    }
  }

  private String readBlob (Repository aRepo, String aBlobId) throws Exception {
    try (ObjectReader reader = aRepo.newObjectReader()) {
      byte[] data = reader.open(aRepo.resolve(aBlobId)).getBytes();
      return new String(data, "utf-8");
    }
  }


}

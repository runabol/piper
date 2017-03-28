package com.creactiviti.piper.core.pipeline;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.ProtocolResolver;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.AntPathMatcher;

import com.google.common.base.Throwables;
import com.google.common.io.Files;

public class GitProtocolResolver implements ProtocolResolver {

  @Override
  public Resource resolve(String aLocation, ResourceLoader aResourceLoader) {
    if(aLocation.startsWith("git")) {
      Resource resolveInternal = resolveInternal(aLocation, aResourceLoader);
      return resolveInternal;
    }
    return null;
  }

  private Resource resolveInternal (String aLocation, ResourceLoader aResourceLoader) {
    try {
      File tempDir = Files.createTempDir();
      String baseUri = aLocation.substring(0, aLocation.indexOf(".git")+4);
      String rootDir = determineRootDir(aLocation);
      String path = rootDir.substring(aLocation.indexOf(".git")+4);
      Git.cloneRepository()
         .setURI(baseUri)
         .setDirectory(tempDir)
         .call();     
      return ( new FileSystemResource( new File(tempDir,path) ) );
    }
    catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }

  private String determineRootDir(String location) {
    int prefixEnd = location.indexOf(":") + 1;
    int rootDirEnd = location.length();
    while (rootDirEnd > prefixEnd && (new AntPathMatcher()).isPattern(location.substring(prefixEnd, rootDirEnd))) {
      rootDirEnd = location.lastIndexOf('/', rootDirEnd - 2) + 1;
    }
    if (rootDirEnd == 0) {
      rootDirEnd = prefixEnd;
    }
    return location.substring(0, rootDirEnd);
  }
  
}

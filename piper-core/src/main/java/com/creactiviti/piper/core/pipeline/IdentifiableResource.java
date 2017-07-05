
package com.creactiviti.piper.core.pipeline;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import org.springframework.core.io.Resource;

public class IdentifiableResource implements Resource  {

  private final String id;
  private final Resource resource;
  
  public IdentifiableResource (String aId, Resource aResource) {
    id = aId;
    resource = aResource;
  }
  
  public String getId() {
    return id;
  }
  
  @Override
  public InputStream getInputStream() throws IOException {
    return resource.getInputStream();
  }
  
  @Override
  public boolean exists() {
    return resource.exists();
  }
  
  @Override
  public boolean isReadable() {
    return resource.isReadable();
  }
  
  @Override
  public boolean isOpen() {
    return resource.isOpen();
  }
  
  @Override
  public URL getURL() throws IOException {
    return resource.getURL();
  }
  
  @Override
  public URI getURI() throws IOException {
    return resource.getURI();
  }
  
  @Override
  public File getFile() throws IOException {
    return resource.getFile();
  }
  
  @Override
  public long contentLength() throws IOException {
    return resource.contentLength();
  }
  
  @Override
  public long lastModified() throws IOException {
    return resource.lastModified();
  }
  
  @Override
  public Resource createRelative(String aRelativePath) throws IOException {
    return resource.createRelative(aRelativePath);
  }
  
  @Override
  public String getFilename() {
    return resource.getFilename();
  }
  
  @Override
  public String getDescription() {
    return resource.getDescription();
  }
  
    
}

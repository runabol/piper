/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.stats;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;


/**
 * <p>Carries application stats.</p>
 * 
 * <p>Each detail element can be singular or a hierarchical object such as a POJO or a nested
 * Map.</p>
 *
 * @author Arik Cohen
 * @since Apt 7, 2017
 */
public class Stats {
  
  private final Map<String, Object> details;

  private Stats(Builder builder) {
    LinkedHashMap<String, Object> content = new LinkedHashMap<String, Object>();
    content.putAll(builder.content);
    this.details = Collections.unmodifiableMap(content);
  }

  /**
   * Return the content.
   * @return the details of the info or an empty map.
   */
  @JsonAnyGetter
  public Map<String, Object> getDetails() {
    return this.details;
  }

  public Object get(String id) {
    return this.details.get(id);
  }

  @SuppressWarnings("unchecked")
  public <T> T get(String id, Class<T> type) {
    Object value = get(id);
    if (value != null && type != null && !type.isInstance(value)) {
      throw new IllegalStateException("Info entry is not of required type ["
          + type.getName() + "]: " + value);
    }
    return (T) value;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj instanceof Stats) {
      Stats other = (Stats) obj;
      return this.details.equals(other.details);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return this.details.hashCode();
  }

  @Override
  public String toString() {
    return getDetails().toString();
  }

  /**
   * Builder for creating immutable {@link Stats} instances.
   */
  public static class Builder {

    private final Map<String, Object> content;

    public Builder() {
      this.content = new LinkedHashMap<String, Object>();
    }

    /**
     * Record detail using given {@code key} and {@code value}.
     * @param key the detail key
     * @param value the detail value
     * @return this {@link Builder} instance
     */
    public Builder withDetail(String key, Object value) {
      this.content.put(key, value);
      return this;
    }

    /**
     * Record several details.
     * @param details the details
     * @return this {@link Builder} instance
     * @see #withDetail(String, Object)
     */
    public Builder withDetails(Map<String, Object> details) {
      this.content.putAll(details);
      return this;
    }

    /**
     * Create a new {@link Info} instance based on the state of this builder.
     * @return a new {@link Info} instance
     */
    public Stats build() {
      return new Stats(this);
    }

  }
}

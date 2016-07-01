package com.creactiviti.piper.core;

public interface Mutator {

  /**
   * Creates an association between the given key
   * and the given value.
   * 
   * @param aKey
   * @param aValue
   */
  void set (String aKey, Object aValue);
  
}

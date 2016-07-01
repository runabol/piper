package com.creactiviti.piper.core;

public interface Mutator {

  /**
   * Creates an association between the given key
   * and the given value.
   * 
   * @param aKey
   *          The key to associate with the value
   * @param aValue
   *          The value to associate with the key (can be <code>null</code>).
   */
  void set (String aKey, Object aValue);
  
}

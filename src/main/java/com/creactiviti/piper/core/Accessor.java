package com.creactiviti.piper.core;


public interface Accessor {

  /**
   * Return the value associated with the given key.
   * 
   * @param aKey
   *         The key associated with the desired value.
   * @return The value or <code>null</code> if no value 
   *         is associated with the given key.
   */
  <T> T get (Object aKey);
  
  /**
   * Return the value associated with the given key -- 
   * converting to the desired return type.
   * 
   * @param aKey
   *          The key associated with the desired value.
   * @param aReturnType
   *          The type to return the value as -- converting
   *          as neccessary.
   * @return The value associated with the given key.
   */
  <T> T get (Object aKey, Class<T> aReturnType);
  
  /**
   * Return the value associated with the given key -- 
   * converting to the desired return type.
   * 
   * @param aKey
   *          The key associated with the desired value.
   * @param aReturnType
   *          The type to return the value as -- converting
   *          as neccessary.
   * @param aDefaultValue
   *          The value to return if a value was not found
   *          for the given key or if the value is null. 
   * @return The value associated with the given key or 
   *         the default value.
   */
  <T> T get (Object aKey, Class<T> aReturnType, T aDefaultValue);
  
  /**
   * Return the {@link String} value associated with the given
   * key -- converting as necessary.
   * 
   * @param aKey
   *          The key associated with the desired value.
   * @return The string value associated with the given key --
   *         converting to {@link String} as needed.
   */
  String getString (Object aKey);
  
  /**
   * Return the {@link String} value associated with the given
   * key -- converting as necessary.
   * 
   * @param aKey
   *          The key associated with the desired value.
   * @param aDefaultValue
   *          The value to return if a value was not found
   *          for the given key or if the value is null.
   * @return The string value associated with the given key --
   *         converting to {@link String} as needed.
   */
  String getString (Object aKey, String aDefaultValue);
  
  Long getLong (Object aKey);
  
  Long getLong (Object aKey, long aDefaultValue);

  Double getDouble (Object aKey);
  
  Double getDouble (Object aKey, double aDefaultValue);
  
  Integer getInteger (Object aKey);
  
  Integer getInteger (Object aKey, int aDefaultValue);
  
}

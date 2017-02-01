package com.creactiviti.piper.core;

import java.util.Date;
import java.util.List;
import java.util.Map;


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
  
  /**
   * Return the {@link List} of items associated with the given
   * key.
   * 
   * @param aKey
   *          The key associated with the desired value.
   * @param aElementType
   *          The type of the list elements.
   * @return The list of items
   */
  <T> List<T> getList (Object aKey, Class<T> aElementType);
  
  /**
   * Return the {@link Long} value associated with the given
   * key -- converting as necessary.
   * 
   * @param aKey
   *          The key associated with the desired value.
   * @return The {@link Long} value associated with the given key --
   *         converting as needed.
   */
  Long getLong (Object aKey);
  
  /**
   * Return the {@link Long} value associated with the given
   * key -- converting as necessary.
   * 
   * @param aKey
   *          The key associated with the desired value.
   * @param aDefaultValue
   *          The value to return if a value was not found
   *          for the given key or if the value is null.
   * @return The {@link Long} value associated with the given key --
   *         converting as needed.
   */
  Long getLong (Object aKey, long aDefaultValue);

  /**
   * Return the {@link Double} value associated with the given
   * key -- converting as necessary.
   * 
   * @param aKey
   *          The key associated with the desired value.
   * @return The {@link Double} value associated with the given key --
   *         converting as needed.
   */
  Double getDouble (Object aKey);
  
  /**
   * Return the {@link Double} value associated with the given
   * key -- converting as necessary.
   * 
   * @param aKey
   *          The key associated with the desired value.
   * @param aDefaultValue
   *          The value to return if a value was not found
   *          for the given key or if the value is null.
   * @return The {@link Double} value associated with the given key --
   *         converting as needed.
   */
  Double getDouble (Object aKey, double aDefaultValue);
  
  /**
   * Return the {@link Integer} value associated with the given
   * key -- converting as necessary.
   * 
   * @param aKey
   *          The key associated with the desired value.
   * @return The {@link Integer} value associated with the given key --
   *         converting as needed.
   */
  Integer getInteger (Object aKey);
  
  /**
   * Return the {@link Integer} value associated with the given
   * key -- converting as necessary.
   * 
   * @param aKey
   *          The key associated with the desired value.
   * @param aDefaultValue
   *          The value to return if a value was not found
   *          for the given key or if the value is null.
   * @return The {@link Integer} value associated with the given key --
   *         converting as needed.
   */
  Integer getInteger (Object aKey, int aDefaultValue);
  
  /**
   * Return the {@link Date} value associated with the given
   * key -- converting as necessary.
   * 
   * @param aKey
   *          The key associated with the desired value.
   * @return The {@link Date} value associated with the given key --
   *         converting as needed.
   */
  Date getDate (Object aKey);
  
  /**
   * Return a map of all key-value pairs.
   * 
   * @return {@link Map}
   */
  Map<String, Object> toMap ();
  
}

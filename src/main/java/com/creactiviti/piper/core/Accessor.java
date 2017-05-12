/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
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
   * Determined if the given task contains the given key.
   * 
   * @param aKey
   *         The key to check for existance.
   * @return <code>true</code> if the key exists.<code>false</code> otherwise.
   */
  boolean containsKey (Object aKey);
  
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
   * Return the array value associated with the given
   * key.
   * 
   * @param aKey
   *          The key associated with the desired value.
   * @return The array value.
   */
  <T> T[] getArray (Object aKey, Class<T> aElementType);
  
  /**
   * Return the {@link MapObject} value associated with the given
   * key.
   * 
   * @param aKey
   *          The key associated with the desired value.
   * @return The {@link MapObject} value associated with the given key.
   */
  Map<String,Object> getMap (Object aKey);

  /**
   * Return the {@link String} value associated with the given
   * key -- converting as necessary.
   * 
   * @param aKey
   *          The key associated with the desired value.
   * @return The string value associated with the given key --
   *         converting to {@link String} as needed.
   * @throws IllegalArgumentException if no value is associated with the given key.
   */
  String getRequiredString (Object aKey);
  
  /**
   * Return the value associated with the given
   * key or throws an exception if no value is associated
   * with the given key.
   * 
   * @param aKey
   *          The key associated with the desired value.
   * @return The value associated with the given key.
   * @throws IllegalArgumentException if no value is associated with the given key.
   */
  <T> T getRequired (Object aKey);
  
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
   * Return the {@link List} of items associated with the given
   * key.
   * 
   * @param aKey
   *          The key associated with the desired value.
   * @param aElementType
   *          The type of the list elements.
   * @param aDefaultValue
   *          The list value to return none was not found
   *          for the given key or if the value is null.
   * @return The list of items
   */
  <T> List<T> getList (Object aKey, Class<T> aElementType, List<T> aDefaultValue);
  
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
  long getLong (Object aKey, long aDefaultValue);

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
   * Return the {@link Float} value associated with the given
   * key -- converting as necessary.
   * 
   * @param aKey
   *          The key associated with the desired value.
   * @return The {@link Float} value associated with the given key --
   *         converting as needed.
   */
  Float getFloat (Object aKey);
  
  /**
   * Return the {@link Float} value associated with the given
   * key -- converting as necessary.
   * 
   * @param aKey
   *          The key associated with the desired value.
   * @param aDefaultValue
   *          The value to return if a value was not found
   *          for the given key or if the value is null.
   * @return The {@link Float} value associated with the given key --
   *         converting as needed.
   */
  float getFloat (Object aKey, float aDefaultValue);
  
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
  int getInteger (Object aKey, int aDefaultValue);
  
  /**
   * Return the {@link Boolean} value associated with the given
   * key -- converting as necessary.
   * 
   * @param aKey
   *          The key associated with the desired value.
   * @return The {@link Integer} value associated with the given key --
   *         converting as needed.
   */
  Boolean getBoolean (Object aKey);
  
  /**
   * Return the {@link Boolean} value associated with the given
   * key -- converting as necessary.
   * 
   * @param aKey
   *          The key associated with the desired value.
   * @param aDefaultValue
   *          The default value if no value is associated with the key
   * @return The {@link Integer} value associated with the given key --
   *         converting as needed.
   */
  boolean getBoolean (Object aKey, boolean aDefaultValue);
  
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
  Map<String, Object> asMap ();
  
}

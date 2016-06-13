package com.creactiviti.piper.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.ConvertUtils;

/**
 * @author Arik Cohen
 * @since Jun 12, 2016
 */
public class Variables implements Map<String, Object> {

  private final Map<String, Object> map = new HashMap<String, Object> ();;

  @Override
  public int size() {
    return map.size();
  }

  @Override
  public boolean isEmpty() {
    return map.isEmpty();
  }

  @Override
  public boolean containsKey(Object aKey) {
    return map.containsKey(aKey);
  }

  @Override
  public boolean containsValue(Object aValue) {
    return map.containsValue(aValue);
  }

  @Override
  public Object get(Object aKey) {
    return map.get(aKey);
  }
  
  public String getString (Object aKey) {
    Object value = get(aKey);
    return ConvertUtils.convert(value);
  }
  
  public String getString (Object aKey, String aDefault) {
    String value = getString(aKey);
    return value != null ? value : aDefault;
  }
  
  @Override
  public Object put(String aKey, Object aValue) {
    return map.put(aKey, aValue);
  }

  @Override
  public Object remove(Object aKey) {
    return map.remove(aKey);
  }

  @Override
  public void putAll(Map<? extends String, ? extends Object> aVariables) {
    map.putAll(aVariables);
  }

  @Override
  public void clear() {
    map.clear();
  }

  @Override
  public Set<String> keySet() {
    return map.keySet();
  }

  @Override
  public Collection<Object> values() {
    return map.values();
  }

  @Override
  public Set<java.util.Map.Entry<String, Object>> entrySet() {
    return map.entrySet();
  }
  
}

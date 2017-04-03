/* 
 * Copyright (C) Creactiviti LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Arik Cohen <arik@creactiviti.com>, Mar 2017
 */
package com.creactiviti.piper.core;

import java.util.Collections;
import java.util.List;

public class ResultPage<T> extends MapObject implements Page<T> {

  private final Class<T> elementType;
  
  public ResultPage(Class<T> aElementType) {
    super(Collections.EMPTY_MAP);
    elementType = aElementType;
  }

  @Override
  public List<T> getContent() {
    List<T> list = getList("content", elementType);
    return list!=null?list:Collections.EMPTY_LIST;
  }

  public void setContent (List<T> aContent) {
    set("content", aContent);
    set("size",aContent.size());
  }
  
  @Override
  public int getSize() {
    return getInteger("size", 0);
  }

  @Override
  public int totalElements() {
    return getInteger("totalElements", 0);
  }

  public void setTotalElements (int aTotalElements) {
    set("totalElements", aTotalElements);
  }
  
  @Override
  public int totalPages() {
    return getInteger("totalPages", 0);
  }
  
  public void setTotalPages(int aTotalPages) {
    set("totalPages", aTotalPages);
  }

  @Override
  public int getNumber() {
    return getInteger("number", -1);
  }
  
  public void setNumber (int aNumber) {
    set("number", aNumber);
  }

}

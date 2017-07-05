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
  public List<T> getItems () {
    List<T> list = getList("items", elementType);
    return list!=null?list:Collections.EMPTY_LIST;
  }

  public void setItems (List<T> aItems) {
    set("items", aItems);
    set("size",aItems.size());
  }
  
  @Override
  public int getSize() {
    return getInteger("size", 0);
  }

  @Override
  public int getTotalItems() {
    return getInteger("totalItems", 0);
  }

  public void setTotalItems (int aTotalElements) {
    set("totalItems", aTotalElements);
  }
  
  @Override
  public int getTotalPages() {
    return getInteger("totalPages", 0);
  }
  
  public void setTotalPages(int aTotalPages) {
    set("totalPages", aTotalPages);
  }

  @Override
  public int getNumber() {
    return getInteger("number", 0);
  }
  
  public void setNumber (int aNumber) {
    set("number", aNumber);
  }

}

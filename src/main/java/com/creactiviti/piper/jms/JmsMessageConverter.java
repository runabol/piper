package com.creactiviti.piper.jms;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Session;

import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.SimpleMessageConverter;
import org.springframework.util.ObjectUtils;

public class JmsMessageConverter extends SimpleMessageConverter {

  private final String dateFormat;
  
  public JmsMessageConverter (String aDateFormat) {
    dateFormat = aDateFormat;
  }
  
  @Override
  protected MapMessage createMessageForMap(Map<?, ?> aMap, Session aSession) throws JMSException {
    MapMessage message = aSession.createMapMessage();
    for (Map.Entry<?, ?> entry : aMap.entrySet()) {
      if (!(entry.getKey() instanceof String)) {
        throw new MessageConversionException("Cannot convert non-String key of type [" +
            ObjectUtils.nullSafeClassName(entry.getKey()) + "] to JMS MapMessage entry");
      }
      if(entry.getValue()!=null&&entry.getValue().getClass().isEnum()) {
        message.setObject((String) entry.getKey(),entry.getValue().toString());
      }
      else if(entry.getValue() instanceof Date) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        message.setObject((String) entry.getKey(),sdf.format(entry.getValue()));
      }
      else {
        message.setObject((String) entry.getKey(), entry.getValue());
      }
    }
    return message;
  }
  
}

package com.creactiviti.piper.jms;

import java.util.Date;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Session;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.SimpleMessageConverter;
import org.springframework.util.ObjectUtils;

public class JmsMessageConverter extends SimpleMessageConverter {

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
        message.setObject((String) entry.getKey(),DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.format(entry.getValue()));
      }
      else {
        message.setObject((String) entry.getKey(), entry.getValue());
      }
    }
    return message;
  }
  
}

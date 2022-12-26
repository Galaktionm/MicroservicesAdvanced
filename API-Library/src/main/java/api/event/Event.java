package api.event;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;

public class Event<K, T> {

	  public enum Type {
	    CREATE,
	    DELETE
	  }

	  private Type eventType;
	  private K key;
	  private T data;
	  private ZonedDateTime eventCreatedAt;

	  public Event() {}

	  public Event(Type eventType, K key, T data) {
	    this.eventType = eventType;
	    this.key = key;
	    this.data = data;
	    this.eventCreatedAt = ZonedDateTime.now();
	  }

	  public Type getEventType() {
	    return eventType;
	  }

	  public K getKey() {
	    return key;
	  }

	  public T getData() {
	    return data;
	  }

	  @JsonSerialize(using = ZonedDateTimeSerializer.class)
	  public ZonedDateTime getEventCreatedAt() {
	    return eventCreatedAt;
	  }
	}

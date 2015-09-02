package com.soagrowers.cqrs.events;

/**
 * Created by Ben on 07/08/2015.
 */
public class ToDoItemCreatedEvent extends Evt {


  private final String itemDescription;

  public ToDoItemCreatedEvent(String eventId, String itemDescription) {
    super(eventId);
    this.itemDescription = itemDescription;
  }

  public String getItemDescription() {
    return itemDescription;
  }

  @Override
  public String toString() {
    return "ToDoItemCreatedEvent{" +
            "eventId='" + super.getEventId() + '\'' +
            "itemDescription='" + itemDescription + '\'' +
            '}';
  }
}

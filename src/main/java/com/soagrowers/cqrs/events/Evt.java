package com.soagrowers.cqrs.events;

import java.io.Serializable;

/**
 * Created by ben on 28/08/15.
 */
public class Evt implements Serializable{

    private final String eventId;

    public Evt(String eventId) {
        this.eventId = eventId;
    }

    public String getEventId() {
        return eventId;
    }
}



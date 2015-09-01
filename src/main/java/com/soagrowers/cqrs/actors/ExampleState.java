package com.soagrowers.cqrs.actors;

import com.soagrowers.cqrs.events.Evt;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ben on 28/08/15.
 */
public class ExampleState implements Serializable {

    private final ArrayList<String> events;

    public ExampleState() {
        this(new ArrayList<String>());
    }

    public ExampleState(ArrayList<String> events) {
        this.events = events;
    }

    public ExampleState copy() {
        return new ExampleState(new ArrayList<String>(events));
    }

    public void update(Evt evt) {
        events.add(evt.getData());
    }

    public int size() {
        return events.size();
    }

    @Override
    public String toString() {
        return events.toString();
    }

}

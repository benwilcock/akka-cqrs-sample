package com.soagrowers.cqrs.actors;

import com.soagrowers.cqrs.events.Evt;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ben on 28/08/15.
 */
public class ToDoItemState implements Serializable {

    private final ArrayList<Evt> events;

    public ToDoItemState() {
        this(new ArrayList<Evt>());
    }

    public ToDoItemState(ArrayList<Evt> events) {
        this.events = events;
    }

    public ToDoItemState copy() {
        return new ToDoItemState(new ArrayList<Evt>(events));
    }

    public void add(Evt evt) {
        events.add(evt);
    }

    public int size() {
        return events.size();
    }

    @Override
    public String toString() {
        return events.toString();
    }

}

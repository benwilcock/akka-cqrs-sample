package com.soagrowers.cqrs.actors;

import com.soagrowers.cqrs.events.Evt;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ben on 28/08/15.
 */
public class Events implements Serializable {

    private final ArrayList<Evt> events;

    public Events() {
        this(new ArrayList<Evt>());
    }

    public Events(ArrayList<Evt> events) {
        this.events = events;
    }

    public Events copy() {
        return new Events(new ArrayList<Evt>(events));
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

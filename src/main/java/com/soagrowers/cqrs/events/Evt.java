package com.soagrowers.cqrs.events;

import java.io.Serializable;

/**
 * Created by ben on 28/08/15.
 */
public class Evt implements Serializable{

    private final String data;

    public Evt(String id) {
        this.data = id;
    }

    public String getData() {
        return data;
    }
}



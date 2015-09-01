package com.soagrowers.cqrs.commands;

import java.io.Serializable;

/**
 * Created by ben on 28/08/15.
 */
public class Cmd implements Serializable{

    private final String data;

    public Cmd(String id) {
        this.data = id;
    }

    public String getData() {
        return data;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.bsc.compss.sample.commands;

import es.bsc.compss.sample.commands.Main.Types;
import es.bsc.compss.runtimelogparser.events.LogEvent;


/**
 *
 * @author flordan
 */
public class MainAccessToObject extends LogEvent<Types> {

    private final String hash;

    public MainAccessToObject(LogEvent event) {
        super(event);
        hash = event.getMessage().substring(48);
    }

    @Override
    public Types getType() {
        return Types.MAIN;
    }

    public String getObjectHash() {
        return hash;
    }

}

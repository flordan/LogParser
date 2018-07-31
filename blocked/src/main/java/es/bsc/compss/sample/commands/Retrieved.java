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
public class Retrieved extends LogEvent<Main.Types> {

    private final String renaming;

    public Retrieved(LogEvent event) {
        super(event);
        String[] message = event.getMessage().split(" ");
        renaming = message[6].split("_")[0];
    }

    public String getRenaming() {
        return renaming;
    }

    @Override
    public Main.Types getType() {
        return Types.RETRIEVED;
    }

}

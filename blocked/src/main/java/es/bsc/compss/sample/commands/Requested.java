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
public class Requested extends LogEvent<Types> {

    private final String dataId;
    private final String renaming;

    public Requested(LogEvent event) {
        super(event);
        String[] message = event.getMessage().split(" ");
        dataId = message[3];
        renaming = message[6].split("_")[0];
    }

    public String getDataId() {
        return dataId;
    }

    public String getRenaming() {
        return renaming;
    }

    @Override
    public Types getType() {
        return Types.REQUESTED;
    }

}

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
public class NewTaskParameter extends LogEvent<Types> {

    String dataId;

    public NewTaskParameter(LogEvent event) {
        super(event);
        String[] message = event.getMessage().split(" ");
        dataId = message[7];
    }

    public String getDataId() {
        return dataId;
    }

    @Override
    public Types getType() {
        return Types.NEW_TASK_PARAM;
    }

}

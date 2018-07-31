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
public class NewDataWriter extends LogEvent<Types> {

    String dataId;
    String taskId;

    public NewDataWriter(LogEvent event) {
        super(event);
        String[] message = event.getMessage().split(" ");
        dataId = message[4];
        taskId = message[7];
    }

    public String getTaskId() {
        return taskId;
    }

    public String getDataId() {
        return dataId;
    }

    @Override
    public Types getType() {
        return Types.NEW_DATA_WRITER;
    }

}

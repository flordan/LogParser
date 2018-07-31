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
public class NewTask extends LogEvent<Types> {

    String taskId;

    public NewTask(LogEvent event) {
        super(event);
        String[] message = event.getMessage().split(" ");
        taskId = message[5];
    }

    public String getTaskId() {
        return taskId;
    }

    @Override
    public Types getType() {
        return Types.NEW_TASK;
    }

    
    
}

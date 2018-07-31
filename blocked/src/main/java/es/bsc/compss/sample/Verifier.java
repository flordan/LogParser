/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.bsc.compss.sample;

import es.bsc.compss.sample.types.MainAccessInformation;
import es.bsc.compss.sample.commands.NewDataWriter;
import es.bsc.compss.sample.commands.NewTaskParameter;
import es.bsc.compss.sample.commands.EndTask;
import es.bsc.compss.sample.commands.NewTask;
import es.bsc.compss.sample.commands.MainAccessToObject;
import es.bsc.compss.sample.commands.NewDataAccess;
import es.bsc.compss.sample.commands.Waiting;
import es.bsc.compss.runtimelogparser.events.LogEvent;
import es.bsc.compss.runtimelogparser.utils.Validator;
import es.bsc.compss.sample.commands.Main.Types;
import es.bsc.compss.sample.commands.Requested;
import es.bsc.compss.sample.commands.Retrieved;
import es.bsc.compss.sample.types.DataInformation;
import es.bsc.compss.sample.types.TaskInformation;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author flordan
 */
public class Verifier implements Validator<Types> {

    public final Map<String, DataInformation> dataInfo = new HashMap<>();
    public final Map<String, TaskInformation> taskInfo = new HashMap<>();
    public final Map<String, MainAccessInformation> mainAccessesInfo = new HashMap<>();


    private enum AccessOrigin {
        MAIN, TASK
    }

    public MainAccessToObject lastMain;
    public NewTask lastNewTask;
    public NewTaskParameter lastTaskParam;
    public DataAccess lastAccess;
    public AccessOrigin lastAccessOrigin;

    public void newTask(NewTask task) {
        lastNewTask = task;
        TaskInformation ti = new TaskInformation(task);
        taskInfo.put(task.getTaskId(), ti);
    }

    private void endTask(EndTask endTask) {
        TaskInformation ti = taskInfo.get(endTask.getTaskId());
        ti.finished(endTask);
    }

    public void newParam(NewTaskParameter param) {
        String dataId = param.getDataId();

        DataInformation di = dataInfo.get(dataId);
        if (di == null) {
            di = new DataInformation(dataId);
            dataInfo.put(dataId, di);
        }

        lastTaskParam = param;
        lastAccessOrigin = AccessOrigin.TASK;
    }

    private void newDataAccess(NewDataAccess newDataAccess) {
        DataAccess access = newDataAccess.getAccesses();
        lastAccess = access;
        switch (lastAccessOrigin) {
            case MAIN:
                mainAccess(access);
                break;
            case TASK:
                taskParamAccess(access);
                break;
        }
    }

    private void mainAccess(DataAccess access) {
        String dataId = lastMain.getObjectHash();
        DataInformation di = dataInfo.get(dataId);
        di.accessedByMain(access);
    }

    private void taskParamAccess(DataAccess access) {
        String dataId = lastTaskParam.getDataId();
        String taskId = lastNewTask.getTaskId();
        TaskInformation ti = taskInfo.get(taskId);
        DataInformation di = dataInfo.get(dataId);
        ti.accesses(dataId, access);
        di.accessedByTask(ti, access);
    }

    public void writingData(NewDataWriter writter) {

    }

    public void newMainAccess(MainAccessToObject main) {
        lastMain = main;
        lastAccessOrigin = AccessOrigin.MAIN;
    }

    public void newWaiting(Waiting main) {
        String dataId = lastMain.getObjectHash();
        DataInformation di = dataInfo.get(dataId);
        TaskInformation dep = di.getTaskDependency(lastAccess);
        MainAccessInformation mai = new MainAccessInformation(dataId, lastAccess, dep);
        mainAccessesInfo.put(lastAccess.getLastVersionId(), mai);
        if (dep == null || dep.isFinished()) {
            mai.valueGenerated(main);
        } else {
            dep.registerFinishListener(mai);
        }
    }

    public void requestedMain(Requested request) {
        String rename = request.getRenaming();
        MainAccessInformation mai = mainAccessesInfo.get(rename);
        mai.ready(request);
    }

    private void retrievedMain(Retrieved retrieved) {
        String rename = retrieved.getRenaming();
        MainAccessInformation mai = mainAccessesInfo.get(rename);
        mai.retrieved(retrieved);
    }

    @Override
    public void process(LogEvent<Types> event) {
        switch (event.getType()) {
            case NEW_TASK:
                newTask((NewTask) event);
                break;
            case END_TASK:
                endTask((EndTask) event);
                break;
            case MAIN:
                newMainAccess((MainAccessToObject) event);
                break;
            case WAITING:
                newWaiting((Waiting) event);
                break;
            case REQUESTED:
                requestedMain((Requested) event);
                break;
            case RETRIEVED:
                retrievedMain((Retrieved) event);
                break;
            case NEW_TASK_PARAM:
                newParam((NewTaskParameter) event);
                break;
            case NEW_DATA_ACCESS:
                newDataAccess((NewDataAccess) event);
                break;
            case NEW_DATA_WRITER:
                writingData((NewDataWriter) event);
                break;
            default:
        }
    }

    public void print() {
        System.out.println("Detected Tasks:");
        for (TaskInformation ti : taskInfo.values()) {
            if (!ti.isFinished()) {
                System.out.println(ti);
            }
        }

        System.out.println("Detected Accesses:");
        for (MainAccessInformation mai : this.mainAccessesInfo.values()) {
            if (!mai.isRetrieved()) {
                System.out.println(mai);
            }
        }
        /*
        System.out.println("Detected Data:");
        for (DataInformation di : dataInfo.values()) {
            System.out.println(di);
        }

         */
    }

}

package es.bsc.compss.sample.types;

import es.bsc.compss.sample.DataAccess;
import es.bsc.compss.sample.commands.EndTask;
import es.bsc.compss.sample.commands.NewTask;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;


public class TaskInformation {

    private final String id;
    private final NewTask creationEvent;
    private EndTask endEvent;

    private LinkedList<MainAccessInformation> completionListeners = new LinkedList<>();

    private final List<TaskDataAccess> accesses = new LinkedList<>();

    public TaskInformation(NewTask task) {
        this.id = task.getTaskId();
        creationEvent = task;
    }

    public void accesses(String dataId, DataAccess access) {
        TaskDataAccess tda = new TaskDataAccess(dataId, access);
        accesses.add(tda);
    }

    public String getId() {
        return this.id;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\tTask ").append(id).append(endEvent != null ? "(Finished)" : "").append(":\n");
        sb.append("\t\tAccesses\n");
        for (TaskDataAccess di : accesses) {
            sb.append("\t\t\t- ").append(di.dataId).append("-").append(di.access).append("\n");
        }
        return sb.toString();
    }

    public boolean isFinished() {
        return endEvent != null;
    }

    public void finished(EndTask endTask) {
        endEvent = endTask;
        for (MainAccessInformation listener : completionListeners) {
            listener.valueGenerated(endTask);
        }
    }

    public void registerFinishListener(MainAccessInformation listener) {
        completionListeners.add(listener);
    }


    private class TaskDataAccess {

        String dataId;
        DataAccess access;

        public TaskDataAccess(String dataId, DataAccess access) {
            this.dataId = dataId;
            this.access = access;
        }

    }
}

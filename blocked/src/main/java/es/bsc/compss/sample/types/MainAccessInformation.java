package es.bsc.compss.sample.types;

import es.bsc.compss.runtimelogparser.events.LogEvent;
import es.bsc.compss.sample.DataAccess;


public class MainAccessInformation {

    public final String dataId;
    public final DataAccess access;
    private final TaskInformation generator;
    public LogEvent waiting;
    public LogEvent generated = null;
    public LogEvent retrieved = null;

    public MainAccessInformation(String dataId, DataAccess access, TaskInformation dep) {
        this.dataId = dataId;
        this.access = access;
        waiting = null;
        generator = dep;

    }

    public void valueGenerated(LogEvent generateEvent) {
        generated = generateEvent;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\tMain accesses object ").append(dataId).append(": (").append(access).append(")\n");
        if (waiting == null) {
            sb.append("\t\tStill waiting for the generator ")
                    .append(generator == null
                            ? ""
                            : "(task " + generator.getId() + (generator.isFinished()
                                    ? "- Finished"
                                    : "")
                            + ") "
                    ).append("to create the value\n");
        } else if (retrieved == null) {
            sb.append("\t\tFetching the value. Last seen event: \n");
            sb.append(waiting.getTime() + " " + waiting.getLogger() + "] " + waiting.getMethod() + " " + waiting.getMessage());
        } else {
            sb.append("\t\t Already available on the master");
        }
        return sb.toString();
    }

    public void ready(LogEvent readyNotification) {
        waiting = readyNotification;
    }

    public void retrieved(LogEvent retrievalEvent) {
        retrieved = retrievalEvent;
    }

    public boolean isRetrieved() {
        return retrieved != null;
    }

}

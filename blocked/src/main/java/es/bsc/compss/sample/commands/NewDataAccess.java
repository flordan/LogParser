package es.bsc.compss.sample.commands;

import es.bsc.compss.sample.commands.Main.Types;
import es.bsc.compss.runtimelogparser.events.LogEvent;
import es.bsc.compss.sample.DataAccess;
import es.bsc.compss.sample.DataAccess.ReadAccess;
import es.bsc.compss.sample.DataAccess.ReadWriteAccess;
import es.bsc.compss.sample.DataAccess.WriteAccess;


/**
 *
 * @author flordan
 */
public class NewDataAccess extends LogEvent<Types> {

    public NewDataAccess(LogEvent event) {
        super(event);
    }

    public DataAccess getAccesses() {
        String[] lines = this.getMessage().split("\n");
        String type = lines[1].split(":")[1].substring(1);
        DataAccess access;
        switch (type) {
            case "R":
                access = new ReadAccess(lines[2].split(":")[1].substring(1));
                break;
            case "RW":
                String read = lines[2].split(":")[1].substring(1);
                String write = lines[3].split(":")[1].substring(1);
                access = new ReadWriteAccess(read, write);
                break;
            case "W":
                access = new WriteAccess(lines[2].split(":")[1].substring(1));
                break;
            default:
                access = null;
        }
        return access;
    }

    @Override
    public Types getType() {
        return Types.NEW_DATA_ACCESS;
    }

}

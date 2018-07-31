/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.bsc.compss.sample.types;

import es.bsc.compss.sample.DataAccess;
import java.util.Iterator;
import java.util.LinkedList;


public class DataInformation {

    private final String oId;
    private LinkedList<Access> accesses = new LinkedList<>();

    public DataInformation(String oId) {
        this.oId = oId;
    }

    public void accessedByTask(TaskInformation ti, DataAccess access) {
        accesses.add(new TaskAccess(ti, access));
    }

    public void accessedByMain(DataAccess access) {
        accesses.add(new Access(access));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\tData ").append(oId).append(":\n");
        sb.append("\t\tAccesses\n");
        for (Access access : accesses) {
            sb.append("\t\t\t- ").append(access).append("\n");
        }
        return sb.toString();
    }

    public TaskInformation getTaskDependency(DataAccess lastAccess) {
        Iterator<Access> accessItr = accesses.descendingIterator();
        Access access = null;
        while (accessItr.hasNext()) {
            access = accessItr.next();
            if (lastAccess.dependendsOn(access.getAccess())) {
                if (access instanceof TaskAccess) {
                    return ((TaskAccess) access).ti;
                } else {
                    return null;
                }
            }
        }
        return null;
    }


    private class Access {

        private final DataAccess access;

        public Access(DataAccess access) {
            this.access = access;
        }

        public DataAccess getAccess() {
            return access;
        }

        @Override
        public String toString() {
            return "main: " + access.toString();
        }
    }


    private class TaskAccess extends Access {

        private final TaskInformation ti;

        public TaskAccess(TaskInformation ti, DataAccess access) {
            super(access);
            this.ti = ti;

        }

        @Override
        public String toString() {
            return "task " + ti.getId() + ": " + getAccess().toString();
        }
    }
}

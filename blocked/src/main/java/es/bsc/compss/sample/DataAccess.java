/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.bsc.compss.sample;


/**
 *
 * @author flordan
 */
public interface DataAccess {

    public String getLastVersionId();


    public enum Type {
        R, W, RW
    }

    public Type getType();

    public boolean dependendsOn(DataAccess access);


    public static class ReadAccess implements DataAccess {

        private final String readVersion;

        public ReadAccess(String readVersion) {
            this.readVersion = readVersion;
        }

        @Override
        public Type getType() {
            return Type.R;
        }

        @Override
        public String toString() {
            return "reads " + readVersion;
        }

        @Override
        public boolean dependendsOn(DataAccess access) {
            switch (access.getType()) {
                case R:
                    return false;
                case RW:
                    return ((ReadWriteAccess) access).writeVersion.equals(readVersion);
                case W:
                    return ((WriteAccess) access).writeVersion.equals(readVersion);
                default:
                    return false;
            }
        }

        @Override
        public String getLastVersionId() {
            return readVersion;
        }
    }


    public static class ReadWriteAccess implements DataAccess {

        private final String readVersion;
        private final String writeVersion;

        public ReadWriteAccess(String readVersion, String writeVersion) {
            this.readVersion = readVersion;
            this.writeVersion = writeVersion;
        }

        @Override
        public Type getType() {
            return Type.RW;
        }

        @Override
        public String toString() {
            return "updates " + readVersion + " to " + writeVersion;
        }

        @Override
        public boolean dependendsOn(DataAccess access) {
            switch (access.getType()) {
                case R:
                    return false;
                case RW:
                    return ((ReadWriteAccess) access).writeVersion.equals(readVersion);
                case W:
                    return ((WriteAccess) access).writeVersion.equals(readVersion);
                default:
                    return false;
            }
        }

        @Override
        public String getLastVersionId() {
            return writeVersion;
        }
    }


    public static class WriteAccess implements DataAccess {

        private final String writeVersion;

        public WriteAccess(String writeVersion) {
            this.writeVersion = writeVersion;
        }

        @Override
        public Type getType() {
            return Type.W;
        }

        @Override
        public String toString() {
            return "creates " + writeVersion;
        }

        @Override
        public boolean dependendsOn(DataAccess access) {
            return false;
        }

        @Override
        public String getLastVersionId() {
            return writeVersion;
        }
    }
}

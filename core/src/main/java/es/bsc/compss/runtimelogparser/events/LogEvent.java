/**
 *
 * Copyright 2013-2018 Barcelona Supercomputing Center (www.bsc.es) All rights
 * reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package es.bsc.compss.runtimelogparser.events;

import es.bsc.compss.runtimelogparser.exception.NoHeaderException;
import es.bsc.compss.runtimelogparser.utils.Validator;
import java.util.regex.Pattern;


/**
 *
 * @author flordan
 */
public abstract class LogEvent<T> {

    private String time;
    private String logger;
    private String method;
    private String message;

    private LogEvent(String time, String logger, String method, String message) {
        this.time = time;
        this.logger = logger;
        this.method = method;
        this.message = message;
    }

    public LogEvent(LogEvent event) {
        this.time = event.time;
        this.logger = event.logger;
        this.method = event.method;
        this.message = event.message;
    }

    public static LogEvent parseEvent(String line) throws NoHeaderException {
        String date;
        String logger;
        String method;
        String message;

        if (line.startsWith(("[("))) {
            String[] parts = line.split(Pattern.quote(")"));
            date = parts[1].substring(1, parts[1].length() - 1);
            logger = parts[2].substring(0, 20);
            logger = logger.replaceAll("\\s+", "");
            StringBuilder sb = new StringBuilder(parts[2]);
            for (int i = 3; i < parts.length; i++) {
                sb.append(")");
                sb.append(parts[i]);
            }
            line = sb.toString();
            parts = line.split(Pattern.quote("@"));
            method = parts[1].substring(0, 17);
            method = method.replaceAll("\\s+", "");
            sb = new StringBuilder(parts[1].substring(20));
            for (int i = 2; i < parts.length; i++) {
                sb.append("@");
                sb.append(parts[i]);
            }
            message = sb.toString();
        } else {
            throw new NoHeaderException();
        }

        return new RawLogEvent(date, logger, method, message);
    }

    public String getTime() {
        return time;
    }

    public String getLogger() {
        return logger;
    }

    public String getMethod() {
        return method;
    }

    public String getMessage() {
        return message;
    }

    public abstract T getType();

    public void process(Validator<T> v) {
        v.process(this);
    }

    public void append(String line) {
        StringBuilder sb = new StringBuilder();
        sb.append(message).append("\n").append(line);
        message = sb.toString();
    }

    @Override
    public String toString() {
        return logger + " " + method + ": " + message;
    }


    private static class RawLogEvent extends LogEvent {

        public RawLogEvent(String date, String logger, String method, String message) {
            super(date, logger, method, message);
        }

        @Override
        public Object getType() {
            return null;
        }
    }
}

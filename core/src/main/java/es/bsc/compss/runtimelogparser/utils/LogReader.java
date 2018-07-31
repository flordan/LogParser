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
package es.bsc.compss.runtimelogparser.utils;

import es.bsc.compss.runtimelogparser.classifiers.Classifier;
import es.bsc.compss.runtimelogparser.exception.NoHeaderException;
import es.bsc.compss.runtimelogparser.events.LogEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;


/**
 *
 * @author flordan
 */
public class LogReader {

    public static <T> List<LogEvent<T>> parseEvents(String logFile, Classifier c) throws Exception {

        File file = new File(logFile);

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        LogEvent log = null;
        List<LogEvent<T>> events = new LinkedList<>();

        while ((st = br.readLine()) != null) {
            try {
                LogEvent newLog = LogEvent.parseEvent(st);
                if (log != null) {
                    List<LogEvent<T>> classified = c.classify(log);
                    events.addAll(classified);
                }
                log = newLog;
            } catch (NoHeaderException nhe) {
                if (log != null) {
                    log.append(st);
                }
            }
        }
        if (log != null) {
            List<LogEvent<T>> classified = c.classify(log);
            events.addAll(classified);
        }
        return events;
    }

}

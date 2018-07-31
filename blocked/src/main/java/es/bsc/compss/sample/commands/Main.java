/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.bsc.compss.sample.commands;

import es.bsc.compss.runtimelogparser.classifiers.Classifier;
import es.bsc.compss.runtimelogparser.classifiers.Classifier.BasicClassifier;
import es.bsc.compss.runtimelogparser.conditions.Condition;
import es.bsc.compss.runtimelogparser.conditions.Condition.Field;
import es.bsc.compss.runtimelogparser.conditions.EqualCondition;
import es.bsc.compss.runtimelogparser.conditions.StartsWithCondition;
import es.bsc.compss.runtimelogparser.events.LogEvent;
import es.bsc.compss.runtimelogparser.utils.LogReader;
import es.bsc.compss.sample.Verifier;
import java.util.List;


/**
 *
 * @author flordan
 */
public class Main {

    public Main() {
    }


    public static enum Types {
        NEW_TASK,
        MAIN,
        RETRIEVED,
        WAITING,
        REQUESTED,
        NEW_TASK_PARAM, 
        NEW_DATA_ACCESS,
        NEW_DATA_WRITER,
        END_TASK
    }

    public static void main(String[] args) throws Exception {

        Condition c = new EqualCondition(Field.METHOD, "inAcessToObject");
        c = c.compose(new EqualCondition(Field.LOGGER, "TaskProcessor"));

        Condition mainAccess = c.compose(new StartsWithCondition(Field.MESSAGE, "Requesting main access to object with hash code"));
        Condition retrieved = c.compose(new StartsWithCondition(Field.MESSAGE, "Object retrieved"));
        Condition waiting = c.compose(new StartsWithCondition(Field.MESSAGE, "Waiting for"));
        Condition request = c.compose(new StartsWithCondition(Field.MESSAGE, "Request object"));
        
        
        Classifier classifier = new BasicClassifier<Types>(mainAccess) {
            @Override
            public LogEvent<Types> createEvent(LogEvent e) {
                return new MainAccessToObject(e);
            }
        };

        classifier = classifier.compose(classifier, new BasicClassifier<Types>(retrieved) {
            @Override
            public LogEvent<Types> createEvent(LogEvent e) {
                return new Retrieved(e);
            }
        });
        classifier = classifier.compose(classifier, new BasicClassifier<Types>(waiting) {
            @Override
            public LogEvent<Types> createEvent(LogEvent e) {
                return new Waiting(e);
            }
        });
        classifier = classifier.compose(classifier, new BasicClassifier<Types>(request) {
            @Override
            public LogEvent<Types> createEvent(LogEvent e) {
                return new Requested(e);
            }
        });


        Condition taskAnalyser = new EqualCondition(Field.LOGGER, "TaskAnalyser");
        Condition dataInfoProvider = new EqualCondition(Field.LOGGER, "DataInfoProvider");

        Condition processTask = taskAnalyser.compose(new EqualCondition(Field.METHOD, "processTask"));
        Condition newTask = processTask.compose(new StartsWithCondition(Field.MESSAGE, "New method task"));
        Condition parameter = processTask.compose(new StartsWithCondition(Field.MESSAGE, "* Parameter : ObjectParameter"));
        Condition parameterAccess = dataInfoProvider.compose(new EqualCondition(Field.METHOD, "willAccess"));

        Condition registerOutputValues = taskAnalyser.compose(new EqualCondition(Field.METHOD, "terOutputValues"));
        Condition newWriter = registerOutputValues.compose(new StartsWithCondition(Field.MESSAGE, "New writer for datum"));

        Condition endTask = taskAnalyser.compose(new EqualCondition(Field.METHOD, "endTask"));
        Condition endTaskMethod = endTask.compose(new StartsWithCondition(Field.MESSAGE, "Ending task"));

        classifier = classifier.compose(classifier, new BasicClassifier<Types>(newTask) {
            @Override
            public LogEvent<Types> createEvent(LogEvent e) {
                return new NewTask(e);
            }
        });
        classifier = classifier.compose(classifier, new BasicClassifier<Types>(parameter) {
            @Override
            public LogEvent<Types> createEvent(LogEvent e) {
                return new NewTaskParameter(e);
            }
        });
        classifier = classifier.compose(classifier, new BasicClassifier<Types>(parameterAccess) {
            @Override
            public LogEvent<Types> createEvent(LogEvent e) {
                return new NewDataAccess(e);
            }
        });
        classifier = classifier.compose(classifier, new BasicClassifier<Types>(newWriter) {
            @Override
            public LogEvent<Types> createEvent(LogEvent e) {
                return new NewDataWriter(e);
            }
        });
        classifier = classifier.compose(classifier, new BasicClassifier<Types>(endTaskMethod) {
            @Override
            public LogEvent<Types> createEvent(LogEvent e) {
                return new EndTask(e);
            }
        });

        Verifier v = new Verifier();
        List<LogEvent<Types>> events = LogReader.parseEvents("/home/flordan/Downloads/runtime (2).log", classifier);
        for (LogEvent<Types> event : events) {
            v.process(event);
        }
        v.print();
    }

}

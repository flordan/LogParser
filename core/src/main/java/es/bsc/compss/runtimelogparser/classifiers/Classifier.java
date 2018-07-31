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
package es.bsc.compss.runtimelogparser.classifiers;

import es.bsc.compss.runtimelogparser.conditions.Condition;
import es.bsc.compss.runtimelogparser.events.LogEvent;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author flordan
 */
public abstract class Classifier<T> {

	public final <T> Classifier<T> compose(Classifier<T> c1, Classifier<T> c2) {
		return new MultiClassifier(c1, c2);
	}

	public abstract List<LogEvent<T>> classify(LogEvent e);

	private static final class MultiClassifier<T> extends Classifier<T> {

		private final Classifier<T> a;
		private final Classifier<T> b;

		private MultiClassifier(Classifier<T> a, Classifier<T> b) {
			this.a = a;
			this.b = b;
		}

		@Override
		public List<LogEvent<T>> classify(LogEvent e) {
			List<LogEvent<T>> events = new LinkedList<>();
			events.addAll(a.classify(e));
			events.addAll(b.classify(e));
			return events;
		}

	}

	public static abstract class BasicClassifier<T> extends Classifier<T> {

		private final Condition c;

		public BasicClassifier(Condition c) {
			this.c = c;
		}

		@Override
		public List<LogEvent<T>> classify(LogEvent e) {
			List<LogEvent<T>> events = new LinkedList<>();
			if (c.evaluate(e)) {
				events.add(createEvent(e));
			}
			return events;
		}

		public abstract LogEvent<T> createEvent(LogEvent e);

	}
}

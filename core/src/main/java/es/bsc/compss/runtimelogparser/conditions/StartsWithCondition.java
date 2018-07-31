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
package es.bsc.compss.runtimelogparser.conditions;

import es.bsc.compss.runtimelogparser.events.LogEvent;

/**
 *
 * @author flordan
 */
public class StartsWithCondition extends Condition {

	private final Condition.Field f;
	private final String value;

	public StartsWithCondition(Condition.Field f, String value) {
		this.f = f;
		this.value = value;
	}

	@Override
	public boolean evaluate(LogEvent e) {
		switch (f) {
			case LOGGER:
				return e.getLogger().startsWith(value);
			case METHOD:
				return e.getMethod().startsWith(value);
			case MESSAGE:
				return e.getMessage().startsWith(value);
			default:
				return true;
		}
	}
}

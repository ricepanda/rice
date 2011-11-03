/**
 * Copyright 2005-2011 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.core.api.criteria;

import org.kuali.rice.core.api.mo.AbstractDataTransferObject;

/**
 * An abstract class from which all {@link Predicate} instances should extend.
 * This class merely defines standard implementations for hashCode, equals,
 * and toString.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
abstract class AbstractPredicate extends AbstractDataTransferObject implements Predicate {

	private static final long serialVersionUID = 7035792141358213138L;

    @Override
    public abstract String toString();
	
}

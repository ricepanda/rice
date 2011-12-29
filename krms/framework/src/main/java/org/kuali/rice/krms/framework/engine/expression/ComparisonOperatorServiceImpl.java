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
package org.kuali.rice.krms.framework.engine.expression;

import org.kuali.rice.krms.api.engine.expression.ComparisonOperatorService;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ComparisonOperatorServiceImpl implements ComparisonOperatorService {

    private List<EngineComparatorExtension> operators = new LinkedList<EngineComparatorExtension>();

    @Override
    public List<EngineComparatorExtension> getOperators() {
        return operators;
    }

    @Override
    public void setOperators(List<EngineComparatorExtension> operators) {
        this.operators = operators;
    }

    @Override
    public EngineComparatorExtension comparatorExtension(Object lhs, Object rhs) {
        EngineComparatorExtension extension;
        Iterator<EngineComparatorExtension> opIter = operators.iterator();
        while (opIter.hasNext()) {
            extension = opIter.next();
            if (extension.canCompare(lhs, rhs)) {
                return extension;
            }
        }
        return null;
    }

    @Override
    public boolean canCompare(Object lhs, Object rhs) {
        return comparatorExtension(lhs, rhs) != null;
    }

}

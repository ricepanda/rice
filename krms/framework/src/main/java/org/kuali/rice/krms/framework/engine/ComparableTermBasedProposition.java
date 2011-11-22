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
package org.kuali.rice.krms.framework.engine;

import java.util.Collections;
import java.util.List;

import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.api.engine.ResultEvent;
import org.kuali.rice.krms.api.engine.Term;
import org.kuali.rice.krms.api.engine.TermResolutionException;
import org.kuali.rice.krms.framework.engine.expression.ComparisonOperator;
import org.kuali.rice.krms.framework.engine.result.BasicResult;

public class ComparableTermBasedProposition<T> implements Proposition {
	private static final ResultLogger LOG = ResultLogger.getInstance();

	private final ComparisonOperator operator;
	private final Term term;
	private final T expectedValue;


	public ComparableTermBasedProposition(ComparisonOperator operator, Term term, T expectedValue) {
		this.operator = operator;
		this.term = term;
		this.expectedValue = expectedValue;
	}

	/**
	 * @see org.kuali.rice.krms.framework.engine.Proposition#evaluate(org.kuali.rice.krms.api.engine.ExecutionEnvironment)
	 * @throws TermResolutionException if there is a problem resolving the {@link Term}
	 */
	@Override
	public PropositionResult evaluate(ExecutionEnvironment environment) {
		Comparable<T> termValue;

		termValue = environment.resolveTerm(term, this);

		boolean result = compare(termValue);

		if (LOG.isEnabled(environment)){
			LOG.logResult(new BasicResult(ResultEvent.PropositionEvaluated, this, environment, result));
		}
		return new PropositionResult(result);
	}

	/**
	 * This method does the actual comparison of the term value w/ the expected value
	 *
	 * @param termValue
	 * @return the boolean result of the comparison
	 */
	protected boolean compare(Comparable<T> termValue) {
		boolean result = Boolean.valueOf(operator.compare(termValue, getExpectedValue()));
		return result;
	}
	

	@Override
	public List<Proposition> getChildren() {
	    return Collections.emptyList();
	}
	
	@Override
	public boolean isCompound() {
	    return false;
	}

	/**
	 * @return the expectedValue
	 */
	protected T getExpectedValue() {
		return this.expectedValue;
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(term.toString());
		sb.append(" "+operator.toString());
		sb.append(" "+expectedValue.toString());
		return sb.toString();
	}
}

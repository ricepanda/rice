/*
 * Copyright 2011 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.krad.datadictionary.validation.processor;

import org.kuali.rice.core.api.uif.DataType;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.krad.datadictionary.exception.AttributeValidationException;
import org.kuali.rice.krad.datadictionary.validation.AttributeValueReader;
import org.kuali.rice.krad.datadictionary.validation.ValidationUtils;
import org.kuali.rice.krad.datadictionary.validation.ValidationUtils.Result;
import org.kuali.rice.krad.datadictionary.validation.capability.RangeConstrainable;
import org.kuali.rice.krad.datadictionary.validation.constraint.Constraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.RangeConstraint;
import org.kuali.rice.krad.datadictionary.validation.result.ConstraintValidationResult;
import org.kuali.rice.krad.datadictionary.validation.result.DictionaryValidationResult;
import org.kuali.rice.krad.datadictionary.validation.result.ProcessorResult;

import java.math.BigDecimal;
import java.util.Date;

/**
 * This class enforces range constraints - that is, constraints that keep a number or a date within a specific range. An attribute 
 * that is {@link RangeConstrainable} will expose a minimum and maximum value, and these will be validated against the passed
 * value in the code below. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org) 
 */
public class RangeConstraintProcessor extends MandatoryElementConstraintProcessor<RangeConstraint> {

	private static final String CONSTRAINT_NAME = "range constraint";

	/**
	 * @see org.kuali.rice.krad.datadictionary.validation.processor.ConstraintProcessor#process(DictionaryValidationResult, Object, org.kuali.rice.krad.datadictionary.validation.capability.Validatable, org.kuali.rice.krad.datadictionary.validation.AttributeValueReader)
	 */
	@Override
	public ProcessorResult process(DictionaryValidationResult result, Object value, RangeConstraint constraint, AttributeValueReader attributeValueReader) throws AttributeValidationException {
		
		// Since any given definition that is range constrained only expressed a single min and max, it means that there is only a single constraint to impose
		return new ProcessorResult(processSingleRangeConstraint(result, value, constraint, attributeValueReader));
	}
	
	@Override 
	public String getName() {
		return CONSTRAINT_NAME;
	}

	/**
	 * @see org.kuali.rice.krad.datadictionary.validation.processor.ConstraintProcessor#getConstraintType()
	 */
	@Override
	public Class<? extends Constraint> getConstraintType() {
		return RangeConstraint.class;
	}
	
	protected ConstraintValidationResult processSingleRangeConstraint(DictionaryValidationResult result, Object value, RangeConstraint constraint, AttributeValueReader attributeValueReader) throws AttributeValidationException {
		// Can't process any range constraints on null values
		if (ValidationUtils.isNullOrEmpty(value))
			return result.addSkipped(attributeValueReader, CONSTRAINT_NAME);
		
	
		// This is necessary because sometimes we'll be getting a string, for example, that represents a date. 
		DataType dataType = constraint.getDataType();
		Object typedValue = value;

		if (dataType != null) {
			typedValue = ValidationUtils.convertToDataType(value, dataType, dateTimeService);
		}	

		// TODO: decide if there is any reason why the following would be insufficient - i.e. if something numeric could still be cast to String at this point
		if (typedValue instanceof Date)
			return validateRange(result, (Date)typedValue, constraint, attributeValueReader);
		else if (typedValue instanceof Number)
			return validateRange(result, (Number)typedValue, constraint, attributeValueReader);
		
		return result.addSkipped(attributeValueReader, CONSTRAINT_NAME);
	}
	
	protected ConstraintValidationResult validateRange(DictionaryValidationResult result, Date value, RangeConstraint constraint, AttributeValueReader attributeValueReader) throws IllegalArgumentException {	

		Date date = value != null ? ValidationUtils.getDate(value, dateTimeService) : null;

        String inclusiveMaxText = constraint.getInclusiveMax();
        String exclusiveMinText = constraint.getExclusiveMin();

        Date inclusiveMax = inclusiveMaxText != null ? ValidationUtils.getDate(inclusiveMaxText, dateTimeService) : null;
        Date exclusiveMin = exclusiveMinText != null ? ValidationUtils.getDate(exclusiveMinText, dateTimeService) : null;
        
		return isInRange(result, date, inclusiveMax, inclusiveMaxText, exclusiveMin, exclusiveMinText, attributeValueReader);
	}
	
	protected ConstraintValidationResult validateRange(DictionaryValidationResult result, Number value, RangeConstraint constraint, AttributeValueReader attributeValueReader) throws IllegalArgumentException {

		// TODO: JLR - need a code review of the conversions below to make sure this is the best way to ensure accuracy across all numerics
        // This will throw NumberFormatException if the value is 'NaN' or infinity... probably shouldn't be a NFE but something more intelligible at a higher level
        BigDecimal number = value != null ? new BigDecimal(value.toString()) : null;

        String inclusiveMaxText = constraint.getInclusiveMax();
        String exclusiveMinText = constraint.getExclusiveMin();
        
        BigDecimal inclusiveMax = inclusiveMaxText != null ? new BigDecimal(inclusiveMaxText) : null;
        BigDecimal exclusiveMin = exclusiveMinText != null ? new BigDecimal(exclusiveMinText) : null;
        
		return isInRange(result, number, inclusiveMax, inclusiveMaxText, exclusiveMin, exclusiveMinText, attributeValueReader);
	}

	private <T> ConstraintValidationResult isInRange(DictionaryValidationResult result, T value, Comparable<T> inclusiveMax, String inclusiveMaxText, Comparable<T> exclusiveMin, String exclusiveMinText, AttributeValueReader attributeValueReader) {
        // What we want to know is that the maximum value is greater than or equal to the number passed (the number can be equal to the max, i.e. it's 'inclusive')
        Result lessThanMax = ValidationUtils.isLessThanOrEqual(value, inclusiveMax); 
        // On the other hand, since the minimum is exclusive, we just want to make sure it's less than the number (the number can't be equal to the min, i.e. it's 'exclusive')
        Result greaterThanMin = ValidationUtils.isGreaterThan(value, exclusiveMin); 
          
        // It's okay for one end of the range to be undefined - that's not an error. It's only an error if one of them is actually invalid. 
        if (lessThanMax != Result.INVALID && greaterThanMin != Result.INVALID) { 
        	// Of course, if they're both undefined then we didn't actually have a real constraint
        	if (lessThanMax == Result.UNDEFINED && greaterThanMin == Result.UNDEFINED)
        		return result.addNoConstraint(attributeValueReader, CONSTRAINT_NAME);
        	
        	// In this case, we've succeeded
        	return result.addSuccess(attributeValueReader, CONSTRAINT_NAME);
        }
        
        // If both comparisons happened then if either comparison failed we can show the end user the expected range on both sides.
        if (lessThanMax != Result.UNDEFINED && greaterThanMin != Result.UNDEFINED) 
        	return result.addError(attributeValueReader, CONSTRAINT_NAME, RiceKeyConstants.ERROR_OUT_OF_RANGE, exclusiveMinText, inclusiveMaxText);
        // If it's the max comparison that fails, then just tell the end user what the max can be
        else if (lessThanMax == Result.INVALID)
        	return result.addError(attributeValueReader, CONSTRAINT_NAME, RiceKeyConstants.ERROR_INCLUSIVE_MAX, inclusiveMaxText);
        // Otherwise, just tell them what the min can be
        else 
        	return result.addError(attributeValueReader, CONSTRAINT_NAME, RiceKeyConstants.ERROR_EXCLUSIVE_MIN, exclusiveMinText);
	}
	
}

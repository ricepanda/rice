/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.krad.service.impl;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.criteria.Predicate;
import org.kuali.rice.core.api.criteria.PredicateFactory;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.core.api.search.SearchOperator;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.core.api.util.type.TypeUtils;
import org.kuali.rice.core.framework.persistence.ojb.conversion.OjbCharBooleanConversion;
import org.kuali.rice.core.framework.persistence.platform.DatabasePlatform;
import org.kuali.rice.krad.bo.InactivatableFromTo;
import org.kuali.rice.krad.data.DataObjectService;
import org.kuali.rice.krad.data.metadata.MetadataRepository;
import org.kuali.rice.krad.lookup.LookupUtils;
import org.kuali.rice.krad.service.DataDictionaryService;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.field.LookupInputField;
import org.kuali.rice.krad.uif.view.LookupView;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.KRADPropertyConstants;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base LookupCriteriaGenerator into which logic common to both OJB and JPA for criteria construction
 * has been extracted.  Subclasses implement backend-specific criteria translation/generation details.
 */
public class LookupCriteriaGeneratorImpl implements LookupCriteriaGenerator {

    private static final Logger LOG = Logger.getLogger(LookupCriteriaGeneratorImpl.class);

    private DateTimeService dateTimeService;
    private DataDictionaryService dataDictionaryService;
    private DatabasePlatform dbPlatform;
    private DataObjectService dataObjectService;
    private MetadataRepository metadataRepository;

    public DateTimeService getDateTimeService() {
        return dateTimeService;
    }

    public void setDateTimeService(DateTimeService dateTimeService) {
        this.dateTimeService = dateTimeService;
    }

    public DataDictionaryService getDataDictionaryService() {
        return dataDictionaryService;
    }

    public void setDataDictionaryService(DataDictionaryService dataDictionaryService) {
        this.dataDictionaryService = dataDictionaryService;
    }

    public DatabasePlatform getDbPlatform() {
        return dbPlatform;
    }

    public void setDbPlatform(DatabasePlatform dbPlatform) {
        this.dbPlatform = dbPlatform;
    }

    public DataObjectService getDataObjectService() {
        return dataObjectService;
    }

    public void setDataObjectService(DataObjectService dataObjectService) {
        this.dataObjectService = dataObjectService;
    }

    public MetadataRepository getMetadataRepository() {
        return metadataRepository;
    }

    public void setMetadataRepository(MetadataRepository metadataRepository) {
        this.metadataRepository = metadataRepository;
    }

    @Override
    public QueryByCriteria.Builder generateCriteria(Class<?> type, Map<String, String> formProps, boolean usePrimaryKeysOnly) {
        if (usePrimaryKeysOnly) {
            return getCollectionCriteriaFromMapUsingPrimaryKeysOnly(type, instantiateLookupDataObject(type), formProps).toQueryBuilder();
        } else {
            return getCollectionCriteriaFromMap(type, instantiateLookupDataObject(type), formProps).toQueryBuilder();
        }
    }

    @Override
    public QueryByCriteria.Builder createObjectCriteriaFromMap(Object example, Map<String, String> formProps) {
        Predicates criteria = new Predicates();

        // iterate through the parameter map for search criteria
        for (Map.Entry<String, String> formProp : formProps.entrySet()) {

            String propertyName = formProp.getKey();
            String searchValue = "";
            if (formProp.getValue() != null) {
                searchValue = formProp.getValue();
            }

            if (StringUtils.isNotBlank(searchValue) & PropertyUtils.isWriteable(example, propertyName)) {
                Class<?> propertyType = getPropertyType(example, propertyName);
                if (TypeUtils.isIntegralClass(propertyType) || TypeUtils.isDecimalClass(propertyType) ) {
                    addEqualNumeric(criteria, propertyName, propertyType, searchValue);
                } else if (TypeUtils.isTemporalClass(propertyType)) {
                    addEqualTemporal(criteria, propertyName, searchValue);
                } else {
                    addEqual(criteria, propertyName, searchValue);
                }
            }
        }

        return criteria.toQueryBuilder();
    }

    /**
     * Instantiates a new instance of the data object for the given type.
     *
     * @param type the type of the data object to pass, must not be null
     * @return new instance of the given data object
     */
    protected Object instantiateLookupDataObject(Class<?> type) {
        Validate.notNull(type, "DataObject type passed to lookup was null");
        try {
            return type.newInstance();
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Could not create instance of " + type, e);
        } catch (InstantiationException e) {
            throw new RuntimeException("Could not create instance of " + type, e);
        }
    }

    protected boolean createCriteria(Object example, String searchValue, String propertyName, Predicates criteria) {
        return createCriteria(example, searchValue, propertyName, false, false, criteria);
    }

    public boolean createCriteria(Object example, String searchValue, String propertyName, boolean caseInsensitive, boolean treatWildcardsAndOperatorsAsLiteral, Predicates criteria) {
        return createCriteria( example, searchValue, propertyName, caseInsensitive, treatWildcardsAndOperatorsAsLiteral, criteria, null );
    }

    protected boolean createCriteria(Object example, String searchValue, String propertyName, boolean caseInsensitive, boolean treatWildcardsAndOperatorsAsLiteral, Predicates criteria, Map<String, String> searchValues) {
        // if searchValue is empty and the key is not a valid property ignore
        if (StringUtils.isBlank(searchValue) || !isWriteable(example, propertyName)) {
            return false;
        }

        // get property type which is used to determine type of criteria
        Class<?> propertyType = getPropertyType(example, propertyName);
        if (propertyType == null) {
            return false;
        }

        // build criteria
        if (example instanceof InactivatableFromTo) {
            if (KRADPropertyConstants.ACTIVE.equals(propertyName)) {
                addInactivateableFromToActiveCriteria(example, searchValue, criteria, searchValues);
            } else if (KRADPropertyConstants.CURRENT.equals(propertyName)) {
                addInactivateableFromToCurrentCriteria(example, searchValue, criteria, searchValues);
            } else if (!KRADPropertyConstants.ACTIVE_AS_OF_DATE.equals(propertyName)) {
                addCriteria(propertyName, searchValue, propertyType, caseInsensitive,
                        treatWildcardsAndOperatorsAsLiteral, criteria);
            }
        } else {
            addCriteria(propertyName, searchValue, propertyType, caseInsensitive, treatWildcardsAndOperatorsAsLiteral,
                    criteria);
        }

        return true;
    }

    protected Predicates getCollectionCriteriaFromMap(Class<?> type, Object example, Map<String, String> formProps) {
        Predicates criteria = new Predicates();
        for (String propertyName : formProps.keySet()) {
            boolean caseInsensitive = determineAttributeSearchCaseSensitivity(type, propertyName);
            boolean treatWildcardsAndOperatorsAsLiteral = doesLookupFieldTreatWildcardsAndOperatorsAsLiteral(type, propertyName);
            String searchValue = formProps.get(propertyName);
            addCriteriaForPropertyValues(example, propertyName, caseInsensitive, treatWildcardsAndOperatorsAsLiteral, criteria, formProps, searchValue);
        }
        return criteria;
    }

    /**
     * Returns whether we should perform comparisons in a case-insensitive manner for this attribute.
     * By default comparisons are case-INsensitive, however, if the attribute is marked as "forceUppercase" in the DD,
     * then the comparison is case-SENSITIVE.
     * NOTE: The assumption is that for forceUppercase-d attributes, the DB data is already uppercased, so we can perform a case-sensitive search
     * @param type the type of the data object
     * @param propertyName the business object property
     * @return whether we should perform comparisons in a case-insensitive manner for this attribute
     */
    protected boolean determineAttributeSearchCaseSensitivity(Class<?> type, String propertyName) {
        Boolean caseInsensitive = Boolean.TRUE;
        if (dataDictionaryService.isAttributeDefined(type, propertyName)) {
            // If forceUppercase is true, both the database value and the user entry should be converted to Uppercase -- so change the caseInsensitive to false since we don't need to
            // worry about the values not matching.  However, if forceUppercase is false, make sure to do a caseInsensitive search because the database value and user entry
            // could be mixed case.  Thus, caseInsensitive will be the opposite of forceUppercase.
            caseInsensitive = dataDictionaryService.getAttributeForceUppercase(type, propertyName);
        }
        if (caseInsensitive == null) {
            caseInsensitive = Boolean.TRUE;
        }
        return caseInsensitive.booleanValue();
    }

    /**
     * Adds a criteria for the property for each search value, handling search value case
     * @param example the example search object
     * @param propertyName the object property
     * @param caseInsensitive case sensitivity determination
     * @param treatWildcardsAndOperatorsAsLiteral whether to treat wildcards and operators as literal
     * @param criteria the criteria we are modifying
     * @param formProps the search form properties
     * @param searchValues the property search values
     * @return whether all criteria were successfully added, false if any were invalid and loop was short-circuited
     */
    protected boolean addCriteriaForPropertyValues(Object example, String propertyName, boolean caseInsensitive, boolean treatWildcardsAndOperatorsAsLiteral, Predicates criteria, Map formProps, String... searchValues) {
        for (String searchValue: searchValues) {
            if (!caseInsensitive) {
                // Verify that the searchValue is uppercased if caseInsensitive is false
                searchValue = searchValue.toUpperCase();
            }
            if (!createCriteria(example, searchValue, propertyName, caseInsensitive, treatWildcardsAndOperatorsAsLiteral, criteria, formProps)) {
                return false;
            }
        }
        return true;
    }

    protected Predicates getCollectionCriteriaFromMapUsingPrimaryKeysOnly(Class<?> type, Object dataObject, Map<String, String> formProps) {
        Predicates criteria = new Predicates();
        List<String> pkFields = listPrimaryKeyFieldNames(type);
        for (String pkFieldName : pkFields) {
            String pkValue = formProps.get(pkFieldName);
            if (StringUtils.isBlank(pkValue)) {
                throw new RuntimeException("Missing pk value for field " + pkFieldName + " when a search based on PK values only is performed.");
            }
            else {
                for (SearchOperator op : SearchOperator.QUERY_CHARACTERS) {
                    if (pkValue.contains(op.op())) {
                        throw new RuntimeException("Value \"" + pkValue + "\" for PK field " + pkFieldName + " contains wildcard/operator characters.");
                    }
                }
            }
            boolean treatWildcardsAndOperatorsAsLiteral = doesLookupFieldTreatWildcardsAndOperatorsAsLiteral(type, pkFieldName);
            createCriteria(dataObject, pkValue, pkFieldName, false, treatWildcardsAndOperatorsAsLiteral, criteria);
        }
        return criteria;
    }

    protected boolean doesLookupFieldTreatWildcardsAndOperatorsAsLiteral(Class<?> type, String fieldName) {
        // determine the LookupInputField for the field and use isDisableWildcardsAndOperators
        Map<String, String> indexKey = new HashMap<String, String>();
        indexKey.put(UifParameters.VIEW_NAME, UifConstants.DEFAULT_VIEW_NAME);
        indexKey.put(UifParameters.DATA_OBJECT_CLASS_NAME, type.getName());

        // obtain the Lookup View for the data object
        View view = getDataDictionaryService().getDataDictionary().getViewByTypeIndex(UifConstants.ViewType.LOOKUP, indexKey);
        if (view != null && view instanceof LookupView) {
            LookupView lookupView = (LookupView) view;
            // iterate through the criteria fields to find the lookup field for the given property
            List<Component> criteriaFields = lookupView.getCriteriaFields();
            for (Component criteriaField: criteriaFields) {
                if (criteriaField instanceof LookupInputField) {
                    LookupInputField lookupInputField = (LookupInputField) criteriaField;
                    if (fieldName.equals(lookupInputField.getPropertyName())) {
                        // this is the droid we're looking for
                        return lookupInputField.isDisableWildcardsAndOperators();
                    }
                }
            }
        }
        return false;
    }

    protected BigDecimal cleanNumeric(String value) {
        String cleanedValue = value.replaceAll("[^-0-9.]", "");
        // ensure only one "minus" at the beginning, if any
        if (cleanedValue.lastIndexOf('-') > 0) {
            if (cleanedValue.charAt(0) == '-') {
                cleanedValue = "-" + cleanedValue.replaceAll("-", "");
            } else {
                cleanedValue = cleanedValue.replaceAll("-", "");
            }
        }
        // ensure only one decimal in the string
        int decimalLoc = cleanedValue.lastIndexOf('.');
        if (cleanedValue.indexOf('.') != decimalLoc) {
            cleanedValue = cleanedValue.substring(0, decimalLoc).replaceAll("\\.", "") + cleanedValue.substring(decimalLoc);
        }
        try {
            return new BigDecimal(cleanedValue);
        } catch (NumberFormatException ex) {
            GlobalVariables.getMessageMap().putError(KRADConstants.DOCUMENT_ERRORS, RiceKeyConstants.ERROR_CUSTOM, new String[] { "Invalid Numeric Input: " + value });
            return null;
        }
    }

    protected void addOrCriteria(String propertyName, String propertyValue, Class propertyType, boolean caseInsensitive, Predicates criteria) {
        addLogicalOperatorCriteria(propertyName, propertyValue, propertyType, caseInsensitive, criteria, SearchOperator.OR.op());
    }

    protected void addAndCriteria(String propertyName, String propertyValue, Class propertyType, boolean caseInsensitive, Predicates criteria) {
        addLogicalOperatorCriteria(propertyName, propertyValue, propertyType, caseInsensitive, criteria, SearchOperator.AND.op());
    }

    /**
     * Adds to the criteria object based on the property type and any query characters given.
     */
    protected void addCriteria(String propertyName, String propertyValue, Class<?> propertyType, boolean caseInsensitive, boolean treatWildcardsAndOperatorsAsLiteral, Predicates criteria) {
        propertyName = parsePropertyName(criteria, propertyName);

        if (!treatWildcardsAndOperatorsAsLiteral && StringUtils.contains(propertyValue, SearchOperator.OR.op())) {
            addOrCriteria(propertyName, propertyValue, propertyType, caseInsensitive, criteria);
            return;
        }

        if (!treatWildcardsAndOperatorsAsLiteral && StringUtils.contains(propertyValue, SearchOperator.AND.op())) {
            addAndCriteria(propertyName, propertyValue, propertyType, caseInsensitive, criteria);
            return;
        }

        if (StringUtils.equalsIgnoreCase(propertyValue, SearchOperator.NULL.op()) || StringUtils.equalsIgnoreCase(propertyValue, SearchOperator.NOT_NULL.op())) {
            // KULRICE-6846 null Lookup criteria causes sql exception
            if (StringUtils.contains(propertyValue, SearchOperator.NOT.op())) {
                addIsNotNull(criteria, propertyName);
            }
            else {
                addIsNull(criteria, propertyName);
            }
        }
        else if (TypeUtils.isStringClass(propertyType)) {

            // XXX TODO: handle case insensitivity for native jpa queries! don't just UPPER(column)

            //            // KULRICE-85 : made string searches case insensitive - used new DBPlatform function to force strings to upper case
            //            if (caseInsensitive) {
            //                propertyName = uppercasePropertyName(propertyName);
            //                propertyValue = propertyValue.toUpperCase();
            //            }
            if (!treatWildcardsAndOperatorsAsLiteral && StringUtils.contains(propertyValue,
                    SearchOperator.NOT.op())) {
                addNotCriteria(propertyName, propertyValue, propertyType, caseInsensitive, criteria);
            } else if (
                    !treatWildcardsAndOperatorsAsLiteral && propertyValue != null && (
                            StringUtils.contains(propertyValue, SearchOperator.BETWEEN.op())
                                    || propertyValue.startsWith(">")
                                    || propertyValue.startsWith("<") ) ) {
                addStringRangeCriteria(propertyName, propertyValue, caseInsensitive, criteria);
            } else {
                if (treatWildcardsAndOperatorsAsLiteral) {
                    propertyValue = StringUtils.replace(propertyValue, "*", "\\*");
                }
                addLike(criteria, propertyName, propertyValue, caseInsensitive);
            }
        } else if (TypeUtils.isIntegralClass(propertyType) || TypeUtils.isDecimalClass(propertyType)) {
            addNumericRangeCriteria(propertyName, propertyValue, treatWildcardsAndOperatorsAsLiteral, criteria);
        } else if (TypeUtils.isTemporalClass(propertyType)) {
            addDateRangeCriteria(propertyName, propertyValue, treatWildcardsAndOperatorsAsLiteral, criteria);
        } else if (TypeUtils.isBooleanClass(propertyType)) {
            addEqualToBoolean(criteria, propertyName, propertyValue);
        } else {
            LOG.error("not adding criterion for: " + propertyName + "," + propertyType + "," + propertyValue);
        }
    }

    protected void addNotCriteria(String propertyName, String propertyValue, Class propertyType, boolean caseInsensitive, Predicates criteria) {
        String[] splitPropVal = StringUtils.split(propertyValue, SearchOperator.NOT.op());

        int strLength = splitPropVal.length;
        // if more than one NOT operator assume an implicit and (i.e. !a!b = !a&!b)
        if (strLength > 1) {
            String expandedNot = SearchOperator.NOT + StringUtils.join(splitPropVal, SearchOperator.AND.op() + SearchOperator.NOT.op());
            // we know that since this method was called, treatWildcardsAndOperatorsAsLiteral must be false
            addCriteria(propertyName, expandedNot, propertyType, caseInsensitive, false, criteria);
        } else {
            // only one so add a not like
            addNotLike(criteria, propertyName, splitPropVal[0], caseInsensitive);
        }
    }

    /**
     * Adds to the criteria object based on query characters given
     */
    protected void addDateRangeCriteria(String propertyName, String propertyValue, boolean treatWildcardsAndOperatorsAsLiteral, Predicates criteria) {
        if (StringUtils.contains(propertyValue, SearchOperator.BETWEEN.op())) {
            if (treatWildcardsAndOperatorsAsLiteral)
                throw new RuntimeException("Wildcards and operators are not allowed on this date field: " + propertyName);
            String[] rangeValues = StringUtils.split(propertyValue, SearchOperator.BETWEEN.op());
            addBetween(criteria, propertyName, parseDate(LookupUtils.scrubQueryCharacters(rangeValues[0])), parseDate(LookupUtils.scrubQueryCharacters(rangeValues[1])));
        } else if (propertyValue.startsWith(SearchOperator.GREATER_THAN_EQUAL.op())) {
            if (treatWildcardsAndOperatorsAsLiteral)
                throw new RuntimeException("Wildcards and operators are not allowed on this date field: " + propertyName);
            addGreaterThanOrEqual(criteria, propertyName, parseDate(LookupUtils.scrubQueryCharacters(propertyValue)));
        } else if (propertyValue.startsWith(SearchOperator.LESS_THAN_EQUAL.op())) {
            if (treatWildcardsAndOperatorsAsLiteral)
                throw new RuntimeException("Wildcards and operators are not allowed on this date field: " + propertyName);
            addLessThanOrEqual(criteria, propertyName, parseDate(LookupUtils.scrubQueryCharacters(propertyValue)));
        } else if (propertyValue.startsWith(SearchOperator.GREATER_THAN.op())) {
            if (treatWildcardsAndOperatorsAsLiteral)
                throw new RuntimeException("Wildcards and operators are not allowed on this date field: " + propertyName);
            addGreaterThan(criteria, propertyName, parseDate(LookupUtils.scrubQueryCharacters(propertyValue)));
        } else if (propertyValue.startsWith(SearchOperator.LESS_THAN.op())) {
            if (treatWildcardsAndOperatorsAsLiteral)
                throw new RuntimeException("Wildcards and operators are not allowed on this date field: " + propertyName);
            addLessThan(criteria, propertyName, parseDate(LookupUtils.scrubQueryCharacters(propertyValue)));
        } else {
            addEqual(criteria, propertyName, parseDate(LookupUtils.scrubQueryCharacters(propertyValue)));
        }
    }

    /**
     * Adds to the criteria object based on query characters given
     */
    protected void addNumericRangeCriteria(String propertyName, String propertyValue, boolean treatWildcardsAndOperatorsAsLiteral, Predicates criteria) {
        if (StringUtils.contains(propertyValue, SearchOperator.BETWEEN.op())) {
            if (treatWildcardsAndOperatorsAsLiteral)
                throw new RuntimeException("Cannot use wildcards and operators on numeric field " + propertyName);
            String[] rangeValues = StringUtils.split(propertyValue, SearchOperator.BETWEEN.op());
            addBetween(criteria, propertyName, cleanNumeric(rangeValues[0]), cleanNumeric(rangeValues[1]));
        } else if (propertyValue.startsWith(SearchOperator.GREATER_THAN_EQUAL.op())) {
            if (treatWildcardsAndOperatorsAsLiteral)
                throw new RuntimeException("Cannot use wildcards and operators on numeric field " + propertyName);
            addGreaterThanOrEqual(criteria, propertyName, cleanNumeric(propertyValue));
        } else if (propertyValue.startsWith(SearchOperator.LESS_THAN_EQUAL.op())) {
            if (treatWildcardsAndOperatorsAsLiteral)
                throw new RuntimeException("Cannot use wildcards and operators on numeric field " + propertyName);
            addLessThanOrEqual(criteria, propertyName, cleanNumeric(propertyValue));
        } else if (propertyValue.startsWith(SearchOperator.GREATER_THAN.op())) {
            if (treatWildcardsAndOperatorsAsLiteral)
                throw new RuntimeException("Cannot use wildcards and operators on numeric field " + propertyName);
            addGreaterThan(criteria, propertyName, cleanNumeric(propertyValue));
        } else if (propertyValue.startsWith(SearchOperator.LESS_THAN.op())) {
            if (treatWildcardsAndOperatorsAsLiteral)
                throw new RuntimeException("Cannot use wildcards and operators on numeric field " + propertyName);
            addLessThan(criteria, propertyName, cleanNumeric(propertyValue));
        } else {
            addEqual(criteria, propertyName, cleanNumeric(propertyValue));
        }
    }

    /**
     * Adds to the criteria object based on query characters given
     */
    protected void addStringRangeCriteria(String propertyName, String propertyValue, boolean caseInsensitive, Predicates criteria) {
        if (StringUtils.contains(propertyValue, SearchOperator.BETWEEN.op())) {
            String[] rangeValues = StringUtils.split(propertyValue, SearchOperator.BETWEEN.op());
            addBetween(criteria, propertyName, rangeValues[0], rangeValues[1], caseInsensitive);
        } else if (propertyValue.startsWith(SearchOperator.GREATER_THAN_EQUAL.op())) {
            addGreaterThanOrEqual(criteria, propertyName, LookupUtils.scrubQueryCharacters(propertyValue), caseInsensitive);
        } else if (propertyValue.startsWith(SearchOperator.LESS_THAN_EQUAL.op())) {
            addLessThanOrEqual(criteria, propertyName, LookupUtils.scrubQueryCharacters(propertyValue), caseInsensitive);
        } else if (propertyValue.startsWith(SearchOperator.GREATER_THAN.op())) {
            addGreaterThan(criteria, propertyName, LookupUtils.scrubQueryCharacters(propertyValue), caseInsensitive);
        } else if (propertyValue.startsWith(SearchOperator.LESS_THAN.op())) {
            addLessThan(criteria, propertyName, LookupUtils.scrubQueryCharacters(propertyValue), caseInsensitive);
        } else {
            addEqual(criteria, propertyName, LookupUtils.scrubQueryCharacters(propertyValue), caseInsensitive);
        }
    }

    /**
     * Translates criteria for active status to criteria on the active from and to fields
     *
     * @param example - business object being queried on
     * @param activeSearchValue - value for the active search field, should convert to boolean
     * @param criteria - Criteria object being built
     * @param searchValues - Map containing all search keys and values
     */
    protected void addInactivateableFromToActiveCriteria(Object example, String activeSearchValue, Predicates criteria, Map<String, String> searchValues) {
        Timestamp activeTimestamp = LookupUtils.getActiveDateTimestampForCriteria(searchValues);

        String activeBooleanStr = (String) (new OjbCharBooleanConversion()).javaToSql(activeSearchValue);
        if (OjbCharBooleanConversion.DATABASE_BOOLEAN_TRUE_STRING_REPRESENTATION.equals(activeBooleanStr)) {
            // (active from date <= date or active from date is null) and (date < active to date or active to date is null)
            Predicates criteriaBeginDate = new Predicates();
            addLessThanOrEqual(criteriaBeginDate, KRADPropertyConstants.ACTIVE_FROM_DATE, activeTimestamp);

            Predicates criteriaBeginDateNull = new Predicates();
            addIsNull(criteriaBeginDateNull, KRADPropertyConstants.ACTIVE_FROM_DATE);
            addOr(criteriaBeginDate, criteriaBeginDateNull);

            addAnd(criteria, criteriaBeginDate);

            Predicates criteriaEndDate = new Predicates();
            addGreaterThan(criteriaEndDate, KRADPropertyConstants.ACTIVE_TO_DATE, activeTimestamp);

            Predicates criteriaEndDateNull = new Predicates();
            addIsNull(criteriaEndDateNull, KRADPropertyConstants.ACTIVE_TO_DATE);
            addOr(criteriaEndDate, criteriaEndDateNull);

            addAnd(criteria, criteriaEndDate);
        }
        else if (OjbCharBooleanConversion.DATABASE_BOOLEAN_FALSE_STRING_REPRESENTATION.equals(activeBooleanStr)) {
            // (date < active from date) or (active from date is null) or (date >= active to date)
            Predicates criteriaNonActive = new Predicates();
            addGreaterThan(criteriaNonActive, KRADPropertyConstants.ACTIVE_FROM_DATE, activeTimestamp);

            // NOTE: Ojb and Jpa implementations of LookupDao disagreed on the content of this query
            // OJB omitted this (active from date is null) clause, meaning the OJB lookup dao would not return
            // records without an active_from_date as "not active", i.e. they were considered active.
            // The opposed was true of JPA, this clause would be added, and those records would be matched and returned
            // as inactive.
            // this has ramifications for existing tests which appear to use the OJB implementation semantics
            // so we conform to the OJB behavior
            // Predicates criteriaBeginDateNull = createCriteria(example.getClass());
            // addIsNull(criteriaBeginDateNull, KRADPropertyConstants.ACTIVE_FROM_DATE);
            // addOr(criteriaNonActive, criteriaBeginDateNull);

            Predicates criteriaEndDate = new Predicates();
            addLessThanOrEqual(criteriaEndDate, KRADPropertyConstants.ACTIVE_TO_DATE, activeTimestamp);
            addOr(criteriaNonActive, criteriaEndDate);

            addAnd(criteria, criteriaNonActive);
        }
    }

    /**
     * Builds a sub criteria object joined with an 'AND' or 'OR' (depending on splitValue) using the split values of propertyValue. Then joins back the
     * sub criteria to the main criteria using an 'AND'.
     */
    protected void addLogicalOperatorCriteria(String propertyName, String propertyValue, Class<?> propertyType, boolean caseInsensitive, Predicates criteria, String splitValue) {
        String[] splitPropVal = StringUtils.split(propertyValue, splitValue);

        Predicates subCriteria;
        if (SearchOperator.OR.op().equals(splitValue)) {
            subCriteria = new OrPredicates();
        } else if (SearchOperator.AND.op().equals(splitValue)) {
            subCriteria = new Predicates();
        } else {
            throw new IllegalArgumentException("Invalid split value: " + splitValue);
        }
        for (int i = 0; i < splitPropVal.length; i++) {
            Predicates predicate = new Predicates();
            // we know that since this method is called, treatWildcardsAndOperatorsAsLiteral is false
            addCriteria(propertyName, splitPropVal[i], propertyType, caseInsensitive, false, subCriteria);
        }
        addAnd(criteria, subCriteria);
    }

    //    protected void addBetween(Predicates criteria, String propertyName, String value1, String value2, boolean caseInsensitive) {
    //        if (caseInsensitive) {
    //            propertyName = uppercasePropertyName(propertyName);
    //            value1 = value1.toUpperCase();
    //            value2 = value2.toUpperCase();
    //        }
    //        addBetween(criteria, propertyName, value1, value2);
    //    }
    //
    //    protected void addEqual(Predicates criteria, String propertyName, String searchValue, boolean caseInsensitive) {
    //        if (caseInsensitive) {
    //            propertyName = uppercasePropertyName(propertyName);
    //            searchValue = searchValue.toUpperCase();
    //        }
    //        addEqual(criteria, propertyName, searchValue);
    //    }
    //
    //    protected void addLessThan(Predicates criteria, String propertyName, String propertyValue, boolean caseInsensitive) {
    //        if (caseInsensitive) {
    //            propertyName = uppercasePropertyName(propertyName);
    //            propertyValue = propertyValue.toUpperCase();
    //        }
    //        addLessThan(criteria, propertyName, propertyValue);
    //    }
    //
    //    protected void addLessThanOrEqual(Predicates criteria, String propertyName, String propertyValue, boolean caseInsensitive) {
    //        if (caseInsensitive) {
    //            propertyName = uppercasePropertyName(propertyName);
    //            propertyValue = propertyValue.toUpperCase();
    //        }
    //        addLessThanOrEqual(criteria, propertyName, propertyValue);
    //    }
    //
    //    protected void addGreaterThan(Predicates criteria, String propertyName, String propertyValue, boolean caseInsensitive) {
    //        if (caseInsensitive) {
    //            propertyName = uppercasePropertyName(propertyName);
    //            propertyValue = propertyValue.toUpperCase();
    //        }
    //        addGreaterThan(criteria, propertyName, propertyValue);
    //    }
    //
    //    protected void addGreaterThanOrEqual(Predicates criteria, String propertyName, String propertyValue, boolean caseInsensitive) {
    //        if (caseInsensitive) {
    //            propertyName = uppercasePropertyName(propertyName);
    //            propertyValue = propertyValue.toUpperCase();
    //        }
    //        addGreaterThanOrEqual(criteria, propertyName, propertyValue);
    //    }
    //
    //    protected void addLike(Predicates criteria, String propertyName, String propertyValue, boolean caseInsensitive) {
    //        if (caseInsensitive) {
    //            propertyName = uppercasePropertyName(propertyName);
    //            propertyValue = propertyValue.toUpperCase();
    //        }
    //        addLike(criteria, propertyName, propertyValue);
    //    }
    //
    //    protected void addNotLike(Predicates criteria, String propertyName, String propertyValue, boolean caseInsensitive) {
    //        if (caseInsensitive) {
    //            propertyName = uppercasePropertyName(propertyName);
    //            propertyValue = propertyValue.toUpperCase();
    //        }
    //        addNotLike(criteria, propertyName, propertyValue);
    //    }


    protected java.sql.Date parseDate(String dateString) {
        dateString = dateString.trim();
        try {
            return dateTimeService.convertToSqlDate(dateString);
        } catch (ParseException ex) {
            return null;
        }
    }

    protected List<String> listPrimaryKeyFieldNames(Class<?> type) {
        return metadataRepository.getMetadata(type).getPrimaryKeyAttributeNames();
    }

    protected Class<?> getPropertyType(Object example, String propertyName) {
        return getDataObjectService().wrap(example).getPropertyType(propertyName);
    }

    /**
     * Return whether or not an attribute is writeable. This method is aware
     * that that Collections may be involved and handles them consistently with
     * the way in which OJB handles specifying the attributes of elements of a
     * Collection.
     *
     * @param o
     * @param p
     * @return
     * @throws IllegalArgumentException
     */
    protected boolean isWriteable(Object o, String p) throws IllegalArgumentException {
        if (null == o || null == p) {
            throw new IllegalArgumentException("Cannot check writeable status with null arguments.");
        }

        boolean b = false;

        // Try the easy way.
        if (!(PropertyUtils.isWriteable(o, p))) {

            // If that fails lets try to be a bit smarter, understanding that
            // Collections may be involved.
            if (-1 != p.indexOf('.')) {

                String[] parts = p.split("\\.");

                // Get the type of the attribute.
                Class<?> c = getPropertyType(o, parts[0]);

                Object i = null;

                // If the next level is a Collection, look into the collection,
                // to find out what type its elements are.
                if (Collection.class.isAssignableFrom(c)) {
                    c = metadataRepository.getMetadata(o.getClass()).getCollection(parts[0]).getRelatedType();
                }

                // Look into the attribute class to see if it is writeable.
                try {
                    i = c.newInstance();
                    StringBuffer sb = new StringBuffer();
                    for (int x = 1; x < parts.length; x++) {
                        sb.append(1 == x ? "" : ".").append(parts[x]);
                    }
                    b = isWriteable(i, sb.toString());
                } catch (InstantiationException ie) {
                    LOG.info(ie);
                } catch (IllegalAccessException iae) {
                    LOG.info(iae);
                }
            }
        } else {
            b = true;
        }

        return b;
    }

    protected void addEqualNumeric(Predicates criteria, String propertyName, Class<?> propertyClass, String searchValue) {
        Predicate pred;
        if (propertyClass.equals(Long.class)) {
            pred = PredicateFactory.equal(propertyName, new Long(searchValue));
        } else {
            pred = PredicateFactory.equal(propertyName, new Integer(searchValue));
        }

        criteria.addPredicate(pred);
    }

    protected void addEqualTemporal(Predicates criteria, String propertyName, String searchValue) {
        criteria.addPredicate(PredicateFactory.equal(propertyName, parseDate(LookupUtils.scrubQueryCharacters(
                searchValue))));
    }

    protected void addEqual(Predicates criteria, String propertyName, Object searchValue) {
        criteria.addPredicate(PredicateFactory.equal(propertyName, searchValue));
    }

    protected void addIsNull(Predicates criteria, String propertyName) {
        criteria.addPredicate(PredicateFactory.isNull(propertyName));
    }

    protected void addIsNotNull(Predicates criteria, String propertyName) {
        criteria.addPredicate(PredicateFactory.isNotNull(propertyName));
    }

    protected void addLike(Predicates criteria, String propertyName, String propertyValue) {
        criteria.addPredicate(PredicateFactory.like(propertyName, propertyValue));
    }

    protected void addNotLike(Predicates criteria, String propertyName, String propertyValue) {
        criteria.addPredicate(PredicateFactory.notLike(propertyName, propertyValue));
    }

    protected void addEqualToBoolean(Predicates criteria, String propertyName, String propertyValue) {
        String temp = LookupUtils.scrubQueryCharacters(propertyValue);
        criteria.addPredicate(PredicateFactory.equal(propertyName, ("Y".equalsIgnoreCase(temp) || "T".equalsIgnoreCase(temp) || "1".equalsIgnoreCase(temp) || "true".equalsIgnoreCase(temp))));
    }

    /**
     * Should return a string which is a server-side identifier for the uppercased property,
     * that is, this is not the uppercased version of the property name, but rather the property value uppercased
     * this is typically a builtin SQL function
     * @param propertyName the property/column name
     * @return expression that represents the uppercased value of the property
     */
    protected String uppercasePropertyName(String propertyName) {
        // return a SQL expression and hope everything goes to plan...
        return dbPlatform.getUpperCaseFunction() + "(" + propertyName + ")";
    }

    protected void addAnd(Predicates criteria, Predicates criteria2) {
        criteria.and(criteria2);
    }

    protected void addLessThan(Predicates criteria, String propertyName, Object propertyValue) {
        criteria.addPredicate(PredicateFactory.lessThan(propertyName, propertyValue));
    }

    protected void addLessThanOrEqual(Predicates criteria, String propertyName, Object propertyValue) {
        criteria.addPredicate(PredicateFactory.lessThanOrEqual(propertyName, propertyValue));
    }

    protected void addGreaterThan(Predicates criteria, String propertyName, Object propertyValue) {
        criteria.addPredicate(PredicateFactory.greaterThan(propertyName, propertyValue));
    }

    protected void addGreaterThanOrEqual(Predicates criteria, String propertyName, Object propertyValue) {
        criteria.addPredicate(PredicateFactory.greaterThanOrEqual(propertyName, propertyValue));
    }

    protected void addBetween(Predicates criteria, String propertyName, Object value1, Object value2) {
        criteria.addPredicate(PredicateFactory.between(propertyName, value1, value2));
    }

    protected void addOr(Predicates criteria, Predicates criteria2) {
        criteria.or(criteria2);
    }

    protected void addEqual(Predicates criteria, String propertyName, String searchValue, boolean caseInsensitive) {
        if (caseInsensitive) {
            criteria.addPredicate(PredicateFactory.equalIgnoreCase(propertyName, searchValue));
        } else {
            addEqual(criteria, propertyName, searchValue);
        }
    }

    protected void addGreaterThan(Predicates criteria, String propertyName, String propertyValue, boolean caseInsensitive) {
        // XXX: QBC does not support case sensitivity for GT
        addGreaterThan(criteria, propertyName, propertyValue);
    }

    protected void addGreaterThanOrEqual(Predicates criteria, String propertyName, String propertyValue, boolean caseInsensitive) {
        // XXX: QBC does not support case sensitivity for GTE
        addGreaterThanOrEqual(criteria, propertyName, propertyValue);
    }

    protected void addLessThan(Predicates criteria, String propertyName, String propertyValue, boolean caseInsensitive) {
        // XXX: QBC does not support case sensitivity for LT
        addLessThan(criteria, propertyName, propertyValue);
    }

    protected void addLessThanOrEqual(Predicates criteria, String propertyName, String propertyValue, boolean caseInsensitive) {
        // XXX: QBC does not support case sensitivity for LTE
        addLessThanOrEqual(criteria, propertyName, propertyValue);
    }

    protected void addLike(Predicates criteria, String propertyName, String propertyValue, boolean caseInsensitive) {
        // XXX: QBC does not support case sensitivity for like
        addLike(criteria, propertyName, propertyValue);
    }

    protected void addBetween(Predicates criteria, String propertyName, String value1, String value2, boolean caseInsensitive) {
        // XXX: QBC does not support case sensitivity for between
        addBetween(criteria, propertyName, value1, value2);
    }

    protected void addNotLike(Predicates criteria, String propertyName, String propertyValue, boolean caseInsensitive) {
        // XXX: QBC does not support case sensitivity for notlike
        addNotLike(criteria, propertyName, propertyValue);
    }

    protected String parsePropertyName(Predicates criteria, String fullyQualifiedPropertyName) {
        return fullyQualifiedPropertyName;
    }

    protected void addInactivateableFromToCurrentCriteria(Object example, String currentSearchValue, Predicates criteria, Map searchValues) {
        // do nothing.  QueryByCriteria In and NotIn predicates do not support sub-queries, which means this type of query cannot be
        // forwarded down to the provider, and that the caller is responsible for filtering in/out "current" data objects from the results
    }

    /**
     * Encapsulates our list of Predicates by default we explicitly AND top level predicates ORing requires oring the
     * existing ANDed predicates, with a new list of ANDed predicates.
     */
    static class Predicates {
        // top level predicates will be anded by query by default
        protected List<Predicate> predicates = new ArrayList<Predicate>();

        void addPredicate(Predicate predicate) {
            predicates.add(predicate);
        }

        void or(Predicates pred) {
            List<Predicate> newpredicates = new ArrayList<Predicate>();
            newpredicates.add(PredicateFactory.or(getCriteriaPredicate(), pred.getCriteriaPredicate()));
            predicates = newpredicates;
        }

        void and(Predicates pred) {
            addPredicate(pred.getCriteriaPredicate());
        }

        protected Predicate getCriteriaPredicate() {
            if (predicates.size() == 1) {
                return predicates.get(0);
            }
            return PredicateFactory.and(predicates.toArray(new Predicate[predicates.size()]));
        }

        QueryByCriteria.Builder toQueryBuilder() {
            QueryByCriteria.Builder qbc = QueryByCriteria.Builder.create();
            qbc.setPredicates(getCriteriaPredicate());
            return qbc;
        }
    }

    static class OrPredicates extends Predicates {

        @Override
        protected Predicate getCriteriaPredicate() {
            if (predicates.size() == 1) {
                return predicates.get(0);
            }
            return PredicateFactory.or(predicates.toArray(new Predicate[predicates.size()]));
        }

    }

}
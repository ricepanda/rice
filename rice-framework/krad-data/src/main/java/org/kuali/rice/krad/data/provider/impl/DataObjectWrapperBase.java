/**
 * Copyright 2005-2014 The Kuali Foundation
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
package org.kuali.rice.krad.data.provider.impl;

import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.krad.data.CompoundKey;
import org.kuali.rice.krad.data.DataObjectService;
import org.kuali.rice.krad.data.DataObjectWrapper;
import org.kuali.rice.krad.data.metadata.DataObjectAttribute;
import org.kuali.rice.krad.data.metadata.DataObjectAttributeRelationship;
import org.kuali.rice.krad.data.metadata.DataObjectCollection;
import org.kuali.rice.krad.data.metadata.DataObjectMetadata;
import org.kuali.rice.krad.data.metadata.DataObjectRelationship;
import org.kuali.rice.krad.data.metadata.MetadataChild;
import org.kuali.rice.krad.data.util.ReferenceLinker;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.NullValueInNestedPathException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.PropertyAccessorUtils;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.CollectionFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;

import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class DataObjectWrapperBase<T> implements DataObjectWrapper<T> {
	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DataObjectWrapperBase.class);

    private final T dataObject;
    private final DataObjectMetadata metadata;
    private final BeanWrapper wrapper;
    private final DataObjectService dataObjectService;
    private final ReferenceLinker referenceLinker;

    protected DataObjectWrapperBase(T dataObject, DataObjectMetadata metadata, DataObjectService dataObjectService,
            ReferenceLinker referenceLinker) {
        this.dataObject = dataObject;
        this.metadata = metadata;
        this.dataObjectService = dataObjectService;
        this.referenceLinker = referenceLinker;
        this.wrapper = PropertyAccessorFactory.forBeanPropertyAccess(dataObject);
        // note that we do *not* want to set auto grow to be true here since we are using this primarily for
        // access to the data, we will expose getPropertyValueNullSafe instead because it prevents a a call to
        // getPropertyValue from modifying the internal state of the object by growing intermediate nested paths
    }

    @Override
    public DataObjectMetadata getMetadata() {
        return metadata;
    }

    @Override
    public T getWrappedInstance() {
        return dataObject;
    }

    @Override
    public Object getPropertyValueNullSafe(String propertyName) throws BeansException {
        try {
            return getPropertyValue(propertyName);
        } catch (NullValueInNestedPathException e) {
            return null;
        }
    }

	@SuppressWarnings("unchecked")
	@Override
    public Class<T> getWrappedClass() {
        return wrapper.getWrappedClass();
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        return wrapper.getPropertyDescriptors();
    }

    @Override
    public PropertyDescriptor getPropertyDescriptor(String propertyName) throws InvalidPropertyException {
        return wrapper.getPropertyDescriptor(propertyName);
    }

    @Override
    public void setAutoGrowNestedPaths(boolean autoGrowNestedPaths) {
        wrapper.setAutoGrowNestedPaths(autoGrowNestedPaths);
    }

    @Override
    public boolean isAutoGrowNestedPaths() {
        return wrapper.isAutoGrowNestedPaths();
    }

    @Override
    public void setAutoGrowCollectionLimit(int autoGrowCollectionLimit) {
        wrapper.setAutoGrowCollectionLimit(autoGrowCollectionLimit);
    }

    @Override
    public int getAutoGrowCollectionLimit() {
        return wrapper.getAutoGrowCollectionLimit();
    }

    @Override
    public void setConversionService(ConversionService conversionService) {
        wrapper.setConversionService(conversionService);
    }

    @Override
    public ConversionService getConversionService() {
        return wrapper.getConversionService();
    }

    @Override
    public void setExtractOldValueForEditor(boolean extractOldValueForEditor) {
        wrapper.setExtractOldValueForEditor(extractOldValueForEditor);
    }

    @Override
    public boolean isExtractOldValueForEditor() {
        return wrapper.isExtractOldValueForEditor();
    }

    @Override
    public boolean isReadableProperty(String propertyName) {
        return wrapper.isReadableProperty(propertyName);
    }

    @Override
    public boolean isWritableProperty(String propertyName) {
        return wrapper.isWritableProperty(propertyName);
    }

    @Override
    public Class<?> getPropertyType(String propertyName) throws BeansException {
        return wrapper.getPropertyType(propertyName);
    }

    @Override
    public TypeDescriptor getPropertyTypeDescriptor(String propertyName) throws BeansException {
        return wrapper.getPropertyTypeDescriptor(propertyName);
    }

    @Override
    public Object getPropertyValue(String propertyName) throws BeansException {
        return wrapper.getPropertyValue(propertyName);
    }

    @Override
    public void setPropertyValue(String propertyName, Object value) throws BeansException {
        wrapper.setPropertyValue(propertyName, value);
    }

    @Override
    public void setPropertyValue(PropertyValue pv) throws BeansException {
        wrapper.setPropertyValue(pv);
    }

    @Override
    public void setPropertyValues(Map<?, ?> map) throws BeansException {
        wrapper.setPropertyValues(map);
    }

    @Override
    public void setPropertyValues(PropertyValues pvs) throws BeansException {
        wrapper.setPropertyValues(pvs);
    }

    @Override
    public void setPropertyValues(PropertyValues pvs, boolean ignoreUnknown) throws BeansException {
        wrapper.setPropertyValues(pvs, ignoreUnknown);
    }

    @Override
    public void setPropertyValues(PropertyValues pvs, boolean ignoreUnknown,
            boolean ignoreInvalid) throws BeansException {
        wrapper.setPropertyValues(pvs, ignoreUnknown, ignoreInvalid);
    }

    @Override
    public void registerCustomEditor(Class<?> requiredType, PropertyEditor propertyEditor) {
        wrapper.registerCustomEditor(requiredType, propertyEditor);
    }

    @Override
    public void registerCustomEditor(Class<?> requiredType, String propertyPath, PropertyEditor propertyEditor) {
        wrapper.registerCustomEditor(requiredType, propertyPath, propertyEditor);
    }

    @Override
    public PropertyEditor findCustomEditor(Class<?> requiredType, String propertyPath) {
        return wrapper.findCustomEditor(requiredType, propertyPath);
    }

    @Override
	public <Y> Y convertIfNecessary(Object value, Class<Y> requiredType) throws TypeMismatchException {
        return wrapper.convertIfNecessary(value, requiredType);
    }

    @Override
	public <Y> Y convertIfNecessary(Object value, Class<Y> requiredType,
            MethodParameter methodParam) throws TypeMismatchException {
        return wrapper.convertIfNecessary(value, requiredType, methodParam);
    }

    @Override
	public <Y> Y convertIfNecessary(Object value, Class<Y> requiredType, Field field) throws TypeMismatchException {
        return wrapper.convertIfNecessary(value, requiredType, field);
    }

    @Override
    public Map<String, Object> getPrimaryKeyValues() {
        Map<String, Object> primaryKeyValues = new HashMap<String, Object>();
		if (metadata != null) {
			List<String> primaryKeyAttributeNames = metadata.getPrimaryKeyAttributeNames();
			if (primaryKeyAttributeNames != null) {
				for (String primaryKeyAttributeName : primaryKeyAttributeNames) {
					primaryKeyValues.put(primaryKeyAttributeName, getPropertyValue(primaryKeyAttributeName));
				}
			}
		} else {
			LOG.warn("Attempt to retrieve PK fields on object with no metadata: " + dataObject.getClass().getName());
        }
        return primaryKeyValues;
    }

    @Override
    public Object getPrimaryKeyValue() {
        if (!areAllPrimaryKeyAttributesPopulated()) {
            return null;
        }
        Map<String, Object> primaryKeyValues = getPrimaryKeyValues();
        if (getPrimaryKeyValues().size() == 1) {
            return primaryKeyValues.values().iterator().next();
        } else {
            return new CompoundKey(primaryKeyValues);
        }
    }

	@Override
	public boolean areAllPrimaryKeyAttributesPopulated() {
		if (metadata != null) {
			List<String> primaryKeyAttributeNames = metadata.getPrimaryKeyAttributeNames();
			if (primaryKeyAttributeNames != null) {
				for (String primaryKeyAttributeName : primaryKeyAttributeNames) {
					Object propValue = getPropertyValue(primaryKeyAttributeName);
					if (propValue == null || (propValue instanceof String && StringUtils.isBlank((String) propValue))) {
						return false;
					}
				}
			}
			return true;
		} else {
			LOG.warn("Attempt to check areAllPrimaryKeyAttributesPopulated on object with no metadata: "
					+ dataObject.getClass().getName());
			return true;
		}
	}

	@Override
	public boolean areAnyPrimaryKeyAttributesPopulated() {
		if (metadata != null) {
			List<String> primaryKeyAttributeNames = metadata.getPrimaryKeyAttributeNames();
			if (primaryKeyAttributeNames != null) {
				for (String primaryKeyAttributeName : primaryKeyAttributeNames) {
					Object propValue = getPropertyValue(primaryKeyAttributeName);
					if (propValue instanceof String && StringUtils.isNotBlank((String) propValue)) {
						return true;
					} else if (propValue != null) {
						return true;
					}
				}
			}
			return false;
		} else {
			LOG.warn("Attempt to check areAnyPrimaryKeyAttributesPopulated on object with no metadata: "
					+ dataObject.getClass().getName());
			return true;
		}
	}

	@Override
	public List<String> getUnpopulatedPrimaryKeyAttributeNames() {
		List<String> emptyKeys = new ArrayList<String>();
		if (metadata != null) {
			List<String> primaryKeyAttributeNames = metadata.getPrimaryKeyAttributeNames();
			if (primaryKeyAttributeNames != null) {
				for (String primaryKeyAttributeName : primaryKeyAttributeNames) {
					Object propValue = getPropertyValue(primaryKeyAttributeName);
					if (propValue == null || (propValue instanceof String && StringUtils.isBlank((String) propValue))) {
						emptyKeys.add(primaryKeyAttributeName);
					}
				}
			}
		} else {
			LOG.warn("Attempt to check getUnpopulatedPrimaryKeyAttributeNames on object with no metadata: "
					+ dataObject.getClass().getName());
		}
		return emptyKeys;
	}

    @Override
    public boolean equalsByPrimaryKey(T object) {
        if (object == null) {
            return false;
        }
        DataObjectWrapper<T> wrap = dataObjectService.wrap(object);
        if (!getWrappedClass().isAssignableFrom(wrap.getWrappedClass())) {
            throw new IllegalArgumentException("The type of the given data object does not match the type of this " +
                    "data object. Given: " + wrap.getWrappedClass() + ", but expected: " + getWrappedClass());
        }
        // since they are the same type, we know they must have the same number of primary keys,
        Map<String, Object> localPks = getPrimaryKeyValues();
        Map<String, Object> givenPks = wrap.getPrimaryKeyValues();
        for (String localPk : localPks.keySet()) {
            Object localPkValue = localPks.get(localPk);
            if (localPkValue == null || !localPkValue.equals(givenPks.get(localPk))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Object getForeignKeyValue(String relationshipName) {
        Object foreignKeyAttributeValue = getForeignKeyAttributeValue(relationshipName);
        if (foreignKeyAttributeValue != null) {
            return foreignKeyAttributeValue;
        }
        // if there are no attribute relationships, or the attribute relationships are not fully populated, fall
        // back to the actual relationship object
        Object relationshipObject = getPropertyValue(relationshipName);
        if (relationshipObject == null) {
            return null;
        }
        return dataObjectService.wrap(relationshipObject).getPrimaryKeyValue();
    }

    @Override
    public Object getForeignKeyAttributeValue(String relationshipName) {
		Map<String, Object> attributeMap = getForeignKeyAttributeMap(relationshipName);
		if (attributeMap == null) {
			return null;
		}
		return asSingleKey(attributeMap);
	}

	public Map<String, Object> getForeignKeyAttributeMap(String relationshipName) {
		MetadataChild relationship = findAndValidateRelationship(relationshipName);
        List<DataObjectAttributeRelationship> attributeRelationships = relationship.getAttributeRelationships();

        if (!attributeRelationships.isEmpty()) {
            Map<String, Object> attributeMap = new LinkedHashMap<String, Object>();

            for (DataObjectAttributeRelationship attributeRelationship : attributeRelationships) {
                // obtain the property value on the current parent object
                String parentAttributeName = attributeRelationship.getParentAttributeName();
                Object parentAttributeValue = getPropertyValue(parentAttributeName);

                // not all of our relationships are populated, so we cannot obtain a valid foreign key
                if (parentAttributeValue == null) {
                    return null;
                }

                // store the mapping with the child attribute name to fetch on the referenced child object
                String childAttributeName = attributeRelationship.getChildAttributeName();
                attributeMap.put(childAttributeName, parentAttributeValue);
            }

            return attributeMap;
        }

        return null;
    }


    private Object asSingleKey(Map<String, Object> keyValues) {
        if (keyValues.size() == 1) {
            return keyValues.values().iterator().next();
        }
        return new CompoundKey(keyValues);
    }

    @Override
    public Class<?> getPropertyTypeNullSafe(Class<?> objectType, String propertyName) {
        DataObjectMetadata objectMetadata = dataObjectService.getMetadataRepository().getMetadata(objectType);
        return getPropertyTypeChild(objectMetadata,propertyName);
    }

    private Class<?> getPropertyTypeChild(DataObjectMetadata objectMetadata, String propertyName){
        if(PropertyAccessorUtils.isNestedOrIndexedProperty(propertyName)){
            String attributePrefix = StringUtils.substringBefore(propertyName,".");
            String attributeName = StringUtils.substringAfter(propertyName,".");

            if(StringUtils.isNotBlank(attributePrefix) && StringUtils.isNotBlank(attributeName) &&
                    objectMetadata!= null){
                Class<?> propertyType = traverseRelationship(objectMetadata,attributePrefix,attributeName);
                if(propertyType != null){
                    return propertyType;
                }
            }
        }
        return getPropertyType(propertyName);
    }

    private Class<?> traverseRelationship(DataObjectMetadata objectMetadata,String attributePrefix,
                                          String attributeName){
        DataObjectRelationship rd = objectMetadata.getRelationship(attributePrefix);
        if(rd != null){
            DataObjectMetadata relatedObjectMetadata =
                    dataObjectService.getMetadataRepository().getMetadata(rd.getRelatedType());
            if(relatedObjectMetadata != null){
                if(PropertyAccessorUtils.isNestedOrIndexedProperty(attributeName)){
                    return getPropertyTypeChild(relatedObjectMetadata,attributeName);
                } else{
                    if(relatedObjectMetadata.getAttribute(attributeName) == null &&
                            relatedObjectMetadata.getRelationship(attributeName)!=null){
                        DataObjectRelationship relationship = relatedObjectMetadata.getRelationship(attributeName);
                        return relationship.getRelatedType();
                    }
                    return relatedObjectMetadata.getAttribute(attributeName).getDataType().getType();
                }
            }
        }
        return null;
    }

    @Override
    public void linkChanges(Set<String> changedPropertyPaths) {
        referenceLinker.linkChanges(getWrappedInstance(), changedPropertyPaths);
    }

    @Override
    public void linkForeignKeys(boolean onlyLinkReadOnly) {
        linkForeignKeysInternalWrapped(this, onlyLinkReadOnly, Sets.newHashSet());
    }

    protected void linkForeignKeysInternal(Object object, boolean onlyLinkReadOnly, Set<Object> linked) {
        if (object == null || linked.contains(object) || !dataObjectService.supports(object.getClass())) {
            return;
        }
        linked.add(object);
        DataObjectWrapper<?> wrapped = dataObjectService.wrap(object);
        linkForeignKeysInternalWrapped(wrapped, onlyLinkReadOnly, linked);
    }

    protected void linkForeignKeysInternalWrapped(DataObjectWrapper<?> wrapped, boolean onlyLinkReadOnly, Set<Object> linked) {
        List<DataObjectRelationship> relationships = wrapped.getMetadata().getRelationships();
        for (DataObjectRelationship relationship : relationships) {
            String relationshipName = relationship.getName();
            Object relationshipValue = wrapped.getPropertyValue(relationshipName);

            // let's get the current value and recurse down if it's a relationship that is cascaded on save
            if (relationship.isSavedWithParent()) {

                linkForeignKeysInternal(relationshipValue, onlyLinkReadOnly, linked);
            }

            // next, if we have related attributes, we need to link our keys
            linkForeignKeysInternal(wrapped, relationship, relationshipValue, onlyLinkReadOnly);
        }
        List<DataObjectCollection> collections = wrapped.getMetadata().getCollections();
        for (DataObjectCollection collection : collections) {
            String relationshipName = collection.getName();

            // let's get the current value and recurse down for each element if it's a collection that is cascaded on save
            if (collection.isSavedWithParent()) {
                Collection<?> collectionValue = (Collection<?>)wrapped.getPropertyValue(relationshipName);
                if (collectionValue != null) {
                    for (Object object : collectionValue) {
                        linkForeignKeysInternal(object, onlyLinkReadOnly, linked);
                    }
                }
            }
        }

    }

    @Override
    public void fetchRelationship(String relationshipName) {
        fetchRelationship(relationshipName, true, true);
    }

    @Override
    public void fetchRelationship(String relationshipName, boolean useForeignKeyAttribute, boolean nullifyDanglingRelationship) {
        fetchRelationship(findAndValidateRelationship(relationshipName), useForeignKeyAttribute, nullifyDanglingRelationship);
    }

	protected void fetchRelationship(MetadataChild relationship, boolean useForeignKeyAttribute, boolean nullifyDanglingRelationship) {
        Class<?> relatedType = relationship.getRelatedType();
        if (!dataObjectService.supports(relatedType)) {
            LOG.warn("Encountered a related type that is not supported by DataObjectService, fetch "
                    + "relationship will do nothing: " + relatedType);
            return;
        }
        // if we have at least one attribute relationships here, then we are set to proceed
        if (useForeignKeyAttribute) {
            fetchRelationshipUsingAttributes(relationship, nullifyDanglingRelationship);
        } else {
            fetchRelationshipUsingIdentity(relationship, nullifyDanglingRelationship);
        }
    }

    protected void fetchRelationshipUsingAttributes(MetadataChild relationship, boolean nullifyDanglingRelationship) {
        Class<?> relatedType = relationship.getRelatedType();
        if (relationship.getAttributeRelationships().isEmpty()) {
            throw new IllegalArgumentException("Attempted to fetch a relationship using a foreign key attribute "
                    + "when one does not exist: " + relationship.getName());
        } else {
            Object fetchedValue = null;
            if (relationship instanceof DataObjectRelationship) {
                Object foreignKey = getForeignKeyAttributeValue(relationship.getName());
                if (foreignKey != null) {
                    fetchedValue = dataObjectService.find(relatedType, foreignKey);
                }
            } else if (relationship instanceof DataObjectCollection) {
                Map<String, Object> foreignKeyAttributeMap = getForeignKeyAttributeMap(relationship.getName());
                fetchedValue = dataObjectService.findMatching(relatedType,
                        QueryByCriteria.Builder.andAttributes(foreignKeyAttributeMap).build()).getResults();
            }
            if (fetchedValue != null || nullifyDanglingRelationship) {
                setPropertyValue(relationship.getName(), fetchedValue);
            }
        }
    }

    protected void fetchRelationshipUsingIdentity(MetadataChild relationship, boolean nullifyDanglingRelationship) {
        Object propertyValue = getPropertyValue(relationship.getName());
        if (propertyValue != null) {
            if (!dataObjectService.supports(propertyValue.getClass())) {
                throw new IllegalArgumentException("Attempting to fetch an invalid relationship, must be a"
                        + "DataObjectRelationship when fetching without a foreign key");
            }
            DataObjectWrapper<?> wrappedRelationship = dataObjectService.wrap(propertyValue);
            Map<String, Object> primaryKeyValues = wrappedRelationship.getPrimaryKeyValues();
            Object newPropertyValue = dataObjectService.find(wrappedRelationship.getWrappedClass(),
                    new CompoundKey(primaryKeyValues));
            if (newPropertyValue != null || nullifyDanglingRelationship) {
                propertyValue = newPropertyValue;
                setPropertyValue(relationship.getName(), propertyValue);
            }
        }
        // now copy pk values back to the foreign key, because we are being explicity asked to fetch the relationship
        // using the identity and not the FK, we don't care about whether the FK field is read only or not so pass
        // "false" for onlyLinkReadOnly argument to linkForeignKeys
        linkForeignKeysInternal(this, relationship, propertyValue, false);
        populateInverseRelationship(relationship, propertyValue);
    }

    @Override
    public void linkForeignKeys(String relationshipName, boolean onlyLinkReadOnly) {
        MetadataChild relationship = findAndValidateRelationship(relationshipName);
        Object propertyValue = getPropertyValue(relationshipName);
        linkForeignKeysInternal(this, relationship, propertyValue, onlyLinkReadOnly);
    }

    protected void linkForeignKeysInternal(DataObjectWrapper<?> wrapped, MetadataChild relationship,
            Object relationshipValue, boolean onlyLinkReadOnly) {
        if (!relationship.getAttributeRelationships().isEmpty()) {
            // this means there's a foreign key so we need to copy values back
            DataObjectWrapper<?> wrappedRelationship = null;
            if (relationshipValue != null) {
                wrappedRelationship = dataObjectService.wrap(relationshipValue);
            }
            for (DataObjectAttributeRelationship attributeRelationship : relationship.getAttributeRelationships()) {
                String parentAttributeName = attributeRelationship.getParentAttributeName();
                // if the property value is null, we need to copy null back to all parent foreign keys,
                // otherwise we copy back the actual value
                Object childAttributeValue = null;
                if (wrappedRelationship != null) {
                    childAttributeValue =
                            wrappedRelationship.getPropertyValue(attributeRelationship.getChildAttributeName());
                }
                if (onlyLinkReadOnly) {
                    DataObjectAttribute attribute = wrapped.getMetadata().getAttribute(parentAttributeName);
                    if (attribute.isReadOnly()) {
                        wrapped.setPropertyValue(parentAttributeName, childAttributeValue);
                    }
                } else {
                    wrapped.setPropertyValue(parentAttributeName, childAttributeValue);
                }
            }
        }
    }

    protected void populateInverseRelationship(MetadataChild relationship, Object propertyValue) {
        if (propertyValue != null) {
            MetadataChild inverseRelationship = relationship.getInverseRelationship();
            if (inverseRelationship != null) {
                DataObjectWrapper<?> wrappedRelationship = dataObjectService.wrap(propertyValue);
                if (inverseRelationship instanceof DataObjectCollection) {
                    DataObjectCollection collectionRelationship = (DataObjectCollection)inverseRelationship;
                    String colRelName = inverseRelationship.getName();
                    Collection<Object> collection =
                            (Collection<Object>)wrappedRelationship.getPropertyValue(colRelName);
                    if (collection == null) {
                        // if the collection is null, let's instantiate an empty one
                        collection =
                                CollectionFactory.createCollection(wrappedRelationship.getPropertyType(colRelName), 1);
                        wrappedRelationship.setPropertyValue(colRelName, collection);
                    }
                    collection.add(getWrappedInstance());
                }
            }
        }
    }

	private MetadataChild findAndValidateRelationship(String relationshipName) {
        if (StringUtils.isBlank(relationshipName)) {
            throw new IllegalArgumentException("The relationshipName must not be null or blank");
        }
        // validate the relationship exists
		MetadataChild relationship = getMetadata().getRelationship(relationshipName);
        if (relationship == null) {
			relationship = getMetadata().getCollection(relationshipName);
			if (relationship == null) {
				throw new IllegalArgumentException("Failed to locate a valid relationship from " + getWrappedClass()
						+ " with the given relationship name '" + relationshipName + "'");
			}
        }
        return relationship;
    }

}

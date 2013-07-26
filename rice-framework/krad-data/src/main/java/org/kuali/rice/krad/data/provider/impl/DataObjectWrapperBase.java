/*
 * Copyright 2006-2013 The Kuali Foundation
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

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.data.DataObjectService;
import org.kuali.rice.krad.data.DataObjectWrapper;
import org.kuali.rice.krad.data.metadata.DataObjectMetadata;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.NullValueInNestedPathException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;

import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class DataObjectWrapperBase<T> implements DataObjectWrapper<T> {

    private final T dataObject;
    private final DataObjectMetadata metadata;
    private final BeanWrapper wrapper;
    private final DataObjectService dataObjectService;

    protected DataObjectWrapperBase(T dataObject, DataObjectMetadata metadata, DataObjectService dataObjectService) {
        this.dataObject = dataObject;
        this.metadata = metadata;
        this.dataObjectService = dataObjectService;
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
    public <T> T convertIfNecessary(Object value, Class<T> requiredType) throws TypeMismatchException {
        return wrapper.convertIfNecessary(value, requiredType);
    }

    @Override
    public <T> T convertIfNecessary(Object value, Class<T> requiredType,
            MethodParameter methodParam) throws TypeMismatchException {
        return wrapper.convertIfNecessary(value, requiredType, methodParam);
    }

    @Override
    public <T> T convertIfNecessary(Object value, Class<T> requiredType, Field field) throws TypeMismatchException {
        return wrapper.convertIfNecessary(value, requiredType, field);
    }

    @Override
    public Map<String, Object> getPrimaryKeyValues() {
        Map<String, Object> primaryKeyValues = new HashMap<String, Object>();
        List<String> primaryKeyAttributeNames = metadata.getPrimaryKeyAttributeNames();
        if (primaryKeyAttributeNames != null) {
            for (String primaryKeyAttributeName : primaryKeyAttributeNames) {
                primaryKeyValues.put(primaryKeyAttributeName, getPropertyValue(primaryKeyAttributeName));
            }
        }
        return primaryKeyValues;
    }

	@Override
	public boolean areAllPrimaryKeyAttributesPopulated() {
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
	}

	@Override
	public boolean areAnyPrimaryKeyAttributesPopulated() {
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
	}

	@Override
	public List<String> getUnpopulatedPrimaryKeyAttributeNames() {
		List<String> emptyKeys = new ArrayList<String>();
		List<String> primaryKeyAttributeNames = metadata.getPrimaryKeyAttributeNames();
		if (primaryKeyAttributeNames != null) {
			for (String primaryKeyAttributeName : primaryKeyAttributeNames) {
				Object propValue = getPropertyValue(primaryKeyAttributeName);
				if (propValue == null || (propValue instanceof String && StringUtils.isBlank((String) propValue))) {
					emptyKeys.add(primaryKeyAttributeName);
				}
			}
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

}

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
package org.kuali.rice.krad.util;

import org.junit.Test;
import org.kuali.rice.coreservice.impl.parameter.ParameterBo;
import org.kuali.rice.kns.web.struts.form.pojo.PojoPlugin;
import org.kuali.rice.kns.web.struts.form.pojo.PojoPropertyUtilsBean;
import org.kuali.rice.krad.bo.DocumentAttachment;
import org.kuali.rice.krad.bo.MultiDocumentAttachment;
import org.kuali.rice.krad.datadictionary.AttributeDefinition;
import org.kuali.rice.krad.datadictionary.DataDictionaryEntryBase;
import org.kuali.rice.krad.maintenance.MaintenanceDocumentBase;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.test.KRADTestCase;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * ObjectUtilsTest
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ObjectUtilsTest extends KRADTestCase {
    @Test
    public void testObjectUtils_equalsByKey() throws Exception {
        ParameterBo parameterInDB = new ParameterBo();
        parameterInDB.setNamespaceCode("KR-NS");
        parameterInDB.setName("OBJ_UTIL_TEST");
        
        ParameterBo parameterNew = new ParameterBo();
        parameterNew.setNamespaceCode("KR-NS");
        parameterInDB.setName(null);
        
        boolean equalsResult = false;
        equalsResult = ObjectUtils.equalByKeys(parameterInDB, parameterNew);
        assertFalse(equalsResult);
    }
    
/*	@Test
	public void testGetFormatterWithDataDictionary() throws Exception {
		// test formatter getting correctly pulled from data dictionary
		TravelAccountUseRate useRate = new TravelAccountUseRate();
		Formatter formatter = ObjectUtils.getFormatterWithDataDictionary(useRate, "active");
		assertTrue("Incorrect formatter returned for active property", formatter instanceof BooleanFormatter);

		changeAttributeDefinitionFormatter(useRate.getClass(), "active", IntegerFormatter.class);
		formatter = ObjectUtils.getFormatterWithDataDictionary(useRate, "active");
		assertTrue("Incorrect formatter returned for active property", formatter instanceof IntegerFormatter);
		
		// test formatter getting correctly pulled by data type
		formatter = ObjectUtils.getFormatterWithDataDictionary(useRate, "activeFromDate");
		assertTrue("Incorrect formatter returned for date type", formatter instanceof DateFormatter);
		
		formatter = ObjectUtils.getFormatterWithDataDictionary(useRate, "rate");
		assertTrue("Incorrect formatter returned for percent type", formatter instanceof PercentageFormatter);
		
		formatter = ObjectUtils.getFormatterWithDataDictionary(useRate, "number");
		assertTrue("Incorrect formatter returned for string type", formatter.getClass().getName().equals("org.kuali.rice.core.web.format.Formatter"));
	}
*/
	private void changeAttributeDefinitionFormatter(Class boClass, String attributeName, Class formatterClass) {
		DataDictionaryEntryBase entry = (DataDictionaryEntryBase) KRADServiceLocatorWeb.getDataDictionaryService()
				.getDataDictionary().getDictionaryObjectEntry(boClass.getName());
		if (entry != null) {
			AttributeDefinition attributeDefinition = entry.getAttributeDefinition(attributeName);
			attributeDefinition.setFormatterClass(formatterClass.getName());
		}
	}

    @Test
    public void testMissingNestedObjectCreation() throws Exception {
        PojoPlugin.initBeanUtils();
        MaintenanceDocumentBase m = new MaintenanceDocumentBase();
        m.setAttachments(new ArrayList<MultiDocumentAttachment>());
        assertNotNull(m.getAttachments());
        Object o = ObjectUtils.getPropertyValue(m, "attachments[0]");
        assertNotNull(o);
        assertTrue(o instanceof MultiDocumentAttachment);
    }

    @Test
    public void testInvalidOJBCollection() {
        // abcd is not a collection (or any other) property
        assertNull(new PojoPropertyUtilsBean.OJBCollectionItemClassProvider().getCollectionItemClass(new MaintenanceDocumentBase(), "abcd"));
        // attachment is a valid property, but not a collection
        assertNull(new PojoPropertyUtilsBean.OJBCollectionItemClassProvider().getCollectionItemClass(new MaintenanceDocumentBase(), "attachment"));
        // attachmentContent is an array
        assertNull(new PojoPropertyUtilsBean.OJBCollectionItemClassProvider().getCollectionItemClass(new DocumentAttachment(), "attachmentContent"));
    }

}

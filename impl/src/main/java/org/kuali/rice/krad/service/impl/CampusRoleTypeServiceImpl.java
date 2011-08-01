/*
 * Copyright 2008-2009 The Kuali Foundation
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

import org.kuali.rice.core.api.uif.RemotableAttributeField;
import org.kuali.rice.kim.api.type.KimAttributeField;
import org.kuali.rice.kim.service.support.impl.KimRoleTypeServiceBase;
import org.kuali.rice.kim.util.KimConstants;

import java.util.ArrayList;
import java.util.List;

public class CampusRoleTypeServiceImpl extends KimRoleTypeServiceBase {

	{
		workflowRoutingAttributes.add( KimConstants.AttributeConstants.CAMPUS_CODE );
		requiredAttributes.add( KimConstants.AttributeConstants.CAMPUS_CODE );
	}

	@Override
	public List<KimAttributeField> getAttributeDefinitions(String kimTypeId) {
		List<KimAttributeField> map = new ArrayList<KimAttributeField>(super.getAttributeDefinitions(kimTypeId));

		for (int i = 0; i < map.size(); i++) {
            final KimAttributeField definition = map.get(i);
			if (KimConstants.AttributeConstants.CAMPUS_CODE.equals(definition.getAttributeField().getName())) {
				KimAttributeField.Builder b = KimAttributeField.Builder.create(definition);

                RemotableAttributeField.Builder fb =  b.getAttributeField();
                fb.setRequired(true);

                b.setAttributeField(fb);
                map.set(i, b.build());
			}
		}
		return map;
	}
}

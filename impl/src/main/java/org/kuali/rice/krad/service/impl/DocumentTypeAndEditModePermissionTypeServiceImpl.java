/*
 * Copyright 2007-2008 The Kuali Foundation
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

import org.kuali.rice.core.api.mo.common.Attributes;
import org.kuali.rice.kim.api.permission.Permission;
import org.kuali.rice.kim.impl.permission.PermissionBo;
import org.kuali.rice.kim.util.KimConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocumentTypeAndEditModePermissionTypeServiceImpl extends DocumentTypePermissionTypeServiceImpl {
	
	{
		requiredAttributes.add(KimConstants.AttributeConstants.EDIT_MODE);
	}
	
	@Override
	protected List<Permission> performPermissionMatches(
			Attributes requestedDetails,
			List<Permission> permissionsList) {
		List<Permission> matchingPermissions = new ArrayList<Permission>();
		for (Permission permission : permissionsList) {
            PermissionBo bo = PermissionBo.from(permission);
			if (requestedDetails.get(KimConstants.AttributeConstants.EDIT_MODE).equals(bo.getDetails().get(KimConstants.AttributeConstants.EDIT_MODE))) {
				matchingPermissions.add(permission);
			}
		}
		return super.performPermissionMatches(requestedDetails, matchingPermissions);
	}
}

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
package org.kuali.rice.krms.framework.type;

import org.kuali.rice.krms.api.repository.action.ActionDefinition;
import org.kuali.rice.krms.framework.engine.Action;

/**
 * ValidationAction constants and mocking of the {@link ValidationActionService}.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface ValidationActionTypeService extends RemotableAttributeOwner, ActionTypeService {

    static public final String VALIDATIONS_ACTION_ATTRIBUTE = "validations";
    static public final String ATTRIBUTE_FIELD_NAME = "validationId";

    @Override
	public Action loadAction(ActionDefinition actionDefinition);

    /**
     * Set the {@link ValidationActionService}.
     *
     * @param validationService the {@link ValidationActionService} to use.
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException if the given Validation Service is null
     */
    void setValidationService(ValidationActionService validationService);
}

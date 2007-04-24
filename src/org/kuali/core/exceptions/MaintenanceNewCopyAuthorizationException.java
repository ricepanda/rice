/*
 * Copyright 2006 The Kuali Foundation.
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
package org.kuali.core.exceptions;

import org.kuali.KeyConstants;

/**
 * This class represents an exception that is thrown when a maintenance document type does not allow
 * new or copy maintenance actions to be performed.
 */
public class MaintenanceNewCopyAuthorizationException extends DocumentTypeAuthorizationException {
    private static final long serialVersionUID = 1L;

    public MaintenanceNewCopyAuthorizationException(String documentType) {
        super("anybody", "newOrCopy", documentType);
    }

    /**
     * @see AuthorizationException#getErrorMessageKey()
     */
    public String getErrorMessageKey() {
        return KeyConstants.AUTHORIZATION_ERROR_MAINTENANCE_NEWCOPY;
    }
}

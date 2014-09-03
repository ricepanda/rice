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
package org.kuali.rice.krad.uif.view;

import org.kuali.rice.krad.datadictionary.DocumentEntry;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.util.LifecycleElement;

/**
 * View type for Transactional documents
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "transactionalDocumentView", parent = "Uif-TransactionalDocumentView")
public class TransactionalDocumentView extends DocumentView {
    private static final long serialVersionUID = 4375336878804984171L;

    public TransactionalDocumentView() {
        super();

        setViewTypeName(UifConstants.ViewType.TRANSACTIONAL);
    }

    /** Override to make sure the header text is set.
     *
     * @param model
     * @param parent
     */
    @Override
    public void performFinalize(Object model, LifecycleElement parent) {
        super.performFinalize(model, parent);

        if (this.getHeader() != null && this.getHeaderText().length() == 0) {
            DocumentEntry documentEntry = getDocumentEntryForView();
            this.setHeaderText(documentEntry.getDocumentTypeName());
        }
    }
}

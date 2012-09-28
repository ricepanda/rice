/*
 * Copyright 2006-2012 The Kuali Foundation
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
package edu.samplu.krad.travelview;

import edu.samplu.common.KradMenuLegacyITBase;
import org.junit.Test;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class MaintenanceNotesAndAttachmentsLegacyIT  extends KradMenuLegacyITBase {
    @Override
    protected String getLinkLocator() {
        return "Travel Account Maintenance (New)";
    }

    @Test
    /**
     * Verify Notes and Attachments section and fields exist
     */
    public void testVerifyNotesAndAttachments() throws Exception {
        gotoMenuLinkLocator();
        waitAndClickByXpath("//span[contains(text(),'Notes and Attachments')]");
        waitForElementPresentByXpath("//button[@title='Add a Note']");
        assertElementPresentByXpath("//span[contains(text(),'Notes and Attachments')]");
        assertElementPresentByXpath("//textarea[@name=\"newCollectionLines['document.notes'].noteText\"]");
        assertElementPresentByXpath("//input[@name='attachmentFile']");
        assertElementPresentByXpath("//input[@name=\"newCollectionLines['document.notes'].attachment.attachmentTypeCode\"]");
    }
}

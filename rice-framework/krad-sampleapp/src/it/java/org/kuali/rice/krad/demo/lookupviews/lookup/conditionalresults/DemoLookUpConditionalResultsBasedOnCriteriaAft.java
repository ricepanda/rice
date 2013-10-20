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
package org.kuali.rice.krad.demo.lookupviews.lookup.conditionalresults;

import org.junit.Assert;
import org.kuali.rice.testtools.selenium.AutomatedFunctionalTestBase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DemoLookUpConditionalResultsBasedOnCriteriaAft extends AutomatedFunctionalTestBase {

    /**
     * /kr-krad/lookup?methodToCall=start&viewId=LookupSampleViewConditionalResultsBasedOnCriteria
     */
    public static final String BOOKMARK_URL = "/kr-krad/lookup?methodToCall=start&viewId=LookupSampleViewConditionalResultsBasedOnCriteria";

    /**
     *  lookupCriteria[number]
     */
    private static final String LOOKUP_CRITERIA_TYPE_CODE="lookupCriteria[typeCode]";

    /**
     *  Search
     */
    private static final String SEARCH="Search";

    /**
     *  Principal Name column
     */
    private static final String PRINCIPAL_NAME_COLUMN_NAME = "Principal Name";

    /**
     *  Group Name column
     */
    private static final String GROUP_NAME_COLUMN_NAME = "Group Name";

    /**
     *  Role Name column
     */
    private static final String ROLE_NAME_COLUMN_NAME = "Role Name";

    /**
     *  Description column
     */
    private static final String DESCRIPTION_COLUMN_NAME = "Description";

    @Override
    public String getBookmarkUrl() {
        return BOOKMARK_URL;
    }

    @Override
    protected void navigate() throws Exception {
        waitAndClickById("Demo-DemoLink", "");
        waitAndClickByLinkText("Lookup Conditional Results Based On Criteria");
    }

    protected void testLookUpConditionalResultsBasedOnCriteria() throws InterruptedException {
        // Case 1 - Search by Principal
        waitAndTypeByName(LOOKUP_CRITERIA_TYPE_CODE,"P");
        waitAndClickButtonByText(SEARCH);
        Thread.sleep(3000);
        Assert.assertEquals(1, getCssCount("div#uLookupResults thead th"));
        assertElementPresent("div#uLookupResults thead th:nth-child(1)");
        assertTextPresent(PRINCIPAL_NAME_COLUMN_NAME, "div#uLookupResults thead th:nth-child(1) label",
                "Principal Name column not present");

        // Case 2 - Search by Group
        waitAndTypeByName(LOOKUP_CRITERIA_TYPE_CODE, "G");
        waitAndClickButtonByText(SEARCH);
        Thread.sleep(3000);
        Assert.assertEquals(2, getCssCount("div#uLookupResults thead th"));
        assertElementPresent("div#uLookupResults thead th:nth-child(1)");
        assertTextPresent(GROUP_NAME_COLUMN_NAME, "div#uLookupResults thead th:nth-child(1) label",
                "Group Name column not present");
        assertElementPresent("div#uLookupResults thead th:nth-child(2)");
        assertTextPresent(DESCRIPTION_COLUMN_NAME, "div#uLookupResults thead th:nth-child(2) label",
                "Description column not present");

        // Case 3 - Search by Role
        waitAndTypeByName(LOOKUP_CRITERIA_TYPE_CODE, "R");
        waitAndClickButtonByText(SEARCH);
        Thread.sleep(3000);
        Assert.assertEquals(2, getCssCount("div#uLookupResults thead th"));
        assertTextPresent(ROLE_NAME_COLUMN_NAME, "div#uLookupResults thead th:nth-child(1) label",
                "Role Name column not present");
        assertElementPresent("div#uLookupResults thead th:nth-child(2)");
        assertTextPresent(DESCRIPTION_COLUMN_NAME, "div#uLookupResults thead th:nth-child(2) label",
                "Description column not present");
    }

    @Test
    public void testLookUpConditionalResultsBasedOnCriteriaBookmark() throws Exception {
        testLookUpConditionalResultsBasedOnCriteria();
        passed();
    }

    @Test
    public void testLookUpConditionalResultsBasedOnCriteriaNav() throws Exception {
        testLookUpConditionalResultsBasedOnCriteria();
        passed();
    }
}

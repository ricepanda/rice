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
package edu.samplu.krad.labs.lookups;

import org.junit.Test;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */

public class DemoLabsLookupDefaultSmokeTest extends DemoLabsLookupBase {

    /**
     * /kr-krad/lookup?methodToCall=start&viewId=LabsLookup-DefaultView&hideReturnLink=true
     */
    public static final String BOOKMARK_URL = "/kr-krad/lookup?methodToCall=start&viewId=LabsLookup-DefaultView&hideReturnLink=true";
    
    @Override
    protected String getBookmarkUrl() {
        return BOOKMARK_URL;
    }

    @Override
    protected void navigate() throws Exception {
        navigateToLookup("Lookup Default");
    }

    @Test
    public void testLabsLookupDefaultBookmark() throws Exception {
        testLabsLookupDefault();
        passed();
    }

    @Test
    public void testLabsLookupDefaultNav() throws Exception {
        testLabsLookupDefault();
        passed();
    }
    
    protected void testLabsLookupDefault()throws Exception {
        waitAndTypeByName("lookupCriteria[number]","a1*");
        waitAndTypeByName("lookupCriteria[name]","Travel *");
        waitAndClickButtonByText("Search");
        Thread.sleep(3000);
        assertTextPresent("TRAVEL ACCOUNT 14");
        assertTextPresent("a14");
        waitAndClickButtonByText("Clear Values");
        waitAndClickButtonByText("Search");
        Thread.sleep(3000);
        assertTextPresent("Travel Account 1");
        assertTextPresent("a1");
    }
}

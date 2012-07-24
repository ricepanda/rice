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
package edu.samplu.mainmenu.test;

import static org.junit.Assert.assertTrue;

import edu.samplu.common.UpgradedSeleniumITBase;
import org.junit.Test;

/**
 * tests that user 'admin' can display the Term lookup screen, search,
 * initiate an Term maintenance document via an edit action on the search results and
 * finally cancel the maintenance document
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class TermLookUpIT extends UpgradedSeleniumITBase {
    @Override
    public String getTestUrl() {
        return PORTAL;
    }
    
    @Test
    public void testTermLookUp() throws Exception {
        selenium.open(System.getProperty("remote.public.url"));
		selenium.type("name=__login_user", "admin");
		selenium.click("css=input[type=\"submit\"]");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Term Lookup");
		selenium.waitForPageToLoad("30000");
		selenium.selectFrame("iframeportlet");
		selenium.click("//button");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=edit");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent("cancel"));
		selenium.click("link=cancel");
		selenium.waitForPageToLoad("30000");
    }
}

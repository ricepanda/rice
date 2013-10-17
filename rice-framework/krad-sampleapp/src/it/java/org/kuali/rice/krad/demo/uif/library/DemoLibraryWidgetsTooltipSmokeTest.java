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
package org.kuali.rice.krad.demo.uif.library;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DemoLibraryWidgetsTooltipSmokeTest extends DemoLibraryBase {

	 /**
     * /kr-krad/kradsampleapp?viewId=Demo-Tooltip-View&methodToCall=start
     */
    public static final String BOOKMARK_URL = "/kr-krad/kradsampleapp?viewId=Demo-Tooltip-View&methodToCall=start";

    @Override
    protected String getBookmarkUrl() {
        return BOOKMARK_URL;
    }

    @Override
    protected void navigate() throws Exception {
        navigateToLibraryDemo("Widgets", "Tooltip");
    }

    protected void testWidgetsTooltipHover() throws Exception {
        fireMouseOverEventByName("dataField1");
        assertElementPresentByXpath("//td[@class='jquerybubblepopup-innerHtml' and contains(text(),'This is a helpful tooltip about this field')]");
       
    }

    protected void testWidgetsTooltipFocus() throws Exception {
        selectByName("exampleShown","Tooltip On Focus");
        waitAndTypeByXpath("//div[@id='Demo-Tooltip-Example2']/div[@class='uif-verticalBoxLayout clearfix']/div/input[@name='dataField1']","");
        assertElementPresentByXpath("//td[@class='jquerybubblepopup-innerHtml' and contains(text(),'This tooltip appears when the field receives focus')]");
    }

    @Test
    public void testWidgetsTooltipBookmark() throws Exception {
        testWidgetsTooltipHover();
        testWidgetsTooltipFocus();
        driver.close();
        passed();
    }

    @Test
    public void testWidgetsTooltipNav() throws Exception {
        testWidgetsTooltipHover();
        testWidgetsTooltipFocus();
        driver.close();
        passed();
    }
}

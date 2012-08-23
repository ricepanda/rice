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

package edu.samplu.krad.configview;

import java.util.Calendar;

import edu.samplu.common.UpgradedSeleniumITBase;
import org.junit.Assert;
import org.junit.Test;

/**
 * Selenium test that tests collections
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class CollectionsIT extends UpgradedSeleniumITBase {

    @Override
    public String getTestUrl() {
        return "/kr-krad/uicomponents?viewId=ConfigurationTestView-Collections&methodToCall=start";
    }

    /**
     * Test adding a column of values to the Default Tests Table Layout
     */
    @Test
    public void testDefaultTestsTableLayout() throws Exception{
        //Thread.sleep(30000);
        Assert.assertTrue(selenium.isTextPresent("Default Tests"));
        Assert.assertTrue(selenium.isTextPresent("Table Layout"));
        Assert.assertTrue(selenium.isTextPresent("* Field 1"));
        Assert.assertTrue(selenium.isTextPresent("* Field 2"));
        Assert.assertTrue(selenium.isTextPresent("* Field 3"));
        Assert.assertTrue(selenium.isTextPresent("* Field 4"));
        Assert.assertTrue(selenium.isTextPresent("Actions"));

        selenium.type("name=newCollectionLines['list1'].field1", "asdf1");
        selenium.type("name=newCollectionLines['list1'].field2", "asdf2");
        selenium.type("name=newCollectionLines['list1'].field3", "asdf3");
        selenium.type("name=newCollectionLines['list1'].field4", "asdf4");
        selenium.click("//button[contains(.,'add')]"); // the first button is the one we want
       
        for (int second = 0;; second++) {
            if (second >= 60) Assert.fail("timeout");
            try { if (selenium.getValue("name=newCollectionLines['list1'].field1").equals("")) break; } catch (Exception e) {}
            Thread.sleep(1000);
        }
        
        Assert.assertEquals("", selenium.getValue("name=newCollectionLines['list1'].field1"));
        Assert.assertEquals("", selenium.getValue("name=newCollectionLines['list1'].field2"));
        Assert.assertEquals("", selenium.getValue("name=newCollectionLines['list1'].field3"));
        Assert.assertEquals("", selenium.getValue("name=newCollectionLines['list1'].field4"));
        Assert.assertEquals("asdf1", selenium.getValue("name=list1[0].field1"));
        Assert.assertEquals("asdf2", selenium.getValue("name=list1[0].field2"));
        Assert.assertEquals("asdf3", selenium.getValue("name=list1[0].field3"));
        Assert.assertEquals("asdf4", selenium.getValue("name=list1[0].field4"));

        // how to figure out which delete button for the one we just added?
    }

    /**
     * Test action column placement in table layout collections
     */
    @Test
    public void testActionColumnPlacement() throws Exception {

        //Lack of proper locators its not possible to uniquely identify/locate this elements without use of ID's.
        //This restricts us to use the XPath to locate elements from the dome. 
        //This test is prone to throw error in case of any changes in the dom Html graph.
        
        
        
        selenium.click("link=Column Sequence");
        Thread.sleep(2000);
        //selenium.click("css=div.jGrowl-close");
        // check if actions column RIGHT by default
        //Assert.assertTrue(selenium.isElementPresent("//div[@id='ConfigurationTestView-collection1']//tr[2]/td[6]//button[contains(.,\"delete\")]"));
        for (int second = 0;; second++) {
            if (second >= 60) Assert.fail("timeout");
            try { if (selenium.isElementPresent("//tr[2]/td[6]/div/fieldset/div/div[2]/button")) break; } catch (Exception e) {}
            Thread.sleep(1000);
        }
        Assert.assertTrue(selenium.isElementPresent("//tr[2]/td[6]/div/fieldset/div/div[2]/button"));

        // check if actions column is LEFT
        //Assert.assertTrue(selenium.isElementPresent("//div[@id='ConfigurationTestView-collection2']//tr[2]/td[1]//button[contains(.,\"delete\")]"));
        for (int second = 0;; second++) {
            if (second >= 60) Assert.fail("timeout");
            try { if (selenium.isElementPresent("//div[2]/div[2]/div[2]/table/tbody/tr[2]/td/div/fieldset/div/div[2]/button")) break; } catch (Exception e) {}
            Thread.sleep(1000);
        }
        Assert.assertTrue(selenium.isElementPresent("//div[2]/div[2]/div[2]/table/tbody/tr[2]/td/div/fieldset/div/div[2]/button"));
        
        // check if actions column is 3rd in a sub collection
        //Assert.assertTrue(selenium.isElementPresent("//div[@id='ConfigurationTestView-subCollection2_line0']//tr[2]/td[3]//button[contains(.,\"delete\")]"));
        for (int second = 0;; second++) {
            if (second >= 60) Assert.fail("timeout");
            try { if (selenium.isElementPresent("//tr[2]/td[3]/div/fieldset/div/div[2]/button")) break; } catch (Exception e) {}
            Thread.sleep(1000);
        }
        Assert.assertTrue(selenium.isElementPresent("//tr[2]/td[3]/div/fieldset/div/div[2]/button"));

        
    }
}

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
package edu.samplu.common;

import edu.samplu.admin.test.ComponentSmokeTest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created as a way to link Rice Smoke Test failures to existing Jiras as a html link in Jenkins.  The more failures
 * the more useful it is to not have to keep tracking down the same Jiras.  Having this feature for Integration Tests
 * as well would be a huge help for the QA team.
 * TODO:
 * <ol>
 *   <li>Integration Test integration.  ITs often fail by the 10s tracking down existing Jiras is a huge time sink.</li>
 *   <li>Possible Extraction of jiraMatches data to property file.</li>
 * </ol>
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class JiraAwareFailureUtil {
    /**
     * KULRICE-8823 Fix broken smoke tests in CI
     */
    public static final String KULRICE_8823_FIX_BROKEN_SMOKE_TESTS_IN_CI = "KULRICE-8823 Fix broken smoke tests in CI";

    /**
     * https://jira.kuali.org/browse/
     */
    public static final String JIRA_BROWSE_URL = "https://jira.kuali.org/browse/";

    static Map<String, String> jiraMatches;

    static {
        jiraMatches = new HashMap<String, String>();

        jiraMatches.put(
                ComponentSmokeTest.CREATE_NEW_DOCUMENT_NOT_SUBMITTED_SUCCESSFULLY_MESSAGE_TEXT + ComponentSmokeTest.FOR_TEST_MESSAGE,
                KULRICE_8823_FIX_BROKEN_SMOKE_TESTS_IN_CI);

        jiraMatches.put("//*[@id='u229']", KULRICE_8823_FIX_BROKEN_SMOKE_TESTS_IN_CI);

        jiraMatches.put("after clicking Expand All", "KULRICE-3833 KRAD Sampleapp (Travel) Account Inquiry Collapse all toggles all and Expand all does nothing");

        jiraMatches.put("Breadcrumb number 6", "KULRICE-10206 KRAD Demo Kitchen Sink Lookup tab throws ClassNotFoundorg.kuali.rice.krad.demo.travel.account.TravelAccount");

        jiraMatches.put("Lookupable is null", "KULRICE-10207 DemoTravelAccountMultivalueLookUpSmokeTest Lookupable is null.");

        jiraMatches.put("Account Maintenance (Edit)", "KULRICE-10216 KRAD Demo Account Maintenance (Edit) Incident Report RiceRuntimeException: Exception trying to invoke action ROUTE");

        jiraMatches.put("An exception occurred processing JSP page /kr/WEB-INF/jsp/KualiMaintenanceDocument.jsp", "KULRICE-10235 JasperException: An exception occurred processing JSP page /kr/WEB-INF/jsp/KualiMaintenanceDocument.jsp");

        jiraMatches.put("BusinessObjectMetaDataServiceImpl.getBusinessObjectRelationship(BusinessObjectMetaDataServiceImpl.java:267)", "KULRICE-10354 Identity links throws NullPointerException");

        jiraMatches.put("//table[@id='row']/tbody/tr[1]/td[1]/a", "KULRICE-10355 Investigate DocSearchWDIT testBasicDocSearch failure");

        jiraMatches.put("BusinessObjectDaoProxy called with non-legacy class: class org.kuali.rice.coreservice.impl.namespace.NamespaceBo", "KULRICE-10356 Agenda edit BusinessObjectDaoProxy called with non-legacy class: class org.kuali.rice.coreservice.impl.namespace.NamespaceBo");

        jiraMatches.put("annot get new instance of class to check for KualiCode", "KULRICE-10358 Component search Action List Incident Report Cannot get new instance of class to check for KualiCode");

        jiraMatches.put("Library Widget Suggest, CAT not suggested", "KULRICE-10365 Library Widget Suggest not suggesting");

        jiraMatches.put("img[src='/krad/images/validation/error.png']", "KULRICE-10372 DemoLibraryFieldsInputmokeTest mouse over error image fix");

        jiraMatches.put("table or view does not exist", "KULRICE-10381 2.4 SQL updates to resolve missing table exception in CI");

        jiraMatches.put("Stacked Collection With Table Sub-Collection", "KULRICE-10382 DemoLibraryLayoutManagersStackedLayoutSmokeTest update to use select drop down");

        jiraMatches.put("TopLink7", "KULRICE-10383 Demo Labs Kitchensink Collections freemarker exception in log when SQL updates have been applied");
        jiraMatches.put("list4[0].subList[0].field1", "KULRICE-10383 Demo Labs Kitchensink Collections freemarker exception in log when SQL updates have been applied");

        jiraMatches.put("document.newMaintainableObject.dataObject.extension.accountTypeCode", "KULRICE-10384 DemoTravelAccountMaintenanceNewSmokeTest update to use select for Type");

        jiraMatches.put("//div[@class='span9 uif-fixedCssGridLayout']/div/div[@class='span3']/div/input[@name='inputField1']", "KULRICE-10333 Update DemoLibraryLayoutManagersCssGridLayoutSmokeTest");

        jiraMatches.put("By.linkText: 1000", "KULRICE-10357 Attribute Lookup results no longer clickable");

        jiraMatches.put("BusinessObjectDaoProxy called with non-legacy class: class org.kuali.rice.coreservice.impl.namespace.NamespaceBo","KULRICE-10356 Agenda edit Incident Report BusinessObjectDaoProxy called with non-legacy class: class org.kuali.rice.coreservice.impl.namespace.NamespaceBo");

        jiraMatches.put("//div[@id='Demo-TableLayoutTotaling-Section1']/div/table/tfoot/tr/th[2]/div/fieldset/div/div[@class='uif-verticalBoxLayout clearfix']/div/span[@data-role]", "KULRICE-10402 Update DemoLibraryCollectionFeaturesColumnCalculationsSmokeTest which is failing");

        jiraMatches.put("Unable to decrypt value from db", "KULRICE-10403 Unable to decrypt value from db: Input length must be multiple of 8 when decrypting with padded ciphe");

        jiraMatches.put("org.kuali.rice.krad.uif.container.LightTable.buildRows", "KULRICE-10421 DemoLibraryCollectionFeaturesLightTableSmokeTest inline freemarker exception");

        jiraMatches.put("Invalid search field sent for property name: foId", "KULRICE-10422 DemoTravelAccountMultivalueLookUpSmokeTest Invalid search field sent for property name: foId");

        jiraMatches.put("org.kuali.rice.krad.uif.layout.TableLayoutManager.buildLine(TableLayoutManager.java:771)", "KULRICE-10429 NPE in Table Layout Manager when opening up Travel Account maintenance documents");

        jiraMatches.put("Document Expired", "KULRICE-9709 Search Edit Back does not show search results in Firefox");

        jiraMatches.put("org.kuali.rice.krad.uif.view.View cannot be cast to org.kuali.rice.krad.uif.view.LookupView", "KULRICE-10489 KRAD Demo Lookup Sample with Conditional Criteria ClassCastException on search");

        jiraMatches.put("WorkFlowRouteRulesBlanketApp expected:<[FINAL]>", "KULRICE-9051 WorkFlow Route Rules Blanket Approval submit status results in Enroute, not Final");

        jiraMatches.put("//div[@class='uif-stackedCollectionLayout']/div[@class='uif-collectionItem uif-gridCollectionItem']/table/tbody/tr/td/div/input[@name='collection4[0].field1']", "KULRICE-10520 Update DemoLibraryLayoutManagersStackedLayoutSmokeTest");
        //jiraMatches.put("", "");
    }

    /**
     * If the contents contents the jiraMatches key, call fail on failable passing in the jiraMatches value for the matched key.
     * @param contents to check for containing of the jiraMatches keys.
     * @param failable to fail with the jiraMatches value if the jiraMatches key is contained in the contents
     */
    public static void failOnMatchedJira(String contents, Failable failable) {
        Iterator<String> iter = jiraMatches.keySet().iterator();
        String key = null;

        while (iter.hasNext()) {
            key = iter.next();
            if (contents.contains(key)) {
                failable.fail(JIRA_BROWSE_URL + jiraMatches.get(key));
            }
        }
    }

    /**
     * Calls failOnMatchedJira with the contents and if no match is detected then the message.
     * @param contents to check for containing of the jiraMatches keys.
     * @param message to check for containing of the jiraMatches keys if contents doesn't
     * @param failable to fail with the jiraMatches value if the contents or message is detected
     */
    public static void failOnMatchedJira(String contents, String message, Failable failable) {
        failOnMatchedJira(contents, failable);
        failOnMatchedJira(message, failable);
    }
}

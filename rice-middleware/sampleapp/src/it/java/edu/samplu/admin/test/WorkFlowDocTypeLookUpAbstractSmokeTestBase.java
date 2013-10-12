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
package edu.samplu.admin.test;

import org.kuali.rice.testtools.selenium.Failable;
import org.kuali.rice.testtools.selenium.ITUtil;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class WorkFlowDocTypeLookUpAbstractSmokeTestBase extends AdminTmplMthdSTNavBase{

    /**
     * ITUtil.PORTAL + "?channelTitle=Document%20Type&channelUrl=" 
     * + ITUtil.getBaseUrlString() + ITUtil.KNS_LOOKUP_METHOD + "org.kuali.rice.kew.doctype.bo.DocumentType&returnLocation=" +
     * ITUtil.PORTAL_URL + ITUtil.HIDE_RETURN_LINK + ITUtil.DOC_FORM_KEY+ "88888888";
     */
    public static final String BOOKMARK_URL = ITUtil.PORTAL + "?channelTitle=Document%20Type&channelUrl=" 
            + ITUtil.getBaseUrlString() + ITUtil.KNS_LOOKUP_METHOD +
            "org.kuali.rice.kew.doctype.bo.DocumentType&returnLocation=" +
            ITUtil.PORTAL_URL + ITUtil.HIDE_RETURN_LINK + ITUtil.DOC_FORM_KEY+ "88888888";

    /**
     * {@inheritDoc}
     * Document Type
     * @return
     */
    @Override
    protected String getLinkLocator() {
        return "Document Type";
    }
   
    public void testWorkFlowDocTypeLookUpBookmark(Failable failable) throws Exception {
        testDocTypeLookup();
        passed();
    }

    public void testWorkFlowDocTypeLookUpNav(Failable failable) throws Exception {
        testDocTypeLookup();
        passed();
    }
}

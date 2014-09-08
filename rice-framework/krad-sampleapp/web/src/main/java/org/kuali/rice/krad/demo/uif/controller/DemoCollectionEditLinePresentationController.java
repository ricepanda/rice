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
package org.kuali.rice.krad.demo.uif.controller;

import org.kuali.rice.krad.demo.uif.form.UITestObject;
import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.uif.view.ViewModel;
import org.kuali.rice.krad.uif.view.ViewPresentationControllerBase;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DemoCollectionEditLinePresentationController extends ViewPresentationControllerBase {

    /*@Override
    public boolean canEditLineField(View view, ViewModel model, CollectionGroup collectionGroup,
            String collectionPropertyName, Object line, Field field, String propertyName) {
        if(field instanceof DataField) {
            DataField dataField = (DataField) field;
            if(dataField.getPropertyName().equalsIgnoreCase("field4") &&
                    dataField.getBindingInfo().getCollectionPath().equalsIgnoreCase("collection1")) {
                return false;
            }
        }
        return true;
    }*/

    @Override
    public boolean canEditLine(View view, ViewModel model, CollectionGroup collectionGroup,
            String propertyName, Object currentLine) {
        if(currentLine != null && currentLine instanceof UITestObject && propertyName.equalsIgnoreCase("collection1_8")) {
            UITestObject testObject = (UITestObject) currentLine;
            if (testObject.getField4() != null && testObject.getField4().equalsIgnoreCase("16")) {
                return false;
            }
        }
        return true;
    }
}

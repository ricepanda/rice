/**
 * Copyright 2005-2012 The Kuali Foundation
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
package org.kuali.rice.krad.uif.widget;

import java.util.HashMap;

/**
 * Used for rendering a lightbox in the UI to display action links in dialog
 * popups
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class LightBox extends WidgetBase {
    private static final long serialVersionUID = -4004284762546700975L;

    private String height;
    private String width;

    private boolean addAppParms;
    private boolean lookupReturnByScript;

    public LightBox() {
        super();
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#getTemplateOptionsJSString()
     */
    @Override
    public String getTemplateOptionsJSString() {
        if (getTemplateOptions() == null) {
            setTemplateOptions(new HashMap<String, String>());
        }

        // Add the width and height properties to the ComponentOptions
        // before the JS String gets generated.
        if (width != null) {
            getTemplateOptions().put("width", width);
        }
        if (height != null) {
            getTemplateOptions().put("height", height);
        }

        return super.getTemplateOptionsJSString();
    }

    /**
     * @return height of light box
     */
    public String getHeight() {
        return height;
    }

    /**
     * Setter for the height of the light box
     * Can be percentage. ie. 75%
     *
     * @param height
     */
    public void setHeight(String height) {
        this.height = height;
    }

    /**
     * @return width of light box
     */
    public String getWidth() {
        return width;
    }

    /**
     * Setter for the width of the light box
     * Can be percentage. ie. 75%
     *
     * @param width
     */
    public void setWidth(String width) {
        this.width = width;
    }

    /**
     * Indicates that the light box link should have application parameters added to it.
     *
     * @return true if the link should have application parameters added, false otherwise
     */
    public boolean isAddAppParms() {
        return addAppParms;
    }

    /**
     * Setter for the addAppParms.
     *
     * @param addAppParms
     */
    public void setAddAppParms(boolean addAppParms) {
        this.addAppParms = addAppParms;
    }

    /**
     * @return the lookupReturnByScript flag
     */
    public boolean isLookupReturnByScript() {
        return lookupReturnByScript;
    }

    /**
     * Setter for the flag to indicate that lookups will return the value
     * by script and not a post
     *
     * @param lookupReturnByScript the lookupReturnByScript flag
     */
    public void setLookupReturnByScript(boolean lookupReturnByScript) {
        this.lookupReturnByScript = lookupReturnByScript;
    }
}

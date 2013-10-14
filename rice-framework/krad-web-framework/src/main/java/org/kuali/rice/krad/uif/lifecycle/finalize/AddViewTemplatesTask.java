/*
 * Copyright 2011 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.krad.uif.lifecycle.finalize;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.container.Container;
import org.kuali.rice.krad.uif.freemarker.LifecycleRenderingContext;
import org.kuali.rice.krad.uif.layout.LayoutManager;
import org.kuali.rice.krad.uif.lifecycle.AbstractViewLifecycleTask;
import org.kuali.rice.krad.uif.lifecycle.ViewLifecycle;
import org.kuali.rice.krad.uif.lifecycle.ViewLifecyclePhase;
import org.kuali.rice.krad.uif.view.View;

/**
 * Add templates defined on this component to the view for rendering.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class AddViewTemplatesTask extends AbstractViewLifecycleTask {

    /**
     * Constructor.
     * 
     * @param phase The finalize phase for the component.
     */
    public AddViewTemplatesTask(ViewLifecyclePhase phase) {
        super(phase);
    }

    /**
     * @see org.kuali.rice.krad.uif.lifecycle.AbstractViewLifecycleTask#performLifecycleTask()
     */
    @Override
    protected void performLifecycleTask() {
        Component component = getPhase().getComponent();
        View view = ViewLifecycle.getView();
        LifecycleRenderingContext renderingContext = ViewLifecycle.getRenderingContext();

        // add the components template to the views list of components
        if (!component.isSelfRendered() && StringUtils.isNotBlank(component.getTemplate())) {
            String template = component.getTemplate();
            view.addViewTemplate(template);
            if (renderingContext != null) {
                renderingContext.importTemplate(template);
            }
        }

        if (component instanceof Container) {
            LayoutManager layoutManager = ((Container) component).getLayoutManager();

            if (layoutManager != null) {
                String template = layoutManager.getTemplate();
                view.addViewTemplate(template);
                if (renderingContext != null) {
                    renderingContext.importTemplate(template);
                }
            }
        }
    }

}

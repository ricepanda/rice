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

import org.kuali.rice.krad.uif.lifecycle.AbstractViewLifecycleTask;
import org.kuali.rice.krad.uif.lifecycle.FinalizeComponentPhase;
import org.kuali.rice.krad.uif.lifecycle.ViewLifecyclePhase;

/**
 * Perform default finalize behavior defined for the component.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ComponentDefaultFinalizeTask extends AbstractViewLifecycleTask {

    /**
     * Constructor.
     * 
     * @param phase The finalize phase for the component.
     */
    public ComponentDefaultFinalizeTask(ViewLifecyclePhase phase) {
        super(phase);
    }

    /**
     * @see org.kuali.rice.krad.uif.lifecycle.AbstractViewLifecycleTask#getPhase()
     */
    @Override
    public FinalizeComponentPhase getPhase() {
        return (FinalizeComponentPhase) super.getPhase();
    }

    /**
     * @see org.kuali.rice.krad.uif.lifecycle.AbstractViewLifecycleTask#performLifecycleTask()
     */
    @SuppressWarnings("deprecation")
    @Override
    protected void performLifecycleTask() {
        FinalizeComponentPhase phase = getPhase();
        phase.getComponent().performFinalize(phase.getModel(), phase.getParent());
    }

}

/*
 * Copyright 2005-2008 The Kuali Foundation
 * 
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
package org.kuali.rice.kew.help.service;

import java.util.List;

import org.kuali.rice.core.framework.impex.xml.XmlExporter;
import org.kuali.rice.core.framework.impex.xml.XmlLoader;
import org.kuali.rice.kew.help.HelpEntry;


/**
 * A service which provides data access for {@link HelpEntry} objects.
 * 
 * @see HelpEntry
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface HelpService extends XmlLoader, XmlExporter {
    public void save(HelpEntry helpEntry);    
    public void saveXmlEntry(HelpEntry helpEntry);
    public void delete(HelpEntry helpEntry);
    public HelpEntry findById(String helpId);
    public List search(HelpEntry helpEntry);
    public HelpEntry findByKey(String helpKey);
}

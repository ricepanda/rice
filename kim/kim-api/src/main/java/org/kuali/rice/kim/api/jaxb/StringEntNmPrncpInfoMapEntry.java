/*
 * Copyright 2007-2010 The Kuali Foundation
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
package org.kuali.rice.kim.api.jaxb;

import org.kuali.rice.kim.api.identity.principal.EntityNamePrincipalName;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * This is a description of what this class does - jim7 don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class StringEntNmPrncpInfoMapEntry {
	
	private static final long serialVersionUID = 1L;
	
	@XmlAttribute
	String key;
	
	@XmlElement(required=true) // maxoccurs == minoccurs == 1
    EntityNamePrincipalName value;
	
	/**
	 *
	 */
	public StringEntNmPrncpInfoMapEntry() {
	    super();
	}
	
	/**
	 * @param name
	 * @param value
	 */
	public StringEntNmPrncpInfoMapEntry(String key, EntityNamePrincipalName value) {
	    super();
	    
	    this.key = key;
	    this.value = value;
	}

}

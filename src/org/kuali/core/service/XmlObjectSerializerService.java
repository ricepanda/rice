/*
 * Copyright 2005-2006 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License");
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
package org.kuali.core.service;

import javax.xml.transform.TransformerException;

/**
 * This interface defines methods that an XmlObjectSerializer Service must provide. This will wrap our needs for xml to object and
 * object to xml functionality.
 * 
 * 
 */
public interface XmlObjectSerializerService {
    /**
     * Marshals out XML from an object instance.
     * 
     * @param object
     * @return
     */
    public String toXml(Object object);

    /**
     * Retrieves an Object instance from a String of XML - unmarshals.
     * 
     * @param xml
     * @return
     */
    public Object fromXml(String xml);

    /**
     * This will serialize the XML without using the OJB Proxy Converter and following proxy object graphs.
     */
    public String toXmlForMaintainables(Object object);

    /**
     * This will deserialize the XML without using the OJB Proxy Converter and following proxy object graphs.
     */
    public Object fromXmlForMaintainables(String xml);

    /**
     * Renders an org.w3c.Node to a String.
     */
    public String writeNode(org.w3c.dom.Node node, boolean indent) throws TransformerException;

}
/*
 * Copyright 2005-2006 The Kuali Foundation.
 * 
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
package edu.iu.uis.eden.messaging;

import java.util.List;

import javax.xml.namespace.QName;

import org.kuali.rice.resourceloader.ResourceLoader;

import edu.iu.uis.eden.messaging.exceptionhandling.MessageExceptionHandler;

/**
 * A {@link ResourceLoader} which locates endpoints to remoted services.
 *
 * @see ResourceLoader
 * @see RemotedServiceHolder
 * @see ServiceInfo
 *
 * @author rkirkend
 */
public interface RemoteResourceServiceLocator extends ResourceLoader {

//	public static final QName REGISTRY_NAME = new QName(Core.getCurrentContextConfig().getMessageEntity(), ResourceLoader.REMOTE_RESOURCE_LOADER_NAME);
	
	public List<RemotedServiceHolder> getAllServices(QName qName);
	public void removeService(ServiceInfo serviceInfo, Object service);
	public Object getService(QName qName, String url);
	public MessageExceptionHandler getMessageExceptionHandler(QName qName);
}
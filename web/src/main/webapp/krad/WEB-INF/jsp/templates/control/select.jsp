<%--
 Copyright 2006-2007 The Kuali Foundation
 
 Licensed under the Educational Community License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
 http://www.opensource.org/licenses/ecl2.php
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
--%>
<%@ include file="/krad/WEB-INF/jsp/tldHeader.jsp"%>

<tiles:useAttribute name="control" classname="org.kuali.rice.krad.uif.control.SelectControl"/>
<tiles:useAttribute name="field" classname="org.kuali.rice.krad.uif.field.InputField"/>

<%--
    Standard HTML Select Input
    
 --%>
 
<form:select id="${field.id}" path="${field.bindingInfo.bindingPath}" 
             size="${control.size}" multiple="${control.multiple}"
             cssClass="${control.styleClassesAsString}" disabled="${control.disabled}"
             tabindex="${control.tabIndex}">
      
  <%-- TODO: need htmlSpacePadding that was removed from KeyValuePair --%>           
  <c:forEach items="${control.options}" var="option" varStatus="optionVarStatus">
    <form:option value="${option.key}">${option.value}</form:option>
  </c:forEach>           
             
</form:select>             
 
 
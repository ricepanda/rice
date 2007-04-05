<%--
 Copyright 2007 The Kuali Foundation.
 
 Licensed under the Educational Community License, Version 1.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
 http://www.opensource.org/licenses/ecl1.php
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
--%>
<%@ taglib prefix="c" uri="/tlds/c.tld"%>
<%@ taglib prefix="fn" uri="/tlds/fn.tld"%>
<%@ taglib uri="/tlds/struts-html.tld" prefix="html"%>
<%@ taglib uri="/tlds/struts-logic.tld" prefix="logic"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="kul"%>
<%@ taglib uri="/tlds/struts-html-el.tld" prefix="html-el"%>
<%@ taglib uri="/tlds/displaytag.tld" prefix="display"%>
<%@ taglib uri="/tlds/struts-bean-el.tld" prefix="bean-el"%>

<%@ attribute name="resultsList" required="true" type="java.util.List" description="The rows of fields that we'll iterate to display." %>

<c:if test="${empty resultsList && KualiForm.methodToCall != 'start' && KualiForm.methodToCall != 'refresh'}">
	There were no results found.
</c:if>
<c:if test="${!empty resultsList}">
    <c:if test="${KualiForm.searchUsingOnlyPrimaryKeyValues}">
    	<bean-el:message key="lookup.using.primary.keys" arg0="${KualiForm.primaryKeyFieldLabels}"/>
    	<br/><br/>
    </c:if>
	<c:choose>
		<c:when test="${param['d-16544-e'] == null}">
			<kul:multipleValueLookupPagingBanner pageNumber="${KualiForm.viewedPageNumber}" totalPages="${KualiForm.totalNumberOfPages}"
				firstDisplayedRow="${KualiForm.firstRowIndex}" lastDisplayedRow="${KualiForm.lastRowIndex}" resultsActualSize="${KualiForm.resultsActualSize}"
				resultsLimitedSize="${KualiForm.resultsLimitedSize}"/>
			<input type="hidden" name="${Constants.MULTIPLE_VALUE_LOOKUP_PREVIOUSLY_SELECTED_OBJ_IDS_PARAM}" value="${KualiForm.compositeSelectedObjectIds}"/>
			<input type="hidden" name="${Constants.MULTIPLE_VALUE_LOOKUP_PREVIOUSLY_SORTED_COLUMN_INDEX_PARAM}" value="${KualiForm.columnToSortIndex}"/>
			<p>
				<input type="image" src="images/buttonsmall_selectall.gif" alt="Select all rows" title="Select all rows" class="tinybutton" name="methodToCall.selectAll.${Constants.METHOD_TO_CALL_PARM12_LEFT_DEL}${KualiForm.searchUsingOnlyPrimaryKeyValues}${Constants.METHOD_TO_CALL_PARM12_RIGHT_DEL}.x" value="Select All Rows"/>
				<input type="image" src="images/buttonsmall_unselall.gif" alt="Unselect all rows" title="Unselect all rows" class="tinybutton" name="methodToCall.unselectAll.${Constants.METHOD_TO_CALL_PARM12_LEFT_DEL}${KualiForm.searchUsingOnlyPrimaryKeyValues}${Constants.METHOD_TO_CALL_PARM12_RIGHT_DEL}.x" value="Unselect All Rows"/>
			</p>
			<table cellpadding="0" class="datatable-100" cellspacing="0" id="row">
				<thead>
					<tr>
						<c:forEach items="${resultsList[0].columns}" var="column" varStatus="columnLoopStatus">
							<th class="sortable">
								${column.columnTitle}
							</th>
						</c:forEach>
						<th>
							Select?
						</th>
					</tr>
					<tr>
						<c:forEach items="${resultsList[0].columns}" var="column" varStatus="columnLoopStatus">
							<th class="sortable" align="center">
								<input name="methodToCall.sort.<c:out value="${columnLoopStatus.index}"/>.${Constants.METHOD_TO_CALL_PARM12_LEFT_DEL}${KualiForm.searchUsingOnlyPrimaryKeyValues}${Constants.METHOD_TO_CALL_PARM12_RIGHT_DEL}.x" type="image" src="images/sort.gif" alt="Sort column ${column.columnTitle}" valign="bottom" title="Sort column ${column.columnTitle}">
							</th>
						</c:forEach>
						<th>
							&nbsp;
						</th>
					</tr>
				</thead>
				<c:forEach items="${resultsList}" var="row" varStatus="rowLoopStatus" begin="${KualiForm.firstRowIndex}" end="${KualiForm.lastRowIndex}">
					<c:set var="rowclass" value="odd"/>
					<c:if test="${rowLoopStatus.count % 2 == 0}">
						<c:set var="rowclass" value="even"/>
					</c:if>
					<tr class="${rowclass}">
						<c:forEach items="${row.columns}" var="column">
							<td class="infocell">
								<c:if test="${!empty column.propertyURL}">
									<a href="<c:out value="${column.propertyURL}"/>" target="blank" title="${column.propertyValue}">
								</c:if>
								<c:out value="${column.propertyValue}"/><c:if test="${!empty column.propertyURL}"></a>
								</c:if>
								&nbsp;
							</td>
						</c:forEach>
						<td class="infocell">
							<c:if test="${empty KualiForm.compositeObjectIdMap[row.objectId]}">
								<input type="checkbox" name="${Constants.MULTIPLE_VALUE_LOOKUP_SELECTED_OBJ_ID_PARAM_PREFIX}${row.objectId}" value="checked"/>
							</c:if>
							<c:if test="${!empty KualiForm.compositeObjectIdMap[row.objectId]}">
								<input type="checkbox" name="${Constants.MULTIPLE_VALUE_LOOKUP_SELECTED_OBJ_ID_PARAM_PREFIX}${row.objectId}" value="checked" checked="checked"/>
							</c:if>
							<input type="hidden" name="${Constants.MULTIPLE_VALUE_LOOKUP_DISPLAYED_OBJ_ID_PARAM_PREFIX}${row.objectId}" value="onscreen"/>
						</td>
					</tr>
				</c:forEach>
			</table>
			<p>
				<input type="submit" name="methodToCall.selectAll.x" value="Select All Rows"/>
				<input type="submit" name="methodToCall.unselectAll.x" value="Unselect All Rows"/>
			</p>
			<kul:multipleValueLookupExportBanner/>
		</c:when>
		<c:otherwise>
			<display:table class="datatable-100" cellspacing="0"
				requestURIcontext="false" cellpadding="0" name="${reqSearchResults}"
				id="row" export="true" pagesize="100">
				<c:forEach items="${row.columns}" var="column" varStatus="loopStatus">
					<display:column class="${colClass}" sortable="${column.sortable}"
								title="${column.columnTitle}" comparator="${column.comparator}"
								maxLength="70"><c:out value="${column.propertyValue}" escapeXml="false" default="" /></display:column>
				</c:forEach>
			</display:table>
		</c:otherwise>
	</c:choose>
</c:if>

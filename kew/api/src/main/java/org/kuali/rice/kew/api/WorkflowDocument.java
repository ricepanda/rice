/*
 * Copyright 2005-2007 The Kuali Foundation
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
package org.kuali.rice.kew.api;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.kuali.rice.core.api.config.ConfigurationException;
import org.kuali.rice.kew.api.action.ActionRequest;
import org.kuali.rice.kew.api.action.ActionRequestType;
import org.kuali.rice.kew.api.action.ActionTaken;
import org.kuali.rice.kew.api.action.ActionType;
import org.kuali.rice.kew.api.action.AdHocRevoke;
import org.kuali.rice.kew.api.action.AdHocToGroup;
import org.kuali.rice.kew.api.action.AdHocToPrincipal;
import org.kuali.rice.kew.api.action.DocumentActionResult;
import org.kuali.rice.kew.api.action.InvalidActionTakenException;
import org.kuali.rice.kew.api.action.MovePoint;
import org.kuali.rice.kew.api.action.RequestedActions;
import org.kuali.rice.kew.api.action.ReturnPoint;
import org.kuali.rice.kew.api.action.ValidActions;
import org.kuali.rice.kew.api.action.WorkflowDocumentActionsService;
import org.kuali.rice.kew.api.doctype.DocumentTypeNotFoundException;
import org.kuali.rice.kew.api.document.Document;
import org.kuali.rice.kew.api.document.DocumentContent;
import org.kuali.rice.kew.api.document.DocumentContentUpdate;
import org.kuali.rice.kew.api.document.DocumentCreationException;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kew.api.document.DocumentUpdate;
import org.kuali.rice.kew.api.document.RouteNodeInstance;
import org.kuali.rice.kew.api.document.WorkflowAttributeDefinition;
import org.kuali.rice.kew.api.document.WorkflowAttributeValidationError;
import org.kuali.rice.kew.api.document.WorkflowDocumentService;

/**
 * TODO ..
 * 
 * <p>This class is *not* thread safe.
 *
 */
public class WorkflowDocument implements java.io.Serializable {

	private static final long serialVersionUID = -3672966990721719088L;

    private String principalId;
    
    private ModifiableDocument modifiableDocument;    
    private ModifiableDocumentContent modifiableDocumentContent;
    private ValidActions validActions;
    private RequestedActions requestedActions;
    
    private boolean documentDeleted = false;

    /**
     * TODO 
     * 
     * @param principalId TODO
     * @param documentTypeName TODO
     * 
     * @return TODO
     * 
     * @throws IllegalArgumentException if principalId is null or blank
     * @throws IllegalArgumentException if documentTypeName is null or blank
     * @throws DocumentTypeNotFoundException if documentTypeName does not represent a valid document type
     * @throws DocumentCreationException if the document type does not allow for creation of a document,
     * this can occur when the given document type is used only as a parent and has no route path configured
     * @throws InvalidActionTakenException if the caller is not allowed to execute this action
     */
    public static WorkflowDocument createDocument(String principalId, String documentTypeName) {
    	return createDocument(principalId, documentTypeName, null, null);
    }
    
    /**
     * TODO
     * 
     * @param principalId TODO
     * @param documentTypeName TODO
     * @param title TODO
     * 
     * @return TODO
     * 
     * @throws IllegalArgumentException if principalId is null or blank
     * @throws IllegalArgumentException if documentTypeName is null or blank
     * @throws DocumentTypeNotFoundException if documentTypeName does not represent a valid document type
     */
    public static WorkflowDocument createDocument(String principalId, String documentTypeName, String title) {
    	DocumentUpdate.Builder builder = DocumentUpdate.Builder.create();
    	builder.setTitle(title);
    	return createDocument(principalId, documentTypeName, builder.build(), null);
    }
    
    /**
     * TODO
     * 
     * @param principalId TODO
     * @param documentTypeName TODO
     * @param documentUpdate TODO
     * @param documentContentUpdate TODO
     * 
     * @return TODO
     * 
     * @throws IllegalArgumentException if principalId is null or blank
     * @throws IllegalArgumentException if documentTypeName is null or blank
     * @throws DocumentTypeNotFoundException if documentTypeName does not represent a valid document type
     */
	public static WorkflowDocument createDocument(String principalId, String documentTypeName, DocumentUpdate documentUpdate, DocumentContentUpdate documentContentUpdate) {
		if (StringUtils.isBlank(principalId)) {
			throw new IllegalArgumentException("principalId was null or blank");
		}
		if (StringUtils.isBlank(documentTypeName)) {
			throw new IllegalArgumentException("documentTypeName was null or blank");
		}
		Document document = getWorkflowDocumentActionsService().create(documentTypeName, principalId, documentUpdate, documentContentUpdate);
		return new WorkflowDocument(principalId, document);
	}
	
	public static WorkflowDocument loadDocument(String principalId, String documentId) {
		if (StringUtils.isBlank(principalId)) {
			throw new IllegalArgumentException("principalId was null or blank");
		}
		if (StringUtils.isBlank(documentId)) {
			throw new IllegalArgumentException("documentId was null or blank");
		}
		Document document = getWorkflowDocumentService().getDocument(documentId);
		if (document == null) {
			throw new IllegalArgumentException("Failed to locate workflow document for given documentId: " + documentId);
		}
		return new WorkflowDocument(principalId, document);
	}
	
	protected WorkflowDocument(String principalId, Document document) {
		if (StringUtils.isBlank("principalId")) {
			throw new IllegalArgumentException("principalId was null or blank");
		}
		if (document == null) {
			throw new IllegalArgumentException("document was null");
		}
		this.principalId = principalId;
		this.modifiableDocument = new ModifiableDocument(document);
    }

    private static WorkflowDocumentActionsService getWorkflowDocumentActionsService() {
    	WorkflowDocumentActionsService workflowDocumentActionsService =  KewApiServiceLocator.getWorkflowDocumentActionsService();
    	if (workflowDocumentActionsService == null) {
    		throw new ConfigurationException("Could not locate the WorkflowDocumentActionsService.  Please ensure that KEW client is configured properly!");
    	}
    	return workflowDocumentActionsService;
    }
    
    private static WorkflowDocumentService getWorkflowDocumentService() {
    	WorkflowDocumentService workflowDocumentService =  KewApiServiceLocator.getWorkflowDocumentService();
    	if (workflowDocumentService == null) {
    		throw new ConfigurationException("Could not locate the WorkflowDocumentService.  Please ensure that KEW client is configured properly!");
    	}
    	return workflowDocumentService;
    }
    
    protected ModifiableDocument getModifiableDocument() {
    	return modifiableDocument;
    }
    
    protected ModifiableDocumentContent getModifiableDocumentContent() {
    	if (this.modifiableDocumentContent == null) {
    		DocumentContent documentContent = getWorkflowDocumentService().getDocumentContent(getDocumentId());
    		if (documentContent == null) {
    			throw new IllegalStateException("Failed to load document content for documentId: " + getDocumentId());
    		}
    		this.modifiableDocumentContent = new ModifiableDocumentContent(documentContent);
    	}
    	return this.modifiableDocumentContent;
    }
    
    public String getDocumentId() {
    	if (documentDeleted) {
    		throw new IllegalStateException("Document has been deleted.");
    	}
    	return getModifiableDocument().getDocumentId();
    }
    
    public Document getDocument() {
        return getModifiableDocument().getDocument();
    }
    
    public DocumentContent getDocumentContent() {
    	return getModifiableDocumentContent().getDocumentContent();
    }

    public String getApplicationContent() {
        return getDocumentContent().getApplicationContent();
    }

    public void setApplicationContent(String applicationContent) {
    	getModifiableDocumentContent().setApplicationContent(applicationContent);
    }

    public void clearAttributeContent() {
    	getModifiableDocumentContent().setAttributeContent("");
    }

    public String getAttributeContent() {
        return getDocumentContent().getAttributeContent();
    }

    public void addAttributeDefinition(WorkflowAttributeDefinition attributeDefinition) {
    	getModifiableDocumentContent().addAttributeDefinition(attributeDefinition);
    }

    public void removeAttributeDefinition(WorkflowAttributeDefinition attributeDefinition) {
    	getModifiableDocumentContent().removeAttributeDefinition(attributeDefinition);
    }

    public void clearAttributeDefinitions() {
    	getAttributeDefinitions().clear();
    }

    public List<WorkflowAttributeDefinition> getAttributeDefinitions() {
        return getModifiableDocumentContent().getAttributeDefinitions();
    }

    public void addSearchableDefinition(WorkflowAttributeDefinition searchableDefinition) {
    	getModifiableDocumentContent().addSearchableDefinition(searchableDefinition);
    }

    public void removeSearchableDefinition(WorkflowAttributeDefinition searchableDefinition) {
    	getModifiableDocumentContent().removeSearchableDefinition(searchableDefinition);
    }

    public void clearSearchableDefinitions() {
    	getSearchableDefinitions().clear();
    }

    public void clearSearchableContent() {
    	getModifiableDocumentContent().setSearchableContent("");
    }

    public List<WorkflowAttributeDefinition> getSearchableDefinitions() {
        return getModifiableDocumentContent().getSearchableDefinitions();
    }
    
    public List<WorkflowAttributeValidationError> validateAttributeDefinition(WorkflowAttributeDefinition attributeDefinition) {
    	return getWorkflowDocumentActionsService().validateWorkflowAttributeDefinition(attributeDefinition);
    }

    public List<ActionRequest> getRootActionRequests() {
    	return getWorkflowDocumentService().getRootActionRequests(getDocumentId());
    }

    public List<ActionTaken> getActionsTaken() {
    	return getWorkflowDocumentService().getActionsTaken(getDocumentId());
    }

    public void setApplicationDocumentId(String applicationDocumentId) {
    	getModifiableDocument().setApplicationDocumentId(applicationDocumentId);
    }

    public String getApplicationDocumentId() {
    	return getModifiableDocument().getApplicationDocumentId();
    }

    public DateTime getDateCreated() {
    	return getModifiableDocument().getDateCreated();
    }

    public String getTitle() {
        return getModifiableDocument().getTitle();
    }
    
    public ValidActions getValidActions() {
    	if (validActions == null) {
    		validActions = getWorkflowDocumentActionsService().determineValidActions(getDocumentId(), getPrincipalId());
    	}
    	return validActions;
    }
    
    public RequestedActions getRequestedActions() {
    	if (requestedActions == null) {
    		requestedActions = getWorkflowDocumentActionsService().determineRequestedActions(getDocumentId(), getPrincipalId());
    	}
    	return requestedActions;
    }
    
    protected DocumentUpdate getDocumentUpdateIfDirty() {
    	if (getModifiableDocument().isDirty()) {
    		return getModifiableDocument().getBuilder().build();
    	}
    	return null;
    }
    
    protected DocumentContentUpdate getDocumentContentUpdateIfDirty() {
    	if (getModifiableDocumentContent().isDirty()) {
    		return getModifiableDocumentContent().getBuilder().build();
    	}
    	return null;
    }
    
    protected void resetStateAfterAction(DocumentActionResult response) {
    	this.modifiableDocument = new ModifiableDocument(response.getDocument());
    	this.validActions = null;
    	if (response.getValidActions() != null) {
    		this.validActions = response.getValidActions();
    	}
    	this.requestedActions = null;
    	if (response.getRequestedActions() != null) {
    		this.requestedActions = response.getRequestedActions();
    	}
    	// regardless of whether modifiable document content is dirty, we null it out so it will be re-fetched next time it's needed
    	this.modifiableDocumentContent = null;
    }
    
    public void saveDocument(String annotation) {
    	DocumentActionResult result = getWorkflowDocumentActionsService().save(getDocumentId(), principalId, annotation, getDocumentUpdateIfDirty(), getDocumentContentUpdateIfDirty());
    	resetStateAfterAction(result);
    }

    public void route(String annotation) {
    	DocumentActionResult result = getWorkflowDocumentActionsService().route(getDocumentId(), principalId, annotation, getDocumentUpdateIfDirty(), getDocumentContentUpdateIfDirty());
    	resetStateAfterAction(result);
    }
    
    public void disapprove(String annotation) {
    	DocumentActionResult result = getWorkflowDocumentActionsService().disapprove(getDocumentId(), principalId, annotation, getDocumentUpdateIfDirty(), getDocumentContentUpdateIfDirty());
    	resetStateAfterAction(result);
    }

    public void approve(String annotation) {
    	DocumentActionResult result = getWorkflowDocumentActionsService().approve(getDocumentId(), principalId, annotation, getDocumentUpdateIfDirty(), getDocumentContentUpdateIfDirty());
    	resetStateAfterAction(result);
    }

    public void cancel(String annotation) {
    	DocumentActionResult result = getWorkflowDocumentActionsService().cancel(getDocumentId(), principalId, annotation, getDocumentUpdateIfDirty(), getDocumentContentUpdateIfDirty());
    	resetStateAfterAction(result);
    }

    public void blanketApprove(String annotation) {
    	DocumentActionResult result = getWorkflowDocumentActionsService().blanketApprove(getDocumentId(), principalId, annotation, getDocumentUpdateIfDirty(), getDocumentContentUpdateIfDirty());
 		resetStateAfterAction(result);
    }
    
    public void blanketApprove(String annotation, String ... nodeNames) {
    	if (nodeNames == null) {
    		throw new IllegalArgumentException("nodeNames was null");
    	}
    	Set<String> nodeNamesSet = new HashSet<String>(Arrays.asList(nodeNames));
    	DocumentActionResult result = getWorkflowDocumentActionsService().blanketApproveToNodes(getDocumentId(), principalId, annotation, getDocumentUpdateIfDirty(), getDocumentContentUpdateIfDirty(), nodeNamesSet);
 		resetStateAfterAction(result);
    }

    public void saveDocumentData() {
    	DocumentActionResult result = getWorkflowDocumentActionsService().saveDocumentData(getDocumentId(), principalId, getDocumentUpdateIfDirty(), getDocumentContentUpdateIfDirty());
    	resetStateAfterAction(result);
    }

    public void updateApplicationDocumentStatus(String applicationDocumentStatus) {
    	setApplicationDocumentStatus(applicationDocumentStatus);
       	saveDocumentData();
    }
    
    public void setApplicationDocumentStatus(String applicationDocumentStatus) {
    	getModifiableDocument().setApplicationDocumentStatus(applicationDocumentStatus);
    }
    
    public void acknowledge(String annotation) {
    	DocumentActionResult result = getWorkflowDocumentActionsService().acknowledge(getDocumentId(), principalId, annotation, getDocumentUpdateIfDirty(), getDocumentContentUpdateIfDirty());
    	resetStateAfterAction(result);
    }

    public void fyi(String annotation) {
    	DocumentActionResult result = getWorkflowDocumentActionsService().clearFyi(getDocumentId(), principalId, annotation, getDocumentUpdateIfDirty(), getDocumentContentUpdateIfDirty());
    	resetStateAfterAction(result);
    }
    
    public void fyi() {
    	fyi("");
    }

    /**
     * TODO - be sure to mention that once this document is deleted, this api effectively becomes "dead" when you try to
     * execute any document operation
     */
    public void delete() {
    	getWorkflowDocumentActionsService().delete(getDocumentId(), principalId);
    	documentDeleted = true;
    }

    public void refresh() {    	
    	Document document = getWorkflowDocumentService().getDocument(getDocumentId());
    	this.modifiableDocument = new ModifiableDocument(document);
    	this.validActions = null;
    	this.requestedActions = null;
    	this.modifiableDocumentContent = null;
    }

    public void adHocToPrincipal(ActionRequestType actionRequested, String annotation, String targetPrincipalId, String responsibilityDescription, boolean forceAction) {
    	adHocToPrincipal(actionRequested, null, annotation, targetPrincipalId, responsibilityDescription, forceAction);
    }

    public void adHocToPrincipal(ActionRequestType actionRequested, String nodeName, String annotation, String targetPrincipalId, String responsibilityDescription, boolean forceAction) {
    	adHocToPrincipal(actionRequested, nodeName, annotation, targetPrincipalId, responsibilityDescription, forceAction, null);
    }

    public void adHocToPrincipal(ActionRequestType actionRequested, String nodeName, String annotation, String targetPrincipalId, String responsibilityDescription, boolean forceAction, String requestLabel) {
    	AdHocToPrincipal.Builder builder = AdHocToPrincipal.Builder.create(actionRequested, nodeName, targetPrincipalId);
    	builder.setResponsibilityDescription(responsibilityDescription);
    	builder.setForceAction(forceAction);
    	builder.setRequestLabel(requestLabel);
    	DocumentActionResult result = getWorkflowDocumentActionsService().adHocToPrincipal(getDocumentId(), getPrincipalId(), annotation, getDocumentUpdateIfDirty(), getDocumentContentUpdateIfDirty(), builder.build());
    	resetStateAfterAction(result);
    }

    public void adHocToGroup(ActionRequestType actionRequested, String annotation, String targetGroupId, String responsibilityDescription, boolean forceAction) {
    	adHocToGroup(actionRequested, null, annotation, targetGroupId, responsibilityDescription, forceAction);
    }

    public void adHocToGroup(ActionRequestType actionRequested, String nodeName, String annotation, String targetGroupId, String responsibilityDescription, boolean forceAction) {
    	adHocToGroup(actionRequested, nodeName, annotation, targetGroupId, responsibilityDescription, forceAction, null);
    }

    public void adHocToGroup(ActionRequestType actionRequested, String nodeName, String annotation, String targetGroupId, String responsibilityDescription, boolean forceAction, String requestLabel) {
    	AdHocToGroup.Builder builder = AdHocToGroup.Builder.create(actionRequested, nodeName, targetGroupId);
    	builder.setResponsibilityDescription(responsibilityDescription);
    	builder.setForceAction(forceAction);
    	builder.setRequestLabel(requestLabel);
    	DocumentActionResult result = getWorkflowDocumentActionsService().adHocToGroup(getDocumentId(), getPrincipalId(), annotation, getDocumentUpdateIfDirty(), getDocumentContentUpdateIfDirty(), builder.build());
    	resetStateAfterAction(result);
    }

    public void revokeAdHocRequestById(String actionRequestId, String annotation) {
    	if (StringUtils.isBlank(actionRequestId)) {
    		throw new IllegalArgumentException("actionRequestId was null or blank");
    	}
    	DocumentActionResult result = getWorkflowDocumentActionsService().revokeAdHocRequestById(getDocumentId(), principalId, annotation, getDocumentUpdateIfDirty(), getDocumentContentUpdateIfDirty(), actionRequestId);
    	resetStateAfterAction(result);
    }
    
    public void revokeAdHocRequests(AdHocRevoke revoke, String annotation) {
    	if (revoke == null) {
    		throw new IllegalArgumentException("revokeFromPrincipal was null");
    	}
    	DocumentActionResult result = getWorkflowDocumentActionsService().revokeAdHocRequests(getDocumentId(), principalId, annotation, getDocumentUpdateIfDirty(), getDocumentContentUpdateIfDirty(), revoke);
    	resetStateAfterAction(result);
    }
    
    public void revokeAllAdHocRequests(String annotation) {
    	DocumentActionResult result = getWorkflowDocumentActionsService().revokeAllAdHocRequests(getDocumentId(), principalId, annotation, getDocumentUpdateIfDirty(), getDocumentContentUpdateIfDirty());
    	resetStateAfterAction(result);
    }

    public void setTitle(String title) {
        getModifiableDocument().setTitle(title);
    }

    public String getDocumentTypeName() {
    	return getDocument().getDocumentTypeName();
    }

    public boolean isCompletionRequested() {
        return getRequestedActions().isCompleteRequested();
    }
    
    public boolean isApprovalRequested() {
        return getRequestedActions().isApproveRequested();
    }
    
    public boolean isAcknowledgeRequested() {
    	return getRequestedActions().isAcknowledgeRequested();
    }
    
    public boolean isFYIRequested() {
        return getRequestedActions().isFyiRequested();
    }

    public boolean isBlanketApproveCapable() {
    	return isActionValid(ActionType.BLANKET_APPROVE) && (isCompletionRequested() || isApprovalRequested() || isInitiated());
    }
    
    public boolean isRouteCapable() {
    	return isActionValid(ActionType.ROUTE);
    }

    public boolean isActionCodeValid(String actionCode) {
    	if (StringUtils.isBlank(actionCode)) {
    		throw new IllegalArgumentException("actionTakenCode was null or blank");
    	}
    	return getValidActions().getValidActions().contains(ActionType.fromCode(actionCode));
    }
    
    public boolean isActionValid(ActionType actionType) {
    	if (actionType == null) {
    		throw new IllegalArgumentException("actionType was null");
    	}
    	return getValidActions().getValidActions().contains(actionType);
    }
    
    public void superUserBlanketApprove(String annotation) {
    	DocumentActionResult result = getWorkflowDocumentActionsService().superUserBlanketApprove(getDocumentId(), principalId, annotation, getDocumentUpdateIfDirty(), getDocumentContentUpdateIfDirty(), true);
    	resetStateAfterAction(result);
    }
    
    public void superUserNodeApprove(String nodeName, String annotation) {
    	DocumentActionResult result = getWorkflowDocumentActionsService().superUserNodeApprove(getDocumentId(), principalId, annotation, getDocumentUpdateIfDirty(), getDocumentContentUpdateIfDirty(), true, nodeName);
    	resetStateAfterAction(result);    	
    }

    public void superUserTakeRequestedAction(String actionRequestId, String annotation) {
    	DocumentActionResult result = getWorkflowDocumentActionsService().superUserTakeRequestedAction(getDocumentId(), principalId, annotation, getDocumentUpdateIfDirty(), getDocumentContentUpdateIfDirty(), true, actionRequestId);
    	resetStateAfterAction(result);
    }

    public void superUserDisapprove(String annotation) {
    	DocumentActionResult result = getWorkflowDocumentActionsService().superUserDisapprove(getDocumentId(), principalId, annotation, getDocumentUpdateIfDirty(), getDocumentContentUpdateIfDirty(), true);
    	resetStateAfterAction(result);
    }

    public void superUserCancel(String annotation) {
    	DocumentActionResult result = getWorkflowDocumentActionsService().superUserCancel(getDocumentId(), principalId, annotation, getDocumentUpdateIfDirty(), getDocumentContentUpdateIfDirty(), true);
    	resetStateAfterAction(result);
    }
    
    public void superUserReturnToPreviousNode(ReturnPoint returnPoint, String annotation) {
    	DocumentActionResult result = getWorkflowDocumentActionsService().superUserReturnToPreviousNode(getDocumentId(), principalId, annotation, getDocumentUpdateIfDirty(), getDocumentContentUpdateIfDirty(), true, returnPoint);
    	resetStateAfterAction(result);
    }

    public boolean isSuperUser() {
    	return KewApiServiceLocator.getDocumentTypeService().isSuperUser(principalId, getDocument().getDocumentTypeId());
	}

    public void complete(String annotation) {
    	DocumentActionResult result = getWorkflowDocumentActionsService().complete(getDocumentId(), principalId, annotation, getDocumentUpdateIfDirty(), getDocumentContentUpdateIfDirty());
    	resetStateAfterAction(result);
    }

//    /**
//     * Performs the 'logDocumentAction' action on the document this WorkflowDocument represents.  If this is a new document,
//     * the document is created first.  The 'logDocumentAction' simply logs a message on the document.
//     * @param annotation the message to log for the action
//     * @throws WorkflowException in case an error occurs logging a document action on the document
//     * @see WorkflowDocumentActions#logDocumentAction(UserIdDTO, RouteHeaderDTO, String)
//     */
//    public void logDocumentAction(String annotation) throws WorkflowException {
//    	createDocumentIfNeccessary();
//    	getWorkflowDocumentActions().logDocumentAction(principalId, getRouteHeader(), annotation);
//    	documentContentDirty = true;
//    }
//
  
    public DocumentStatus getStatus() {
    	return getDocument().getStatus();
    }
    
    public boolean checkStatus(DocumentStatus status) {
    	if (status == null) {
    		throw new IllegalArgumentException("status was null");
    	}
    	return status == getStatus();
    }
    
    /**
     * Indicates if the document is in the initiated state or not.
     *
     * @return true if in the specified state
     */
    public boolean isInitiated() {
        return checkStatus(DocumentStatus.INITIATED);
    }

    /**
     * Indicates if the document is in the saved state or not.
     *
     * @return true if in the specified state
     */
    public boolean isSaved() {
        return checkStatus(DocumentStatus.SAVED);
    }

    /**
     * Indicates if the document is in the enroute state or not.
     *
     * @return true if in the specified state
     */
    public boolean isEnroute() {
        return checkStatus(DocumentStatus.ENROUTE);
    }

    /**
     * Indicates if the document is in the exception state or not.
     *
     * @return true if in the specified state
     */
    public boolean isException() {
        return checkStatus(DocumentStatus.EXCEPTION);
    }

    /**
     * Indicates if the document is in the canceled state or not.
     *
     * @return true if in the specified state
     */
    public boolean isCanceled() {
        return checkStatus(DocumentStatus.CANCELED);
    }

    /**
     * Indicates if the document is in the disapproved state or not.
     *
     * @return true if in the specified state
     */
    public boolean isDisapproved() {
        return checkStatus(DocumentStatus.DISAPPROVED);
    }

    /**
     * Indicates if the document is in the Processed or Finalized state.
     *
     * @return true if in the specified state
     */
    public boolean isApproved() {
    	return isProcessed() || isFinal();
    }

    /**
     * Indicates if the document is in the processed state or not.
     *
     * @return true if in the specified state
     */
    public boolean isProcessed() {
        return checkStatus(DocumentStatus.PROCESSED);
    }

    /**
     * Indicates if the document is in the final state or not.
     *
     * @return true if in the specified state
     */
    public boolean isFinal() {
        return checkStatus(DocumentStatus.FINAL);
    }

    /**
     * Returns the principalId with which this WorkflowDocument was constructed
     * 
     * @return the principalId with which this WorkflowDocument was constructed
     */
    public String getPrincipalId() {
        return principalId;
    }
    
    public void switchPrincipal(String principalId) {
    	if (StringUtils.isBlank(this.principalId)) {
    		throw new IllegalArgumentException("principalId was null or blank");
    	}
    	this.principalId = principalId;
    	this.validActions = null;
    	this.requestedActions = null;
    }

    public void takeGroupAuthority(String annotation, String groupId) {
    	DocumentActionResult result = getWorkflowDocumentActionsService().takeGroupAuthority(getDocumentId(), principalId, annotation, getDocumentUpdateIfDirty(), getDocumentContentUpdateIfDirty(), groupId);
    	resetStateAfterAction(result);
    }

    public void releaseGroupAuthority(String annotation, String groupId) {
    	DocumentActionResult result = getWorkflowDocumentActionsService().releaseGroupAuthority(getDocumentId(), principalId, annotation, getDocumentUpdateIfDirty(), getDocumentContentUpdateIfDirty(), groupId);
    	resetStateAfterAction(result);
    }

    public Set<String> getNodeNames() {
    	List<RouteNodeInstance> activeNodeInstances = getActiveRouteNodeInstances();
    	Set<String> nodeNames = new HashSet<String>(activeNodeInstances.size());
    	for (RouteNodeInstance routeNodeInstance : activeNodeInstances) {
    		nodeNames.add(routeNodeInstance.getName());
    	}
    	return Collections.unmodifiableSet(nodeNames);
    }

    public void returnToPreviousNode(String nodeName, String annotation) {
    	if (StringUtils.isBlank(nodeName)) {
    		throw new IllegalArgumentException("nodeName was null or blank");
    	}
        returnToPreviousNode(ReturnPoint.create(nodeName), annotation);
    }

    public void returnToPreviousNode(ReturnPoint returnPoint, String annotation) {
    	if (returnPoint == null) {
    		throw new IllegalArgumentException("returnPoint was null");
    	}
    	DocumentActionResult result = getWorkflowDocumentActionsService().returnToPreviousNode(getDocumentId(), principalId, annotation, getDocumentUpdateIfDirty(), getDocumentContentUpdateIfDirty(), returnPoint);
    	resetStateAfterAction(result);
    }

    public void move(MovePoint movePoint, String annotation) {
    	if (movePoint == null) {
    		throw new IllegalArgumentException("movePoint was null");
    	}
    	DocumentActionResult result = getWorkflowDocumentActionsService().move(getDocumentId(), principalId, annotation, getDocumentUpdateIfDirty(), getDocumentContentUpdateIfDirty(), movePoint);
    	resetStateAfterAction(result);
    }
    

    public List<RouteNodeInstance> getActiveRouteNodeInstances() {
    	return getWorkflowDocumentService().getActiveRouteNodeInstances(getDocumentId());
    }
    
    public List<RouteNodeInstance> getRouteNodeInstances() {
    	return getWorkflowDocumentService().getRouteNodeInstances(getDocumentId());
    }

    public List<String> getPreviousNodeNames() {
    	return getWorkflowDocumentService().getPreviousRouteNodeNames(getDocumentId());
	}

//    /**
//     * Returns a document detail VO representing the route header along with action requests, actions taken,
//     * and route node instances.
//     * @return Returns a document detail VO representing the route header along with action requests, actions taken, and route node instances.
//     * @throws WorkflowException
//     */
//    public DocumentDetailDTO getDetail() throws WorkflowException {
//    	return getWorkflowUtility().getDocumentDetail(getDocumentId());
//    }
//
//    /**
//     * Saves the given DocumentContentVO for this document.
//     * @param documentContent document content VO to store for this document
//     * @since 2.3
//     * @see WorkflowDocumentActions#saveDocumentContent(DocumentContentDTO)
//     */
//    public DocumentContentDTO saveDocumentContent(DocumentContentDTO documentContent) throws WorkflowException {
//    	if (documentContent.getDocumentId() == null) {
//    		throw new WorkflowException("Document Content does not have a valid document ID.");
//    	}
//    	// important to check directly against getRouteHeader().getDocumentId() instead of just getDocumentId() because saveDocumentContent
//    	// is called from createDocumentIfNeccessary which is called from getDocumentId().  If that method was used, we would have an infinite loop.
//    	if (!documentContent.getDocumentId().equals(getRouteHeader().getDocumentId())) {
//    		throw new WorkflowException("Attempted to save content on this document with an invalid document id of " + documentContent.getDocumentId());
//    	}
//    	DocumentContentDTO newDocumentContent = getWorkflowDocumentActions().saveDocumentContent(documentContent);
//    	this.documentContent = new ModifiableDocumentContentDTO(newDocumentContent);
//    	documentContentDirty = false;
//    	return this.documentContent;
//    }
//    
//    public void placeInExceptionRouting(String annotation) throws WorkflowException {
//    	createDocumentIfNeccessary();
//    	routeHeader = getWorkflowDocumentActions().placeInExceptionRouting(principalId, getRouteHeader(), annotation);
//    	documentContentDirty = true;
//    }
//
//    /**
//     * Returns a list of NoteVO representing the notes on the document
//     * @return a list of NoteVO representing the notes on the document
//     * @see RouteHeaderDTO#getNotes()
//     */
//    public List<NoteDTO> getNoteList(){
//    	List<NoteDTO> notesList = new ArrayList<NoteDTO>();
//    	NoteDTO[] notes = routeHeader.getNotes();
//    	if (notes != null){
//	    	for (int i=0; i<notes.length; i++){
//	    		if (! isDeletedNote(notes[i])){
//	    			notesList.add(notes[i]);
//	    		}
//	    	}
//    	}
//    	return notesList;
//    }
//
//    /**
//     * Deletes a note from the document.  The deletion is deferred until the next time the document is committed (via an action).
//     * @param noteVO the note to remove from the document
//     */
//    public void deleteNote(NoteDTO noteVO){
//    	if (noteVO != null && noteVO.getNoteId()!=null){
//    		NoteDTO noteToDelete = new NoteDTO();
//    		noteToDelete.setNoteId(new Long(noteVO.getNoteId().longValue()));
//    		/*noteToDelete.setDocumentId(noteVO.getDocumentId());
//    		noteToDelete.setNoteAuthorWorkflowId(noteVO.getNoteAuthorWorkflowId());
//    		noteToDelete.setNoteCreateDate(noteVO.getNoteCreateDate());
//    		noteToDelete.setNoteText(noteVO.getNoteText());
//    		noteToDelete.setLockVerNbr(noteVO.getLockVerNbr());*/
//    		increaseNotesToDeleteArraySizeByOne();
//    		routeHeader.getNotesToDelete()[routeHeader.getNotesToDelete().length - 1]=noteToDelete;
//    	}
//    }
//
//    /**
//     * Updates the note of the same note id, on the document. The update is deferred until the next time the document is committed (via an action).
//     * @param noteVO the note to update
//     */
//    public void updateNote (NoteDTO noteVO){
//    	boolean isUpdateNote = false;
//    	if (noteVO != null){
//    		NoteDTO[] notes = routeHeader.getNotes();
//    		NoteDTO  copyNote = new NoteDTO();
//			if (noteVO.getNoteId() != null){
//				copyNote.setNoteId(new Long(noteVO.getNoteId().longValue()));
//			}
//
//			if (noteVO.getDocumentId() != null){
//				copyNote.setDocumentId(noteVO.getDocumentId());
//			} else {
//				copyNote.setDocumentId(routeHeader.getDocumentId());
//			}
//			
//			if (noteVO.getNoteAuthorWorkflowId() != null){
//				copyNote.setNoteAuthorWorkflowId(new String(noteVO.getNoteAuthorWorkflowId()));
//			} else {
//			    copyNote.setNoteAuthorWorkflowId(principalId.toString())	;
//			}
//
//			if (noteVO.getNoteCreateDate() != null){
//				Calendar cal = Calendar.getInstance();
//				cal.setTimeInMillis(noteVO.getNoteCreateDate().getTimeInMillis());
//				copyNote.setNoteCreateDate(cal);
//			} else {
//				copyNote.setNoteCreateDate(Calendar.getInstance());
//			}
//
//			if (noteVO.getNoteText() != null){
//				copyNote.setNoteText(new String(noteVO.getNoteText()));
//			}
//			if (noteVO.getLockVerNbr() != null){
//				copyNote.setLockVerNbr(new Integer(noteVO.getLockVerNbr().intValue()));
//			}
//    		if (notes != null){
//	    		for (int i=0; i<notes.length; i++){
//	    			if (notes[i].getNoteId()!= null && notes[i].getNoteId().equals(copyNote.getNoteId())){
//	    				notes[i] = copyNote;
//	    				isUpdateNote = true;
//	    				break;
//	    			}
//	    		}
//    		}
//    		// add new note to the notes array
//    		if (! isUpdateNote){
//	    		copyNote.setNoteId(null);
//	    		increaseNotesArraySizeByOne();
//	    		routeHeader.getNotes()[routeHeader.getNotes().length-1]= copyNote;
//    		}
//    	}
//    }
//
//    /**
//     * Sets a variable on the document.  The assignment is deferred until the next time the document is committed (via an action).
//     * @param name name of the variable
//     * @param value value of the variable
//     */
//    public void setVariable(String name, String value) throws WorkflowException {
//    	createDocumentIfNeccessary();
//        getRouteHeader().setVariable(name, value);
//    }
//
//    /**
//     * Gets the value of a variable on the document, creating the document first if it does not exist.
//     * @param name variable name
//     * @return variable value
//     */
//    public String getVariable(String name) throws WorkflowException {
//    	createDocumentIfNeccessary();
//        return getRouteHeader().getVariable(name);
//    }
//
//    /**
//     *
//     * Tells workflow that the current the document is constructed as will receive all future requests routed to them
//     * disregarding any force action flags set on the action request.  Uses the setVariable method behind the seens so
//     * an action needs taken on the document to set this state on the document.
//     *
//     * @throws WorkflowException
//     */
//    public void setReceiveFutureRequests() throws WorkflowException {
//        WorkflowUtility workflowUtility = getWorkflowUtility();
//        this.setVariable(workflowUtility.getFutureRequestsKey(principalId), workflowUtility.getReceiveFutureRequestsValue());
//    }
//
//    /**
//     * Tell workflow that the current document is constructed as will not receive any future requests routed to them
//     * disregarding any force action flags set on action requests.  Uses the setVariable method behind the scenes so
//     * an action needs taken on the document to set this state on the document.
//     *
//     * @throws WorkflowException
//     */
//    public void setDoNotReceiveFutureRequests() throws WorkflowException {
//        WorkflowUtility workflowUtility = getWorkflowUtility();
//        this.setVariable(workflowUtility.getFutureRequestsKey(principalId), workflowUtility.getDoNotReceiveFutureRequestsValue());
//    }
//
//    /**
//     * Clears any state set on the document regarding a user receiving or not receiving action requests.  Uses the setVariable method
//     * behind the seens so an action needs taken on the document to set this state on the document.
//     *
//     * @throws WorkflowException
//     */
//    public void setClearFutureRequests() throws WorkflowException {
//        WorkflowUtility workflowUtility = getWorkflowUtility();
//        this.setVariable(workflowUtility.getFutureRequestsKey(principalId), workflowUtility.getClearFutureRequestsValue());
//    }
//
//    /**
//     * Deletes the note of with the same id as that of the argument on the document.
//     * @param noteVO the note to test for deletion
//     * @return whether the note is already marked for deletion.
//     */
//    private boolean isDeletedNote(NoteDTO noteVO) {
//    	NoteDTO[] notesToDelete = routeHeader.getNotesToDelete();
//    	if (notesToDelete != null){
//    		for (int i=0; i<notesToDelete.length; i++){
//    			if (notesToDelete[i].getNoteId().equals(noteVO.getNoteId())){
//    				return true;
//    			}
//    		}
//    	}
//    	return false;
//    }
//
//    /**
//     * Increases the size of the routeHeader notes VO array
//     */
//   private void increaseNotesArraySizeByOne() {
//	   NoteDTO[] tempArray;
//	   NoteDTO[] notes = routeHeader.getNotes();
//	   if (notes == null){
//		   tempArray = new NoteDTO[1];
//	   } else {
//		   tempArray = new NoteDTO[notes.length + 1];
//		   for (int i=0; i<notes.length; i++){
//			   tempArray[i] = notes[i];
//		   }
//	   }
//	   routeHeader.setNotes(tempArray);
//   }
//
//   /**
//    * Increases the size of the routeHeader notesToDelete VO array
//    */
//   private void increaseNotesToDeleteArraySizeByOne() {
//	   NoteDTO[] tempArray;
//	   NoteDTO[] notesToDelete = routeHeader.getNotesToDelete();
//	   if (notesToDelete == null){
//		   tempArray = new NoteDTO[1];
//	   } else {
//		   tempArray = new NoteDTO[notesToDelete.length + 1];
//		   for (int i=0; i<notesToDelete.length; i++){
//			   tempArray[i] = notesToDelete[i];
//		   }
//	   }
//	   routeHeader.setNotesToDelete(tempArray);
//   }
//   
//   //add 1 link between 2 docs by DTO, double link added
//   public void addLinkedDocument(DocumentLinkDTO docLinkVO) throws WorkflowException{
//	   try{
//		   if(DocumentLinkDTO.checkDocLink(docLinkVO))
//			   getWorkflowUtility().addDocumentLink(docLinkVO);
//	   }
//	   catch(Exception e){
//		   throw handleExceptionAsRuntime(e); 
//	   } 
//   }
//   
//   //get link from orgn doc to a specifc doc
//   public DocumentLinkDTO getLinkedDocument(DocumentLinkDTO docLinkVO) throws WorkflowException{
//	   try{
//		   if(DocumentLinkDTO.checkDocLink(docLinkVO))
//			   return getWorkflowUtility().getLinkedDocument(docLinkVO);
//		   else
//			   return null;
//	   }
//	   catch(Exception e){
//		   throw handleExceptionAsRuntime(e); 
//	   }
//   }
//   
//   //get all links to orgn doc
//   public List<DocumentLinkDTO> getLinkedDocumentsByDocId(String documentId) throws WorkflowException{
//	   if(documentId == null)
//		   throw new WorkflowException("document Id is null");
//	   try{   
//		   return getWorkflowUtility().getLinkedDocumentsByDocId(documentId);
//	   } 
//	   catch (Exception e) {
//		   throw handleExceptionAsRuntime(e);
//	   }
//   }
//   
//   //remove all links from orgn: double links removed
//   public void removeLinkedDocuments(String docId) throws WorkflowException{
//	   
//	   if(docId == null)
//		   throw new WorkflowException("doc id is null");
//	   
//	   try{   
//		   getWorkflowUtility().deleteDocumentLinksByDocId(docId);
//	   } 
//	   catch (Exception e) {
//		   throw handleExceptionAsRuntime(e);
//	   }
//   }
//   
//   //remove link between 2 docs, double link removed
//   public void removeLinkedDocument(DocumentLinkDTO docLinkVO) throws WorkflowException{
//	   
//	   try{
//		   if(DocumentLinkDTO.checkDocLink(docLinkVO))
//			   getWorkflowUtility().deleteDocumentLink(docLinkVO);
//	   }
//	   catch(Exception e){
//		   throw handleExceptionAsRuntime(e); 
//	   } 
//   }
   
   protected static class ModifiableDocumentContent implements Serializable {
	   
	   private static final long serialVersionUID = -4458431160327214042L;

	   private boolean dirty;
	   private DocumentContent originalDocumentContent;
	   private DocumentContentUpdate.Builder builder;
	   
	   protected ModifiableDocumentContent(DocumentContent documentContent) {
		   this.dirty = false;
		   this.originalDocumentContent = documentContent;
		   this.builder = DocumentContentUpdate.Builder.create(documentContent);
	   }
	   
	   protected DocumentContent getDocumentContent() {
		   if (!dirty) {
			   return originalDocumentContent;
		   }
		   DocumentContent.Builder documentContentBuilder = DocumentContent.Builder.create(originalDocumentContent);
		   documentContentBuilder.setApplicationContent(builder.getApplicationContent());
		   documentContentBuilder.setAttributeContent(builder.getApplicationContent());
		   documentContentBuilder.setSearchableContent(builder.getSearchableContent());
		   return documentContentBuilder.build();
	   }
	   
	   protected DocumentContentUpdate.Builder getBuilder() {
		   return builder;
	   }
	   
	   protected void addAttributeDefinition(WorkflowAttributeDefinition definition) {
		   builder.getAttributeDefinitions().add(definition);
		   dirty = true;
	   }
	   
	   protected void removeAttributeDefinition(WorkflowAttributeDefinition definition) {
		   builder.getAttributeDefinitions().remove(definition);
		   dirty = true;
	   }
	   
	   protected List<WorkflowAttributeDefinition> getAttributeDefinitions() {
		   return builder.getAttributeDefinitions();
	   }

	   protected void addSearchableDefinition(WorkflowAttributeDefinition definition) {
		   builder.getSearchableDefinitions().add(definition);
		   dirty = true;
	   }

	   protected void removeSearchableDefinition(WorkflowAttributeDefinition definition) {
		   builder.getSearchableDefinitions().remove(definition);
		   dirty = true;
	   }
	   
	   protected List<WorkflowAttributeDefinition> getSearchableDefinitions() {
		   return builder.getAttributeDefinitions();
	   }

	   protected void setApplicationContent(String applicationContent) {
		   builder.setApplicationContent(applicationContent);
		   dirty = true;
	   }

	   protected void setAttributeContent(String attributeContent) {
		   builder.setAttributeContent(attributeContent);
		   dirty = true;
	   }

	   public void setAttributeDefinitions(List<WorkflowAttributeDefinition> attributeDefinitions) {
		   builder.setAttributeDefinitions(attributeDefinitions);
		   dirty = true;
	   }

	   public void setSearchableContent(String searchableContent) {
		   builder.setSearchableContent(searchableContent);
		   dirty = true;
	   }

	   public void setSearchableDefinitions(List<WorkflowAttributeDefinition> searchableDefinitions) {
		   builder.setSearchableDefinitions(searchableDefinitions);
		   dirty = true;
	   }
	   
	   boolean isDirty() {
		   return dirty;
	   }
	   	   
   }

	protected static class ModifiableDocument implements Serializable {

		private static final long serialVersionUID = -3234793238863410378L;

		private boolean dirty;
		private Document originalDocument;
		private DocumentUpdate.Builder builder;

		protected ModifiableDocument(Document document) {
			this.dirty = false;
			this.originalDocument = document;
			this.builder = DocumentUpdate.Builder.create(document);
		}

		protected Document getDocument() {
			if (!dirty) {
				return originalDocument;
			}
			Document.Builder documentBuilder = Document.Builder.create(originalDocument);
			documentBuilder.setApplicationDocumentId(builder.getApplicationDocumentId());
			documentBuilder.setTitle(builder.getTitle());
			return documentBuilder.build();
		}

		protected DocumentUpdate.Builder getBuilder() {
			return builder;
		}
		
		/**
		 * Immutable value which is accessed frequently, provide direct access to it.
		 */
		protected String getDocumentId() {
			return originalDocument.getDocumentId();
		}
		
		/**
		 * Immutable value which is accessed frequently, provide direct access to it.
		 */
		protected DateTime getDateCreated() {
			return originalDocument.getDateCreated();
		}

		protected String getApplicationDocumentId() {
			return builder.getApplicationDocumentId();
		}
		
		protected void setApplicationDocumentId(String applicationDocumentId) {
			builder.setApplicationDocumentId(applicationDocumentId);
			dirty = true;
		}

		protected String getTitle() {
			return builder.getTitle();
		}
		
		protected void setTitle(String title) {
			builder.setTitle(title);
			dirty = true;
		}
		
		protected String getApplicationDocumentStatus() {
			return builder.getApplicationDocumentStatus();
		}
		
		protected void setApplicationDocumentStatus(String applicationDocumentStatus) {
			builder.setApplicationDocumentStatus(applicationDocumentStatus);
			dirty = true;
		}

		boolean isDirty() {
			return dirty;
		}

	}
   
}

/*
 * Copyright 2006-2011 The Kuali Foundation
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
package org.kuali.rice.kew.actionitem;


import org.junit.Test;
import org.kuali.rice.kew.actionlist.service.ActionListService;
import org.kuali.rice.kew.dto.ActionRequestDTO;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.service.WorkflowDocument;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.util.KEWConstants;
import org.kuali.rice.kim.api.group.GroupMember;
import org.kuali.rice.kim.api.group.GroupService;
import org.kuali.rice.kim.api.services.KIMServiceLocator;
import org.kuali.rice.kim.service.KIMServiceLocatorInternal;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.bo.entity.KimPrincipal;
import org.kuali.rice.kim.util.KimConstants;
import org.kuali.rice.test.BaselineTestCase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;
@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.NONE)
public class ActionItemServiceTest extends KEWTestCase {

	private ActionListService actionListService;

    protected void loadTestData() throws Exception {
    	loadXmlFile("ActionItemConfig.xml");
    }

    protected void setUpAfterDataLoad() throws Exception {
		super.setUpAfterDataLoad();
		actionListService = KEWServiceLocator.getActionListService();
	}

	/**
     * When workgroup membership changes all action items to that workgroup need to reflect
     * the new membership
     *
     * @throws Exception
     */
    @Test public void testUpdateActionItemsForWorkgroupChange() throws Exception {

        WorkflowDocument document = new WorkflowDocument(getPrincipalIdForName("user1"), "ActionItemDocumentType");
        document.setTitle("");
        document.routeDocument("");

        Group oldWorkgroup = KIMServiceLocator.getIdentityManagementService().getGroupByName("KR-WKFLW", "AIWG-Admin");
        //Group oldWorkgroup = this.getGroupImpl(oldGroup.getId());


        GroupService groupService = KIMServiceLocator.getGroupService();
        assertEquals("Workgroup should have 6 members.", 6, groupService.getMembersOfGroup(oldWorkgroup.getId()).size());

        KimPrincipal user1 = KIMServiceLocator.getIdentityManagementService().getPrincipalByPrincipalName("user1");
        KimPrincipal user2 = KIMServiceLocator.getIdentityManagementService().getPrincipalByPrincipalName("user2");

        KimPrincipal rkirkend = KIMServiceLocator.getIdentityManagementService().getPrincipalByPrincipalName("rkirkend");
        KimPrincipal shenl = KIMServiceLocator.getIdentityManagementService().getPrincipalByPrincipalName("shenl");

        List<GroupMember> usersToRemove = new ArrayList<GroupMember>();
        //remove 'rkirkend' and 'shenl' from the workgroup
        Collection<GroupMember> members = groupService.getMembersOfGroup(oldWorkgroup.getId());
        for (GroupMember recipient : members) {
        		if (recipient.getMemberId().equals(rkirkend.getPrincipalId()) || recipient.getMemberId().equals(shenl.getPrincipalId())) {
        			KIMServiceLocatorInternal.getGroupUpdateService().removePrincipalFromGroup(recipient.getMemberId(), oldWorkgroup.getId());
        		}

        }

        //add user1 and user2
        KIMServiceLocatorInternal.getGroupUpdateService().addPrincipalToGroup(user1.getPrincipalId(), oldWorkgroup.getId());
        KIMServiceLocatorInternal.getGroupUpdateService().addPrincipalToGroup(user2.getPrincipalId(), oldWorkgroup.getId());


        // verify that the new workgroup is sane...
        Group loadedNewWorkgroup = this.getGroup(oldWorkgroup.getId());

        boolean foundUser1 = false;
        boolean foundUser2 = false;
        Collection<GroupMember> loadedNewGroupMembers = groupService.getMembersOfGroup(loadedNewWorkgroup.getId());
        assertEquals("Workgroup should have 6 members.", 6, loadedNewGroupMembers.size());


        for (GroupMember recipient : loadedNewGroupMembers) {
    		if (recipient.getMemberId().equals(user1.getPrincipalId())){
    			foundUser1 = true;
    		} else if (recipient.getMemberId().equals(user2.getPrincipalId())){
    			foundUser2 = true;
    		}
    }

        assertTrue("Did not find user 1 on workgroup.", foundUser1);
        assertTrue("Did not find user 2 on workgroup.", foundUser2);

        Collection actionItems = KEWServiceLocator.getActionListService().findByRouteHeaderId(document.getRouteHeaderId());
        boolean foundrkirkend = false;
        boolean foundlshen = false;
        boolean founduser1 = false;
        boolean founduser2 = false;

        for (Iterator iter = actionItems.iterator(); iter.hasNext();) {
            ActionItem actionItem = (ActionItem) iter.next();
            String authId = actionItem.getPrincipal().getPrincipalName();
            if (authId.equals("rkirkend")) {
                foundrkirkend = true;
            } else if (authId.equals("user1")) {
                founduser1 = true;
            } else if (authId.equals("lshen")) {
                foundlshen = true;
            } else if (authId.equals("user2")) {
                founduser2 = true;
            }
        }

        assertTrue("rkirkend should still have an AI because he is in 2 workgroups that are routed to.", foundrkirkend);
        assertTrue("user1 should have an AI because they were added to 'AIWG-Admin'", founduser1);
        assertTrue("user2 should have an AI because they were added to 'AIWG-Admin'", founduser2);
        assertFalse("lshen should not have an AI because they were removed from 'AIWG-Admin'", foundlshen);

    }

    /**
     * When workgroup membership changes all action items to that workgroup need to reflect
     * the new membership even in the case of nested workgroups.
     *
     * @throws Exception
     */

    @Test public void testUpdateActionItemsForNestedGroupChange() throws Exception {

        WorkflowDocument document = new WorkflowDocument(getPrincipalIdForName("user1"), "ActionItemDocumentType");
        document.setTitle("");

        Group workgroup1 = KIMServiceLocator.getIdentityManagementService().getGroupByName("KR-WKFLW", "AIWG-Admin");
        document.adHocRouteDocumentToGroup(KEWConstants.ACTION_REQUEST_APPROVE_REQ, "",workgroup1.getId(), "", true);
        document.routeDocument("");

        KimPrincipal ewestfal = KIMServiceLocator.getIdentityManagementService().getPrincipalByPrincipalName("ewestfal");

        assertEquals("User should have 1 action item", 1, KEWServiceLocator.getActionListService().findByPrincipalId(ewestfal.getPrincipalId()).size());
        assertEquals("Workgroup should have 6 members.", 6, KIMServiceLocator.getGroupService().getMemberPrincipalIds(workgroup1.getId()).size());
        KIMServiceLocator.getIdentityManagementService().removePrincipalFromGroup(ewestfal.getPrincipalId(), workgroup1.getId());

        assertEquals("Workgroup should have 5 members.", 5, KIMServiceLocator.getGroupService().getMemberPrincipalIds(workgroup1.getId()).size());
        assertEquals("User should have 0 action item", 0, KEWServiceLocator.getActionListService().findByPrincipalId(ewestfal.getPrincipalId()).size());

         KIMServiceLocator.getIdentityManagementService().addPrincipalToGroup(ewestfal.getPrincipalId(), workgroup1.getId());
         assertEquals("Workgroup should have 6 members.", 6, KIMServiceLocator.getGroupService().getMemberPrincipalIds(workgroup1.getId()).size());
         assertEquals("User should have 1 action item", 1, KEWServiceLocator.getActionListService().findByPrincipalId(ewestfal.getPrincipalId()).size());


         // test the save group
         Group workgroup1Impl = this.getGroup(workgroup1.getId());
         KimPrincipal dewey = KIMServiceLocator.getIdentityManagementService().getPrincipalByPrincipalName("dewey");
         KIMServiceLocatorInternal.getGroupUpdateService().addPrincipalToGroup(dewey.getPrincipalId(), workgroup1Impl.getId());
         //GroupMember groupMember = GroupMember.Builder.create(workgroup1Impl.getId(), dewey.getPrincipalId(), KimGroupMemberTypes.PRINCIPAL_MEMBER_TYPE).build();
         //GroupBo workgroup1Bo = GroupBo.from(workgroup1Impl);
         //workgroup1Bo.getMembersOfGroup().add(GroupMemberBo.from(groupMember));
         //workgroup1Impl.getMembersOfGroup().add(groupMember);

         //KIMServiceLocatorInternal.getGroupInternalService().saveWorkgroup(workgroup1Bo);

         assertEquals("Workgroup should have 7 members.", 7, KIMServiceLocator.getGroupService().getMemberPrincipalIds(workgroup1.getId()).size());
         assertEquals("User should have 1 action item", 1, KEWServiceLocator.getActionListService().findByPrincipalId(dewey.getPrincipalId()).size());

         // test nested
         KimPrincipal user1 = KIMServiceLocator.getIdentityManagementService().getPrincipalByPrincipalName("user1");

         document = new WorkflowDocument(getPrincipalIdForName("jhopf"), "ActionItemDocumentType");
         document.setTitle("");

         workgroup1 = KIMServiceLocator.getIdentityManagementService().getGroupByName("KR-WKFLW", "AIWG-Nested1");
         document.adHocRouteDocumentToGroup(KEWConstants.ACTION_REQUEST_APPROVE_REQ, "",workgroup1.getId(), "", true);
         document.routeDocument("");

         assertEquals("User should have 1 action item", 1, KEWServiceLocator.getActionListService().findByPrincipalId(user1.getPrincipalId()).size());
         assertEquals("Workgroup should have 6 members.", 6, KIMServiceLocator.getGroupService().getMemberPrincipalIds(workgroup1.getId()).size());

         //get the subgroup so we can remove the member.
         Group workgroupSub = KIMServiceLocator.getIdentityManagementService().getGroupByName("KR-WKFLW", "AIWG-Nested2");
         KIMServiceLocator.getIdentityManagementService().removePrincipalFromGroup(user1.getPrincipalId(), workgroupSub.getId());

         assertEquals("Workgroup should have 5 members.", 5, KIMServiceLocator.getGroupService().getMemberPrincipalIds(workgroup1.getId()).size());
         assertEquals("User should have 0 action item", 0, KEWServiceLocator.getActionListService().findByPrincipalId(user1.getPrincipalId()).size());

          KIMServiceLocator.getIdentityManagementService().addPrincipalToGroup(user1.getPrincipalId(), workgroupSub.getId());
          assertEquals("Workgroup should have 6 members.", 6, KIMServiceLocator.getGroupService().getMemberPrincipalIds(workgroup1.getId()).size());
          assertEquals("User should have 1 action item", 1, KEWServiceLocator.getActionListService().findByPrincipalId(user1.getPrincipalId()).size());

    }

    /**
     * addresses the following bug http://fms.dfa.cornell.edu:8080/browse/KULWF-428
     *
     * @throws Exception
     */
    @Test public void testWorkgroupActionItemGenerationWhenMultipleWorkgroupRequests() throws Exception {
        WorkflowDocument document = new WorkflowDocument(getPrincipalIdForName("user1"), "ActionItemDocumentType");
        document.setTitle("");
        document.routeDocument("");

        document = new WorkflowDocument(getPrincipalIdForName("jitrue"), document.getRouteHeaderId());

        Group testGroup = KIMServiceLocator.getIdentityManagementService().getGroupByName(KimConstants.KIM_GROUP_WORKFLOW_NAMESPACE_CODE, "AIWG-Test");
        Group adminGroup = KIMServiceLocator.getIdentityManagementService().getGroupByName(KimConstants.KIM_GROUP_WORKFLOW_NAMESPACE_CODE, "AIWG-Admin");

        ActionRequestDTO[] ars = document.getActionRequests();
        boolean routedWorkflowAdmin = false;
        boolean routedTestWorkgroup = false;
        for (int i = 0; i < ars.length; i++) {
            ActionRequestDTO request = ars[i];
            if (request.isGroupRequest() && testGroup.getId().equals(request.getGroupId())) {
                routedTestWorkgroup = true;
            } else if (request.isGroupRequest() && adminGroup.getId().equals(request.getGroupId())) {
                routedWorkflowAdmin = true;
            }
        }

        //verify that our test is sane
        assertTrue("Should have routed to 'AIWG-Test'", routedTestWorkgroup);
        assertTrue("Should have routed to 'AIWG-Admin'", routedWorkflowAdmin);
        assertTrue("Approve should be requested to member of 'AIWG-Test'", document.isApprovalRequested());

        document.approve("");

        Collection actionItems = KEWServiceLocator.getActionListService().findByRouteHeaderId(document.getRouteHeaderId());

        assertEquals("There should be 6 action items to the AIWG-Admin.", 6, actionItems.size());

        for (Iterator iter = actionItems.iterator(); iter.hasNext();) {
            ActionItem actionItem = (ActionItem)iter.next();
            //don't worry about which workgroup - they can get activated in any order
            assertNotNull("this should be a workgroup request", actionItem.getGroup());
        }
    }

    /**
     * This test verifies that if someone gets more than one request routed to them then they will get
     * multiple Action Items but only one of them will show up in their Action List.
     */
    @Test public void testMultipleActionItemGeneration() throws Exception {
    	WorkflowDocument document = new WorkflowDocument(getPrincipalIdForName("user1"), "ActionItemDocumentType");
        document.setTitle("");
        document.routeDocument("");

        // now the document should be at both the AIWG-Admin workgroup and the AIWG-Test
        // ewestfal is a member of both Workgroups so verify that he has two action items
        String ewestfalPrincipalId = getPrincipalIdForName("ewestfal");
        String jitruePrincipalId = getPrincipalIdForName("jitrue");

        Collection actionItems = KEWServiceLocator.getActionListService().findByWorkflowUserRouteHeaderId(ewestfalPrincipalId, document.getRouteHeaderId());
        assertEquals("Ewestfal should have two action items.", 2, actionItems.size());

        // now check the action list, there should be only one entry
        actionItems = KEWServiceLocator.getActionListService().getActionList(ewestfalPrincipalId, null);
        assertEquals("Ewestfal should have one action item in his action list.", 1, actionItems.size());
        document = new WorkflowDocument(ewestfalPrincipalId, document.getRouteHeaderId());
        assertTrue("Ewestfal should have an approval requested.", document.isApprovalRequested());

        // approve as a member from the first workgroup
        document = new WorkflowDocument(jitruePrincipalId, document.getRouteHeaderId());
        assertTrue("Jitrue should have an approval requested.", document.isApprovalRequested());
        document.approve("");

        // now ewestfal should have only one action item in both his action items and his action list
        actionItems = KEWServiceLocator.getActionListService().findByWorkflowUserRouteHeaderId(ewestfalPrincipalId, document.getRouteHeaderId());
        assertEquals("Ewestfal should have one action item.", 1, actionItems.size());
        Long actionItemId = ((ActionItem)actionItems.iterator().next()).getActionItemId();
        actionItems = KEWServiceLocator.getActionListService().getActionList(ewestfalPrincipalId, null);
        assertEquals("Ewestfal should have one action item in his action list.", 1, actionItems.size());
        assertEquals("The two action items should be the same.", actionItemId, ((ActionItem)actionItems.iterator().next()).getActionItemId());
    }

    /**
     * This tests verifies that bug KULWF-507 has been fixed:
     *
     * https://test.kuali.org/jira/browse/KULWF-507
     *
     * To fix this, we implemented the system so that multiple action items are generated rather then just
     * one which gets reassigned across multiple requests as needed.
     *
     * This test verifies that after the blanket approval, there should no longer be an orphaned Acknowledge
     * request.  The workgroup used here is the TestWorkgroup and "user1" is ewestfal with "user2" as rkirkend.
     *
     * The routing is configured in the BAOrphanedRequestDocumentType.
     */
    @Test public void testOrphanedAcknowledgeFromBlanketApprovalFix() throws Exception {
    	WorkflowDocument document = new WorkflowDocument(getPrincipalIdForName("ewestfal"), "BAOrphanedRequestDocumentType");
    	document.blanketApprove("");
    	assertTrue("Document should be processed.", document.stateIsProcessed());

    	// after the document has blanket approved there should be 2 action items since the blanket approver
    	// is in the final workgroup.  These action items should be the acknowledges generated to both
    	// rkirkend and user1
    	int numActionItems = actionListService.findByRouteHeaderId(document.getRouteHeaderId()).size();
    	assertEquals("Incorrect number of action items.", 2, numActionItems);

    	String user1PrincipalId = getPrincipalIdForName("user1");
    	String rkirkendPrincipalId = getPrincipalIdForName("rkirkend");

    	// check that user1 has 1 action item
    	Collection actionItems = actionListService.findByWorkflowUserRouteHeaderId(user1PrincipalId, document.getRouteHeaderId());
    	assertEquals("user1 should have one action item.", 1, actionItems.size());

    	// check that rkirkend still has 1, the is where the bug would have manifested itself before, rkirkend would have had
    	// no action item (hence the orphaned request)
    	actionItems = actionListService.findByWorkflowUserRouteHeaderId(rkirkendPrincipalId, document.getRouteHeaderId());
    	assertEquals("rkirkend should have one action item.", 1, actionItems.size());

    	// lets go ahead and take it to final for funsies
    	document = new WorkflowDocument(rkirkendPrincipalId, document.getRouteHeaderId());
    	assertTrue("Should have ack request.", document.isAcknowledgeRequested());
    	document.acknowledge("");
    	assertTrue("Should still be PROCESSED.", document.stateIsProcessed());

    	document = new WorkflowDocument(user1PrincipalId, document.getRouteHeaderId());
    	assertTrue("Should have ack request.", document.isAcknowledgeRequested());
    	document.acknowledge("");
    	assertTrue("Should now be FINAL.", document.stateIsFinal());
    }

    /**
     * Executes a deep copy of the BaseWorkgroup
     */
 /*   private BaseWorkgroup copy(BaseWorkgroup workgroup) throws Exception {
    	BaseWorkgroup workgroupCopy = (BaseWorkgroup)KEWServiceLocator.getWorkgroupService().copy(workgroup);
    	// copy above does a shallow copy so we need to deep copy members
    	List<BaseWorkgroupMember> members = workgroupCopy.getWorkgroupMembers();
    	List<BaseWorkgroupMember> membersCopy = new ArrayList<BaseWorkgroupMember>();
    	for (BaseWorkgroupMember member : members) {
    		membersCopy.add(copy(member));
    	}
    	workgroupCopy.setWorkgroupMembers(membersCopy);
    	workgroupCopy.setMembers(new ArrayList<Recipient>());
    	workgroupCopy.materializeMembers();
    	return workgroupCopy;
    }

    private BaseWorkgroupMember copy(BaseWorkgroupMember member) throws Exception {
    	return (BaseWorkgroupMember)BeanUtils.cloneBean(member);
    }
*/

    protected Group getGroup(String groupId) {
		return KIMServiceLocator.getGroupService().getGroup(groupId);
    }
}

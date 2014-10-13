/**
 * Copyright 2005-2014 The Kuali Foundation
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
package org.kuali.rice.krms.test;

import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.krms.api.repository.agenda.AgendaDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaItemDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaItemDefinitionContract;
import org.kuali.rice.krms.api.repository.context.ContextDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsAttributeDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition;
import org.kuali.rice.krms.impl.repository.ActionAttributeBo;
import org.kuali.rice.krms.impl.repository.ActionBo;
import org.kuali.rice.krms.impl.repository.AgendaBo;
import org.kuali.rice.krms.impl.repository.AgendaItemBo;
import org.kuali.rice.krms.impl.repository.KrmsAttributeDefinitionBo;
import org.kuali.rice.test.BaselineTestCase;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Integration test for the AgendaBoService.  Note that we inherit the test data created by AbstractAgendaBoTest, and
 * test the service against that data.
 */
@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.CLEAR_DB)
public class AgendaBoServiceTest extends AbstractAgendaBoTest {

    private static final String NULL = new String("null"); // null String.  Hah. Using it as a "null object".

    private static <A> A nullConvertingGet(List<A> list, int index) {
        // being lazy, no input checks here
        A result = list.get(index);
        if (result == NULL) result = null;
        return result;
    }

    @Test public void testGetByContextId() {

        assertTrue(CollectionUtils.isEmpty(getAgendaBoService().getAgendasByContextId("#$^$ BogusContextId !@#$")));
        assertTrue(CollectionUtils.isEmpty(getAgendaBoService().getAgendaItemsByContext("#$^$ BogusContextId !@#$")));

        for (String contextName : Arrays.asList(CONTEXT1, CONTEXT2, CONTEXT3)) {
            String namespace = getNamespaceByContextName(contextName);
            if (StringUtils.isBlank(namespace)) {
                throw new RiceRuntimeException("namespace is " + namespace + " for context with name " + contextName);
            }

            String contextId = getContextRepository().getContextByNameAndNamespace(contextName, namespace).getId();

            List<AgendaDefinition> agendas = getAgendaBoService().getAgendasByContextId(contextId);
            List<AgendaItemDefinition> agendaItems = getAgendaBoService().getAgendaItemsByContext(contextId);

            assertEquals("agenda count doesn't match our tally for context " + contextName, agendas.size(),
                    getBoService().countMatching(AgendaBo.class, Collections.singletonMap("contextId", contextId)));

            int totalAgendaItems = 0; // count agenda items in the context for verification purposes

            Set<String> agendaIds = new HashSet<String>(); // build set of agenda ids, also for verification purposes

            for (AgendaDefinition agenda : agendas) {
                assertEquals("agenda w/ ID "+ agenda.getId() +" has a context ID that doesn't match",
                        agenda.getContextId(), contextId);

                totalAgendaItems += getBoService().countMatching(
                        AgendaItemBo.class, Collections.singletonMap("agendaId", agenda.getId())
                );

                agendaIds.add(agenda.getId());
            }

            for (AgendaItemDefinition agendaItem : agendaItems) {
                assertTrue("agenda item is not part of any agendas in " + contextName,
                        agendaIds.contains(agendaItem.getAgendaId()));
            }

            assertEquals("number of agenda items doesn't match our tally", agendaItems.size(), totalAgendaItems);
        }

    }

    @Test public void testGetAgendasByContextId_nullOrBlank() {

        for (String contextId : Arrays.asList(null, "", " ")) {
            try {
                getAgendaBoService().getAgendasByContextId(contextId);
                fail("getAgendasByContextId should have thrown "+ RiceIllegalArgumentException.class.getSimpleName() +
                        " for invalid contextId=" + contextId +".");
            } catch (RiceIllegalArgumentException e) {
                // good, that's what it should do
            }
        }
    }

    @Test public void testGetAgendaItemsByContextId_nullOrBlank() {

        for (String contextId : Arrays.asList(null, "", " ")) {
            try {
                getAgendaBoService().getAgendaItemsByContext(contextId);
                fail("getAgendaItemsByContext should have thrown "+ RiceIllegalArgumentException.class.getSimpleName() +
                        " for invalid contextId=" + contextId +".");
            } catch (RiceIllegalArgumentException e) {
                // good, that's what it should do
            }
        }
    }

    @Test
    public void testGetByType() {

        assertTrue(CollectionUtils.isEmpty(getAgendaBoService().getAgendasByType("#$^$ BogusTypeId !@#$")));
        assertTrue(CollectionUtils.isEmpty(getAgendaBoService().getAgendaItemsByType("#$^$ BogusTypeId !@#$")));

        List<KrmsTypeDefinition> agendaTypes =  getAgendaTypesForContexts(Arrays.asList(CONTEXT1, CONTEXT2, CONTEXT3));

        assertTrue("We must have some types to test with or we prove nothing", agendaTypes.size() > 0);

        for (KrmsTypeDefinition agendaType : agendaTypes) {
            String typeName = agendaType.getName();
            String typeNamespace = agendaType.getNamespace();

            KrmsTypeDefinition type = getKrmsTypeRepository().getTypeByName(typeNamespace, typeName);

            List<AgendaDefinition> agendas = getAgendaBoService().getAgendasByType(type.getId());
            List<AgendaItemDefinition> agendaItems = getAgendaBoService().getAgendaItemsByType(type.getId());


            assertEquals("agenda count doesn't match our tally for type " + typeNamespace+":"+typeName,
                    agendas.size(), getBoService().countMatching(
                    AgendaBo.class, Collections.singletonMap("typeId", type.getId()))
            );

            int totalAgendaItems = 0; // count agenda items in the type for verification purposes

            Set<String> agendaIds = new HashSet<String>(); // build set of agenda ids, also for verification purposes

            for (AgendaDefinition agenda : agendas) {
                assertEquals("agenda w/ ID "+ agenda.getTypeId() +" has a type ID that doesn't match",
                        agenda.getTypeId(), type.getId());

                totalAgendaItems += getBoService().countMatching(
                        AgendaItemBo.class, Collections.singletonMap("agendaId", agenda.getId())
                );

                agendaIds.add(agenda.getId());
            }

            for (AgendaItemDefinition agendaItem : agendaItems) {
                assertTrue("agenda item is not part of any agendas in type " + typeNamespace+":"+typeName,
                        agendaIds.contains(agendaItem.getAgendaId()));
            }

            assertEquals("number of agenda items doesn't match our tally", agendaItems.size(), totalAgendaItems);

        }
    }

    private List<KrmsTypeDefinition> getAgendaTypesForContexts(List<String> contextNames) {
        List<KrmsTypeDefinition> results = new ArrayList<KrmsTypeDefinition>();

        // collect all the types used for the agendas in our contexts
        for (String contextName : contextNames) {
            String namespace = getNamespaceByContextName(contextName);
            if (StringUtils.isBlank(namespace)) {
                throw new RiceRuntimeException("namespace is " + namespace + " for context with name " + contextName);
            }

            String contextId = getContextRepository().getContextByNameAndNamespace(contextName, namespace).getId();

            // depending on good behavior of getAgendasByContextId which is tested elsewhere
            List<AgendaDefinition> agendas = getAgendaBoService().getAgendasByContextId(contextId);

            // stacked filters here
            if (!CollectionUtils.isEmpty(agendas)) {
                for (AgendaDefinition agenda : agendas) {
                    if (agenda.getTypeId() != null) {
                        KrmsTypeDefinition type = getKrmsTypeRepository().getTypeById(agenda.getTypeId());

                        // we depend on working hashcode & equals for KrmsTypeDefinition here
                        if (!results.contains(type)) {
                            results.add(type);
                        }
                    }
                }
            }
        }
        return results;
    }

    @Test public void testGetAgendasByType_nullOrBlank() {

        for (String contextId : Arrays.asList(null, "", " ")) {
            try {
                getAgendaBoService().getAgendasByType(contextId);
                fail("getAgendasByType should have thrown "+ RiceIllegalArgumentException.class.getSimpleName() +
                        " for invalid contextId=" + contextId +".");
            } catch (RiceIllegalArgumentException e) {
                // good, that's what it should do
            }
        }
    }

    @Test public void testGetAgendaItemsByType_nullOrBlank() {

        for (String contextId : Arrays.asList(null, "", " ")) {
            try {
                getAgendaBoService().getAgendaItemsByType(contextId);
                fail("getAgendaItemsByType should have thrown "+ RiceIllegalArgumentException.class.getSimpleName() +
                        " for invalid contextId=" + contextId +".");
            } catch (RiceIllegalArgumentException e) {
                // good, that's what it should do
            }
        }
    }

    @Test public void testGetByTypeAndContext() {

        boolean testedSomeTypes = false;

        for (String contextName : Arrays.asList(CONTEXT1, CONTEXT2, CONTEXT3)) {

            List<KrmsTypeDefinition> agendaTypes =  getAgendaTypesForContexts(Collections.singletonList(contextName));

            String namespace = getNamespaceByContextName(contextName);
            if (StringUtils.isBlank(namespace)) {
                throw new RiceRuntimeException("namespace is " + namespace + " for context with name " + contextName);
            }

            ContextDefinition context = getContextRepository().getContextByNameAndNamespace(contextName, namespace);

            for (KrmsTypeDefinition agendaType : agendaTypes) {

                testedSomeTypes = true; // prove we got to the inner loop

                assertTrue(CollectionUtils.isEmpty(getAgendaBoService().getAgendasByTypeAndContext("#$^$ BogusTypeId !@#$", context.getId())));
                assertTrue(CollectionUtils.isEmpty(getAgendaBoService().getAgendaItemsByTypeAndContext("#$^$ BogusTypeId !@#$", context.getId())));
                assertTrue(CollectionUtils.isEmpty(getAgendaBoService().getAgendasByTypeAndContext(agendaType.getId(), "#$^$ BogusContextId !@#$")));
                assertTrue(CollectionUtils.isEmpty(getAgendaBoService().getAgendaItemsByTypeAndContext(
                        agendaType.getId(), "#$^$ BogusContextId !@#$")));

                List<AgendaDefinition> agendas = getAgendaBoService().getAgendasByTypeAndContext(agendaType.getId(), context.getId());
                List<AgendaItemDefinition> agendaItems = getAgendaBoService().getAgendaItemsByTypeAndContext(agendaType.getId(), context.getId());

                Map<String, String> agendaCountCrit = new HashMap<String, String>();
                agendaCountCrit.put("typeId", agendaType.getId());
                agendaCountCrit.put("contextId", context.getId());
                assertEquals(
                        "agenda count doesn't match our tally for type " + agendaType.getNamespace() + ":" + agendaType
                                .getName(), agendas.size(), getBoService().countMatching(AgendaBo.class,
                        agendaCountCrit));

                int totalAgendaItems = 0; // count agenda items in the type for verification purposes

                Set<String> agendaIds = new HashSet<String>(); // build set of agenda ids, also for verification purposes

                for (AgendaDefinition agenda : agendas) {
                    assertEquals("agenda w/ ID "+ agenda.getTypeId() +" has a type ID that doesn't match",
                            agenda.getTypeId(), agendaType.getId());

                    totalAgendaItems += getBoService().countMatching(
                            AgendaItemBo.class, Collections.singletonMap("agendaId", agenda.getId())
                    );

                    agendaIds.add(agenda.getId());
                }

                for (AgendaItemDefinition agendaItem : agendaItems) {
                    String assertionString = "agenda item is not part of any agendas in type " +
                            agendaType.getNamespace()+":"+agendaType.getName() +
                            " and context " + context.getNamespace()+":"+context.getName();

                    assertTrue(assertionString, agendaIds.contains(agendaItem.getAgendaId())
                    );
                }

                assertEquals("number of agenda items doesn't match our tally", agendaItems.size(), totalAgendaItems);
            }

            assertTrue("We have to test some types or we prove nothing", testedSomeTypes);

        }
    }

    @Test public void testGetAgendaItemsByTypeAndContext_nullOrBlank() {


        Set<String> emptyValues = new HashSet<String>();
        emptyValues.addAll(Arrays.asList(NULL, "", " "));

        Set<String> oneNonBlank = Sets.union(emptyValues, Collections.singleton("fakeId"));
        Set<List<String>> testIds = Sets.union(Sets.cartesianProduct(emptyValues, oneNonBlank),
                Sets.cartesianProduct(oneNonBlank, emptyValues));

        for (List<String> ids : testIds) {
            try {
                getAgendaBoService().getAgendaItemsByTypeAndContext(nullConvertingGet(ids, 0), nullConvertingGet(ids, 1));
                fail("getAgendaItemsByType should have thrown "+ RiceIllegalArgumentException.class.getSimpleName() +
                        " for invalid combo of contextId=" + ids +".");
            } catch (RiceIllegalArgumentException e) {
                // good, that's what it should do
            }
        }
    }

    // TODO:

    // methods left to test:
    //
    // deleteAgendaItem
    // updateAgenda
    // deleteAgenda
    // createAgendaItem

    @Test
    public void testUpdateAgendaItem() {
        ContextDefinition context = getContextRepository().getContextByNameAndNamespace(CONTEXT1, NAMESPACE1);
        assertNotNull("context " + CONTEXT1 + " not found", context);
        AgendaDefinition agenda = getAgendaBoService().getAgendaByNameAndContextId(AGENDA1, context.getId());
        assertNotNull("agenda " + AGENDA1 + " not found", agenda);
        AgendaItemDefinition agendaItem = getAgendaBoService().getAgendaItemById(agenda.getFirstItemId());
        assertNotNull("agenda item " + agenda.getFirstItemId() + " not found", agendaItem);

        KrmsAttributeDefinition attributeDefinition = getKrmsAttributeDefinitionService().createAttributeDefinition(
                KrmsAttributeDefinition.Builder.create(null, ATTRIBUTE1, NAMESPACE1).build());
        KrmsAttributeDefinitionBo attributeDefinitionBo = KrmsAttributeDefinitionBo.from(attributeDefinition);

        // verify the agenda item
        AgendaItemBo agendaItemBo = AgendaItemBo.from(agendaItem);
        assertNotNull("agenda item null", agendaItemBo);
        List<ActionBo> agendaItemActionBos = agendaItemBo.getRule().getActions();
        assertEquals("incorrect number of agenda item rule actions found", 1, agendaItemActionBos.size());
        ActionBo agendaItemActionBo = agendaItemActionBos.get(0);
        assertTrue("agenda item rule action attributes found", agendaItemActionBo.getAttributes().isEmpty());
        assertTrue("agenda item rule action attributes found", agendaItemActionBo.getAttributeBos().isEmpty());

        // verify the child agenda item
        AgendaItemBo alwaysBo = agendaItemBo.getAlways();
        assertNotNull("always agenda item null", alwaysBo);
        List<ActionBo> alwaysActionBos = alwaysBo.getRule().getActions();
        assertEquals("incorrect number of always agenda item rule actions found", 1, alwaysActionBos.size());
        ActionBo alwaysActionBo = alwaysActionBos.get(0);
        assertTrue("always agenda item rule action attributes found", alwaysActionBo.getAttributes().isEmpty());
        assertTrue("always agenda item rule action attributes found", alwaysActionBo.getAttributeBos().isEmpty());

        // add agenda item attribute
        ActionAttributeBo agendaItemActionAttributeBo = new ActionAttributeBo();
        agendaItemActionAttributeBo.setAction(agendaItemActionBo);
        agendaItemActionAttributeBo.setAttributeDefinition(attributeDefinitionBo);
        agendaItemActionAttributeBo.setValue("testAgendaItem");
        agendaItemActionBo.setAttributeBos(Arrays.asList(agendaItemActionAttributeBo));

        // add child agenda item attribute
        ActionAttributeBo whenAlwaysActionAttributeBo = new ActionAttributeBo();
        whenAlwaysActionAttributeBo.setAction(alwaysActionBo);
        whenAlwaysActionAttributeBo.setAttributeDefinition(attributeDefinitionBo);
        whenAlwaysActionAttributeBo.setValue("testAlwaysAgendaItem");
        alwaysActionBo.setAttributeBos(Arrays.asList(whenAlwaysActionAttributeBo));

        // update the agenda item
        AgendaItemDefinition updatedAgendaItem
                = getAgendaBoService().updateAgendaItem(AgendaItemDefinition.Builder.create(agendaItemBo).build());

        // verify the updated agenda item
        AgendaItemBo updatedAgendaItemBo = AgendaItemBo.from(updatedAgendaItem);
        assertNotNull("updated agenda item null", updatedAgendaItemBo);
        List<ActionBo> updatedAgendaItemActionBos = updatedAgendaItemBo.getRule().getActions();
        assertEquals("incorrect number of updated agenda item rule actions found", 1, updatedAgendaItemActionBos.size());
        ActionBo updatedAgendaItemActionBo = updatedAgendaItemActionBos.get(0);
        assertEquals("incorrect number of updated agenda item rule action attributes found", 1, updatedAgendaItemActionBo.getAttributes().size());
        assertEquals("incorrect number of updated agenda item rule action attributes found", 1, updatedAgendaItemActionBo.getAttributeBos().size());

        // verify the updated child agenda item
        AgendaItemBo updatedAlwaysBo = updatedAgendaItemBo.getAlways();
        assertNotNull("updated always agenda item null", updatedAlwaysBo);
        List<ActionBo> updatedAlwaysActionBos = updatedAlwaysBo.getRule().getActions();
        assertEquals("incorrect number of updated always agenda item rule actions found", 1, updatedAlwaysActionBos.size());
        ActionBo updatedAlwaysActionBo = updatedAlwaysActionBos.get(0);
        assertEquals("incorrect number of updated always agenda item rule action attributes found", 1, updatedAlwaysActionBo.getAttributes().size());
        assertEquals("incorrect number of updated always agenda item rule action attributes found", 1, updatedAlwaysActionBo.getAttributeBos().size());
    }

    @Test
    public void testAgendaCrud() {
        String namespace = getNamespaceByContextName(CONTEXT1);
        if (StringUtils.isBlank(namespace)) {
            throw new RiceRuntimeException("namespace is " + namespace + " for context with name " + CONTEXT1);
        }

        ContextDefinition context = getContextRepository().getContextByNameAndNamespace(CONTEXT1, namespace);

        // Get an agenda to use as a template for agenda creation
        List<AgendaDefinition> agendas = getAgendaBoService().getAgendasByContextId(context.getId());
        AgendaDefinition templateAgenda = agendas.get(0);

//        AgendaBo templateAgendaBo = getBoService().findBySinglePrimaryKey(AgendaBo.class, templateAgenda.getId());
//        AgendaBo.to(templateAgendaBo.copyAgenda("newTestAgenda", "FooTime"));


        AgendaDefinition.Builder agendaBuilder = AgendaDefinition.Builder.create(templateAgenda);

        agendaBuilder.setFirstItemId(null);
        agendaBuilder.setId(null);
        agendaBuilder.setVersionNumber(null);
        agendaBuilder.setName("testAgendaCrud-agenda");

        // create agenda
        AgendaDefinition newAgenda = getAgendaBoService().createAgenda(agendaBuilder.build());

        // verify the agenda is there and
        assertNotNull(newAgenda);
        // we depend on working equals for AgendaDefinition here
        assertEquals(newAgenda, getAgendaBoService().getAgendaByAgendaId(newAgenda.getId()));

//        List<AgendaItemDefinition> templateAgendaItems = getAgendaBoService().getAgendaItemsByAgendaId(templateAgenda.getId());
//        List<AgendaItemDefinition.Builder> agendaItemBuilders = new ArrayList<AgendaItemDefinition.Builder>();
//
//        for (AgendaItemDefinition templateAgendaItem : templateAgendaItems) {
//            AgendaItemDefinition.Builder agendaItemBuilder = AgendaItemDefinition.Builder.create(templateAgendaItem);
//            agendaItemBuilder.setAlwaysId(null);
//            agendaItemBuilder.setWhenFalseId(null);
//            agendaItemBuilder.setWhenTrueId(null);
//            agendaItemBuilder.setAgendaId(newAgenda.getId());
//            agendaItemBuilder.set
//        }

    }

}

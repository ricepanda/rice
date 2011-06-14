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





package org.kuali.rice.shareddata.impl.state

import groovy.mock.interceptor.MockFor
import org.junit.Assert
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.kuali.rice.krad.service.BusinessObjectService
import org.kuali.rice.shareddata.api.state.StateService

class StateServiceImplTest {

    private final shouldFail = new GroovyTestCase().&shouldFail

    static sampleStates = new HashMap<List<String, String>, StateBo>()
    static sampleStatesPerCountry = new HashMap<String, List<StateBo>>()

    private MockFor mockBoService
    private BusinessObjectService boService
    StateServiceImpl stateServiceImpl;
    StateService stateService

    @BeforeClass
    static void createSampleStateBOs() {
        def michiganBo = new StateBo(active: true, countryCode: "US", code: "MI",
                name: "Michigan")
        def illinoisBo = new StateBo(active: true, countryCode: "US", code: "IL",
                name: "Illinois")
        def britishColumbiaBo = new StateBo(active: true, countryCode: "CA", code: "BC",
                name: "British Columbia")
        [michiganBo, illinoisBo, britishColumbiaBo].each {
            sampleStates[[it.code, it.countryCode].asImmutable()] = it
        }
        sampleStatesPerCountry["US"] = [michiganBo, illinoisBo]
        sampleStatesPerCountry["CA"] = [britishColumbiaBo]
    }

    @Before
    void setupBoServiceMockContext() {
        mockBoService = new MockFor(BusinessObjectService)
    }

    @Before
    void setupServiceUnderTest() {
        stateServiceImpl = new StateServiceImpl()
        stateService = stateServiceImpl
    }

    void injectBusinessObjectServiceIntoStateService() {
        boService = mockBoService.proxyDelegateInstance()
        stateServiceImpl.setBusinessObjectService(boService)
    }

    @Test
    void test_get_state_null_countryCode() {
        injectBusinessObjectServiceIntoStateService()

        shouldFail(IllegalArgumentException) {
            stateService.getState(null, "MI")
        }
        mockBoService.verify(boService)
    }

    @Test
    void test_get_state_null_code() {
        injectBusinessObjectServiceIntoStateService()

        shouldFail(IllegalArgumentException) {
            stateService.getState("US", null)
        }
        mockBoService.verify(boService)
    }

    @Test
    void test_get_state_exists() {
        mockBoService.demand.findByPrimaryKey(1..1) { clazz, map -> sampleStates[map["countryCode"], [map["code"]]] }
        injectBusinessObjectServiceIntoStateService()
        Assert.assertEquals(StateBo.to(sampleStates[["US", "MI"]]), stateService.getState("US", "MI"))
        mockBoService.verify(boService)
    }

    @Test
    void test_get_state_does_not_exist() {
        mockBoService.demand.findByPrimaryKey(1..1) { clazz, map -> sampleStates[map["countryCode"], [map["code"]]] }
        injectBusinessObjectServiceIntoStateService()
        Assert.assertNull(stateService.getState("FOO", "BAR"))
        mockBoService.verify(boService)
    }

    @Test
    void test_find_all_states_in_country_null_countryCode() {
        injectBusinessObjectServiceIntoStateService()

        shouldFail(IllegalArgumentException) {
            stateService.findAllStatesInCountry(null)
        }
        mockBoService.verify(boService)
    }

    @Test
    void test_find_all_states_in_country_exists() {
        mockBoService.demand.findMatching(1..1) { clazz, map -> sampleStatesPerCountry[map["countryCode"]] }
        injectBusinessObjectServiceIntoStateService()
        def values = stateService.findAllStatesInCountry("US")
        Assert.assertEquals(sampleStatesPerCountry["US"].collect { StateBo.to(it) }, values)

        //is this unmodifiable?
        shouldFail(UnsupportedOperationException) {
            values.add(StateBo.to(sampleStates[["CA", "BC"]]))
        }
        mockBoService.verify(boService)
    }

    @Test
    void test_find_all_states_in_country_does_not_exist() {
        mockBoService.demand.findMatching(1..1) { clazz, map -> sampleStatesPerCountry[map["countryCode"]] }
        injectBusinessObjectServiceIntoStateService()
        def values = stateService.findAllStatesInCountry("FOO")
        Assert.assertEquals([], values)

        //is this unmodifiable?
        shouldFail(UnsupportedOperationException) {
            values.add(StateBo.to(sampleStates[["CA", "BC"]]))
        }

        mockBoService.verify(boService)
    }
}

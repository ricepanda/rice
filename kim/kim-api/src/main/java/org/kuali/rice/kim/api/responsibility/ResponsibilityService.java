/*
 * Copyright 2008 The Kuali Foundation
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
package org.kuali.rice.kim.api.responsibility;

import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.exception.RiceIllegalStateException;
import org.kuali.rice.core.api.mo.common.Attributes;
import org.kuali.rice.kim.api.common.template.Template;
import org.kuali.rice.kim.api.common.template.TemplateQueryResults;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.List;

/**
 * This service provides operations for determining what responsibility actions
 * a principal has and for querying about responsibility data.  It also provides several
 * write operations.
 *
 * <p>
 * A responsibility represents an action that a principal is requested to
 * take.  This is used for defining workflow actions (such as approve,
 * acknowledge, fyi) that the principal has the responsibility to take.  The
 * workflow engine integrates with this service to provide
 * responsibility-driven routing.
 * <p/>
 * <p>
 * A responsibility is very similar to a permission in a couple of ways.
 * First of all, responsibilities are always granted to a role, never assigned
 * directly to a principal or group.  Furthermore, in a similar fashion to
 * permissions, a role has the concept of a responsibility template.  The
 * responsibility template specifies what additional responsibility details
 * need to be defined when the responsibility is created.
 * <p/>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@WebService(name = "ResponsibilityServiceSoap", targetNamespace = CoreConstants.Namespaces.CORE_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface ResponsibilityService {

    /**
     * This will create a {@link Responsibility} exactly like the responsibility passed in.
     *
     * @param responsibility the responsibility to create
     * @return the id of the newly created object.  will never be null.
     * @throws IllegalArgumentException if the responsibility is null
     * @throws IllegalStateException if the responsibility is already existing in the system
     */
    @WebMethod(operationName="createResponsibility")
    @WebResult(name = "responsibility")
    Responsibility createResponsibility(@WebParam(name = "responsibility") Responsibility responsibility)
            throws RiceIllegalArgumentException, RiceIllegalStateException;

    /**
     * This will update a {@link Responsibility}.
     *
     * @param responsibility the responsibility to update
     * @throws IllegalArgumentException if the responsibility is null
     * @throws IllegalStateException if the responsibility does not exist in the system
     */
    @WebMethod(operationName="updateResponsibility")
    @WebResult(name = "responsibility")
    Responsibility updateResponsibility(@WebParam(name = "responsibility") Responsibility responsibility)
            throws RiceIllegalArgumentException, RiceIllegalStateException;


    @WebMethod(operationName = "getResponsibility")
    @WebResult(name = "responsibility")
    Responsibility getResponsibility(@WebParam(name = "id") String id);

    @WebMethod(operationName = "findRespsByNamespaceCodeAndName")
    @WebResult(name = "responsibilities")
    List<Responsibility> findRespsByNamespaceCodeAndName(@WebParam(name = "namespaceCode") String namespaceCode,
                                                         @WebParam(name = "name") String name);

    @WebMethod(operationName = "getResponsibilityTemplate")
    @WebResult(name = "template")
    Template getResponsibilityTemplate(@WebParam(name = "id") String id);

    @WebMethod(operationName = "findRespTemplatesByNamespaceCodeAndName")
    @WebResult(name = "templates")
    List<Template> findRespTemplatesByNamespaceCodeAndName(@WebParam(name = "namespaceCode") String namespaceCode,
                                                           @WebParam(name = "name") String name);

    @WebMethod(operationName = "hasResponsibility")
    @WebResult(name = "result")
    boolean hasResponsibility(@WebParam(name = "principalId") String principalId,
                              @WebParam(name = "namespaceCode") String namespaceCode,
                              @WebParam(name = "respName") String respName,
                              @WebParam(name = "qualification") Attributes qualification,
                              @WebParam(name = "responsibilityDetails") Attributes responsibilityDetails);

    @WebMethod(operationName = "hasResponsibilityByTemplateName")
    @WebResult(name = "result")
    boolean hasResponsibilityByTemplateName(@WebParam(name = "principalId") String principalId,
                                            @WebParam(name = "namespaceCode") String namespaceCode,
                                            @WebParam(name = "respTemplateName") String respTemplateName,
                                            @WebParam(name = "qualification") Attributes qualification,
                                            @WebParam(name = "responsibilityDetails") Attributes responsibilityDetails);

    @WebMethod(operationName = "getResponsibilityActions")
    @WebResult(name = "responsibilityActions")
    List<ResponsibilityAction> getResponsibilityActions(@WebParam(name = "namespaceCode") String namespaceCode,
                                                        @WebParam(name = "responsibilityName") String responsibilityName,
                                                        @WebParam(name = "qualification") Attributes qualification,
                                                        @WebParam(name = "responsibilityDetails") Attributes responsibilityDetails);

    @WebMethod(operationName = "getResponsibilityActionsByTemplateName")
    @WebResult(name = "responsibilityActions")
    List<ResponsibilityAction> getResponsibilityActionsByTemplateName(@WebParam(name = "namespaceCode") String namespaceCode,
                                                                      @WebParam(name = "responsibilityTemplateName") String respTemplateName,
                                                                      @WebParam(name = "qualification") Attributes qualification,
                                                                      @WebParam(name = "responsibilityDetails") Attributes responsibilityDetails);

    @WebMethod(operationName = "getRoleIdsForResponsibility")
    @WebResult(name = "roleIds")
    List<String> getRoleIdsForResponsibility(@WebParam(name = "id") String id,
                                             @WebParam(name = "qualification") Attributes qualification);

    /**
     * This method find Responsibilities based on a query criteria.  The criteria cannot be null.
     *
     * @param queryByCriteria the criteria.  Cannot be null.
     * @return query results.  will never return null.
     * @throws IllegalArgumentException if the queryByCriteria is null
     */
    @WebMethod(operationName = "findResponsibilities")
    @WebResult(name = "results")
    ResponsibilityQueryResults findResponsibilities(@WebParam(name = "query") QueryByCriteria queryByCriteria);


    /**
     * This method find Responsibility Templates based on a query criteria.  The criteria cannot be null.
     *
     * @param queryByCriteria the criteria.  Cannot be null.
     * @return query results.  will never return null.
     * @throws IllegalArgumentException if the queryByCriteria is null
     */
    @WebMethod(operationName = "findResponsibilityTemplates")
    @WebResult(name = "results")
    TemplateQueryResults findResponsibilityTemplates(@WebParam(name = "query") QueryByCriteria queryByCriteria);
}

/*
 * Copyright 2007-2008 The Kuali Foundation
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
package org.kuali.rice.kim.impl.role

import org.kuali.rice.kim.impl.attribute.KimAttributeDataBo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Entity
@Table(name="KRIM_ROLE_MBR_ATTR_DATA_T")
public class RoleMemberAttributeDataBo extends KimAttributeDataBo {
    @Column(name="ROLE_MBR_ID")
    protected String roleMemberId;

    public String getRoleMemberId() {
        return this.roleMemberId;
    }

    public void setRoleMemberId(String roleMemberId) {
        this.roleMemberId = roleMemberId;
    }
}

/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.kim.impl.role;

import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Customizer;
import org.joda.time.DateTime;
import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.api.type.KimType;
import org.kuali.rice.kim.api.type.KimTypeInfoService;
import org.kuali.rice.kim.framework.role.RoleEbo;
import org.kuali.rice.kim.impl.type.KimTypeBo;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krad.data.provider.jpa.eclipselink.EclipseLinkSequenceCustomizer;

import org.springframework.util.AutoPopulatingList;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

@Entity
@Customizer(EclipseLinkSequenceCustomizer.class)
@Table(name = "KRIM_ROLE_T")
public class RoleBo extends PersistableBusinessObjectBase implements RoleEbo {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ROLE_ID")
    private String id;
    @Column(name = "ROLE_NM")
    private String name;
    @Column(name = "DESC_TXT", length = 4000)
    private String description;
    @javax.persistence.Convert(converter=org.kuali.rice.krad.data.converters.BooleanYNConverter.class)
    @Column(name = "ACTV_IND")
    private boolean active;
    @Column(name = "KIM_TYP_ID")
    private String kimTypeId;
    @Column(name = "NMSPC_CD")
    private String namespaceCode;
    @OneToMany(targetEntity = RoleMemberBo.class, cascade = {CascadeType.MERGE, CascadeType.REFRESH},
            fetch = FetchType.EAGER)
    @JoinColumn(name = "ROLE_ID", insertable = false, updatable = false)
    private List<RoleMemberBo> members = new AutoPopulatingList<RoleMemberBo>(RoleMemberBo.class);
    @Transient
    private String principalName;
    @Transient
    private String groupNamespaceCode;
    @Transient
    private String groupName;
    @Transient
    private String permNamespaceCode;
    @Transient
    private String permName;
    @Transient
    private String permTmplNamespaceCode;
    @Transient
    private String permTmplName;
    @Transient
    private String respNamespaceCode;
    @Transient
    private String respName;
    @Transient
    private String respTmplNamespaceCode;
    @Transient
    private String respTmplName;
    private transient KimTypeInfoService kimTypeInfoService;

    protected List<String> getMembersOfType(String memberTypeCode) {List<String> roleMembers = new ArrayList<String>();
        for (RoleMemberBo member: getMembers()) {
            if (member.getType().equals(MemberType.valueOf(memberTypeCode))
                    && member.isActive(new DateTime())) {
                roleMembers.add(member.getMemberId());
            }
        }
        return roleMembers;
    }

    public KimTypeBo getKimRoleType() {
        if (kimTypeId == null) {
            return null;
        }

        KimType type = getTypeInfoService().getKimType(kimTypeId);
        if (type == null) {
            return null;
        }

        return KimTypeBo.from(type);
    }

    protected KimTypeInfoService getTypeInfoService() {
        if (kimTypeInfoService == null) {
            kimTypeInfoService = KimApiServiceLocator.getKimTypeInfoService();
        }

        return kimTypeInfoService;
    }

    public static Role to(RoleBo bo) {
        if (bo == null) {
            return null;
        }

        return Role.Builder.create(bo).build();
    }

    public static RoleBo from(Role immutable) {
        if (immutable == null) {
            return null;
        }

        RoleBo bo = new RoleBo();
        bo.id = immutable.getId();
        bo.name = immutable.getName();
        bo.namespaceCode = immutable.getNamespaceCode();
        bo.description = immutable.getDescription();
        bo.kimTypeId = immutable.getKimTypeId();
        bo.active = immutable.isActive();
        bo.setVersionNumber(immutable.getVersionNumber());
        bo.setObjectId(immutable.getObjectId());

        return bo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getActive() {
        return active;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getKimTypeId() {
        return kimTypeId;
    }

    public void setKimTypeId(String kimTypeId) {
        this.kimTypeId = kimTypeId;
    }

    public String getNamespaceCode() {
        return namespaceCode;
    }

    public void setNamespaceCode(String namespaceCode) {
        this.namespaceCode = namespaceCode;
    }

    public List<RoleMemberBo> getMembers() {
        return members;
    }

    public void setMembers(List<RoleMemberBo> members) {
        this.members = members;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public String getGroupNamespaceCode() {
        return groupNamespaceCode;
    }

    public void setGroupNamespaceCode(String groupNamespaceCode) {
        this.groupNamespaceCode = groupNamespaceCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getPermNamespaceCode() {
        return permNamespaceCode;
    }

    public void setPermNamespaceCode(String permNamespaceCode) {
        this.permNamespaceCode = permNamespaceCode;
    }

    public String getPermName() {
        return permName;
    }

    public void setPermName(String permName) {
        this.permName = permName;
    }

    public String getPermTmplNamespaceCode() {
        return permTmplNamespaceCode;
    }

    public void setPermTmplNamespaceCode(String permTmplNamespaceCode) {
        this.permTmplNamespaceCode = permTmplNamespaceCode;
    }

    public String getPermTmplName() {
        return permTmplName;
    }

    public void setPermTmplName(String permTmplName) {
        this.permTmplName = permTmplName;
    }

    public String getRespNamespaceCode() {
        return respNamespaceCode;
    }

    public void setRespNamespaceCode(String respNamespaceCode) {
        this.respNamespaceCode = respNamespaceCode;
    }

    public String getRespName() {
        return respName;
    }

    public void setRespName(String respName) {
        this.respName = respName;
    }

    public String getRespTmplNamespaceCode() {
        return respTmplNamespaceCode;
    }

    public void setRespTmplNamespaceCode(String respTmplNamespaceCode) {
        this.respTmplNamespaceCode = respTmplNamespaceCode;
    }

    public String getRespTmplName() {
        return respTmplName;
    }

    public void setRespTmplName(String respTmplName) {
        this.respTmplName = respTmplName;
    }


}

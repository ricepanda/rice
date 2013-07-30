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
package org.kuali.rice.kim.impl.group;

import org.kuali.rice.kim.api.common.attribute.KimAttribute;
import org.kuali.rice.kim.api.common.attribute.KimAttributeDataHistory;
import org.kuali.rice.kim.impl.common.attribute.KimAttributeBo;
import org.kuali.rice.kim.impl.common.attribute.KimAttributeDataHistoryBo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "KRIM_HIST_GRP_ATTR_DATA_T")
public class GroupAttributeHistoryBo extends KimAttributeDataHistoryBo {

    private static final long serialVersionUID = -1358263879165065051L;

    @Id
    @Column(name="HIST_ID")
    private String historyId;

    @Column(name="ATTR_DATA_ID")
    private String id;

    @Column(name = "GRP_ID")
    private String assignedToId;

    @Column(name="GRP_HIST_ID")
    private Long assignedToHistoryId;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Long getAssignedToHistoryId() {
        return assignedToHistoryId;
    }

    @Override
    public void setAssignedToHistoryId(Long assignedToHistoryId) {
        this.assignedToHistoryId = assignedToHistoryId;
    }

    @Override
    public String getAssignedToId() {
        return assignedToId;
    }

    @Override
    public void setAssignedToId(String assignedToId) {
        this.assignedToId = assignedToId;
    }

    /**
     * Converts a mutable bo to its immutable counterpart
     * @param bo the mutable business object
     * @return the immutable object
     */
    public static KimAttributeDataHistory to(GroupAttributeHistoryBo bo) {
        if (bo == null) {
            return null;
        }

        return KimAttributeDataHistory.Builder.create(bo).build();
    }

    /*public static GroupAttributeHistoryBo from(KimAttributeData im, Timestamp fromDate, Timestamp toDate) {
        if (im == null) {
            return null;
        }

        GroupAttributeHistoryBo bo = new GroupAttributeHistoryBo();
        bo.setId(im.getId());
        bo.setAssignedToId(im.getAssignedToId());
        bo.setKimAttribute(KimAttributeBo.from(im.getKimAttribute()));
        final KimAttribute attribute = im.getKimAttribute();
        bo.setKimAttributeId((attribute == null ? null : attribute.getId()));
        bo.setAttributeValue(bo.getAttributeValue());
        bo.setKimTypeId(im.getKimTypeId());
        bo.setVersionNumber(im.getVersionNumber());
        bo.setObjectId(im.getObjectId());

        return bo;
    }*/

    public static GroupAttributeHistoryBo from(KimAttributeDataHistory im) {
        if (im == null) {
            return null;
        }

        GroupAttributeHistoryBo bo = new GroupAttributeHistoryBo();
        bo.setId(im.getId());
        //bo.setHistoryId(im.getHistoryId());
        bo.setAssignedToId(im.getAssignedToId());
        bo.setAssignedToHistoryId(im.getAssignedToHistoryId());
        bo.setKimAttribute(KimAttributeBo.from(im.getKimAttribute()));
        final KimAttribute attribute = im.getKimAttribute();
        bo.setKimAttributeId((attribute == null ? null : attribute.getId()));
        bo.setAttributeValue(bo.getAttributeValue());
        bo.setKimTypeId(im.getKimTypeId());
        bo.setVersionNumber(im.getVersionNumber());
        bo.setObjectId(im.getObjectId());

        return bo;
    }

}

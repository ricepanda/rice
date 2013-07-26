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
package org.kuali.rice.kim.impl.identity.affiliation;

import org.eclipse.persistence.annotations.Convert;
import org.joda.time.DateTime;
import org.kuali.rice.core.api.mo.common.active.InactivatableFromToUtils;
import org.kuali.rice.kim.api.identity.CodedAttribute;
import org.kuali.rice.kim.api.identity.CodedAttributeHistory;
import org.kuali.rice.kim.api.identity.affiliation.EntityAffiliationType;
import org.kuali.rice.kim.api.identity.affiliation.EntityAffiliationTypeHistory;
import org.kuali.rice.kim.api.identity.affiliation.EntityAffiliationTypeHistoryContract;
import org.kuali.rice.kim.impl.identity.CodedAttributeHistoryBoContract;
import org.kuali.rice.kim.impl.identity.CodedAttributeHistoryBoUtil;

import javax.persistence.Column;
import java.sql.Timestamp;

public class EntityAffiliationTypeHistoryBo extends EntityAffiliationTypeBo implements EntityAffiliationTypeHistoryContract, CodedAttributeHistoryBoContract {
    private static final long serialVersionUID = -907313055424585268L;
    @Column(name ="HIST_ID")
    private Long historyId;
    @Column(name = "ACTV_FRM_DT")
    private Timestamp activeFromDateValue;
    @Column(name = "ACTV_TO_DT")
    private Timestamp activeToDateValue;
    @javax.persistence.Convert(converter=org.kuali.rice.krad.data.converters.BooleanYNConverter.class)
    @Column(name = "EMP_AFLTN_TYP_IND")
    private boolean employmentAffiliationType;

    @Override
    public Long getHistoryId() {
        return historyId;
    }

    @Override
    public void setHistoryId(Long historyId) {
        this.historyId = historyId;
    }

    public Timestamp getActiveFromDateValue() {
        return activeFromDateValue;
    }

    @Override
    public void setActiveFromDateValue(Timestamp activeFromDateValue) {
        this.activeFromDateValue = activeFromDateValue;
    }

    public Timestamp getActiveToDateValue() {
        return activeToDateValue;
    }

    @Override
    public void setActiveToDateValue(Timestamp activeToDateValue) {
        this.activeToDateValue = activeToDateValue;
    }

    @Override
    public boolean isEmploymentAffiliationType() {
        return employmentAffiliationType;
    }

    @Override
    public void setEmploymentAffiliationType(boolean employmentAffiliationType) {
        this.employmentAffiliationType = employmentAffiliationType;
    }

    public boolean isActive(Timestamp activeAsOfDate) {
        return InactivatableFromToUtils.isActive(getActiveFromDate(), getActiveToDate(), new DateTime(
                activeAsOfDate.getTime()));
    }

    @Override
    public boolean isActive(DateTime activeAsOfDate) {
        return this.isActive() && InactivatableFromToUtils.isActive(getActiveFromDate(), getActiveToDate(), activeAsOfDate);
    }

    @Override
    public boolean isActiveNow() {
        return this.isActive() && InactivatableFromToUtils.isActive(getActiveFromDate(), getActiveToDate(), null);
    }

    @Override
    public DateTime getActiveFromDate() {
        return this.activeFromDateValue == null ? null : new DateTime(this.activeFromDateValue.getTime());
    }

    @Override
    public DateTime getActiveToDate() {
        return this.activeToDateValue == null ? null : new DateTime(this.activeToDateValue.getTime());
    }


    public static EntityAffiliationTypeHistoryBo from(CodedAttributeHistory immutable) {
        return CodedAttributeHistoryBoUtil.from(EntityAffiliationTypeHistoryBo.class, immutable);
    }

    public static EntityAffiliationTypeHistoryBo from(CodedAttribute immutable,
            Timestamp fromDate,
            Timestamp toDate) {
        return CodedAttributeHistoryBoUtil.from(EntityAffiliationTypeHistoryBo.class, immutable, fromDate, toDate);
    }

    public static EntityAffiliationTypeHistoryBo from(EntityAffiliationTypeHistory immutable) {
        EntityAffiliationTypeHistoryBo bo = CodedAttributeHistoryBoUtil.from(EntityAffiliationTypeHistoryBo.class,
                CodedAttributeHistory.Builder.create(immutable).build());
        bo.setEmploymentAffiliationType(immutable.isEmploymentAffiliationType());
        return bo;
    }

    public static EntityAffiliationTypeHistoryBo from(EntityAffiliationType immutable,
            Timestamp fromDate,
            Timestamp toDate) {
        EntityAffiliationTypeHistoryBo bo =  CodedAttributeHistoryBoUtil.from(EntityAffiliationTypeHistoryBo.class,
                CodedAttribute.Builder.create(immutable).build(), fromDate, toDate);
        bo.setEmploymentAffiliationType(immutable.isEmploymentAffiliationType());
        return bo;
    }


}

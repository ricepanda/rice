package org.kuali.rice.kim.impl.common.history;

import org.joda.time.DateTime;
import org.kuali.rice.core.api.mo.common.active.InactivatableFromToUtils;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import javax.persistence.Column;
import java.sql.Timestamp;


public abstract class AbstractHistoryBo<T extends PersistableBusinessObjectBase> extends PersistableBusinessObjectBase {
    protected T bo;
    //private static final long serialVersionUID = -7272085876524380076L;
    @Column(name = "ACTV_FRM_DT")
    private Timestamp activeFromDateValue;
    @Column(name = "ACTV_TO_DT")
    private Timestamp activeToDateValue;

    public boolean isActive(Timestamp activeAsOfDate) {
        return InactivatableFromToUtils.isActive(getActiveFromDate(), getActiveToDate(), new DateTime(
                activeAsOfDate.getTime()));
    }

    public boolean isActive(DateTime activeAsOfDate) {
        return InactivatableFromToUtils.isActive(getActiveFromDate(), getActiveToDate(), activeAsOfDate);
    }

    public boolean isActive() {
        return InactivatableFromToUtils.isActive(getActiveFromDate(), getActiveToDate(), null);
    }

    public DateTime getActiveFromDate() {
        return this.activeFromDateValue == null ? null : new DateTime(this.activeFromDateValue.getTime());
    }

    public DateTime getActiveToDate() {
        return this.activeToDateValue == null ? null : new DateTime(this.activeToDateValue.getTime());
    }

    public Timestamp getActiveFromDateValue() {
        return activeFromDateValue;
    }

    public void setActiveFromDateValue(Timestamp activeFromDateValue) {
        this.activeFromDateValue = activeFromDateValue;
    }

    public Timestamp getActiveToDateValue() {
        return activeToDateValue;
    }

    public void setActiveToDateValue(Timestamp activeToDateValue) {
        this.activeToDateValue = activeToDateValue;
    }

    public T getBo() {
        return bo;
    }

    public void setBo(T bo) {
        this.bo = bo;
    }
}

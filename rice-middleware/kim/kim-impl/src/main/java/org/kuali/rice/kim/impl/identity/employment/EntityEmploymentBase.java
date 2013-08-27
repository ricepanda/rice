package org.kuali.rice.kim.impl.identity.employment;

import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.kim.api.identity.employment.EntityEmploymentContract;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krad.data.jpa.converters.BooleanYNConverter;
import org.kuali.rice.krad.data.jpa.converters.EncryptionConverter;
import org.kuali.rice.krad.data.jpa.converters.KualiDecimalConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class EntityEmploymentBase extends PersistableBusinessObjectBase implements EntityEmploymentContract {
    private static final long serialVersionUID = 1L;
    @Column(name = "ENTITY_ID")
    private String entityId;
    @Column(name = "EMP_ID")
    private String employeeId;
    @Column(name = "EMP_REC_ID")
    private String employmentRecordId;
    @Column(name = "ENTITY_AFLTN_ID")
    private String entityAffiliationId;
    @Column(name = "EMP_STAT_CD")
    private String employeeStatusCode;
    @Column(name = "EMP_TYP_CD")
    private String employeeTypeCode;
    @Column(name = "PRMRY_DEPT_CD")
    private String primaryDepartmentCode;
    @Convert(converter = KualiDecimalConverter.class)
    @Column(name = "BASE_SLRY_AMT")
    private KualiDecimal baseSalaryAmount;
    @javax.persistence.Convert(converter=BooleanYNConverter.class)
    @Column(name = "PRMRY_IND")
    private boolean primary;
    @javax.persistence.Convert(converter=BooleanYNConverter.class)
    @Column(name = "ACTV_IND")
    private boolean active;
    @javax.persistence.Convert(converter=BooleanYNConverter.class)
    @Column(name = "TNR_IND")
    private boolean tenured;

    @Override
    public boolean isTenured() {
        return tenured;
    }

    public void setTenured(boolean tenured) {
        this.tenured = tenured;
    }

    @Override
    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    @Override
    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public String getEmploymentRecordId() {
        return employmentRecordId;
    }

    public void setEmploymentRecordId(String employmentRecordId) {
        this.employmentRecordId = employmentRecordId;
    }

    public String getEntityAffiliationId() {
        return entityAffiliationId;
    }

    public void setEntityAffiliationId(String entityAffiliationId) {
        this.entityAffiliationId = entityAffiliationId;
    }

    public String getEmployeeStatusCode() {
        return employeeStatusCode;
    }

    public void setEmployeeStatusCode(String employeeStatusCode) {
        this.employeeStatusCode = employeeStatusCode;
    }

    public String getEmployeeTypeCode() {
        return employeeTypeCode;
    }

    public void setEmployeeTypeCode(String employeeTypeCode) {
        this.employeeTypeCode = employeeTypeCode;
    }

    @Override
    public String getPrimaryDepartmentCode() {
        return primaryDepartmentCode;
    }

    public void setPrimaryDepartmentCode(String primaryDepartmentCode) {
        this.primaryDepartmentCode = primaryDepartmentCode;
    }

    @Override
    public KualiDecimal getBaseSalaryAmount() {
        return baseSalaryAmount;
    }

    public void setBaseSalaryAmount(KualiDecimal baseSalaryAmount) {
        this.baseSalaryAmount = baseSalaryAmount;
    }

    public boolean getPrimary() {
        return primary;
    }

    @Override
    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public boolean getActive() {
        return active;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}

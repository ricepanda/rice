package org.kuali.rice.kim.impl.identity.external;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.identity.external.EntityExternalIdentifier;
import org.kuali.rice.kim.api.identity.external.EntityExternalIdentifierContract;
import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krad.data.jpa.eclipselink.PortableSequenceGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "KRIM_ENTITY_EXT_ID_T")
public class EntityExternalIdentifierBase extends PersistableBusinessObjectBase implements EntityExternalIdentifierContract {
    private static final Logger LOG = Logger.getLogger(EntityExternalIdentifierBase.class);
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "KRIM_ENTITY_EXT_ID_ID_S")
    @PortableSequenceGenerator(name = "KRIM_ENTITY_EXT_ID_ID_S")
    @Column(name = "ENTITY_EXT_ID_ID")
    private String id;
    @Column(name = "ENTITY_ID")
    private String entityId;
    @Column(name = "EXT_ID_TYP_CD")
    private String externalIdentifierTypeCode;
    @Column(name = "EXT_ID")
    private String externalId;
    @ManyToOne(targetEntity = EntityExternalIdentifierTypeBo.class, fetch = FetchType.EAGER, cascade = {})
    @JoinColumn(
            name = "EXT_ID_TYP_CD", insertable = false, updatable = false)
    private EntityExternalIdentifierTypeBo externalIdentifierType;
    @Transient
    private EntityExternalIdentifierTypeBo cachedExtIdType = null;
    @Transient
    private boolean encryptionRequired = false;
    @Transient
    private boolean decryptionNeeded = false;

    public static EntityExternalIdentifier to(EntityExternalIdentifierBase bo) {
        if (bo == null) {
            return null;
        }

        return EntityExternalIdentifier.Builder.create(bo).build();
    }

    /**
     * Creates a EntityExternalIdentifierBo business object from an immutable representation of a
     * EntityExternalIdentifier.
     *
     * @param immutable immutable EntityExternalIdentifier
     * @return a EntityExternalIdentifierBo
     */
    public static EntityExternalIdentifierBase from(EntityExternalIdentifier immutable) {
        if (immutable == null) {
            return null;
        }

        EntityExternalIdentifierBase bo = new EntityExternalIdentifierBase();
        bo.id = immutable.getId();
        bo.externalId = immutable.getExternalId();
        bo.entityId = immutable.getEntityId();
        bo.externalIdentifierTypeCode = immutable.getExternalIdentifierTypeCode();
        bo.externalIdentifierType = immutable.getExternalIdentifierType() != null
                ? EntityExternalIdentifierTypeBo.from(immutable.getExternalIdentifierType()) : null;
        bo.setVersionNumber(immutable.getVersionNumber());
        bo.setObjectId(immutable.getObjectId());

        return bo;
    }

    @Override
    protected void prePersist() {
        super.prePersist();
        encryptExternalId();
    }

    @Override
    protected void postLoad() {
        super.postLoad();
        decryptExternalId();
    }

    @Override
    protected void preUpdate() {
        super.preUpdate();
        if (!this.decryptionNeeded) {
            encryptExternalId();
        }

    }

    protected void encryptExternalId() {
        evaluateExternalIdentifierType();
        if (encryptionRequired && StringUtils.isNotEmpty(this.externalId)) {
            try {
                if (CoreApiServiceLocator.getEncryptionService().isEnabled()) {
                    this.externalId = CoreApiServiceLocator.getEncryptionService().encrypt(this.externalId);
                    this.decryptionNeeded = true;
                }

            } catch (Exception e) {
                LOG.info("Unable to encrypt value : " + e.getMessage() + " or it is already encrypted");
            }

        }

    }

    protected void decryptExternalId() {
        evaluateExternalIdentifierType();
        if (encryptionRequired && StringUtils.isNotEmpty(externalId)) {
            try {
                if (CoreApiServiceLocator.getEncryptionService().isEnabled()) {
                    this.externalId = CoreApiServiceLocator.getEncryptionService().decrypt(this.externalId);
                }

            } catch (Exception e) {
                LOG.info("Unable to decrypt value : " + e.getMessage() + " or it is already decrypted");
            }

        }

    }

    protected void evaluateExternalIdentifierType() {
        if (cachedExtIdType == null) {
            Map<String, String> criteria = new HashMap<String, String>();
            ((HashMap<String, String>) criteria).put(KimConstants.PrimaryKeyConstants.CODE, externalIdentifierTypeCode);
            cachedExtIdType =
                    (EntityExternalIdentifierTypeBo) KNSServiceLocator.getBusinessObjectService().findByPrimaryKey(
                            EntityExternalIdentifierTypeBo.class, criteria);
            encryptionRequired = cachedExtIdType != null && cachedExtIdType.isEncryptionRequired();
        }

    }

    protected String decryptedExternalId() {
        evaluateExternalIdentifierType();
        if (encryptionRequired && StringUtils.isNotEmpty(externalId)) {
            try {
                if (CoreApiServiceLocator.getEncryptionService().isEnabled()) {
                    return CoreApiServiceLocator.getEncryptionService().decrypt(this.externalId);
                }

            } catch (Exception e) {
                LOG.info("Unable to decrypt value : " + e.getMessage() + " or it is already decrypted");
            }

        }

        return "";
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
        this.decryptionNeeded = false;
    }

    public void setExternalIdentifierTypeCode(String externalIdentifierTypeCode) {
        this.externalIdentifierTypeCode = externalIdentifierTypeCode;
        cachedExtIdType = null;
    }

    public void setExternalIdentifierType(EntityExternalIdentifierTypeBo externalIdentifierType) {
        this.externalIdentifierType = externalIdentifierType;
        cachedExtIdType = null;
    }

    @Override
    public EntityExternalIdentifierTypeBo getExternalIdentifierType() {
        return this.externalIdentifierType;
    }

    @Override
    public String getExternalId() {
        if (this.decryptionNeeded) {
            return decryptedExternalId();
        }

        return externalId;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    @Override
    public String getExternalIdentifierTypeCode() {
        return externalIdentifierTypeCode;
    }

}

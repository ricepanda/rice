package org.kuali.rice.kim.api.identity.principal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.core.api.mo.ModelObjectComplete;
import org.kuali.rice.kim.api.identity.name.EntityName;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.Collection;

@XmlRootElement(name = EntityNamePrincipalName.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = EntityNamePrincipalName.Constants.TYPE_NAME, propOrder = {
    EntityNamePrincipalName.Elements.DEFAULT_NAME,
    EntityNamePrincipalName.Elements.PRINCIPAL_NAME,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public class EntityNamePrincipalName implements ModelObjectComplete {
    @XmlElement(name = Elements.PRINCIPAL_NAME, required = false)
    private final String principalName;
    @XmlElement(name = Elements.DEFAULT_NAME, required = false)
    private final EntityName defaultName;
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;


    private EntityNamePrincipalName() {
        this.principalName = null;
        this.defaultName = null;
    }

    private EntityNamePrincipalName(Builder builder) {
        this.principalName = builder.getPrincipalName();
        this.defaultName = builder.getDefaultName() == null ? null : builder.getDefaultName().build();
    }

    public String getPrincipalName() {
        return principalName;
    }

    public EntityName getDefaultName() {
        return defaultName;
    }
    /**
     * A builder which can be used to construct {@link EntityDefault} instances.
     *
     */
    public final static class Builder
        implements Serializable, ModelBuilder
    {
        private String principalName;
        private EntityName.Builder defaultName;

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(String principalName, EntityName.Builder defaultName) {
            Builder builder = new Builder();
            builder.setPrincipalName(principalName);
            builder.setDefaultName(defaultName);
            return builder;
        }

        public static Builder create(EntityNamePrincipalName immutable) {
            if (immutable == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = new Builder();
            if (immutable.getDefaultName() != null) {
                builder.setDefaultName(EntityName.Builder.create(immutable.getDefaultName()));
            }
            return builder;
        }

        public String getPrincipalName() {
            return principalName;
        }

        public void setPrincipalName(String principalName) {
            this.principalName = principalName;
        }

        public EntityName.Builder getDefaultName() {
            return defaultName;
        }

        public void setDefaultName(EntityName.Builder defaultName) {
            this.defaultName = defaultName;
        }

        public EntityNamePrincipalName build() {
            return new EntityNamePrincipalName(this);
        }

    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, Constants.HASH_CODE_EQUALS_EXCLUDE);
    }

    @Override
    public boolean equals(Object object) {
        return EqualsBuilder.reflectionEquals(object, this, Constants.HASH_CODE_EQUALS_EXCLUDE);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * Defines some internal constants used on this class.
     *
     */
    static class Constants {

        final static String ROOT_ELEMENT_NAME = "entityNamePrincipalName";
        final static String TYPE_NAME = "EntityNamePrincipalNameType";
        final static String[] HASH_CODE_EQUALS_EXCLUDE = new String[] {CoreConstants.CommonElements.FUTURE_ELEMENTS };

    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     *
     */
    static class Elements {
        final static String DEFAULT_NAME = "defaultName";
        final static String PRINCIPAL_NAME = "principalName";
    }

}

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

package org.kuali.rice.core.api.namespace;

import java.io.Serializable;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.core.api.mo.ModelObjectComplete;
import org.w3c.dom.Element;

/**
 * An immutable representation of a {@link NamespaceContract}.
 *
 * <p>To construct an instance of a Namespace, use the {@link Namespace.Builder} class.
 * 
 * @see NamespaceContract
 */
@XmlRootElement(name = Namespace.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = Namespace.Constants.TYPE_NAME, propOrder = {
    Namespace.Elements.CODE,
    Namespace.Elements.APPLICATION_ID,
    Namespace.Elements.NAME,
    Namespace.Elements.ACTIVE,
    CoreConstants.CommonElements.VERSION_NUMBER,
    CoreConstants.CommonElements.OBJECT_ID,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
    })
public final class Namespace implements NamespaceContract, ModelObjectComplete {
	
	private static final long serialVersionUID = -5206398776503106883L;

	@XmlElement(name = Elements.CODE, required=true)
    private final String code;

    @XmlElement(name = Elements.APPLICATION_ID, required=true)
    private final String applicationId;

    @XmlElement(name = Elements.NAME, required=false)
    private final String name;

    @XmlElement(name = Elements.ACTIVE, required=false)
    private final boolean active;

    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;

    @XmlElement(name = CoreConstants.CommonElements.OBJECT_ID, required = false)
	private final String objectId;
    
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /** 
     * This constructor should never be called.  It is only present for use during JAXB unmarshalling. 
     */
    private Namespace() {
    	this.code = null;
    	this.applicationId = null;
    	this.name = null;
    	this.active = true;
        this.versionNumber = null;
        this.objectId = null;
    }

	/**
	* Constructs a Namespace from the given builder.  This constructor is private and should only
	* ever be invoked from the builder.
	*
	* @param builder the Builder from which to construct the namespace
	*/
    private Namespace(Builder builder) {
        code = builder.getCode();
        applicationId = builder.getApplicationId();
        name = builder.getName();
        active = builder.isActive();
        versionNumber = builder.getVersionNumber();
        objectId = builder.getObjectId();
    }

    @Override
    public String getCode() {
		return code;
	}

    @Override
	public String getApplicationId() {
		return applicationId;
	}

    @Override
	public String getName() {
		return name;
	}

    @Override
	public boolean isActive() {
		return active;
	}

    @Override
    public Long getVersionNumber() {
        return versionNumber;
    }
    
    @Override
	public String getObjectId() {
		return objectId;
	}

	/**
     * This builder is used to construct instances of Namespace.  It enforces the constraints of the {@link NamespaceContract}.
     */
	public static final class Builder implements NamespaceContract, ModelBuilder, Serializable {
       
		private static final long serialVersionUID = -70194982373806749L;

		private String code;
		private String applicationId;
		private String name;
		private boolean active;
        private Long versionNumber;
        private String objectId;

		/**
		 * Constructs a Namespace Builder with the given namespace code and application id.  Defaults the active indicator to true.
		 *
		 * @param code the namespace code to use when constructing this builder
		 * @param applicationId the application id to use when constructing this builder
		 * @throws IllegalArgumentException if the code or applicationId are null or blank
		 */
        private Builder(String code, String applicationId) {
            setCode(code);
            setApplicationId(applicationId);
			setActive(true);
        }

        /**
         * Creates a builder from the given namespace code and application id.
         * 
		 * @param code the namespace code to use when constructing this builder
		 * @param applicationId the application id to use when constructing this builder
		 * @return an instance of the builder with the given data already populated
		 * @throws IllegalArgumentException if the code or applicationId are null or blank
         */
        public static Builder create(String code, String applicationId) {
            return new Builder(code, applicationId);
        }

		/**
		 * Creates a builder by populating it with data from the given {@link NamespaceContract}.
         * 
         * @param contract the contract from which to populate this builder
         * @return an instance of the builder populated with data from the contract
         */
        public static Builder create(NamespaceContract contract) {
            Builder builder  = new Builder(contract.getCode(), contract.getApplicationId());
            builder.setName(contract.getName());
            builder.setActive(contract.isActive());
            builder.setVersionNumber(contract.getVersionNumber());
            builder.setObjectId(contract.getObjectId());
            return builder;
        }

		/**
		 * Sets the value of the code on this builder to the given value.
		 *
		 * @param code the code value to set, must not be null or blank
		 * @throws IllegalArgumentException if the code is null or blank
		 */
        public void setCode(String code) {
            if (StringUtils.isBlank(code)) {
                throw new IllegalArgumentException("code is blank");
            }
            this.code = code;
        }

		/**
		 * Sets the value of the applicationId on this builder to the given value.
		 *
		 * @param applicationId the application id value to set, must not be null or blank
		 * @throws IllegalArgumentException if the application id is null or blank
		 */
        public void setApplicationId(String applicationId) {
            if (StringUtils.isBlank(applicationId)) {
                throw new IllegalArgumentException("applicationId is blank");
            }
            this.applicationId = applicationId;
        }
        
		public void setVersionNumber(Long versionNumber) {
			this.versionNumber = versionNumber;
		}
		
		public void setObjectId(String objectId) {
        	this.objectId = objectId;
        }

        @Override
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

        @Override
		public boolean isActive() {
			return active;
		}

		public void setActive(boolean active) {
			this.active = active;
		}

        @Override
		public String getCode() {
			return code;
		}

        @Override
		public String getApplicationId() {
			return applicationId;
		}

        @Override
		public Long getVersionNumber() {
			return versionNumber;
		}
		
        @Override
        public String getObjectId() {
        	return objectId;
        }

		/**
		 * Builds an instance of a Namespace based on the current state of the builder.
		 *
		 * @return the fully-constructed Namespace
		 */
        @Override
        public Namespace build() {
            return new Namespace(this);
        }

    }

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, Constants.HASH_CODE_EQUALS_EXCLUDE);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(obj, this, Constants.HASH_CODE_EQUALS_EXCLUDE);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	/**
	 * Defines some internal constants used on this class.
	 */
	static class Constants {
		final static String ROOT_ELEMENT_NAME = "namespace";
		final static String TYPE_NAME = "NamespaceType";
		final static String[] HASH_CODE_EQUALS_EXCLUDE = { CoreConstants.CommonElements.FUTURE_ELEMENTS};
	}
   
   /**
	* A private class which exposes constants which define the XML element names to use
	* when this object is marshalled to XML.
	*/
   static class Elements {
	   final static String CODE = "code";
	   final static String APPLICATION_ID = "applicationId";
	   final static String NAME = "name";
	   final static String ACTIVE = "active";
   }
	
}

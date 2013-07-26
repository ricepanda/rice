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
package org.kuali.rice.krad.test.document.bo;

import org.eclipse.persistence.annotations.Customizer;
import org.kuali.rice.krad.bo.VersionedAndGloballyUniqueBase;
import org.kuali.rice.krad.data.provider.jpa.eclipselink.EclipseLinkSequenceCustomizer;
import org.kuali.rice.krad.data.platform.generator.Sequence;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Duplicate of {@link Account} which overrides {@link #getExtension()} to avoid
 * automatic extension creation
 */
@Entity
@Table(name="TRV_ACCT")
@Sequence(name="TRVL_ID_SEQ", property="whatever")
@Customizer(EclipseLinkSequenceCustomizer.class)
public class SimpleAccount extends VersionedAndGloballyUniqueBase {
    @Id
    @Column(name="ACCT_NUM")
    //@GeneratedValue(generator="trvl_id_seq")
    private String number;
    @Column(name="ACCT_NAME")
    private String name;
    @Column(name="ACCT_FO_ID")
    private Long amId;

    @Transient
    private Object extension;

    @ManyToOne(fetch=FetchType.LAZY, cascade={CascadeType.REFRESH})
    @JoinColumn(name="ACCT_FO_ID",insertable=false,updatable=false)
    private AccountManager accountManager;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Long getAmId() {
        return this.amId;
    }

    public void setAmId(Long id) {
        System.err.println("Setting AmId from " + this.amId + " to " + id);
        this.amId = id;
    }

    public AccountManager getAccountManager() {
        return this.accountManager;
    }

    public void setAccountManager(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    public Object getExtension() {
        return extension;
    }

    public void setExtension(Object extension) {
        this.extension = extension;
    }
}

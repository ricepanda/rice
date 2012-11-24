/**
 * Copyright 2005-2012 The Kuali Foundation
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
package edu.sampleu.travel.approval;

import org.kuali.rice.krad.document.TransactionalDocumentBase;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Sample Travel Transactional Document
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Entity
@Table(name = "TRVL_AUTH_DOC_T")
public class TravelAuthorizationDocument extends TransactionalDocumentBase {

    private String tripTypeCode;
    private Timestamp tripBegin;
    private Timestamp tripEnd;
    private String tripDescription;
    private Boolean primaryDestinationIndicator = false;

    private Integer primaryDestinationId;
    private String primaryDestinationName;
    private String primaryDestinationCountryState;
    private String primaryDestinationCounty;

    public TravelAuthorizationDocument() {
        super();
    }

    public String getTripTypeCode() {
        return tripTypeCode;
    }

    public void setTripTypeCode(String tripTypeCode) {
        this.tripTypeCode = tripTypeCode;
    }

    public Timestamp getTripBegin() {
        return tripBegin;
    }

    public void setTripBegin(Timestamp tripBegin) {
        this.tripBegin = tripBegin;
    }

    public Timestamp getTripEnd() {
        return tripEnd;
    }

    public void setTripEnd(Timestamp tripEnd) {
        this.tripEnd = tripEnd;
    }

    public String getTripDescription() {
        return tripDescription;
    }

    public void setTripDescription(String tripDescription) {
        this.tripDescription = tripDescription;
    }

    public Integer getPrimaryDestinationId() {
        return primaryDestinationId;
    }

    public void setPrimaryDestinationId(Integer primaryDestinationId) {
        this.primaryDestinationId = primaryDestinationId;
    }

    public Boolean getPrimaryDestinationIndicator() {
        return primaryDestinationIndicator;
    }

    public void setPrimaryDestinationIndicator(Boolean primaryDestinationIndicator) {
        this.primaryDestinationIndicator = primaryDestinationIndicator;
    }

    public String getPrimaryDestinationName() {
        return primaryDestinationName;
    }

    public void setPrimaryDestinationName(String primaryDestinationName) {
        this.primaryDestinationName = primaryDestinationName;
    }

    public String getPrimaryDestinationCountryState() {
        return primaryDestinationCountryState;
    }

    public void setPrimaryDestinationCountryState(String primaryDestinationCountryState) {
        this.primaryDestinationCountryState = primaryDestinationCountryState;
    }

    public String getPrimaryDestinationCounty() {
        return primaryDestinationCounty;
    }

    public void setPrimaryDestinationCounty(String primaryDestinationCounty) {
        this.primaryDestinationCounty = primaryDestinationCounty;
    }
}

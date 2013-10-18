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
package edu.sampleu.travel.dataobject;

import org.kuali.rice.krad.bo.DataObjectBase;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * This class provides the mileage rate
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */

public class TravelMileageRate extends DataObjectBase implements Serializable {

    private static final long serialVersionUID = 4525338013753227579L;

    private String mileageRateId;

    private String mileageRateCd;

    private String mileageRateName;

    private BigDecimal mileageRate;

    boolean active = Boolean.TRUE;

    public String getMileageRateId() {
        return mileageRateId;
    }

    public void setMileageRateId(String mileageRateId) {
        this.mileageRateId = mileageRateId;
    }

    public String getMileageRateCd() {
        return mileageRateCd;
    }

    public void setMileageRateCd(String mileageRateCd) {
        this.mileageRateCd = mileageRateCd;
    }

    public String getMileageRateName() {
        return mileageRateName;
    }

    public void setMileageRateName(String mileageRateName) {
        this.mileageRateName = mileageRateName;
    }

    public BigDecimal getMileageRate() {
        return mileageRate;
    }

    public void setMileageRate(BigDecimal mileageRate) {
        this.mileageRate = mileageRate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

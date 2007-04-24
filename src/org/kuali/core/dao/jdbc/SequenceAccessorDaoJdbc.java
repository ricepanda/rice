/*
 * Copyright 2007 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.core.dao.jdbc;

import org.kuali.core.dao.SequenceAccessorDao;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class SequenceAccessorDaoJdbc extends JdbcDaoSupport implements SequenceAccessorDao {

    /**
     * @see org.kuali.core.dao.SequenceAccessorDao#getNextAvailableSequenceNumber(java.lang.String)
     */
    public Long getNextAvailableSequenceNumber(String sequenceName) {
        String sql = "Select " + sequenceName + ".nextVal from dual";
        return getJdbcTemplate().queryForLong(sql);
    }
}

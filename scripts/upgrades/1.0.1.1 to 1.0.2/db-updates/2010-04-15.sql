--
-- Copyright 2005-2011 The Kuali Foundation
--
-- Licensed under the Educational Community License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
-- http://www.opensource.org/licenses/ecl2.php
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

-- KULRICE-4079: setup Configuration View Screen permission, assign to KR-SYS Technical Administrators

INSERT INTO krim_perm_t(PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND)
  VALUES('840','97469975-D110-9A65-5EE5-F21FD1BEB5B2',	'1',	'29',	'KR-BUS',	'Use Screen',	'Allows users to access the Configuration Viewer screen',	'Y')  
/

INSERT INTO krim_perm_attr_data_t(ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL)
  VALUES('880',	'ECCB8A6C-A0DA-5311-6A57-40F743EA334C',	'1',	'840',	'12',	'2','org.kuali.rice.ksb.messaging.web.ConfigViewerAction')
/

INSERT INTO krim_role_perm_t(ROLE_PERM_ID, OBJ_ID, VER_NBR, ROLE_ID, PERM_ID, ACTV_IND)
  VALUES('855',	'E83AB210-EB48-3BDE-2D6F-F6177869AE27',	'1',	'63',	'840',	'Y')  
/

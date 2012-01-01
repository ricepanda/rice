--
-- Copyright 2005-2012 The Kuali Foundation
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

drop table KRSB_SVC_DEF_T
/
drop table KRSB_FLT_SVC_DEF_T
/
drop table KRSB_SVC_DEF_S
/
drop table KRSB_FLT_SVC_DEF_S
/
CREATE TABLE KRSB_SVC_DSCRPTR_T (
  SVC_DSCRPTR_ID varchar(40) NOT NULL,
  DSCRPTR longtext NOT NULL,
  PRIMARY KEY (SVC_DSCRPTR_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin
/
CREATE TABLE KRSB_SVC_DSCRPTR_S (
  ID bigint(19) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (ID)
) ENGINE=MyISAM AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COLLATE=utf8_bin
/
CREATE TABLE KRSB_SVC_DEF_T (
  SVC_DEF_ID varchar(40) NOT NULL,
  SVC_NM varchar(255) NOT NULL,
  SVC_URL varchar(500) NOT NULL,
  INSTN_ID varchar(255) NOT NULL,
  APPL_NMSPC varchar(255) NOT NULL,
  SRVR_IP varchar(40) NOT NULL,
  TYP_CD varchar(40) NOT NULL,
  SVC_VER varchar(40) NOT NULL,
  STAT_CD varchar(1) NOT NULL,
  SVC_DSCRPTR_ID varchar(40) NOT NULL,
  CHKSM varchar(30) NOT NULL,
  VER_NBR decimal(8,0) DEFAULT '0',
  PRIMARY KEY (SVC_DEF_ID),
  INDEX KRSB_SVC_DEF_TI1 (INSTN_ID),
  INDEX KRSB_SVC_DEF_TI2 (SVC_NM, STAT_CD),
  INDEX KRSB_SVC_DEF_TI3 (STAT_CD),
  FOREIGN KEY KRSB_SVC_DEF_FK1 (SVC_DSCRPTR_ID) REFERENCES KRSB_SVC_DSCRPTR_T(SVC_DSCRPTR_ID) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin
/
CREATE TABLE KRSB_SVC_DEF_S (
  ID bigint(19) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (ID)
) ENGINE=MyISAM AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COLLATE=utf8_bin
/
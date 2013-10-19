--
-- Copyright 2005-2013 The Kuali Foundation
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

TRUNCATE TABLE KRIM_PERM_TMPL_T DROP STORAGE
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','1','Default','KUALI','5ADF18B6D4857954E0404F8189D85002','1',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','3','Initiate Document','KR-SYS','5ADF18B6D4BF7954E0404F8189D85002','10',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','8','Cancel Document','KR-WKFLW','5ADF18B6D4CA7954E0404F8189D85002','14',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','8','Save Document','KR-WKFLW','5ADF18B6D4CB7954E0404F8189D85002','15',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','8','Edit Document','KR-NS','5ADF18B6D4CC7954E0404F8189D85002','16',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','3','Copy Document','KR-NS','5ADF18B6D4AF7954E0404F8189D85002','2',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','10','Look Up Records','KR-NS','5ADF18B6D4DA7954E0404F8189D85002','23',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','10','Inquire Into Records','KR-NS','5ADF18B6D4DB7954E0404F8189D85002','24',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','11','View Inquiry or Maintenance Document Field','KR-NS','5ADF18B6D4DF7954E0404F8189D85002','25',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','11','Modify Maintenance Document Field','KR-NS','5ADF18B6D4E07954E0404F8189D85002','26',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','11','Full Unmask Field','KR-NS','5ADF18B6D4E17954E0404F8189D85002','27',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','11','Partial Unmask Field','KR-NS','5ADF18B6D4E27954E0404F8189D85002','28',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','12','Use Screen','KR-NS','5ADF18B6D4E67954E0404F8189D85002','29',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','3','Administer Routing for Document','KR-WKFLW','5ADF18B6D4B07954E0404F8189D85002','3',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','13','Perform Custom Maintenance Document Function','KR-NS','5ADF18B6D4E97954E0404F8189D85002','30',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','14','Use Transactional Document','KR-NS','5ADF18B6D4EC7954E0404F8189D85002','31',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','15','Modify Batch Job','KR-NS','5ADF18B6D4F07954E0404F8189D85002','32',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','15','Upload Batch Input File(s)','KR-NS','5ADF18B6D4F17954E0404F8189D85002','33',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','16','Maintain System Parameter','KR-NS','5ADF18B6D4F67954E0404F8189D85002','34',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','18','Assign Role','KR-IDM','5ADF18B6D4FC7954E0404F8189D85002','35',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','19','Grant Permission','KR-IDM','5ADF18B6D5007954E0404F8189D85002','36',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','20','Grant Responsibility','KR-IDM','5ADF18B6D5047954E0404F8189D85002','37',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','21','Populate Group','KR-IDM','5ADF18B6D5087954E0404F8189D85002','38',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','3','Blanket Approve Document','KR-WKFLW','5ADF18B6D4B17954E0404F8189D85002','4',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','3','Open Document','KR-NS','5ADF18B6D4AE7954E0404F8189D85002','40',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','56','Create / Maintain Record(s)','KR-NS','603B735FA6C4FE21E0404F8189D8083B','42',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','57','View Inquiry or Maintenance Document Section','KR-NS','603B735FA6C0FE21E0404F8189D8083B','43',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','57','Modify Maintenance Document Section','KR-NS','603B735FA6C1FE21E0404F8189D8083B','44',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','9','Add Note / Attachment','KR-NS','606763510FC096D3E0404F8189D857A2','45',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','9','View Note / Attachment','KR-NS','606763510FC196D3E0404F8189D857A2','46',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','59','Delete Note / Attachment','KR-NS','606763510FC296D3E0404F8189D857A2','47',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','5','Send Ad Hoc Request','KR-NS','662384B381B867A1E0404F8189D868A6','49',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','3','Route Document','KR-WKFLW','5ADF18B6D4B27954E0404F8189D85002','5',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','3','Add Message to Route Log','KR-WKFLW','430ad531-89e4-11df-98b1-1300c9ee50c1','51',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,DESC_TXT,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','View/Maintain Agenda','67','KRMS Agenda Permission','KR-RULE','B7DBFABEFD2A8CBFE0402E0AA9D757C9','52',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','68','Open View','KR-KRAD','B7DBFABEFD578CBFE0402E0AA9D757C9','53',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','68','Edit View','KR-KRAD','B7DBFABEFD588CBFE0402E0AA9D757C9','54',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','69','Use View','KR-KRAD','B7DBFABEFD598CBFE0402E0AA9D757C9','55',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','70','View Field','KR-KRAD','B7DBFABEFD5A8CBFE0402E0AA9D757C9','56',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','70','Edit Field','KR-KRAD','B7DBFABEFD5B8CBFE0402E0AA9D757C9','57',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','71','View Group','KR-KRAD','B7DBFABEFD5C8CBFE0402E0AA9D757C9','58',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','71','Edit Group','KR-KRAD','B7DBFABEFD5D8CBFE0402E0AA9D757C9','59',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','72','View Widget','KR-KRAD','B7DBFABEFD5E8CBFE0402E0AA9D757C9','60',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','72','Edit Widget','KR-KRAD','B7DBFABEFD5F8CBFE0402E0AA9D757C9','61',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','73','Perform Action','KR-KRAD','B7DBFABEFD608CBFE0402E0AA9D757C9','62',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','71','View Line','KR-KRAD','B7DBFABEFD618CBFE0402E0AA9D757C9','63',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','71','Edit Line','KR-KRAD','B7DBFABEFD628CBFE0402E0AA9D757C9','64',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','74','View Line Field','KR-KRAD','B7DBFABEFD638CBFE0402E0AA9D757C9','65',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','74','Edit Line Field','KR-KRAD','B7DBFABEFD648CBFE0402E0AA9D757C9','66',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','75','Perform Line Action','KR-KRAD','B7DBFABEFD658CBFE0402E0AA9D757C9','67',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','8','Recall Document','KR-WKFLW','BC5444677C24328CE0402E0AA9D77D80','68',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','4','Take Requested Action','KR-NS','5ADF18B6D4B77954E0404F8189D85002','8',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','5','Ad Hoc Review Document','KR-WKFLW','5ADF18B6D4BB7954E0404F8189D85002','9',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','KR1000','Super User Approve Single Action Request','KR-WKFLW','CDC48BA7E67A87DFE040F90A05B92A31','KR1000',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','KR1000','Super User Approve Document','KR-WKFLW','CDC48BA7E67B87DFE040F90A05B92A31','KR1001',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','KR1000','Super User Disapprove Document','KR-WKFLW','CDC48BA7E67C87DFE040F90A05B92A31','KR1002',1)
/
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,DESC_TXT,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','Backdoor Restriction','KR1001','Backdoor Restriction','KR-SYS','CEA27F2DB2DC3593E040F90A05B924DB','KR1003',1)
/

TRUNCATE TABLE KRMS_CNTXT_T DROP STORAGE
/
INSERT INTO KRMS_CNTXT_T (ACTV,CNTXT_ID,DESC_TXT,NM,NMSPC_CD,TYP_ID,VER_NBR)
  VALUES ('Y','CONTEXT1','null','Context1','KR-RULE-TEST','T1003',1)
/
INSERT INTO KRMS_CNTXT_T (ACTV,CNTXT_ID,DESC_TXT,NM,NMSPC_CD,TYP_ID,VER_NBR)
  VALUES ('Y','CONTEXT_NO_PERMISSION','null','Context with no premissions','KRMS_TEST_VOID','T1003',1)
/
INSERT INTO KRMS_CNTXT_T (ACTV,CNTXT_ID,DESC_TXT,NM,NMSPC_CD,TYP_ID,VER_NBR)
  VALUES ('Y','trav-acct-test-ctxt','null','Travel Account','KR-SAP','T4',1)
/

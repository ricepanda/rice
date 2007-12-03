DROP TABLE EN_DOC_RTE_QUE_T;
DROP INDEX EN_DOC_RTE_QUE_TI1;
DROP INDEX EN_DOC_RTE_QUE_TI2;

ALTER TABLE EN_DOC_TYP_T ADD MESSAGE_ENTITY_NM VARCHAR2(10);
ALTER TABLE EN_RULE_ATTRIB_T ADD MESSAGE_ENTITY_NM VARCHAR(10) NULL;
ALTER TABLE EN_RTE_NODE_T ADD CONTENT_FRAGMENT VARCHAR2(4000);

CREATE SEQUENCE SERVICE_DEF_SEQ INCREMENT BY 1 START WITH 2000;
CREATE SEQUENCE EN_SERVICE_DEF_INTERFACE_SEQ INCREMENT BY 1 START WITH 2000;
CREATE SEQUENCE BAM_SEQ INCREMENT BY 1 START WITH 2000;
CREATE SEQUENCE BAM_PARAM_SEQ INCREMENT BY 1 START WITH 2000;

CREATE TABLE EN_SERVICE_DEF_T (
	SERVICE_DEF_ID			   	   NUMBER(14) NOT NULL,
	SERVICE_NM				   	   VARCHAR2(255) NOT NULL,
    SERVICE_URL	            	   VARCHAR2(500) NOT NULL,
    EXCEPTION_HANDLER			   VARCHAR2(500) NULL,
	ASYNC_QUEUE_IND				   NUMBER(1) NULL,
	PRIORITY					   NUMBER(8) NULL,
	RTRY_CNT					   NUMBER(8) NULL,
	MILLIS_TO_LIVE				   NUMBER(14) NULL,
	MESSAGE_ENTITY_NM 		  	   VARCHAR2(10) NOT NULL,
	SERVER_IP					   VARCHAR2(40) NOT NULL,
	SERVICE_ALIVE				   NUMBER(1) NOT NULL,
	DB_LOCK_VER_NBR	               NUMBER(8) DEFAULT 0,
	CONSTRAINT EN_SERVICE_DEF_T_PK PRIMARY KEY (SERVICE_DEF_ID)
);




CREATE TABLE EN_SERVICE_DEF_INTER_T (
	SERVICE_DEF_INTER_ID		NUMBER(14) NOT NULL,
	SERVICE_DEF_ID			   	   NUMBER(14) NOT NULL,
	SERVICE_INTERFACE 			VARCHAR2(500) NOT NULL,
	DB_LOC_VER_NBR			    NUMBER(8) DEFAULT 0,
	CONSTRAINT EN_SERVICE_DEF_INTER_T_PK PRIMARY KEY (SERVICE_DEF_INTER_ID)
);





CREATE TABLE EN_MESSAGE_QUE_T (
   MESSAGE_QUE_ID			  NUMBER(14) NOT NULL,
   MESSAGE_QUE_DT             DATE NOT NULL,
   MESSAGE_EXP_DT			  DATE NULL,
   MESSAGE_QUE_PRIO_NBR       NUMBER(8) NOT NULL,
   MESSAGE_QUE_STAT_CD        CHAR(1) NOT NULL,
   MESSAGE_QUE_RTRY_CNT       NUMBER(8) NOT NULL,
   MESSAGE_QUE_IP_NBR         VARCHAR2(2000) NOT NULL,
   MESSAGE_PAYLOAD 		      LONG NOT NULL,
   MESSAGE_SERVICE_NM		  VARCHAR2(255),
   MESSAGE_ENTITY_NM 		  VARCHAR2(10) NOT NULL,
   SERVICE_METHOD_NM		  VARCHAR2(2000) NULL,
   DB_LOCK_VER_NBR	          NUMBER(8) DEFAULT 0,
   CONSTRAINT EN_MESSAGE_QUE_T_PK PRIMARY KEY (MESSAGE_QUE_ID) USING INDEX
);




CREATE TABLE EN_BAM_T (
	BAM_ID 				NUMBER(14) NOT NULL,
	SERVICE_NM			VARCHAR2(255) NOT NULL,
	SERVICE_URL			VARCHAR2(500) NOT NULL,
	METHOD_NM			VARCHAR(2000) NOT NULL,
	THREAD_NM			VARCHAR(500) NOT NULL,
	CALL_DT				DATE NOT NULL,
	TARGET_TO_STRING	VARCHAR2(2000) NOT NULL,
	SRVR_IND_IND		NUMBER(1) NOT NULL,
	EXCEPTION_TO_STRING	VARCHAR2(2000),
	EXCEPTION_MSG		LONG,
	CONSTRAINT EN_BAM_T_PK PRIMARY KEY (BAM_ID)
);



CREATE TABLE EN_BAM_PARAM_T (
	BAM_PARAM_ID		NUMBER(14) NOT NULL,
	BAM_ID 				NUMBER(14) NOT NULL,
	PARAM				LONG NOT NULL,
	CONSTRAINT EN_BAM_PARAM_T_PK PRIMARY KEY (BAM_PARAM_ID)
);

CREATE INDEX EN_SERVICE_DEF_TI1 ON EN_SERVICE_DEF_T (SERVER_IP, MESSAGE_ENTITY_NM);
CREATE INDEX EN_SERVICE_DEF_INTER_TI1 ON EN_SERVICE_DEF_INTER_T (SERVICE_DEF_ID);
CREATE INDEX EN_MESSAGE_QUE_TI1 ON EN_MESSAGE_QUE_T (MESSAGE_SERVICE_NM, SERVICE_METHOD_NM);
CREATE INDEX EN_MESSAGE_QUE_TI2 ON EN_MESSAGE_QUE_T (MESSAGE_ENTITY_NM, MESSAGE_QUE_STAT_CD, MESSAGE_QUE_IP_NBR, MESSAGE_QUE_DT);
CREATE INDEX EN_BAM_TI1 ON EN_BAM_T (SERVICE_NM, METHOD_NM);
CREATE INDEX EN_BAM_TI2 ON EN_BAM_T (SERVICE_NM);
CREATE INDEX EN_BAM_PARAM_TI1 ON EN_BAM_PARAM_T (BAM_ID);


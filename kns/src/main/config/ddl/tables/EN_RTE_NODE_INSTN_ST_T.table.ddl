CREATE TABLE EN_RTE_NODE_INSTN_ST_T (
	RTE_NODE_INSTN_ST_ID		   NUMBER(19) NOT NULL,
    RTE_NODE_INSTN_ID   		   NUMBER(19) NOT NULL,
	ST_KEY						   VARCHAR2(255) NOT NULL,
	ST_VAL_TXT					   VARCHAR2(2000),
	DB_LOCK_VER_NBR	               NUMBER(8) DEFAULT 0,
	CONSTRAINT EN_RTE_NODE_INSTN_ST_T_PK PRIMARY KEY (RTE_NODE_INSTN_ST_ID) USING INDEX
)
/
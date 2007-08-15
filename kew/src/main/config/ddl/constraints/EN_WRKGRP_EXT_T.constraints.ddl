ALTER TABLE EN_WRKGRP_EXT_T ADD CONSTRAINT EN_WRKGRP_EXT_TR1
FOREIGN KEY (WRKGRP_ID, WRKGRP_VER_NBR)
REFERENCES EN_WRKGRP_T (WRKGRP_ID, WRKGRP_VER_NBR)
/

ALTER TABLE EN_WRKGRP_EXT_T ADD CONSTRAINT EN_WRKGRP_EXT_TR2
FOREIGN KEY (WRKGRP_TYP_ATTRIB_ID)
REFERENCES EN_WRKGRP_TYP_ATTRIB_T (WRKGRP_TYP_ATTRIB_ID)
/

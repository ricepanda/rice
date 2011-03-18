package org.kuali.rice.krms.api.repository;


//import org.kuali.rice.kns.bo.ExternalizableBusinessObject;

public interface KrmsTypeContract {
	/**
	 * This is the ID for the KRMSType 
	 *
	 * <p>
	 * It is a ID of a KRMS type.
	 * </p>
	 * @return ID for KRMS type.
	 */
	public String getId();

	/**
	 * This is the name for the KRMSType 
	 *
	 * <p>
	 * It is a name of a KRMS type.
	 * </p>
	 * @return name for KRMS type.
	 */
	public String getName();

	/**
	 * This is the namespace code. 
	 *
	 * <p>
	 * It provides scope of the KRMS type.
	 * </p>
	 * @return the namespace code of the KRMS type.
	 */
	public String getNamespace();

	/**
	 * This is the name of the KRMS KrmsType service
	 * 
	 * @return the service name of the KRMS type
	 */
	public String getServiceName();
	
	/**
	 * @return the active
	 */
	public boolean isActive();

}

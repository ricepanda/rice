/*
 * Copyright 2007-2008 The Kuali Foundation
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
package org.kuali.rice.edl.impl.xml.export;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.kuali.rice.core.api.impex.ExportDataSet;
import org.kuali.rice.core.api.services.CoreApiServiceLocator;
import org.kuali.rice.core.impl.style.StyleBo;
import org.kuali.rice.edl.impl.bo.EDocLiteAssociation;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.help.HelpEntry;
import org.kuali.rice.kew.rule.RuleBaseValues;
import org.kuali.rice.kew.rule.RuleDelegation;
import org.kuali.rice.kew.rule.bo.RuleAttribute;
import org.kuali.rice.kew.rule.bo.RuleTemplate;
import org.kuali.rice.kim.bo.impl.GroupImpl;
import org.kuali.rice.kns.bo.BusinessObject;
import org.kuali.rice.kns.bo.Exporter;
import org.kuali.rice.kns.exception.ExportNotSupportedException;
import org.kuali.rice.kns.util.KNSConstants;

/**
 * An implementation of the {@link Exporter} class which facilitates exporting
 * of EDocLite data from the GUI.
 * 
 * @see ExportDataSet
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class EdlDataExporter implements Exporter {

	private List<String> supportedFormats = new ArrayList<String>();

	public EdlDataExporter() {
		supportedFormats.add(KNSConstants.XML_FORMAT);
	}

	@Override
	public void export(Class<? extends BusinessObject> businessObjectClass, List<BusinessObject> businessObjects, String exportFormat, OutputStream outputStream) throws IOException {
		if (!KNSConstants.XML_FORMAT.equals(exportFormat)) {
			throw new ExportNotSupportedException("The given export format of " + exportFormat + " is not supported by the EDocLite XML Exporter!");
		}
		ExportDataSet dataSet = buildExportDataSet(businessObjectClass, businessObjects);
		outputStream.write(CoreApiServiceLocator.getXmlExporterService().export(dataSet));
		outputStream.flush();
	}

	@Override
	public List<String> getSupportedFormats(Class<? extends BusinessObject> businessObjectClass) {
		return supportedFormats;
	}

	/**
	 * Builds the ExportDataSet based on the BusinessObjects passed in.
	 */
	protected ExportDataSet buildExportDataSet(Class<? extends BusinessObject> businessObjectClass, List<BusinessObject> businessObjects) {
		EdlExportDataSet dataSet = new EdlExportDataSet();
		for (BusinessObject businessObject : businessObjects) {
			if (businessObjectClass.equals(EDocLiteAssociation.class)) {
				dataSet.getEdocLites().add((EDocLiteAssociation)businessObject);
			}   
		}

		return  dataSet.createExportDataSet();
	}

}

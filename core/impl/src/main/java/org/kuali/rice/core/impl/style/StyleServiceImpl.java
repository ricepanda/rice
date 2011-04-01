/*
 * Copyright 2006-2011 The Kuali Foundation
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

package org.kuali.rice.core.impl.style;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.style.Style;
import org.kuali.rice.core.api.style.StyleService;
import org.kuali.rice.core.framework.services.CoreFrameworkServiceLocator;
import org.kuali.rice.core.impl.services.CoreImplServiceLocator;
import org.kuali.rice.core.util.RiceUtilities;
import org.kuali.rice.kew.util.KEWConstants;
import org.kuali.rice.kns.util.KNSConstants;
import org.kuali.rice.ksb.cache.RiceCacheAdministrator;


/**
 * Implements generic StyleService via existing EDL style table
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class StyleServiceImpl implements StyleService {
    private static final Logger LOG = Logger.getLogger(StyleServiceImpl.class);

    private static final String TEMPLATES_CACHE_GROUP_NAME = "Templates";
    private static final String STYLE_CONFIG_PREFIX = "edl.style";

    private StyleDao dao;
    private RiceCacheAdministrator cache;

    public void setStyleDao(StyleDao dao) {
        this.dao = dao;
    }
    
    public void setCache(RiceCacheAdministrator cache) {
    	this.cache = cache;
    }

    /**
     * Loads the named style from the database, or (if configured) imports it from a file
     * specified via a configuration parameter with a name of the format edl.style.&lt;styleName&gt;
     * {@inheritDoc}
     * @see org.kuali.rice.edl.impl.service.StyleService#getStyle(java.lang.String)
     */
    @Override
    public Style getStyle(String styleName) {
    	// try to fetch the style from the database
        StyleBo style = dao.getStyle(styleName);
        // if it's null, look for a config param specifiying a file to load
        if (style == null) {
            String propertyName = STYLE_CONFIG_PREFIX + "." + styleName;
            String location = ConfigContext.getCurrentContextConfig().getProperty(propertyName);
            if (location != null) {
            	
                InputStream xml = null;

                try {
                    xml = RiceUtilities.getResourceAsStream(location);
                } catch (MalformedURLException e) {
                    throw new RiceRuntimeException(getUnableToLoadMessage(propertyName, location), e);
                } catch (IOException e) {
                    throw new RiceRuntimeException(getUnableToLoadMessage(propertyName, location), e);
                }

                if (xml == null) {
                    throw new RiceRuntimeException(getUnableToLoadMessage(propertyName, location) + ", no such file");
                }
                
                LOG.info("Automatically importing style '" + styleName + "' from '" + location + "' as configured by "+ propertyName);
                CoreImplServiceLocator.getStyleXmlLoader().loadXml(xml, null);
            }
        }
        return StyleBo.to(style);
    }

    /**
     * This method ...
     *
     * @param propertyName
     * @param location
     * @return
     */
    private String getUnableToLoadMessage(String propertyName, String location) {
        return "unable to load resource at '" + location +
                "' specified by configuration parameter '" + propertyName + "'";
    }

    @Override
    public Templates getStyleAsTranslet(String name) throws TransformerConfigurationException {
        if (name == null) return null;
        Templates translet = fetchTemplatesFromCache(name);
        if (translet == null) {
            Style style = getStyle(name);
            if (style == null) {
                return null;
            }

            boolean useXSLTC = CoreFrameworkServiceLocator.getParameterService().getParameterValueAsBoolean(KEWConstants.KEW_NAMESPACE, KNSConstants.DetailTypes.EDOC_LITE_DETAIL_TYPE, KEWConstants.EDL_USE_XSLTC_IND);
            if (useXSLTC) {
                LOG.info("using xsltc to compile stylesheet");
                String key = "javax.xml.transform.TransformerFactory";
                String value = "org.apache.xalan.xsltc.trax.TransformerFactoryImpl";
                Properties props = System.getProperties();
                props.put(key, value);
                System.setProperties(props);
            }

            TransformerFactory factory = TransformerFactory.newInstance();
            factory.setURIResolver(new StyleUriResolver(this));

            if (useXSLTC) {
                factory.setAttribute("translet-name",name);
                factory.setAttribute("generate-translet",Boolean.TRUE);
                String debugTransform = CoreFrameworkServiceLocator.getParameterService().getParameterValueAsString(KEWConstants.KEW_NAMESPACE, KNSConstants.DetailTypes.EDOC_LITE_DETAIL_TYPE, KEWConstants.EDL_DEBUG_TRANSFORM_IND);
                if (debugTransform.trim().equals("Y")) {
                    factory.setAttribute("debug", Boolean.TRUE);
                }
            }

            translet = factory.newTemplates(new StreamSource(new StringReader(style.getXmlContent())));
            putTemplatesInCache(name, translet);
        }
        return translet;
    }

    /**
     * Returns all styles
     */
    @Override
    public List<Style> getStyles() {
        List<StyleBo> styleBos = dao.getStyles();
        List<Style> styles = new ArrayList<Style>();
        if (styleBos != null) {
        	for (StyleBo styleBo : styleBos) {
        		styles.add(StyleBo.to(styleBo));
        	}
        }
        return styles;
    }

    /**
     * Returns all style names
     */
    @Override
    public List<String> getStyleNames() {
        return dao.getStyleNames();
    }

    /**
     * Does not currently take into account style sheet dependences robustly
     */
    private void removeStyleFromCache(String styleName) {
        LOG.info("Removing Style " + styleName + " from the style cache");
        // we don't know what styles may import other styles so we need to flush them all
        cache.flushGroup(TEMPLATES_CACHE_GROUP_NAME);
        //KEWServiceLocator.getCacheAdministrator().flushEntry(getTemplatesCacheKey(styleName));
    }

    @Override
    public void saveStyle(Style data) {
    	if (data == null) {
    		throw new IllegalArgumentException("The given style was null.");
    	}
    	StyleBo styleToUpdate = StyleBo.from(data);
    	saveStyleBo(styleToUpdate);
    }
    
    protected void saveStyleBo(StyleBo styleBo) {
    	StyleBo existingData = dao.getStyle(styleBo.getName());
        if (existingData != null) {
            existingData.setActive(false);
            dao.saveStyle(existingData);
        }
        dao.saveStyle(styleBo);
        removeStyleFromCache(styleBo.getName());
    }
    
    // cache helper methods

    /**
     * Returns the key to be used for caching the Templates for the given style name.
     */
    protected String getTemplatesCacheKey(String styleName) {
        return TEMPLATES_CACHE_GROUP_NAME + ":" + styleName;
    }

    protected Templates fetchTemplatesFromCache(String styleName) {
        return (Templates) cache.getFromCache(getTemplatesCacheKey(styleName));
    }

    protected void putTemplatesInCache(String styleName, Templates templates) {
        cache.putInCache(getTemplatesCacheKey(styleName), templates, TEMPLATES_CACHE_GROUP_NAME);
    }

}

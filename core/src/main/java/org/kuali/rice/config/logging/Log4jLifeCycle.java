package org.kuali.rice.config.logging;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.FileWatchdog;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.xml.DOMConfigurator;
import org.kuali.rice.config.Config;
import org.kuali.rice.core.Core;
import org.kuali.rice.lifecycle.BaseLifecycle;
import org.springframework.util.Log4jConfigurer;
import org.springframework.util.ResourceUtils;
import org.w3c.dom.Document;

/**
 * Lifecycle implementation that initializes and shuts down Log4J logging
 */
public class Log4jLifeCycle extends BaseLifecycle {

    /**
     * Convenience constant representing a minute in milliseconds
     */
    private static final int MINUTE = 60 * 1000;

    /**
     * Location of default/automatic Log4J configuration properties, in Spring ResourceUtils resource/url syntax
     */
    private static final String AUTOMATIC_LOGGING_CONFIG_URL = "classpath:META-INF/workflow-log4j.properties";

    /**
     * Default settings reload interval to use in the case that the settings are reloadable (i.e. they originate from a file)
     */
    private static final int DEFAULT_RELOAD_INTERVAL = 5 * MINUTE; // 5 minutes

    /**
     * Non-static and non-final so that it can be reset after configuration is read
     */
    private Logger log = Logger.getLogger(getClass());

	public void start() throws Exception {
        // obtain the root workflow config
		Config config = Core.getRootConfig();

        // first check for in-line xml configuration
		String log4jconfig = config.getProperty(Config.LOG4J_SETTINGS_XML);
		if (log4jconfig != null) {
			try {
				DocumentBuilder b = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				Document doc = b.parse(new ByteArrayInputStream(log4jconfig.getBytes()));
				DOMConfigurator.configure(doc.getDocumentElement());
				// now get the reconfigured log instance
				log = Logger.getLogger(getClass());
			} catch (Exception e) {
				log.error("Error parsing Log4J configuration settings: " + log4jconfig, e);
			}
        // next check for in-line properties configuration
		} else if ((log4jconfig = config.getProperty(Config.LOG4J_SETTINGS_PROPS)) != null) {
			Properties p = new Properties(config.getProperties());
			try {
				p.load(new ByteArrayInputStream(log4jconfig.getBytes()));
				PropertyConfigurator.configure(p);
				log = Logger.getLogger(getClass());
			} catch (IOException ioe) {
				log.error("Error loading Log4J configuration settings: " + log4jconfig, ioe);
			}
        // check for an external file location specification
		} else if ((log4jconfig = config.getProperty(Config.LOG4J_SETTINGS_PATH)) != null) {
			log.info("Configuring Log4J logging.");

            int reloadInterval = DEFAULT_RELOAD_INTERVAL;

            String log4jReloadInterval = config.getProperty(Config.LOG4J_SETTINGS_RELOADINTERVAL_MINS);
			if (log4jReloadInterval != null) {
				try {
                    reloadInterval = new Integer(log4jReloadInterval).intValue() * MINUTE;
				} catch (NumberFormatException nfe) {
					log.warn("Invalid reload interval: " + log4jReloadInterval + ", using default: 5 minutes");
				}
			}

            // if we are using a specific version of Log4j for which we have written subclasses that allow
            // variable substitution using core config, then use those custom classes to do so
            // otherwise use the plain Spring Log4jConfigurer
            if ("1.2.13".equals(getLog4jVersion())) {
                log.info("Using custom Log4j 1.2.13 configurer to make workflow config properties accessible");
                // use custom impl based on 1.2.13 to insert workflow config properties for resolution

                WorkflowLog4j_1_2_13_Configurer.initLoggingWithProperties(config.getProperties(), log4jconfig, reloadInterval);
            } else {
                log.info("Using standard Spring Log4jConfigurer");
                // just use standard Spring configurer
                Log4jConfigurer.initLogging(log4jconfig, reloadInterval);
            }

			log = Logger.getLogger(getClass());
        // finally fall back to a Log4J configuration shipped with workflow
		} else {
			Log4jConfigurer.initLogging(AUTOMATIC_LOGGING_CONFIG_URL, DEFAULT_RELOAD_INTERVAL);
			log = Logger.getLogger(getClass());
		}
		super.start();
	}

    /**
     * Uses reflection to attempt to obtain the ImplementationVersion of the org.apache.log4j
     * package from the jar manifest.
     * @return the value returned from Package.getPackage("org.apache.log4j").getImplementationVersion()
     * or null if package is not found
     */
    private static String getLog4jVersion() {
        Package p = Package.getPackage("org.apache.log4j");
        if (p == null) return null;
        return p.getImplementationVersion();
    }

    /**
     * Subclasses the Spring Log4jConfigurer to expose a static method which accepts an initial set of
     * properties (to use for variable substitution)
     * @author Aaron Hamid (arh14 at cornell dot edu)
     */
    private static final class WorkflowLog4j_1_2_13_Configurer extends Log4jConfigurer {
        public static void initLoggingWithProperties(Properties props, String location, long refreshInterval) throws FileNotFoundException {
            File file = ResourceUtils.getFile(location);
            if (!file.exists()) {
                throw new FileNotFoundException("Log4J config file [" + location + "] not found");
            }
            if (location.toLowerCase().endsWith(XML_FILE_EXTENSION)) {
                DOMConfigurator.configureAndWatch(file.getAbsolutePath(), refreshInterval);
            } else {
                assert(props != null);
                WorkflowLog4j_1_2_13_PropertyConfigurator.configureAndWatch(props, file.getAbsolutePath(), refreshInterval);
            }
        }
    }

    /**
     * Subclasses the Log4j 1.2.13 PropertyConfigurator to add a static method which accepts an initial
     * set of properties (to use for variable substitution)
     */
    protected static final class WorkflowLog4j_1_2_13_PropertyConfigurator extends PropertyConfigurator {
        static public void configureAndWatch(final Properties initialProperties, String configFilename, long delay) {
            // cannot just use a subclass and pass the initial properties to constructor as the super constructor
            // is invoked before the properties member can be set, and doOnChange will be called from constructor
            // with null properties
            // so instead create an anonymous subclass with closure that includes initialProperties
            FileWatchdog watchDog = new FileWatchdog(configFilename) {
                public void doOnChange() {
                    new WorkflowLog4j_1_2_13_PropertyConfigurator().doConfigure(initialProperties, this.filename, LogManager.getLoggerRepository());
                }
            };
            watchDog.setDelay(delay);
            watchDog.start();
        }

        public void doConfigure(Properties initialProperties, String configFileName, LoggerRepository hierarchy) {
          Properties props = new Properties();
          props.putAll(initialProperties);

          try {
            FileInputStream istream = new FileInputStream(configFileName);
            props.load(istream);
            istream.close();
          }
          catch (IOException e) {
            LogLog.error("Could not read configuration file ["+configFileName+"].", e);
            LogLog.error("Ignoring configuration file [" + configFileName+"].");
            return;
          }
          // If we reach here, then the config file is alright.
          doConfigure(props, hierarchy);
        }
    }

    public void stop() throws Exception {
		LogManager.shutdown();
		super.stop();
	}

}

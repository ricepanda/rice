/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.testtools.selenium;

import org.kuali.rice.testtools.common.PropertiesUtils;

import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Link test failures to existing Jiras as a html link in Jenkins.
 * </p><p>
 * The more failures the more useful it is to not have to keep tracking down the same Jiras.
 * </p><p>
 * Set jira.aware.regex.failures.location and jira.aware.contains.failures.location to define file locations, else
 * JiraAwareRegexFailures.properties and JiraAwareContainsFailures.properties will be read as a resource stream.
 * </p><p>
 * TODO:
 * <ol>
 *   <li>Integration Test integration.  ITs often fail by the 10s tracking down existing Jiras is a huge time sink.</li>
 * </ol>
 * </p>
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class JiraAwareFailureUtil {

    /**
     * https://jira.kuali.org/browse/
     */
    public static final String JIRA_BROWSE_URL = "https://jira.kuali.org/browse/";

    private static String REGEX_PROPS_LOCATION = System.getProperty("jira.aware.regex.failures.location", null);
    private static String REGEX_DEFAULT_PROPS_LOCATION = "JiraAwareRegexFailures.properties";

    private static String CONTAINS_PROPS_LOCATION = System.getProperty("jira.aware.contains.failures.location", null);
    private static String CONTAINS_DEFAULT_PROPS_LOCATION = "JiraAwareContainsFailures.properties";

    static Properties regexJiraMatches; // for more powerful matching

    static Properties jiraMatches; // simple contains matching

    static {
        try {
            regexJiraMatches = new PropertiesUtils().loadProperties(REGEX_PROPS_LOCATION, REGEX_DEFAULT_PROPS_LOCATION);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            jiraMatches = new PropertiesUtils().loadProperties(CONTAINS_PROPS_LOCATION, CONTAINS_DEFAULT_PROPS_LOCATION);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>
     * Calls {@see #failOnMatchedJira(String, Failable)} and calls fail on the {@see Failable#fail} if no matched jira failures.
     * </p>
     * @param message to pass to fail
     * @param failable {@see Failable#fail}
     */
    public static void fail(String message, Failable failable) {
        failOnMatchedJira(message, failable);
        failable.fail(message);
    }

    /**
     * <p>
     * Calls {@see #failOnMatchedJira(String, String, Failable)} and calls fail on the {@see Failable#fail} fails if no matched jira failures.
     * </p>
     * @param contents
     * @param message to pass to fail
     * @param failable {@see Failable#fail}
     */
    public static void fail(String contents, String message, Failable failable) {
        failOnMatchedJira(contents, message, failable);
        failable.fail(message);
    }

    /**
     * <p>
     * Calls {@see #failOnMatchedJira(String, String)} with the contents and if no match is detected then the message.
     * </p>
     * @param contents to check for containing of the jiraMatches keys.
     * @param message to check for containing of the jiraMatches keys if contents doesn't
     * @param failable to fail with the jiraMatches value if the contents or message is detected
     */
    public static void failOnMatchedJira(String contents, String message, Failable failable) {
        failOnMatchedJira(contents, failable);
        failOnMatchedJira(message, failable);
    }

    /**
     * <p>
     * If the contents contents the jiraMatches key, calls fail on the {@see Failable#fail} passing in the jiraMatches value for the matched key.
     * </p>
     * @param contents to check for containing of the jiraMatches keys.
     * @param failable to fail with the jiraMatches value if the jiraMatches key is contained in the contents
     */
    public static void failOnMatchedJira(String contents, Failable failable) {
        String key = null;
        Pattern pattern = null;
        Matcher matcher = null;

        Iterator iter = regexJiraMatches.keySet().iterator();

        while (iter.hasNext()) {
            key = (String)iter.next();
            pattern = Pattern.compile(key, Pattern.DOTALL); // match across line terminators
            matcher = pattern.matcher(contents);

            if (matcher.find()) {
                failable.fail("\n" + JIRA_BROWSE_URL + regexJiraMatches.get(key) + "\n\n" + contents);
            }
        }

        iter = jiraMatches.keySet().iterator();

        while (iter.hasNext()) {
            key = (String)iter.next();
            if (contents.contains(key)) {
                failable.fail("\n" + JIRA_BROWSE_URL + jiraMatches.get(key) + "\n\n" + contents);
            }
        }
    }
}
/*
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
package edu.samplu.common;

import org.junit.Assert;
import org.junit.runner.RunWith;

/**
 * SmokeTests should extend this Base class or have it in their class hierarchy.
 *
 * The abstract method getBookmarkUrl should be implemented to return the Bookmark URL
 * of the page under test.  The abstract method navigate should be implemented to Navigate
 * through the UI to the page under test.
 */
@RunWith(SmokeTestRunner.class)
public abstract class SmokeTestBase extends WebDriverLegacyITBase {

    private String testUrl = ITUtil.KRAD_PORTAL;

    private boolean shouldNavigate = false;

    protected abstract String getBookmarkUrl();

    protected abstract void navigate() throws Exception;

    @Override
    public String getTestUrl() {
        return testUrl;
    }

    protected void enableBookmarkMode() {
        this.testUrl = getBookmarkUrl();
    }

    protected void enableNavigationMode() {
        this.shouldNavigate = true;
        this.testUrl = ITUtil.KRAD_PORTAL;
    }

    @Override
    public void fail(String message) {
        passed = false;
        Assert.fail(message);
    }

    @Override
    protected void navigateInternal() throws Exception {
        if (this.shouldNavigate) {
            navigate();
        }
    }
}
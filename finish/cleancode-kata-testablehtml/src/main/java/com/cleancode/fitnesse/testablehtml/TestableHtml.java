package com.cleancode.fitnesse.testablehtml;

import fitnesse.wiki.*;

public class TestableHtml {

    public String testableHtml(PageData pageData, boolean includeSuiteSetup) throws Exception {
        return new SetupTeardownSurrounder(pageData, includeSuiteSetup).surround();
    }
}

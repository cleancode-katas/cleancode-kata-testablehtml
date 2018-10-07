package com.cleancode.fitnesse.testablehtml;

import fitnesse.responders.run.SuiteResponder;
import fitnesse.wiki.*;

public class SetupTeardownSurrounder {
    private PageData pageData;
    private boolean includeSuiteSetup;
    private WikiPage wikiPage;
    private String content;
    private PageCrawler crawler;

    public SetupTeardownSurrounder(PageData pageData, boolean includeSuiteSetup) {
        this.pageData = pageData;
        this.includeSuiteSetup = includeSuiteSetup;
        wikiPage = pageData.getWikiPage();
        content = "";
        crawler = wikiPage.getPageCrawler();
    }

    public String surround() throws Exception {

        if (isTestPage()) {
            surroundPageWithSetupsAndTeardowns();
        } else {
            content += pageData.getContent();
        }

        pageData.setContent(content);
        return pageData.getHtml();
    }

    private void surroundPageWithSetupsAndTeardowns() throws Exception {
        content += includeSetups();
        content += pageData.getContent();
        content += includeTeardowns();
    }

    private boolean isTestPage() throws Exception {
        return pageData.hasAttribute("Test");
    }

    private String includeTeardowns() throws Exception {
        String teardowns = "";
        teardowns += includeIfInherited("teardown", "TearDown");
        if (includeSuiteSetup) {
            teardowns += includeIfInherited("teardown", SuiteResponder.SUITE_TEARDOWN_NAME);
        }
        return teardowns;
    }

    private String includeSetups() throws Exception {
        String setups = "";
        if (includeSuiteSetup) {
            setups += includeIfInherited("setup", SuiteResponder.SUITE_SETUP_NAME);
        }
        setups += includeIfInherited("setup", "SetUp");
        return setups;
    }

    private String includeIfInherited(String mode, String pageName) throws Exception {
        WikiPage page = PageCrawlerImpl.getInheritedPage(pageName, wikiPage);
        if (page != null) {
            return includePage(mode, page);
        }
        return "";
    }

    private String includePage(String mode, WikiPage page) throws Exception {
        WikiPagePath pagePath = crawler.getFullPath(page);
        String pagePathName = PathParser.render(pagePath);
        return String.format("!include -%s .%s\n", mode, pagePathName);
    }
}

package com.cleancode.fitnesse.testablehtml;

import fitnesse.responders.run.SuiteResponder;
import fitnesse.wiki.*;

public class TestableHtmlMaker {
    private PageData pageData;
    private boolean includeSuiteSetup;
    private WikiPage wikiPage;
    private StringBuffer buffer;
    private PageCrawler crawler;

    public TestableHtmlMaker(PageData pageData, boolean includeSuiteSetup) {
        this.pageData = pageData;
        this.includeSuiteSetup = includeSuiteSetup;
        wikiPage = pageData.getWikiPage();
        buffer = new StringBuffer();
        crawler = wikiPage.getPageCrawler();
    }

    public String invoke() throws Exception {

        if (pageData.hasAttribute("Test")) {
            String mode = "setup";
            if (includeSuiteSetup) {
                includeIfInherited(mode, SuiteResponder.SUITE_SETUP_NAME);
            }
            includeIfInherited(mode, "SetUp");
        }

        buffer.append(pageData.getContent());
        if (pageData.hasAttribute("Test")) {
            String mode = "teardown";
            includeIfInherited(mode, "TearDown");
            if (includeSuiteSetup) {
                includeIfInherited(mode, SuiteResponder.SUITE_TEARDOWN_NAME);
            }
        }

        pageData.setContent(buffer.toString());
        return pageData.getHtml();
    }

    private void includeIfInherited(String mode, String pageName) throws Exception {
        WikiPage suiteSetup = PageCrawlerImpl.getInheritedPage(pageName, wikiPage);
        if (suiteSetup != null) {
            includePage(mode, suiteSetup);
        }
    }

    private void includePage(String mode, WikiPage page) throws Exception {
        WikiPagePath pagePath = crawler.getFullPath(page);
        String pagePathName = PathParser.render(pagePath);
        buffer.append(String.format("!include -%s .%s\n", mode, pagePathName));
    }
}

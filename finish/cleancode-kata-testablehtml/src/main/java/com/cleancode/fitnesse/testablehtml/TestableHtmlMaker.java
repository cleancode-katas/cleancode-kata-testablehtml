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
        WikiPage page = PageCrawlerImpl.getInheritedPage(pageName, wikiPage);
        if (page != null) {
            buffer.append(includePage(mode, page));
        }
    }

    private String includePage(String mode, WikiPage page) throws Exception {
        WikiPagePath pagePath = crawler.getFullPath(page);
        String pagePathName = PathParser.render(pagePath);
        return String.format("!include -%s .%s\n", mode, pagePathName);
    }
}

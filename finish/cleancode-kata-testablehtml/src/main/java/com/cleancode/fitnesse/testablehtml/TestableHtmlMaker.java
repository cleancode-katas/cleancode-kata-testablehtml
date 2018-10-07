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
                WikiPage suiteSetup = PageCrawlerImpl.getInheritedPage(SuiteResponder.SUITE_SETUP_NAME, wikiPage);
                if (suiteSetup != null) {
                    WikiPagePath pagePath = crawler.getFullPath(suiteSetup);
                    String pagePathName = PathParser.render(pagePath);
                    buffer.append("!include -" + mode + " .").append(pagePathName).append("\n");
                }
            }
            WikiPage setup = PageCrawlerImpl.getInheritedPage("SetUp", wikiPage);
            if (setup != null) {
                WikiPagePath setupPath = crawler.getFullPath(setup);
                String setupPathName = PathParser.render(setupPath);
                buffer.append("!include -" + mode + " .").append(setupPathName).append("\n");
            }
        }

        buffer.append(pageData.getContent());
        if (pageData.hasAttribute("Test")) {
            WikiPage teardown = PageCrawlerImpl.getInheritedPage("TearDown", wikiPage);
            String mode = "teardown";
            if (teardown != null) {
                WikiPagePath tearDownPath = crawler.getFullPath(teardown);
                String tearDownPathName = PathParser.render(tearDownPath);
                buffer.append("!include -" + mode + " .").append(tearDownPathName).append("\n");
            }
            if (includeSuiteSetup) {
                WikiPage suiteTeardown = PageCrawlerImpl.getInheritedPage(SuiteResponder.SUITE_TEARDOWN_NAME, wikiPage);
                if (suiteTeardown != null) {
                    WikiPagePath pagePath = crawler.getFullPath(suiteTeardown);
                    String pagePathName = PathParser.render(pagePath);
                    buffer.append("!include -" + mode + " .").append(pagePathName).append("\n");
                }
            }
        }

        pageData.setContent(buffer.toString());
        return pageData.getHtml();
    }
}

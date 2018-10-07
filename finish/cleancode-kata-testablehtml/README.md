# Clean Code Kata - TestableHtml

Code used to demonstrate in a workshop how clean code principles can be
applied to existing legacy code.

Activity

- select the whole code of testableHtml method
- Extract into Method Object name it TestableHtmlMaker
- Move TestableHtmlMaker class to top level
- Make the local variables fields
- make wikiPage field, shift init in constructor
- make buffer a field, shift init in constructor

Observe

- three lines of code repeating pattern
- the code is different in some parts
- direct extract method is not possible

Activity

- Extract wikiPage.getPageCrawler() into field crawler, init in constructor
- test should pass
- Select setup word in the string and use Extract Variable, replace all 2 occurences
- name the variable mode
- Select teardown word in the string and use Extract Variable, replace all 2 occurences
- name the variable mode




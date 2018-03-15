package com.rousseau_alexandre.scrawleo.services;


import static com.rousseau_alexandre.scrawleo.models.ScrapedPage.DESCRIPTION_MAX;
import static com.rousseau_alexandre.scrawleo.models.ScrapedPage.DESCRIPTION_MIN;
import static com.rousseau_alexandre.scrawleo.models.ScrapedPage.TITLE_MAX;

public enum PageError implements Comparable<PageError> {

    TITLE_TOO_LONG("Title should not exceed " + TITLE_MAX + " chars", 10),
    DESCRIPTION_TOO_SHORT("Meta tag \"Description\" should not be lower than " + DESCRIPTION_MIN + " chars", 5),
    DESCRIPTION_TOO_LONG("Meta tag \"Description\" should not exceed " + DESCRIPTION_MAX + " chars", 5),
    // empty
    TITLE_EMPTY("\"Title\" tag not found", 40),
    H1_EMPTY("\"h1\" tag not found", 40),
    DESCRIPTION_EMPTY("Meta tag \"Description\" not found", 10),
    KEYWORDS_EMPTY("Meta tag \"Keywords\" not found", 10),
    IMG_ALT_EMPTY("\"alt\" attribute is missing for an \"img\" tag", 10),
    // duplicates
    TITLE_DUPLICATE("\"Title\" is duplicate on another page", 30),
    H1_DUPLICATE("\"h1\" is duplicate on another page", 30),
    DESCRIPTION_DUPLICATE("Meta tag \"Description\" is duplicate on another page", 10),
    // not found
    UNREACHABLE("ScrapedPage is unreachable", 100);

    private final String description;

    private final int severity;

    PageError(String _description, int _severity) {
        description = _description;
        severity = _severity;
    }

    public int getSeverity() {
        return severity;
    }
}

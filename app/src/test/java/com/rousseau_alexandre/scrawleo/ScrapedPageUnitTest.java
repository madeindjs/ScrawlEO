package com.rousseau_alexandre.scrawleo;

import com.rousseau_alexandre.scrawleo.models.ScrapedPage;

import junit.framework.TestCase;

/**
 * Created by arousseau on 20/02/18.
 */

public class ScrapedPageUnitTest extends TestCase {

    public void testGetUrlWithoutDomain() {
        ScrapedPage scrapedPage = new ScrapedPage("http://test.fr/test/test");

        assertEquals("/test/test", scrapedPage.getUrlWithoutDomain());
    }

}

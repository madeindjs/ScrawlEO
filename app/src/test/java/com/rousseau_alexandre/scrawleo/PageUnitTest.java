package com.rousseau_alexandre.scrawleo;

import com.rousseau_alexandre.scrawleo.models.Page;

import junit.framework.TestCase;

/**
 * Created by arousseau on 20/02/18.
 */

public class PageUnitTest extends TestCase {

    public void testGetUrlWithoutDomain() {
        Page page = new Page("http://test.fr/test/test");

        assertEquals("/test/test", page.getUrlWithoutDomain());
    }

}

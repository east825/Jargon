package jargon;

import jargon.options.Options;
import org.junit.Test;

/**
 * User: Mikhail Golubev
 * Date: 19.02.13
 * Time: 21:12
 */
public class BuilderTest {
    @Test
    public void testValidOptionNames() {
        Options.newStringOption("-f", "--file", "--file-name", "-1");
    }

    @Test (expected = IllegalArgumentException.class)
    public void testInvalidOptionNames() {
        Options.newStringOption("-not-legal");
    }
}

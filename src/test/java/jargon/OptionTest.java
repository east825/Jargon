package jargon;

import jargon.options.Option;
import jargon.options.Options;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * User: Mikhail Golubev
 * Date: 19.02.13
 * Time: 20:39
 */
public class OptionTest {
    private OptionParser parser;
    private Option<String> fileName;
    @Before
    public void createParser() {
        parser = OptionParser.newOptionParser("test parser").build();
        fileName = Options.newStringOption("-f").build();
        parser.addOption(fileName);
    }
    @Test
    public void testParsing() {
        List<String> posArgs = parser.parse("-f /tmp/123 other options".split("\\s+"));
        assertEquals(Arrays.asList("other", "options"), posArgs);
        assertEquals("/tmp/123", fileName.getSingleValue());
    }
}

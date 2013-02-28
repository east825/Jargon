package jargon;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * User: Mikhail Golubev
 * Date: 19.02.13
 * Time: 20:39
 */
public class QuantifiersTest {
    private OptionParser parser;

    @Before
    public void setUpParser() {
        parser = OptionParser.newInstance("Sample program").build();
        Flag verbose = Options.newFlagOption("-v").build();
        parser.addOption(verbose);
    }

    private List<String> splitAndParse(String args) {
        return parser.parse(args.split(" "));
    }

    @Test
    public void testNoArguments() {
        MultiOption<String> option = Options.newStringOption("-w").nargs(0).build();
        parser.addOption(option);
        List<String> rest = splitAndParse("-w foo bar baz -v quux");
        assertThat(rest, equalTo(Arrays.asList("foo", "bar", "baz", "quux")));
        assertThat(option.getValue().isEmpty(), is(true));
    }

    @Test
    public void testSingleArguments() {
        // default for StringOption and IntegerOption
        Option<String> option = Options.newStringOption("-w").build();
        parser.addOption(option);
        List<String> rest = splitAndParse("-w foo bar baz -v quux");
        assertThat(rest, equalTo(Arrays.asList("bar", "baz", "quux")));
        assertThat(option.getValue(), equalTo("foo"));
    }

    @Test
    public void testTwoArguments() {
        // default for StringOption and IntegerOption
        MultiOption<String> option = Options.newStringOption("-w").nargs(2).build();
        parser.addOption(option);
        List<String> rest = splitAndParse("-w foo bar baz -v quux");
        assertThat(rest, equalTo(Arrays.asList("baz", "quux")));
        assertThat(option.getValue(), equalTo(Arrays.asList("foo", "bar")));
    }

    @Test
    public void testOneOrMore() {
        MultiOption<String> option = Options.newStringOption("-w").nargs("+").build();
        parser.addOption(option);
        List<String> rest = splitAndParse("-w foo bar baz -v quux");
        assertThat(rest, equalTo(Arrays.asList("quux")));
        assertThat(option.getValue(), equalTo(Arrays.asList("foo", "bar", "baz")));
    }

    @Test
    public void testMaybeOne() {
        MultiOption<String> option = Options.newStringOption("-w").nargs("?").build();
        parser.addOption(option);
        List<String> rest = splitAndParse("-w foo bar baz -v quux");
        assertThat(rest, equalTo(Arrays.asList("bar", "baz", "quux")));
        assertThat(option.getValue(), equalTo(Arrays.asList("foo")));
    }

}

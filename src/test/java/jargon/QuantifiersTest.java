package jargon;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

/**
 * User: Mikhail Golubev
 * Date: 19.02.13
 * Time: 20:39
 */
public class QuantifiersTest {
    private OptionParser parser;
    private Option<String> option;
    private List<String> rest;

    @Before
    public void setUpParser() {
        parser = OptionParser.newInstance("Sample program").build();
        Option<Boolean> verbose = Options.newFlagOption("-v").build();
        parser.addOption(verbose);
    }

    private void addOptionAndParse(String args) {
        parser.addOption(option);
        rest = parser.parse(args.split("\\s+"));
    }

    @Test
    public void testNoArguments() {
        option = Options.newStringOption("-w").nargs(0).build();
        addOptionAndParse("-w foo bar baz -v quux");
        assertThat(rest, equalTo(Arrays.asList("foo", "bar", "baz", "quux")));
        assertThat(option.getValue(), is(nullValue()));
        assertThat(option.getAllValues().isEmpty(), is(true));
    }

    @Test
    public void testSingleArguments() {
        // default for StringOption and IntegerOption
        option = Options.newStringOption("-w").build();
        addOptionAndParse("-w foo bar baz -v quux");
        assertThat(rest, equalTo(Arrays.asList("bar", "baz", "quux")));
        assertThat(option.getValue(), equalTo("foo"));
        assertThat(option.getAllValues(), equalTo(Arrays.asList("foo")));
    }

    @Test
    public void testTwoArguments() {
        // default for StringOption and IntegerOption
        option = Options.newStringOption("-w").nargs(2).build();
        addOptionAndParse("-w foo bar baz -v quux");
        assertThat(rest, equalTo(Arrays.asList("baz", "quux")));
        assertThat(option.getValue(), equalTo("foo"));
        assertThat(option.getAllValues(), equalTo(Arrays.asList("foo", "bar")));
    }

    @Test
    public void testOneAndMore() {
        option = Options.newStringOption("-w").nargs("+").build();
        addOptionAndParse("-w foo bar baz -v quux");
        assertThat(rest, equalTo(Arrays.asList("quux")));
        assertThat(option.getValue(), equalTo("foo"));
        assertThat(option.getAllValues(), equalTo(Arrays.asList("foo", "bar", "baz")));
    }

    @Test
    public void testMaybeOne() {
        option = Options.newStringOption("-w").nargs("?").build();
        addOptionAndParse("-w foo bar baz -v quux");
        assertThat(rest, equalTo(Arrays.asList("bar", "baz", "quux")));
        assertThat(option.getValue(), equalTo("foo"));
        assertThat(option.getAllValues(), equalTo(Arrays.asList("foo")));
    }

}

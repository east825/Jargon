package jargon;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * User: Mikhail Golubev
 * Date: 2/28/13
 * Time: 1:52 PM
 */
public class ParsingTest {
    private OptionParser parser;
    private Option<Boolean> verboseMode;
    private Option<String> fileName;
    private Option<Integer> size;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUpParser() {
        parser = OptionParser.newInstance("Sample program").exitOnError(false).build();
        verboseMode = Options.newFlagOption("-v", "--verbose").build();
        parser.addOption(verboseMode);
        fileName = Options.newStringOption("-f", "--file").defaultValue("output.txt").build();
        parser.addOption(fileName);
        size = Options.newInteger("-s", "--size").build();
        parser.addOption(size);
    }

    private List<String> splitAndParse(String args) {
        return parser.parse(args.split(" "));
    }

    @Test
    public void separatedArguments() {
        List<String> rest = splitAndParse("-v -s 100 -f somefile foo bar");
        assertThat(rest, equalTo(Arrays.asList("foo", "bar")));
        assertThat(fileName.getValue(), equalTo("somefile"));
        assertThat(size.getValue(), equalTo(100));
        assertThat(verboseMode.wasGiven(), is(true));
    }

    @Test
    public void optionsWithArguments() {
        List<String> rest = splitAndParse("-s100 --file=somefile foo bar");
        assertThat(rest, equalTo(Arrays.asList("foo", "bar")));
        assertThat(fileName.getValue(), equalTo("somefile"));
        assertThat(size.getValue(), equalTo(100));
        assertThat(verboseMode.wasGiven(), is(false));
    }

    @Test
    public void shortOptionsConcatenated() {
        List<String> rest = splitAndParse("-vs100 --file somefile foo bar");
        assertThat(rest, equalTo(Arrays.asList("foo", "bar")));
        assertThat(fileName.getValue(), equalTo("somefile"));
        assertThat(size.getValue(), equalTo(100));
        assertThat(verboseMode.wasGiven(), is(true));
    }

    @Test
    public void onlyLastShortOptionRequiresValue1() {
        exception.expect(OptionParserException.class);
        exception.expectMessage("Illegal value v100 for option --size");
        splitAndParse("-sv100 -file somefile foo bar");
    }

    @Test
    public void onlyLastShortOptionRequiresValue2() {
        exception.expect(OptionParserException.class);
        exception.expectMessage("Illegal value f for option --size");
        splitAndParse("-sf 100 somefile foo bar");
    }

    @Test
    public void testOptionCountAndDefault() {
        List<String> rest = splitAndParse("-vvvs 100 -vv foo bar");
        assertThat(rest, equalTo(Arrays.asList("foo", "bar")));
        assertThat(verboseMode.getCount(), equalTo(5));
        assertThat(size.getValue(), equalTo(100));
        assertThat(fileName.getValue(), equalTo("output.txt"));
    }

}

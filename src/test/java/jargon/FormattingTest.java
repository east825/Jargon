package jargon;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * User: Mikhail Golubev
 * Date: 3/1/13
 * Time: 5:51 PM
 */
public class FormattingTest {
    private HelpFormatter formatter;
    private OptionParser parser;

    @Before
    public void setUpParserAndFormatter() {
        parser = OptionParser.newInstance("prog").printHelp(false).help("Sample program").build();
        formatter = new HelpFormatter(parser);
    }

    @Test
    public void testFlagFormat() {
        Flag flag = Options.newFlagOption("-v", "--verbose").build();
        parser.addOption(flag);
        Flag requiredFlag = Options.newFlagOption("-e", "--extended").required().build();
        parser.addOption(requiredFlag);

        assertThat(formatter.formatProgramUsage(), equalTo("prog: [-v] -e"));
    }

    @Test
    public void testOptionFormat() {
        Option<String> shortNameOnlyOption = Options.newStringOption("-f").build();
        parser.addOption(shortNameOnlyOption);
        Option<String> shortLongNameOption = Options.newStringOption("-r", "--revision").build();
        parser.addOption(shortLongNameOption);
        Option<String> requiredOption = Options.newStringOption("-p", "--prompt").required().build();
        parser.addOption(requiredOption);
        assertThat(formatter.formatProgramUsage(), equalTo("prog: [-f F] [-r REVISION] -p PROMPT"));
    }

    @Test
    public void testMultiOption() {
        MultiOption<String> maybeOption = Options.newStringOption("-a").nargs("?").build();
        parser.addOption(maybeOption);
        MultiOption<String> starOption = Options.newStringOption("-b").nargs("*").build();
        parser.addOption(starOption);
        MultiOption<String> plusOption = Options.newStringOption("-c").nargs("+").build();
        parser.addOption(plusOption);
        MultiOption<String> exactNargsOption = Options.newStringOption("-d").nargs(3).build();
        parser.addOption(exactNargsOption);
        MultiOption<String> floatingNargsOption = Options.newStringOption("-e").nargs(3, 5).build();
        parser.addOption(floatingNargsOption);
        assertThat(formatter.formatProgramUsage(), equalTo(
                "prog: [-a [A]] [-b [B [B ...]]] [-c C [C ...]] [-d D D D] [-e E E E [E [E]]]"
        ));
    }

    @Test
    public void testHelpMessage() {
        // taken from mercurial help
        Option<Integer> revision = Options.newIntegerOption("-r", "--rev").help("change made by revision").build();
        parser.addOption(revision);
        Option<String> withLongDescription = Options.newStringOption("-l", "--long").help(
                "goooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooogle\n" +
                "foo bar baz\t\n\nfoo bar baz"
        ).build();
        parser.addOption(withLongDescription);
        assertThat(formatter.formatProgramHelp(), equalTo(
                "prog: [-r REV] [-l LONG]\n" +
                "\n" +
                "--rev REV, -r REV        change made by revision\n" +
                "--long LONG, -l LONG     \n" +
                "                         goooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooogle \n" +
                "                         foo bar baz foo bar baz\n" +
                "Sample program"
        ));
    }
}

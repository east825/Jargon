package jargon;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * User: Mikhail Golubev
 * Date: 3/1/13
 * Time: 5:51 PM
 */
public class FormattingTest {
    @Test
    public void testFlagFormat() {
        Flag flag = Options.newFlagOption("-v", "--verbose").build();
        assertThat(flag.getFormat(), equalTo("[-v]"));

        Flag requiredFlag = Options.newFlagOption("-v", "--verbose").required().build();
        assertThat(requiredFlag.getFormat(), equalTo("-v"));
    }

    @Test
    public void testOptionFormat() {
        Option<String> shortNameOnlyOption = Options.newStringOption("-f").build();
        assertThat(shortNameOnlyOption.getFormat(), equalTo("[-f F]"));

        Option<String> shortLongNameOption = Options.newStringOption("-f", "--file").build();
        assertThat(shortLongNameOption.getFormat(), equalTo("[-f FILE]"));

        Option<String> requiredOption = Options.newStringOption("-f", "--file").required().build();
        assertThat(requiredOption.getFormat(), equalTo("-f FILE"));
    }

    @Test
    public void testMultiOption() {
        MultiOption<String> maybeOption = Options.newStringOption("-f").nargs("?").build();
        assertThat(maybeOption.getFormat(), equalTo("[-f [F]]"));

        MultiOption<String> starOption = Options.newStringOption("-f").nargs("*").build();
        assertThat(starOption.getFormat(), equalTo("[-f [F [F ...]]]"));

        MultiOption<String> plusOption = Options.newStringOption("-f").nargs("+").build();
        assertThat(plusOption.getFormat(), equalTo("[-f F [F ...]]"));

        MultiOption<String> exactNargsOption = Options.newStringOption("-f").nargs(3).build();
        assertThat(exactNargsOption.getFormat(), equalTo("[-f F F F]"));

        MultiOption<String> threeToFiveOption = Options.newStringOption("-f").nargs(3, 5).build();
        assertThat(threeToFiveOption.getFormat(), equalTo("[-f F F F [F [F]]]"));
    }
}

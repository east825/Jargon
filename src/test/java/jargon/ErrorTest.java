package jargon;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * User: Mikhail Golubev
 * Date: 2/23/13
 * Time: 2:51 AM
 */
public class ErrorTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();
    private OptionParser parser = OptionParser.newInstance("Sample program").build();
    private Option<Boolean> verbose = Options.newFlagOption("-v").build();

    @Test
    public void optionNotAddedToParser() {
        exception.expect(IllegalStateException.class);
        exception.expectMessage("Add option to parser");
        verbose.getValue();
    }

    @Test
    public void parserNotCalled() {
        exception.expect(IllegalStateException.class);
        exception.expectMessage("Call OptionParser.parse() method first");
        parser.addOption(verbose);
        verbose.getValue();
    }

    @Test
    public void conflictingOptionsNames() {
        exception.expect(IllegalArgumentException.class);
        parser.addOption(Options.newStringOption("-l", "--list").build());
        parser.addOption(Options.newStringOption("-l", "--last").build());
    }
}

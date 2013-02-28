package jargon.options;

/**
 * User: Mikhail Golubev
 * Date: 14.02.13
 * Time: 0:14
 */
public class Options {
    static public OptionBuilder<String> newStringOption(String... names) {
        return new StringOptionBuilder(names);
    }

    static public OptionBuilder<Integer> newIntergerOption(String... names) {
        return new IntegerOptionBuilder(names);
    }

    static public OptionBuilder<Boolean> newFlagOption(String... names) {
        return new FlagOptionBuilder(names);
    }

    static public <T> OptionBuilder<T> newOption(String... names) {
        return new OptionBuilder<>(names);
    }

}

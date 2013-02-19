package jargon.options;

/**
 * User: Mikhail Golubev
 * Date: 14.02.13
 * Time: 0:14
 */
public class Options {
    static public Builder<String> newStringOption(String... names) {
        return new StringOptionBuilder(names);
    }

    static public Builder<Integer> newIntergerOption(String... names) {
        return new IntegerOptionBuilder(names);
    }

    static public Builder<Boolean> newFlagOption(String... names) {
        return new FlagOptionBuilder(names);
    }

    static public <T> Builder<T> newOption(String... names) {
        return new Builder<>(names);
    }

}

package jargon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: Mikhail Golubev
 * Date: 14.02.13
 * Time: 0:11
 */

public final class OptionBuilder<T> {

    // Next fields are package private for accessing from Option class
    List<String> names;
    List<String> shortNames = new ArrayList<String>();
    List<String> longNames = new ArrayList<String>();
    String helpMessage;
    boolean isRequired = false;
    int minArgs = 1, maxArgs = 1;
    T defaultValue;
    Converter<? extends T> converter;

    OptionBuilder(String... names) {
        if (names.length == 0)
            throw new IllegalArgumentException("No option names given");

        for (String name : names) {
            if (name.matches(BaseOption.SHORT_OPTION_REGEX))
                shortNames.add(name);
            else if (name.matches(BaseOption.LONG_OPTION_REGEX))
                longNames.add(name);
            else
                throw new IllegalArgumentException("Invalid option name: " + name);
        }
        this.names = Arrays.asList(names);
    }

    public OptionBuilder<T> help(String helpMessage) {
        this.helpMessage = helpMessage;
        return this;
    }

    public MultiOptionBuilder<T> nargs(int minArgs, int maxArgs) {
        if (minArgs < 0 || maxArgs < 0 || minArgs > maxArgs)
            throw new IllegalArgumentException("Invalid options number quantifier: {" + minArgs + " ," + maxArgs + "}");
        this.minArgs = minArgs;
        this.maxArgs = maxArgs;
        return new MultiOptionBuilder<T>(this);
    }

    public MultiOptionBuilder<T> nargs(int n) {
        return nargs(n, n);
    }

    public MultiOptionBuilder<T> nargs(String wildcard) {
        if (wildcard.equals("+")) {
            return nargs(1, Integer.MAX_VALUE);
        } else if (wildcard.equals("*")) {
            return nargs(0, Integer.MAX_VALUE);
        } else if (wildcard.equals("?")) {
            return nargs(0, 1);
        }
        throw new IllegalArgumentException("Unknown nargs wildcard " + wildcard);
    }

    public OptionBuilder<T> defaultValue(T value) {
        defaultValue = value;
        return this;
    }

    public OptionBuilder<T> converter(Converter<T> c) {
        converter = c;
        return this;
    }

    public OptionBuilder<T> required() {
        this.isRequired = true;
        return this;
    }

    public Option<T> build() {
        if (converter == null)
            throw new IllegalStateException("Converter is required");
        return new Option<T>(this);
    }
}

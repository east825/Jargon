package jargon.options;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: Mikhail Golubev
 * Date: 14.02.13
 * Time: 0:11
 */

public class Builder<T> {

    // Next fields are package private for accessing from Option class
    List<String> names;
    List<String> shortNames = new ArrayList<>();
    List<String> longNames = new ArrayList<>();
    String helpMessage;
    boolean isRequired = false;
    int minArgs = 0, maxArgs = 0;
    T defaultValue;
    Converter<? extends T> converter;

    Builder(String... names) {
        if (names.length == 0)
            throw new IllegalArgumentException("No option names given");

        for (String name : names) {
            if (name.matches(Option.SHORT_OPTION_REGEX))
                shortNames.add(name);
            else if (name.matches(Option.LONG_OPTION_REGEX))
                longNames.add(name);
            else
                throw new IllegalArgumentException("Invalid option name: " + name);
        }
        this.names = Arrays.asList(names);
    }

    public Builder<T> help(String helpMessage) {
        this.helpMessage = helpMessage;
        return this;
    }

    public Builder<T> nargs(int minArgs, int maxArgs) {
        if (minArgs < 0 || maxArgs < 0 || minArgs > maxArgs)
            throw new IllegalArgumentException("Invalid options number quantifier: {" + minArgs + " ," + maxArgs + "}");
        this.minArgs = minArgs;
        this.maxArgs = maxArgs;
        return this;
    }

    public Builder<T> nargs(int n) {
        return nargs(n, n);
    }

    public Builder<T> nargs(String wildcard) {
        if (wildcard.equals("+")) {
            nargs(1, Integer.MAX_VALUE);
        } else if (wildcard.equals("?")) {
            nargs(0, 1);
        }
        return this;
    }

    public Builder<T> defaultValue(T value) {
        defaultValue = value;
        return this;
    }

    public Builder<T> converter(Converter<T> c) {
        converter = c;
        return this;
    }

    public Builder<T> required(boolean isRequired) {
        this.isRequired = isRequired;
        return this;
    }

    protected Converter<? extends T> getConverter() {
        return converter;
    }

    public Option<T> build() {
        converter = getConverter();
        if (converter == null)
            throw new IllegalStateException("Converter is required");
        return new Option<>(this);
    }
}

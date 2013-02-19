package jargon.options;


import jargon.ArgParserException;

import java.util.LinkedList;
import java.util.List;

/**
 * User: Mikhail Golubev
 * Date: 13.02.13
 * Time: 23:06
 */
public class Option<T> {
    public static final String SHORT_OPTION_REGEX = "^-\\p{Alnum}$";
    public static final String LONG_OPTION_REGEX = "^--\\p{Alnum}(\\p{Alnum}|-)+$";


    // Option settings
    private List<String> shortNames, longNames;
    private int minArgs, maxArgs;
    private String helpMessage;
    private Converter<? extends T> converter;
    private boolean isRequired;
    private T defaultValue;
    private int count;

    // Parsed values
    private List<T> values = new LinkedList<>();


    // package private constructor
    Option(Builder<T> builder) {
        shortNames = builder.shortNames;
        longNames = builder.longNames;
        minArgs = builder.minArgs;
        maxArgs = builder.maxArgs;
        converter = builder.converter;
        helpMessage = builder.helpMessage;
        isRequired = builder.isRequired;
        defaultValue = builder.defaultValue;
    }

    public boolean matchName(String name) {
        return shortNames.contains(name) || longNames.contains(name);
    }

    public boolean isRequired() {
        return isRequired;
    }

    public int getCount() {
        return count;
    }

    public String getFormat() {
        StringBuilder b = new StringBuilder(getName());
        int maxPrintedArgs = 3;
        b.append(" ");
        for (int i = 1; i <= Math.min(minArgs, maxPrintedArgs); i++) {
            b.append("arg").append(i).append(' ');
        }
        if (minArgs > maxPrintedArgs)
            b.append(" ... arg").append(minArgs).append(" ");

        if (maxArgs - minArgs > 0) {
            b.append("[").append(maxArgs - minArgs > 1 ? " ... " : "");
            b.append("arg").append(maxArgs).append("]");
        }
        else if (maxArgs - minArgs > 1)
            b.append("[ ... arg").append(maxArgs).append("]");

        if (!isRequired) {
            b.insert(0, "[").append("]");
        }
        return b.toString();
    }

    public String buildHelpMessage() {
        StringBuilder b = new StringBuilder(getName());
        if (shortNames.size() + longNames.size() > 1) {
            b.append(" (also ");
            for (String name: shortNames) {
                if (!name.equals(getName()))
                    b.append(name).append(" ");
            }
            for (String name: longNames) {
                if (!name.equals(getName()))
                    b.append(" ").append(name);
            }
            b.insert(b.length() - 1, ")");
        }
        if (helpMessage != null && !helpMessage.isEmpty())
            b.append("\t").append(helpMessage);
        return b.toString();
    }

    public String getName() {
        if (!longNames.isEmpty())
            return longNames.get(0);
        return shortNames.get(0);
    }

    public int parse(List<String> args, int index) {
        count++;
        for (int i = 0; i < maxArgs; i++, index++) {
            if (index < args.size()) {
                String a = args.get(index);
                try {
                    values.add(converter.convert(a));
                } catch (IllegalArgumentException e) {
                    throw new ArgParserException("Invalid argument " + a + " for option " + getName());
                }
            } else if (i < minArgs) {
                throw new ArgParserException(
                        String.format("Not enough arguments for option %s (%d required, %d found)", getName(), minArgs, i));
            } else {
                break;
            }
        }
        return index;
    }

    public T getSingleValue() {
        if (values.isEmpty())
            values.add(defaultValue);
        return values.get(0);
    }

    public List<T> getAllValues() {
        if (values.isEmpty())
            values.add(defaultValue);
        return values;
    }

    public boolean wasGiven() {
        return count != 0;
    }
}

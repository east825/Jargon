package jargon;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Mikhail Golubev
 * Date: 13.02.13
 * Time: 23:06
 */
public abstract class BaseOption<T> {
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

    // Registered in addOption method
    private OptionParser parser = null;

    public BaseOption(OptionBuilder<T> builder) {
        shortNames = builder.shortNames;
        longNames = builder.longNames;
        minArgs = builder.minArgs;
        maxArgs = builder.maxArgs;
        converter = builder.converter;
        helpMessage = builder.helpMessage;
        isRequired = builder.isRequired;
        defaultValue = builder.defaultValue;
    }

    public List<String> getShortNames() {
        return Collections.unmodifiableList(shortNames);
    }

    public List<String> getLongNames() {
        return Collections.unmodifiableList(longNames);
    }

    public List<String> getNames() {
        ArrayList<String> allNames = new ArrayList<String>(longNames);
        allNames.addAll(shortNames);
        return allNames;
    }

    public int getMinArgs() {
        return minArgs;
    }

    public int getMaxArgs() {
        return maxArgs;
    }

    public String getHelpMessage() {
        return helpMessage;
    }

    public int getCount() {
        return count;
    }

    void setParser(OptionParser parser) {
        this.parser = parser;
    }

    private String getFirstShortName() {
        return !shortNames.isEmpty() ? shortNames.get(0) : longNames.get(0);
    }

    private String getFirstLongName() {
        return !longNames.isEmpty() ? longNames.get(0) : shortNames.get(0);
    }

    public String getName() {
        return getFirstLongName();
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public boolean isRequired() {
        return isRequired;
    }

    protected void checkLegalState() {
        if (parser == null)
            throw new IllegalStateException("Add option to parser using OptionParser.addOption() method");
        if (!parser.isParsed())
            throw new IllegalStateException("Call OptionParser.parse() method first");
    }

    public int parse(List<String> args, int index) {
        count++;
        if (maxArgs == 0)
            return index;
        String arg = args.get(index);
        if (maxArgs == 1) {
            String remainder = "";
            if (parser.isLongOption(arg) && arg.contains("=")) {
                remainder = arg.split("=", 2)[1];
            } else if (parser.isShortOption(arg)) {
                for (String name : shortNames) {
                    int from = arg.indexOf(name.charAt(1));
                    if (from != -1) {
                        remainder = arg.substring(from + 1);
                        break;
                    }
                }
            }
            if (!remainder.isEmpty()) {
                proceedValue(remainder);
                return index + 1;
            }
        }
        index++;
        for (int i = 0; i < maxArgs; i++, index++) {
            if (index < args.size() && !parser.isOption(args.get(index))) {
                proceedValue(args.get(index));
            } else if (i < minArgs) {
                throw new OptionParserException(
                        String.format("Not enough arguments for option %s (%d required, %d found)", getName(), minArgs, i));
            } else {
                break;
            }
        }
        return index;
    }

    private void proceedValue(String tail) {
        try {
            storeValue(converter.convert(tail));
        } catch (Exception e) {
            throw new OptionParserException("Illegal value " + tail + " for option " + getName(), e);
        }
    }

    protected abstract void storeValue(T value);
    public abstract Object getValue();

    @Override
    public String toString() {
        return "<Option: " + getNames() + " >";
    }
}

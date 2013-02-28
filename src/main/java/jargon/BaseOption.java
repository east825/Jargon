package jargon;


import java.util.ArrayList;
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

    void setParser(OptionParser parser) {
        this.parser = parser;
    }

    String getFormat() {
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
        } else if (maxArgs - minArgs > 1)
            b.append("[ ... arg").append(maxArgs).append("]");

        if (!isRequired) {
            b.insert(0, "[").append("]");
        }
        return b.toString();
    }

    String buildHelpMessage() {
        StringBuilder b = new StringBuilder(getName());
        if (shortNames.size() + longNames.size() > 1) {
            b.append(" (also ");
            for (String name : shortNames) {
                if (!name.equals(getName()))
                    b.append(name).append(" ");
            }
            for (String name : longNames) {
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

    public List<String> getNames() {
        ArrayList<String> allNames = new ArrayList<>(longNames);
        allNames.addAll(shortNames);
        return allNames;
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

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "<Option: " + getNames() + " >";
    }
}

package jargon;

import java.util.*;

/**
 * User: Mikhail Golubev
 * Date: 14.02.13
 * Time: 0:34
 */
public class OptionParser {
    public static final String STOP_ARG_SYMBOL = "--";

    private String helpMessage;
    private String programName;
    private boolean exitOnError;
    private List<Option<?>> options = new LinkedList<Option<?>>();
    private Map<String, Option<?>> optionRegistry = new HashMap<String, Option<?>>();
    // set in parse method
    private boolean parsed = false;

    public static class ParserBuilder {
        private String programName;
        private String helpMessage;
        private boolean exitOnError = true;
        private boolean printHelp = true;

        ParserBuilder(String programName) {
            this.programName = programName;
        }
        public ParserBuilder help(String helpMessage) {
            this.helpMessage = helpMessage;
            return this;
        }
        public ParserBuilder exitOnError(boolean exit) {
            exitOnError = exit;
            return this;
        }
        public ParserBuilder printHelp(boolean print) {
            printHelp = print;
            return this;
        }

        public OptionParser build() {
            return new OptionParser(this);
        }
    }

    static ParserBuilder newInstance(String programName) {
        return new ParserBuilder(programName);
    }

    private OptionParser(ParserBuilder builder) {
        programName = builder.programName;
        helpMessage = builder.helpMessage;
        exitOnError = builder.exitOnError;
        if (builder.printHelp) {
            HelpOption helpOption = new HelpOption(Options.newFlagOption("-h", "--help").help(
                    "Print this help message and exit"
            ));
            addOption(helpOption);
        }
    }

    public void addOption(Option<?> option) {
        for (String name: option.getNames()) {
            if (optionRegistry.containsKey(name)) {
                throw new IllegalArgumentException("Conflicting options: " + option + " and " + optionRegistry.get(name));
            }
            optionRegistry.put(name, option);
        }
        options.add(option);
        option.setParser(this);
    }

    private Option<?> findOption(String arg) {
        Option<?> option;
        if (isLongOption(arg))
            option = optionRegistry.get(arg.split("=")[0]);
        else if (isShortOption(arg))
            option = optionRegistry.get("-" + arg.charAt(1));
        else
            throw new IllegalArgumentException("Malformed option " + arg);
        if (option == null)
            throw new OptionParserException("Unknown option " + arg);
        return option;
    }

    public boolean isParsed() {
        return parsed;
    }

    public boolean isOption(String arg) {
        return arg.startsWith("-");
    }

    public boolean isShortOption(String arg) {
        return arg.startsWith("-") && !arg.startsWith("--");
    }

    public boolean isLongOption(String arg) {
        return arg.startsWith("--");
    }

    public String buildHelpMessage() {
        StringBuilder b = new StringBuilder(programName);
        for (Option<?> o: options) {
            b.append(" ");
            b.append(o.getFormat());
        }
        b.append("\n\n");
        for (Option<?> o: options) {
            b.append(o.buildHelpMessage());
            b.append("\n");
        }
        if (helpMessage != null && !helpMessage.isEmpty())
            b.append("\n").append(helpMessage);
        return b.toString();
    }

    public List<String> parse(String... args) throws OptionParserException {
        parsed = true;
        ArrayList<String> positionalArgs = new ArrayList<String>();
        List<String> argsList = Arrays.asList(args);
        try {
            for (int i = 0; i < argsList.size(); /* empty */) {
                String arg = argsList.get(i);
                if (arg.equals(STOP_ARG_SYMBOL)) {
                    positionalArgs.addAll(argsList.subList(i + 1, argsList.size()));
                    break;
                }
                if (isLongOption(arg)) {
                    i = findOption(arg).parse(argsList, i);
                } else if (isShortOption(arg)) {
                    int nextArgPos = i;
                    for (char c: arg.substring(1).toCharArray()) {
                        nextArgPos = findOption("-" + c).parse(argsList, i);
                        if (nextArgPos != i)
                            break;
                    }
                    // last option shifted index of args list
                    // or we quit because character sequence exhausted
                    i = nextArgPos == i? i + 1: nextArgPos;
                } else {
                    positionalArgs.add(arg);
                    i++;
                }
            }
            for (Option<?> opt: options) {
                if (opt.isRequired() && !opt.wasGiven())
                    throw new OptionParserException("Option " + opt.getName() + " is required, but was not specified");
            }
        } catch (OptionParserException e) {
            if (exitOnError) {
                System.err.println(e.getMessage());
                System.exit(1);
            }
            throw e;
        }
        return positionalArgs;
    }

    private class HelpOption extends Option<Boolean> {
        private HelpOption(OptionBuilder<Boolean> builder) {
            super(builder);
        }

        @Override
        public int parse(List<String> args, int index) {
            String helpMessage = OptionParser.this.buildHelpMessage();
            System.err.println(helpMessage);
            System.exit(2);
            return -1;
        }
    }
}

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
    private List<BaseOption<?>> options = new LinkedList<>();
    private Map<String, BaseOption<?>> optionRegistry = new HashMap<>();
    // set in parse method
    private boolean parsed = false;
    private Flag helpOption;
    private HelpFormatter formatter = new HelpFormatter(this);

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
            helpOption = Options.newFlagOption("-h", "--help").help("Print this help message and exit").build();
            addOption(helpOption);
        }
    }

    public void addOption(BaseOption<?> option) {
        for (String name: option.getNames()) {
            if (optionRegistry.containsKey(name)) {
                throw new IllegalArgumentException("Conflicting options: " + option + " and " + optionRegistry.get(name));
            }
            optionRegistry.put(name, option);
        }
        options.add(option);
        option.setParser(this);
    }

    private BaseOption<?> findOption(String arg) {
        BaseOption<?> option;
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

    public List<BaseOption<?>> getOptions() {
        return Collections.unmodifiableList(options);
    }

    public String getProgramName() {
        return programName;
    }

    public String getHelpMessage() {
        return helpMessage;
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

    private void printHelpAndExit() {
        System.err.println(formatter.formatProgramHelp());
        System.exit(2);
    }

    public List<String> parse(String... args) {
        parsed = true;
        ArrayList<String> positionalArgs = new ArrayList<String>();
        List<String> argsList = Arrays.asList(args);
        BaseOption<?> option;
        try {
            for (int i = 0; i < argsList.size(); /* empty */) {
                String arg = argsList.get(i);
                if (arg.equals(STOP_ARG_SYMBOL)) {
                    positionalArgs.addAll(argsList.subList(i + 1, argsList.size()));
                    break;
                }
                if (isLongOption(arg)) {
                    option = findOption(arg);
                    if (option == helpOption)
                        printHelpAndExit();
                    i = option.parse(argsList, i);
                } else if (isShortOption(arg)) {
                    int nextArgPos = i;
                    for (char c: arg.substring(1).toCharArray()) {
                        option = findOption("-" + c);
                        if (option == helpOption)
                            printHelpAndExit();
                        nextArgPos = option.parse(argsList, i);
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
            for (BaseOption<?> opt: options) {
                if (opt.isRequired() && opt.getCount() == 0)
                    throw new OptionParserException("Option " + opt.getName() + " is required, but was not specified");
            }
        } catch (OptionParserException e) {
            if (exitOnError) {
                System.err.println(e.getMessage());
                System.err.println(formatter.formatProgramUsage());
                System.exit(1);
            }
            throw e;
        }
        return positionalArgs;
    }
}

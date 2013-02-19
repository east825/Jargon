package jargon;

import jargon.options.Option;

import java.util.*;

/**
 * User: Mikhail Golubev
 * Date: 14.02.13
 * Time: 0:34
 */
public class OptionParser {
    public static final String STOP_ARG_SYMBOL = "--";
    public static final String LONG_OPTION_PREFIX = "--";
    public static final String SHORT_OPTION_PREFIX = "-";

    private String helpMessage;
    private String programName;
    private boolean exitOnError;
    private boolean printHelp;
    private List<Option<?>> options = new LinkedList<>();

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

    static ParserBuilder newOptionParser(String programName) {
        return new ParserBuilder(programName);
    }

    private OptionParser(ParserBuilder builder) {
        programName = builder.programName;
        helpMessage = builder.helpMessage;
        exitOnError = builder.exitOnError;
        printHelp = builder.printHelp;
    }

    public void addOption(Option<?> option) {
        options.add(option);
    }

    public void printHelpAndExit() {
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
        System.out.println(b.toString());
        System.exit(0);
    }

    public List<String> parse(String... args) throws ArgParserException {
        ArrayList<String> positionalArgs = new ArrayList<>();
        List<String> argsList = Arrays.asList(args);
        try {
            for (int i = 0; i < argsList.size(); /* empty */) {
                String arg = argsList.get(i);
                if (arg.equals(STOP_ARG_SYMBOL)) {
                    positionalArgs.addAll(argsList.subList(i + 1, argsList.size()));
                    break;
                }
                boolean matched = false;
                if (arg.startsWith(LONG_OPTION_PREFIX)) {
                    if (printHelp && args.equals("--help")) {
                        printHelpAndExit();
                    }
                    for (Option<?> opt : options) {
                        if (opt.matchName(arg)) {
                            matched = true;
                            i = opt.parse(argsList, i + 1);
                            break;
                        }
                    }
                } else if (arg.startsWith(SHORT_OPTION_PREFIX)) {
                    i++;
                    for (char c : arg.substring(1).toCharArray()) {
                        if (c == 'h') {
                            printHelpAndExit();
                        }
                        for (Option<?> opt : options) {
                            if (opt.matchName("-" + c)) {
                                matched = true;
                                i = opt.parse(argsList, i);
                                break;
                            }
                        }
                    }
                } else {
                    positionalArgs.add(arg);
                    i++;
                    continue;
                }
                if (!matched) {
                    throw new ArgParserException("Unknown option: " + arg + "");
                }
            }
            for (Option<?> opt: options) {
                if (opt.isRequired() && !opt.wasGiven())
                    throw new ArgParserException("Option " + opt.getName() + " is required, but was not specified");
            }
        } catch (ArgParserException e) {
            if (exitOnError) {
                System.err.println(e.getMessage());
                System.exit(1);
            }
            throw e;
        }
        return positionalArgs;
    }
}

package jargon;

/**
 * User: Mikhail Golubev
 * Date: 3/3/13
 * Time: 9:53 PM
 */
public class HelpFormatter {
    private static final int OPTION_FORMAT_MARGIN = 25;
    private static final int OPTION_HELP_MARGING = 80;
    private static final String INDENT;

    static {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < OPTION_FORMAT_MARGIN; i++) {
            sb.append(" ");
        }
        INDENT = sb.toString();
    }

    private final OptionParser parser;

    public HelpFormatter(OptionParser parser) {
        this.parser = parser;
    }

    public String formatProgramHelp() {
        StringBuilder helpMessage = new StringBuilder();
        helpMessage.append(formatProgramUsage()).append("\n\n");
        for (BaseOption<?> o : parser.getOptions()) {
            helpMessage.append(formatOptionHelp(o)).append("\n");
        }
        if (parser.getHelpMessage() != null)
            helpMessage.append(parser.getHelpMessage());
        return helpMessage.toString();
    }

    public String formatProgramUsage() {
        StringBuilder usage = new StringBuilder(parser.getProgramName() + ": ");
        for (BaseOption<?> o : parser.getOptions()) {
            if (!o.isRequired())
                usage.append("[");
            usage.append(optionFirstShortName(o));
            String callFormat = optionCallFormat(o);
            if (!callFormat.isEmpty())
                usage.append(" ").append(callFormat);
            if (!o.isRequired())
                usage.append("]");
            usage.append(" ");
        }
        return usage.toString().trim();
    }

    public String formatOptionHelp(BaseOption<?> o) {
        StringBuilder helpMessage = new StringBuilder();
        StringBuilder line = new StringBuilder();
        String callFormat = optionCallFormat(o);
        for (String name : o.getNames()) {
            line.append(name).append(" ").append(callFormat).append(", ");
        }
        // strip trailing ", "
        line.delete(line.length() - 2, line.length());
        String[] words = o.getHelpMessage().split("\\s+");
        int wordIndex = 0;
        if (line.length() < OPTION_FORMAT_MARGIN) {
            // padding with whitespaces till margin
            int tillMargin = OPTION_FORMAT_MARGIN - line.length();
            for (int i = 0; i < tillMargin; i++) {
                line.append(" ");
            }
            while (wordIndex < words.length && line.length() + words[wordIndex].length() < OPTION_HELP_MARGING) {
                line.append(words[wordIndex++]).append(" ");
            }
        }
        helpMessage.append(line.toString()).append("\n");
        while (wordIndex < words.length) {
            line = new StringBuilder(INDENT);
            do {
                line.append(words[wordIndex++]).append(" ");
            } while (wordIndex < words.length && line.length() + words[wordIndex].length() < OPTION_HELP_MARGING);
            helpMessage.append(line).append("\n");
        }
        return helpMessage.toString().trim();
    }

    private String optionFirstShortName(BaseOption<?> o) {
        return !o.getShortNames().isEmpty() ? o.getShortNames().get(0) : o.getLongNames().get(0);
    }

    private String optionFirstLongName(BaseOption<?> o) {
        return !o.getLongNames().isEmpty() ? o.getLongNames().get(0) : o.getShortNames().get(0);
    }

    private String optionMetaVar(BaseOption<?> o) {
        String metaName = optionFirstLongName(o);
        metaName = metaName.replaceFirst("-+", "");
        return metaName.toUpperCase();
    }

    private String optionCallFormat(BaseOption<?> o) {
        int minArgs = o.getMinArgs(), maxArgs = o.getMaxArgs();
        String metaVar = optionMetaVar(o);
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < minArgs; i++) {
            b.append(" ").append(metaVar);
        }
        if (maxArgs == Integer.MAX_VALUE) {
            String placeholder = " [" + metaVar + " ...]";
            // for nargs='*': [N [N ...]]
            if (minArgs == 0)
                b.append(" [").append(metaVar).append(placeholder).append("]");
            else
                b.append(placeholder);
        } else {
            // for nargs=(2, 5): N N [N [N [N]]]
            for (int i = 0; i < maxArgs - minArgs; i++) {
                b.append(" [").append(metaVar);
            }
            for (int i = 0; i < maxArgs - minArgs; i++) {
                b.append("]");
            }
        }
        return b.toString().trim();
    }

}

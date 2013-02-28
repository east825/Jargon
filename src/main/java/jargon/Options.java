package jargon;

/**
 * User: Mikhail Golubev
 * Date: 14.02.13
 * Time: 0:14
 */
public class Options {

    private static final Converter<String> STRING_CONVERTER = new Converter<String>() {
        @Override
        public String convert(String value) {
            return value;
        }
    };
    private static final Converter<Integer> INTEGER_CONVERTER = new Converter<Integer>() {
        @Override
        public Integer convert(String value) {
            return Integer.parseInt(value);
        }
    };

    static public OptionBuilder<String> newStringOption(String... names) {
        return new OptionBuilder<String>(names).converter(STRING_CONVERTER);
    }

    static public OptionBuilder<Integer> newInteger(String... names) {
        return new OptionBuilder<Integer>(names).converter(INTEGER_CONVERTER);
    }

    static public OptionBuilder<Boolean> newFlagOption(String... names) {
        return new FlagOptionBuilder(names);
    }

    static public <T> OptionBuilder<T> newOptionFor(Converter<T> c, String... names) {
        return new OptionBuilder<T>(names).converter(c);
    }

}

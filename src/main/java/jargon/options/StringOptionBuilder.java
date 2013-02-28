package jargon.options;

/**
 * User: Mikhail Golubev
 * Date: 19.02.13
 * Time: 23:31
 */
public final class StringOptionBuilder extends OptionBuilder<String> {

    private static final Converter<String> STRING_CONVERTER = new Converter<String>() {
        @Override
        public String convert(String value) {
            return value;
        }
    };

    StringOptionBuilder(String... names) {
        super(names);
        this.converter = STRING_CONVERTER;
    }

    @Override
    protected Converter<? extends String> getConverter() {
        return STRING_CONVERTER;
    }

    @Override
    public OptionBuilder<String> converter(Converter<String> c) {
        throw new UnsupportedOperationException("StringOptionBuilder has builtin converter");
    }
}

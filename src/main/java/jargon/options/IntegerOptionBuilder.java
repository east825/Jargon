package jargon.options;

/**
 * User: Mikhail Golubev
 * Date: 19.02.13
 * Time: 23:39
 */
public final class IntegerOptionBuilder extends Builder<Integer> {

    private static final Converter<Integer> INTEGER_CONVERTER = new Converter<Integer>() {
        @Override
        public Integer convert(String value) {
            return Integer.parseInt(value);
        }
    };

    public IntegerOptionBuilder(String... names) {
        super(names);
        this.minArgs = this.maxArgs = 1;
        this.converter = INTEGER_CONVERTER;
    }

    @Override
    protected Converter<? extends Integer> getConverter() {
        return INTEGER_CONVERTER;
    }

    @Override
    public Builder<Integer> converter(Converter<Integer> c) {
        throw new UnsupportedOperationException("StringOptionBuilder has builtin converter");
    }
}

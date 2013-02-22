package jargon.options;

/**
 * User: Mikhail Golubev
 * Date: 20.02.13
 * Time: 2:10
 */
public class FlagOptionBuilder extends Builder<Boolean> {
    private static final Converter<Boolean> STUB_CONVERTER = new Converter<Boolean>() {
        @Override
        public Boolean convert(String value) {
            throw new AssertionError("Stub conveter was called on value " + value);
        }
    };

    public FlagOptionBuilder(String... names) {
        super(names);
        this.converter = STUB_CONVERTER;
    }

    @Override
    public Builder<Boolean> nargs(int minArgs, int maxArgs) {
        throw new UnsupportedOperationException("FlagOption must take no arguments");
    }

    @Override
    public Builder<Boolean> converter(Converter<Boolean> c) {
        throw new UnsupportedOperationException("Flag option must take no arguments");
    }
}

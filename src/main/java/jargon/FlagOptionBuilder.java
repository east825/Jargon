package jargon;

/**
 * User: Mikhail Golubev
 * Date: 20.02.13
 * Time: 2:10
 */
public class FlagOptionBuilder extends OptionBuilder<Boolean> {
    private static final Converter<Boolean> STUB_CONVERTER = new Converter<Boolean>() {
        @Override
        public Boolean convert(String value) {
            throw new AssertionError("Stub conveter was called on value " + value);
        }
    };

    public FlagOptionBuilder(String... names) {
        super(names);
        this.converter = STUB_CONVERTER;
        this.maxArgs = this.minArgs = 0;
    }

    @Override
    public OptionBuilder<Boolean> nargs(int minArgs, int maxArgs) {
        throw new UnsupportedOperationException("FlagOption takes no arguments");
    }

    @Override
    public OptionBuilder<Boolean> converter(Converter<Boolean> c) {
        throw new UnsupportedOperationException("Flag option takes no arguments");
    }
}

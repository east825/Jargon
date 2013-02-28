package jargon;

/**
 * User: Mikhail Golubev
 * Date: 20.02.13
 * Time: 2:10
 */
public final class FlagOptionBuilder {
    private static final Converter<Boolean> STUB_CONVERTER = new Converter<Boolean>() {
        @Override
        public Boolean convert(String value) {
            throw new AssertionError("Stub converter was called on value " + value);
        }
    };
    private final OptionBuilder<Boolean> builder;

    public FlagOptionBuilder(String... names) {
        builder = new OptionBuilder<>(names);
        builder.converter = STUB_CONVERTER;
        builder.maxArgs = builder.minArgs = 0;
    }

    public FlagOptionBuilder help(String helpMessage) {
        builder.help(helpMessage);
        return this;
    }

    public FlagOptionBuilder defaultValue(Boolean value) {
        builder.defaultValue(value);
        return this;
    }

    public FlagOptionBuilder required() {
        builder.required();
        return this;
    }

    public Flag build() {
        return new Flag(builder);
    }
}

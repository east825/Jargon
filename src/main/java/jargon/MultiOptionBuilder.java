package jargon;

/**
 * User: Mikhail Golubev
 * Date: 3/1/13
 * Time: 12:01 AM
 */
public final class MultiOptionBuilder<T> {
    private OptionBuilder<T> builder;

    public MultiOptionBuilder(OptionBuilder<T> b) {
        this.builder = b;
    }

    public MultiOptionBuilder<T> help(String helpMessage) {
        builder.help(helpMessage);
        return this;
    }

    public MultiOptionBuilder<T> defaultValue(T value) {
        builder.defaultValue(value);
        return this;
    }

    public MultiOptionBuilder<T> converter(Converter<T> c) {
        builder.converter(c);
        return this;
    }

    public MultiOptionBuilder<T> required() {
        builder.required();
        return this;
    }

    public MultiOption<T> build() {
        return new MultiOption<T>(builder);
    }
}

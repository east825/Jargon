package jargon;

/**
 * User: Mikhail Golubev
 * Date: 2/28/13
 * Time: 11:49 PM
 */
public class Option<T> extends BaseOption<T> {

    private T value;

    public Option(OptionBuilder<T> builder) {
        super(builder);
    }

    @Override
    protected void storeValue(T value) {
        this.value = value;
    }

    @Override
    public T getValue() {
        checkLegalState();
        return getCount() > 0 ? value : getDefaultValue();
    }
}

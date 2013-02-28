package jargon;

/**
 * User: Mikhail Golubev
 * Date: 2/28/13
 * Time: 11:44 PM
 */
public class Flag extends BaseOption<Boolean> {
    public Flag(OptionBuilder<Boolean> builder) {
        super(builder);
    }

    @Override
    protected void storeValue(Boolean value) {
        throw new AssertionError("Flag option doesn't require any arguments");
    }

    public boolean isSet() {
        return getCount() > 0;
    }

    @Override
    public Boolean getValue() {
        checkLegalState();
        return isSet()? !getDefaultValue() : getDefaultValue();
    }
}

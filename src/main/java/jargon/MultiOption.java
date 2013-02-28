package jargon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: Mikhail Golubev
 * Date: 2/28/13
 * Time: 11:54 PM
 */
public class MultiOption<T> extends BaseOption<T> {
    private List<T> values = new ArrayList<>();

    public MultiOption(OptionBuilder<T> builder) {
        super(builder);
    }

    @Override
    protected void storeValue(T value) {
        values.add(value);
    }

    @Override
    public List<T> getValue() {
        checkLegalState();
        return getCount() > 0 ? values : Arrays.asList(getDefaultValue());
    }
}

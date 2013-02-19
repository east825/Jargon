package jargon.options;

import java.util.Arrays;
import java.util.List;

/**
 * User: Mikhail Golubev
 * Date: 20.02.13
 * Time: 2:10
 */
public class FlagOptionBuilder extends Builder<Boolean> {

    private static class FlagOption extends Option<Boolean> {
        FlagOption(Builder<Boolean> builder) {
            super(builder);
        }

        @Override
        public Boolean getSingleValue() {
            return wasGiven();
        }

        @Override
        public List<Boolean> getAllValues() {
            return Arrays.asList(getSingleValue());
        }
    }

    public FlagOptionBuilder(String... names) {
        super(names);
        this.minArgs = 0;
        this.maxArgs = 0;
    }

    @Override
    public Builder<Boolean> nargs(int minArgs, int maxArgs) {
        throw new UnsupportedOperationException("FlagOption takes 0 arguments");
    }

    @Override
    public Builder<Boolean> converter(Converter<Boolean> c) {
        throw new UnsupportedOperationException("Flag option takes 0 arguments");
    }

    @Override
    public Option<Boolean> build() {
        return new FlagOption(this);
    }
}

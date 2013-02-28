package jargon;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * User: Mikhail Golubev
 * Date: 2/22/13
 * Time: 2:57 PM
 */
public class CustomConverterTest {
    private static final SimpleDateFormat ISO8601_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static enum Ingredient { EGGS, HAM, SAUSAGES, BREAD, SPAM }

    @Test
    public void dateOption() {
        Converter<Date> dateConverter = new Converter<Date>() {
            @Override
            public Date convert(String value) {
                try {
                    return ISO8601_DATE_FORMAT.parse(value);
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        };
        // Type parameter of factory method can't be inferred, so it's explicit
        Option<Date> dateOption = Options.newOption(dateConverter, "-d", "--date").build();
        OptionParser parser = OptionParser.newInstance("Sample program").build();
        parser.addOption(dateOption);
        parser.parse("-d 1970-01-01".split(" "));
        // Note that month numbers start from zero
        Date expectedDate = new GregorianCalendar(1970, 0, 1).getTime();
        assertThat(dateOption.getValue(), equalTo(expectedDate));
    }

    @Test
    public void enumerationOption () {
        Converter<Ingredient> enumConverter = new Converter<Ingredient>() {
            @Override
            public Ingredient convert(String value) {
                return Ingredient.valueOf(value.toUpperCase());
            }
        };
        MultiOption<Ingredient> ingredients = Options.newOption(enumConverter, "-I").nargs("+").build();
        OptionParser parser = OptionParser.newInstance("Sample program").build();
        parser.addOption(ingredients);
        parser.parse("-I Spam Spam sausages".split(" "));
        assertThat(ingredients.getValue(), equalTo(Arrays.asList(
            Ingredient.SPAM, Ingredient.SPAM, Ingredient.SAUSAGES
        )));
    }
}

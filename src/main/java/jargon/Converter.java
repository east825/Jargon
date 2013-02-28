package jargon;

/**
 * User: Mikhail Golubev
 * Date: 19.02.13
 * Time: 21:02
 */
public interface Converter<T> {
    public T convert(String value);
}

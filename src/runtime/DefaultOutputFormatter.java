package runtime;

// Default formatter: prints whole numbers without decimal point. 
// 10.0 → "10",  3.14 → "3.14",  "hello" → "hello" 
// SRP: this class exists only to format. Nothing else. 
public final class DefaultOutputFormatter implements OutputFormatter {

    @Override
    public String format(Object value) {
        if (value instanceof Double d) {
            return (d == Math.floor(d))
                    ? String.valueOf(d.longValue())
                    : d.toString();
        }
        return String.valueOf(value);
    }
}
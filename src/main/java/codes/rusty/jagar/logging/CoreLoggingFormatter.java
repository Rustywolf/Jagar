package codes.rusty.jagar.logging;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class CoreLoggingFormatter extends Formatter {
    
    @Override
    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder();
        builder.append(DateFormat.getInstance().format(new Date(record.getMillis())));
        builder.append(" ");
        builder.append(DateFormat.getInstance().getTimeZone().getDisplayName(false, 0, Locale.getDefault(Locale.Category.DISPLAY)));
        builder.append(" [");
        builder.append(record.getLevel());
        builder.append("] ");
        builder.append(MessageFormat.format(record.getMessage(), record.getParameters()));
        return builder.toString();
    }
    
}

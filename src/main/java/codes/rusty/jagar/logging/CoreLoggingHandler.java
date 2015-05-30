package codes.rusty.jagar.logging;

import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class CoreLoggingHandler extends Handler {

    private final CoreLoggingFormatter formatter;

    public CoreLoggingHandler() {
        formatter = new CoreLoggingFormatter();
    }
    
    @Override
    public void publish(LogRecord record) {
        System.out.println(getFormatter().format(record));
    }

    @Override
    public void flush() {
        
    }

    @Override
    public void close() throws SecurityException {
        
    }

    @Override
    public Formatter getFormatter() {
        return formatter;
    }

}

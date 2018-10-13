package jmri.jmrit.newlogix.log;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;

/**
 * A writer that writes to a NewLogix log.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class LogWriter {
    
    private final LogHeader logHeader;
    LogWriterEncoder encoder;
    
    /**
     * Creates a LogWriter object.
     * 
     * @param log the log
     * @param output the output stream
     * @param name the name of the log
     * 
     * @throws java.io.IOException if an I/O error occurs
     * @throws java.lang.NoSuchMethodException
     * @throws java.lang.InstantiationException
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.reflect.InvocationTargetException
     */
    public LogWriter(Log log, OutputStream output, String name) throws IOException,
            NoSuchMethodException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        
        logHeader = new LogHeader(log);
        logHeader.setName(name);
        logHeader.writeHeader(output);
        
        encoder = logHeader.getEncoding().getEncoderClass()
                .getDeclaredConstructor().newInstance();
        encoder.init(output);
    }
    
    /**
     * Writes one row of data.
     * @param row the row to write
     * @throws java.io.IOException if an I/O error occurs
     */
    public void write(LogRow row) throws IOException {
        encoder.write(row);
    }
    
}

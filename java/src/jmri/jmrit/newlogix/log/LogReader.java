package jmri.jmrit.newlogix.log;

import java.io.InputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * A reader that reads from a NewLogix log.
 * 
 * @author Daniel Bergqvist 2018
 */
public class LogReader {

    private final Log _newLogixLog;
    private final InputStream _input;
    private final LogHeader _logHeader;
    LogReaderDecoder decoder;
    
    /**
     * Creates a LogWriter object.
     * 
     * @param log the log
     * @param input the input stream
     * @param name the name of the log
     * 
     * @throws java.io.IOException if an I/O error occurs
     * @throws java.lang.NoSuchMethodException
     * @throws java.lang.InstantiationException
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.reflect.InvocationTargetException
     * @throws jmri.jmrit.newlogix.log.Log.InvalidFormatException
     * @throws jmri.jmrit.newlogix.log.Log.UnsupportedVersionException
     */
    public LogReader(Log log, InputStream input) throws IOException,
            NoSuchMethodException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException,
            Log.InvalidFormatException, Log.UnsupportedVersionException {
        _newLogixLog = log;
        _input = input;
        
        _logHeader = new LogHeader(log);
        _logHeader.readHeader(input);
        
        decoder = _logHeader.getEncoding().getDecoderClass()
                .getDeclaredConstructor().newInstance();
        decoder.init(_newLogixLog, input);
    }
    
    /**
     * Get the name of the log.
     * @return the name
     */
    public String getName() {
        return _logHeader.getName();
    }
    
    /**
     * Try to read one more row of data. Returns null if end of data.
     * @return a row of data or null if end of data
     * @throws java.io.IOException if an I/O error occurs
     * @throws jmri.jmrit.newlogix.log.Log.InvalidFormatException
     */
    public LogRow read() throws IOException, Log.InvalidFormatException {
        return decoder.read();
    }
    
}

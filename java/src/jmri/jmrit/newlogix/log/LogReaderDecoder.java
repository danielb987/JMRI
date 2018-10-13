package jmri.jmrit.newlogix.log;

import java.io.IOException;
import java.io.InputStream;
import jmri.jmrit.newlogix.log.Log.InvalidFormatException;

/**
 * Reads the data part of the log from a stream and decodes it.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface LogReaderDecoder {

    /**
     * Init the decoder.
     * 
     * @param newLogixLog the log
     * @param input the input stream
     */
    public void init(Log newLogixLog, InputStream input);
    
    /**
     * Get the encoding this decoder implements.
     * @return the encoding
     */
    public Encodings getEncoding();
    
    /**
     * Try to read one more row of data. Returns null if end of data.
     * @return a row of data or null if end of data
     * @throws java.io.IOException if an I/O error occurs
     * @throws jmri.jmrit.newlogix.log.Log.InvalidFormatException if the log has invalid format
     */
    public LogRow read() throws IOException, InvalidFormatException;
    
}

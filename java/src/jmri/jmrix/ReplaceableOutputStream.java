package jmri.jmrix;

import java.io.IOException;
import java.io.OutputStream;

/**
 * An output stream where the stream can be replaced on the fly.
 *
 * @author Daniel Bergqvist (C) 2024
 */
public class ReplaceableOutputStream extends OutputStream {

    private OutputStream _stream;

    public synchronized void replaceStream(OutputStream stream) {
        this._stream = stream;
    }

    @Override
    public synchronized void write(int b) throws IOException {
        if (_stream != null) {
            _stream.write(b);
        }
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void write(byte b[]) throws IOException {
        if (_stream != null) {
            _stream.write(b);
        }
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void write(byte b[], int off, int len) throws IOException {
        if (_stream != null) {
            _stream.write(b, off, len);
        }
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void flush() throws IOException {
        if (_stream != null) {
            _stream.flush();
        }
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void close() throws IOException {
        if (_stream != null) {
            _stream.close();
        }
    }

}

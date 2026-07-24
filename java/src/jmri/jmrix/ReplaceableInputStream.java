package jmri.jmrix;

import java.io.*;

/**
 * An input stream where the stream can be replaced on the fly.
 *
 * @author Daniel Bergqvist (C) 2024
 */
public class ReplaceableInputStream extends InputStream {

    private InputStream _stream;

    public synchronized void replaceStream(InputStream stream) {
        this._stream = stream;
    }

    /** {@inheritDoc} */
    @Override
    public synchronized int read() throws IOException {
        if (_stream != null) {
            return _stream.read();
        } else {
            return -1;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public synchronized int read(byte[] b) throws IOException {
        if (_stream != null) {
            return _stream.read(b);
        } else {
            return -1;
        }
    }

    /** {@inheritDoc} */
    @Override
    public synchronized int read(byte[] b, int off, int len) throws IOException {
        if (_stream != null) {
            return _stream.read(b, off, len);
        } else {
            return -1;
        }
    }

    /** {@inheritDoc} */
    @Override
    public synchronized byte[] readAllBytes() throws IOException {
        if (_stream != null) {
            return _stream.readAllBytes();
        } else {
            return new byte[0];
        }
    }

    /** {@inheritDoc} */
    @Override
    public synchronized byte[] readNBytes(int len) throws IOException {
        if (_stream != null) {
            return _stream.readNBytes(len);
        } else {
            return new byte[0];
        }
    }

    /** {@inheritDoc} */
    @Override
    public synchronized int readNBytes(byte[] b, int off, int len) throws IOException {
        if (_stream != null) {
            return _stream.readNBytes(b, off, len);
        } else {
            return -1;
        }
    }

    /** {@inheritDoc} */
    @Override
    public synchronized long skip(long n) throws IOException {
        if (_stream != null) {
            return _stream.skip(n);
        } else {
            return 0;
        }
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void skipNBytes(long n) throws IOException {
        if (_stream != null) {
            _stream.skipNBytes(n);
        }
    }

    /** {@inheritDoc} */
    @Override
    public synchronized int available() throws IOException {
        if (_stream != null) {
            return _stream.available();
        } else {
            return 0;
        }
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void close() throws IOException {
        if (_stream != null) {
            _stream.close();
        }
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void mark(int readlimit) {
        if (_stream != null) {
            _stream.mark(readlimit);
        }
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void reset() throws IOException {
        if (_stream != null) {
            _stream.reset();
        }
    }

    /** {@inheritDoc} */
    @Override
    public synchronized boolean markSupported() {
        if (_stream != null) {
            return _stream.markSupported();
        } else {
            return false;
        }
    }

    /** {@inheritDoc} */
    @Override
    public synchronized long transferTo(OutputStream out) throws IOException {
        if (_stream != null) {
            return _stream.transferTo(out);
        } else {
            return 0;
        }
    }

}

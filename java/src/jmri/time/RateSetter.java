package jmri.time;

/**
 * Set the rate of the time provider.
 *
 * @author Daniel Bergqvist (C) 2025
 */
public interface RateSetter {

    /**
     * Can the rate be set?
     * @return true if the rate can be set, false otherwise
     */
    boolean canSetRate();

    /**
     * Set the rate of the time provider and fire the rate event.
     * @param rate the rate
     * @throws UnsupportedOperationException if the rate couldn't be set
     */
    void setRate(double rate) throws UnsupportedOperationException;

    /**
     * Set the rate of the time provider without firing the rate event.
     * Only intended for internal use in the {@code jmri.time} package.
     * @param rate the rate
     * @throws UnsupportedOperationException if the rate couldn't be set
     */
    void setRateInternal(double rate) throws UnsupportedOperationException;

}

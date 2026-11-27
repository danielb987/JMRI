package jmri.time.rate;

/**
 * A rate of integer values.
 *
 * @author Daniel Bergqvist (C) 2025
 */
public class ChangeableIntegerRate extends IntegerRate {

    public ChangeableIntegerRate(int rate) {
        super(rate);
    }

    /**
     * Set the rate, rounded to an integer.
     * @param rate the rate
     * @throws UnsupportedOperationException if the rate couldn't be set
     */
    public void setRate(double rate) throws UnsupportedOperationException {
        this._rate = (int) Math.round(rate);
    }

    /**
     * Set the rate of the clock.
     * @param rate the rate.
     * @throws UnsupportedOperationException if the rate couldn't be set
     */
    public void setRate(int rate) throws UnsupportedOperationException {
        this._rate = rate;
    }

}

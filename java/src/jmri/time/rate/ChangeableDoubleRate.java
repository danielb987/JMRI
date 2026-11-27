package jmri.time.rate;

/**
 * A changeable rate of double values.
 *
 * @author Daniel Bergqvist (C) 2025
 */
public class ChangeableDoubleRate extends DoubleRate {

    public ChangeableDoubleRate(double rate) {
        super(rate);
    }

    /**
     * Set the rate.
     * @param rate the rate
     */
    public void setRate(double rate) {
        this._rate = rate;
    }

}

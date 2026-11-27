package jmri.time.rate;

/**
 * A rate of integer values.
 *
 * @author Daniel Bergqvist (C) 2025
 */
public class ChangeableOnceDecimalRate extends OnceDecimalRate
        {

    public ChangeableOnceDecimalRate(int rateTimesTen) {
        super(rateTimesTen);
    }

    /**
     * Set the rate, rounded to one decimal.
     * @param rate the rate
     * @throws UnsupportedOperationException if the rate couldn't be set
     */
    public void setRate(double rate) throws UnsupportedOperationException {
        this._rateTimesTen = (int) Math.round(rate * 10);
    }

    /**
     * Set the rate of the clock.
     * @param rateTimesTen the rate times ten. If this number is 23, the rate is 2.3
     * @throws UnsupportedOperationException if the rate couldn't be set
     */
    public void setRate(int rateTimesTen) throws UnsupportedOperationException {
        this._rateTimesTen = rateTimesTen;
    }

}

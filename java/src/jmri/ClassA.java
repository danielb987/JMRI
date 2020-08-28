package jmri;

import javax.annotation.*;

@ParametersAreNonnullByDefault
class ClassA {
    
    public void testA(@CheckForNull Object param) {
        testC(param);
    }
    
    public void testB(@Nullable Object param) {
        testC(param);
    }
    
    private void testC(Object param) {
        // Do nothing
    }
    
}

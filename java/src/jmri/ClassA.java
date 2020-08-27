package jmri;

import javax.annotation.*;

@ParametersAreNonnullByDefault
class ClassA {
    
    public void testA(@CheckForNull Object param) {
        // Do nothing
    }
    
    public void testB(@Nullable Object param) {
        // Do nothing
    }
}

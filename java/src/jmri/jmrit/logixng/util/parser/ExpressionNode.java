package jmri.jmrit.logixng.util.parser;

/**
 * A parsed expression
 */
public interface ExpressionNode<E> {

    public E calculate();
    
}

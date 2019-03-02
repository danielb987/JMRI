package jmri.jmrit.logixng;

import javax.annotation.Nonnull;
import jmri.Manager;

/**
 * Manager for Expression
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface StringExpressionManager extends Manager<MaleStringExpressionSocket> {

    /**
     * Remember a NamedBean Object created outside the manager.
     * This method creates a MaleStringExpressionSocket for the action.
     *
     * @param expression the bean
     * @return the male socket for this expression
     * @throws IllegalArgumentException if the expression has an invalid system name
     */
    public MaleStringExpressionSocket register(@Nonnull StringExpression expression)
            throws IllegalArgumentException;
    
    /**
     * Create a new system name for an Expression.
     *
     * @param newLogix the LogixNG that this analog expression belongs to
     * @return a new system name
     */
    public String getNewSystemName(LogixNG newLogix);

    public FemaleStringExpressionSocket createFemaleStringExpressionSocket(
            Base parent, FemaleSocketListener listener, String socketName);

    public FemaleStringExpressionSocket createFemaleStringExpressionSocket(
            Base parent,
            FemaleSocketListener listener,
            String socketName,
            MaleStringExpressionSocket maleSocket);

    /*.*
     * Add an Expression.
     *
     * @param expression the expression to add
     * @throws IllegalArgumentException if the expression has an invalid system name
     */
//    public void addExpression(Expression expression)
//            throws IllegalArgumentException;

    /*.*
     * Locate via user name, then system name if needed. Does not create a new
     * one if nothing found
     *
     * @param name User name or system name to match
     * @return null if no match found
     */
//    public Expression getExpression(String name);

//    public Expression getByUserName(String s);

//    public Expression getBySystemName(String s);

    /**
     * Delete Expression by removing it from the manager. The Expression must
     * first be deactivated so it stops processing.
     *
     * @param x the Expression to delete
     */
//    void deleteExpression(Expression x);

}

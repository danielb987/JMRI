package jmri.jmrit.newlogix;

import javax.annotation.Nonnull;
import jmri.Manager;
import jmri.jmrit.newlogix.NewLogix;
import jmri.jmrit.newlogix.Expression;

/**
 * Manager for Expression
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface ExpressionManager extends Manager<MaleExpressionSocket> {

    /**
     * Remember a NamedBean Object created outside the manager.
     * This method creates a MaleActionSocket for the action.
     *
     * @param expression the bean
     * @return the male socket for this expression
     * @throws IllegalArgumentException if the expression has an invalid system name
     */
    public MaleExpressionSocket register(@Nonnull Expression expression)
            throws IllegalArgumentException;
    
    /**
     * Create a new system name for an Expression.
     *
     * @param newLogix the NewLogix that this expression belongs to
     * @return a new system name
     */
    public String getNewSystemName(NewLogix newLogix);

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

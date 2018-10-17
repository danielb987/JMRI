package jmri;

/**
 * Manager for NewLogixExpression
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface ExpressionManager extends Manager<NewLogixExpression> {

    /**
     * Create a new system name for an NewLogixExpression.
     *
     * @param newLogix the NewLogix that this expression belongs to
     * @return a new system name
     */
    public String getNewSystemName(NewLogix newLogix);

    /**
     * Add an NewLogixExpression.
     *
     * @param expression the expression to add
     * @throws IllegalArgumentException if the expression has an invalid system name
     */
    public void addExpression(NewLogixExpression expression)
            throws IllegalArgumentException;

    /**
     * Locate via user name, then system name if needed. Does not create a new
     * one if nothing found
     *
     * @param name User name or system name to match
     * @return null if no match found
     */
    public NewLogixExpression getExpression(String name);

    public NewLogixExpression getByUserName(String s);

    public NewLogixExpression getBySystemName(String s);

    /**
     * Delete NewLogixExpression by removing it from the manager. The NewLogixExpression must
 first be deactivated so it stops processing.
     *
     * @param x the NewLogixExpression to delete
     */
    void deleteExpression(NewLogixExpression x);

}

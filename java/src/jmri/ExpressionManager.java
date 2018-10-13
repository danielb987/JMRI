package jmri;

/**
 * Manager for Expression
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface ExpressionManager extends Manager<Expression> {

    /**
     * Create a new Expression if the Expression does not exist.
     *
     * @param systemName the system name
     * @param userName   the user name
     * @return a new Expression or null if unable to create: An error, or the
     * Expression already exists
     */
    public Expression createNewExpression(String systemName, String userName);

    /**
     * For use with User GUI, to allow the auto generation of systemNames, where
     * the user can optionally supply a username.
     *
     * @param userName the user name
     * @return a new NewLogix or null if unable to create
     */
    public Expression createNewExpression(String userName);

    /**
     * Locate via user name, then system name if needed. Does not create a new
     * one if nothing found
     *
     * @param name User name or system name to match
     * @return null if no match found
     */
    public Expression getExpression(String name);

    public Expression getByUserName(String s);

    public Expression getBySystemName(String s);

    /**
     * Delete Expression by removing it from the manager. The Expression must
     * first be deactivated so it stops processing.
     *
     * @param x the Expression to delete
     */
    void deleteExpression(Expression x);

}

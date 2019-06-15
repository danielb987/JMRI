package jmri.jmrit.logixng.digital.expressions;

import static jmri.Conditional.OPERATOR_AND;
import static jmri.Conditional.OPERATOR_NONE;
import static jmri.Conditional.OPERATOR_OR;

import java.util.List;
import java.util.ArrayList;
import java.util.BitSet;
import jmri.InstanceManager;
import jmri.JmriException;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.ConditionalNG;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.jmrit.logixng.DigitalExpression;
import jmri.jmrit.logixng.DigitalExpressionManager;
import jmri.jmrit.logixng.FemaleDigitalExpressionSocket;
import jmri.jmrit.logixng.SocketAlreadyConnectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Evaluates to True if all the child expressions evaluate to true.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class Antecedent extends AbstractDigitalExpression implements FemaleSocketListener {

    static final java.util.ResourceBundle rbx = java.util.ResourceBundle.getBundle("jmri.jmrit.conditional.ConditionalBundle");  // NOI18N
    
    private Antecedent _template;
    String _antecedent;
    List<String> _childrenSystemNames;
    List<FemaleDigitalExpressionSocket> _children = new ArrayList<>();
    
    /**
     * Create a new instance of Antecedent and generate a new system name.
     */
    public Antecedent(ConditionalNG conditionalNG) {
        super(InstanceManager.getDefault(DigitalExpressionManager.class).getNewSystemName(conditionalNG));
        init();
    }
    
    /**
     * Create a new instance of Antecedent and generate a new system name.
     */
    public Antecedent(ConditionalNG conditionalNG, String antecedent) {
        super(InstanceManager.getDefault(DigitalExpressionManager.class).getNewSystemName(conditionalNG));
        _antecedent = antecedent;
        init();
    }
    
    public Antecedent(String sys) throws BadSystemNameException {
        super(sys);
        init();
    }

    public Antecedent(String sys, String user)
            throws BadUserNameException, BadSystemNameException {
        super(sys, user);
        init();
    }
    
//    public Antecedent(String sys, List<String> childrenSystemNames) throws BadSystemNameException {
//        super(sys);
//        _childrenSystemNames = childrenSystemNames;
//    }

    public Antecedent(String sys, String user, List<String> childrenSystemNames)
            throws BadUserNameException, BadSystemNameException {
        super(sys, user);
        _childrenSystemNames = childrenSystemNames;
    }

    public Antecedent(String sys, String user, String antecedent)
            throws BadUserNameException, BadSystemNameException {
        super(sys, user);
        _antecedent = antecedent;
    }

    private Antecedent(Antecedent template, String sys) {
        super(sys);
        _template = template;
        if (_template == null) throw new NullPointerException();    // Temporary solution to make variable used.
    }
    
    /** {@inheritDoc} */
    @Override
    public Base getNewObjectBasedOnTemplate(String sys) {
        return new Antecedent(this, sys);
    }
    
    private void init() {
        _children.add(InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleSocket(this, this, getNewSocketName()));
    }

    /** {@inheritDoc} */
    @Override
    public Category getCategory() {
        return Category.COMMON;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isExternal() {
        return false;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean evaluate() {
        
        if (_antecedent.isEmpty()) {
            return false;
        }
        
        boolean result;
        
        char[] ch = _antecedent.toCharArray();
        int n = 0;
        for (int j = 0; j < ch.length; j++) {
            if (ch[j] != ' ') {
                if (ch[j] == '{' || ch[j] == '[') {
                    ch[j] = '(';
                } else if (ch[j] == '}' || ch[j] == ']') {
                    ch[j] = ')';
                }
                ch[n++] = ch[j];
            }
        }
        try {
            List<DigitalExpression> list = new ArrayList<>();
            for (DigitalExpression e : _children) {
                list.add(e);
            }
            DataPair dp = parseCalculate(new String(ch, 0, n), list);
            result = dp.result;
        } catch (NumberFormatException nfe) {
            result = false;
            log.error(getDisplayName() + " parseCalculation error antecedent= " + _antecedent + ", ex= " + nfe);  // NOI18N
        } catch (IndexOutOfBoundsException ioob) {
            result = false;
            log.error(getDisplayName() + " parseCalculation error antecedent= " + _antecedent + ", ex= " + ioob);  // NOI18N
        } catch (JmriException je) {
            result = false;
            log.error(getDisplayName() + " parseCalculation error antecedent= " + _antecedent + ", ex= " + je);  // NOI18N
        }
        
        return result;
    }
    
    /** {@inheritDoc} */
    @Override
    public void reset() {
        for (DigitalExpression e : _children) {
            e.reset();
        }
    }
    
    @Override
    public FemaleSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
        return _children.get(index);
    }
    
    @Override
    public int getChildCount() {
        return _children.size();
    }
    
    public void setChildCount(int count) {
        // Is there too many children?
        while (_children.size() > count) {
            int childNo = _children.size()-1;
            FemaleSocket socket = _children.get(childNo);
            if (socket.isConnected()) {
                socket.disconnect();
            }
            _children.remove(childNo);
        }
        
        // Is there not enough children?
        while (_children.size() < count) {
            _children.add(InstanceManager.getDefault(DigitalExpressionManager.class)
                    .createFemaleSocket(this, this, getNewSocketName()));
        }
    }
    
    @Override
    public String getShortDescription() {
        return Bundle.getMessage("Antecedent");
    }
    
    @Override
    public String getLongDescription() {
        return getShortDescription();
    }

    @Override
    public void connected(FemaleSocket socket) {
        boolean hasFreeSocket = false;
        for (FemaleDigitalExpressionSocket child : _children) {
            hasFreeSocket = !child.isConnected();
            if (hasFreeSocket) {
                break;
            }
        }
        if (!hasFreeSocket) {
            _children.add(InstanceManager.getDefault(DigitalExpressionManager.class).createFemaleSocket(this, this, getNewSocketName()));
        }
    }

    @Override
    public void disconnected(FemaleSocket socket) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /** {@inheritDoc} */
    @Override
    public void setup() {
        if (!_children.isEmpty()) {
            throw new RuntimeException("expression system names cannot be set more than once");
        }
        
        DigitalExpressionManager manager =
                InstanceManager.getDefault(DigitalExpressionManager.class);
        
        for (String systemName : _childrenSystemNames) {
            FemaleDigitalExpressionSocket femaleSocket =
                    manager.createFemaleSocket(this, this, getNewSocketName());
            
            try {
                femaleSocket.connect(manager.getBeanBySystemName(systemName));
            } catch (SocketAlreadyConnectedException ex) {
                // This shouldn't happen and is a runtime error if it does.
                throw new RuntimeException("socket is already connected");
            }
        }
        
        // Add one extra empty socket
        _children.add(manager.createFemaleSocket(this, this, getNewSocketName()));
    }



    /**
     * Check that an antecedent is well formed.
     *
     * @param ant the antecedent string description
     * @param variableList arraylist of existing Conditional variables
     * @return error message string if not well formed
     */
    public String validateAntecedent(String ant, List<DigitalExpression> variableList) {
        char[] ch = ant.toCharArray();
        int n = 0;
        for (int j = 0; j < ch.length; j++) {
            if (ch[j] != ' ') {
                if (ch[j] == '{' || ch[j] == '[') {
                    ch[j] = '(';
                } else if (ch[j] == '}' || ch[j] == ']') {
                    ch[j] = ')';
                }
                ch[n++] = ch[j];
            }
        }
        int count = 0;
        for (int j = 0; j < n; j++) {
            if (ch[j] == '(') {
                count++;
            }
            if (ch[j] == ')') {
                count--;
            }
        }
        if (count > 0) {
            return java.text.MessageFormat.format(
                    rbx.getString("ParseError7"), new Object[]{')'});  // NOI18N
        }
        if (count < 0) {
            return java.text.MessageFormat.format(
                    rbx.getString("ParseError7"), new Object[]{'('});  // NOI18N
        }
        try {
            DataPair dp = parseCalculate(new String(ch, 0, n), variableList);
            if (n != dp.indexCount) {
                return java.text.MessageFormat.format(
                        rbx.getString("ParseError4"), new Object[]{ch[dp.indexCount - 1]});  // NOI18N
            }
            int index = dp.argsUsed.nextClearBit(0);
            if (index >= 0 && index < variableList.size()) {
                return java.text.MessageFormat.format(
                        rbx.getString("ParseError5"),  // NOI18N
                        new Object[]{variableList.size(), index + 1});
            }
        } catch (NumberFormatException | IndexOutOfBoundsException | JmriException nfe) {
            return rbx.getString("ParseError6") + nfe.getMessage();  // NOI18N
        }
        return null;
    }

    /**
     * Parses and computes one parenthesis level of a boolean statement.
     * <p>
     * Recursively calls inner parentheses levels. Note that all logic operators
     * are detected by the parsing, therefore the internal negation of a
     * variable is washed.
     *
     * @param s            The expression to be parsed
     * @param variableList ConditionalVariables for R1, R2, etc
     * @return a data pair consisting of the truth value of the level a count of
     *         the indices consumed to parse the level and a bitmap of the
     *         variable indices used.
     * @throws jmri.JmriException if unable to compute the logic
     */
    DataPair parseCalculate(String s, List<DigitalExpression> variableList)
            throws JmriException {

        // for simplicity, we force the string to upper case before scanning
        s = s.toUpperCase();

        BitSet argsUsed = new BitSet(variableList.size());
        DataPair dp = null;
        boolean leftArg = false;
        boolean rightArg = false;
        int oper = OPERATOR_NONE;
        int k = -1;
        int i = 0;      // index of String s
        //int numArgs = 0;
        if (s.charAt(i) == '(') {
            dp = parseCalculate(s.substring(++i), variableList);
            leftArg = dp.result;
            i += dp.indexCount;
            argsUsed.or(dp.argsUsed);
        } else // cannot be '('.  must be either leftArg or notleftArg
        {
            if (s.charAt(i) == 'R') {  // NOI18N
                try {
                    k = Integer.parseInt(String.valueOf(s.substring(i + 1, i + 3)));
                    i += 2;
                } catch (NumberFormatException | IndexOutOfBoundsException nfe) {
                    k = Integer.parseInt(String.valueOf(s.charAt(++i)));
                }
                leftArg = variableList.get(k - 1).evaluate();
                i++;
                argsUsed.set(k - 1);
            } else if ("NOT".equals(s.substring(i, i + 3))) {  // NOI18N
                i += 3;

                // not leftArg
                if (s.charAt(i) == '(') {
                    dp = parseCalculate(s.substring(++i), variableList);
                    leftArg = dp.result;
                    i += dp.indexCount;
                    argsUsed.or(dp.argsUsed);
                } else if (s.charAt(i) == 'R') {  // NOI18N
                    try {
                        k = Integer.parseInt(String.valueOf(s.substring(i + 1, i + 3)));
                        i += 2;
                    } catch (NumberFormatException | IndexOutOfBoundsException nfe) {
                        k = Integer.parseInt(String.valueOf(s.charAt(++i)));
                    }
                    leftArg = variableList.get(k - 1).evaluate();
                    i++;
                    argsUsed.set(k - 1);
                } else {
                    throw new JmriException(java.text.MessageFormat.format(
                            rbx.getString("ParseError1"), new Object[]{s.substring(i)}));  // NOI18N
                }
                leftArg = !leftArg;
            } else {
                throw new JmriException(java.text.MessageFormat.format(
                        rbx.getString("ParseError9"), new Object[]{s}));  // NOI18N
            }
        }
        // crank away to the right until a matching parent is reached
        while (i < s.length()) {
            if (s.charAt(i) != ')') {
                // must be either AND or OR
                if ("AND".equals(s.substring(i, i + 3))) {  // NOI18N
                    i += 3;
                    oper = OPERATOR_AND;
                } else if ("OR".equals(s.substring(i, i + 2))) {  // NOI18N
                    i += 2;
                    oper = OPERATOR_OR;
                } else {
                    throw new JmriException(java.text.MessageFormat.format(
                            rbx.getString("ParseError2"), new Object[]{s.substring(i)}));  // NOI18N
                }
                if (s.charAt(i) == '(') {
                    dp = parseCalculate(s.substring(++i), variableList);
                    rightArg = dp.result;
                    i += dp.indexCount;
                    argsUsed.or(dp.argsUsed);
                } else // cannot be '('.  must be either rightArg or notRightArg
                {
                    if (s.charAt(i) == 'R') {  // NOI18N
                        try {
                            k = Integer.parseInt(String.valueOf(s.substring(i + 1, i + 3)));
                            i += 2;
                        } catch (NumberFormatException | IndexOutOfBoundsException nfe) {
                            k = Integer.parseInt(String.valueOf(s.charAt(++i)));
                        }
                        rightArg = variableList.get(k - 1).evaluate();
                        i++;
                        argsUsed.set(k - 1);
                    } else if ("NOT".equals(s.substring(i, i + 3))) {  // NOI18N
                        i += 3;
                        // not rightArg
                        if (s.charAt(i) == '(') {
                            dp = parseCalculate(s.substring(++i), variableList);
                            rightArg = dp.result;
                            i += dp.indexCount;
                            argsUsed.or(dp.argsUsed);
                        } else if (s.charAt(i) == 'R') {  // NOI18N
                            try {
                                k = Integer.parseInt(String.valueOf(s.substring(i + 1, i + 3)));
                                i += 2;
                            } catch (NumberFormatException | IndexOutOfBoundsException nfe) {
                                k = Integer.parseInt(String.valueOf(s.charAt(++i)));
                            }
                            rightArg = variableList.get(k - 1).evaluate();
                            i++;
                            argsUsed.set(k - 1);
                        } else {
                            throw new JmriException(java.text.MessageFormat.format(
                                    rbx.getString("ParseError3"), new Object[]{s.substring(i)}));  // NOI18N
                        }
                        rightArg = !rightArg;
                    } else {
                        throw new JmriException(java.text.MessageFormat.format(
                                rbx.getString("ParseError9"), new Object[]{s.substring(i)}));  // NOI18N
                    }
                }
                if (oper == OPERATOR_AND) {
                    leftArg = (leftArg && rightArg);
                } else if (oper == OPERATOR_OR) {
                    leftArg = (leftArg || rightArg);
                }
            } else {  // This level done, pop recursion
                i++;
                break;
            }
        }
        dp = new DataPair();
        dp.result = leftArg;
        dp.indexCount = i;
        dp.argsUsed = argsUsed;
        return dp;
    }


    static class DataPair {
        boolean result = false;
        int indexCount = 0;         // index reached when parsing completed
        BitSet argsUsed = null;     // error detection for missing arguments
    }

    private final static Logger log = LoggerFactory.getLogger(Antecedent.class);
}

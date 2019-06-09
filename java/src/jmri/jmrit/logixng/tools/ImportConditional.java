package jmri.jmrit.logixng.tools;

import java.util.List;
import jmri.Conditional;
import jmri.ConditionalAction;
import jmri.ConditionalVariable;
import jmri.InstanceManager;
import jmri.Light;
import jmri.Logix;
import jmri.Memory;
import jmri.NamedBean;
import jmri.Sensor;
import jmri.SignalHead;
import jmri.SignalMast;
import jmri.Turnout;
import jmri.jmrit.logix.OBlock;
import jmri.jmrit.logix.Warrant;
import jmri.jmrit.logix.WarrantManager;
import jmri.jmrit.logixng.ConditionalNG;
import jmri.jmrit.logixng.DigitalExpression;
import jmri.jmrit.logixng.LogixNG;
import jmri.jmrit.logixng.implementation.DefaultConditionalNG;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Imports Logixs to LogixNG
 * 
 * @author Daniel Bergqvist 2019
 */
public class ImportConditional {

    private final Logix _logix;
    private final Conditional _conditional;
    private final LogixNG _logixNG;
    private final ConditionalNG _conditionalNG;
    
    public ImportConditional(Logix logix, Conditional conditional, LogixNG logixNG, String conditionalNG_SysName) {
        
        _logix = logix;
        _conditional = conditional;
        _logixNG = logixNG;
        _conditionalNG = new DefaultConditionalNG(conditionalNG_SysName);
//        _conditionalNG = InstanceManager.getDefault(jmri.jmrit.logixng.ConditionalNG_Manager.class)
//                .createConditionalNG("Logix: "+_logix.getDisplayName(), false);
        
//        log.debug("Import Logix {} to LogixNG {}", _logix.getSystemName(), _logixNG.getSystemName());
        log.error("AA: Import Conditional {} to ConditionalNG {}", _conditional.getSystemName(), _conditionalNG.getSystemName());
    }
    
    public void doImport() {
        // Temporary fix Spotbugs error. We will use these later.
        if (_logix != null) {}
        if (_logixNG != null) {}
        
//        Conditional.AntecedentOperator ao = _conditional.getLogicType();
//        String antecedentExpression = _conditional.getAntecedentExpression();
        List<ConditionalVariable> conditionalVariables = _conditional.getCopyOfStateVariables();
//        boolean triggerOnChange = _conditional.getTriggerOnChange();
//        List<ConditionalAction> conditionalActions = _conditional.getCopyOfActions();
        
        buildExpression(conditionalVariables);
        
//        for (ConditionalAction ca : conditionalActions) {
            
//        }
    }
    
    
    private DigitalExpression buildExpression(List<ConditionalVariable> conditionalVariables) {
        for (ConditionalVariable cv : conditionalVariables) {
            NamedBean nb = cv.getNamedBeanData();
            switch (cv.getType().getItemType()) {
                case SENSOR:
                    Sensor sn = (Sensor)nb;
                    getSensorExpression(cv, sn);
                    break;
                case TURNOUT:
                    Turnout tn = (Turnout)nb;
                    getTurnoutExpression(cv, tn);
                    break;
                case MEMORY:
                    Memory my = (Memory)nb;
                    getMemoryExpression(cv, my);
                    break;
                case LIGHT:
                    Light l = (Light)nb;
                    getLightExpression(cv, l);
                    break;
                case SIGNALHEAD:
                    SignalHead s = (SignalHead)nb;
                    getSignalHeadExpression(cv, s);
                    break;
                case SIGNALMAST:
                    SignalMast sm = (SignalMast)nb;
                    getSignalMastExpression(cv, sm);
                    break;
                case ENTRYEXIT:
//                    NamedBean nb = jmri.InstanceManager.getDefault(jmri.jmrit.entryexit.EntryExitPairs.class).getBySystemName(_name);
//                    getSensorExpression(cv, sn);
                    break;
                case CONDITIONAL:
                    Conditional c = (Conditional)nb;
                    getConditionalExpression(cv, c);
                    break;
                case WARRANT:
                    Warrant w = (Warrant)nb;
                    getWarrantExpression(cv, w);
                    break;
                case OBLOCK:
                    OBlock b = (OBlock)nb;
                    getOBlockExpression(cv, b);
                    break;

                default:
                    log.warn("Unexpected type in ImportConditional.doImport(): {} -> {}", cv.getType(), cv.getType().getItemType());
                    break;
            }
        }
        
        return null;
    }
    
    
    private DigitalExpression getSensorExpression(ConditionalVariable cv, Sensor sn) {
        return null;
    }
    
    
    private DigitalExpression getTurnoutExpression(ConditionalVariable cv, Turnout tn) {
        return null;
    }
    
    
    private DigitalExpression getMemoryExpression(ConditionalVariable cv, Memory my) {
        return null;
    }
    
    
    private DigitalExpression getLightExpression(ConditionalVariable cv, Light l) {
        return null;
    }
    
    
    private DigitalExpression getSignalHeadExpression(ConditionalVariable cv, SignalHead s) {
        return null;
    }
    
    
    private DigitalExpression getSignalMastExpression(ConditionalVariable cv, SignalMast sm) {
        return null;
    }
    
    
    private DigitalExpression getConditionalExpression(ConditionalVariable cv, Conditional c) {
        return null;
    }
    
    
    private DigitalExpression getWarrantExpression(ConditionalVariable cv, Warrant w) {
        return null;
    }
    
    
    private DigitalExpression getOBlockExpression(ConditionalVariable cv, OBlock b) {
        return null;
    }
    
    
    private final static Logger log = LoggerFactory.getLogger(ImportConditional.class);

}

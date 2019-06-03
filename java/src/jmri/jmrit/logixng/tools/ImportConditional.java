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
        Conditional.AntecedentOperator ao = _conditional.getLogicType();
        String antecedentExpression = _conditional.getAntecedentExpression();
        List<ConditionalVariable> conditionalVariables = _conditional.getCopyOfStateVariables();
        boolean triggerOnChange = _conditional.getTriggerOnChange();
        List<ConditionalAction> conditionalActions = _conditional.getCopyOfActions();
        
        for (ConditionalVariable cv : conditionalVariables) {
            NamedBean nb = cv.getNamedBeanData();
            switch (cv.getType().getItemType()) {
                case SENSOR:
                    Sensor sn = (Sensor)nb;
                    break;
                case TURNOUT:
                    Turnout tn = (Turnout)nb;
                    break;
                case MEMORY:
                    Memory my = (Memory)nb;
                    break;
                case LIGHT:
                    Light l = (Light)nb;
                    break;
                case SIGNALHEAD:
                    SignalHead s = (SignalHead)nb;
                    break;
                case SIGNALMAST:
                    SignalMast sm = (SignalMast)nb;
                    break;
                case ENTRYEXIT:
//                    NamedBean nb = jmri.InstanceManager.getDefault(jmri.jmrit.entryexit.EntryExitPairs.class).getBySystemName(_name);
                    break;
                case CONDITIONAL:
                    Conditional c = (Conditional)nb;
                    break;
                case WARRANT:
                    Warrant w = (Warrant)nb;
                    break;
                case OBLOCK:
                    OBlock b = (OBlock)nb;
                    break;

                default:
                    log.warn("Unexpected type in ImportConditional.doImport(): {} -> {}", cv.getType(), cv.getType().getItemType());
                    break;
            }
        }
        
        for (ConditionalAction ca : conditionalActions) {
            
        }
//        for (int i=0; i < _logix.getNumConditionals(); i++) {
//            Conditional c = _logix.getConditional(_logix.getConditionalByNumberOrder(i));
//            log.error("Import Conditional {} to ConditionalNG {}", c.getSystemName(), _logixNG.getSystemName());
//        }
    }
    
    
    private final static Logger log = LoggerFactory.getLogger(ImportConditional.class);

}

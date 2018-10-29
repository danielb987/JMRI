package jmri.util;

import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.annotation.Nonnull;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import jmri.InvokeOnGuiThread;

/**
 * Utilities for handling JMRI's threading conventions.
 * <p>
 * For background, see
 * <a href="http://jmri.org/help/en/html/doc/Technical/Threads.shtml">http://jmri.org/help/en/html/doc/Technical/Threads.shtml</a>
 * <p>
 * Note this distinguishes "on layout", for example, Setting a sensor, from "on
 * GUI", for example, manipulating the Swing GUI. That may not be an important
 * distinction now, but it might be later, so we build it into the calls.
 *
 * @author Bob Jacobsen Copyright 2015
 */
public class ThreadingUtil {

    /**
     * Should GUI and layout be separate threads?
     * 
     * WARNING: Setting this variable to true will probably break things.
     * It's only intended for testing.
     */
//    private static final boolean SEPARATE_GUI_AND_LAYOUT_THREADS = false;
    private static final boolean SEPARATE_GUI_AND_LAYOUT_THREADS = true;

    static public interface ThreadAction extends Runnable {

        /**
         * {@inheritDoc}
         * <p>
         * Must handle its own exceptions.
         */
        @Override
        public void run();
    }
    
    static private class LayoutEvent {
        private final ThreadAction _threadAction;
        private final Condition _wait;
        
        public LayoutEvent(ThreadAction threadAction) {
            _threadAction = threadAction;
            _wait = null;
        }
        
        public LayoutEvent(ThreadAction threadAction,
                Condition wait) {
            _threadAction = threadAction;
            _wait = wait;
        }
    }
    
    private static Thread layoutThread = null;
    private static final Object layoutThreadLock = new Object();
    private static BlockingQueue<LayoutEvent> layoutEventQueue = null;

    @InvokeOnGuiThread
    public static void launchLayoutThread() {
        if (!SEPARATE_GUI_AND_LAYOUT_THREADS) {
            return;
        }
        
        layoutEventQueue = new ArrayBlockingQueue<>(1024);
        layoutThread = new Thread(() -> {
            while (true) {
                try {
                    LayoutEvent event = layoutEventQueue.take();
                    event._threadAction.run();
                    if (event._wait != null) {
                        event._wait.signalAll();
                    }
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "JMRI LayoutThread");
        layoutThread.setDaemon(true);
        layoutThread.start();
    }
    
    /**
     * Run some layout-specific code before returning.
     * <p>
     * Typical uses:
     * <p> {@code
     * ThreadingUtil.runOnLayout(() -> {
     *     sensor.setState(value);
     * }); 
     * }
     *
     * @param ta What to run, usually as a lambda expression
     */
    static public void runOnLayout(@Nonnull ThreadAction ta) {
        System.out.format("runOnLayout%n");
        if (layoutThread != null) {
            Lock lock = new ReentrantLock();
            Condition wait = lock.newCondition();
            layoutEventQueue.add(new LayoutEvent(ta, wait));
            try {
                wait.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } else {
            runOnGUI(ta);
        }
    }

    /**
     * Run some layout-specific code at some later point.
     * <p>
     * Please note the operation may have happened before this returns. Or
     * later. No long-term guarantees.
     * <p>
     * Typical uses:
     * <p> {@code
     * ThreadingUtil.runOnLayoutEventually(() -> {
     *     sensor.setState(value);
     * }); 
     * }
     *
     * @param ta What to run, usually as a lambda expression
     */
    static public void runOnLayoutEventually(@Nonnull ThreadAction ta) {
        if (layoutThread != null) {
            layoutEventQueue.add(new LayoutEvent(ta));
        } else {
            runOnGUIEventually(ta);
        }
    }

    /**
     * Run some layout-specific code at some later point, at least a known time
     * in the future.
     * <p>
     * There is no long-term guarantee about the accuracy of the interval.
     * <p>
     * Typical uses:
     * <p> {@code
     * ThreadingUtil.runOnLayoutEventually(() -> {
     *     sensor.setState(value);
     * }, 1000); 
     * }
     *
     * @param ta    What to run, usually as a lambda expression
     * @param delay interval in milliseconds
     * @return reference to timer object handling delay so you can cancel if desired; note that operation may have already taken place.
     */
    @Nonnull 
    static public Timer runOnLayoutDelayed(@Nonnull ThreadAction ta, int delay) {
        throw new RuntimeException("Not implemented yet");
//        return runOnGUIDelayed(ta, delay);
    }

    /**
     * Check if on the layout-operation thread.
     *
     * @return true if on the layout-operation thread
     */
    static public boolean isLayoutThread() {
        if (layoutThread != null) {
            return layoutThread == Thread.currentThread();
        } else {
            return isGUIThread();
        }
    }

    /**
     * Run some GUI-specific code before returning
     * <p>
     * Typical uses:
     * <p> {@code
     * ThreadingUtil.runOnGUI(() -> {
     *     mine.setVisible();
     * });
     * }
     * <p>
     * If an InterruptedException is encountered, it'll be deferred to the 
     * next blocking call via Thread.currentThread().interrupt()
     * 
     * @param ta What to run, usually as a lambda expression
     */
    static public void runOnGUI(@Nonnull ThreadAction ta) {
        if (isGUIThread()) {
            // run now
            ta.run();
        } else {
            // dispatch to Swing
            warnLocks();
            try {
                SwingUtilities.invokeAndWait(ta);
            } catch (InterruptedException e) {
                log.debug("Interrupted while running on GUI thread");
                Thread.currentThread().interrupt();
            } catch (InvocationTargetException e) {
                log.error("Error while on GUI thread", e.getCause());
                log.error("   Came from call to runOnGUI:", e);
                // should have been handled inside the ThreadAction
            }
        }
    }

    /**
     * Run some GUI-specific code before returning a value
     * <p>
     * Typical uses:
     * <p> {@code
     * ThreadingUtil.runOnGUI(() -> {
     *     mine.setVisible();
     * });
     * }
     * <p>
     * If an InterruptedException is encountered, it'll be deferred to the 
     * next blocking call via Thread.currentThread().interrupt()
     * 
     * @param ta What to run, usually as a lambda expression
     */
    static public <E> E runOnGUIwithReturn(@Nonnull ReturningThreadAction<E> ta) {
        if (isGUIThread()) {
            // run now
            return ta.run();
        } else {
            warnLocks();
            // dispatch to Swing
            final java.util.concurrent.atomic.AtomicReference<E> result = new java.util.concurrent.atomic.AtomicReference<>();
            try {
                SwingUtilities.invokeAndWait(() -> {
                    result.set(ta.run());
                });
            } catch (InterruptedException e) {
                log.debug("Interrupted while running on GUI thread");
                Thread.currentThread().interrupt();
            } catch (InvocationTargetException e) {
                log.error("Error while on GUI thread", e.getCause());
                log.error("   Came from call to runOnGUIwithReturn:", e);
                // should have been handled inside the ThreadAction
            }
            return result.get();
        }
    }

    public interface ReturningThreadAction<E> {
        public E run();
    }
    
    /**
     * Run some GUI-specific code at some later point.
     * <p>
     * If invoked from the GUI thread, the work is guaranteed to happen only
     * after the current routine has returned.
     * <p>
     * Typical uses:
     * <p> {@code 
     * ThreadingUtil.runOnGUIEventually( ()->{ 
     *      mine.setVisible();
     * } ); 
     * }
     *
     * @param ta What to run, usually as a lambda expression
     */
    static public void runOnGUIEventually(@Nonnull ThreadAction ta) {
        // dispatch to Swing
        SwingUtilities.invokeLater(ta);
    }

    /**
     * Run some GUI-specific code at some later point, at least a known time in
     * the future.
     * <p>
     * There is no long-term guarantee about the accuracy of the interval.
     * <p>
     * Typical uses:
     * <p>
     * {@code 
     * ThreadingUtil.runOnGUIEventually( ()->{ 
     *  mine.setVisible(); 
     * }, 1000);
     * }
     *
     * @param ta    What to run, usually as a lambda expression
     * @param delay interval in milliseconds
     * @return reference to timer object handling delay so you can cancel if desired; note that operation may have already taken place.
     */
    @Nonnull 
    static public Timer runOnGUIDelayed(@Nonnull ThreadAction ta, int delay) {
        // dispatch to Swing via timer
        Timer timer = new Timer(delay, (ActionEvent e) -> {
            ta.run();
        });
        timer.setRepeats(false);
        timer.start();
        return timer;
    }

    /**
     * Check if on the GUI event dispatch thread.
     *
     * @return true if on the event dispatch thread
     */
    static public boolean isGUIThread() {
        return SwingUtilities.isEventDispatchThread();
    }

    /**
     * Check whether a specific thread is running (or able to run) right now.
     *
     * @param t the thread to check
     * @return true is the specified thread is or could be running right now
     */
    static public boolean canThreadRun(@Nonnull Thread t) {
        Thread.State s = t.getState();
        return s.equals(Thread.State.RUNNABLE);
    }

    /**
     * Check whether a specific thread is currently waiting.
     * <p>
     * Note: This includes both waiting due to an explicit wait() call, and due
     * to being blocked attempting to synchronize.
     * <p>
     * Note: {@link #canThreadRun(Thread)} and {@link #isThreadWaiting(Thread)}
     * should never simultaneously be true, but it might look that way due to
     * sampling delays when checking on another thread.
     *
     * @param t the thread to check
     * @return true is the specified thread is or could be running right now
     */
    static public boolean isThreadWaiting(@Nonnull Thread t) {
        Thread.State s = t.getState();
        return s.equals(Thread.State.BLOCKED) || s.equals(Thread.State.WAITING) || s.equals(Thread.State.TIMED_WAITING);
    }


    /**
     * Warn if a thread is holding locks. Used when transitioning to another context.
     */
    static public void warnLocks() {
        if ( log.isDebugEnabled() ) {
            try {
                java.lang.management.ThreadInfo threadInfo = java.lang.management.ManagementFactory
                                                    .getThreadMXBean()
                                                        .getThreadInfo(new long[]{Thread.currentThread().getId()}, true, true)[0];

                java.lang.management.MonitorInfo[] monitors = threadInfo.getLockedMonitors();
                for (java.lang.management.MonitorInfo mon : monitors) {
                    log.warn("Thread was holding monitor {} from {}", mon, mon.getLockedStackFrame()); // yes, warn - for re-enable later
                }

                java.lang.management.LockInfo[] locks = threadInfo.getLockedSynchronizers();
                for (java.lang.management.LockInfo lock : locks) {
                    log.warn("Thread was holding lock {} from {}", lock);  // yes, warn - for re-enable later
                }
            } catch (RuntimeException ex) {
                // just record exceptions for later pick up during debugging
                if (!lastWarnLocksLimit) log.warn("Exception in warnLocks", ex);
                lastWarnLocksLimit = true;
                lastWarnLocksException = ex;
            }
        }
    }
    private static boolean lastWarnLocksLimit = false;
    public static RuntimeException lastWarnLocksException = null;
    
    private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ThreadingUtil.class);

}


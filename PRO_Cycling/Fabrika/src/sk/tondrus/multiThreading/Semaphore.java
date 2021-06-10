package sk.tondrus.multiThreading;

/**
 * trieda sluzi na to aby si thready navzajom neprepisovali udaje
 */
public class Semaphore {
    private MonitorObject myMonitorObject = new MonitorObject();
    private boolean wasSignalled_ = false;

    /**
     * funkcia zastavi thready dovtedy kym to nezmeni externy pokyn k tomu aby thready zacali pracovat
     */
    public void doWait() {
        synchronized (myMonitorObject) {
            while (!wasSignalled_) {
                try {
                    myMonitorObject.wait();
                } catch (InterruptedException e) {
                }
            }
            //clear signal and continue running.
            wasSignalled_ = false;
        }
    }

    /**
     * nastavi semafor na zelenu
     */
    public void doNotify() {
        synchronized (myMonitorObject) {
            wasSignalled_ = true;
            myMonitorObject.notify();
        }
    }

    public boolean wasSignalled() {
        return wasSignalled_;
    }

    private class MonitorObject {
    }
}
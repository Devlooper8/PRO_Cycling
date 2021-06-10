package sk.tondrus.view;

import javax.swing.*;

/**
 * Basic CursorToolkit that still allows mouseclicks
 */
public class CursorToolkitOne implements Cursors {
    public CursorToolkitOne() {
    }

    /**
     * Sets cursor for specified component to Wait cursor
     */
    public void startWaitCursor(JComponent component) {
        RootPaneContainer root =
                (RootPaneContainer) component.getTopLevelAncestor();
        root.getGlassPane().setCursor(WAIT_CURSOR);
        root.getGlassPane().setVisible(true);
    }

    /**
     * Sets cursor for specified component to normal cursor
     */
    public void stopWaitCursor(JComponent component) {
        RootPaneContainer root =
                (RootPaneContainer) component.getTopLevelAncestor();
        root.getGlassPane().setCursor(DEFAULT_CURSOR);
        root.getGlassPane().setVisible(false);
    }
}
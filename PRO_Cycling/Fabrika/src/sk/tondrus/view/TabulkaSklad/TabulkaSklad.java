/*
 * Created by JFormDesigner on Sat May 15 17:48:58 CEST 2021
 */

package sk.tondrus.view.TabulkaSklad;

import org.jdesktop.application.Application;
import sk.tondrus.AppFabrika;
import sk.tondrus.RMI.ObchodDataListener;
import sk.tondrus.databaza.databazaEntities.Sklad;
import sk.tondrus.view.CursorToolkitOne;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author Tomas Ondrus
 */
public class TabulkaSklad extends JPanel implements ObchodDataListener {
    private JTable tbl;
    private boolean prebiehaRefresh = false, notified = false;
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel panel1;

    public TabulkaSklad() {
        initComponents();
        createTable();
    }

    /**
     * vytvori tabulku a priradi jej nazvy stlpcov
     */
    public void createTable() {


        tbl = new JTable();
        JScrollPane jScrollPane = new JScrollPane(tbl);
        DefaultTableModel dtm = new DefaultTableModel(0, 0);
        String header[] = new String[]{"ID dielu", "Nazov dielu", "mnozstvo"};
        //Prida nazvy stlpcov v tabulke
        dtm.setColumnIdentifiers(header);

        tbl.setModel(dtm);

        // ButtonColumn buttonColumn = new ButtonColumn(tbl, make, 4);
        panel1.add(jScrollPane);
    }

    /**
     * naplni tabulku
     */
    public void fillTable() {
        TabulkaSklad.RefreshWorker worker = new TabulkaSklad.RefreshWorker();
        worker.execute();

    }

    ;

    /**
     * informuje pouzivatela ze sa zmenili data v databaze a mal by prekreslit data na obrazovke
     * @throws RemoteException
     */
    public void NotifyUser() throws RemoteException {
        if (prebiehaRefresh) {
            notified = true;
            return;
        }
        prebiehaRefresh = true;
        DefaultTableModel defaultTableModel = ((DefaultTableModel) tbl.getModel());
        for (int i = defaultTableModel.getRowCount() - 1; i >= 0; i--) {
            defaultTableModel.removeRow(i);
        }
        fillTable();
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        panel1 = new JPanel();

        //======== this ========
        setLayout(new BorderLayout());

        //======== panel1 ========
        {
            panel1.setLayout(new BorderLayout());
        }
        add(panel1, BorderLayout.CENTER);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }


    private class RefreshWorker extends SwingWorker<java.util.List<Sklad>, Void> {

        CursorToolkitOne cursorToolkitOne = new CursorToolkitOne();

        @Override
        protected List<Sklad> doInBackground() throws Exception {
            cursorToolkitOne.startWaitCursor(tbl);
            AppFabrika appFabrika = (AppFabrika) Application.getInstance();
            List<Sklad> skladList = null;
            skladList = appFabrika.LoadSklad();

            return skladList;
        }

        @Override
        protected void done() {
            cursorToolkitOne.stopWaitCursor(tbl);
            //vykonava sa v EDT
            List<Sklad> skladList = null;
            try {
                skladList = get();
                DefaultTableModel dtm = (DefaultTableModel) tbl.getModel();

                for (Sklad sklad : skladList) {

                    dtm.addRow(new Object[]{sklad.getPartID(), sklad.getNazov(), sklad.getPocetKusov()});
                    tbl.setRowHeight(30);

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } finally {
                prebiehaRefresh = false;
                if (notified) {
                    try {
                        notified = false;
                        NotifyUser();
                    } catch (RemoteException exception) {
                        exception.printStackTrace();
                    }
                }
            }

        }
    }
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}

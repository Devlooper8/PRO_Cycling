/*
 * Created by JFormDesigner on Fri May 07 18:30:30 CEST 2021
 */

package sk.tondrus.view.TabulkaObjednavokFabrika;

import org.jdesktop.application.Application;
import sk.tondrus.AppFabrika;
import sk.tondrus.RMI.ObchodDataListener;
import sk.tondrus.databaza.databazaEntities.Objednavka;
import sk.tondrus.view.CursorToolkitOne;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author Tomas Ondrus
 */
public class TabulkaObjednavokFabrika extends JPanel implements ObchodDataListener {
    Action make = new AbstractAction("spustit vyrobu") {
        public void actionPerformed(ActionEvent e) {


            AppFabrika appFabrika = (AppFabrika) Application.getInstance();
            JTable table = (JTable) e.getSource();
            int modelRow = Integer.valueOf(e.getActionCommand());
            int index = table.getSelectedRow();
            int OrderID = (int) table.getValueAt(modelRow, 0);
            try {
                appFabrika.ZacniVyrobu(OrderID);
            } catch (RemoteException remoteException) {
                remoteException.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();

            }
        }
    };
    private JTable tbl;
    private boolean prebiehaRefresh = false, notified = false;
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel panel1;


    public TabulkaObjednavokFabrika() {
        initComponents();
        createTable();
    }

    /**
     * vytvori tabulku a priradi jej nazvy stlpcov
     */
    public void createTable() {


        tbl = new JTable();
        tbl.setDefaultRenderer(Object.class, new ObjednavkyCellRenderer());
        JScrollPane jScrollPane = new JScrollPane(tbl);
        DefaultTableModel dtm = new DefaultTableModel(0, 0);
        String header[] = new String[]{"Cislo objednavky", "ID uzivatela", "popis", "cena", "stav objednavky"};
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
        RefreshWorker worker = new RefreshWorker();
        worker.execute();

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

    /**
     * informuje pouzivatela ze sa zmenili data v databaze a mal by prekreslit data na obrazovke
     * @throws RemoteException
     */
    @Override
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



    private class ObjednavkyCellRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table,
                                                       Object value,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int row,
                                                       int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (column == 4) {
                int stav = (int) value;
                switch (stav) {
                    case Objednavka.VYTVORENA:
                        label.setText("vytvorena");
                        break;
                    case Objednavka.SPRACOVANA:
                        label.setText("spracovana");
                        break;
                    case Objednavka.VYRABA_SA:
                        label.setText("vyraba sa");
                        break;
                    case Objednavka.DOKONCENA:
                        label.setText("vyrobena");
                        break;
                    case Objednavka.EXPEDOVANA:
                        label.setText("expedovana");
                        break;
                }
            }
            return label;
        }
    }


    private class RefreshWorker extends SwingWorker<List<Objednavka>, Void> {

        CursorToolkitOne cursorToolkitOne = new CursorToolkitOne();

        @Override
        protected List<Objednavka> doInBackground() throws Exception {
            cursorToolkitOne.startWaitCursor(tbl);
            AppFabrika appFabrika = (AppFabrika) Application.getInstance();
            java.util.List<Objednavka> productList = null;
            productList = appFabrika.FabrikaLoadObjednavky();

            return productList;
        }

        @Override
        protected void done() {
            cursorToolkitOne.stopWaitCursor(tbl);
            //vykonava sa v EDT
            List<Objednavka> productList = null;
            try {
                productList = get();
                DefaultTableModel dtm = (DefaultTableModel) tbl.getModel();

                for (Objednavka objednavka : productList) {

                    dtm.addRow(new Object[]{objednavka.getOrderID(), objednavka.getUserId(), objednavka.getPopis(), objednavka.getCena(), objednavka.getStav()});
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




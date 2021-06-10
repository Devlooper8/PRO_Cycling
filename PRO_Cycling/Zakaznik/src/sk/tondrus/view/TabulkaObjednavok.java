package sk.tondrus.view;

import org.jdesktop.application.Application;
import sk.tondrus.AppZakaznik;
import sk.tondrus.Exceptions.UnautorizedAccess;
import sk.tondrus.RMI.ObchodDataListener;
import sk.tondrus.databaza.databazaEntities.Objednavka;
import sk.tondrus.view.TabulkaObjednavokFabrika.ButtonColumn;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutionException;
/*
 * Created by JFormDesigner on Tue Apr 13 00:41:49 CEST 2021
 */


/**
 * @author Tomas Ondrus
 */
public class TabulkaObjednavok extends JPanel implements ObchodDataListener {
    private JTable tbl;
    public TabulkaObjednavok() {
        initComponents();
        createTable();
    }
/*



*/
        public void createTable() {


        tbl = new JTable();
        JScrollPane jScrollPane= new JScrollPane(tbl);
        tbl.setDefaultRenderer(Object.class,new ObjednavkyCellRenderer());
        DefaultTableModel dtm = new DefaultTableModel(0, 0){
            public boolean isCellEditable(int row,int column){
                if (column==4) {
                    int stav = (int) getValueAt(row, 3);
                    return stav==Objednavka.VYTVORENA;
                }
                return false;
            }
        };
        String header[] = new String[]{"Cislo objednavky","popis", "cena","Stav Objednávky",""};

            dtm.setColumnIdentifiers(header);
            tbl.setModel(dtm);

            ButtonColumn buttonColumn= new ButtonColumn(tbl,delete,4){
                public Component getTableCellRendererComponent(
                        JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
                    JButton button= (JButton) super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
                    int stav= (int) table.getValueAt(row,3);

                        if (stav!=Objednavka.VYTVORENA){
                            button.setText("");
                            button.setContentAreaFilled(false);
                        } else {
                            button.setContentAreaFilled(true);
                        }

                        return button;
                }
            };
            panel1.add(jScrollPane);
        }


        public void fillTable() {
            SwingWorker<java.util.List<Objednavka>,Void> worker= new SwingWorker<java.util.List<Objednavka>,Void>(){
                CursorToolkitOne cursorToolkitOne= new CursorToolkitOne();
                @Override
                protected java.util.List<Objednavka> doInBackground() throws Exception {
                    cursorToolkitOne.startWaitCursor(tbl);
                    AppZakaznik appZakaznik = (AppZakaznik) Application.getInstance();
                    java.util.List<Objednavka> productList = null;
                    productList = appZakaznik.getObchod().UserLoadObjednavky(appZakaznik.getUserID());

                    return productList;
                }
                @Override
                protected void done(){
                    cursorToolkitOne.stopWaitCursor(tbl);
                    //vykonava sa v EDT
                    List<Objednavka> productList= null;
                    try {
                        productList = get();
                        DefaultTableModel dtm = (DefaultTableModel) tbl.getModel();
                        for(int i= dtm.getRowCount()-1;i>=0;i--)
                            dtm.removeRow(i);

                        for (Objednavka objednavka : productList) {

                            dtm.addRow(new Object[]{objednavka.getOrderID(), objednavka.getPopis(), objednavka.getCena(),objednavka.getStav()});
                            tbl.setRowHeight(30);

                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }
            };
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

    Action delete = new AbstractAction("vymazat")
    {
        /**
         * handler pri stlaceni tlacidla sa vymaze objednavka
         */
        public void actionPerformed(ActionEvent e)
        {
            AppZakaznik appZakaznik = (AppZakaznik) Application.getInstance();
            JTable table = (JTable)e.getSource();
            int modelRow = Integer.valueOf( e.getActionCommand() );
            int index= table.getSelectedRow();
            int OrderID= (int) table.getValueAt(modelRow,0);
            try {
                appZakaznik.getObchod().UserDeleteObjednavky(appZakaznik.getUserID(), OrderID);
            } catch (RemoteException remoteException) {
                remoteException.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (UnautorizedAccess unautorizedAccess) {
                unautorizedAccess.printStackTrace();
            }
            ((DefaultTableModel)table.getModel()).removeRow(modelRow);
        }
    };

    /**
     * informuje pouzivatela ze sa zmenili data v databaze a mal by prekreslit data na obrazovke
     * @throws RemoteException
     */
    @Override
    public void NotifyUser() throws RemoteException {
        DefaultTableModel defaultTableModel= ((DefaultTableModel)tbl.getModel());
        for(int i= defaultTableModel.getRowCount()-1;i>=0;i--){
            defaultTableModel.removeRow(i);
        }
        fillTable();
    }

    private class ObjednavkyCellRenderer extends DefaultTableCellRenderer{
            public Component getTableCellRendererComponent(JTable table,
                                                           Object value,
                                                           boolean isSelected,
                                                           boolean hasFocus,
                                                           int row,
                                                           int column){
                JLabel label= (JLabel) super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
                if (column==3){
                    int stav= (int) value;
                    switch (stav){
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
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel panel1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}

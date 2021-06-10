package sk.tondrus.view;

import org.jdesktop.application.Application;
import sk.tondrus.AppFabrika;
import sk.tondrus.RMI.ObchodDataListener;
import sk.tondrus.view.TabulkaObjednavokFabrika.TabulkaObjednavokFabrika;
import sk.tondrus.view.TabulkaSklad.TabulkaSklad;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
/*
 * Created by JFormDesigner on Mon Mar 22 19:00:12 CET 2021
 */


/**
 * @author Tomas Ondrus
 * Hlavné okno aplikacie zo strany servera
 */
 public class MainScreenFabrika extends JFrame implements ObchodDataListener {

    AppFabrika appFabrika = (AppFabrika) Application.getInstance();
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JMenuBar menuBar1;
    private JMenu Sklad;
    private JMenu Objednavky;
    private JPanel hSpacer1;

    public MainScreenFabrika() {
        initComponents();
    }

    private void ObjednavkyMouseClicked(MouseEvent e) {
        TabulkaObjednavokFabrika tabulkaObjednavokFabrika = new TabulkaObjednavokFabrika();
        BorderLayout layout = (BorderLayout) getContentPane().getLayout();
        Component currContent = layout.getLayoutComponent(BorderLayout.CENTER);
        if (currContent != null) getContentPane().remove(currContent);
        tabulkaObjednavokFabrika.fillTable();
        getContentPane().add(tabulkaObjednavokFabrika);
        getContentPane().revalidate();

    }

    /**
     * informuje pouzivatela ze sa zmenili data v databaze a mal by prekreslit data na obrazovke
     * @throws RemoteException
     */
    @Override
    public void NotifyUser() throws RemoteException {
        BorderLayout layout = (BorderLayout) getContentPane().getLayout();
        Component currContent = layout.getLayoutComponent(BorderLayout.CENTER);
        if (currContent != null && currContent instanceof ObchodDataListener) {
            ((ObchodDataListener) currContent).NotifyUser();
        }

    }

    private void SkladMouseClicked(MouseEvent e) {
        TabulkaSklad tabulkaSklad = new TabulkaSklad();
        BorderLayout layout = (BorderLayout) getContentPane().getLayout();
        Component currContent = layout.getLayoutComponent(BorderLayout.CENTER);
        if (currContent != null) getContentPane().remove(currContent);
        tabulkaSklad.fillTable();
        getContentPane().add(tabulkaSklad);
        getContentPane().revalidate();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        menuBar1 = new JMenuBar();
        Sklad = new JMenu();
        Objednavky = new JMenu();
        hSpacer1 = new JPanel(null);

        //======== this ========
        setMinimumSize(new Dimension(600, 400));
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== menuBar1 ========
        {

            //======== Sklad ========
            {
                Sklad.setText("Sklad");
                Sklad.setFont(Sklad.getFont().deriveFont(Sklad.getFont().getSize() + 4f));
                Sklad.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        SkladMouseClicked(e);
                    }
                });
            }
            menuBar1.add(Sklad);

            //======== Objednavky ========
            {
                Objednavky.setText("Objedn\u00e1vky");
                Objednavky.setFont(Objednavky.getFont().deriveFont(Objednavky.getFont().getSize() + 4f));
                Objednavky.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        ObjednavkyMouseClicked(e);
                    }
                });
            }
            menuBar1.add(Objednavky);
            menuBar1.add(hSpacer1);
        }
        setJMenuBar(menuBar1);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}


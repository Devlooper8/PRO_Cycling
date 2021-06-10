package sk.tondrus.view;

import org.jdesktop.application.Application;
import sk.tondrus.AppZakaznik;
import sk.tondrus.RMI.ObchodDataListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;

/*
 * Created by JFormDesigner on Sun Mar 14 16:45:20 CET 2021
 */



/**
 * @author Tomáš Ondruš
 */
public class MainScreenZakaznik extends JFrame implements ObchodDataListener {

    /**
     * konstruktor
     */
    public MainScreenZakaznik() {
        initComponents();
    }



    private void MenuBicykleActionPerformed(ActionEvent e) {
        BorderLayout layout = (BorderLayout)getContentPane().getLayout();
        Component currContent=layout.getLayoutComponent(BorderLayout.CENTER);
        if (currContent != null) getContentPane().remove(currContent);
        Katalog katalog=new Katalog();
        getContentPane().add(katalog);
        getContentPane().revalidate();
        try {
            katalog.showKatalog(0);
        } catch (Exception e1) {
            e1.printStackTrace();
            JOptionPane.showMessageDialog(this,e1.getMessage(),"Chyba",JOptionPane.ERROR_MESSAGE);
            getContentPane().remove(katalog);
            getContentPane().add(currContent);
            getContentPane().revalidate();
        }
    }

    private void OdhlasitMouseClicked(MouseEvent e) {
        AppZakaznik appZakaznik = (AppZakaznik) Application.getInstance();
        appZakaznik.setStav(1);
        MainScreenZakaznik.this.dispose();
    }

    private void ObjednavkyMouseClicked(MouseEvent e) {
        TabulkaObjednavok tabulkaObjednavok= new TabulkaObjednavok();
        BorderLayout layout = (BorderLayout)getContentPane().getLayout();
        Component currContent=layout.getLayoutComponent(BorderLayout.CENTER);
        if (currContent != null) getContentPane().remove(currContent);
        tabulkaObjednavok.fillTable();
        getContentPane().add(tabulkaObjednavok);
        getContentPane().revalidate();
    }

    /**
     * informuje pouzivatela ze sa zmenili data v databaze a mal by prekreslit data na obrazovke
     * @throws RemoteException
     */
    @Override
    public void NotifyUser() throws RemoteException {
        BorderLayout layout = (BorderLayout)getContentPane().getLayout();
        Component currContent=layout.getLayoutComponent(BorderLayout.CENTER);
        if (currContent != null && currContent instanceof ObchodDataListener ) {
            ((ObchodDataListener)currContent).NotifyUser();
        }

    }

    private void menuItem1ActionPerformed(ActionEvent e) {
        BorderLayout layout = (BorderLayout)getContentPane().getLayout();
        Component currContent=layout.getLayoutComponent(BorderLayout.CENTER);
        if (currContent != null) getContentPane().remove(currContent);
        Katalog katalog=new Katalog();
        getContentPane().add(katalog);
        getContentPane().revalidate();
        try {
            katalog.showKatalog(1);
        } catch (Exception e1) {
            e1.printStackTrace();
            JOptionPane.showMessageDialog(this,e1.getMessage(),"Chyba",JOptionPane.ERROR_MESSAGE);
            getContentPane().remove(katalog);
            getContentPane().add(currContent);
            getContentPane().revalidate();
        }
    }

    private void menuItem2ActionPerformed(ActionEvent e) {
        BorderLayout layout = (BorderLayout)getContentPane().getLayout();
        Component currContent=layout.getLayoutComponent(BorderLayout.CENTER);
        if (currContent != null) getContentPane().remove(currContent);
        Katalog katalog=new Katalog();
        getContentPane().add(katalog);
        getContentPane().revalidate();
        try {
            katalog.showKatalog(2);
        } catch (Exception e1) {
            e1.printStackTrace();
            JOptionPane.showMessageDialog(this,e1.getMessage(),"Chyba",JOptionPane.ERROR_MESSAGE);
            getContentPane().remove(katalog);
            getContentPane().add(currContent);
            getContentPane().revalidate();
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        menuBar1 = new JMenuBar();
        menu2 = new JMenu();
        MenuBicykle = new JMenuItem();
        menuItem1 = new JMenuItem();
        menuItem2 = new JMenuItem();
        hSpacer1 = new JPanel(null);
        Objednavky = new JMenu();
        Odhlasit = new JMenu();
        Intro = new JPanel();

        //======== this ========
        setMinimumSize(new Dimension(600, 400));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== menuBar1 ========
        {
            menuBar1.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            menuBar1.setOpaque(false);

            //======== menu2 ========
            {
                menu2.setText("Katalog");
                menu2.setPreferredSize(new Dimension(65, 21));
                menu2.setHorizontalTextPosition(SwingConstants.CENTER);
                menu2.setFont(menu2.getFont().deriveFont(menu2.getFont().getSize() + 4f));
                menu2.setMaximumSize(new Dimension(70, 32767));
                menu2.setMinimumSize(new Dimension(70, 21));

                //---- MenuBicykle ----
                MenuBicykle.setText("Cestn\u00e9 bicykle");
                MenuBicykle.setFont(MenuBicykle.getFont().deriveFont(MenuBicykle.getFont().getSize() + 3f));
                MenuBicykle.addActionListener(e -> MenuBicykleActionPerformed(e));
                menu2.add(MenuBicykle);

                //---- menuItem1 ----
                menuItem1.setText("Horsk\u00e9 bicykle");
                menuItem1.setFont(menuItem1.getFont().deriveFont(menuItem1.getFont().getSize() + 3f));
                menuItem1.addActionListener(e -> menuItem1ActionPerformed(e));
                menu2.add(menuItem1);

                //---- menuItem2 ----
                menuItem2.setText("Zjazdov\u00e9 bicykle");
                menuItem2.setFont(menuItem2.getFont().deriveFont(menuItem2.getFont().getSize() + 3f));
                menuItem2.addActionListener(e -> menuItem2ActionPerformed(e));
                menu2.add(menuItem2);
            }
            menuBar1.add(menu2);
            menuBar1.add(hSpacer1);

            //======== Objednavky ========
            {
                Objednavky.setText("Objedn\u00e1vky");
                Objednavky.setRequestFocusEnabled(false);
                Objednavky.setFont(Objednavky.getFont().deriveFont(Objednavky.getFont().getSize() + 4f));
                Objednavky.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        ObjednavkyMouseClicked(e);
                    }
                });
            }
            menuBar1.add(Objednavky);

            //======== Odhlasit ========
            {
                Odhlasit.setText("Odhl\u00e1si\u0165");
                Odhlasit.setFont(Odhlasit.getFont().deriveFont(Odhlasit.getFont().getSize() + 4f));
                Odhlasit.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        OdhlasitMouseClicked(e);
                    }
                });
            }
            menuBar1.add(Odhlasit);
        }
        setJMenuBar(menuBar1);

        //======== Intro ========
        {
            Intro.setLayout(new BorderLayout());
        }
        contentPane.add(Intro, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JMenuBar menuBar1;
    private JMenu menu2;
    private JMenuItem MenuBicykle;
    private JMenuItem menuItem1;
    private JMenuItem menuItem2;
    private JPanel hSpacer1;
    private JMenu Objednavky;
    private JMenu Odhlasit;
    private JPanel Intro;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}

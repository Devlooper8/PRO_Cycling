package sk.tondrus.view;

import org.jdesktop.application.Application;
import sk.tondrus.AppZakaznik;
import sk.tondrus.Exceptions.UnautorizedAccess;
import sk.tondrus.databaza.databazaEntities.Objednavka;
import sk.tondrus.databaza.databazaEntities.Parts;
import sk.tondrus.databaza.databazaEntities.Product;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
/*
 * Created by JFormDesigner on Mon Apr 12 10:45:05 CEST 2021
 */



/**
 * @author Tomas Ondrus
 */
public class ProduktDetail extends JDialog  {
    private int productID;
    public ProduktDetail() {
        initComponents();

    }

    /**
     * konstruktor
     */
    public ProduktDetail(int productID){
        this();
        this.productID=productID;
    }

    private void thisWindowOpened(WindowEvent e) {
        AppZakaznik appZakaznik= (AppZakaznik) Application.getInstance();
        try {
            Product product= appZakaznik.getObchod().getProductDetail(appZakaznik.getUserID(),productID);

            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(product.getObrazok()));
                label1.setIcon(new ImageIcon(bufferedImage));
                label1.repaint();
                label2.setText(product.getPopis());
                String popis ="<html><body><table>" ;
                         for(Parts part:  product.getParts()){
                             popis+="<tr><td><b>"+part.getPartType()+"</b></td><td>"+part.getPartVariant()+"</td></tr>";
                         }
                        popis+="</table></body></html>";
                         label3.setText(popis);
                pack();


              


        } catch (RemoteException remoteException) {
            remoteException.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (UnautorizedAccess unautorizedAccess) {
            unautorizedAccess.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }


    }
    

    private void ObjednatActionPerformed(ActionEvent e) {
        AppZakaznik appZakaznik= (AppZakaznik) Application.getInstance();
        try {
            productID= appZakaznik.getObchod().objednaj(appZakaznik.getUserID(), new Objednavka(productID,appZakaznik.getUserID()));
        } catch (RemoteException remoteException) {
            label3.setText("Chyba servera.");
        } catch (SQLException throwables) {
            label3.setText("Nepodarilo sa vytvorit uzivatela.");
        } catch (UnautorizedAccess unautorizedAccess) {
            unautorizedAccess.printStackTrace();
        }
        dispose();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        label1 = new JLabel();
        label2 = new JLabel();
        label3 = new JLabel();
        Objednat = new JButton();

        //======== this ========
        setResizable(false);
        setModal(true);
        setMinimumSize(new Dimension(80, 353));
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                thisWindowOpened(e);
            }
        });
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());
        ((GridBagLayout)contentPane.getLayout()).columnWidths = new int[] {5, 262, 102, 0};
        ((GridBagLayout)contentPane.getLayout()).rowHeights = new int[] {50, 0, 0, 5, 0};
        ((GridBagLayout)contentPane.getLayout()).columnWeights = new double[] {0.0, 0.0, 1.0, 1.0E-4};
        ((GridBagLayout)contentPane.getLayout()).rowWeights = new double[] {0.0, 1.0, 0.0, 0.0, 1.0E-4};

        //---- label1 ----
        label1.setIcon(null);
        label1.setPreferredSize(new Dimension(500, 353));
        label1.setMinimumSize(new Dimension(500, 353));
        label1.setMaximumSize(new Dimension(500, 353));
        contentPane.add(label1, new GridBagConstraints(0, 0, 2, 3, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0), 0, 0));

        //---- label2 ----
        label2.setVerticalTextPosition(SwingConstants.TOP);
        label2.setVerticalAlignment(SwingConstants.TOP);
        contentPane.add(label2, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
            new Insets(0, 0, 0, 0), 0, 0));

        //---- label3 ----
        label3.setVerticalAlignment(SwingConstants.TOP);
        contentPane.add(label3, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0), 0, 0));

        //---- Objednat ----
        Objednat.setText("Objedna\u0165");
        Objednat.setHorizontalTextPosition(SwingConstants.CENTER);
        Objednat.addActionListener(e -> ObjednatActionPerformed(e));
        contentPane.add(Objednat, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
            GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
            new Insets(0, 0, 0, 0), 0, 0));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JButton Objednat;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}

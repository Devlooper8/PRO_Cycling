package sk.tondrus.view;

import org.jdesktop.swingx.border.DropShadowBorder;
import sk.tondrus.databaza.databazaEntities.Product;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
/*
 * Created by JFormDesigner on Sun Apr 11 10:36:40 CEST 2021
 */


/**
 * @author Tomas Ondrus
 */
public class ProduktInfo extends JPanel {
    int ProduktId;

    public ProduktInfo() {
        initComponents();
    }


    private void produktMouseClicked(MouseEvent e) {
        ProduktDetail produktDetail = new ProduktDetail(ProduktId);
        produktDetail.setVisible(true);

    }

    /**
     * Vypise info o produkte
     */
    public ProduktInfo(Product product) {
        this();
        BufferedImage bufferedImage = null;
        try {
            ProduktId = product.getId();
            bufferedImage = ImageIO.read(new ByteArrayInputStream(product.getNahlad()));
            prvok.setIcon(new ImageIcon(bufferedImage));
            popis.setText(product.getPopis());
            cena.setText(new String(Float.toString(product.getCena()) + "€"));
            //  prvok.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        prvok = new JLabel();
        popis = new JLabel();
        cena = new JLabel();

        //======== this ========
        setPreferredSize(new Dimension(283, 180));
        setMinimumSize(new Dimension(283, 180));
        setBorder(new DropShadowBorder());
        setLayout(new BorderLayout(5, 5));

        //---- prvok ----
        prvok.setIcon(new ImageIcon(getClass().getResource("/cestny1 50x100.jpg")));
        prvok.setHorizontalTextPosition(SwingConstants.CENTER);
        prvok.setVerticalTextPosition(SwingConstants.BOTTOM);
        prvok.setHorizontalAlignment(SwingConstants.CENTER);
        prvok.setPreferredSize(new Dimension(283, 180));
        prvok.setMaximumSize(new Dimension(283, 180));
        prvok.setMinimumSize(new Dimension(283, 180));
        prvok.setText(null);
        prvok.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        prvok.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                produktMouseClicked(e);
            }
        });
        add(prvok, BorderLayout.CENTER);
        add(popis, BorderLayout.SOUTH);

        //---- cena ----
        cena.setHorizontalTextPosition(SwingConstants.CENTER);
        cena.setHorizontalAlignment(SwingConstants.CENTER);
        add(cena, BorderLayout.NORTH);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel prvok;
    private JLabel popis;
    private JLabel cena;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}

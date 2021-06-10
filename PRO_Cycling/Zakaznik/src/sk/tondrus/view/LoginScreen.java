package sk.tondrus.view;

import org.jdesktop.application.Application;
import sk.tondrus.AppZakaznik;
import sk.tondrus.Exceptions.InvalidUserException;
import sk.tondrus.Exceptions.UserAlreadyExist;
import sk.tondrus.Exceptions.WrongPasswordException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.sql.SQLException;
/*
 * Created by JFormDesigner on Wed Mar 17 13:29:38 CET 2021
 */


/**
 * @author Tomáš Ondruš
 */
public class LoginScreen extends JDialog {
    private int userID = 0;

    public LoginScreen() {
        initComponents();
    }

    private void LoginActionPerformed(ActionEvent e) {
        AppZakaznik appZakaznik = (AppZakaznik) Application.getInstance();
        String email = textField1.getText();
        String password = new String(passwordField1.getPassword());
        try {
            userID = appZakaznik.getObchod().login(email, password);
            if (userID != 0) {
                System.out.println(userID);
                dispose();
            }

        } catch (Exception error) {
            if (error instanceof RemoteException)
                label3.setText("Chyba servera.");
            else if (error instanceof WrongPasswordException)
                label3.setText("Zle heslo.");
            else if (error instanceof InvalidUserException)
                label3.setText("Pouzivatel neexistuje.");
        }
    }


    public int getUserID() {

        return userID;
    }


    private void RegisterActionPerformed(ActionEvent e) {
        String email = textField1.getText();
        String password = new String(passwordField1.getPassword());
        AppZakaznik appZakaznik = (AppZakaznik) Application.getInstance();
        try {
            userID = appZakaznik.getObchod().register(email, password);
            dispose();
        } catch (RemoteException remoteException) {
            label3.setText("Chyba servera.");
        } catch (SQLException throwables) {
            label3.setText("Nepodarilo sa vytvorit uzivatela.");
        } catch (UserAlreadyExist userAlreadyExist) {
            userAlreadyExist.printStackTrace();
            label3.setText("Uzivatel uz existuje.");
        }

    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        label1 = new JLabel();
        textField1 = new JTextField();
        label2 = new JLabel();
        passwordField1 = new JPasswordField();
        Login = new JButton();
        Register = new JButton();
        label3 = new JLabel();

        //======== this ========
        setMinimumSize(new Dimension(450, 300));
        setResizable(false);
        setModal(true);
        Container contentPane = getContentPane();

        //---- label1 ----
        label1.setText("Email:");
        label1.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        //---- textField1 ----
        textField1.setText("devloopers8@gmail.com");

        //---- label2 ----
        label2.setText("Password:");
        label2.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        //---- passwordField1 ----
        passwordField1.setText("1234");

        //---- Login ----
        Login.setText("Login");
        Login.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        Login.addActionListener(e -> LoginActionPerformed(e));

        //---- Register ----
        Register.setText("Register");
        Register.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        Register.addActionListener(e -> RegisterActionPerformed(e));

        //---- label3 ----
        label3.setHorizontalAlignment(SwingConstants.CENTER);

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addContainerGap(38, Short.MAX_VALUE)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                                                .addComponent(Login, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(Register, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addComponent(label1)
                                                .addGap(18, 18, 18)
                                                .addComponent(textField1, GroupLayout.PREFERRED_SIZE, 189, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addComponent(label2)
                                                .addGap(18, 18, 18)
                                                .addGroup(contentPaneLayout.createParallelGroup()
                                                        .addComponent(passwordField1, GroupLayout.PREFERRED_SIZE, 189, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(label3, GroupLayout.PREFERRED_SIZE, 111, GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap(61, Short.MAX_VALUE))
        );
        contentPaneLayout.linkSize(SwingConstants.HORIZONTAL, new Component[]{label1, label2});
        contentPaneLayout.linkSize(SwingConstants.HORIZONTAL, new Component[]{passwordField1, textField1});
        contentPaneLayout.linkSize(SwingConstants.HORIZONTAL, new Component[]{Login, Register});
        contentPaneLayout.setVerticalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addContainerGap(49, Short.MAX_VALUE)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(label1)
                                        .addComponent(textField1, 0, 0, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(label2)
                                        .addComponent(passwordField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(label3, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
                                .addGap(19, 19, 19)
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addComponent(Register)
                                        .addComponent(Login))
                                .addContainerGap(49, Short.MAX_VALUE))
        );
        contentPaneLayout.linkSize(SwingConstants.VERTICAL, new Component[]{Login, Register});
        contentPaneLayout.linkSize(SwingConstants.VERTICAL, new Component[]{passwordField1, textField1});
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel label1;
    private JTextField textField1;
    private JLabel label2;
    private JPasswordField passwordField1;
    private JButton Login;
    private JButton Register;
    private JLabel label3;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}

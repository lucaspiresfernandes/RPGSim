package com.rpgsim.client;

import com.rpgsim.client.util.ClientConfigurations;
import com.rpgsim.common.Account;
import com.rpgsim.common.ConnectionType;
import java.awt.CardLayout;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class MainFrame extends javax.swing.JFrame
{
    private final CardLayout c;
    private ClientConfigurations clientConfig;
    
    public MainFrame()
    {
        initComponents();
        c = (CardLayout) pnlCards.getLayout();
        try
        {
            clientConfig = new ClientConfigurations();
        }
        catch (IOException ex)
        {
            JOptionPane.showMessageDialog(null, "Could not load options.");
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            dispose();
            return;
        }
        internalInit();
    }
    
    private void internalInit()
    {
        boolean saveLogin = clientConfig.getBooleanProperty("SaveUsername");
        chkLogin.setSelected(saveLogin);
        if (saveLogin)
            txtLogin.setText(clientConfig.getProperty("Username"));
        
        boolean savePassword = clientConfig.getBooleanProperty("SavePassword");
        chkPassword.setSelected(savePassword);
        if (savePassword)
            txtPassword.setText(clientConfig.getProperty("Password"));
        
        txtIP.setText(clientConfig.getProperty("IP"));
        txtTCP.setText(clientConfig.getProperty("TCPPort"));
        txtUDP.setText(clientConfig.getProperty("UDPPort"));
        
        btnLogin.addActionListener(l -> 
        {
            c.show(pnlCards, "login");
        });
        
        btnRegister.addActionListener(l -> 
        {
            c.show(pnlCards, "register");
        });
        
        btnOptions.addActionListener(l -> 
        {
            c.show(pnlCards, "options");
        });
        
        btnExit.addActionListener(l ->
        {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        });
        
        btnConfirm.addActionListener(l -> login());
        btnRegisterConfirm.addActionListener(l -> register());
    }
    
    private void login()
    {
        String username = txtLogin.getText();
        String password = new String(txtPassword.getPassword());
        
        if (username.isEmpty() || password.isEmpty())
        {
            JOptionPane.showMessageDialog(null, "Login or password are blank.");
            return;
        }
        connect(username, password, ConnectionType.LOGIN);
    }
    
    private void register()
    {
        String username = txtRegisterLogin.getText();
        String password = new String(txtRegisterPassword.getPassword());
        String cPassword = new String(txtRegisterConfirmPassword.getPassword());
        
        if (username.isEmpty() || password.isEmpty() || cPassword.isEmpty())
        {
            JOptionPane.showMessageDialog(null, "Login, password or confirmation password are blank.");
            return;
        }
        else if(!password.equals(cPassword))
        {
            JOptionPane.showMessageDialog(null, "The passwords does not match.");
            return;
        }
        connect(username, password, ConnectionType.REGISTER);
    }
    
    private void connect(String username, String password, ConnectionType type)
    {
        ClientManager manager = new ClientManager(this, clientConfig, new Account(username, password));
        try
        {
            manager.start(type);
        }
        catch (IOException ex)
        {
            manager.stop();
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "The server seems to be down.");
        }
        
        boolean saveUsername = chkLogin.isSelected();
        boolean savePassword = chkPassword.isSelected();
        
        clientConfig.setProperty("SaveUsername", saveUsername + "");
        if (saveUsername)
            clientConfig.setProperty("Username", username);
        else
            clientConfig.setProperty("Username", "null");
        clientConfig.setProperty("SavePassword", savePassword + "");
        if (savePassword)
            clientConfig.setProperty("Password", password);
        else
            clientConfig.setProperty("Password", "null");
        clientConfig.setProperty("IP", txtIP.getText());
        clientConfig.setProperty("TCPPort", txtTCP.getText());
        clientConfig.setProperty("UDPPort", txtUDP.getText());
        
        try
        {
            clientConfig.saveConfigurations();
        }
        catch (IOException ex)
        {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlCards = new javax.swing.JPanel();
        pnlLogin = new javax.swing.JPanel();
        lblLogin = new javax.swing.JLabel();
        lblPassword = new javax.swing.JLabel();
        txtLogin = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        chkLogin = new javax.swing.JCheckBox();
        chkPassword = new javax.swing.JCheckBox();
        btnConfirm = new javax.swing.JButton();
        pnlRegister = new javax.swing.JPanel();
        lblRegisterLogin = new javax.swing.JLabel();
        lblRegisterPassword = new javax.swing.JLabel();
        txtRegisterLogin = new javax.swing.JTextField();
        txtRegisterPassword = new javax.swing.JPasswordField();
        btnRegisterConfirm = new javax.swing.JButton();
        txtRegisterConfirmPassword = new javax.swing.JPasswordField();
        lblRegisterConfirmPassword = new javax.swing.JLabel();
        pnlOptions = new javax.swing.JPanel();
        lblIP = new javax.swing.JLabel();
        txtIP = new javax.swing.JTextField();
        lblTCP = new javax.swing.JLabel();
        txtTCP = new javax.swing.JTextField();
        lblUDP = new javax.swing.JLabel();
        txtUDP = new javax.swing.JTextField();
        btnLogin = new javax.swing.JButton();
        btnRegister = new javax.swing.JButton();
        btnOptions = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("RPG Simulator");
        setResizable(false);

        pnlCards.setLayout(new java.awt.CardLayout());

        lblLogin.setText("Login:");

        lblPassword.setText("Password:");

        chkLogin.setText("Remember Login");

        chkPassword.setText("Remember Password");

        btnConfirm.setText("Confirm");

        javax.swing.GroupLayout pnlLoginLayout = new javax.swing.GroupLayout(pnlLogin);
        pnlLogin.setLayout(pnlLoginLayout);
        pnlLoginLayout.setHorizontalGroup(
            pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLoginLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblLogin)
                    .addComponent(lblPassword))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkPassword)
                    .addGroup(pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(chkLogin)
                        .addComponent(txtLogin, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                        .addComponent(txtPassword)))
                .addContainerGap(131, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLoginLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlLoginLayout.setVerticalGroup(
            pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLoginLayout.createSequentialGroup()
                .addContainerGap(52, Short.MAX_VALUE)
                .addGroup(pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLogin)
                    .addComponent(txtLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkLogin)
                .addGap(46, 46, 46)
                .addGroup(pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPassword)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkPassword)
                .addGap(18, 18, 18)
                .addComponent(btnConfirm)
                .addContainerGap())
        );

        pnlCards.add(pnlLogin, "login");

        lblRegisterLogin.setText("Login:");

        lblRegisterPassword.setText("Password:");

        btnRegisterConfirm.setText("Confirm");

        lblRegisterConfirmPassword.setText("Confirm Password:");

        javax.swing.GroupLayout pnlRegisterLayout = new javax.swing.GroupLayout(pnlRegister);
        pnlRegister.setLayout(pnlRegisterLayout);
        pnlRegisterLayout.setHorizontalGroup(
            pnlRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRegisterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblRegisterLogin)
                    .addComponent(lblRegisterPassword)
                    .addComponent(lblRegisterConfirmPassword))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtRegisterLogin)
                    .addComponent(txtRegisterPassword)
                    .addComponent(txtRegisterConfirmPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(119, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlRegisterLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnRegisterConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlRegisterLayout.setVerticalGroup(
            pnlRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRegisterLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(pnlRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRegisterLogin)
                    .addComponent(txtRegisterLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46)
                .addGroup(pnlRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRegisterPassword)
                    .addComponent(txtRegisterPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46)
                .addGroup(pnlRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtRegisterConfirmPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRegisterConfirmPassword))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addComponent(btnRegisterConfirm)
                .addContainerGap())
        );

        pnlCards.add(pnlRegister, "register");

        lblIP.setText("IP:");

        lblTCP.setText("TCP Port:");

        lblUDP.setText("UDP Port");

        javax.swing.GroupLayout pnlOptionsLayout = new javax.swing.GroupLayout(pnlOptions);
        pnlOptions.setLayout(pnlOptionsLayout);
        pnlOptionsLayout.setHorizontalGroup(
            pnlOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOptionsLayout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addGroup(pnlOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlOptionsLayout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(lblIP)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtIP, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlOptionsLayout.createSequentialGroup()
                        .addGroup(pnlOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblUDP)
                            .addComponent(lblTCP))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTCP)
                            .addComponent(txtUDP, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(155, Short.MAX_VALUE))
        );
        pnlOptionsLayout.setVerticalGroup(
            pnlOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOptionsLayout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(pnlOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIP)
                    .addComponent(txtIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46)
                .addGroup(pnlOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTCP)
                    .addComponent(txtTCP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46)
                .addGroup(pnlOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblUDP)
                    .addComponent(txtUDP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(47, Short.MAX_VALUE))
        );

        pnlCards.add(pnlOptions, "options");

        btnLogin.setText("Login");

        btnRegister.setText("Register");

        btnOptions.setText("Options");

        btnExit.setText("Exit");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlCards, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(btnRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnOptions, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlCards, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLogin)
                    .addComponent(btnRegister)
                    .addComponent(btnOptions)
                    .addComponent(btnExit))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConfirm;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnOptions;
    private javax.swing.JButton btnRegister;
    private javax.swing.JButton btnRegisterConfirm;
    private javax.swing.JCheckBox chkLogin;
    private javax.swing.JCheckBox chkPassword;
    private javax.swing.JLabel lblIP;
    private javax.swing.JLabel lblLogin;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblRegisterConfirmPassword;
    private javax.swing.JLabel lblRegisterLogin;
    private javax.swing.JLabel lblRegisterPassword;
    private javax.swing.JLabel lblTCP;
    private javax.swing.JLabel lblUDP;
    private javax.swing.JPanel pnlCards;
    private javax.swing.JPanel pnlLogin;
    private javax.swing.JPanel pnlOptions;
    private javax.swing.JPanel pnlRegister;
    private javax.swing.JTextField txtIP;
    private javax.swing.JTextField txtLogin;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JPasswordField txtRegisterConfirmPassword;
    private javax.swing.JTextField txtRegisterLogin;
    private javax.swing.JPasswordField txtRegisterPassword;
    private javax.swing.JTextField txtTCP;
    private javax.swing.JTextField txtUDP;
    // End of variables declaration//GEN-END:variables

}

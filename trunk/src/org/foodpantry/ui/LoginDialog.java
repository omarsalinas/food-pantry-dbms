package org.foodpantry.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import org.foodpantry.db.DBConnection;

/**
 * Borrowed from:
 * http://www.javaswing.org/jdialog-login-dialog-example.aspx
 * TODO need functionality for a database connection timeout
 */

public class LoginDialog extends JDialog {
	/**
	 * Generated Serial Version UID
	 */
	private static final long serialVersionUID = -2496684384580696427L;
	private JTextField tfUsername;
    private JPasswordField pfPassword;
    private JLabel lbUsername;
    private JLabel lbPassword;
    private JButton btnLogin;
    private JButton btnCancel;
    // Initialize these to false, for security
    private boolean succeeded = false;
    private boolean administrator = false;

    public LoginDialog(Frame parent) {
        super(parent, "Login", true);
        //
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();

        cs.fill = GridBagConstraints.HORIZONTAL;

        lbUsername = new JLabel("Username: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(lbUsername, cs);

        tfUsername = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(tfUsername, cs);

        lbPassword = new JLabel("Password: ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(lbPassword, cs);

        pfPassword = new JPasswordField(20);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(pfPassword, cs);
        panel.setBorder(new LineBorder(Color.GRAY));


        btnLogin = new JButton("Login");

        btnLogin.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
					if (Login.authenticate(getUsername(), getPassword())) {
					    JOptionPane.showMessageDialog(LoginDialog.this,
					            "Hi " + getUsername() + "! You have successfully logged in.",
					            "Login",
					            JOptionPane.INFORMATION_MESSAGE);
					    succeeded = true;
					    // TODO make this an actual setting...
					    String query = "SELECT Admin FROM C242386_foodpantry.Pantry_Security where User_Name = '" + getUsername() + "'";
				    	//get connection to database from MainUI
				    	DBConnection conn = MainUI.dbConnection;
				    	//execute user authentication query
			    		Statement stmt = conn.getDBConnection().createStatement();
						ResultSet rs = stmt.executeQuery(query);
						rs.next();
					    if (rs.getInt(1) == 1) {
					    	administrator = true;
					    }
					    dispose();
					} else {
					    JOptionPane.showMessageDialog(LoginDialog.this,
					            "Invalid username or password",
					            "Login",
					            JOptionPane.ERROR_MESSAGE);
					    // reset username and password
					    tfUsername.setText("");
					    pfPassword.setText("");
					    succeeded = false;

					}
				} catch (HeadlessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        JPanel bp = new JPanel();
        bp.add(btnLogin);
        bp.add(btnCancel);

        getRootPane().setDefaultButton(btnLogin);

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);

        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    public String getUsername() {
        return tfUsername.getText().trim();
    }

    public String getPassword() {
        return new String(pfPassword.getPassword());
    }

    public boolean isSucceeded() {
        return succeeded;
    }
    
    public boolean isAdministrator() {
    	return this.administrator;
    }
}

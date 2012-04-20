package org.foodpantry.ui;

/**
 * Borrowed from:
 * http://www.javaswing.org/jdialog-login-dialog-example.aspx
 */

public class Login {
    public static boolean authenticate(String username, String password) {
        // hardcoded username and password
        if (username.equals("bob") && password.equals("secret")) {
            return true;
        }
        return false;
    }
}

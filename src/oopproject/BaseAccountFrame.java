package oopproject;

import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public abstract class BaseAccountFrame extends JFrame implements ActionListener, PasswordManageable { //ABSTRACT PARENT CLASS OF AdminAccount AND UserAccount

    protected void changePassword() { //CHANGE PASSWORD FUNCTIONALITY SHARED BY ADMIN AND USER
        JPasswordField currentPass = new JPasswordField();
        JPasswordField newPass = new JPasswordField();
        JPasswordField confirmPass = new JPasswordField();
        Object[] fields = {"Current Password: ", currentPass, "New Password :", newPass,
            "Confirm New Password: ", confirmPass};
        int option = JOptionPane.showConfirmDialog(null, fields, "Change Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option != JOptionPane.OK_OPTION) {
            return;
        }
        //CHECK IF ANY FIELD IS EMPTY
        String current = String.valueOf(currentPass.getPassword());
        String newPassword = String.valueOf(newPass.getPassword());
        String confirm = String.valueOf(confirmPass.getPassword());
        if (current.isEmpty() || newPassword.isEmpty() || confirm.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in all fields.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        //CHECK IF THE ACCOUNT EXISTS
        if (!accountExists()) {
            JOptionPane.showMessageDialog(this, "User account not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        //CHECK IF THE CURRENT PASSWORD IS CORRECT
        if (!verifyCurrentPassword(current)) {
            JOptionPane.showMessageDialog(this, "Current password is incorrect.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        //CHECK IF THE NEW PASSWORDS MATCH
        if (!newPassword.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "New passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        //UPDATE THE PASSWORD (DIFFERENT FOR ADMIN AND USER)
        updatePassword(newPassword);
        JOptionPane.showMessageDialog(this, "Password changed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    } //CHANGE PASSWORD FUNCTIONALITY SHARED BY ADMIN AND USER
} //ABSTRACT PARENT CLASS OF AdminAccount AND UserAccount
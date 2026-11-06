package oopproject;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class AccountCreation extends JFrame implements ActionListener {

    //COMPONENTS THAT actionPerformed NEEDS TO READ
    private JTextField textUsername = new JTextField();
    private JPasswordField pass = new JPasswordField();
    private JPasswordField confirmPass = new JPasswordField();
    private JButton register = new JButton("Register");
    private JButton cancel = new JButton("Cancel");
    //VARIABLES THAT STORES MULTIPLE ACCOUNT
    private static ArrayList<String> usernamesList = new ArrayList<>();
    private static ArrayList<String> passwordsList = new ArrayList<>();
    private static final String HINT = "Hello World!";

    private static ArrayList<Integer> failedAttempts = new ArrayList<>();
    private static ArrayList<String> blockedUsernames = new ArrayList<>();

    private static ArrayList<String> userId = new ArrayList<>();
    private static Random random = new Random();

    public AccountCreation() { //CONSTRUCTOR, WILL IMPLEMENT EVERY TIME IT IS CALLED
        //JFRAME DESIGN
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setTitle("Budget Tracker System");
        Image appIcon = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("Image/logo.png"));
        this.setIconImage(appIcon);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        //THE BLUE PART IS THE SAME ONE THE LOG-IN FORM USES
        JPanel p1Display = new JPanel();
        LogInForm.panelDisplay(p1Display);
        this.add(p1Display);
        panelDisplay();
    } //CONSTRUCTOR, WILL IMPLEMENT EVERY TIME IT IS CALLED

    static { //RUNS ONCE WHEN THE CLASS LOADS: ADMIN (SO "Admin" CAN NEVER BE REGISTERED) AND THE DEFAULT USER ACCOUNT
        usernamesList.add(LogInForm.getUserAdmin());
        passwordsList.add(LogInForm.getPassAdmin());
        userId.add(generateUserId());
        failedAttempts.add(0);
        usernamesList.add("Tipida");
        passwordsList.add("pass");
        userId.add(generateUserId());
        failedAttempts.add(0);
        TransactionInfo.initializeDefaultData();
    } //RUNS ONCE WHEN THE CLASS LOADS: ADMIN (SO "Admin" CAN NEVER BE REGISTERED) AND THE DEFAULT USER ACCOUNT

    public void panelDisplay() { //MODIFICATION OF ALL THE DESIGN IN PANEL2 (WHITE PART)
        //PANEL USED IN PANEL2
        JPanel p2 = new JPanel();
        p2.setBounds(750, 0, 800, 800);
        p2.setBackground(Color.white);
        p2.setLayout(null);
        this.add(p2);
        //WELCOME
        JLabel body1 = new JLabel("WELCOME");
        body1.setBounds(140, 110, 150, 20);
        body1.setFont(new Font("Sans-Serif", Font.PLAIN, 20));
        body1.setForeground(new Color(30, 58, 138));
        p2.add(body1);
        //Create your own account
        JLabel body2 = new JLabel("Create your own account");
        body2.setBounds(140, 140, 700, 50);
        body2.setFont(new Font("Serif", Font.BOLD, 40));
        body2.setForeground(Color.black);
        p2.add(body2);
        //Your budgets, goals, and activity are waiting for you.
        JLabel body3 = new JLabel("Your budgets, goals, and activity are waiting for you.");
        body3.setBounds(140, 200, 600, 30);
        body3.setFont(new Font("Sans-Serif", Font.PLAIN, 20));
        body3.setForeground(Color.gray);
        p2.add(body3);
        //USERNAME LABEL
        JLabel username = new JLabel("Username");
        username.setBounds(140, 280, 150, 20);
        username.setFont(new Font("Sans-Serif", Font.PLAIN, 20));
        username.setForeground(Color.darkGray);
        p2.add(username);
        //USERNAME TEXTFIELD
        textUsername.setBounds(140, 315, 500, 40);
        textUsername.setFont(new Font("Sans-Serif", Font.PLAIN, 20));
        textUsername.setForeground(Color.darkGray);
        p2.add(textUsername);
        //PASSWORD LABEL
        JLabel password = new JLabel("Password");
        password.setBounds(140, 380, 150, 20);
        password.setFont(new Font("Sans-Serif", Font.PLAIN, 20));
        password.setForeground(Color.darkGray);
        p2.add(password);
        //PASSWORD PASSWORDFIELD
        pass.setBounds(140, 415, 500, 40);
        pass.setFont(new Font("Sans-Serif", Font.PLAIN, 20));
        pass.setForeground(Color.darkGray);
        p2.add(pass);
        //CONFIRM PASSWORD LABEL
        JLabel confirmPassword = new JLabel("Confirm Password");
        confirmPassword.setBounds(140, 480, 200, 20);
        confirmPassword.setFont(new Font("Sans-Serif", Font.PLAIN, 20));
        confirmPassword.setForeground(Color.darkGray);
        p2.add(confirmPassword);
        //CONFIRM PASSWORD PASSWORDFIELD
        confirmPass.setBounds(140, 515, 500, 40);
        confirmPass.setFont(new Font("Sans-Serif", Font.PLAIN, 20));
        confirmPass.setForeground(Color.darkGray);
        p2.add(confirmPass);
        //REGISTER BUTTON
        register.setBounds(190, 585, 200, 40);
        register.setFont(new Font("Sans-Serif", Font.BOLD, 17));
        register.setForeground(Color.white);
        register.setBackground(new Color(30, 58, 138));
        p2.add(register);
        register.addActionListener(this);
        //CANCEL BUTTON
        cancel.setBounds(400, 585, 200, 40);
        cancel.setFont(new Font("Sans-Serif", Font.BOLD, 17));
        cancel.setForeground(Color.white);
        cancel.setBackground(new Color(30, 58, 138));
        p2.add(cancel);
        cancel.addActionListener(this);
        //Personal money, made clear.
        JLabel body4 = new JLabel("Personal money, made clear.");
        body4.setBounds(305, 650, 500, 30);
        body4.setFont(new Font("Serif", Font.ITALIC, 15));
        body4.setForeground(Color.gray);
        p2.add(body4);
    } //MODIFICATION OF ALL THE DESIGN IN PANEL2 (WHITE PART)

    public static ArrayList<String> getUsernamesList() {
        return usernamesList;
    } //TO ACCESS ALL THE USERNAME ACCOUNT

    public static ArrayList<String> getPasswordsList() {
        return passwordsList;
    } //TO ACCESS ALL THE PASSWORD ACCOUNT

    public static String getHint() {
        return HINT;
    }//TO ACCES THE ADMIN HINT

    public static ArrayList<Integer> getFailedAttempts() {
        return failedAttempts;
    } //TO ACCESS ALL THE FAILED ATTEMPTS

    public static ArrayList<String> getBlockedUsernames() {
        return blockedUsernames;
    } //TO ACCESS ALL THE BLOCKED USERNAMES

    public static ArrayList<String> getUserId() {
        return userId;
    } //TO ACCESS ALL THE USER ID

    public static String generateUserId() {//GENERATES USER ID
        String id;
        do {
            id = "TU-" + (1000 + random.nextInt(1000));
        } while (userId.contains(id));
        return id;
    } //GENERATES USER ID

    @Override
    public void actionPerformed(ActionEvent e) { //FUNCTIONS OF ALL THE BUTTON
        try {
            if (e.getSource().equals(register)) { //REGISTER BUTTON
                String newUsername = textUsername.getText().trim();
                String newPassword = String.valueOf(pass.getPassword());
                //VALIDATE EMPTY FIELDS
                if (newUsername.isEmpty() || newPassword.isEmpty() || String.valueOf(confirmPass.getPassword()).isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please input the required text", "Warning Message", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                //CHECK IF USERNAME ALREADY EXISTS
                if (usernamesList.contains(newUsername)) {
                    JOptionPane.showMessageDialog(null, "Username already exists!", "Error Message", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                //CHECK IF PASSWORDS MATCH
                if (!newPassword.equals(String.valueOf(confirmPass.getPassword()))) {
                    JOptionPane.showMessageDialog(null, "Password did not match!", "Error Message", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                //ADD CONFIRMATION DIALOG BEFORE REGISTERING
                int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to create this account?",
                        "Confirmation", JOptionPane
                                .YES_NO_OPTION);
                //PROCEED IF USER CLICKS "YES"
                if (confirmation == JOptionPane.YES_OPTION) {
                    usernamesList.add(newUsername);
                    passwordsList.add(newPassword);
                    userId.add(generateUserId());
                    failedAttempts.add(0);
                    JOptionPane.showMessageDialog(null, "Account Successfully Created", "Message", JOptionPane.INFORMATION_MESSAGE);
                    LogInForm login = new LogInForm();
                    login.setVisible(true);
                    this.dispose();
                }
            } else if (e.getSource().equals(cancel)) { //CANCEL BUTTON
                LogInForm login = new LogInForm();
                login.setVisible(true);
                this.dispose();
            }
        } catch (Exception handling) {
            JOptionPane.showMessageDialog(null, "An error occurred during registration.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } //FUNCTIONS OF ALL THE BUTTON 
    //(NOTE: TO MAKE THE BUTTON WORKABLE ADD .addActionListener(), EXAMPLE: button.addActionListener(this);
}
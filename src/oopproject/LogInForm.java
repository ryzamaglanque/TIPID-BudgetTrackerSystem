package oopproject;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LogInForm extends JFrame implements ActionListener {

    //COMPONENTS THAT actionPerformed NEEDS TO READ
    JTextField textUsername = new JTextField();
    JPasswordField pass = new JPasswordField();
    JCheckBox showPass = new JCheckBox("Show Password");
    JButton login = new JButton("Log-in");
    JButton createAccount = new JButton("Create an account");
    //ADMIN USERNAME & PASSWORD
    private static final String USER_ADMIN = "Admin";
    private static String PASS_ADMIN = "password";
    //TO ACCESS THE USERNAME OF THE USER WHO IS CURRENTLY LOGGED IN; WILL BE USE INSIDE OF EVERY ACCOUNT
    private static String loggedInUsername;

    public LogInForm() { //CONSTRUCTOR, WILL IMPLEMENT EVERY TIME IT IS CALLED
        //JFRAME DESIGN
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setTitle("Budget Tracker System");
        Image appIcon = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("Image/logo.png"));
        this.setIconImage(appIcon);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        //DISPLAY ALL THE DESIGN/DISPLAY METHOD WE MADE
        JPanel p1 = new JPanel();
        panelDisplay(p1);
        this.add(p1);
        panelDisplay();
    } //CONSTRUCTOR, WILL IMPLEMENT EVERY TIME IT IS CALLED

    public static void panelDisplay(JPanel p1) { //MODIFICATION OF ALL THE DESIGN IN PANEL1(BLUE PART) WILL ALSO BE USE IN class AccountCreation
        //PANEL USED IN PANEL1
        p1.setBounds(0, 0, 750, 1000);
        p1.setBackground(new Color(30, 58, 138));
        p1.setLayout(null);
        //LOGO
        JLabel logoLabel = new JLabel();
        logoLabel.setBounds(30, 60, 110, 80);
        Image logo = new ImageIcon(LogInForm.class.getResource("/Image/logo.png")).getImage();
        logoLabel.setIcon(new ImageIcon(logo.getScaledInstance(110, 80, Image.SCALE_SMOOTH)));
        p1.add(logoLabel);
        //T.I.P.I.D
        JLabel title = new JLabel("T.I.P.I.D");
        title.setBounds(150, 70, 150, 40);
        title.setFont(new Font("Sans-Serif", Font.BOLD, 40));
        title.setForeground(Color.white);
        p1.add(title);
        //Tracking Income, Purchases, and Individual Daily expenses
        JLabel body1 = new JLabel("Tracking Income, Purchases, and Individual Daily expenses");
        body1.setBounds(150, 110, 500, 30);
        body1.setFont(new Font("Sans-Serif", Font.PLAIN, 17));
        body1.setForeground(Color.white);
        p1.add(body1);
        //YOUR MONTH AT A GLANCE
        JLabel body2 = new JLabel("YOUR MONTH AT A GLANCE");
        body2.setBounds(30, 180, 350, 25);
        body2.setFont(new Font("Sans-Serif", Font.PLAIN, 20));
        body2.setForeground(Color.white);
        p1.add(body2);
        //Build better habits,
        JLabel body3 = new JLabel("Build better habits,");
        body3.setBounds(30, 230, 600, 70);
        body3.setFont(new Font("Serif", Font.BOLD, 70));
        body3.setForeground(Color.white);
        p1.add(body3);
        //one budget at a time.
        JLabel body4 = new JLabel("one budget at a time.");
        body4.setBounds(30, 300, 650, 90);
        body4.setFont(new Font("Serif", Font.BOLD, 70));
        body4.setForeground(Color.white);
        p1.add(body4);
        //Plan your spending, follow every category, and keep your
        JLabel body5 = new JLabel("Plan your spending, follow every category, and keep your");
        body5.setBounds(30, 410, 600, 25);
        body5.setFont(new Font("Sans-Serif", Font.PLAIN, 20));
        body5.setForeground(Color.white);
        p1.add(body5);
        //financial goals moving forward.
        JLabel body6 = new JLabel("financial goals moving forward.");
        body6.setBounds(30, 435, 600, 30);
        body6.setFont(new Font("Sans-Serif", Font.PLAIN, 20));
        body6.setForeground(Color.white);
        p1.add(body6);
        //PICTURE BELOW THE BLUE PART
        JLabel pictureLabel = new JLabel();
        pictureLabel.setBounds(30, 490, 600, 200);
        Image picture = new ImageIcon(LogInForm.class.getResource("/Image/logInDesign.jpeg")).getImage();
        pictureLabel.setIcon(new ImageIcon(picture.getScaledInstance(600, 200, Image.SCALE_SMOOTH)));
        p1.add(pictureLabel);
        //Simple tracking. Thoughtful spending.
        JLabel body7 = new JLabel("Simple tracking. Thoughtful spending.");
        body7.setBounds(30, 710, 500, 30);
        body7.setFont(new Font("Serif", Font.ITALIC, 20));
        body7.setForeground(Color.white);
        p1.add(body7);
    } //MODIFICATION OF ALL THE DESIGN IN PANEL1(BLUE PART) WILL ALSO BE USE IN class AccountCreation

    public void panelDisplay() { //MODIFICATION OF ALL THE DESIGN IN PANEL2 (WHITE PART)
        //PANEL USED IN PANEL2
        JPanel p2 = new JPanel();
        p2.setBounds(750, 0, 1150, 1000);
        p2.setBackground(Color.white);
        p2.setLayout(null);
        this.add(p2);
        //WELCOME
        JLabel body8 = new JLabel("WELCOME");
        body8.setBounds(140, 110, 150, 20);
        body8.setFont(new Font("Sans-Serif", Font.PLAIN, 20));
        body8.setForeground(new Color(30, 58, 138));
        p2.add(body8);
        //Log-in to your account
        JLabel body9 = new JLabel("Log-in to your account");
        body9.setBounds(140, 140, 700, 50);
        body9.setFont(new Font("Serif", Font.BOLD, 40));
        body9.setForeground(Color.black);
        p2.add(body9);
        //Your budgets, goals, and activity are waiting for you.
        JLabel body10 = new JLabel("Your budgets, goals, and activity are waiting for you.");
        body10.setBounds(140, 200, 600, 30);
        body10.setFont(new Font("Sans-Serif", Font.PLAIN, 20));
        body10.setForeground(Color.gray);
        p2.add(body10);
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
        //SHOW PASSWORD CHECKBOX
        showPass.setBounds(140, 470, 150, 30);
        showPass.setFont(new Font("Sans-Serif", Font.PLAIN, 15));
        showPass.setBackground(Color.white);
        p2.add(showPass);
        showPass.addActionListener(this);
        //LOGIN BUTTON
        login.setBounds(140, 520, 500, 40);
        login.setFont(new Font("Sans-Serif", Font.BOLD, 17));
        login.setForeground(Color.white);
        login.setBackground(new Color(30, 58, 138));
        p2.add(login);
        login.addActionListener(this);
        //New to T.I.P.I.D?
        JLabel body11 = new JLabel("New to T.I.P.I.D?");
        body11.setBounds(320, 590, 600, 30);
        body11.setFont(new Font("Sans-Serif", Font.PLAIN, 18));
        body11.setForeground(Color.gray);
        p2.add(body11);
        //CREATE ACCOUNT BUTTON
        createAccount.setBounds(265, 615, 250, 40);
        createAccount.setFont(new Font("Sans-Serif", Font.BOLD, 19));
        createAccount.setForeground(new Color(30, 58, 138));
        createAccount.setBackground(Color.white);
        createAccount.setBorderPainted(false);
        p2.add(createAccount);
        createAccount.addActionListener(this);
    } //MODIFICATION OF ALL THE DESIGN IN PANEL2 (WHITE PART)

    public static String getUserAdmin() {
        return USER_ADMIN;
    } //TO ACCESS ADMIN USERNAME

    public static String getPassAdmin() {
        return PASS_ADMIN;
    } //TO ACCESS ADMIN PASSWORD

    public static void setPASS_ADMIN(String PASS_ADMIN) {
        LogInForm.PASS_ADMIN = PASS_ADMIN;
    } //TO SET PASSWORD IF ADMIN CLICK THE CHANGE PASSWORD

    public static String getLoggedInUsername() {
        return loggedInUsername;
    } //TO ACCESS THE USERNAME OF THE USER WHO IS CURRENTLY LOGGED IN

    @Override
    public void actionPerformed(ActionEvent e) { //FUNCTIONS OF ALL THE BUTTON
        try {
            if (e.getSource().equals(login)) { //LOGIN BUTTON
                String enteredUser = textUsername.getText().trim();
                String enteredPass = String.valueOf(pass.getPassword());
                //VALIDATE EMPTY FIELDS
                if (enteredUser.isEmpty() || enteredPass.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please input the required text", "Warning Message", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                //CHECK IF USER EXISTS IN REGULAR ACCOUNTS
                int index = AccountCreation.getUsernamesList().indexOf(enteredUser);
                if (index != -1 && !enteredUser.equals(USER_ADMIN)) {
                    if (AccountCreation.getBlockedUsernames().contains(enteredUser)) {
                        JOptionPane.showMessageDialog(null, "This account is blocked"
                                + "\nPlease contact the administrator to request account recovery."
                                + "\nHotline Number: 041-125", "Account Blocked", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    if (AccountCreation.getPasswordsList().get(index).equals(enteredPass)) {
                        AccountCreation.getFailedAttempts().set(index, 0);
                        JOptionPane.showMessageDialog(null, "Successfully Log-in", "Welcome Message", JOptionPane.INFORMATION_MESSAGE);
                        loggedInUsername = enteredUser;
                        UserAccount userAcc = new UserAccount();
                        userAcc.setVisible(true);
                        this.dispose();
                    } else {
                        int attempts = AccountCreation.getFailedAttempts().get(index) + 1;
                        AccountCreation.getFailedAttempts().set(index, attempts);
                        if (attempts >= 3) {
                            AccountCreation.getBlockedUsernames().add(enteredUser);
                            JOptionPane.showMessageDialog(null, "Your account has beed blocked after 3 failed attempts."
                                    + "\nPlease contact the administrator to request account recovery."
                                    + "\nHotline Number: 041-125", "Account Blocked", JOptionPane.WARNING_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Invalid Username or Password\nAttempts remaining: " + (3 - attempts), "Login Warning", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                    return;
                }
                //CHECK IF USER IS ADMIN
                if (enteredUser.equals(USER_ADMIN)) {
                    if (enteredPass.trim().equals(PASS_ADMIN)) {
                        JPasswordField text = new JPasswordField();
                        JPanel hintPanel = new JPanel(new GridLayout(2, 1, 5, 5));
                        hintPanel.add(new JLabel("Enter security hint"));
                        hintPanel.add(text);
                        int choice = JOptionPane.showConfirmDialog(null, hintPanel, "Admin Verification", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                        if (choice != JOptionPane.OK_OPTION) {
                            return;
                        }
                        String text2 = new String(text.getPassword());
                        if (!text2.equals(AccountCreation.getHint())) {
                            JOptionPane.showMessageDialog(null, "Wrong Security Hint!", "Error Message", JOptionPane.WARNING_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Admin Account Successfully Log-in", "Welcome Message", JOptionPane.INFORMATION_MESSAGE);
                            AdminAccount adminAcc = new AdminAccount();
                            adminAcc.setVisible(true);
                            this.dispose();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid Username or Password!", "Error Message", JOptionPane.ERROR_MESSAGE);
                    }
                    return;
                }
                //DEFAULT INVALID MESSAGE IF NEITHER USER NOR ADMIN MATCHES
                JOptionPane.showMessageDialog(null, "Invalid Username or No User Found!", "Error Message", JOptionPane.ERROR_MESSAGE);
            } else if (e.getSource().equals(createAccount)) { //CREATE NEW ACCOUNT BUTTON
                AccountCreation acc = new AccountCreation();
                acc.setVisible(true);
                this.dispose();
            } else if (e.getSource().equals(showPass)) { //SHOW PASSWORD CHECKBOX
                if (showPass.isSelected()) {
                    pass.setEchoChar((char) 0);
                } else {
                    pass.setEchoChar('•');
                }
            }
        } catch (Exception handling) {
            JOptionPane.showMessageDialog(null, "An error occurred during login.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } //FUNCTIONS OF ALL THE BUTTON 
    //(NOTE: TO MAKE THE BUTTON WORKABLE ADD .addActionListener(), EXAMPLE: button.addActionListener(this);

    public static void main(String[] args) {
        LogInForm login = new LogInForm();
        login.setVisible(true);
    }
}

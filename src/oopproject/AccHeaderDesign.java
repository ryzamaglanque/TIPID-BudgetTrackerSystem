package oopproject;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class AccHeaderDesign { 

    private static ImageIcon scaledIcon(String path, int width, int height) { //LOADS AN IMAGE FROM THE Image FOLDER AND SCALES IT
        Image image = new ImageIcon(AccHeaderDesign.class.getResource(path)).getImage();
        return new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_SMOOTH));
    } //LOADS AN IMAGE FROM THE Image FOLDER AND SCALES IT

    private static JButton menuButton(String text, String iconPath, int y) { //ONE BUTTON OF THE DROPDOWN MENU
        JButton button = new JButton(text);
        button.setBounds(15, y, 200, 35);
        button.setFont(new Font("Sans-Serif", Font.PLAIN, 14));
        button.setForeground(Color.DARK_GRAY);
        button.setIcon(scaledIcon(iconPath, 18, 18));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(Color.WHITE);
        return button;
    } //ONE BUTTON OF THE DROPDOWN MENU

    public static JPanel titleHeader(JFrame frame, String username, Runnable changePasswordAction) { //METHOD TO DISPLAY TITLE HEADER (BLUE PART)

        JPanel header = new JPanel();
        header.setBounds(0, 0, 1540, 80);
        header.setBackground(new Color(30, 58, 138));
        header.setLayout(null);

        //LOGO
        JLabel logoImage = new JLabel(scaledIcon("/Image/logo.png", 80, 50));
        logoImage.setBounds(10, 15, 80, 50);
        header.add(logoImage);

        //TITLE
        JLabel logo = new JLabel("T.I.P.I.D");
        logo.setBounds(100, 13, 200, 30);
        logo.setFont(new Font("Sans-Serif", Font.BOLD, 25));
        logo.setForeground(Color.WHITE);
        header.add(logo);

        JLabel subtitle = new JLabel("Tracking Income, Purchases, and Individual Daily expenses");
        subtitle.setBounds(100, 43, 500, 20);
        subtitle.setFont(new Font("Sans-Serif", Font.PLAIN, 15));
        subtitle.setForeground(Color.decode("#E2E8F0"));
        header.add(subtitle);

        //DASHBOARD ICON
        JLabel dashIcon = new JLabel(scaledIcon("/Image/dash.png", 40, 40));
        dashIcon.setBounds(1480, 20, 40, 40);
        header.add(dashIcon);

        //DROPDOWN PANEL
        JPanel dropdown = new JPanel();
        dropdown.setBounds(1267, 60, 250, 155);
        dropdown.setBackground(Color.WHITE);
        dropdown.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
        dropdown.setLayout(null);

        JLabel user = new JLabel("Hi, " + username + "!");
        user.setBounds(20, 15, 220, 25);
        user.setFont(new Font("Sans-Serif", Font.BOLD, 16));
        dropdown.add(user);

        JButton changePass = menuButton("Change Password", "/Image/changePass.png", 55);
        dropdown.add(changePass);

        JButton logOut = menuButton("Log-out", "/Image/logOut.png", 95);
        dropdown.add(logOut);

        //CHANGE PASSWORD BUTTON
        if (changePasswordAction == null) {
            changePass.setVisible(false);
        } else {
            changePass.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    changePasswordAction.run();
                }
            });
        }

        //LOG-OUT BUTTON
        logOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to log out?", "Confirm Log-out",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (choice == JOptionPane.YES_OPTION) {
                    frame.dispose();
                    LogInForm login = new LogInForm();
                    login.setVisible(true);
                }
            }
        });

        frame.add(dropdown);
        dropdown.setVisible(false);

        //SHOW OR HIDE DROPDOWN
        dashIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (dropdown.isVisible()) {
                    dropdown.setVisible(false);
                } else {
                    dropdown.setVisible(true);
                    frame.getContentPane().setComponentZOrder(dropdown, 0);
                }
            }
        });

        frame.add(header);
        frame.getContentPane().setComponentZOrder(header, 1);

        return header;
    } //METHOD TO DISPLAY TITLE HEADER (BLUE PART)
}
package oopproject;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class AdminAccount extends BaseAccountFrame implements DocumentListener {

    //VARIABLES USED FOR JFRAME DISPLAY 
    JPanel mainPanel = new JPanel();
    JPanel accounts = new JPanel();
    JTextField search = new JTextField();
    JComboBox<String> filter = new JComboBox<>(new String[]{"All", "Active", "Blocked"});
    JLabel totalUsers = new JLabel("0");
    JLabel accountCount = new JLabel();
    //TABLE MODEL FOR USER ACCOUNTS TABLE (NON-EDITABLE)
    String col[] = {"User ID", "User Name", "Status"};
    DefaultTableModel model = new DefaultTableModel(null, col) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    JTable table = new JTable(model);
    JScrollPane scroll = new JScrollPane(table);

    //ACTION BUTTONS FOR ADMIN MANAGEMENT
    JButton unblockButton = new JButton("Unblock User");
    JButton deleteButton = new JButton("Delete User");
//    JButton viewButton = new JButton("View User Transactions");
    Color blue = Color.decode("#315DA8");

    public AdminAccount() { //CONSTRUCTOR, WILL IMPLEMENT EVERY TIME IT IS CALLED
        //JFRAME DESIGN INITIALIZATION
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setTitle("Budget Tracker System");
        Image appIcon = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("Image/logo.png"));
        this.setIconImage(appIcon);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);

        //DISPLAY TITLE HEADER (BLUE PART)
        AccHeaderDesign.titleHeader(this, LogInForm.getUserAdmin(),
                new Runnable() {
            @Override
            public void run() {
                changePassword();
            }
        });
        createDashboard();
    } //CONSTRUCTOR, WILL IMPLEMENT EVERY TIME IT IS CALLED

    @Override
    public boolean accountExists() { //ADMIN ACCOUNT ALWAYS EXISTS
        return true;
    } //ADMIN ACCOUNT ALWAYS EXISTS

    @Override
    public boolean verifyCurrentPassword(String current) { //CHECKS IF THE ENTERED PASSWORD MATCHES THE ADMIN PASSWORD
        return LogInForm.getPassAdmin().equals(current);
    } //CHECKS IF THE ENTERED PASSWORD MATCHES THE ADMIN PASSWORD

    @Override
    public void updatePassword(String newPassword) { //UPDATE THE ADMIN'S PASSWORD
        LogInForm.setPASS_ADMIN(newPassword);
    } //UPDATE THE ADMIN'S PASSWORD

    public void createDashboard() { //CREATES AND POSITIONS THE ADMIN OVERVIEW DASHBOARD
        mainPanel.setLayout(null);
        mainPanel.setBounds(0, 70, 1550, 1000);
        //HEADING LABEL
        JLabel heading = new JLabel("ADMIN OVERVIEW");
        heading.setBounds(320, 20, 300, 25);
        heading.setForeground(blue);
        mainPanel.add(heading);
        //TITLE LABEL
        JLabel title = new JLabel("User monitoring");
        title.setBounds(320, 45, 500, 40);
        title.setFont(new Font("Serif", Font.BOLD, 30));
        mainPanel.add(title);
        //SUBTITLE LABEL
        JLabel subtitle = new JLabel("Review account activity and high-level budget health across the app.");
        subtitle.setBounds(320, 85, 800, 25);
        subtitle.setForeground(Color.GRAY);
        mainPanel.add(subtitle);
        //TOTAL USERS CARD
        createTotalUsersCard();
        //ADD ACCOUNTS TABLE PANEL
        createAccountsPanel();
        mainPanel.add(accounts);
        add(mainPanel);
        updateUserTotal();
    } //CREATES AND POSITIONS THE ADMIN OVERVIEW DASHBOARD

    public void createTotalUsersCard() { //CREATES TOTAL USER DASHBOARD
        JPanel dashboardP = new JPanel();
        dashboardP.setLayout(null);
        dashboardP.setBackground(Color.WHITE);
        dashboardP.setBounds(320, 130, 250, 95);
        dashboardP.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));

        JLabel headingLabel = new JLabel("Total Users");
        headingLabel.setBounds(15, 10, 220, 20);
        headingLabel.setForeground(Color.GRAY);
        dashboardP.add(headingLabel);

        totalUsers.setBounds(15, 35, 225, 30);
        totalUsers.setFont(new Font("Arial", Font.BOLD, 20));
        totalUsers.setForeground(blue);
        dashboardP.add(totalUsers);

        JLabel descriptionLabel = new JLabel("registered accounts");
        descriptionLabel.setBounds(15, 67, 220, 20);
        descriptionLabel.setForeground(Color.GRAY);
        dashboardP.add(descriptionLabel);

        mainPanel.add(dashboardP);
    } //CREATES TOTAL USER DASHBOARD

    public void createAccountsPanel() { //MODIFICATION AND LAYOUT OF THE ACCOUNTS TABLE CONTAINER
        accounts.setLayout(null);
        accounts.setBackground(Color.WHITE);
        accounts.setBounds(320, 250, 900, 460);
        accounts.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
        //TABLE SECTION TITLE
        JLabel accountTitle = new JLabel("User accounts");
        accountTitle.setBounds(20, 10, 300, 30);
        accountTitle.setFont(new Font("Serif", Font.BOLD, 20));
        accounts.add(accountTitle);
        accountCount.setBounds(20, 35, 300, 20);
        accountCount.setForeground(Color.GRAY);
        accounts.add(accountCount);
        //SEARCH TEXTFIELD
        search.setBounds(515, 20, 200, 30);
        search.setToolTipText("Search by username or user ID");
        accounts.add(search);
        //STATUS FILTER COMBOBOX
        filter.setBounds(725, 20, 155, 30);
        filter.setBackground(Color.white);
        accounts.add(filter);
        //TABLE DESIGN PROPERTIES
        table.setRowHeight(35);
        table.setFont(new Font("Sans-Serif", Font.PLAIN, 15));
        table.getTableHeader().setFont(new Font("Serif", Font.BOLD, 18));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scroll.setBounds(20, 75, 860, 300);
        accounts.add(scroll);
        //UNBLOCK BUTTON 
        unblockButton.setBounds(20, 385, 150, 35);
        unblockButton.setFont(new Font("Sans-Serif", Font.BOLD, 15));
        unblockButton.setForeground(Color.white);
        unblockButton.setBackground(new Color(30, 58, 138));
        //DELETE BUTTON 
        deleteButton.setBounds(180, 385, 150, 35);
        deleteButton.setFont(new Font("Sans-Serif", Font.BOLD, 15));
        deleteButton.setForeground(Color.white);
        deleteButton.setBackground(new Color(30, 58, 138));
        //VIEW TRANSACTIONS BUTTON CONFIGURATION
//        viewButton.setBounds(340, 385, 220, 35);
//        viewButton.setFont(new Font("Sans-Serif", Font.BOLD, 15));
//        viewButton.setForeground(Color.white);
//        viewButton.setBackground(new Color(30, 58, 138));

        accounts.add(unblockButton);
        accounts.add(deleteButton);
//        accounts.add(viewButton);

        //LISTENERS: SEARCH IS LIVE (DocumentListener), THE REST ARE ACTIONS
        search.getDocument().addDocumentListener(this);
        filter.addActionListener(this);
        deleteButton.addActionListener(this);
        unblockButton.addActionListener(this);
//        viewButton.addActionListener(this);

        refreshUsers();
    } //MODIFICATION AND LAYOUT OF THE ACCOUNTS TABLE CONTAINER

    public void refreshUsers() { //UPDATE THE USER LIST TABLE BASED ON THE SEARCH TEXT AND THE STATUS FILTER
        model.setRowCount(0);
        String keyword = search.getText().trim();
        String selectedStatus = (String) filter.getSelectedItem();
        ArrayList<String> usernames = AccountCreation.getUsernamesList();
        for (int index = 0; index < usernames.size(); index++) {
            String name = usernames.get(index);
            if (name.equalsIgnoreCase(LogInForm.getUserAdmin())) {
                continue; //SKIP ADMIN ACCOUNT FROM USER MONITORING LIST
            }
            String id = AccountCreation.getUserId().get(index);
            String status;
            if (AccountCreation.getBlockedUsernames().contains(name)) {
                status = "Blocked";
            } else {
                status = "Active";
            }
            boolean matchesSearch = keyword.isEmpty() || linearSearch(new String[]{name, id}, keyword) != -1;
            boolean matchesStatus = selectedStatus.equals("All") || status.equals(selectedStatus);
            if (matchesSearch && matchesStatus) {
                model.addRow(new Object[]{id, name, status});
            }
        }
        accountCount.setText(model.getRowCount() + " accounts shown");
        table.clearSelection();
    } //UPDATE THE USER LIST TABLE BASED ON THE SEARCH TEXT AND THE STATUS FILTER

    public void updateUserTotal() { //UPDATE/SHOW THE TOTAL AMOUNT OF REGISTERED USERS
        int totalUsersCount = 0;
        for (String name : AccountCreation.getUsernamesList()) {
            if (!name.equalsIgnoreCase(LogInForm.getUserAdmin())) { //EXCLUDE ADMIN FROM TOTAL USER COUNT
                totalUsersCount++;
            }
        }
        totalUsers.setText(String.valueOf(totalUsersCount));
    } //UPDATE/SHOW THE TOTAL AMOUNT OF REGISTERED USERS

    public void deleteSelectedAccount() { //DELETE USER IN SELECTED ROW WITH ADMIN PASSWORD VERIFICATION
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select an account first.", "No Account Selected",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        String selectedUsername = model.getValueAt(selectedRow, 1).toString();
        int choice = JOptionPane.showConfirmDialog(null, "Do you want to delete this account?", "Confirm Delete",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (choice != JOptionPane.YES_OPTION) {
            return;
        }
        JPasswordField text = new JPasswordField();
        int choice2 = JOptionPane.showConfirmDialog(null, text, "Enter Admin Password",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (choice2 != JOptionPane.OK_OPTION) {
            return;
        }
        //CHECK AGAINST THE CURRENT ADMIN PASSWORD (IT CAN BE CHANGED, SO IT IS NOT HARD-CODED)
        if (!verifyCurrentPassword(new String(text.getPassword()))) {
            JOptionPane.showMessageDialog(null, "Wrong admin password!");
            return;
        }
        int index = AccountCreation.getUsernamesList().indexOf(selectedUsername);
        //DELETE EVERY VARIABLE ASSOCIATED WITH THE SELECTED USER
        if (index != -1) {
            AccountCreation.getUsernamesList().remove(index);
            AccountCreation.getPasswordsList().remove(index);
            AccountCreation.getUserId().remove(index);
            AccountCreation.getFailedAttempts().remove(index);
            AccountCreation.getBlockedUsernames().remove(selectedUsername);
            TransactionInfo.deleteTransactions(selectedUsername);
            refreshUsers();
            updateUserTotal();
            JOptionPane.showMessageDialog(null, "Account successfully deleted.", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    } //DELETE USER IN SELECTED ROW WITH ADMIN PASSWORD VERIFICATION

    public void unblockSelectedAccount() { //UNBLOCK THE SELECTED BLOCKED USER ACCOUNT
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select an account first.", "No Account Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String selectedUsername = model.getValueAt(selectedRow, 1).toString();
        if (!AccountCreation.getBlockedUsernames().contains(selectedUsername)) {
            JOptionPane.showMessageDialog(null, "This account is not blocked.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        //REMOVE FROM BLOCKED LIST AND RESET FAILED ATTEMPTS TO 0
        AccountCreation.getBlockedUsernames().remove(selectedUsername);
        int index = AccountCreation.getUsernamesList().indexOf(selectedUsername);
        if (index >= 0 && index < AccountCreation.getFailedAttempts().size()) {
            AccountCreation.getFailedAttempts().set(index, 0);
        }
        JOptionPane.showMessageDialog(null, "Account successfully unblocked.", "Success", JOptionPane.INFORMATION_MESSAGE);
        refreshUsers();
    } //UNBLOCK THE SELECTED BLOCKED USER ACCOUNT

//    public void viewSelectedAccount() { //VIEW TRANSACTIONS OF THE SELECTED USER ACCOUNT IN A NEW WINDOW
//        int selectedRow = table.getSelectedRow();
//        if (selectedRow == -1) {
//            JOptionPane.showMessageDialog(null, "Please select an account first.", "No Account Selected",
//                    JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//        String selectedUsername = model.getValueAt(selectedRow, 1).toString();
//
//        JPanel holder = new JPanel();
//        holder.setBounds(35, 0, 780, 500);
//        holder.setLayout(null);
//
//        JLabel title = new JLabel("VIEW THE TRANSACTIONS HISTORY OF");
//        title.setBounds(15, 20, 300, 10);
//        title.setFont(new Font("Sans-Serif", Font.PLAIN, 13));
//        title.setForeground(Color.darkGray);
//        holder.add(title);
//
//        JLabel user = new JLabel("User: " + selectedUsername);
//        user.setBounds(15, 33, 300, 30);
//        user.setFont(new Font("Serif", Font.BOLD, 20));
//        user.setForeground(new Color(30, 58, 138));
//        holder.add(user);
//
//        //EACH WINDOW GETS ITS OWN TABLE MODEL, SO TWO OPEN WINDOWS DO NOT OVERWRITE EACH OTHER
//        DefaultTableModel tb = new DefaultTableModel(null,
//                new String[]{"Transaction Number", "Description", "Category", "Amount", "Date"}) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return false;
//            }
//        };
//        ArrayList<TransactionInfo> userTransactions = TransactionInfo.getAllTransactions(selectedUsername);
//        TransactionInfo.sortByDate(userTransactions, false); //LATEST TO OLDEST
//        for (int i = 0; i < userTransactions.size(); i++) {
//            TransactionInfo t = userTransactions.get(i);
//            tb.addRow(new Object[]{
//                t.getItemNum(),
//                t.getItemName(),
//                t.getCategory(),
//                String.format("%,.2f", t.getAmount()),
//                t.getDate()
//            });
//        }
//
//        JTable historyTable = new JTable(tb);
//        historyTable.setRowHeight(30);
//        historyTable.setFont(new Font("Sans-Serif", Font.PLAIN, 14));
//        historyTable.getTableHeader().setFont(new Font("Sans-Serif", Font.BOLD, 14));
//        JScrollPane historyScroll = new JScrollPane(historyTable);
//        historyScroll.setBounds(10, 75, 760, 420);
//        holder.add(historyScroll);
//
//        //CREATE AND SHOW SEPARATE POPUP FRAME FOR USER TRANSACTIONS
//        JFrame frame = new JFrame("Transactions History");
//        frame.setSize(855, 550);
//        frame.setLayout(null);
//        frame.setResizable(false);
//        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        frame.add(holder);
//        frame.setLocationRelativeTo(null);
//        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("Image/logo.png")));
//        frame.setVisible(true);
//    } //VIEW TRANSACTIONS OF THE SELECTED USER ACCOUNT IN A NEW WINDOW

    public static int linearSearch(String[] arr, String target) { //FROM CC105 LINEARSEARCH ALGORITHM
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].toLowerCase().contains(target.toLowerCase())) {
                return i;
            }
        }
        return -1;
    } //FROM CC105 LINEARSEARCH ALGORITHM

    @Override
    public void insertUpdate(DocumentEvent e) { //LIVE SEARCH WHILE TYPING
        refreshUsers();
    } //LIVE SEARCH WHILE TYPING

    @Override
    public void removeUpdate(DocumentEvent e) { //LIVE SEARCH WHILE TYPING
        refreshUsers();
    } //LIVE SEARCH WHILE TYPING

    @Override
    public void changedUpdate(DocumentEvent e) { //LIVE SEARCH WHILE TYPING
        refreshUsers();
    } //LIVE SEARCH WHILE TYPING

    @Override
    public void actionPerformed(ActionEvent e) { //FUNCTIONS OF ALL THE BUTTONS AND COMBOBOX
        if (e.getSource() == filter) {
            refreshUsers();
        } else if (e.getSource() == deleteButton) {
            deleteSelectedAccount();
        } else if (e.getSource() == unblockButton) {
            unblockSelectedAccount();
        } 
//        else if (e.getSource() == viewButton) {
//            viewSelectedAccount();
//        }
    } //FUNCTIONS OF ALL THE BUTTONS AND COMBOBOX
    //(NOTE: TO MAKE THE BUTTON WORKABLE ADD .addActionListener(), EXAMPLE: button.addActionListener(this);

}
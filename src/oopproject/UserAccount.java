package oopproject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class UserAccount extends BaseAccountFrame implements ItemListener, DocumentListener {

//GLOBAL VARIABLES SECTION
    //FORMATTERS 
    DecimalFormat df = new DecimalFormat("#,##0.00");
    //PANEL CONTAINER FOR DISPLAYING PAGES
    JPanel panel = new JPanel();
    //VARIABLES FOR NAVIGATION PAGES HEADER BAR
    JPanel pagesTitle = new JPanel();
    JLabel dashboard = new JLabel("Dashboard");
    JLabel transactions = new JLabel("Transactions");
    JLabel budget = new JLabel("Budget");
    JLabel spendingHistory = new JLabel("Spending History");
    //VALUES SHOWN ON THE DASHBOARD CARDS
    JLabel totalIncome = new JLabel("P0.00");
    JLabel totalSpent = new JLabel("P0.00");
    JLabel remaining = new JLabel("P0.00");
    JLabel budgeted = new JLabel("P0.00");
    //VARIABLES FOR TRANSACTION FORM MENU PANEL
    JPanel menuP = new JPanel();
    JPanel titleMenu = new JPanel();
    JLabel activeMonthYearL = new JLabel();
    JLabel addT = new JLabel("Add a transaction");
    //THE CATEGORY LIST COMES FROM TransactionInfo SO IT ONLY EXISTS IN ONE PLACE
    private static final String CATEGORY_PROMPT = "-- Select Category --";
    private final String[] CATEGORY_NAMES = TransactionInfo.getCategories();
    JComboBox<String> categoryCB = new JComboBox<>();
    //TRANSACTION NUMBER
    JLabel itemNumL = new JLabel("Item Number");
    JTextField itemNumTF = new JTextField();
    String transNum = "TN";
    //DESCRIPTION
    JLabel itemNameL = new JLabel("Description");
    JTextField itemNameTF = new JTextField();
    //AMOUNT
    JLabel amountL = new JLabel("Amount");
    JTextField amountTF = new JTextField();
    //DATE
    JLabel dateL = new JLabel("Day (1-31)");
    JTextField dateTF = new JTextField();
    //BUDGET AND LEFT
    JPanel budgetP = new JPanel();
    JLabel budgetL = new JLabel("Budget");
    JLabel budgetS = new JLabel();
    //BUTTONS IN ADDTRANSACTION 
    JButton add = new JButton("Add");
    JButton newB = new JButton("New");
    JButton update = new JButton("Update");
    JButton delete = new JButton("Delete");
    JButton clear = new JButton("Clear");
    //TRANSACTIONS FOR THE CURRENT USER AND ACTIVE PERIOD (SAME LIST OBJECT THAT TransactionInfo KEEPS)
    private String currentUsername;
    private ArrayList<TransactionInfo> transacInfo = new ArrayList<>();
    //VARIABLES FOR JTABLE DISPLAY
    JPanel tableP = new JPanel();
    String col[] = {"Transaction Number", "Description", "Category", "Amount", "Date"};
    DefaultTableModel model = new DefaultTableModel(null, col) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    JTable table = new JTable(model);
    JScrollPane sPane = new JScrollPane(table); //CREATED ONCE: A NEW ONE PER VISIT WOULD LEAVE AN EMPTY OLD ONE COVERING THE TABLE
    //VARIABLES FOR BUDGET ALLOCATION PANEL
    JLabel budgetPageTitleL = new JLabel("Budget Allocation");
    JLabel setPageL = new JLabel("Set your monthly budget setting now.");
    //ACTIVE FOR
    JPanel bp1 = new JPanel();
    JLabel activeL = new JLabel("ACTIVE FOR: ");
    JComboBox<String> monthYearCB = new JComboBox<>();
    //TOTAL BUDGET
    JLabel totalL = new JLabel("TOTAL BUDGET:");
    JLabel totalB = new JLabel();
    //ALLOCATED BUDGET
    JLabel allocatedBL = new JLabel("ALLOCATED BUDGET:");
    JLabel allocatedB = new JLabel();
    //BUTTONS IN BUDGETPAGE
    JButton set = new JButton("Set");
    JButton edit = new JButton("Edit");
    //VARIABLES FOR MONTH SELECTION PANEL
    JPanel bp2 = new JPanel();
    JLabel monthL = new JLabel("Select Month");
    JComboBox<String> monthCB = new JComboBox<>(TransactionInfo.getMonths());
    //VARIABLES FOR YEAR ENTRY PANEL
    JPanel bp3 = new JPanel();
    JLabel yearL = new JLabel("Enter Year");
    JTextField yearTF = new JTextField();
    //VARIABLES FOR TOTAL BUDGET ENTRY PANEL
    JPanel bp4 = new JPanel();
    JLabel totalBL = new JLabel("Enter Total Budget");
    JTextField totalBTF = new JTextField();
    //VARIABLES FOR SCROLLPANE CATEGORIES CONTAINER
    JPanel contentPanel = new JPanel();
    JScrollPane scroll;
    //CATEGORY BUDGET INPUT FIELDS (SAME ORDER AS CATEGORY_NAMES)
    private final JTextField[] CATEGORY_FIELDS = new JTextField[CATEGORY_NAMES.length];
    private final Color[] CATEGORY_COLORS = {
        Color.decode("#D97706"),
        Color.decode("#10B981"),
        Color.decode("#EF4444"),
        Color.decode("#3B82F6"),
        Color.decode("#8B5CF6"),
        Color.decode("#EC4899"),
        Color.decode("#F97316"),
        Color.decode("#06B6D4")
    };
    private final JLabel[] CATEGORY_SPENT_LABELS = new JLabel[CATEGORY_NAMES.length];
    private final JLabel[] CATEGORY_LEFT_LABELS = new JLabel[CATEGORY_NAMES.length];
    //BUDGET PAGE STATE
    private static final String DRAFT_TAG = " (draft)"; //MARKS A PERIOD THAT IS TYPED BUT NOT SAVED YET
    private String draftItem = null; //THE DRAFT ITEM CURRENTLY INSIDE monthYearCB
    private String formPeriod = ""; //THE PERIOD THE FIELDS CURRENTLY FOLLOW (CHANGING MONTH/YEAR TO ANOTHER PERIOD RELOADS THE FIELDS)
    private boolean editing = false; //TRUE AFTER CLICKING EDIT, FALSE AFTER SET
    private boolean updatingBudgetUI = false; //TRUE WHILE THE PROGRAM (NOT THE USER) CHANGES THE BUDGET FIELDS, SO LISTENERS STAY QUIET
    //VARIABLES FOR SPENDING HISTORY 
    JLabel shL = new JLabel("Spending History");
    JLabel shL2 = new JLabel("Search and organize your spending history.");
    JLabel search = new JLabel("Search");
    JTextField searchTF = new JTextField();
    String sortH[] = {"All Transactions", "Oldest to Latest", "Latest to Oldest",
        "Lowest to Highest Spent", "Highest to Lowest Spent", "Filter by Category"};
    JComboBox<String> sort = new JComboBox<>(sortH);
    JComboBox<String> filterC = new JComboBox<>(CATEGORY_NAMES);
    JPanel shPanel = new JPanel();
    private DefaultTableModel historyModel;

//CONSTRUCTOR & INITIALIZATION METHODS
    public UserAccount() { //CONSTRUCTOR TO INITIALIZE USER ACCOUNT FRAME AND DATA
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setTitle("Budget Tracker System");
        Image appIcon = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("Image/logo.png"));
        this.setIconImage(appIcon);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);

        panel.setBounds(0, 130, 1540, 1000);
        panel.setLayout(null);
        this.add(panel);

        currentUsername = LogInForm.getLoggedInUsername();

        ArrayList<String> savedPeriods = TransactionInfo.getSavedMonthYears(currentUsername);
        if (!savedPeriods.isEmpty()) {
            String activePeriod = savedPeriods.get(savedPeriods.size() - 1);
            TransactionInfo.setCurrentPeriod(currentUsername, activePeriod);
            transacInfo = TransactionInfo.getTransactionsForPeriod(currentUsername, activePeriod);
        } else {
            TransactionInfo.setCurrentPeriod(currentUsername, "");
        }

        //CATEGORY OPTIONS FOR TRANSACTIONS
        categoryCB.addItem(CATEGORY_PROMPT);
        for (int i = 0; i < CATEGORY_NAMES.length; i++) {
            categoryCB.addItem(CATEGORY_NAMES[i]);
        }
        initTransactionTable();

        //LISTENERS ARE ADDED ONLY ONCE HERE (ADDING THEM WHEN A PAGE IS BUILT STACKS THEM UP EVERY VISIT)
        add.addActionListener(this);
        update.addActionListener(this);
        delete.addActionListener(this);
        newB.addActionListener(this);
        clear.addActionListener(this);
        set.addActionListener(this);
        edit.addActionListener(this);
        searchTF.addActionListener(this);

        categoryCB.addItemListener(this);
        monthCB.addItemListener(this);
        monthYearCB.addItemListener(this);
        sort.addItemListener(this);
        filterC.addItemListener(this);

        yearTF.getDocument().addDocumentListener(this);
        totalBTF.getDocument().addDocumentListener(this);
        searchTF.getDocument().addDocumentListener(this);
        for (int i = 0; i < CATEGORY_FIELDS.length; i++) {
            CATEGORY_FIELDS[i] = new JTextField();
            CATEGORY_FIELDS[i].getDocument().addDocumentListener(this);
        }

        //DISPLAY TITLE HEADER (BLUE PART)
        AccHeaderDesign.titleHeader(this, currentUsername,
                new Runnable() {
            @Override
            public void run() {
                changePassword();
            }
        });
        pagesHeader();

        //DEFAULT INITIALIZATION TO DASHBOARD PAGE
        highlightNav(dashboard);
        dashboardPage();
    } //CONSTRUCTOR TO INITIALIZE USER ACCOUNT FRAME AND DATA

    private int getAccountIndex() { //GETS THE INDEX OF THE LOGGED-IN USER IN THE ACCOUNT LIST
        return AccountCreation.getUsernamesList().indexOf(currentUsername);
    } //GETS THE INDEX OF THE LOGGED-IN USER IN THE ACCOUNT LIST

    @Override
    public boolean accountExists() { //CHECKS IF THE LOGGED-IN USER HAS AN ACCOUNT
        return getAccountIndex() != -1;
    } //CHECKS IF THE LOGGED-IN USER HAS AN ACCOUNT

    @Override
    public boolean verifyCurrentPassword(String current) { //CHECKS IF THE ENTERED PASSWORD MATCHES THE USER PASSWORD
        return AccountCreation.getPasswordsList().get(getAccountIndex()).equals(current);
    } //CHECKS IF THE ENTERED PASSWORD MATCHES THE USER PASSWORD

    @Override
    public void updatePassword(String newPassword) { //UPDATE THE USER'S PASSWORD
        AccountCreation.getPasswordsList().set(getAccountIndex(), newPassword);
    } //UPDATE THE USER'S PASSWORD

    private void refreshPanel() { //REDRAWS THE CURRENT PAGE
        panel.revalidate();
        panel.repaint();
    } //REDRAWS THE CURRENT PAGE

    public void pagesHeader() { //METHOD TO CREATE NAVIGATION HEADER BAR
        pagesTitle.setBounds(0, 80, 1540, 50);
        pagesTitle.setBackground(Color.white);
        pagesTitle.setLayout(null);
        this.add(pagesTitle);

        addNavTab(dashboard, 10, 150);
        addNavTab(budget, 160, 100);
        addNavTab(transactions, 260, 150);
        addNavTab(spendingHistory, 400, 200);
    } //METHOD TO CREATE NAVIGATION HEADER BAR

    private void addNavTab(final JLabel tab, int x, int width) { //ADDS ONE CLICKABLE TAB TO THE NAVIGATION BAR
        tab.setBounds(x, 10, width, 30);
        tab.setHorizontalAlignment(SwingConstants.CENTER);
        pagesTitle.add(tab);
        tab.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showPage(tab);
            }
        });
    } //ADDS ONE CLICKABLE TAB TO THE NAVIGATION BAR

    private void showPage(JLabel tab) { //HIGHLIGHTS THE TAB AND SHOWS ITS PAGE
        highlightNav(tab);
        if (tab == dashboard) {
            dashboardPage();
        } else if (tab == budget) {
            budgetPage();
        } else if (tab == transactions) {
            transactionsPage();
        } else {
            spendingHistoryPage();
        }
    } //HIGHLIGHTS THE TAB AND SHOWS ITS PAGE

    private void highlightNav(JLabel active) { //BOLD + BLUE FOR THE OPEN PAGE, PLAIN + GRAY FOR THE OTHERS
        JLabel[] tabs = {dashboard, budget, transactions, spendingHistory};
        for (int i = 0; i < tabs.length; i++) {
            if (tabs[i] == active) {
                tabs[i].setFont(new Font("Serif", Font.BOLD, 20));
                tabs[i].setForeground(new Color(30, 58, 138));
            } else {
                tabs[i].setFont(new Font("Serif", Font.PLAIN, 20));
                tabs[i].setForeground(Color.darkGray);
            }
        }
    } //BOLD + BLUE FOR THE OPEN PAGE, PLAIN + GRAY FOR THE OTHERS

//FOR DASHBOARD PAGE METHODS
    public void dashboardPage() { //METHOD TO CALL THE DASHBOARD PAGE VIEW
        panel.removeAll();

        dashboardHeader(panel);
        updateDashboardTotals();
        spendingBreakdown(panel);
        dashboardCategoryProgress(panel);
        recentTransactions(panel);

        refreshPanel();
    } //METHOD TO CALL THE DASHBOARD PAGE VIEW

    public void drawPieChart(Graphics2D g2) { //METHOD TO DRAW THE PIE CHART PROPORTIONS
        double totalSpentSum = 0;
        for (int i = 0; i < CATEGORY_NAMES.length; i++) {
            totalSpentSum += TransactionInfo.getCategorySpent(CATEGORY_NAMES[i]);
        }
        if (totalSpentSum <= 0) {
            g2.setColor(Color.LIGHT_GRAY);
            g2.fillArc(135, 30, 150, 150, 0, 360);
            return;
        }
        int startAngle = 0;
        for (int i = 0; i < CATEGORY_NAMES.length; i++) {
            double spent = TransactionInfo.getCategorySpent(CATEGORY_NAMES[i]);
            int angle = (int) Math.round((spent / totalSpentSum) * 360);
            g2.setColor(CATEGORY_COLORS[i]);
            g2.fillArc(135, 30, 150, 150, startAngle, angle);
            startAngle += angle;
        }
    } //METHOD TO DRAW THE PIE CHART PROPORTIONS

    public void spendingBreakdown(JPanel parentPanel) { //METHOD TO CALL SPENDING BREAKDOWN PANEL AND LEGEND
        JPanel spendingbreakdown = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                drawPieChart(g2);
            }
        };
        JLabel sP = new JLabel("Spending Breakdown");
        sP.setBounds(20, 15, 300, 25);
        sP.setFont(new Font("Sans-Serif", Font.BOLD, 13));
        sP.setForeground(Color.darkGray);
        spendingbreakdown.add(sP);
        spendingbreakdown.setBackground(Color.white);
        spendingbreakdown.setBounds(240, 150, 420, 500);
        spendingbreakdown.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
        spendingbreakdown.setLayout(null);
        parentPanel.add(spendingbreakdown);
        double totalSpentSum = 0;
        for (int i = 0; i < CATEGORY_NAMES.length; i++) {
            totalSpentSum += TransactionInfo.getCategorySpent(CATEGORY_NAMES[i]);
        }
        int startY = 220;
        int rowHeight = 35;
        //LOOP TO RENDER LEGEND ITEMS AND PERCENTAGES
        for (int i = 0; i < CATEGORY_NAMES.length; i++) {
            double percentageValue = 0.0;
            if (totalSpentSum > 0) {
                percentageValue = (TransactionInfo.getCategorySpent(CATEGORY_NAMES[i]) / totalSpentSum) * 100;
            }
            JPanel colorDot = new JPanel();
            colorDot.setBackground(Color.white);
            colorDot.setBounds(40, startY + (i * rowHeight), 15, 15);
            colorDot.setBorder(BorderFactory.createLineBorder(CATEGORY_COLORS[i], 200, true));
            spendingbreakdown.add(colorDot);

            JLabel catLabel = new JLabel(CATEGORY_NAMES[i]);
            catLabel.setBounds(70, startY + (i * rowHeight) - 2, 200, 20);
            catLabel.setFont(new Font("Sans-Serif", Font.PLAIN, 13));
            catLabel.setForeground(Color.black);
            spendingbreakdown.add(catLabel);

            JLabel percentLabel = new JLabel(Math.round(percentageValue) + "%");
            percentLabel.setBounds(310, startY + (i * rowHeight) - 2, 50, 20);
            percentLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            percentLabel.setFont(new Font("Sans-Serif", Font.BOLD, 13));
            percentLabel.setForeground(Color.darkGray);
            spendingbreakdown.add(percentLabel);
        }
    } //METHOD TO CALL SPENDING BREAKDOWN PANEL AND LEGEND

    public void dashboardCategoryProgress(JPanel parentPanel) { //METHOD TO CALL CATEGORY PROGRESS BARS ON DASHBOARD
        JPanel categoryprogress = new JPanel();
        JLabel cP = new JLabel("Category Progress");
        cP.setBounds(20, 15, 300, 25);
        cP.setFont(new Font("Sans-Serif", Font.BOLD, 13));
        cP.setForeground(Color.darkGray);
        categoryprogress.add(cP);
        categoryprogress.setBackground(Color.white);
        categoryprogress.setBounds(675, 150, 640, 385);
        categoryprogress.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
        categoryprogress.setLayout(null);
        parentPanel.add(categoryprogress);

        int startY = 45;
        int rowHeight = 42;

        //LOOP TO ITERATE AND RENDER CATEGORY PROGRESS BARS
        for (int i = 0; i < CATEGORY_NAMES.length; i++) {
            double spent = TransactionInfo.getCategorySpent(CATEGORY_NAMES[i]);
            double categoryBudget = TransactionInfo.getCategoryBudget(CATEGORY_NAMES[i]);

            JLabel catLabel = new JLabel(CATEGORY_NAMES[i]);
            catLabel.setBounds(20, startY + (i * rowHeight), 180, 20);
            catLabel.setFont(new Font("Sans-Serif", Font.PLAIN, 13));
            catLabel.setForeground(Color.black);
            categoryprogress.add(catLabel);

            JLabel moneyLabel = new JLabel("P " + df.format(spent) + " / " + df.format(categoryBudget));
            moneyLabel.setBounds(415, startY + (i * rowHeight), 200, 20);
            moneyLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            moneyLabel.setFont(new Font("Sans-Serif", Font.PLAIN, 13));
            moneyLabel.setForeground(Color.darkGray);
            categoryprogress.add(moneyLabel);

            int maxVal;
            if (categoryBudget >= 1) {
                maxVal = (int) categoryBudget;
            } else {
                maxVal = 100;
            }
            int val;
            if (spent > maxVal) {
                val = maxVal;
            } else {
                val = (int) spent;
            }
            JProgressBar progressBar = new JProgressBar();
            progressBar.setMaximum(maxVal);
            progressBar.setValue(val);
            progressBar.setBackground(new Color(227, 244, 244));
            progressBar.setForeground(CATEGORY_COLORS[i]);
            progressBar.setBounds(20, startY + (i * rowHeight) + 22, 595, 10);
            categoryprogress.add(progressBar);
        }
    } //METHOD TO CALL CATEGORY PROGRESS BARS ON DASHBOARD

    private String activePeriodText() { //TEXT SHOWN FOR THE ACTIVE PERIOD (DASHBOARD + TRANSACTIONS PAGE)
        String period = TransactionInfo.getCurrentPeriod();
        if (period.isEmpty()) {
            period = "Not Set";
        }
        return "Active Period: " + period;
    } //TEXT SHOWN FOR THE ACTIVE PERIOD (DASHBOARD + TRANSACTIONS PAGE)

    public void recentTransactions(JPanel parentPanel) { //METHOD TO CALL RECENT TRANSACTIONS PANEL ON DASHBOARD
        JPanel recent = new JPanel();
        recent.setBackground(Color.white);
        recent.setBounds(675, 545, 640, 105);
        recent.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
        recent.setLayout(null);
        parentPanel.add(recent);

        JLabel activePeriodLabel = new JLabel(activePeriodText());
        activePeriodLabel.setBounds(20, 15, 300, 25);
        activePeriodLabel.setFont(new Font("Sans-Serif", Font.BOLD, 13));
        activePeriodLabel.setForeground(Color.darkGray);
        recent.add(activePeriodLabel);

        JButton viewAllBtn = new JButton("View All");
        viewAllBtn.setBounds(540, 15, 80, 25);
        viewAllBtn.setFont(new Font("Sans-Serif", Font.PLAIN, 12));
        viewAllBtn.setForeground(Color.white);
        viewAllBtn.setBackground(new Color(30, 58, 138));
        viewAllBtn.setFocusPainted(false);
        viewAllBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPage(transactions);
            }
        });
        recent.add(viewAllBtn);

        JPanel recentContentPanel = new JPanel();
        recentContentPanel.setBackground(Color.white);
        recentContentPanel.setLayout(null);

        int rowHeight = 40;
        recentContentPanel.setPreferredSize(new Dimension(600, transacInfo.size() * rowHeight));

        JScrollPane recentScroll = new JScrollPane(recentContentPanel);
        recentScroll.setBounds(15, 45, 610, 50);
        recentScroll.setBorder(BorderFactory.createEmptyBorder());
        recentScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        recentScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        recent.add(recentScroll);

        int startY = 5;

        //LOOP TO DISPLAY ALL TRANSACTIONS (NEWEST FIRST)
        for (int i = 0; i < transacInfo.size(); i++) {
            TransactionInfo t = transacInfo.get(transacInfo.size() - 1 - i);

            JLabel nameLabel = new JLabel(t.getItemName());
            nameLabel.setBounds(5, startY + (i * rowHeight), 300, 20);
            nameLabel.setFont(new Font("Serif", Font.BOLD, 16));
            nameLabel.setForeground(Color.black);
            recentContentPanel.add(nameLabel);

            JLabel idLabel = new JLabel("TransactID: " + t.getItemNum().replace("TN", ""));
            idLabel.setBounds(5, startY + (i * rowHeight) + 18, 200, 15);
            idLabel.setFont(new Font("Sans-Serif", Font.PLAIN, 11));
            idLabel.setForeground(Color.gray);
            recentContentPanel.add(idLabel);

            JLabel dateLabel = new JLabel(t.getDate());
            dateLabel.setBounds(380, startY + (i * rowHeight) + 5, 220, 20);
            dateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            dateLabel.setFont(new Font("Sans-Serif", Font.PLAIN, 13));
            dateLabel.setForeground(Color.darkGray);
            recentContentPanel.add(dateLabel);
        }
    } //METHOD TO CALL RECENT TRANSACTIONS PANEL ON DASHBOARD

    public void dashboardHeader(JPanel parentPanel) { //METHOD TO CALL TOP PART ON DASHBOARD - WILL ALSO BE USE IN TRANSACTION PAGE
        addStatCard(parentPanel, "Total Budget", totalIncome, "all recorded transactions", 240, new Color(76, 175, 80));
        addStatCard(parentPanel, "Total Spent", totalSpent, "all recorded transactions", 515, Color.decode("#DC2626"));
        addStatCard(parentPanel, "Remaining", remaining, "available", 790, new Color(102, 0, 153));
        addStatCard(parentPanel, "Budgeted", budgeted, "allocated", 1065, Color.decode("#D97706"));
    } //METHOD TO CALL TOP PART ON DASHBOARD - WILL ALSO BE USE IN TRANSACTION PAGE

    private void addStatCard(JPanel parentPanel, String title, JLabel valueLabel, String caption, int x, Color valueColor) { //ONE CARD OF THE DASHBOARD TOP PART
        JPanel card = new JPanel();
        card.setBounds(x, 30, 250, 100);
        card.setBackground(Color.white);
        card.setLayout(null);
        parentPanel.add(card);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setBounds(10, 10, 200, 20);
        titleLabel.setFont(new Font("Sans-Serif", Font.PLAIN, 15));
        titleLabel.setForeground(Color.gray);
        card.add(titleLabel);

        valueLabel.setBounds(10, 35, 200, 30);
        valueLabel.setFont(new Font("Serif", Font.BOLD, 30));
        valueLabel.setForeground(valueColor);
        card.add(valueLabel);

        JLabel captionLabel = new JLabel(caption);
        captionLabel.setBounds(10, 70, 200, 20);
        captionLabel.setFont(new Font("Sans-Serif", Font.PLAIN, 13));
        captionLabel.setForeground(Color.gray);
        card.add(captionLabel);
    } //ONE CARD OF THE DASHBOARD TOP PART

    public void updateDashboardTotals() { //METHOD TO UPDATE DASHBOARD TOTALS
        double totalSpentVal = 0.00;
        for (int i = 0; i < transacInfo.size(); i++) {
            totalSpentVal += transacInfo.get(i).getAmount();
        }
        double totalBudgetVal = TransactionInfo.getTotalBudget();

        totalSpent.setText("P" + df.format(totalSpentVal));
        totalIncome.setText("P" + df.format(totalBudgetVal));
        remaining.setText("P" + df.format(totalBudgetVal - totalSpentVal));
        budgeted.setText("P" + df.format(TransactionInfo.getTotalAllocatedBudget()));
    } //METHOD TO UPDATE DASHBOARD TOTALS

//FOR BUDGET PAGE METHODS
    public void budgetPage() { //METHOD TO CALL BUDGET PAGE VIEW
        panel.removeAll();

        budgetAllocationDesign(panel);
        categoryPanelDesign(panel);
        refreshSavedPeriods();
        budgetDisabled();   //EVERY VISIT STARTS IN VIEW MODE (A DRAFT THAT WAS NOT SET IS DISCARDED)
        syncBudgetForm();

        refreshPanel();
    } //METHOD TO CALL BUDGET PAGE VIEW

    public void budgetAllocationDesign(JPanel panel) { //METHOD TO DESIGN BUDGET ALLOCATION PANEL
        budgetPageTitleL.setBounds(380, 10, 300, 30);
        budgetPageTitleL.setFont(new Font("Serif", Font.BOLD, 22));
        budgetPageTitleL.setForeground(Color.black);
        panel.add(budgetPageTitleL);

        setPageL.setBounds(380, 35, 300, 20);
        setPageL.setFont(new Font("Sans-Serif", Font.PLAIN, 14));
        setPageL.setForeground(Color.gray);
        panel.add(setPageL);

        bp1.setBounds(380, 60, 770, 65);
        bp1.setBackground(Color.white);
        bp1.setLayout(null);
        panel.add(bp1);

        activeL.setBounds(20, 5, 95, 25);
        activeL.setFont(new Font("Sans-Serif", Font.PLAIN, 13));
        activeL.setForeground(Color.darkGray);
        bp1.add(activeL);

        monthYearCB.setBounds(20, 30, 200, 25);
        monthYearCB.setFont(new Font("Serif", Font.BOLD, 16));
        monthYearCB.setForeground(Color.black);
        monthYearCB.setBackground(Color.white);
        bp1.add(monthYearCB);

        totalL.setBounds(250, 5, 115, 25);
        totalL.setFont(new Font("Sans-Serif", Font.PLAIN, 13));
        totalL.setForeground(Color.darkGray);
        bp1.add(totalL);

        totalB.setBounds(250, 25, 150, 30);
        totalB.setFont(new Font("Serif", Font.BOLD, 27));
        totalB.setForeground(Color.black);
        bp1.add(totalB);

        allocatedBL.setBounds(420, 5, 140, 25);
        allocatedBL.setFont(new Font("Sans-Serif", Font.PLAIN, 13));
        allocatedBL.setForeground(Color.darkGray);
        bp1.add(allocatedBL);

        allocatedB.setBounds(420, 25, 150, 30);
        allocatedB.setFont(new Font("Serif", Font.BOLD, 27));
        allocatedB.setForeground(Color.black);
        bp1.add(allocatedB);

        set.setBounds(595, 30, 75, 25);
        set.setFont(new Font("Sans-Serif", Font.BOLD, 13));
        set.setForeground(Color.white);
        set.setBackground(Color.decode("#10B981"));
        bp1.add(set);

        edit.setBounds(678, 30, 75, 25);
        edit.setFont(new Font("Sans-Serif", Font.BOLD, 13));
        edit.setForeground(Color.white);
        edit.setBackground(Color.decode("#2563EB"));
        bp1.add(edit);

        bp2.setBounds(380, 132, 248, 75);
        bp2.setBackground(Color.white);
        bp2.setLayout(null);
        panel.add(bp2);

        monthL.setBounds(12, 10, 150, 20);
        monthL.setFont(new Font("Sans-Serif", Font.BOLD, 13));
        monthL.setForeground(Color.darkGray);
        bp2.add(monthL);

        monthCB.setBounds(12, 33, 224, 30);
        monthCB.setFont(new Font("Sans-Serif", Font.PLAIN, 13));
        monthCB.setBackground(Color.white);
        bp2.add(monthCB);

        bp3.setBounds(641, 132, 248, 75);
        bp3.setBackground(Color.white);
        bp3.setLayout(null);
        panel.add(bp3);

        yearL.setBounds(12, 10, 150, 20);
        yearL.setFont(new Font("Sans-Serif", Font.BOLD, 13));
        yearL.setForeground(Color.darkGray);
        bp3.add(yearL);

        yearTF.setBounds(12, 33, 224, 30);
        yearTF.setFont(new Font("Sans-Serif", Font.PLAIN, 13));
        yearTF.setBackground(Color.white);
        bp3.add(yearTF);

        bp4.setBounds(902, 132, 248, 75);
        bp4.setBackground(Color.white);
        bp4.setLayout(null);
        panel.add(bp4);

        totalBL.setBounds(12, 10, 150, 20);
        totalBL.setFont(new Font("Sans-Serif", Font.BOLD, 13));
        totalBL.setForeground(Color.darkGray);
        bp4.add(totalBL);

        totalBTF.setBounds(12, 33, 224, 30);
        totalBTF.setFont(new Font("Sans-Serif", Font.PLAIN, 13));
        totalBTF.setBackground(Color.white);
        bp4.add(totalBTF);
    } //METHOD TO DESIGN BUDGET ALLOCATION PANEL

    public void categoryPanelDesign(JPanel panel) { //METHOD TO DESIGN CATEGORY BUDGET PANELS
        contentPanel.removeAll();
        contentPanel.setLayout(null);
        contentPanel.setPreferredSize(new Dimension(1540, 700));

        scroll = new JScrollPane(contentPanel);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setBounds(0, 215, 1540, 650);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        panel.add(scroll);

        //LOOP TO CREATE INDIVIDUAL CATEGORY PANELS
        for (int i = 0; i < CATEGORY_FIELDS.length; i++) {
            JPanel categoryPanel = new JPanel(null);
            categoryPanel.setBounds(380 + (i % 2) * 395, 5 + (i / 2) * 120, 375, 110);
            categoryPanel.setBackground(Color.white);
            contentPanel.add(categoryPanel);

            JLabel titleLabel = new JLabel(CATEGORY_NAMES[i]);
            titleLabel.setBounds(12, 10, 200, 20);
            titleLabel.setFont(new Font("Sans-Serif", Font.BOLD, 14));
            titleLabel.setForeground(CATEGORY_COLORS[i]);
            categoryPanel.add(titleLabel);

            JLabel budgetLabel = new JLabel("Budget");
            budgetLabel.setBounds(12, 38, 50, 20);
            budgetLabel.setFont(new Font("Sans-Serif", Font.PLAIN, 13));
            budgetLabel.setForeground(Color.darkGray);
            categoryPanel.add(budgetLabel);

            CATEGORY_FIELDS[i].setBounds(65, 35, 296, 28);
            CATEGORY_FIELDS[i].setFont(new Font("Sans-Serif", Font.PLAIN, 13));
            CATEGORY_FIELDS[i].setBackground(Color.white);
            categoryPanel.add(CATEGORY_FIELDS[i]);

            CATEGORY_SPENT_LABELS[i] = new JLabel("Spent: P0.00");
            CATEGORY_SPENT_LABELS[i].setBounds(12, 75, 172, 20);
            CATEGORY_SPENT_LABELS[i].setFont(new Font("Sans-Serif", Font.PLAIN, 12));
            CATEGORY_SPENT_LABELS[i].setForeground(Color.gray);
            categoryPanel.add(CATEGORY_SPENT_LABELS[i]);

            CATEGORY_LEFT_LABELS[i] = new JLabel("Left: P0.00");
            CATEGORY_LEFT_LABELS[i].setBounds(188, 75, 172, 20);
            CATEGORY_LEFT_LABELS[i].setFont(new Font("Sans-Serif", Font.PLAIN, 12));
            CATEGORY_LEFT_LABELS[i].setForeground(Color.gray);
            CATEGORY_LEFT_LABELS[i].setHorizontalAlignment(SwingConstants.RIGHT);
            categoryPanel.add(CATEGORY_LEFT_LABELS[i]);
        }
    } //METHOD TO DESIGN CATEGORY BUDGET PANELS

    private void refreshSavedPeriods() { //FILLS THE "ACTIVE FOR" COMBOBOX WITH THE SAVED PERIODS
        updatingBudgetUI = true;
        try {
            monthYearCB.removeAllItems();
            draftItem = null;
            ArrayList<String> savedPeriods = TransactionInfo.getSavedMonthYears(currentUsername);
            for (int i = 0; i < savedPeriods.size(); i++) {
                monthYearCB.addItem(savedPeriods.get(i));
            }
            String active = TransactionInfo.getCurrentPeriod();
            if (active.isEmpty()) {
                monthYearCB.setSelectedIndex(-1);
            } else {
                monthYearCB.setSelectedItem(active);
            }
        } finally {
            updatingBudgetUI = false;
        }
    } //FILLS THE "ACTIVE FOR" COMBOBOX WITH THE SAVED PERIODS

    private boolean containsPeriod(String period) { //METHOD TO CHECK IF PERIOD ALREADY EXISTS IN COMBOBOX
        for (int i = 0; i < monthYearCB.getItemCount(); i++) {
            if (period.equals(monthYearCB.getItemAt(i))) {
                return true;
            }
        }
        return false;
    } //METHOD TO CHECK IF PERIOD ALREADY EXISTS IN COMBOBOX

    private void discardDraftItem() { //REMOVES THE "(draft)" PERIOD FROM THE COMBOBOX
        boolean wasUpdating = updatingBudgetUI;
        updatingBudgetUI = true;
        try {
            if (draftItem != null) {
                monthYearCB.removeItem(draftItem);
                draftItem = null;
            }
        } finally {
            updatingBudgetUI = wasUpdating;
        }
    } //REMOVES THE "(draft)" PERIOD FROM THE COMBOBOX

    private void showDraftPeriod(String period) { //SHOWS THE TYPED PERIOD IN "ACTIVE FOR" ("" = SHOW THE SAVED ACTIVE PERIOD)
        boolean wasUpdating = updatingBudgetUI;
        updatingBudgetUI = true;
        try {
            discardDraftItem();
            if (period.isEmpty()) {
                String active = TransactionInfo.getCurrentPeriod();
                if (!active.isEmpty() && containsPeriod(active)) {
                    monthYearCB.setSelectedItem(active);
                } else {
                    monthYearCB.setSelectedIndex(-1);
                }
            } else if (containsPeriod(period)) {
                monthYearCB.setSelectedItem(period);
            } else {
                draftItem = period + DRAFT_TAG;
                monthYearCB.addItem(draftItem);
                monthYearCB.setSelectedItem(draftItem);
            }
        } finally {
            updatingBudgetUI = wasUpdating;
        }
    } //SHOWS THE TYPED PERIOD IN "ACTIVE FOR" ("" = SHOW THE SAVED ACTIVE PERIOD)

    private void syncBudgetForm() { //COPIES THE SAVED ACTIVE PERIOD INTO THE FIELDS, THEN REFRESHES THE LABELS
        updatingBudgetUI = true;
        try {
            String active = TransactionInfo.getCurrentPeriod();
            if (!active.isEmpty()) {
                String[] parts = active.split(" ");
                monthCB.setSelectedItem(parts[0]);
                yearTF.setText(parts[1]);
                totalBTF.setText(df.format(TransactionInfo.getTotalBudget()));
                for (int i = 0; i < CATEGORY_FIELDS.length; i++) {
                    CATEGORY_FIELDS[i].setText(df.format(TransactionInfo.getCategoryBudget(CATEGORY_NAMES[i])));
                }
            } else { //NO BUDGET YET: START FROM THE CURRENT MONTH AND YEAR
                LocalDate today = LocalDate.now();
                monthCB.setSelectedIndex(today.getMonthValue() - 1);
                yearTF.setText(String.valueOf(today.getYear()));
                totalBTF.setText("");
                for (int i = 0; i < CATEGORY_FIELDS.length; i++) {
                    CATEGORY_FIELDS[i].setText("");
                }
            }
        } finally {
            updatingBudgetUI = false;
        }
        formPeriod = draftPeriod();
        refreshBudgetPreview();
    } //COPIES THE SAVED ACTIVE PERIOD INTO THE FIELDS, THEN REFRESHES THE LABELS

    private void followPeriod() { //WHEN MONTH/YEAR POINT TO A DIFFERENT SAVED PERIOD, THE TOTAL AND CATEGORY FIELDS LOAD ITS VALUES (A NEW PERIOD KEEPS WHAT IS TYPED)
        String period = draftPeriod();
        if (period.isEmpty() || period.equals(formPeriod)) {
            return;
        }
        formPeriod = period;
        if (!TransactionInfo.getSavedMonthYears(currentUsername).contains(period)) {
            return;
        }
        updatingBudgetUI = true;
        try {
            totalBTF.setText(df.format(TransactionInfo.getTotalBudget(currentUsername, period)));
            for (int i = 0; i < CATEGORY_FIELDS.length; i++) {
                CATEGORY_FIELDS[i].setText(df.format(TransactionInfo.getCategoryBudget(currentUsername, period, CATEGORY_NAMES[i])));
            }
        } finally {
            updatingBudgetUI = false;
        }
    } //WHEN MONTH/YEAR POINT TO A DIFFERENT SAVED PERIOD, THE TOTAL AND CATEGORY FIELDS LOAD ITS VALUES (A NEW PERIOD KEEPS WHAT IS TYPED)

    private String draftPeriod() { //PERIOD MADE FROM THE MONTH AND YEAR FIELDS ("" WHILE THE YEAR IS NOT VALID YET)
        try {
            int year = Integer.parseInt(yearTF.getText().trim());
            if (year >= 1900 && year <= 9999) {
                return monthCB.getSelectedItem() + " " + year;
            }
        } catch (NumberFormatException handling) {
            //IGNORED WHILE TYPING PARTIAL NUMBERS
        }
        return "";
    } //PERIOD MADE FROM THE MONTH AND YEAR FIELDS ("" WHILE THE YEAR IS NOT VALID YET)

    private double parseAmount(String text) { //READS A TYPED AMOUNT, COMMAS ALLOWED (0 WHILE IT IS EMPTY OR NOT A NUMBER YET)
        try {
            double value = Double.parseDouble(text.trim().replace(",", ""));
            if (Double.isFinite(value) && value >= 0) {
                return value;
            }
        } catch (NumberFormatException handling) {
            //IGNORED WHILE TYPING PARTIAL NUMBERS
        }
        return 0.0;
    } //READS A TYPED AMOUNT, COMMAS ALLOWED (0 WHILE IT IS EMPTY OR NOT A NUMBER YET)

    private void refreshBudgetPreview() { //UPDATES ACTIVE PERIOD, TOTAL BUDGET, ALLOCATED BUDGET AND EVERY SPENT/LEFT LABEL FROM WHAT IS TYPED
        String period = draftPeriod();
        double total = parseAmount(totalBTF.getText());
        double allocated = 0;
        for (int i = 0; i < CATEGORY_FIELDS.length; i++) {
            double categoryBudget = parseAmount(CATEGORY_FIELDS[i].getText());
            double spent = TransactionInfo.getCategorySpent(currentUsername, period, CATEGORY_NAMES[i]);
            allocated += categoryBudget;
            CATEGORY_SPENT_LABELS[i].setText("Spent: P" + df.format(spent));
            CATEGORY_LEFT_LABELS[i].setText("Left: P" + df.format(categoryBudget - spent));
        }
        totalB.setText("P" + df.format(total));
        allocatedB.setText("P" + df.format(allocated));
        if (allocated > total + 0.005) {
            allocatedB.setForeground(Color.decode("#DC2626")); //RED WHEN THE CATEGORIES ADD UP TO MORE THAN THE TOTAL
        } else {
            allocatedB.setForeground(Color.black);
        }
        if (editing) {
            showDraftPeriod(period);
        } else {
            showDraftPeriod("");
        }
    } //UPDATES ACTIVE PERIOD, TOTAL BUDGET, ALLOCATED BUDGET AND EVERY SPENT/LEFT LABEL FROM WHAT IS TYPED

    public void budgetEnabled() { //METHOD TO ENABLE BUDGET EDITING INPUTS
        editing = true;
        set.setEnabled(true);
        monthCB.setEnabled(true);
        yearTF.setEnabled(true);
        totalBTF.setEnabled(true);
        edit.setEnabled(false);
        updateCategoryFieldsState();
        refreshBudgetPreview();
    } //METHOD TO ENABLE BUDGET EDITING INPUTS

    public void budgetDisabled() { //METHOD TO DISABLE BUDGET EDITING INPUTS
        editing = false;
        set.setEnabled(false);
        monthCB.setEnabled(false);
        yearTF.setEnabled(false);
        totalBTF.setEnabled(false);
        for (int i = 0; i < CATEGORY_FIELDS.length; i++) {
            CATEGORY_FIELDS[i].setEnabled(false);
        }
        edit.setEnabled(true);
    } //METHOD TO DISABLE BUDGET EDITING INPUTS

    private void updateCategoryFieldsState() { //CATEGORY FIELDS NEED EDIT MODE + A YEAR + A TOTAL BUDGET
        boolean canEnable = editing && !yearTF.getText().trim().isEmpty() && !totalBTF.getText().trim().isEmpty();
        for (int i = 0; i < CATEGORY_FIELDS.length; i++) {
            CATEGORY_FIELDS[i].setEnabled(canEnable);
        }
    } //CATEGORY FIELDS NEED EDIT MODE + A YEAR + A TOTAL BUDGET

    private void loadBudgetPeriod(String period) { //METHOD TO LOAD A SAVED BUDGET PERIOD
        TransactionInfo.setCurrentPeriod(currentUsername, period);
        transacInfo = TransactionInfo.getTransactionsForPeriod(currentUsername, TransactionInfo.getCurrentPeriod());

        discardDraftItem();
        budgetDisabled();
        syncBudgetForm();
        updateActivePeriodLabel();

        displayingDataInTable();
        updateDashboardTotals();
        clear();
        disabled();
    } //METHOD TO LOAD A SAVED BUDGET PERIOD

    private void saveBudgetSettings() { //SAVE ENTIRE MONTHLY BUDGET SETTINGS
        String yrText = yearTF.getText().trim();
        String totalText = totalBTF.getText().trim().replace(",", "");
        if (yrText.isEmpty() || totalText.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter both year and total budget!", "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int year;
        double totalBudget;
        double[] categoryValues = new double[CATEGORY_FIELDS.length];
        try {
            year = Integer.parseInt(yrText);
            totalBudget = Double.parseDouble(totalText);
            for (int i = 0; i < CATEGORY_FIELDS.length; i++) {
                String valStr = CATEGORY_FIELDS[i].getText().trim().replace(",", "");
                if (valStr.isEmpty()) {
                    categoryValues[i] = 0.0;
                } else {
                    categoryValues[i] = Double.parseDouble(valStr);
                }
            }
        } catch (NumberFormatException handling) {
            JOptionPane.showMessageDialog(null, "Enter valid numbers for the year, total budget, and category budgets! Nothing has been saved.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String period = monthCB.getSelectedItem() + " " + year;
        //TransactionInfo CHECKS EVERYTHING (YEAR RANGE, FUTURE MONTH, NEGATIVES, ALREADY-SPENT, TOTAL) BEFORE SAVING ANYTHING
        if (!TransactionInfo.saveBudgetSettings(currentUsername, period, totalBudget, categoryValues)) {
            return;
        }
        transacInfo = TransactionInfo.getTransactionsForPeriod(currentUsername, TransactionInfo.getCurrentPeriod());
        refreshSavedPeriods();
        budgetDisabled();
        syncBudgetForm();
        updateActivePeriodLabel();
        displayingDataInTable();
        updateDashboardTotals();
        clear();
        disabled();
        JOptionPane.showMessageDialog(null, "Budget settings saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    } //SAVE ENTIRE MONTHLY BUDGET SETTINGS

//FOR TRANSACTIONS PAGE METHODS
    public void transactionsPage() { //METHOD TO CALL TRANSACTIONS PAGE VIEW
        panel.removeAll();

        dashboardHeader(panel);
        tableDesign();
        addTransactionDesign();
        displayingDataInTable();
        updateDashboardTotals();
        disabled();

        refreshPanel();
    } //METHOD TO CALL TRANSACTIONS PAGE VIEW

    private void updateActivePeriodLabel() { //SHOWS THE ACTIVE PERIOD ON THE TRANSACTION FORM
        activeMonthYearL.setText(activePeriodText());
    } //SHOWS THE ACTIVE PERIOD ON THE TRANSACTION FORM

    private void updateCategoryBudgetLabels() { //SHOWS THE BUDGET AND LEFT OF THE CATEGORY CHOSEN IN THE TRANSACTION FORM
        if (categoryCB.getSelectedIndex() <= 0) {
            budgetL.setText("Budget");
            budgetS.setText("");
            return;
        }
        String selectedCategory = categoryCB.getSelectedItem().toString();
        double categoryBudget = TransactionInfo.getCategoryBudget(selectedCategory);
        double spent = TransactionInfo.getCategorySpent(selectedCategory);
        budgetL.setText("Budget: P" + df.format(categoryBudget));
        budgetS.setText("Left: P" + df.format(categoryBudget - spent));
    } //SHOWS THE BUDGET AND LEFT OF THE CATEGORY CHOSEN IN THE TRANSACTION FORM

    public void addTransactionDesign() { //METHOD TO DESIGN THE ADD TRANSACTION FORM PANEL
        menuP.setBounds(1030, 150, 450, 500);
        menuP.setBackground(Color.white);
        menuP.setLayout(null);
        panel.add(menuP);

        titleMenu.setBounds(0, 0, 450, 90);
        titleMenu.setBackground(new Color(30, 58, 138));
        titleMenu.setLayout(null);
        menuP.add(titleMenu);

        addT.setBounds(20, 15, 410, 30);
        addT.setFont(new Font("Serif", Font.BOLD, 26));
        addT.setForeground(Color.white);
        titleMenu.add(addT);

        updateActivePeriodLabel();
        activeMonthYearL.setBounds(20, 48, 410, 20);
        activeMonthYearL.setFont(new Font("Sans-Serif", Font.PLAIN, 14));
        activeMonthYearL.setForeground(Color.decode("#E2E8F0"));
        titleMenu.add(activeMonthYearL);

        categoryCB.setBounds(20, 105, 400, 60);
        categoryCB.setFont(new Font("Sans-Serif", Font.PLAIN, 15));
        categoryCB.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Category", TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION, new Font("Serif", Font.PLAIN, 18), Color.darkGray));
        categoryCB.setBackground(Color.white);
        menuP.add(categoryCB);
        categoryCB.setSelectedIndex(0);
        updateCategoryBudgetLabels();

        itemNumL.setBounds(28, 180, 200, 25);
        itemNumL.setFont(new Font("Serif", Font.PLAIN, 18));
        itemNumL.setForeground(Color.darkGray);
        menuP.add(itemNumL);

        itemNumTF.setBounds(24, 210, 195, 30);
        itemNumTF.setFont(new Font("Sans-Serif", Font.PLAIN, 15));
        itemNumTF.setBackground(Color.white);
        itemNumTF.setText(generatedTransactionNumber());
        menuP.add(itemNumTF);

        itemNameL.setBounds(230, 180, 200, 25);
        itemNameL.setFont(new Font("Serif", Font.PLAIN, 18));
        itemNameL.setForeground(Color.darkGray);
        menuP.add(itemNameL);

        itemNameTF.setBounds(226, 210, 195, 30);
        itemNameTF.setFont(new Font("Sans-Serif", Font.PLAIN, 15));
        menuP.add(itemNameTF);

        amountL.setBounds(28, 255, 200, 25);
        amountL.setFont(new Font("Serif", Font.PLAIN, 18));
        amountL.setForeground(Color.darkGray);
        menuP.add(amountL);

        amountTF.setBounds(24, 285, 195, 30);
        amountTF.setFont(new Font("Sans-Serif", Font.PLAIN, 15));
        menuP.add(amountTF);

        dateL.setBounds(230, 255, 195, 25);
        dateL.setFont(new Font("Serif", Font.PLAIN, 18));
        dateL.setForeground(Color.darkGray);
        menuP.add(dateL);

        dateTF.setBounds(226, 285, 195, 30);
        dateTF.setFont(new Font("Sans-Serif", Font.PLAIN, 15));
        menuP.add(dateTF);

        budgetP.setBounds(24, 335, 397, 45);
        budgetP.setBorder(BorderFactory.createLineBorder(new Color(30, 58, 138), 2, true));
        budgetP.setLayout(null);
        budgetP.setBackground(Color.white);
        menuP.add(budgetP);

        budgetL.setBounds(5, 8, 193, 30);
        budgetL.setFont(new Font("Serif", Font.PLAIN, 16));
        budgetL.setForeground(Color.darkGray);
        budgetP.add(budgetL);

        budgetS.setBounds(198, 8, 193, 30);
        budgetS.setFont(new Font("Serif", Font.PLAIN, 16));
        budgetS.setForeground(Color.BLACK);
        budgetS.setHorizontalAlignment(SwingConstants.RIGHT);
        budgetP.add(budgetS);

        add.setBounds(24, 400, 126, 38);
        add.setFont(new Font("Sans-Serif", Font.BOLD, 14));
        add.setForeground(Color.white);
        add.setBackground(Color.decode("#10B981"));
        menuP.add(add);

        update.setBounds(158, 400, 129, 38);
        update.setFont(new Font("Sans-Serif", Font.BOLD, 14));
        update.setForeground(Color.white);
        update.setBackground(Color.decode("#D97706"));
        menuP.add(update);

        delete.setBounds(293, 400, 128, 38);
        delete.setFont(new Font("Sans-Serif", Font.BOLD, 14));
        delete.setForeground(Color.white);
        delete.setBackground(Color.decode("#DC2626"));
        menuP.add(delete);

        newB.setBounds(24, 450, 195, 38);
        newB.setFont(new Font("Sans-Serif", Font.BOLD, 14));
        newB.setForeground(Color.white);
        newB.setBackground(Color.decode("#2563EB"));
        menuP.add(newB);

        clear.setBounds(226, 450, 195, 38);
        clear.setFont(new Font("Sans-Serif", Font.BOLD, 14));
        clear.setForeground(Color.white);
        clear.setBackground(Color.decode("#6B7280"));
        menuP.add(clear);
    } //METHOD TO DESIGN THE ADD TRANSACTION FORM PANEL

    private void initTransactionTable() { //ONE-TIME SETUP OF THE TRANSACTIONS TABLE AND ITS MOUSE SELECTION LISTENER
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Serif", Font.BOLD, 18));
        table.setFont(new Font("Sans-Serif", Font.PLAIN, 15));
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    itemNumTF.setText(model.getValueAt(selectedRow, 0).toString());
                    itemNameTF.setText(model.getValueAt(selectedRow, 1).toString());
                    categoryCB.setSelectedItem(model.getValueAt(selectedRow, 2).toString());
                    amountTF.setText(model.getValueAt(selectedRow, 3).toString());
                    dateTF.setText(TransactionInfo.getTransactionDay(model.getValueAt(selectedRow, 4).toString()));
                }
            }
        });
    } //ONE-TIME SETUP OF THE TRANSACTIONS TABLE AND ITS MOUSE SELECTION LISTENER

    public void tableDesign() { //METHOD TO PLACE THE TRANSACTIONS TABLE ON THE PAGE
        tableP.setBounds(50, 150, 950, 500);
        tableP.setBackground(Color.white);
        tableP.setLayout(null);
        panel.add(tableP);

        sPane.setBounds(0, 0, 950, 500);
        tableP.add(sPane);
    } //METHOD TO PLACE THE TRANSACTIONS TABLE ON THE PAGE

    public String generatedTransactionNumber() { //METHOD TO GENERATE RANDOM TRANSACTION NUMBER
        int randomNumber = (int) (Math.random() * 10000 + 10000);
        return this.transNum + randomNumber;
    } //METHOD TO GENERATE RANDOM TRANSACTION NUMBER

    public void addDataInArrayList() { //METHOD TO ADD NEW TRANSACTION DATA TO LIST
        String selectedCategory = categoryCB.getSelectedItem().toString();
        String period = TransactionInfo.getCurrentPeriod();
        String description = itemNameTF.getText().trim();
        String dayText = dateTF.getText().trim();
        String amountText = amountTF.getText().trim();
        //PREVENT ADDING IF NO CATEGORY IS SELECTED
        if (selectedCategory.equals(CATEGORY_PROMPT)) {
            JOptionPane.showMessageDialog(null, "Please select a valid category!", "Warning Message", JOptionPane.WARNING_MESSAGE);
            return;
        }
        //CHECK IF MONTHLY BUDGET IS SET
        if (period.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Set a monthly budget first.");
            return;
        }
        //CHECK REQUIRED INPUTS
        if (description.isEmpty() || dayText.isEmpty() || amountText.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Complete all the transaction details!");
            return;
        }
        try {
            double amount = Double.parseDouble(amountText);
            if (!Double.isFinite(amount) || amount <= 0) {
                JOptionPane.showMessageDialog(null, "Amount must be greater than zero.");
                return;
            }
            //CREATE AND VALIDATE FULL DATE
            String fullDate = TransactionInfo.createTransactionDate(period, dayText);
            if (fullDate.isEmpty()) {
                return;
            }
            try {
                TransactionInfo.checkCanSpend(selectedCategory, amount);
            } catch (BudgetExceededException handling) { //SHOWS THE REMAINING BUDGET WHEN THE EXPENSE IS TOO LARGE
                JOptionPane.showMessageDialog(null, "Expense exceeds the remaining category budget.\nRemaining: P"
                        + df.format(handling.getRemaining()), "Budget Exceeded", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to add this transaction?",
                    "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirmation != JOptionPane.YES_OPTION) {
                return;
            }
            //ADD THE TRANSACTION TO THE ACTIVE PERIOD (transacInfo IS THE LIST TransactionInfo STORES)
            transacInfo.add(new TransactionInfo(itemNumTF.getText(), description, selectedCategory, amount, fullDate));

            displayingDataInTable();
            updateDashboardTotals();
            clear();
            disabled();
        } catch (NumberFormatException handling) {
            JOptionPane.showMessageDialog(null, "Enter a valid amount.");
        }
    } //METHOD TO ADD NEW TRANSACTION DATA TO LIST

    public void displayingDataInTable() { //METHOD TO DISPLAY TRANSACTIONS DATA INTO JTABLE
        model.setRowCount(0);
        for (int i = 0; i < transacInfo.size(); i++) {
            TransactionInfo transacItem = transacInfo.get(i);
            model.addRow(new Object[]{
                transacItem.getItemNum(),
                transacItem.getItemName(),
                transacItem.getCategory(),
                transacItem.getAmount(),
                transacItem.getDate()
            });
        }
    } //METHOD TO DISPLAY TRANSACTIONS DATA INTO JTABLE

    private void updateSelectedTransaction() { //METHOD TO UPDATE SELECTED TRANSACTION IN LIST
        int row = table.getSelectedRow();
        //CHECK IF A TRANSACTION IS SELECTED
        if (row < 0) {
            JOptionPane.showMessageDialog(null, "Select a transaction to update!");
            return;
        }
        String itemNum = model.getValueAt(row, 0).toString();
        String description = itemNameTF.getText().trim();
        String dayText = dateTF.getText().trim();
        String amountText = amountTF.getText().trim();
        String selectedCategory = categoryCB.getSelectedItem().toString();
        String period = TransactionInfo.getCurrentPeriod();
        //CHECK IF CATEGORY IS SELECTED
        if (selectedCategory.equals(CATEGORY_PROMPT)) {
            JOptionPane.showMessageDialog(null, "Please select a category!", "Warning Message", JOptionPane.WARNING_MESSAGE);
            return;
        }
        //CHECK IF MONTHLY BUDGET IS SET
        if (period.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Set a monthly budget first.");
            return;
        }
        //CHECK REQUIRED INPUTS
        if (description.isEmpty() || dayText.isEmpty() || amountText.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Complete all the transaction details!");
            return;
        }
        try {
            double amount = Double.parseDouble(amountText);
            if (!Double.isFinite(amount) || amount <= 0) {
                JOptionPane.showMessageDialog(null, "Amount must be greater than zero.");
                return;
            }
            //VALIDATE DATE
            String fullDate = TransactionInfo.createTransactionDate(period, dayText);
            if (fullDate.isEmpty()) {
                return;
            }
            //CHECK CATEGORY BUDGET (THE TRANSACTION BEING EDITED IS NOT COUNTED TWICE)
            try {
                TransactionInfo.checkCanSpend(selectedCategory, amount, itemNum);
            } catch (BudgetExceededException handling) {
                JOptionPane.showMessageDialog(null, "This update exceeds the remaining category budget.\nRemaining: P"
                        + df.format(handling.getRemaining()), "Budget Exceeded", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to update this transaction?",
                    "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirmation != JOptionPane.YES_OPTION) {
                return;
            }
            //FIND AND UPDATE THE SELECTED TRANSACTION
            for (int i = 0; i < transacInfo.size(); i++) {
                TransactionInfo item = transacInfo.get(i);
                if (item.getItemNum().equals(itemNum)) {
                    item.setItemName(description);
                    item.setCategory(selectedCategory);
                    item.setAmount(amount);
                    item.setDate(fullDate);
                    break;
                }
            }
            displayingDataInTable();
            updateDashboardTotals();
            clear();
            disabled();
        } catch (NumberFormatException handling) {
            JOptionPane.showMessageDialog(null, "Enter a valid transaction amount.");
        }
    } //METHOD TO UPDATE SELECTED TRANSACTION IN LIST

    private void deleteSelectedTransaction() { //METHOD TO DELETE SELECTED TRANSACTION FROM LIST
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(null, "Select a transaction to delete!");
            return;
        }
        String itemNum = model.getValueAt(row, 0).toString();
        int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this transaction?",
                "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            for (int i = 0; i < transacInfo.size(); i++) {
                if (transacInfo.get(i).getItemNum().equals(itemNum)) {
                    transacInfo.remove(i);
                    break;
                }
            }
            displayingDataInTable();
            updateDashboardTotals();
            clear();
            disabled();
        }
    } //METHOD TO DELETE SELECTED TRANSACTION FROM LIST

    public void clear() { //METHOD TO CLEAR FORM INPUT FIELDS
        categoryCB.setSelectedIndex(0);
        itemNumTF.setText(generatedTransactionNumber());
        itemNameTF.setText("");
        amountTF.setText("");
        dateTF.setText("");
    } //METHOD TO CLEAR FORM INPUT FIELDS

    public void enabled() { //METHOD TO ENABLE FORM INPUTS
        categoryCB.setEnabled(true);
        itemNumTF.setEditable(false);
        itemNameTF.setEnabled(true);
        amountTF.setEnabled(true);
        dateTF.setEnabled(true);
        add.setEnabled(true);
        update.setEnabled(true);
        delete.setEnabled(true);
        newB.setEnabled(false);
        clear.setEnabled(true);
        table.setRowSelectionAllowed(true);
    } //METHOD TO ENABLE FORM INPUTS

    public void disabled() { //METHOD TO DISABLE FORM INPUTS
        categoryCB.setEnabled(false);
        itemNumTF.setEditable(false);
        itemNameTF.setEnabled(false);
        amountTF.setEnabled(false);
        dateTF.setEnabled(false);
        add.setEnabled(false);
        update.setEnabled(false);
        delete.setEnabled(false);
        newB.setEnabled(true);
        clear.setEnabled(false);
        table.setRowSelectionAllowed(false);
        table.clearSelection();
    } //METHOD TO DISABLE FORM INPUTS    

//FOR SPENDING HISTORY PAGE METHODS
    public void spendingHistoryPage() { //METHOD TO CALL SPENDING HISTORY PAGE VIEW
        panel.removeAll();

        shL.setBounds(295, 10, 300, 30);
        shL.setFont(new Font("Serif", Font.BOLD, 22));
        shL.setForeground(Color.black);
        panel.add(shL);

        shL2.setBounds(295, 35, 300, 20);
        shL2.setFont(new Font("Sans-Serif", Font.PLAIN, 14));
        shL2.setForeground(Color.gray);
        panel.add(shL2);

        search.setBounds(295, 57, 300, 30);
        search.setFont(new Font("Sans-Serif", Font.PLAIN, 15));
        search.setForeground(Color.darkGray);
        panel.add(search);

        searchTF.setBounds(295, 85, 725, 30);
        searchTF.setFont(new Font("Sans-Serif", Font.PLAIN, 15));
        searchTF.setBorder(BorderFactory.createLineBorder(Color.gray, 1, true));
        panel.add(searchTF);

        sort.setBounds(1030, 85, 215, 30);
        sort.setFont(new Font("Sans-Serif", Font.PLAIN, 15));
        sort.setBackground(Color.white);
        panel.add(sort);

        //ONE TABLE FOR EVERY VIEW; refreshHistory() ONLY CHANGES ITS ROWS
        historyModel = new DefaultTableModel(col, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable historyTable = new JTable(historyModel);
        historyTable.setRowSelectionAllowed(false);
        historyTable.setRowHeight(35);
        historyTable.getTableHeader().setFont(new Font("Serif", Font.BOLD, 18));
        historyTable.setFont(new Font("Sans-Serif", Font.PLAIN, 15));

        shPanel.removeAll();
        shPanel.setBackground(Color.white);
        shPanel.setLayout(new BorderLayout());
        shPanel.add(new JScrollPane(historyTable), BorderLayout.CENTER);
        panel.add(shPanel);

        layoutHistoryFilter();
        refreshHistory();
    } //METHOD TO CALL SPENDING HISTORY PAGE VIEW

    private void layoutHistoryFilter() { //SHOWS THE CATEGORY COMBOBOX ONLY FOR "Filter by Category"
        panel.remove(filterC);
        if (sort.getSelectedIndex() == 5) {
            filterC.setBounds(1095, 120, 150, 30);
            filterC.setFont(new Font("Sans-Serif", Font.PLAIN, 15));
            filterC.setBackground(Color.white);
            panel.add(filterC);
            shPanel.setBounds(295, 165, 950, 470);
        } else {
            shPanel.setBounds(295, 135, 950, 500);
        }
        refreshPanel();
    } //SHOWS THE CATEGORY COMBOBOX ONLY FOR "Filter by Category"

    private void refreshHistory() { //REBUILDS THE HISTORY ROWS FROM THE SEARCH TEXT + THE SORT/FILTER CHOICE (THEY WORK TOGETHER)
        if (historyModel == null) {
            return;
        }
        String target = searchTF.getText().trim();
        ArrayList<TransactionInfo> rows;
        if (target.isEmpty()) {
            rows = TransactionInfo.getAllTransactions(currentUsername);
        } else {
            rows = TransactionInfo.linearSearch(currentUsername, target);
        }
        switch (sort.getSelectedIndex()) {
            case 1:
                TransactionInfo.sortByDate(rows, true); //OLDEST TO LATEST
                break;
            case 2:
                TransactionInfo.sortByDate(rows, false); //LATEST TO OLDEST
                break;
            case 3:
                TransactionInfo.sortByAmount(rows, false); //LOWEST TO HIGHEST
                break;
            case 4:
                TransactionInfo.sortByAmount(rows, true); //HIGHEST TO LOWEST
                break;
            case 5:
                String selectedCategory = filterC.getSelectedItem().toString();
                for (int i = rows.size() - 1; i >= 0; i--) {
                    if (!rows.get(i).getCategory().equalsIgnoreCase(selectedCategory)) {
                        rows.remove(i);
                    }
                }
                break;
            default:
                break;
        }
        historyModel.setRowCount(0);
        for (int i = 0; i < rows.size(); i++) {
            TransactionInfo t = rows.get(i);
            historyModel.addRow(new Object[]{
                t.getItemNum(),
                t.getItemName(),
                t.getCategory(),
                t.getAmount(),
                t.getDate()
            });
        }
    } //REBUILDS THE HISTORY ROWS FROM THE SEARCH TEXT + THE SORT/FILTER CHOICE (THEY WORK TOGETHER)

//DOCUMENT LISTENERS
    @Override
    public void insertUpdate(DocumentEvent e) { //TRIGGERED WHEN TEXT IS INSERTED INTO A MONITORED TEXT FIELD DOCUMENT
        handleDocumentChange(e);
    } //TRIGGERED WHEN TEXT IS INSERTED INTO A MONITORED TEXT FIELD DOCUMENT
    //NOTE: TO MAKE A TEXT FIELD WORKABLE FOR LIVE TYPING, ADD .getDocument().addDocumentListener(this); (ONCE, IN THE CONSTRUCTOR)

    @Override
    public void removeUpdate(DocumentEvent e) { //TRIGGERED WHEN TEXT IS REMOVED FROM A MONITORED TEXT FIELD DOCUMENT
        handleDocumentChange(e);
    } //TRIGGERED WHEN TEXT IS REMOVED FROM A MONITORED TEXT FIELD DOCUMENT

    @Override
    public void changedUpdate(DocumentEvent e) { //TRIGGERED WHEN THE STYLE OR ATTRIBUTES OF TEXT CHANGE IN A MONITORED DOCUMENT
        handleDocumentChange(e);
    } //TRIGGERED WHEN THE STYLE OR ATTRIBUTES OF TEXT CHANGE IN A MONITORED DOCUMENT

    private void handleDocumentChange(DocumentEvent e) { //EVERY TYPED CHARACTER LANDS HERE
        if (e.getDocument() == searchTF.getDocument()) {
            refreshHistory();
        } else if (!updatingBudgetUI) { //YEAR, TOTAL BUDGET OR ANY CATEGORY FIELD
            if (e.getDocument() == yearTF.getDocument()) {
                followPeriod();
            }
            updateCategoryFieldsState();
            refreshBudgetPreview();
        }
    } //EVERY TYPED CHARACTER LANDS HERE

//ACTION LISTENERS
    @Override
    public void actionPerformed(ActionEvent e) { //FUNCTIONS OF ALL THE BUTTONS AND TEXTFIELDS
        if (e.getSource().equals(clear)) {
            clear();
            disabled();
        } else if (e.getSource().equals(newB)) {
            enabled();
            itemNumTF.setText(generatedTransactionNumber());
        } else if (e.getSource().equals(add)) {
            addDataInArrayList();
        } else if (e.getSource().equals(update)) {
            updateSelectedTransaction();
        } else if (e.getSource().equals(delete)) {
            deleteSelectedTransaction();
        } else if (e.getSource().equals(edit)) {
            budgetEnabled();
        } else if (e.getSource().equals(set)) {
            saveBudgetSettings();
        } else if (e.getSource().equals(searchTF)) { //ENTER IN THE SEARCH BOX
            String target = searchTF.getText().trim();
            if (!target.isEmpty() && TransactionInfo.linearSearch(currentUsername, target).isEmpty()) {
                JOptionPane.showMessageDialog(null, "Search not found.", "No Results", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    } //FUNCTIONS OF ALL THE BUTTONS AND TEXTFIELDS
    //NOTE: TO MAKE THE BUTTON WORKABLE ADD .addActionListener(), EXAMPLE: button.addActionListener(this);

//ITEM LISTENERS    
    @Override
    public void itemStateChanged(ItemEvent e) { //FUNCTIONS OF ALL THE COMBO BOX (ONLY THE NEWLY SELECTED ITEM COUNTS)
        if (e.getStateChange() != ItemEvent.SELECTED || updatingBudgetUI) {
            return;
        }
        if (e.getSource().equals(categoryCB)) {
            updateCategoryBudgetLabels();
        } else if (e.getSource().equals(monthCB)) {
            if (editing) {
                followPeriod();
                updateCategoryFieldsState();
                refreshBudgetPreview();
            }
        } else if (e.getSource().equals(monthYearCB)) {
            String selected = (String) monthYearCB.getSelectedItem();
            if (selected != null && !selected.endsWith(DRAFT_TAG)) {
                loadBudgetPeriod(selected);
            }
        } else if (e.getSource().equals(sort)) {
            layoutHistoryFilter();
            refreshHistory();
        } else if (e.getSource().equals(filterC)) {
            refreshHistory();
        }
    }//FUNCTIONS OF ALL THE COMBO BOX (ONLY THE NEWLY SELECTED ITEM COUNTS)
    //NOTE: TO MAKE THE COMBOBOX WORKABLE ADD .addItemListener(), EXAMPLE: combobox.addItemListener(this);
}
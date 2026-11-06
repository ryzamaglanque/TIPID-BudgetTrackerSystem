package oopproject;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import javax.swing.JOptionPane;

public class TransactionInfo {

//GLOBAL VARIABLES SECTION
    //STORES TRANSACTION DETAILS
    private String itemNum;
    private String itemName;
    private String category;
    private double amount;
    private String date;
    //LIST OF MONTH NAMES FOR VALIDATION
    private static final String[] MONTHS = {"January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"};
    //THE ONE AND ONLY LIST OF CATEGORIES (EVERY CLASS READS IT THROUGH getCategories())
    private static final String[] CATEGORIES = {"🍔 Food & Dining", "💡 Bills & Utilities", "💳 Debt Payments", "💰 Savings",
        "🚗 Transport", "🏥 Health", "🎮 Entertainment", "📦 Other"};
    //DATE FORMAT USED BY EVERY TRANSACTION DATE
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
    //TOLERANCE SO THAT VALUES LIKE 0.1 + 0.2 ARE TREATED AS EQUAL TO 0.3
    private static final double EPS = 0.005;
    //STORES BUDGETS AND TRANSACTIONS SEPARATELY FOR EACH MONTH AND YEAR
    private static final ArrayList<BudgetPeriod> BUDGETS = new ArrayList<>();
    private static String activeUsername = "";
    private static String activePeriod = "";

    private static class BudgetPeriod { //CLASS TO KEEP TRACK OF BUDGETS AND TRANSACTIONS PER PERIOD

        final String USERNAME;
        final String PERIOD;
        double totalBudget;
        final double[] CATEGORY_BUDGETS = new double[CATEGORIES.length]; //SAME ORDER AS CATEGORIES
        final ArrayList<TransactionInfo> TRANSACTIONS = new ArrayList<>();

        BudgetPeriod(String username, String period) {
            this.USERNAME = username;
            this.PERIOD = period;
        }
    } //CLASS TO KEEP TRACK OF BUDGETS AND TRANSACTIONS PER PERIOD

//FOR BUDGET PAGE METHODS  
    public static String[] getMonths() {
        return MONTHS.clone();
    } //GETS A COPY OF THE MONTHS ARRAY

    public static String[] getCategories() {
        return CATEGORIES.clone();
    } //GETS A COPY OF THE CATEGORIES ARRAY

    private static int monthIndex(String name) { //FINDS THE POSITION (0-11) OF A MONTH NAME, -1 IF IT DOES NOT EXIST
        for (int i = 0; i < MONTHS.length; i++) {
            if (MONTHS[i].equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    } //FINDS THE POSITION (0-11) OF A MONTH NAME, -1 IF IT DOES NOT EXIST

    private static int categoryIndex(String category) { //FINDS THE POSITION OF A CATEGORY, -1 IF IT DOES NOT EXIST
        for (int i = 0; i < CATEGORIES.length; i++) {
            if (CATEGORIES[i].equals(category)) {
                return i;
            }
        }
        return -1;
    } //FINDS THE POSITION OF A CATEGORY, -1 IF IT DOES NOT EXIST

    public static void setCurrentPeriod(String username, String period) { //SETS THE CURRENTLY ACTIVE USER AND MONTH/YEAR PERIOD
        if (username == null) {
            activeUsername = "";
        } else {
            activeUsername = username.trim();
        }
        if (period == null || period.trim().isEmpty()) {
            activePeriod = "";
            return;
        }
        String normalized = normalizePeriod(period);
        if (!normalized.isEmpty()) {
            activePeriod = normalized;
            findOrCreateBudget(activeUsername, activePeriod);
        }
    } //SETS THE CURRENTLY ACTIVE USER AND MONTH/YEAR PERIOD

    public static String getCurrentPeriod() {
        return activePeriod;
    } //GETS THE CURRENT ACTIVE PERIOD

    public static ArrayList<String> getSavedMonthYears(String username) { //GETS A LIST OF ALL SAVED MONTH-YEAR PERIODS FOR A USER
        ArrayList<String> periods = new ArrayList<>();
        String selectedUsername = "";
        if (username != null) {
            selectedUsername = username.trim();
        }
        for (int i = 0; i < BUDGETS.size(); i++) {
            if (BUDGETS.get(i).USERNAME.equals(selectedUsername)) {
                periods.add(BUDGETS.get(i).PERIOD);
            }
        }
        return periods;
    } //GETS A LIST OF ALL SAVED MONTH-YEAR PERIODS FOR A USER

    private static BudgetPeriod findBudget(String username, String period) { //FINDS AN EXISTING BUDGET PERIOD FOR A USER (NULL IF NONE)
        for (int i = 0; i < BUDGETS.size(); i++) {
            BudgetPeriod budget = BUDGETS.get(i);
            if (budget.USERNAME.equals(username) && budget.PERIOD.equals(period)) {
                return budget;
            }
        }
        return null;
    } //FINDS AN EXISTING BUDGET PERIOD FOR A USER (NULL IF NONE)

    private static BudgetPeriod findOrCreateBudget(String username, String period) { //FINDS A BUDGET PERIOD OR CREATES ONE IF IT DOES NOT EXIST YET
        BudgetPeriod budget = findBudget(username, period);
        if (budget == null) {
            budget = new BudgetPeriod(username, period);
            BUDGETS.add(budget);
        }
        return budget;
    } //FINDS A BUDGET PERIOD OR CREATES ONE IF IT DOES NOT EXIST YET

    private static BudgetPeriod currentBudget() { //GETS THE ACTIVE BUDGET PERIOD FOR THE LOGGED-IN USER
        if (activeUsername.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No active user is logged in.", "Session Error", JOptionPane.ERROR_MESSAGE);
            return new BudgetPeriod("", "");
        }
        BudgetPeriod budget = findBudget(activeUsername, activePeriod);
        if (budget == null) {
            return new BudgetPeriod(activeUsername, activePeriod); //NO PERIOD SET YET: EMPTY BUDGET THAT IS NOT STORED
        }
        return budget;
    } //GETS THE ACTIVE BUDGET PERIOD FOR THE LOGGED-IN USER

    public static double getTotalBudget() {
        return currentBudget().totalBudget;
    } //GETS THE TOTAL BUDGET AMOUNT OF THE ACTIVE PERIOD

    public static double getCategoryBudget(String category) { //GETS THE BUDGET SET FOR A SPECIFIC CATEGORY
        int index = categoryIndex(category);
        if (index == -1) {
            return 0.0;
        }
        return currentBudget().CATEGORY_BUDGETS[index];
    } //GETS THE BUDGET SET FOR A SPECIFIC CATEGORY

    public static double getTotalBudget(String username, String period) { //TOTAL BUDGET OF ANY SAVED PERIOD (0 IF IT IS NOT SAVED; DOES NOT CREATE IT)
        BudgetPeriod budget = findBudget(username, period);
        if (budget == null) {
            return 0.0;
        }
        return budget.totalBudget;
    } //TOTAL BUDGET OF ANY SAVED PERIOD (0 IF IT IS NOT SAVED; DOES NOT CREATE IT)

    public static double getCategoryBudget(String username, String period, String category) { //CATEGORY BUDGET OF ANY SAVED PERIOD (0 IF IT IS NOT SAVED; DOES NOT CREATE IT)
        BudgetPeriod budget = findBudget(username, period);
        int index = categoryIndex(category);
        if (budget == null || index == -1) {
            return 0.0;
        }
        return budget.CATEGORY_BUDGETS[index];
    } //CATEGORY BUDGET OF ANY SAVED PERIOD (0 IF IT IS NOT SAVED; DOES NOT CREATE IT)

    public static double getTotalAllocatedBudget() { //GETS THE TOTAL AMOUNT ALLOCATED ACROSS ALL CATEGORIES
        double total = 0;
        double[] values = currentBudget().CATEGORY_BUDGETS;
        for (int i = 0; i < values.length; i++) {
            total += values[i];
        }
        return total;
    } //GETS THE TOTAL AMOUNT ALLOCATED ACROSS ALL CATEGORIES

    private static double spentIn(BudgetPeriod budget, String category, String excludedItemNum) { //SUMS THE SPENDING OF A CATEGORY (OPTIONALLY SKIPPING ONE TRANSACTION)
        double spent = 0;
        if (budget == null) {
            return spent;
        }
        for (int i = 0; i < budget.TRANSACTIONS.size(); i++) {
            TransactionInfo item = budget.TRANSACTIONS.get(i);
            boolean isExcluded = (excludedItemNum != null && excludedItemNum.equals(item.getItemNum()));
            if (!isExcluded && category.equals(item.getCategory())) {
                spent += item.getAmount();
            }
        }
        return spent;
    } //SUMS THE SPENDING OF A CATEGORY (OPTIONALLY SKIPPING ONE TRANSACTION)

    public static double getCategorySpent(String category) { //TOTAL SPENT IN A CATEGORY FOR THE ACTIVE PERIOD
        return spentIn(currentBudget(), category, null);
    } //TOTAL SPENT IN A CATEGORY FOR THE ACTIVE PERIOD

    public static double getCategorySpent(String username, String period, String category) { //TOTAL SPENT IN A CATEGORY FOR ANY PERIOD (DOES NOT CREATE THE PERIOD)
        return spentIn(findBudget(username, period), category, null);
    } //TOTAL SPENT IN A CATEGORY FOR ANY PERIOD (DOES NOT CREATE THE PERIOD)

    private static boolean canSpendInCategory(String category, double newAmount, String excludedItemNum) { //CHECKS IF A NEW EXPENSE FITS WITHIN THE REMAINING CATEGORY BUDGET
        double spent = spentIn(currentBudget(), category, excludedItemNum);
        return newAmount > 0 && spent + newAmount <= getCategoryBudget(category) + EPS;
    } //CHECKS IF A NEW EXPENSE FITS WITHIN THE REMAINING CATEGORY BUDGET

    public static void checkCanSpend(String category, double newAmount, String excludedItemNum) throws BudgetExceededException { //CHECKS THE CATEGORY BUDGET AND THROWS AN EXCEPTION IF EXCEEDED
        if (!canSpendInCategory(category, newAmount, excludedItemNum)) {
            double left = getCategoryBudget(category) - spentIn(currentBudget(), category, excludedItemNum);
            throw new BudgetExceededException(Math.max(0, left));
        }
    } //CHECKS THE CATEGORY BUDGET AND THROWS AN EXCEPTION IF EXCEEDED

    public static void checkCanSpend(String category, double newAmount) throws BudgetExceededException { //OVERLOADED VERSION FOR NEW TRANSACTIONS (NO EXCLUDED ITEM)
        checkCanSpend(category, newAmount, null);
    } //OVERLOADED VERSION FOR NEW TRANSACTIONS (NO EXCLUDED ITEM)

    public static boolean saveBudgetSettings(String username, String period, double totalBudget, double[] categoryValues) { //VALIDATES EVERYTHING FIRST, THEN SAVES TOTAL AND CATEGORY BUDGETS TOGETHER
        String normalized = normalizePeriod(period);
        if (normalized.isEmpty()) {
            return false;
        }
        if (!Double.isFinite(totalBudget) || totalBudget < 0) {
            JOptionPane.showMessageDialog(null, "Total budget must be a non-negative number!", "Budget Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (categoryValues.length != CATEGORIES.length) {
            return false;
        }
        //PREVENT FUTURE BUDGET PERIODS
        String[] parts = normalized.split(" ");
        YearMonth periodMonth = YearMonth.of(Integer.parseInt(parts[1]), monthIndex(parts[0]) + 1);
        if (periodMonth.isAfter(YearMonth.now())) {
            JOptionPane.showMessageDialog(null, "You cannot set a budget period for a future month!", "Budget Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        BudgetPeriod existing = findBudget(username, normalized); //NULL WHEN THE PERIOD IS NEW
        double totalAllocated = 0;
        for (int i = 0; i < CATEGORIES.length; i++) {
            double amount = categoryValues[i];
            if (!Double.isFinite(amount) || amount < 0) {
                JOptionPane.showMessageDialog(null, "Category budgets must be non-negative numbers!", "Budget Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            //CATEGORY BUDGET MUST COVER EXISTING SPENDING
            if (amount + EPS < spentIn(existing, CATEGORIES[i], null)) {
                JOptionPane.showMessageDialog(null, CATEGORIES[i] + " budget cannot be less than the amount already spent!", "Budget Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            totalAllocated += amount;
        }
        if (totalAllocated > totalBudget + EPS) {
            JOptionPane.showMessageDialog(null, "Total Budget cannot be lower than the allocated category budgets! Nothing has been saved.",
                    "Budget Limit Reached", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        //EVERYTHING IS VALID: ONLY NOW THE PERIOD IS CREATED AND SELECTED
        setCurrentPeriod(username, normalized);
        BudgetPeriod budget = currentBudget();
        budget.totalBudget = totalBudget;
        for (int i = 0; i < CATEGORIES.length; i++) {
            budget.CATEGORY_BUDGETS[i] = categoryValues[i];
        }
        return true;
    } //VALIDATES EVERYTHING FIRST, THEN SAVES TOTAL AND CATEGORY BUDGETS TOGETHER

//FOR TRANSACTIONS PAGE METHODS
    public TransactionInfo(String itemNum, String itemName, String category, double amount, String date) { //CONSTRUCTOR WITH VALUES FOR A TRANSACTION
        this.itemNum = itemNum;
        this.itemName = itemName;
        this.category = category;
        this.amount = amount;
        this.date = date;
    } //CONSTRUCTOR WITH VALUES FOR A TRANSACTION

    public String getItemNum() {
        return itemNum;
    } //GETS THE TRANSACTION NUMBER

    public void setItemNum(String itemNum) {
        this.itemNum = itemNum;
    } //SETS THE TRANSACTION NUMBER

    public String getItemName() {
        return itemName;
    } //GETS THE ITEM NAME

    public void setItemName(String itemName) {
        this.itemName = itemName;
    } //SETS THE ITEM NAME

    public String getCategory() {
        return category;
    } //GETS THE CATEGORY

    public void setCategory(String category) {
        this.category = category;
    } //SETS THE CATEGORY

    public double getAmount() {
        return amount;
    } //GETS THE TRANSACTION AMOUNT

    public void setAmount(double amount) {
        this.amount = amount;
    } //SETS THE TRANSACTION AMOUNT

    public String getDate() {
        return date;
    } //GETS THE DATE

    public void setDate(String date) {
        this.date = date;
    } //SETS THE DATE

    public static String normalizePeriod(String period) { //FIXES MONTH AND YEAR TEXT FORMAT SO DIFFERENT CAPITALIZATIONS MATCH
        if (period == null || period.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Month and year are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return "";
        }
        String[] parts = period.trim().split("\\s+");
        if (parts.length != 2) {
            JOptionPane.showMessageDialog(null, "Use a month and year, such as May 2026.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return "";
        }
        int month = monthIndex(parts[0]);
        if (month == -1) {
            JOptionPane.showMessageDialog(null, "Invalid month.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return "";
        }
        int year;
        try {
            year = Integer.parseInt(parts[1]);
        } catch (NumberFormatException handling) {
            JOptionPane.showMessageDialog(null, "Invalid year.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return "";
        }
        if (year < 1900 || year > 9999) {
            JOptionPane.showMessageDialog(null, "Year must be from 1900 to 9999.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return "";
        }
        return MONTHS[month] + " " + year;
    } //FIXES MONTH AND YEAR TEXT FORMAT SO DIFFERENT CAPITALIZATIONS MATCH

    public static String createTransactionDate(String period, String dayText) { //CHECKS IF THE ENTERED DAY IS VALID AND CREATES THE FULL DATE
        String normalized = normalizePeriod(period);
        if (normalized.isEmpty()) {
            return "";
        }
        String[] parts = normalized.split(" ");
        int day;
        try {
            day = Integer.parseInt(dayText.trim());
        } catch (NumberFormatException handling) {
            JOptionPane.showMessageDialog(null, "Enter a valid day, such as 15.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return "";
        }
        try {
            LocalDate date = LocalDate.of(Integer.parseInt(parts[1]), monthIndex(parts[0]) + 1, day);
            //PREVENT FUTURE TRANSACTION DATES
            if (date.isAfter(LocalDate.now())) {
                JOptionPane.showMessageDialog(null, "You cannot add a transaction for a future date!", "Date Error", JOptionPane.ERROR_MESSAGE);
                return "";
            }
            return date.format(DATE_FORMAT);
        } catch (DateTimeException handling) {
            JOptionPane.showMessageDialog(null, "That day does not exist in " + normalized + ".", "Date Error", JOptionPane.ERROR_MESSAGE);
            return "";
        }
    } //CHECKS IF THE ENTERED DAY IS VALID AND CREATES THE FULL DATE

    public static String getTransactionDay(String fullDate) { //GETS ONLY THE DAY NUMBER FROM A FULL DATE STRING
        try {
            return String.valueOf(LocalDate.parse(fullDate, DATE_FORMAT).getDayOfMonth());
        } catch (DateTimeException handling) {
            JOptionPane.showMessageDialog(null, "Invalid saved transaction date.", "Date Error", JOptionPane.ERROR_MESSAGE);
            return "";
        }
    } //GETS ONLY THE DAY NUMBER FROM A FULL DATE STRING

    public static ArrayList<TransactionInfo> getTransactionsForPeriod(String username, String period) { //GETS THE (LIVE) TRANSACTION LIST OF A SPECIFIC USER AND PERIOD
        if (username == null || period == null || period.trim().isEmpty()) {
            return new ArrayList<>();
        }
        String normalized = normalizePeriod(period);
        if (normalized.isEmpty()) {
            return new ArrayList<>();
        }
        return findOrCreateBudget(username.trim(), normalized).TRANSACTIONS;
    } //GETS THE (LIVE) TRANSACTION LIST OF A SPECIFIC USER AND PERIOD

//FOR SPENDING HISTORY PAGE METHODS
    public static ArrayList<TransactionInfo> getAllTransactions(String username) { //GET A COPY OF ALL TRANSACTIONS FROM ALL MONTHS AND YEARS
        ArrayList<TransactionInfo> allTransactions = new ArrayList<>();
        if (username == null || username.trim().isEmpty()) {
            return allTransactions;
        }
        for (int i = 0; i < BUDGETS.size(); i++) {
            if (BUDGETS.get(i).USERNAME.equals(username.trim())) {
                allTransactions.addAll(BUDGETS.get(i).TRANSACTIONS);
            }
        }
        return allTransactions;
    } //GET A COPY OF ALL TRANSACTIONS FROM ALL MONTHS AND YEARS

    private static boolean matchesSearch(TransactionInfo t, String target) { //CHECKS IF ANY FIELD OF A TRANSACTION MATCHES THE SEARCH TEXT (target MUST BE LOWERCASE)
        return t.getItemName().toLowerCase().contains(target)
                || t.getItemNum().toLowerCase().contains(target)
                || t.getDate().toLowerCase().contains(target)
                || t.getCategory().toLowerCase().contains(target)
                || String.valueOf(t.getAmount()).contains(target);
    } //CHECKS IF ANY FIELD OF A TRANSACTION MATCHES THE SEARCH TEXT (target MUST BE LOWERCASE)

    public static ArrayList<TransactionInfo> linearSearch(String username, String target) { //SEARCHES TRANSACTIONS BY DESCRIPTION, NUMBER, DATE, CATEGORY, OR AMOUNT
        ArrayList<TransactionInfo> all = getAllTransactions(username);
        ArrayList<TransactionInfo> results = new ArrayList<>();
        String key = target.toLowerCase().trim();
        for (int i = 0; i < all.size(); i++) {
            if (matchesSearch(all.get(i), key)) {
                results.add(all.get(i));
            }
        }
        return results;
    } //SEARCHES TRANSACTIONS BY DESCRIPTION, NUMBER, DATE, CATEGORY, OR AMOUNT

    public static void sortByDate(ArrayList<TransactionInfo> transactions, boolean oldestFirst) { //FROM CC105 INSERTIONSORT ALGORITHM (SORT BY DATE)
        for (int i = 1; i < transactions.size(); i++) {
            TransactionInfo key = transactions.get(i);
            LocalDate keyDate = LocalDate.parse(key.getDate(), DATE_FORMAT);
            int j = i - 1;
            while (j >= 0) {
                LocalDate previousDate = LocalDate.parse(transactions.get(j).getDate(), DATE_FORMAT);
                boolean shouldMove;
                if (oldestFirst) {
                    shouldMove = previousDate.isAfter(keyDate);
                } else {
                    shouldMove = previousDate.isBefore(keyDate);
                }
                if (!shouldMove) {
                    break;
                }
                transactions.set(j + 1, transactions.get(j));
                j--;
            }
            transactions.set(j + 1, key);
        }
    } //FROM CC105 INSERTIONSORT ALGORITHM (SORT BY DATE)

    public static void sortByAmount(ArrayList<TransactionInfo> transactions, boolean highestFirst) { //FROM CC105 INSERTIONSORT ALGORITHM (SORT BY AMOUNT)
        for (int i = 1; i < transactions.size(); i++) {
            TransactionInfo key = transactions.get(i);
            int j = i - 1;
            while (j >= 0) {
                double previousAmount = transactions.get(j).getAmount();
                boolean shouldMove;
                if (highestFirst) {
                    shouldMove = previousAmount < key.getAmount();
                } else {
                    shouldMove = previousAmount > key.getAmount();
                }
                if (!shouldMove) {
                    break;
                }
                transactions.set(j + 1, transactions.get(j));
                j--;
            }
            transactions.set(j + 1, key);
        }
    } //FROM CC105 INSERTIONSORT ALGORITHM (SORT BY AMOUNT)

//DEFAULT DATA VALUES FOR DEFAULT USER ACCOUNT
    private static void addDefaultPeriod(String period, double total, double[] categoryBudgets, TransactionInfo[] items) { //CREATES ONE DEFAULT BUDGET PERIOD FOR TIPIDA
        BudgetPeriod budget = findOrCreateBudget("Tipida", period);
        budget.totalBudget = total;
        for (int i = 0; i < categoryBudgets.length; i++) {
            budget.CATEGORY_BUDGETS[i] = categoryBudgets[i];
        }
        for (int i = 0; i < items.length; i++) {
            budget.TRANSACTIONS.add(items[i]);
        }
    } //CREATES ONE DEFAULT BUDGET PERIOD FOR TIPIDA

    public static void initializeDefaultData() { //DEFAULT DATA VALUES FOR TIPIDA ACCOUNT
        //CATEGORY ORDER: 0 FOOD, 1 BILLS, 2 DEBT, 3 SAVINGS, 4 TRANSPORT, 5 HEALTH, 6 ENTERTAINMENT, 7 OTHER
        addDefaultPeriod("May 2025", 15000.0,
                new double[]{5000, 4000, 0, 0, 0, 0, 2000, 0},
                new TransactionInfo[]{
                    new TransactionInfo("TN10001", "Old Diner", CATEGORIES[0], 450.0, "May 3, 2025"),
                    new TransactionInfo("TN10002", "Electric Bill", CATEGORIES[1], 2200.0, "May 10, 2025"),
                    new TransactionInfo("TN10003", "Cinema Ticket", CATEGORIES[6], 350.0, "May 14, 2025"),
                    new TransactionInfo("TN10004", "Grocery Store", CATEGORIES[0], 1200.0, "May 20, 2025"),
                    new TransactionInfo("TN10005", "Water Utility", CATEGORIES[1], 850.0, "May 28, 2025")
                });
        addDefaultPeriod("September 2026", 18000.0,
                new double[]{6000, 4500, 0, 0, 0, 0, 0, 3500},
                new TransactionInfo[]{
                    new TransactionInfo("TN10006", "Supermarket", CATEGORIES[0], 1800.0, "September 2, 2026"),
                    new TransactionInfo("TN10007", "Internet Bill", CATEGORIES[1], 1500.0, "September 8, 2026"),
                    new TransactionInfo("TN10008", "Clothing Store", CATEGORIES[7], 2100.0, "September 15, 2026"),
                    new TransactionInfo("TN10009", "Fast Food", CATEGORIES[0], 650.0, "September 21, 2026"),
                    new TransactionInfo("TN10010", "Electronics Shop", CATEGORIES[7], 1200.0, "September 27, 2026")
                });
        addDefaultPeriod("October 2026", 20000.0,
                new double[]{7000, 5000, 0, 4000, 0, 0, 0, 0},
                new TransactionInfo[]{
                    new TransactionInfo("TN10011", "Organic Market", CATEGORIES[0], 1450.0, "October 4, 2026"),
                    new TransactionInfo("TN10012", "Power & Light", CATEGORIES[1], 2800.0, "October 10, 2026"),
                    new TransactionInfo("TN10013", "Coffee Shop", CATEGORIES[0], 280.0, "October 14, 2026"),
                    new TransactionInfo("TN10014", "Emergency Savings Deposit", CATEGORIES[3], 3000.0, "October 18, 2026"),
                    new TransactionInfo("TN10015", "Restaurant Dinner", CATEGORIES[0], 1100.0, "October 21, 2026")
                });
    } //DEFAULT DATA VALUES FOR TIPIDA ACCOUNT

//ADMIN ACCOUNT
    public static void deleteTransactions(String username) { //DELETE SELECTED USER IN AdminAccount
        for (int i = BUDGETS.size() - 1; i >= 0; i--) {
            if (BUDGETS.get(i).USERNAME.equalsIgnoreCase(username)) {
                BUDGETS.remove(i);
            }
        }
        if (activeUsername.equalsIgnoreCase(username)) {
            activeUsername = "";
            activePeriod = "";
        }
    } //DELETE SELECTED USER IN AdminAccount

}
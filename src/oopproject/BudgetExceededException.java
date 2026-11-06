package oopproject;

public class BudgetExceededException extends Exception { //CUSTOM EXCEPTION WHEN AN EXPENSE GOES OVER THE CATEGORY BUDGET

    private final double REMAINING; //REMAINING BUDGET OF THE CATEGORY

    public BudgetExceededException(double remaining) { //CONSTRUCTOR THAT STORES THE REMAINING BUDGET
        super("Expense exceeds the remaining category budget.");
        this.REMAINING = remaining;
    } //CONSTRUCTOR THAT STORES THE REMAINING BUDGET

    public double getRemaining() {
        return REMAINING;
    } //GETS THE REMAINING BUDGET
} //CUSTOM EXCEPTION WHEN AN EXPENSE GOES OVER THE CATEGORY BUDGET
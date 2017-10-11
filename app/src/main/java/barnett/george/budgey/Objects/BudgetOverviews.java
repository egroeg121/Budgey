package barnett.george.budgey.Objects;


import java.util.ArrayList;

public class BudgetOverviews {

    double amount;
    double totalamount;
    long startdate;
    long nextdate;

    public BudgetOverviews( double amount,double totalamount, long startdate, long nextdate) {
        this.amount = amount;
        this.totalamount = totalamount;
        this.startdate = startdate;
        this.nextdate = nextdate;
    }

    public double getTotalamount() {
        return totalamount;
    }

    public long getStartdate() {
        return startdate;
    }

    public long getNextdate() {
        return nextdate;
    }

    public double getAmount() {
        return amount;
    }

    public void setTotalamount(double totalamount) {
        this.totalamount = totalamount;
    }

    public void setStartdate(long startdate) {
        this.startdate = startdate;
    }

    public void setNextdate(long nextdate) {
        this.nextdate = nextdate;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}

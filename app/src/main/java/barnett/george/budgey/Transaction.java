package barnett.george.budgey;

public class Transaction {

    /*     Main Variables      */
    int id;
    String name;
    double amount;
    long date;
    String category;
    int recurringid;

    /*     Constructor      */
    public Transaction(int id, String name, double amount, long date, String category, int recurringid) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.recurringid = recurringid;
    }

    /*     Getters      */
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public double getAmount() {
        return amount;
    }
    public long getDate() {
        return date;
    }
    public String getCategory() {
        return category;
    }
    public int getRecurringid() {
        return recurringid;
    }
    public String getDateString(){
        DateHandler dateHandler = new DateHandler();
        String DateString = dateHandler.MillitoDateString(date);
        return DateString;
    }


    /*     Setters      */
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public void setDate(long date) {
        this.date = date;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setRecurringid(int recurringid) {
        this.recurringid = recurringid;
    }

    /*    Other Methods     */
    //
}

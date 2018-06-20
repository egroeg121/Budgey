package barnett.george.budgey;


import java.util.ArrayList;

public class Budget {

    int ID;
    String name;
    double totalamount;
    String categorystring;
    long nextdate;
    int numofunit;
    int timetype;
    double amount;

    public Budget(int ID, String name, double totalamount, String categorystring, long nextdate, int numofunit, int timetype, double amount) {
        this.ID = ID;
        this.name = name;
        this.totalamount = totalamount;
        this.categorystring = categorystring;
        this.nextdate = nextdate;
        this.numofunit = numofunit;
        this.timetype = timetype;
        this.amount = amount;
    }

    public int getID() {
        return ID;
    }
    public String getName() {
        return name;
    }
    public double getTotalAmount() {
        return totalamount;
    }
    public String getCategoryString() {
        return categorystring;
    }
    public long getNextDate() {
        return nextdate;
    }
    public int getNumofUnit() {
        return numofunit;
    }
    public int getTimeType() {
        return timetype;
    }
    public double getAmount() {
        return amount;
    }
    public String[] getCategoryList(){
        String[] CategoryList = categorystring.split(";");
        return CategoryList;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setTotalAmount(double totalamount) {
        this.totalamount = totalamount;
    }
    public void setCategorystring(String categorystring) {
        this.categorystring = categorystring;
    }
    public void setNextdate(long nextdate) {
        this.nextdate = nextdate;
    }
    public void setNumofunit(int numofunit) {
        this.numofunit = numofunit;
    }
    public void setTimeType(int timetype) {
        this.timetype = timetype;
    }
    public void setAmout(double amount) {
        this.amount = amount;
    }
    public void setCategoryList (String [] categorylist){
        categorystring = null;
        for (int i = 0; i < categorylist.length; i++) {
           categorystring = categorystring + ";" + categorylist[i];
        }
    }
}

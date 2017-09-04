package barnett.george.budgey;


import java.util.ArrayList;

public class Budget {

    int ID;
    String Name;
    double totalamount;
    String categorystring;
    long nextdate;
    int numofunit;
    int typetime;
    double currentamount;

    public Budget(int ID, String name, double totalamount, String categorystring, long nextdate, int numofunit, int typetime, double currentamount) {
        this.ID = ID;
        Name = name;
        this.totalamount = totalamount;
        this.categorystring = categorystring;
        this.nextdate = nextdate;
        this.numofunit = numofunit;
        this.typetime = typetime;
        this.currentamount = currentamount;
    }

    public int getID() {
        return ID;
    }
    public String getName() {
        return Name;
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
    public int getTypeTime() {
        return typetime;
    }
    public double getCurrentAmount() {
        return currentamount;
    }
    public String[] getCategoryList(){
        String[] CategoryList = categorystring.split(";");
        return CategoryList;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
    public void setName(String name) {
        Name = name;
    }
    public void setTotalamount(double totalamount) {
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
    public void setTypetime(int typetime) {
        this.typetime = typetime;
    }
    public void setCurrentamount(double currentamount) {
        this.currentamount = currentamount;
    }
    public void setCategoryList (String [] categorylist){
        categorystring = null;
        for (int i = 0; i < categorylist.length; i++) {
           categorystring = categorystring + ";" + categorylist[i];
        }
    }
}

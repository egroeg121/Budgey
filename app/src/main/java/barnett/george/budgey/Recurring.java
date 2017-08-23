package barnett.george.budgey;


public class Recurring {

    /*
        Main Variables
    */
    int id = 0;
    String name;
    double amount;
    long startdate;
    long nextdate;
    String category;
    int numofunit;
    int timetype;
    int counter;


    /*
        Constructor
    */

    public Recurring(int id, String name, double amount, long startdate, long nextdate, String category, int numofunit, int timetype, int counter) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.startdate = startdate;
        this.nextdate = nextdate;
        this.category = category;
        this.numofunit = numofunit;
        this.timetype = timetype;
        this.counter = counter;
    }

    /*
         Getters
    */

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public double getAmount() {
        return amount;
    }
    public long getStartdate() {
        return startdate;
    }
    public long getNextdate() {
        return nextdate;
    }
    public String getCategory() {
        return category;
    }
    public int getNumofunit() {
        return numofunit;
    }
    public int getTimetype() {
        return timetype;
    }
    public int getCounter() {
        return counter;
    }


    /*
         Setters
    */

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public void setStartdate(long startdate) {
        this.startdate = startdate;
    }
    public void setNextdate(long nextdate) {
        this.nextdate = nextdate;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setNumofunit(int numofunit) {
        this.numofunit = numofunit;
    }
    public void setTimetype(int timetype) {
        this.timetype = timetype;
    }
    public void setCounter(int counter) {
        this.counter = counter;
    }
    public void setAll(int id, String name, double amount, long startdate, long nextdate, String category, int numofunit, int timetype, int counter){
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.startdate = startdate;
        this.nextdate = nextdate;
        this.category = category;
        this.numofunit = numofunit;
        this.timetype = timetype;
        this.counter = counter;
    }
/*    Other Methods     */
    //
}

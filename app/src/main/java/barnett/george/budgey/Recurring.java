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
    int repeats;
    int counter;


    /*
        Constructor
    */

    public Recurring(int id, String name, double amount, long startdate, long nextdate, String category, int numofunit, int timetype, int repeats, int counter) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.startdate = startdate;
        this.nextdate = nextdate;
        this.category = category;
        this.numofunit = numofunit;
        this.timetype = timetype;
        this.repeats = repeats;
        this.counter = counter;
    }

    /*
         Getters
    */

    public int getID() {
        return id;
    }
    public String getName() {
        return name;
    }
    public double getAmount() {
        return amount;
    }
    public long getStartDate() {
        return startdate;
    }
    public long getNextDate() {
        return nextdate;
    }
    public String getCategory() {
        return category;
    }
    public int getNumofUnit() {
        return numofunit;
    }
    public int getTimeType() {
        return timetype;
    }
    public int getRepeats() {
        return repeats;
    }
    public int getCounter() {
        return counter;
    }
    public String getStartDateString(){
        DateHandler dateHandler = new DateHandler();
        String DateString = dateHandler.MillitoDateString(startdate);
        return DateString;
    }
    public String getNextDateString(){
        DateHandler dateHandler = new DateHandler();
        String DateString = dateHandler.MillitoDateString(nextdate);
        return DateString;
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
    public void setStartDate(long startdate) {
        this.startdate = startdate;
    }
    public void setNextDate(long nextdate) {
        this.nextdate = nextdate;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setNumofUnit(int numofunit) {
        this.numofunit = numofunit;
    }
    public void setTimeType(int timetype) {
        this.timetype = timetype;
    }
    public void setRepeats(int repeats) {
        this.repeats = repeats;
    }
    public void setCounter(int counter) {
        this.counter = counter;
    }
    public void setAll(int id, String name, double amount, long startdate, long nextdate, String category, int numofunit, int timetype,int repeats, int counter){
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.startdate = startdate;
        this.nextdate = nextdate;
        this.category = category;
        this.numofunit = numofunit;
        this.timetype = timetype;
        this.repeats = repeats;
        this.counter = counter;
    }
/*    Other Methods     */
    //
}

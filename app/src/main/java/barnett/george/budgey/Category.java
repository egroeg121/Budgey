package barnett.george.budgey;

public class Category {

    int ID;
    String name;
    int counter;
    double amount;

    public Category(int ID, String name, int counter, double amount) {
        this.ID = ID;
        this.name = name;
        this.counter = counter;
        this.amount = amount;
    }

    public int getID() {
        return ID;
    }
    public String getName() {
        return name;
    }
    public int getCounter() {
        return counter;
    }
    public double getAmount() {
        return amount;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setCounter(int counter) {
        this.counter = counter;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public void setAll(int ID, String name, int counter, double amount){
        this.ID = ID;
        this.name = name;
        this.counter = counter;
        this.amount = amount;
    }

    public void increaseCounter(int increaseamount){
        this.counter += increaseamount;
    }

    public void increaseAmount(double increaseAmount){
        this.amount += increaseAmount;
    }
}

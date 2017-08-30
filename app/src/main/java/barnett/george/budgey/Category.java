package barnett.george.budgey;

/**
 * Created by georgebarnett on 30/08/2017.
 */

public class Category {

    int _ID;
    String Name;
    int counter;

    public Category(int _ID, String name, int counter) {
        this._ID = _ID;
        Name = name;
        this.counter = counter;
    }

    public int getID() {
        return _ID;
    }
    public String getName() {
        return Name;
    }
    public int getCounter() {
        return counter;
    }

    public void setID(int _ID) {
        this._ID = _ID;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}

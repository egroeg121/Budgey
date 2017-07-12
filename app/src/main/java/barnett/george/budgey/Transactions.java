package barnett.george.budgey;

public class Transactions {

    int _id;
    String _note;
    double _amount;

    public Transactions(){

    }


    public Transactions(String note, Double amount){
        this._note = note;
        this._amount = amount;
    }


    // Setters
    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_note(String _note) {
        this._note = _note;
    }

    public void set_amount(Double _amount) {
        this._amount = _amount;
    }

    public void set_all(int _id, String _note, Double _amount){
        this._id = _id;
        this._note = _note;
        this._amount = _amount;
    }

    // Getters
    public int get_id() {
        return _id;
    }

    public String get_note() {
        return _note;
    }

    public Double get_amount() {
        return _amount;
    }
}

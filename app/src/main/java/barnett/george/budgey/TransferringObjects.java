package barnett.george.budgey;

import barnett.george.budgey.Objects.Recurring;
import barnett.george.budgey.Objects.Transaction;

public class TransferringObjects {

    /** Converts Recurring to a Transaction object. ID is -1, date = nextdate. */
    public Transaction RecurringToTransaction(Recurring recurring){
        int ID = -1;
        String name = recurring.getName();
        double amount = recurring.getAmount();
        String category = recurring.getCategory();
        long date = recurring.getNextDate();
        int recurringid = recurring.getID();

        Transaction transaction = new Transaction(ID,name,amount,date,category,recurringid);
        return transaction;
    }

}

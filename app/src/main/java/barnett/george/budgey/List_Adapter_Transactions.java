package barnett.george.budgey;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import barnett.george.budgey.Objects.Transaction;

public class List_Adapter_Transactions extends RecyclerView.Adapter<List_Adapter_Transactions.ViewHolder> {
    private ArrayList<Transaction> values;
    CardView TransactionCard;
    Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        public TextView NameText;
        public TextView AmountText;
        public TextView CategoryText;
        public TextView DateText;

        public ViewHolder(View v) {
            super(v);
            context = v.getContext();
            NameText = (TextView) v.findViewById(R.id.StartText);
            AmountText = (TextView) v.findViewById(R.id.AmountText);
            CategoryText = (TextView) v.findViewById(R.id.CategoryText);
            DateText = (TextView) v.findViewById(R.id.DateText);
            TransactionCard = (CardView) v.findViewById(R.id.TransactionCard);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, Info_Activity.class);
            Transaction transaction = values.get( getAdapterPosition() );

            intent.putExtra("Transaction",transaction.getID());
            context.startActivity(intent);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public List_Adapter_Transactions(ArrayList<Transaction> myDataset) {
        values = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public List_Adapter_Transactions.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from( parent.getContext() );
        View v = inflater.inflate(R.layout.overview_transaction_card, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Transaction transaction = values.get(position);
        holder.NameText.setText( transaction.getName() );
        String teststring = String.format("%1$,.2f", transaction.getAmount());
        holder.AmountText.setText( teststring );
        holder.CategoryText.setText( transaction.getCategory() );
        holder.DateText.setText( transaction.getDateString() );
    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(values.isEmpty())
        {
            return 0;
        }else {
            return values.size();
        }
    }


}

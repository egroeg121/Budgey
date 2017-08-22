package barnett.george.budgey;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Overview_Transaction_Adapter extends RecyclerView.Adapter<Overview_Transaction_Adapter.ViewHolder> {
    private ArrayList<Transaction> values;
    CardView TransactionCard;
    Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView NameText;
        public TextView AmountText;
        public TextView CategoryText;
        public TextView DateText;

        public View layout;
        public ViewHolder(View v) {
            super(v);
            context = v.getContext();
            layout = v;
            NameText = (TextView) v.findViewById(R.id.NameText);
            AmountText = (TextView) v.findViewById(R.id.AmountText);
            CategoryText = (TextView) v.findViewById(R.id.CategoryText);
            DateText = (TextView) v.findViewById(R.id.DateText);
            TransactionCard = (CardView) v.findViewById(R.id.TransactionCard);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public Overview_Transaction_Adapter(ArrayList<Transaction> myDataset) {
        values = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public Overview_Transaction_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
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
        
        TransactionCard.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Info_Activity.class);
                intent.putExtra("Transaction",transaction.getId());
                context.startActivity(intent);
            }

        });

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

package barnett.george.budgey;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import barnett.george.budgey.Objects.Budget;
import barnett.george.budgey.Objects.BudgetOverviews;

public class List_Adapter_PreviousBudgets extends RecyclerView.Adapter<List_Adapter_PreviousBudgets.ViewHolder> {
    private ArrayList<BudgetOverviews> values;
    CardView BudgetCard;
    Context context;

    DateHandler dateHandler;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        public TextView StartText;
        public TextView NextText;
        public TextView AmountText;
        public TextView TotalAmountText;



        public ViewHolder(View v) {
            super(v);
            context = v.getContext();
            StartText = (TextView) v.findViewById(R.id.StartText);
            NextText= (TextView) v.findViewById(R.id.NextText);
            AmountText = (TextView) v.findViewById(R.id.AmountText);
            TotalAmountText = (TextView) v.findViewById(R.id.TotalAmountText);
            BudgetCard = (CardView) v.findViewById(R.id.PreviousBudgetCard);

            //v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, Info_Activity.class);
            BudgetOverviews budgetOverview = values.get( getAdapterPosition() );
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public List_Adapter_PreviousBudgets(ArrayList<BudgetOverviews> myDataset) {
        values = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public List_Adapter_PreviousBudgets.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from( parent.getContext() );
        View v = inflater.inflate(R.layout.info_budgets_card, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        dateHandler = new DateHandler();

        final BudgetOverviews budgetOverview = values.get(position);
        String startString = dateHandler.MillitoDateString( budgetOverview.getStartdate() );
        holder.StartText.setText( startString );
        String nextString = dateHandler.MillitoDateString( budgetOverview.getNextdate() );
        holder.StartText.setText( startString );
        String amountstring = String.format("%1$,.2f", budgetOverview.getAmount());
        holder.AmountText.setText( amountstring );
        String Totalamountstring = File.separator + String.format("%1$,.2f", budgetOverview.getTotalamount());
        holder.TotalAmountText.setText( Totalamountstring );
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

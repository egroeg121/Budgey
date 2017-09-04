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
import java.sql.Time;
import java.util.ArrayList;

public class Overview_Budgets_Adapter extends RecyclerView.Adapter<Overview_Budgets_Adapter.ViewHolder> {
    private ArrayList<Budget> values;
    CardView BudgetCard;
    Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        public TextView NameText;
        public TextView AmountText;
        public TextView TotalAmountText;
        public TextView NumOfTypeText;
        public TextView TimeTypeText;


        public ViewHolder(View v) {
            super(v);
            context = v.getContext();
            NameText = (TextView) v.findViewById(R.id.NameText);
            AmountText = (TextView) v.findViewById(R.id.AmountText);
            TotalAmountText = (TextView) v.findViewById(R.id.TotalAmountText);
            NumOfTypeText = (TextView) v.findViewById(R.id.NumOfTypeText);
            TimeTypeText = (TextView) v.findViewById(R.id.TimeTypeText);
            BudgetCard = (CardView) v.findViewById(R.id.BudgetCard);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, Info_Activity.class);
            Budget budget = values.get( getAdapterPosition() );

            intent.putExtra("Budget",budget.getID());
            context.startActivity(intent);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public Overview_Budgets_Adapter(ArrayList<Budget> myDataset) {
        values = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public Overview_Budgets_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from( parent.getContext() );
        View v = inflater.inflate(R.layout.overview_budgets_card, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Budget budget = values.get(position);
        holder.NameText.setText( budget.getName() );
        String amountstring = String.format("%1$,.2f", budget.getAmount());
        holder.AmountText.setText( amountstring );
        String Totalamountstring = File.separator + String.format("%1$,.2f", budget.getTotalAmount());
        holder.TotalAmountText.setText( Totalamountstring );
        String NumofUnitString = Integer.toString( budget.getNumofUnit() );
        holder.NumOfTypeText.setText( NumofUnitString );

        // set type of Time
        int TimeType = budget.getTimeType();
        String TimeTypeString = null;
        switch (TimeType){
            case 0:
                TimeTypeString = " Days";
                break;
            case 1:
                TimeTypeString = " Weeks";
                break;
            case 2:
                TimeTypeString = " Months";
                break;
            case 3:
                TimeTypeString = " Years";
                break;

        }
        holder.TimeTypeText.setText( TimeTypeString );


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

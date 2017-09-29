package barnett.george.budgey;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.sql.Time;
import java.util.ArrayList;

public class List_Adapter_Recurring extends RecyclerView.Adapter<List_Adapter_Recurring.ViewHolder> {
    private ArrayList<Recurring> values;
    CardView RecurringCard;
    Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        public TextView NameText;
        public TextView AmountText;
        public TextView NumOfTypeText;
        public TextView TimeTypeText;
        public TextView DoneText;


        public ViewHolder(View v) {
            super(v);
            context = v.getContext();
            NameText = (TextView) v.findViewById(R.id.NameText);
            AmountText = (TextView) v.findViewById(R.id.AmountText);
            NumOfTypeText = (TextView) v.findViewById(R.id.NumOfTypeText);
            TimeTypeText = (TextView) v.findViewById(R.id.TimeTypeText);
            DoneText = (TextView) v.findViewById(R.id.DoneText);
            RecurringCard = (CardView) v.findViewById(R.id.RecurringCard);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, Info_Activity.class);
            Recurring recurring = values.get( getAdapterPosition() );

            intent.putExtra("Recurring",recurring.getID());
            context.startActivity(intent);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public List_Adapter_Recurring(ArrayList<Recurring> myDataset) {
        values = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public List_Adapter_Recurring.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from( parent.getContext() );
        View v = inflater.inflate(R.layout.overview_recurring_card, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Recurring recurring = values.get(position);
        holder.NameText.setText( recurring.getName() );
        String amountstring = String.format("%1$,.2f", recurring.getAmount());
        holder.AmountText.setText( amountstring );
        String NumofUnitString = Integer.toString( recurring.getNumofUnit() );
        holder.NumOfTypeText.setText( NumofUnitString );
        if (recurring.getCounter() != 0 ){ holder.DoneText.setVisibility(View.INVISIBLE); }
        // set type of Time
        int TimeType = recurring.getTimeType();
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

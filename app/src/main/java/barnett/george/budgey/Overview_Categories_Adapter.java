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

public class Overview_Categories_Adapter extends RecyclerView.Adapter<Overview_Categories_Adapter.ViewHolder> {
    private ArrayList<Category> values;
    CardView CategoryCard;
    Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        public TextView NameText;
        public TextView AmountText;
        public TextView UsesAmount;


        public ViewHolder(View v) {
            super(v);
            context = v.getContext();
            NameText = (TextView) v.findViewById(R.id.NameText);
            AmountText = (TextView) v.findViewById(R.id.AmountText);
            UsesAmount = (TextView) v.findViewById(R.id.UsesAmount);
            CategoryCard = (CardView) v.findViewById(R.id.CategoryCard);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, Info_Activity.class);
            Category category = values.get( getAdapterPosition() );

            // onclick goes to category info
            intent.putExtra("Category",category.getID());
            context.startActivity(intent);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public Overview_Categories_Adapter(ArrayList<Category> myDataset) {
        values = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public Overview_Categories_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from( parent.getContext() );
        View v = inflater.inflate(R.layout.overview_categories_card, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Category category = values.get(position);
        holder.NameText.setText( category.getName() );
        String amountstring = String.format("%1$,.2f", category.getAmount());
        holder.AmountText.setText( amountstring );
        String NumofUnitString = Integer.toString( category.getCounter() );
        holder.UsesAmount.setText( NumofUnitString );
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

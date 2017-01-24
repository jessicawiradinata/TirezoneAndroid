package id.co.tirezone.tirezonemobile;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.Collections;
import java.util.List;

/**
 * Created by Jessica on 23-Jan-17.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<CartViewHolder> {
    List<Item> list = Collections.emptyList();
    Context context;

    public RecyclerViewAdapter(List<Item> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction_product,
                parent, false);
        CartViewHolder mViewHolder = new CartViewHolder(mView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, final int position) {
        holder.name.setText(list.get(position).getPattern());
        holder.size.setText(list.get(position).getSize());
        holder.qty.setText("Qty " + Integer.toString(list.get(position).getQty()));
        holder.price.setText("Rp " + Integer.toString(list.get(position).getPrice()));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupAlertDialog(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void insert(int position, Item item) {
        list.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(Item item) {
        int position = list.indexOf(item);
        list.remove(position);
        notifyItemRemoved(position);
    }

    private void setupAlertDialog(final int position) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setCancelable(true);

        Context mContext = alertDialog.getContext();
        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50,50,50,0);

        final EditText qtyField = new EditText(mContext);
        qtyField.setHint("Quantity");
        qtyField.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(qtyField);

        final EditText priceField = new EditText(mContext);
        priceField.setHint("Price");
        priceField.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(priceField);

        alertDialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String quantity = qtyField.getText().toString();
                String price = priceField.getText().toString();
                if(!quantity.equals("")) {
                    int qtyInt = Integer.parseInt(quantity);
                    list.get(position).setQty(qtyInt);
                    Log.v("NEW QTY ", Integer.toString(list.get(position).getQty()));
                }
                if(!price.equals("")) {
                    int priceInt = Integer.parseInt(price);
                    list.get(position).setPrice(priceInt);
                    Log.v("NEW PRICE ", Integer.toString(list.get(position).getPrice()));
                }
                notifyDataSetChanged();
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.setView(layout);
        alertDialog.create();
        alertDialog.show();
    }
}

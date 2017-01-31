package id.co.tirezone.tirezonemobile;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
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

import java.text.DecimalFormat;
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

        int subtotal = list.get(position).getSubtotal();
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String subtotalString = formatter.format(subtotal);
        holder.price.setText("Rp " + subtotalString);

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
        final int oldSubtotal = list.get(position).getSubtotal();

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
                    int oldPrice = list.get(position).getSubtotal() / list.get(position).getQty();
                    int qtyInt = Integer.parseInt(quantity);
                    list.get(position).setQty(qtyInt);
                    Log.v("NEW QTY ", Integer.toString(list.get(position).getQty()));
                    if(price.equals("")) {
                        list.get(position).setSubtotal(qtyInt * oldPrice);
                    }
                }
                if(!price.equals("")) {
                    if(quantity.equals("")) {
                        int subtotalInt = Integer.parseInt(price) * list.get(position).getQty();
                        list.get(position).setSubtotal(subtotalInt);
                        Log.v("NEW SUBTOTAL ", Integer.toString(list.get(position).getSubtotal()));
                    }
                    else {
                        int subtotalInt = Integer.parseInt(price) * Integer.parseInt(quantity);
                        Log.v("SET PRICE ", price);
                        Log.v("SET QUANTITY ", quantity);
                        list.get(position).setSubtotal(subtotalInt);
                        Log.v("SET SUBTOTAL ", Integer.toString(subtotalInt));
                        Log.v("NEW SUBTOTAL 2 ", Integer.toString(list.get(position).getSubtotal()));
                    }
                }
                notifyDataSetChanged();
                int newSubtotal = list.get(position).getSubtotal();
                sendLocalBroadcast(newSubtotal, oldSubtotal);
            }
        });

        alertDialog.setNegativeButton("Remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int subTotal = list.get(position).getSubtotal();
                sendLocalBroadcast(0, subTotal);
                list.remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
            }
        });

        alertDialog.setView(layout);
        alertDialog.create();
        alertDialog.show();
    }

    private void sendLocalBroadcast(int newSubtotal, int oldSubtotal) {
        int subTotal = newSubtotal - oldSubtotal;
        Log.v("OLD SUBTOTAL ", Integer.toString(oldSubtotal));
        Log.v("NEW SUBTOTAL ", Integer.toString(newSubtotal));
        Intent intent = new Intent("SubtotalService");
        intent.putExtra("subTotal", subTotal);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}

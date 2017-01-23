package id.co.tirezone.tirezonemobile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    public void onBindViewHolder(CartViewHolder holder, int position) {
        holder.name.setText(list.get(position).getPattern());
        holder.size.setText(list.get(position).getSize());
        holder.qty.setText("Qty " + Integer.toString(list.get(position).getQty()));
        holder.price.setText("Rp " + Integer.toString(list.get(position).getPrice()));
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
}

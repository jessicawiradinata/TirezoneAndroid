package id.co.tirezone.tirezonemobile;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Jessica on 23-Jan-17.
 */

public class CartViewHolder extends RecyclerView.ViewHolder {
    View mView;
    TextView name;
    TextView size;
    TextView qty;
    TextView price;

    public CartViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        name = (TextView) mView.findViewById(R.id.name);
        size = (TextView) mView.findViewById(R.id.size);
        qty = (TextView) mView.findViewById(R.id.quantity);
        price = (TextView) mView.findViewById(R.id.price);
    }


}

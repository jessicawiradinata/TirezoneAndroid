package id.co.tirezone.tirezonemobile;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.Map;

public class TransactionDetailsActivity extends AppCompatActivity {
    private String customerKey;
    private String cartKey;
    private String salesKey;
    private String userId;

    private Firebase fSales;
    private Firebase fCustomer;
    private Firebase fCart;
    private DatabaseReference itemsRef;
    private RecyclerView itemsRecyclerView;
    private RecyclerView.LayoutManager itemsLayoutManager;


    private TextView invoiceNoField;
    private TextView nameField;
    private TextView phoneField;
    private TextView vehicleIdField;
    private TextView carField;
    private TextView totalPriceField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);

        setReference();

        Bundle bundle = getIntent().getExtras();
        salesKey = bundle.getString("salesKey");
        customerKey = bundle.getString("customerKey");
        cartKey = bundle.getString("cartKey");
        Log.v("CART KEY ", cartKey);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        itemsRecyclerView = (RecyclerView) findViewById(R.id.transaction_recyclerview);
        itemsLayoutManager = new LinearLayoutManager(this);
        itemsRecyclerView.setLayoutManager(itemsLayoutManager);
        itemsRef = FirebaseDatabase.getInstance().getReference().child("carts").child(cartKey)
                .child("items");

        fSales = new Firebase("https://tirezonemobile.firebaseio.com/users/" + userId + "/sales/" + salesKey);
        fSales.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> map = dataSnapshot.getValue(Map.class);
                getSalesData(map);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        fCustomer = new Firebase("https://tirezonemobile.firebaseio.com/users/" + userId + "/customers/" + customerKey);
        fCustomer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> map = dataSnapshot.getValue(Map.class);
                getCustomerData(map);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        fCart = new Firebase("https://tirezonemobile.firebaseio.com/carts/" + cartKey);
        fCart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Integer> map = dataSnapshot.getValue(Map.class);
                getCartData(map);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        FirebaseRecyclerAdapter<Item, TransactionViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Item, TransactionViewHolder>(
                Item.class,
                R.layout.item_transaction_product,
                TransactionViewHolder.class,
                itemsRef
        ) {
            @Override
            protected void populateViewHolder(TransactionViewHolder viewHolder, Item model, int position) {
                int count = 0;
                Log.v("POPULATING", Integer.toString(count++));
                viewHolder.setPattern(model.getPattern());
                viewHolder.setSize(model.getSize());
                viewHolder.setQty(Integer.toString(model.getQty()));
                viewHolder.setPrice(Integer.toString(model.getSubtotal()));
            }

        };
        itemsRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_mystore) {
            startActivity(new Intent(TransactionDetailsActivity.this, MyStoreActivity.class));
            return true;
        }
        else if (id == R.id.action_sales) {
            startActivity(new Intent(TransactionDetailsActivity.this, SalesActivity.class));
            return true;
        }
        else if (id == R.id.action_customers) {
            startActivity(new Intent(TransactionDetailsActivity.this, CustomersActivity.class));
            return true;
        }
        else if (id == R.id.action_profile) {
            startActivity(new Intent(TransactionDetailsActivity.this, ProfileActivity.class));
            return true;
        }
        else if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(TransactionDetailsActivity.this, LoginActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setReference() {
        invoiceNoField = (TextView) findViewById(R.id.invoice_id);
        nameField = (TextView) findViewById(R.id.name);
        phoneField = (TextView) findViewById(R.id.phone);
        vehicleIdField = (TextView) findViewById(R.id.vehicle_id);
        carField = (TextView) findViewById(R.id.car);
        totalPriceField = (TextView) findViewById(R.id.total_price);
    }

    private void getSalesData(Map<String, String> map) {
        String invoiceNo = map.get("invoiceno");
        invoiceNoField.setText(invoiceNo);
    }

    private void getCustomerData(Map<String, String> map) {
        String name = map.get("name");
        String phone = map.get("phone");
        String vehicleId = map.get("vehicleid");
        String car = map.get("car");

        nameField.setText(name);
        phoneField.setText(phone);
        vehicleIdField.setText(vehicleId);
        carField.setText(car);
    }

    private void getCartData(Map<String, Integer> map) {
        String totalPrice = Integer.toString(map.get("totalprice"));
        totalPriceField.setText("Rp " + totalPrice);
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public TransactionViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setPattern(String thePattern) {
            TextView pattern = (TextView) mView.findViewById(R.id.name);
            pattern.setText(thePattern);
        }

        public void setSize(String theSize) {
            TextView size = (TextView) mView.findViewById(R.id.size);
            size.setText(theSize);
        }

        public void setQty(String quantity) {
            TextView qty = (TextView) mView.findViewById(R.id.quantity);
            qty.setText("Qty " + quantity);
        }

        public void setPrice(String thePrice) {
            TextView price = (TextView) mView.findViewById(R.id.price);
            price.setText("Rp " + thePrice);
        }
    }
}

package id.co.tirezone.tirezonemobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TransactionActivity1 extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction1);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("customers");
        mRecyclerView = (RecyclerView) findViewById(R.id.customers_sales_recyclerview);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Customer, CustomersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Customer, CustomersViewHolder>(
                Customer.class,
                R.layout.item_transaction_customer,
                CustomersViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(CustomersViewHolder viewHolder, Customer model, int position) {
                ProgressBar spinner = (ProgressBar) findViewById(R.id.progress_bar);
                spinner.setVisibility(View.GONE);

                viewHolder.setVehicleId(model.getVehicleid());
                viewHolder.setCustomerName(model.getName());
                viewHolder.setPhoneNum(model.getPhone());
                viewHolder.setEmail(model.getEmail());
                viewHolder.setCar(model.getCar());
                viewHolder.setButton();
                final String key = getRef(position).getKey();
                viewHolder.detailButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("stuff", key);
                        Intent i = new Intent(TransactionActivity1.this, CustomerDetailsActivity.class);
                        i.putExtras(bundle);
                        startActivity(i);
                    }
                });
                viewHolder.selectButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(TransactionActivity1.this, TransactionActivity3.class);
                        i.putExtra("customerKey", key);
                        startActivity(i);
                    }
                });
            }
        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.customers_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_mystore) {
            startActivity(new Intent(TransactionActivity1.this, MyStoreActivity.class));
            return true;
        }
        else if (id == R.id.action_sales) {
            startActivity(new Intent(TransactionActivity1.this, SalesActivity.class));
            return true;
        }
        else if (id == R.id.action_customers) {
            startActivity(new Intent(TransactionActivity1.this, CustomersActivity.class));
            return true;
        }
        else if (id == R.id.action_profile) {
            startActivity(new Intent(TransactionActivity1.this, ProfileActivity.class));
            return true;
        }
        else if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(TransactionActivity1.this, LoginActivity.class));
            return true;
        }
        else if (id == R.id.action_add) {
            startActivity(new Intent(TransactionActivity1.this, AddCustomerActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class CustomersViewHolder extends RecyclerView.ViewHolder {
        View mView;
        Button detailButton;
        Button selectButton;

        public CustomersViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setVehicleId(String id) {
            TextView vehicleId = (TextView) mView.findViewById(R.id.vehicle_id);
            vehicleId.setText(id);
        }

        public void setCustomerName(String name) {
            TextView customerName = (TextView) mView.findViewById(R.id.name);
            customerName.setText(name);
        }

        public void setPhoneNum(String num) {
            TextView phoneNum = (TextView) mView.findViewById(R.id.phone);
            phoneNum.setText(num);
        }

        public void setEmail(String theEmail) {
            TextView email = (TextView) mView.findViewById(R.id.email);
            email.setText(theEmail);
        }

        public void setCar(String theCar) {
            TextView car = (TextView) mView.findViewById(R.id.car);
            car.setText(theCar);
        }

        public void setButton() {
            detailButton = (Button) mView.findViewById(R.id.detail_button);
            selectButton = (Button) mView.findViewById(R.id.select_button);
        }
    }

}

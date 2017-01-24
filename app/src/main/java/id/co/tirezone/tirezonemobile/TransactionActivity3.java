package id.co.tirezone.tirezonemobile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TransactionActivity3 extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private Spinner tireSpinner;
    private Spinner sizeSpinner;
    private final HashMap<String, String> tireMap = new HashMap<String, String>();
    private List<Item> items = new ArrayList<>();
    private int totalPrice;
    private String customerKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction3);

        tireSpinner = (Spinner) findViewById(R.id.tire_spinner);
        sizeSpinner = (Spinner) findViewById(R.id.size_spinner);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.sales_recyclerview);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(items, getApplication());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setupTireSpinner();
        setupNextButton();
        setupAddButton(adapter);

        Bundle bundle = getIntent().getExtras();
        customerKey = bundle.getString("customerKey");
        Log.v("CUSTOMER KEY ", customerKey);
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
            startActivity(new Intent(TransactionActivity3.this, MyStoreActivity.class));
            return true;
        }
        else if (id == R.id.action_sales) {
            startActivity(new Intent(TransactionActivity3.this, SalesActivity.class));
            return true;
        }
        else if (id == R.id.action_customers) {
            startActivity(new Intent(TransactionActivity3.this, CustomersActivity.class));
            return true;
        }
        else if (id == R.id.action_profile) {
            startActivity(new Intent(TransactionActivity3.this, ProfileActivity.class));
            return true;
        }
        else if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(TransactionActivity3.this, LoginActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupTireSpinner() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("patterns");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> tires = new ArrayList<String>();
                for (DataSnapshot tireSnapshot: dataSnapshot.getChildren()) {
                    String tireName = tireSnapshot.child("name").getValue(String.class);
                    String tireKey = tireSnapshot.getKey().toString();
                    tireMap.put(tireName, tireKey);
                    tires.add(tireName);
                }

                ArrayAdapter<String> tireAdapter = new ArrayAdapter<String>(TransactionActivity3.this,
                        R.layout.support_simple_spinner_dropdown_item, tires);
                tireAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                tireSpinner.setAdapter(tireAdapter);
                tireSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String tire = parent.getItemAtPosition(position).toString();
                        String tireKey = tireMap.get(tire);
                        setupSizeSpinner(tireKey);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setupSizeSpinner(String key) {
        mDatabase.child(key).child("sizes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> sizes = new ArrayList<String>();
                for (DataSnapshot sizeSnapshot: dataSnapshot.getChildren()) {
                    sizes.add(sizeSnapshot.getValue().toString());
                }

                ArrayAdapter<String> sizeAdapter = new ArrayAdapter<String>(TransactionActivity3.this,
                        R.layout.support_simple_spinner_dropdown_item, sizes);
                sizeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                sizeSpinner.setAdapter(sizeAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setupNextButton() {
        Button nextButton = (Button) findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionActivity3.this, TransactionActivity4.class);
                intent.putExtra("customerKey", customerKey);
                Cart mCart = new Cart(items);
                intent.putExtra("cart", mCart);
                startActivity(intent);
            }
        });
    }

    private void setupAddButton(final RecyclerViewAdapter adapter) {
        Button addButton = (Button) findViewById(R.id.add_button);
        final EditText qtyField = (EditText) findViewById(R.id.quantity);
        final EditText priceField = (EditText) findViewById(R.id.price);
        final TextView totalPriceField = (TextView) findViewById(R.id.total_price);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String tire = tireSpinner.getSelectedItem().toString();
                final String size = sizeSpinner.getSelectedItem().toString();
                final int qty = Integer.parseInt(qtyField.getText().toString());
                final int price = Integer.parseInt(priceField.getText().toString());
                final int subTotal = price * qty;
                Item newItem = new Item(tire, size, qty, subTotal);
                adapter.insert(items.size(), newItem);
                Log.v("CHECK ARRAY ", Integer.toString(items.size()));
                totalPrice += subTotal;
                totalPriceField.setText("Rp " + Integer.toString(totalPrice));
            }
        });
    }

}

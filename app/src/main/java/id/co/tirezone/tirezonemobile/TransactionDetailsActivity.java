package id.co.tirezone.tirezonemobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Map;

public class TransactionDetailsActivity extends AppCompatActivity {
    private String customerKey;
    private String cartKey;
    private String salesKey;
    private String userId;

    private Firebase fSales;
    private Firebase fCustomer;

    private TextView invoiceNoField;
    private TextView nameField;
    private TextView phoneField;
    private TextView vehicleIdField;
    private TextView carField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);

        setReference();

        Bundle bundle = getIntent().getExtras();
        salesKey = bundle.getString("salesKey");
        customerKey = bundle.getString("customerKey");
        cartKey = bundle.getString("cartKey");
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

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
}

package id.co.tirezone.tirezonemobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.Map;

public class CustomerDetailsActivity extends AppCompatActivity {
    private EditText vehicleIdField;
    private EditText nameField;
    private EditText phoneField;
    private EditText emailField;
    private EditText addressField;
    private EditText postalCodeField;
    private Spinner carSpinner;

    private Firebase fRoot;
    private String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);

        setReference();

        Bundle bundle = getIntent().getExtras();
        link = bundle.getString("stuff");
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        fRoot = new Firebase("https://tirezonemobile.firebaseio.com/users/" + userId + "/customers/" + link);
        fRoot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> map = dataSnapshot.getValue(Map.class);
                getData(map);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        setupUpdateButton();
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
            startActivity(new Intent(CustomerDetailsActivity.this, MyStoreActivity.class));
            return true;
        }
        else if (id == R.id.action_sales) {
            startActivity(new Intent(CustomerDetailsActivity.this, SalesActivity.class));
            return true;
        }
        else if (id == R.id.action_customers) {
            startActivity(new Intent(CustomerDetailsActivity.this, CustomersActivity.class));
            return true;
        }
        else if (id == R.id.action_profile) {
            startActivity(new Intent(CustomerDetailsActivity.this, ProfileActivity.class));
            return true;
        }
        else if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(CustomerDetailsActivity.this, LoginActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setReference() {
        vehicleIdField = (EditText) findViewById(R.id.vehicle_id);
        nameField = (EditText) findViewById(R.id.name);
        phoneField = (EditText) findViewById(R.id.phone);
        emailField = (EditText) findViewById(R.id.email);
        addressField = (EditText) findViewById(R.id.address);
        postalCodeField = (EditText) findViewById(R.id.postal_code);
        carSpinner = (Spinner) findViewById(R.id.car_spinner);
    }

    private void getData(Map<String, String> map) {
        String vehicleId = map.get("vehicleid");
        String name = map.get("name");
        String phone = map.get("phone");
        String email = map.get("email");
        String address = map.get("address");
        String postalCode = map.get("postalcode");
        String car = map.get("car");

        vehicleIdField.setText(vehicleId);
        nameField.setText(name);
        phoneField.setText(phone);
        emailField.setText(email);
        addressField.setText(address);
        postalCodeField.setText(postalCode);

        if (!car.equals(null)) {
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) carSpinner.getAdapter();
            carSpinner.setSelection(adapter.getPosition(car));
        }

        RelativeLayout rLayout = (RelativeLayout) findViewById(R.id.progress_layout);
        ProgressBar spinner = (ProgressBar) findViewById(R.id.progress_bar);
        spinner.setVisibility(View.GONE);
        rLayout.setVisibility(View.GONE);
    }

    private void setupUpdateButton() {
        Button updateButton = (Button) findViewById(R.id.update_button);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vehicleId = vehicleIdField.getText().toString();
                String name = nameField.getText().toString();
                String phone = phoneField.getText().toString();
                String email = emailField.getText().toString();
                String address = addressField.getText().toString();
                String postalCode = postalCodeField.getText().toString();
                String car = carSpinner.getSelectedItem().toString();

                fRoot.child("vehicleid").setValue(vehicleId);
                fRoot.child("name").setValue(name);
                fRoot.child("phone").setValue(phone);
                fRoot.child("email").setValue(email);
                fRoot.child("address").setValue(address);
                fRoot.child("postalcode").setValue(postalCode);
                fRoot.child("car").setValue(car);

                Toast.makeText(CustomerDetailsActivity.this, "Information updated", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CustomerDetailsActivity.this, CustomersActivity.class));
            }
        });
    }

}

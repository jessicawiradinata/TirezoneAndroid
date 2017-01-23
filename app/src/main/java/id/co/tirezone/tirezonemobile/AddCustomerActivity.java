package id.co.tirezone.tirezonemobile;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCustomerActivity extends AppCompatActivity {
    private EditText vehicleIdField;
    private EditText nameField;
    private EditText phoneField;
    private EditText emailField;
    private EditText addressField;
    private EditText postalCodeField;
    private Spinner carSpinner;
    private DatabaseReference dataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        setReference();
        setupSaveButton();
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
            startActivity(new Intent(AddCustomerActivity.this, MyStoreActivity.class));
            return true;
        }
        else if (id == R.id.action_sales) {
            startActivity(new Intent(AddCustomerActivity.this, SalesActivity.class));
            return true;
        }
        else if (id == R.id.action_customers) {
            startActivity(new Intent(AddCustomerActivity.this, CustomersActivity.class));
            return true;
        }
        else if (id == R.id.action_profile) {
            startActivity(new Intent(AddCustomerActivity.this, ProfileActivity.class));
            return true;
        }
        else if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(AddCustomerActivity.this, LoginActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupSaveButton() {
        Button saveButton = (Button) findViewById(R.id.save_button);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dataRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("customers");

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vehicleId = vehicleIdField.getText().toString();
                String name = nameField.getText().toString();
                String phone = phoneField.getText().toString();
                String email = emailField.getText().toString();
                String address = addressField.getText().toString();
                String postalCode = postalCodeField.getText().toString();
                String car = carSpinner.getSelectedItem().toString();

                DatabaseReference newSession = dataRef.push();
                newSession.child("vehicleid").setValue(vehicleId);
                newSession.child("name").setValue(name);
                newSession.child("phone").setValue(phone);
                newSession.child("email").setValue(email);
                newSession.child("address").setValue(address);
                newSession.child("postalcode").setValue(postalCode);
                newSession.child("car").setValue(car);

                Toast.makeText(AddCustomerActivity.this, "Customer added", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddCustomerActivity.this, CustomersActivity.class));
            }
        });
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
}

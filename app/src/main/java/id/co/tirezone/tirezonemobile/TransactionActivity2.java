package id.co.tirezone.tirezonemobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;

public class TransactionActivity2 extends AppCompatActivity {
    private String customerKey;
    private Spinner carSpinner;
    private EditText mileageField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction2);

        Bundle bundle = getIntent().getExtras();
        customerKey = bundle.getString("customerKey");
        Log.v("Customer Key ", customerKey);

        carSpinner = (Spinner) findViewById(R.id.car_spinner);
        mileageField = (EditText) findViewById(R.id.mileage);

        setupNextButton();
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
            startActivity(new Intent(TransactionActivity2.this, MyStoreActivity.class));
            return true;
        }
        else if (id == R.id.action_sales) {
            startActivity(new Intent(TransactionActivity2.this, SalesActivity.class));
            return true;
        }
        else if (id == R.id.action_customers) {
            startActivity(new Intent(TransactionActivity2.this, CustomersActivity.class));
            return true;
        }
        else if (id == R.id.action_profile) {
            startActivity(new Intent(TransactionActivity2.this, ProfileActivity.class));
            return true;
        }
        else if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(TransactionActivity2.this, LoginActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupNextButton() {
        Button nextButton = (Button) findViewById(R.id.next_button);
        final String car = carSpinner.getSelectedItem().toString();
        final int mileage = Integer.parseInt(mileageField.getText().toString());

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionActivity2.this, TransactionActivity3.class);
                intent.putExtra("customerKey", customerKey);
                intent.putExtra("car", car);
                intent.putExtra("mileage", mileage);
                startActivity(intent);
            }
        });
    }
}

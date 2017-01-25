package id.co.tirezone.tirezonemobile;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TransactionActivity4 extends AppCompatActivity {
    private String customerKey;
    private Cart mCart;
    private DatabaseReference dataRef;
    private EditText notesField;
    private EditText technicianField;
    private EditText dateField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction4);

        setReference();
        setupFinishButton();

        Bundle bundle = getIntent().getExtras();
        customerKey = bundle.getString("customerKey");
        mCart = bundle.getParcelable("cart");
        Log.v("CUSTOMER KEY4 ", customerKey);
        Log.v("CART ITEMS ", Integer.toString(mCart.getCartSize()));
        Log.v("CART TOTAL PRICE ", Integer.toString(mCart.getTotalPrice()));
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
            startActivity(new Intent(TransactionActivity4.this, MyStoreActivity.class));
            return true;
        }
        else if (id == R.id.action_sales) {
            startActivity(new Intent(TransactionActivity4.this, SalesActivity.class));
            return true;
        }
        else if (id == R.id.action_customers) {
            startActivity(new Intent(TransactionActivity4.this, CustomersActivity.class));
            return true;
        }
        else if (id == R.id.action_profile) {
            startActivity(new Intent(TransactionActivity4.this, ProfileActivity.class));
            return true;
        }
        else if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(TransactionActivity4.this, LoginActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupFinishButton() {
        Button finishButton = (Button) findViewById(R.id.finish_button);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dataRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("sales");

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String notes = notesField.getText().toString();
                String technician = technicianField.getText().toString();
                String date = dateField.getText().toString();

                DatabaseReference newSession = dataRef.push();
                newSession.child("customerkey").setValue(customerKey);
                newSession.child("notes").setValue(notes);
                newSession.child("technician").setValue(technician);
                newSession.child("date").setValue(date);

                DatabaseReference newCart = FirebaseDatabase.getInstance().getReference().child("carts").push();
                newCart.child("totalprice").setValue(mCart.getTotalPrice());
                String newCartKey = newCart.getKey();
                DatabaseReference dataRef2 = newCart.child("items");
                newSession.child("cart").setValue(newCartKey);

                for(Item item: mCart.getItems()) {
                    DatabaseReference newSession2 = dataRef2.push();
                    newSession2.child("pattern").setValue(item.getPattern());
                    newSession2.child("size").setValue(item.getSize());
                    newSession2.child("qty").setValue(item.getQty());
                    newSession2.child("subtotal").setValue(item.getSubtotal());
                }

                Toast.makeText(TransactionActivity4.this, "Transaction record submitted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(TransactionActivity4.this, SalesActivity.class));
            }
        });
    }

    private void setReference() {
        notesField = (EditText) findViewById(R.id.notes);
        technicianField = (EditText) findViewById(R.id.technician);
        dateField = (EditText) findViewById(R.id.date);
    }
}

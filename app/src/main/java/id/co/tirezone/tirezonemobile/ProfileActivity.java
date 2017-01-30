package id.co.tirezone.tirezonemobile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private EditText groupField;
    private EditText nameField;
    private EditText phoneField;
    private EditText addressField;
    private EditText emailField;

    private Firebase fRoot;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setReference();

        user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        fRoot = new Firebase("https://tirezonemobile.firebaseio.com/users/" + userId);
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
            startActivity(new Intent(ProfileActivity.this, MyStoreActivity.class));
            return true;
        }
        else if (id == R.id.action_sales) {
            startActivity(new Intent(ProfileActivity.this, SalesActivity.class));
            return true;
        }
        else if (id == R.id.action_customers) {
            startActivity(new Intent(ProfileActivity.this, CustomersActivity.class));
            return true;
        }
        else if (id == R.id.action_profile) {
            startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));
            return true;
        }
        else if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setReference() {
        groupField = (EditText) findViewById(R.id.group);
        nameField = (EditText) findViewById(R.id.name);
        phoneField = (EditText) findViewById(R.id.phone);
        addressField = (EditText) findViewById(R.id.address);
        emailField = (EditText) findViewById(R.id.email);
    }

    private void getData(Map<String, String> map) {
        String group = map.get("group");
        String name = map.get("name");
        String phone = map.get("phone");
        String address = map.get("address");
        String email = user.getEmail();

        groupField.setText(group);
        nameField.setText(name);
        phoneField.setText(phone);
        addressField.setText(address);
        emailField.setText(email);
    }

    private void setupUpdateButton() {
        Button updateButton = (Button) findViewById(R.id.update_button);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentEmail = user.getEmail();
                String newEmail = emailField.getText().toString();
                if(!currentEmail.equals(newEmail)) {
                    setupLoginDialog();
                }
                else {
                    submitData();
                }
            }
        });
    }

    private void setupLoginDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfileActivity.this);
        alertDialog.setCancelable(true);
        alertDialog.setTitle("Login");
        alertDialog.setMessage("Please login to update email");

        Context context = alertDialog.getContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50,0,50,0);

        final EditText loginEmail = new EditText(context);
        loginEmail.setHint("Email");
        loginEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        layout.addView(loginEmail);

        final EditText loginPassword = new EditText(context);
        loginPassword.setHint("Password");
        loginPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        loginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        layout.addView(loginPassword);

        alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = loginEmail.getText().toString();
                String password = loginPassword.getText().toString();
                loginUpdate(email, password);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.setView(layout);
        alertDialog.create();
        alertDialog.show();
    }

    private void loginUpdate(String email, String password) {
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        user.reauthenticate(credential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        updateProfile(emailField.getText().toString());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, "Login failed. Profile update cancelled.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void submitData() {
        String group = groupField.getText().toString();
        String name = nameField.getText().toString();
        String phone = phoneField.getText().toString();
        String address = addressField.getText().toString();

        fRoot.child("group").setValue(group);
        fRoot.child("name").setValue(name);
        fRoot.child("phone").setValue(phone);
        fRoot.child("address").setValue(address);

        Toast.makeText(ProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
    }

    private void updateProfile(String newEmail) {
        user.updateEmail(newEmail)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        submitData();
                        Toast.makeText(ProfileActivity.this, "Profile updated.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, "Failed to update profile.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

package id.co.tirezone.tirezonemobile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText currentPasswordField;
    private EditText newPasswordField;
    private EditText confirmPasswordField;
    private FirebaseUser user;
    ProgressBar buttonProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        user = FirebaseAuth.getInstance().getCurrentUser();

        setReference();
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
            startActivity(new Intent(ChangePasswordActivity.this, MyStoreActivity.class));
            return true;
        }
        else if (id == R.id.action_sales) {
            startActivity(new Intent(ChangePasswordActivity.this, SalesActivity.class));
            return true;
        }
        else if (id == R.id.action_customers) {
            startActivity(new Intent(ChangePasswordActivity.this, CustomersActivity.class));
            return true;
        }
        else if (id == R.id.action_profile) {
            startActivity(new Intent(ChangePasswordActivity.this, ProfileActivity.class));
            return true;
        }
        else if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(ChangePasswordActivity.this, LoginActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setReference() {
        currentPasswordField = (EditText) findViewById(R.id.current_password);
        newPasswordField = (EditText) findViewById(R.id.new_password);
        confirmPasswordField = (EditText) findViewById(R.id.confirm_new_password);
        buttonProgressBar = (ProgressBar) findViewById(R.id.button_progress_bar);
    }

    private void setupUpdateButton() {
        Button updateButton = (Button) findViewById(R.id.update_button);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonProgressBar.setVisibility(View.VISIBLE);
                String email = user.getEmail();
                String currentPassword = currentPasswordField.getText().toString();
                String newPassword = newPasswordField.getText().toString();
                String confirmPassword = confirmPasswordField.getText().toString();
                if(newPassword.equals(confirmPassword)) {
                    loginUpdate(email, currentPassword);
                }
                else {
                    buttonProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(ChangePasswordActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loginUpdate(String email, String password) {
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        user.reauthenticate(credential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        updatePassword();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        buttonProgressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(ChangePasswordActivity.this, "Login failed. Password update cancelled.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updatePassword() {
        String newPassword = newPasswordField.getText().toString();
        user.updatePassword(newPassword)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        buttonProgressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(ChangePasswordActivity.this, "Password successfully updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ChangePasswordActivity.this, ProfileActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        buttonProgressBar.setVisibility(View.INVISIBLE);
                        String errorMessage = e.getMessage();
                        Toast.makeText(ChangePasswordActivity.this, "Password change failed. " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }
}

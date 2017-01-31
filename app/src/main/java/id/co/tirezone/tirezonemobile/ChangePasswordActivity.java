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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        user = FirebaseAuth.getInstance().getCurrentUser();

        setReference();
        setupUpdateButton();
    }

    private void setReference() {
        currentPasswordField = (EditText) findViewById(R.id.current_password);
        newPasswordField = (EditText) findViewById(R.id.new_password);
        confirmPasswordField = (EditText) findViewById(R.id.confirm_new_password);
    }

    private void setupUpdateButton() {
        Button updateButton = (Button) findViewById(R.id.update_button);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = user.getEmail();
                String currentPassword = currentPasswordField.getText().toString();
                String newPassword = newPasswordField.getText().toString();
                String confirmPassword = confirmPasswordField.getText().toString();
                if(newPassword.equals(confirmPassword)) {
                    loginUpdate(email, currentPassword);
                }
                else {
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
                        Toast.makeText(ChangePasswordActivity.this, "Password successfully updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ChangePasswordActivity.this, ProfileActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String errorMessage = e.getMessage();
                        Toast.makeText(ChangePasswordActivity.this, "Password change failed. " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }
}

package com.androidchatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    private EditText username, password;
    private String user, pass;
    private FirebaseAuth mAuth;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        Button registerButton = (Button) findViewById(R.id.registerButton);
        TextView login = (TextView) findViewById(R.id.login);
        pd = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("/users");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.intentWithClear(Register.this, Login.class);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                pass = password.getText().toString();
                if (!TextUtils.isEmpty(user) && !TextUtils.isEmpty(pass)) {
                    pd.setMessage("Adding.....");
                    pd.show();
                    mAuth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()) {
                                pd.dismiss();
                                Toast.makeText(Register.this, "FAILED! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                pd.dismiss();
                                FirebaseUser tempUser = FirebaseAuth.getInstance().getCurrentUser();
                                if (tempUser != null) {
                                    userRef.child(tempUser.getUid()).setValue(tempUser.getEmail());
                                    Utils.intentWithClear(Register.this, Login.class);
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.intentWithClear(Register.this, Login.class);
    }
}
package com.androidchatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {
    EditText username, password;
    Button registerButton;
    String user, pass;
    TextView login;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        registerButton = (Button) findViewById(R.id.registerButton);
        login = (TextView) findViewById(R.id.login);
        mAuth = FirebaseAuth.getInstance();
        //Firebase.setAndroidContext(this);
        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("/users");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                pass = password.getText().toString();
                mAuth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            Toast.makeText(Register.this, "FAILED", Toast.LENGTH_SHORT).show();
                        } else {
                            userRef.push().setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                            Intent intent = new Intent(Register.this, Login.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
                //https://androidchatapp-5321f.firebaseio.com/
//                if (user.equals("")) {
//                    username.setError("can't be blank");
//                } else if (pass.equals("")) {
//                    password.setError("can't be blank");
//                } else if (!user.matches("[A-Za-z0-9]+")) {
//                    username.setError("only alphabet or number allowed");
//                } else if (user.length() < 5) {
//                    username.setError("at least 5 characters long");
//                } else if (pass.length() < 5) {
//                    password.setError("at least 5 characters long");
//                } else {
//                    final ProgressDialog pd = new ProgressDialog(Register.this);
//                    pd.setMessage("Loading...");
//                    pd.show();
//
//                    String url = "https://rtchat-6d4d7.firebaseio.com/users.json";
//
//                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String s) {
//                            Firebase reference = new Firebase("https://rtchat-6d4d7.firebaseio.com/users");
//
//                            if (s.equals("null")) {
//                                reference.child(user).child("password").setValue(pass);
//                                Toast.makeText(Register.this, "registration successful", Toast.LENGTH_LONG).show();
//                            } else {
//                                try {
//                                    JSONObject obj = new JSONObject(s);
//
//                                    if (!obj.has(user)) {
//                                        reference.child(user).child("password").setValue(pass);
//                                        Toast.makeText(Register.this, "registration successful", Toast.LENGTH_LONG).show();
//                                    } else {
//                                        Toast.makeText(Register.this, "username already exists", Toast.LENGTH_LONG).show();
//                                    }
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                            pd.dismiss();
//                        }
//
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError volleyError) {
//                            System.out.println("" + volleyError);
//                            pd.dismiss();
//                        }
//                    });
//
//                    RequestQueue rQueue = Volley.newRequestQueue(Register.this);
//                    rQueue.add(request);
//                }
            }
        });
    }
}
package com.androidchatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    TextView registerUser;
    EditText username, password;
    Button loginButton;
    String user, pass;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        registerUser = (TextView) findViewById(R.id.register);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.loginButton);

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                pass = password.getText().toString();

//                mAuth.signInWithEmailAndPassword(user, pass)
//                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                // Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
//
//                                // If sign in fails, display a message to the user. If sign in succeeds
//                                // the auth state listener will be notified and logic to handle the
//                                // signed in user can be handled in the listener.
//                                if (!task.isSuccessful()) {
//                                    //    Log.w(TAG, "signInWithEmail:failed", task.getException());
//                                    Toast.makeText(Login.this, "FAILED" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                                } else {
//
//                                }                                // ...
//                            }
//                        });
                mAuth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(Login.this, Users.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else{
                            Toast.makeText(Login.this, "FAILED" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
//                if (user.equals("")) {
//                    username.setError("can't be blank");
//                } else if (pass.equals("")) {
//                    password.setError("can't be blank");
//                } else {
//                    String url = "https://rtchat-6d4d7.firebaseio.com/users.json";
//                    final ProgressDialog pd = new ProgressDialog(Login.this);
//                    pd.setMessage("Loading...");
//                    pd.show();
//
//                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String s) {
//                            if (s.equals("null")) {
//                                Toast.makeText(Login.this, "user not found", Toast.LENGTH_LONG).show();
//                            } else {
//                                try {
//                                    JSONObject obj = new JSONObject(s);
//
//                                    if (!obj.has(user)) {
//                                        Toast.makeText(Login.this, "user not found", Toast.LENGTH_LONG).show();
//                                    } else if (obj.getJSONObject(user).getString("password").equals(pass)) {
//                                        UserDetails.username = user;
//                                        UserDetails.password = pass;
//                                        startActivity(new Intent(Login.this, Users.class));
//                                    } else {
//                                        Toast.makeText(Login.this, "incorrect password", Toast.LENGTH_LONG).show();
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                            pd.dismiss();
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError volleyError) {
//                            System.out.println("" + volleyError);
//                            pd.dismiss();
//                        }
//                    });
//
//                    RequestQueue rQueue = Volley.newRequestQueue(Login.this);
//                    rQueue.add(request);
//                }

            }
        });
    }
}

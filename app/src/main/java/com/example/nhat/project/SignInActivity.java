package com.example.nhat.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    Button btnSignin;
    TextView txtGoSignup, txtResetPassword;
    Switch swRememberme;
    FirebaseAuth auth;
    String PRE = "PREFERENCE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        auth = FirebaseAuth.getInstance();
        addControls();
        addEvents();
    }

    @Override
    protected void onPause() {
        super.onPause();
        savingPreference();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        restoringPreference();
    }

    private void restoringPreference() {
        SharedPreferences pre = getSharedPreferences(PRE,MODE_PRIVATE);
        boolean save = pre.getBoolean("ischecked",false);
        if (save){
            edtEmail.setText(pre.getString("username",""));
            edtPassword.setText(pre.getString("password",""));
            swRememberme.setChecked(save);
        }

    }

    private void savingPreference() {
        SharedPreferences pre = getSharedPreferences(PRE,MODE_PRIVATE);
        SharedPreferences.Editor edit = pre.edit();

        if (!swRememberme.isChecked()){
            edit.clear();
        }
        else {
            edit.putString("username",edtEmail.getText().toString());
            edit.putString("password",edtPassword.getText().toString());
            edit.putBoolean("ischecked",swRememberme.isChecked());
        }
        edit.commit();
    }

    private void addEvents() {
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(SignInActivity.this,
                            "Please enter the email", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(SignInActivity.this,
                            "Please enter the password", Toast.LENGTH_LONG).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(SignInActivity.this,
                            "Password musts be larger than 6 character", Toast.LENGTH_LONG).show();
                    return;
                }

                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                        SignInActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignInActivity.this,
                                            "Log in is unsuccessful" + task.isSuccessful(), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(SignInActivity.this,
                                            "Log in is successful!" + task.getException(), Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }
                );
            }
        });

        txtGoSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });

        txtResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, ResetActivity.class));
            }
        });
    }

    private void addControls() {
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnSignin = (Button) findViewById(R.id.btnSignin);
        txtGoSignup = (TextView) findViewById(R.id.txtGotoSignup);
        txtResetPassword = (TextView) findViewById(R.id.txtFogotPassword);
        swRememberme = (Switch) findViewById(R.id.swRemember);
    }
}

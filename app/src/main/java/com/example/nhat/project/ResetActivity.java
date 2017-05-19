package com.example.nhat.project;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetActivity extends AppCompatActivity {
    EditText edtEmail;
    Button btnSend;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        btnSend = (Button) findViewById(R.id.btnSend);
        auth = FirebaseAuth.getInstance();


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    edtEmail.requestFocus();
                    Toast.makeText(ResetActivity.this,"Please enter the email",Toast.LENGTH_LONG).show();
                    return;
                }
                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Successful!\n" + task.getResult(),Toast.LENGTH_LONG);
                            finish();
                        }
                        else
                            Toast.makeText(getApplicationContext(),"Unsuccessful! \n" + task.getException(),Toast.LENGTH_LONG);
                    }
                });

            }
        });
    }
}

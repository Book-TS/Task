package com.example.app_sk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login_Activity extends AppCompatActivity {

    private Button bt_login;
    private TextView txt_sign_in;
    private TextView txt_forgot;
    private EditText account_logIn, password_logIn;
    private final DatabaseReference mData = FirebaseDatabase.getInstance().getReference();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String childTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        anhXa();

        signIn();

        logIn();

        forgotPass();
    }

    private void anhXa() {
        bt_login = findViewById(R.id.bt_login);
        txt_sign_in = findViewById(R.id.txt_sign_in);
        txt_forgot = findViewById(R.id.txt_forgot);

        account_logIn = findViewById(R.id.account_logIn);
        password_logIn = findViewById(R.id.password_logIn);
    }

    private void signIn() {
        txt_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login_Activity.this, Register_Activity.class);
                startActivity(intent);
            }
        });
    }

    private void logIn() {
        bt_login.setOnClickListener(v -> {
            String email1 = account_logIn.getText().toString();
            String pass1 = password_logIn.getText().toString();
            // Kiểm tra xem đã nhập email và password chưa
            if (email1.isEmpty()) {
                Toast.makeText(Login_Activity.this, "Vui lòng nhập địa chỉ Email", Toast.LENGTH_SHORT).show();
            } else if (pass1.isEmpty()) {
                Toast.makeText(Login_Activity.this, "Vui lòng nhập Password", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.signInWithEmailAndPassword(email1, pass1).addOnCompleteListener(Login_Activity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login_Activity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                            startActivity(intent);

                            if(email1.equals("hung49696@donga.edu.vn")) {
                                childTxt = "devices_1";
                            }
                            else if (email1.equals("an49966@donga.edu.vn")) {
                                childTxt = "devices";
                            }

                            Account account_1 = new Account(email1, pass1, 1);
                            mData.child(childTxt + "/Account").setValue(account_1);

                            Intent intent_Service = new Intent(getApplicationContext(), MyService.class);
                            startService(intent_Service);

                        } else {
                            Toast.makeText(Login_Activity.this, "Tên tài khoản hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void forgotPass() {
        txt_forgot.setOnClickListener(v -> {
            String email1 = account_logIn.getText().toString();
            // Kiểm tra xem đã nhập email chưa
            if (email1.isEmpty())
                Toast.makeText(Login_Activity.this, "Vui lòng nhập địa chỉ mail", Toast.LENGTH_SHORT).show();
            else {
                mAuth.sendPasswordResetEmail(email1).addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                        Toast.makeText(Login_Activity.this, "Đã gửi mã xác nhận, vui lòng check mail", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(Login_Activity.this, "Gửi yêu cầu thất bại", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}
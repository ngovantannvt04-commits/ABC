package com.baocao.kttx2_de6;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditUserActivity extends AppCompatActivity {
    EditText edtUsername, edtPassword, edtEmail;
    Button btnSave, btnCancel;
    boolean isEdit = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        mapping();
        Intent intent = getIntent();
        String hoTenCu = intent.getStringExtra("hoTenCu");
        String emailCu = intent.getStringExtra("emailCu");
        String passCu = intent.getStringExtra("passCu");
        isEdit = intent.getBooleanExtra("isEdit", false);
        // Nếu là sửa → hiện dữ liệu cũ
        if (isEdit) {
            edtUsername.setText(hoTenCu);
            edtEmail.setText(emailCu);
            edtPassword.setText(passCu);
        }
        btnSave.setOnClickListener(v -> {
            if(edtUsername.getText().toString().isEmpty() || edtPassword.getText().toString().isEmpty() ||edtEmail.getText().toString().isEmpty() ){
                Toast.makeText(EditUserActivity.this,"Khong duoc de trong cac truong!",Toast.LENGTH_SHORT).show();
                return;
            }
            Intent i = new Intent();
            i.putExtra("username", edtUsername.getText().toString());
            i.putExtra("password", edtPassword.getText().toString());
            i.putExtra("email", edtEmail.getText().toString());
            i.putExtra("isEdit", isEdit);
            setResult(RESULT_OK, i);
            finish();
        });

        btnCancel.setOnClickListener(v -> finish());
    }

    private void mapping() {
        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);
        edtEmail = findViewById(R.id.edt_email);
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);
    }
}
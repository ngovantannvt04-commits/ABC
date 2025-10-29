package com.baocao.kttx2_de6;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import android.content.Intent;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lvUsers;
    Button btnClose;
    ArrayList<User> lstUsers;
    ArrayAdapter<User> adapter;
    int viTriSua;
    final String FILE_NAME = "users.txt";

    // Activity Result API
    private final ActivityResultLauncher<Intent> editLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Intent data = result.getData();
                            String username = data.getStringExtra("username");
                            String password = data.getStringExtra("password");
                            String email = data.getStringExtra("email");
                            boolean isEdit = result.getData().getBooleanExtra("isEdit", false);
                            if (isEdit && viTriSua!=-1) {
                                lstUsers.get(viTriSua).setUsername(username);
                                lstUsers.get(viTriSua).setEmail(email);
                                lstUsers.get(viTriSua).setPassword(password);
                                viTriSua=-1;
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapping();
        loadData();

        if (lstUsers.isEmpty()) {
            lstUsers.add(new User("HaNoi", "abc123", "hanoi@gmail.com"));
            lstUsers.add(new User("NgheAn", "xyz", "nghean@gmail.com"));
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lstUsers);
        lvUsers.setAdapter(adapter);

        registerForContextMenu(lvUsers);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Thong bao!");
                builder.setMessage("Ban co chac chan muon thoat ung dung?");
                builder.setPositiveButton("Co", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveData();
                        finish();
                    }
                });
                builder.setNegativeButton("Khong",null);
                builder.show();
            }
        });
    }

    private void mapping() {
        lvUsers = findViewById(R.id.lv_users);
        btnClose = findViewById(R.id.btn_close);

        lstUsers = new ArrayList<>();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context_mn,menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        if (id == R.id.menu_edit) {
            viTriSua = position;
            User user = lstUsers.get(position);
            String hoTenCu = user.getUsername();
            String emailCu = user.getEmail();
            String passCu = user.getPassword();
            Intent intent = new Intent(this, EditUserActivity.class);
            intent.putExtra("hoTenCu", hoTenCu);
            intent.putExtra("emailCu", emailCu);
            intent.putExtra("passCu",passCu);
            intent.putExtra("isEdit", true); // báo là đang sửa
            editLauncher.launch(intent);
        } else if (id == R.id.menu_refresh) {
            loadData();
            adapter.notifyDataSetChanged();
        }
        return super.onContextItemSelected(item);
    }
    private void saveData() {
        try {
            FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            for (User u : lstUsers) {
                bw.write(u.getUsername() + ";" + u.getPassword() + ";" + u.getEmail());
                bw.newLine();
            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        lstUsers.clear();
        try {
            FileInputStream fis = openFileInput(FILE_NAME);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(";");
                if (p.length >= 3)
                    lstUsers.add(new User(p[0], p[1], p[2]));
            }
            br.close();
        } catch (Exception e) {
            // nếu file chưa tồn tại thì bỏ qua
        }
    }
}
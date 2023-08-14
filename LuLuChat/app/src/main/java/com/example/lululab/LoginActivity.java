package com.example.lululab;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {
    private EditText studentIdEditText, passwordEditText;
    private Button loginButton;

    public static String studentId; // Add this line
    public static final String DATABASE_NAME = "My_database";
    public static final String url = "" +
            DATABASE_NAME;
    public static final String username = "", password = "";

    public static final String TABLE_NAME = "Info";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        studentIdEditText = findViewById(R.id.editText_student_id);
        passwordEditText = findViewById(R.id.editText_password);
        loginButton = findViewById(R.id.button_login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputStudentId = studentIdEditText.getText().toString();
                String inputPassword = passwordEditText.getText().toString();

                if (inputStudentId.isEmpty() || inputPassword.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "학번과 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(() -> {
                        try {
                            Class.forName("com.mysql.jdbc.Driver");
                            Connection connection = DriverManager.getConnection(url, username, password);
                            Statement statement = connection.createStatement();
                            ResultSet rs = statement.executeQuery("SELECT * FROM " + TABLE_NAME + " WHERE studentId='" + inputStudentId + "' AND password='" + inputPassword + "'");

                            if (rs.next()) {
                                studentId = inputStudentId;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this, studentId + "님 환영합니다.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, SelectActivity.class);
                                        intent.putExtra("studentId", inputStudentId);
                                        startActivity(intent);
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this, "로그인 실패. 학번 또는 비밀번호가 틀립니다.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            connection.close();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
            }
        });
    }
}

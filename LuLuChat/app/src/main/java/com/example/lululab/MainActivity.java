package com.example.lululab;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class MainActivity extends AppCompatActivity {
    public static String studentId; // Add this line
    public static final String DATABASE_NAME = "My_database";
    public static final String url = "" +
            DATABASE_NAME;
    public static final String username = "", password = "";

    public static final String TABLE_NAME = "Info";
    private Button loginButton, signupButton;
    private EditText passwordtext,studentidtext;

    private TextView luluchat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = findViewById(R.id.login); // ID를 실제 사용하는 버튼의 ID로 바꿔주세요.
        signupButton = findViewById(R.id.signup); // ID를 실제 사용하는 버튼의 ID로 바꿔주세요.
        passwordtext=findViewById(R.id.passwordtext);
        studentidtext=findViewById(R.id.studentidtext);

        luluchat=findViewById(R.id.lululabview);
        String lulutext = "LuLuChat";
        SpannableString spannableString = new SpannableString(lulutext);
        int start = 0;
        int end = 8;
        int color = Color.WHITE;
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        spannableString.setSpan(colorSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        luluchat.setText(spannableString);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class); // SignupActivity는 실제 회원가입 화면의 액티비티 이름으로 바꿔주세요.
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String inputStudentId = studentidtext.getText().toString();
                String inputPassword = passwordtext.getText().toString();

                if (inputStudentId.isEmpty() || inputPassword.isEmpty()) {
                    Toast.makeText(MainActivity.this, "학번과 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(MainActivity.this, studentId + "님 환영합니다", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MainActivity.this, SelectActivity.class);
                                        intent.putExtra("studentId", inputStudentId);
                                        startActivity(intent);
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, "로그인 실패. 학번 또는 비밀번호가 틀립니다.", Toast.LENGTH_SHORT).show();
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


        passwordtext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean b) {
                if(b)
                {
                    passwordtext.setBackgroundResource(R.drawable.textview_border);
                }
                else {
                    passwordtext.setBackgroundResource(0);
                }
            }
        });
        studentidtext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean b) {
                if(b)
                {
                    studentidtext.setBackgroundResource(R.drawable.textview_border);
                }
                else {
                    studentidtext.setBackgroundResource(0);
                }
            }
        });


    }
}
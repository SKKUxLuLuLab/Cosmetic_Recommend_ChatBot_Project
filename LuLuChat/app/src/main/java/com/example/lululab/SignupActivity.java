package com.example.lululab;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SignupActivity extends AppCompatActivity {
    private EditText studentIdEditText;
    private EditText passwordEditText;
    private RadioGroup genderRadioGroup;

    private EditText confirmPasswordEditText;

    private SeekBar ageSeekBar;
    private TextView ageTextView;
    private Button addRecordButton;
    private String gender;
    private String age;

    private TextView studentIdError;
    private TextView passwordError;
    private TextView genderError;

    private TextView confirmPasswordError;

    public static final String DATABASE_NAME = "My_database";
    public static final String url = "" +
            DATABASE_NAME;
    public static final String username = "", password = "";
    public static final String TABLE_NAME = "Info";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        studentIdEditText = findViewById(R.id.editText_student_id);
        passwordEditText = findViewById(R.id.editText_password);
        genderRadioGroup = findViewById(R.id.radioGroup_gender);
        ageSeekBar = findViewById(R.id.seekBar_age);
        ageTextView = findViewById(R.id.textView_age);
        addRecordButton = findViewById(R.id.buttonAdd);
        confirmPasswordEditText = findViewById(R.id.editText_confirm_password);
        confirmPasswordError = findViewById(R.id.confirmPasswordError);

        studentIdEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean b) {
                if(b)
                {
                    studentIdEditText.setBackgroundResource(R.drawable.textview_border);
                }
                else {
                    studentIdEditText.setBackgroundResource(R.drawable.originaltextview_order);
                }
            }
        });
        passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean b) {
                if(b)
                {
                    passwordEditText.setBackgroundResource(R.drawable.textview_border);
                }
                else {
                    passwordEditText.setBackgroundResource(R.drawable.originaltextview_order);
                }
            }
        });
        confirmPasswordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean b) {
                if(b)
                {
                    confirmPasswordEditText.setBackgroundResource(R.drawable.textview_border);
                }
                else {
                    confirmPasswordEditText.setBackgroundResource(R.drawable.originaltextview_order);
                }
            }
        });




        ageSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int ageLowerBound = progress * 5 + 10;
                int ageUpperBound = ageLowerBound + 5;
                age = ageLowerBound + "~" + ageUpperBound;
                ageTextView.setText("연령대: " + age);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        studentIdError = findViewById(R.id.studentIdError);
        passwordError = findViewById(R.id.passwordError);
        genderError = findViewById(R.id.genderError);

        addRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input
                String studentId = studentIdEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();
                int selectedId = genderRadioGroup.getCheckedRadioButtonId();

                // Check if any field is empty
                boolean allFieldsFilled = true;

                if (studentId.isEmpty() || !studentId.matches("\\d+")) {
                    studentIdEditText.setBackgroundResource(R.drawable.box_error);
                    studentIdError.setText("학번을 입력해주세요");
                    studentIdError.setVisibility(View.VISIBLE);
                    allFieldsFilled = false;
                } else {
                    studentIdEditText.setBackgroundResource(R.drawable.box);
                    studentIdError.setVisibility(View.GONE);
                }

                String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
                if (password.isEmpty() || !password.matches(passwordPattern)) {
                    passwordEditText.setBackgroundResource(R.drawable.box_error);
                    passwordError.setText("패스워드를 입력해주세요");
                    passwordError.setVisibility(View.VISIBLE);
                    allFieldsFilled = false;
                } else {
                    passwordEditText.setBackgroundResource(R.drawable.box);
                    passwordError.setVisibility(View.GONE);
                }

                if (confirmPassword.isEmpty() || !confirmPassword.equals(password)) {
                    confirmPasswordEditText.setBackgroundResource(R.drawable.box_error);
                    confirmPasswordError.setText("패스워드를 입력해주세요");
                    confirmPasswordError.setVisibility(View.VISIBLE);
                    allFieldsFilled = false;
                } else {
                    confirmPasswordEditText.setBackgroundResource(R.drawable.box);
                    confirmPasswordError.setVisibility(View.GONE);
                }

                if (selectedId == -1) {
                    genderRadioGroup.setBackgroundResource(R.drawable.box_error);
                    genderError.setVisibility(View.VISIBLE);
                    allFieldsFilled = false;
                } else {
                    genderRadioGroup.setBackgroundResource(R.drawable.box);
                    genderError.setVisibility(View.GONE);
                }

                // Proceed only if all fields are filled
                if (allFieldsFilled) {
                    addRecord(studentId, password, gender, age);
                    Log.d("Test", "success");
                    Toast.makeText(SignupActivity.this, "회원가입 성공!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        studentIdEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String studentId = s.toString();
                if (studentId.isEmpty()) {
                    studentIdEditText.setBackgroundResource(R.drawable.box_error);
                    studentIdError.setText("학번을 입력해주세요");
                    studentIdError.setVisibility(View.VISIBLE);
                    studentIdEditText.setBackgroundResource(R.drawable.box_error);
                } else if (!studentId.matches("\\d+")) {
                    studentIdError.setText("올바른 학번 형식이 아닙니다");
                    studentIdError.setVisibility(View.VISIBLE);
                } else {
                    studentIdEditText.setBackgroundResource(R.drawable.box);
                    studentIdError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    studentIdEditText.setBackgroundResource(R.drawable.box);
                }
            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                passwordEditText.setBackgroundResource(R.drawable.box_typing);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = s.toString();
                if (password.isEmpty()) {
                    passwordEditText.setBackgroundResource(R.drawable.box_error);
                    passwordError.setText("비밀번호를 입력해주세요");
                    passwordError.setVisibility(View.VISIBLE);
                } else if (!password.matches(".*\\d.*")) {
                    passwordEditText.setBackgroundResource(R.drawable.box_error);
                    passwordError.setText("패스워드에 숫자가 포함되어야 합니다");
                    passwordError.setVisibility(View.VISIBLE);
                } else if (!password.matches(".*[a-z].*")) {
                    passwordEditText.setBackgroundResource(R.drawable.box_error);
                    passwordError.setText("패스워드에 문자가 포함되어야 합니다");
                    passwordError.setVisibility(View.VISIBLE);
                } else if (!password.matches(".*[@#$%^&+=!].*")) {
                    passwordEditText.setBackgroundResource(R.drawable.box_error);
                    passwordError.setText("패스워드에 특수문자가 포함되어야 합니다");
                    passwordError.setVisibility(View.VISIBLE);
                } else if (password.length() < 8) {
                    passwordEditText.setBackgroundResource(R.drawable.box_error);
                    passwordError.setText("패스워드는 8자리 이상이어야 합니다");
                    passwordError.setVisibility(View.VISIBLE);
                } else {
                    passwordEditText.setBackgroundResource(R.drawable.box);
                    passwordError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    passwordEditText.setBackgroundResource(R.drawable.box);
                }
            }
        });

        confirmPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                confirmPasswordEditText.setBackgroundResource(R.drawable.box_typing);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String originalPassword = passwordEditText.getText().toString();
                String confirmPassword = s.toString();
                if (!confirmPassword.equals(originalPassword)) {
                    confirmPasswordEditText.setBackgroundResource(R.drawable.box_error);
                    confirmPasswordError.setText("입력한 비밀번호와 다릅니다");
                    confirmPasswordError.setVisibility(View.VISIBLE);
                } else {
                    confirmPasswordEditText.setBackgroundResource(R.drawable.box);
                    confirmPasswordError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    confirmPasswordEditText.setBackgroundResource(R.drawable.box);
                }
            }
        });

        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButton_male) {
                    gender = "Male";
                    genderRadioGroup.setBackgroundResource(R.drawable.box);
                    genderError.setVisibility(View.GONE);
                } else if (checkedId == R.id.radioButton_female) {
                    gender = "Female";
                    genderRadioGroup.setBackgroundResource(R.drawable.box);
                    genderError.setVisibility(View.GONE);
                }
            }
        });

    }


    public void shakeAnimation(final View view) {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake_animation);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(view instanceof EditText){
                    view.setBackgroundResource(R.drawable.box);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        view.startAnimation(shake);
    }

    public void addRecord(String studentId, String password_str, String gender_str, String age_str) {
        new Thread(() -> {
            try {
                Log.d("Test2", studentId);
                Log.d("Test2", password_str);
                Log.d("Test2", gender_str);
                Log.d("Test2", age_str);
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, username, password);

                // Check connection
                if(connection != null){
                    Log.d("DB_CONNECTION", "Successfully connected to the database");
                } else {
                    Log.d("DB_CONNECTION", "Failed to connect to the database");
                }

                Statement statement = connection.createStatement();
                // add to RDS DB:
                statement.execute("INSERT INTO " + TABLE_NAME + "(studentId, password, gender, age) VALUES('"
                        + studentId + "', '" + password_str + "', '" + gender_str + "', '" + age_str + "')");

                connection.close();

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("DB_ERROR", "Error occurred while inserting record", e);
            }
        }).start();
    }
}
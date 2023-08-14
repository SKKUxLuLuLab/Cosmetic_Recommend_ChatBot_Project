package com.example.lululab;

import static com.example.lululab.MainActivity.studentId;

import android.app.MediaRouteButton;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.lululab.Model.Message;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MyPageActivity extends AppCompatActivity {

    private TextView studentIdTextView, genderTextView, ageTextView;
    private ImageView profileImageView;
    private ImageView todayImage;
    private Button scrapButton, todayButton, productButton;
    private Button BackButton,LogoutButton;
    private ListView listView;
    private ListView list_product_View;

    public static final String DATABASE_NAME = "My_database";
    public static final String url = "" +
            DATABASE_NAME;
    public static final String username = "", password = "";
    public static final String TABLE_NAME = "Info";

    private void changeButtonColor(Button button, int backgroundResource) {
        button.setBackgroundResource(backgroundResource);
    }

    public void reloadMessages() {
        new ShowScrapMessageTask(studentId, MyPageActivity.this, listView).execute();
    }

    public void reloadProducts() {
        new ShowProductTask(studentId, MyPageActivity.this, list_product_View).execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        studentIdTextView = findViewById(R.id.textView_studentId);
        genderTextView = findViewById(R.id.textView_gender);
        ageTextView = findViewById(R.id.textView_age);
        scrapButton = findViewById(R.id.button_scrap);
        todayButton = findViewById(R.id.button_today);
        listView = findViewById(R.id.listView_scrappedMessages);
        list_product_View = findViewById(R.id.listView_product);
        BackButton=findViewById(R.id.BackButton);
        scrapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadMessages();
                listView.setVisibility(View.VISIBLE);
                todayImage.setVisibility(View.GONE);
                list_product_View.setVisibility(View.GONE);
                changeButtonColor(todayButton, R.drawable.button_default);
                changeButtonColor(scrapButton, R.drawable.button_click);
                changeButtonColor(productButton, R.drawable.button_default);
            }
        });
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPageActivity.this, SelectActivity.class); // SignupActivity는 실제 회원가입 화면의 액티비티 이름으로 바꿔주세요.
                startActivity(intent);
            }
        });

        todayImage = findViewById(R.id.today_image);  // Add this line

        todayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeButtonColor(scrapButton, R.drawable.button_default);
                changeButtonColor(todayButton, R.drawable.button_click);
                changeButtonColor(productButton, R.drawable.button_default);
                listView.setVisibility(View.GONE);
                list_product_View.setVisibility(View.GONE);
                todayImage.setVisibility(View.VISIBLE);  // Add this line
                // You may also want to load an image into todayImage here
            }
        });

        productButton = findViewById(R.id.button_product);
        productButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear or hide other views
                reloadProducts();
                list_product_View.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                todayImage.setVisibility(View.GONE);
                // Load scrapped products here

                // Change colors
                changeButtonColor(todayButton, R.drawable.button_default);
                changeButtonColor(scrapButton, R.drawable.button_default);
                changeButtonColor(productButton, R.drawable.button_click);
            }
        });

        // Today 버튼을 눌러진 상태로 시작합니다.
        todayButton.performClick();
        getAndShowProfileInfo();
    }

    private void getAndShowProfileInfo() {
        new Thread(() -> {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, username, password);
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT * FROM " + TABLE_NAME + " WHERE studentId='" + studentId + "'");

                if (rs.next()) {
                    String studentId = rs.getString("studentId");
                    String gender = rs.getString("gender");
                    String age = rs.getString("age");

                    runOnUiThread(() -> {
                        studentIdTextView.setText("Student ID: " + studentId);
                        genderTextView.setText("성별: " + gender);
                        ageTextView.setText("나이: " + age);
                        // Load profile image here, if available
                    });
                }

                connection.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
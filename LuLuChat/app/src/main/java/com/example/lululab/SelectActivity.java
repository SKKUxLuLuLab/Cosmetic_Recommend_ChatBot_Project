package com.example.lululab;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class SelectActivity extends AppCompatActivity {

    private Button mypageButton;
    private Button luluChatButton;
    private Button dicButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        mypageButton = (Button) findViewById(R.id.mypage_button);
        luluChatButton = (Button) findViewById(R.id.chat_button);
        dicButton = (Button) findViewById(R.id.dic_button);

        mypageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMypage = new Intent(SelectActivity.this, MyPageActivity.class);
                startActivity(goToMypage);
            }
        });

        dicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToDictionary = new Intent(SelectActivity.this, DictionaryActivity.class);
                startActivity(goToDictionary);
            }
        });

        luluChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToLuluChat = new Intent(SelectActivity.this, ChatActivity.class);
                startActivity(goToLuluChat);
            }
        });
    }
}
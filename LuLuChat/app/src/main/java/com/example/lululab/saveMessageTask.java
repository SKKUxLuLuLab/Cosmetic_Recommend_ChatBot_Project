package com.example.lululab;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.lululab.Model.Message;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class saveMessageTask extends AsyncTask<Message, Void, Boolean> {

    public static final String DATABASE_NAME = "My_database";
    public static final String url = "" +
            DATABASE_NAME;
    public static final String username = "", password = "";

    public static final String TABLE_NAME = "Scrab";

    private String studentId;
    private Context context;
    public saveMessageTask(String studentId, Context context) {
        this.studentId = studentId;
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Message... messages) {
        Message message = messages[0];
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            String sql = "INSERT INTO " + TABLE_NAME + " (studentId, message) VALUES ('" + studentId + "', '" + message.getMessage().replace("'", "''") + "')";
            int rowsInserted = statement.executeUpdate(sql);
            if (rowsInserted > 0) {
                System.out.println("A new message was inserted successfully!");
                return true;
            }
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    protected void onPostExecute(Boolean success) {
        if (success) {
            Toast.makeText(context, "메세지를 성공적으로 스크랩하였습니다.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Failed to save message. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}

package com.example.lululab;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SaveProductTask extends AsyncTask<String, Void, Boolean> {

    public static final String DATABASE_NAME = "My_database";
    public static final String url = "" +
            DATABASE_NAME;
    public static final String username = "", password = "";

    public static final String TABLE_NAME = "Product";

    private String studentID;

    private Context context;
    public SaveProductTask(String studentId, Context context) {
        this.studentID = studentId;
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(String... productNames) {
        String productName = productNames[0];
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            String sql = "INSERT INTO " + TABLE_NAME + " (studentID, product_name) VALUES ('" + studentID + "', '" + productName.replace("'", "''") + "')";
            int rowsInserted = statement.executeUpdate(sql);
            if (rowsInserted > 0) {
                System.out.println("A new product name was inserted successfully!");
                return true;
            }
            connection.close();

        } catch (Exception e) {
            Log.e("SaveProductTask", "Error saving product", e);
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success) {
            Toast.makeText(context, "제품을 스크랩했습니다!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Failed to save product name. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
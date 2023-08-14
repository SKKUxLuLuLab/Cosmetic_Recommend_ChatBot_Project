package com.example.lululab;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.lululab.Adapter.CustomAdapter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FetchFavoriteProductsTask extends AsyncTask<String, Void, List<String>> {

    // Your database configuration here
    public static final String DATABASE_NAME = "My_database";
    public static final String url = "" +
            DATABASE_NAME;
    public static final String username = "", password = "";
    public static final String TABLE_NAME = "Product";

    private CustomAdapter adapter;

    private String studentId;
    private Context context;

    public FetchFavoriteProductsTask(String studentId, Context context, CustomAdapter adapter) {
        this.studentId = studentId;
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    protected List<String> doInBackground(String... strings) {
        List<String> favoriteProducts = new ArrayList<>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);

            String sql = "SELECT product_name FROM " + TABLE_NAME + " WHERE studentID = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, studentId);

            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                favoriteProducts.add(resultSet.getString("product_name"));
            }

            return favoriteProducts;

        } catch (Exception e) {
            Log.e("FetchFavoriteProductsTask", "doInBackground: error", e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<String> favoriteProducts) {
        if (favoriteProducts != null) {
            adapter.updateFavoriteStatus(favoriteProducts);
        }
    }
}
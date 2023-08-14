package com.example.lululab;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.lululab.Model.Product;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class DeleteProductTask extends AsyncTask<Product, Void, Boolean> {

    public static final String DATABASE_NAME = "My_database";
    public static final String url = "" +
            DATABASE_NAME;
    public static final String username = "", password = "";

    public static final String TABLE_NAME = "Product";

    private String studentId;
    private Context context;

    public DeleteProductTask(String studentId, Context context) {
        this.studentId = studentId;
        this.context = context;
    }

    public interface OnDeleteCompleteListener {
        void onDeleteComplete(Boolean success);
    }

    private OnDeleteCompleteListener onDeleteCompleteListener;

    public void setOnDeleteCompleteListener(OnDeleteCompleteListener listener) {
        this.onDeleteCompleteListener = listener;
    }

    @Override
    protected Boolean doInBackground(Product... products) {
        Product product = products[0];
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);

            String sql = "DELETE FROM " + TABLE_NAME + " WHERE studentID= ? AND product_name= ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, studentId);
            pstmt.setString(2, product.getProduct());

            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("제품을 삭제했습니다.");
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
        if (onDeleteCompleteListener != null) {
            onDeleteCompleteListener.onDeleteComplete(success);
        }
        if (success) {
            Toast.makeText(context, "제품을 삭제했습니다.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Failed to delete product. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}

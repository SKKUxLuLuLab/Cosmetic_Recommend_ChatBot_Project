package com.example.lululab;

import static com.example.lululab.LoginActivity.password;
import static com.example.lululab.LoginActivity.url;
import static com.example.lululab.LoginActivity.username;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.lululab.Model.Message;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class DeleteMessageTask extends AsyncTask<Message, Void, Boolean> {

    public static final String DATABASE_NAME = "My_database";
    public static final String url = "" +
            DATABASE_NAME;
    public static final String username = "", password = "";

    public static final String TABLE_NAME = "Scrab";

    private String studentId;
    private Context context;

    public DeleteMessageTask(String studentId, Context context) {
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
    protected Boolean doInBackground(Message... messages) {
        Message message = messages[0];
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);

            String sql = "DELETE FROM " + TABLE_NAME + " WHERE studentId= ? AND message= ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, studentId);
            pstmt.setString(2, message.getMessage());

            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("메세지를 삭제했습니다.");
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
            Toast.makeText(context, "메세지를 삭제했습니다.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Failed to delete message. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}

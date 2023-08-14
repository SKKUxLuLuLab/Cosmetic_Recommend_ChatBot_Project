package com.example.lululab;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lululab.Model.Message;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ShowScrapMessageTask extends AsyncTask<Void, Void, List<Message>> {

    private String studentId;

    public static final String DATABASE_NAME = "My_database";
    public static final String url = "" +
            DATABASE_NAME;
    public static final String username = "", password = "";

    public static final String TABLE_NAME = "Scrab";

    private Context context;
    private ListView listView;

    public interface OnMessageDeletedListener {
        void onMessageDeleted();
    }

    private OnMessageDeletedListener onMessageDeletedListener;

    private MyPageActivity myPageActivity;

    public ShowScrapMessageTask(String studentId, MyPageActivity myPageActivity, ListView listView) {
        this.studentId = studentId;
        this.context = myPageActivity;
        this.listView = listView;
        this.myPageActivity = myPageActivity;
    }

    @Override
    protected List<Message> doInBackground(Void... voids) {
        List<Message> messages = new ArrayList<>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM " + TABLE_NAME + " WHERE studentId = '" + studentId + "'";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                // 아래의 코드는 Message 클래스가 어떤 형태인지에 따라 수정되어야 합니다.
                // 적절한 생성자를 사용하도록 바꾸어 주세요.
                Message message = new Message(rs.getString("message"), null, null);
                messages.add(message);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    protected void onPostExecute(List<Message> messages) {

        class MessageAdapter extends ArrayAdapter<Message> {
            MessageAdapter(Context context, List<Message> messages) {
                super(context, R.layout.list_item_message, messages);
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.list_item_message, parent, false);
                }

                Message message = getItem(position);
                TextView textView = convertView.findViewById(R.id.message_text);
                ImageButton deleteButton = convertView.findViewById(R.id.button_delete);

                textView.setText(message.getMessage());
                deleteButton.setOnClickListener(v -> {
                    // AlertDialog 생성
                    new AlertDialog.Builder(context)
                            .setTitle("삭제 확인")
                            .setMessage("정말로 삭제하시겠습니까?")
                            // Positive 버튼 설정
                            .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                                // 삭제 작업 수행
                                DeleteMessageTask deleteTask = new DeleteMessageTask(studentId, context);
                                deleteTask.setOnDeleteCompleteListener(success -> {
                                    if (success) {
                                        remove(getItem(position)); // 리스트에서 해당 아이템을 제거합니다.
                                        notifyDataSetChanged(); // 리스트뷰에게 데이터셋이 변경되었음을 알립니다.
                                    }
                                });
                                deleteTask.execute(message);
                            })
                            // Negative 버튼 설정
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                });

                return convertView;
            }
        }

        listView.setAdapter(new MessageAdapter(context, messages));
    }
}

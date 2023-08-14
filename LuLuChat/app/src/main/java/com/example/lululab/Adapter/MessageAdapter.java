package com.example.lululab.Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lululab.DeleteMessageTask;
import com.example.lululab.LoginActivity;
import com.example.lululab.MainActivity;
import com.example.lululab.Model.Message;
import com.example.lululab.R;

import java.util.ArrayList;
import java.util.List;
import com.bumptech.glide.Glide;
import com.example.lululab.saveMessageTask;

import android.widget.ImageView;

import org.json.JSONException;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    List<Message> messageList;
    List<LinearLayout> leftChatViews;
    private Context context;
    private int doubleClickFlag = 0;
    private final long  CLICK_DELAY = 250;
    private int isScrab = 0;
    private boolean isButtonClicked = false;

    public interface OnButtonClickListener {
        void onButtonClick(int position) throws JSONException;
    }

    private OnButtonClickListener onButtonClickListener;

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.onButtonClickListener = listener;
    }


    public MessageAdapter(List<Message> messageList){
        this.messageList = messageList;
        leftChatViews = new ArrayList<>();
    }
    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messageList.get(position);
        if(message.getSentBy().equals(Message.SENT_BY_ME)){
            holder.chat_item_button.setVisibility((View.GONE));
            holder.left_suggest_tv.setVisibility((View.GONE));
            holder.left_chat_view.setVisibility(View.GONE);
            holder.right_chat_view.setVisibility(View.VISIBLE);
            holder.right_chat_iv.setVisibility(View.GONE);
            holder.left_chat_iv.setVisibility(View.GONE);

            holder.right_chat_tv.setText(message.getMessage());
            if (message.getImageUrl() != null) {
                holder.right_chat_iv.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(message.getImageUrl())
                        .into(holder.right_chat_iv);
            } else {
                holder.right_chat_iv.setVisibility(View.GONE);
            }
        } else if(message.getSentBy().equals(Message.SENT_BY_BOT)){
            holder.left_suggest_tv.setVisibility((View.GONE));
            holder.left_chat_iv.setVisibility(View.GONE);
            holder.right_chat_iv.setVisibility(View.GONE);
            holder.chat_item_button.setVisibility((View.GONE));
            holder.right_chat_view.setVisibility(View.GONE);
            holder.left_chat_view.setVisibility(View.VISIBLE);
            holder.left_chat_tv.setText(message.getMessage());

            if (message.getIsScrab()) {
                int newColor = ContextCompat.getColor(context, R.color.green);
                holder.left_chat_view.setBackgroundTintList(ColorStateList.valueOf(newColor));
            } else {
                int newColor = ContextCompat.getColor(context, R.color.pink);
                holder.left_chat_view.setBackgroundTintList(ColorStateList.valueOf(newColor));
            }

            if (message.getImageUrl() != null) {
                holder.left_chat_iv.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(message.getImageUrl())
                        .into(holder.left_chat_iv);
            } else {
                holder.left_chat_iv.setVisibility(View.GONE);
            }
        }
        else {
            holder.left_suggest_tv.setVisibility((View.VISIBLE));
            holder.chat_item_button.setVisibility((View.VISIBLE));
            holder.left_chat_iv.setVisibility(View.VISIBLE);
            holder.right_chat_view.setVisibility(View.GONE);
            holder.left_chat_view.setVisibility(View.VISIBLE);
            holder.left_chat_tv.setText(message.getMessage());
        }



    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout left_chat_view, right_chat_view;
        TextView left_chat_tv, right_chat_tv, left_suggest_tv;
        ImageView left_chat_iv, right_chat_iv; // 추가
        Button chat_item_button;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            left_chat_view = itemView.findViewById(R.id.left_chat_view);
            right_chat_view = itemView.findViewById(R.id.right_chat_view);
            left_chat_tv = itemView.findViewById(R.id.left_chat_tv);
            right_chat_tv = itemView.findViewById(R.id.right_chat_tv);
            left_chat_iv = itemView.findViewById(R.id.left_chat_iv); // 추가
            right_chat_iv = itemView.findViewById(R.id.right_chat_iv); // 추가
            chat_item_button = itemView.findViewById(R.id.chat_item_view_button); // 추가
            left_suggest_tv = itemView.findViewById(R.id.left_suggest_tv); //추가

            chat_item_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onButtonClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            try {
                                onButtonClickListener.onButtonClick(position);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    }
                }
            });


            left_chat_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println(100);
                    int position = getAdapterPosition();
                    Message clickedMessage = messageList.get(position);

                    doubleClickFlag++;
                    Handler handler = new Handler();
                    Runnable clickRunnable = new Runnable() {
                        @Override
                        public void run() {
                            doubleClickFlag = 0;
                        }
                    };
                    if (doubleClickFlag == 1) {
                        handler.postDelayed(clickRunnable, CLICK_DELAY);
                    } else if (doubleClickFlag == 2) {
                        doubleClickFlag = 0;

                        // 더블클릭 이벤트: 메시지의 isScrab 상태 변경 및 채팅 뷰 색상 변경
                        if (!clickedMessage.getIsScrab()) {
                            clickedMessage.setIsScrab(true);
                            int newColor = ContextCompat.getColor(view.getContext(), R.color.green);
                            view.setBackgroundTintList(ColorStateList.valueOf(newColor));
                            new saveMessageTask(MainActivity.studentId, context).execute(clickedMessage);
                        } else {
                            clickedMessage.setIsScrab(false);
                            int newColor = ContextCompat.getColor(view.getContext(), R.color.pink);
                            view.setBackgroundTintList(ColorStateList.valueOf(newColor));
                            new DeleteMessageTask(MainActivity.studentId, context).execute(clickedMessage);
                        }
                    }
                }
            });
        }
    }
}
package com.example.lululab.Adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lululab.DictionaryActivity;
import com.example.lululab.MainActivity;
import com.example.lululab.Model.Product;
import com.example.lululab.R;
import com.example.lululab.SaveProductTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<String> productList;
    private List<LinearLayout> prod_scrab;
    private String searchText;

    public ProductAdapter(List<String> productList) {
        this.productList = productList;
        prod_scrab = new ArrayList<>();
    }

    public void setProductList(List<String> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        String productName = productList.get(position); // 변경한 부분
        holder.productNameTextView.setText(highlightSearchText(productName, searchText)); // 변경한 부분

        holder.starButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재 클릭한 제품의 이름을 가져옵니다.
                String productName = holder.productNameTextView.getText().toString();
                // 제품 이름을 데이터베이스에 저장합니다.
                new SaveProductTask(MainActivity.studentId, v.getContext()).execute(productName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
    private SpannableString highlightSearchText(String productName, String searchText) {
        SpannableString spannableString = new SpannableString(productName);
        if (searchText != null && !searchText.isEmpty()) {
            int start = productName.toLowerCase().indexOf(searchText.toLowerCase());
            if (start >= 0) {
                int end = start + searchText.length();
                spannableString.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannableString;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        public ImageButton starButton;
        TextView productNameTextView;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.product_name);
        }
    }

}

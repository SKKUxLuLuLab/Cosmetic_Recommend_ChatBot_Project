package com.example.lululab.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lululab.FetchFavoriteProductsTask;
import com.example.lululab.DeleteProductTask;
import com.example.lululab.MainActivity;
import com.example.lululab.Model.Product;
import com.example.lululab.R;
import com.example.lululab.SaveProductTask;

import java.util.HashMap;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<String> {

    private HashMap<String, Boolean> favoriteStatus = new HashMap<>();

    public CustomAdapter(Context context, int resource, int textViewResourceId, List<String> objects, String studentId) {
        super(context, resource, textViewResourceId, objects);
        new FetchFavoriteProductsTask(studentId, getContext(), this).execute();
    }

    public void updateFavoriteStatus(List<String> favoriteProducts) {
        for (String product : favoriteProducts) {
            favoriteStatus.put(product, true);
        }
        notifyDataSetChanged();  // Adapter data set has changed, refresh the view
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.product_item, null);
        } else {
            view = convertView;
        }

        TextView productNameTextView = view.findViewById(R.id.product_name);
        ImageButton heartButton = view.findViewById(R.id.heart_button);

        String productName = getItem(position);
        productNameTextView.setText(productName);

        Boolean isFavorite = favoriteStatus.get(productName);
        if (isFavorite != null && isFavorite) {
            heartButton.setImageResource(R.drawable.full_heart);
            heartButton.setTag(R.drawable.full_heart);
        } else {
            heartButton.setImageResource(R.drawable.empty_heart);
            heartButton.setTag(R.drawable.empty_heart);
        }

        heartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((Integer) heartButton.getTag() == R.drawable.empty_heart) {
                    heartButton.setImageResource(R.drawable.full_heart);
                    heartButton.setTag(R.drawable.full_heart);
                    favoriteStatus.put(productName, true);
                    new SaveProductTask(MainActivity.studentId, getContext()).execute(productName);
                } else {
                    heartButton.setImageResource(R.drawable.empty_heart);
                    heartButton.setTag(R.drawable.empty_heart);
                    favoriteStatus.put(productName, false);
                    Product product = new Product(productName, null, null);
                    new DeleteProductTask(MainActivity.studentId, getContext()).execute(product);
                }
            }
        });

        return view;
    }
}
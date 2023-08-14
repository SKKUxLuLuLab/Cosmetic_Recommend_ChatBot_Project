package com.example.lululab;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.lululab.Adapter.CustomAdapter;
import com.example.lululab.Adapter.ProductAdapter;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class DictionaryActivity extends AppCompatActivity {

    private Spinner category;
    private TextView tvSubcategory;
    private RecyclerView recyclerviewSubcategories;
    private HashMap<String, HashMap<String, List<String>>> categories;
    Button close_button;
    private ListView listView;
    private EditText searchEditText;
    private ArrayAdapter<String> adapter;
    private List<String> productNames;
    private ProductAdapter productAdapter;
    private LinearLayout product_item;
    private String badIngredient = null;

    private List<Product> productList = new ArrayList<>();

    public class Product {
        private String name;
        private String category;
        private String categoryDetail;
        private String skinTypes;
        private String ingredients;

        // constructor, getters and setters
        public Product(String name, String category, String categoryDetail, String skinTypes, String ingredients) {
            this.name = name;
            this.category = category;
            this.categoryDetail = categoryDetail;
            this.skinTypes = skinTypes;
            this.ingredients = ingredients;
        }

        public String getCategory() {return category;}
        public String getCategoryDetail() {return categoryDetail;}
        public String getSkinTypes() {return skinTypes;}
        public String getName() {return name;}
        public String getIngredients() {return ingredients;}
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        category = findViewById(R.id.category);
        category.setBackgroundResource(R.drawable.spinner_border);

        tvSubcategory = findViewById(R.id.tv_subcategory);
        recyclerviewSubcategories = findViewById(R.id.recyclerview_subcategories);
        Button filterButton = findViewById(R.id.filter_button);

        View prod = getLayoutInflater().inflate(R.layout.product_item, null);
        product_item = prod.findViewById(R.id.prod_scrab);

        RelativeLayout filterLayout = findViewById(R.id.filter_layout);
        listView=findViewById(R.id.listView);
        searchEditText=findViewById(R.id.searchEditText);


        productNames = readCSV();
        adapter = new CustomAdapter(this, R.layout.product_item, R.id.product_name, productNames, MainActivity.studentId);
        listView.setAdapter(adapter);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.product_item, null);

        productAdapter = new ProductAdapter(productNames);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Filter the product names based on the entered text
                String searchText = s.toString().toLowerCase();

                List<String> selectedSkinTypes = new ArrayList<>();
                LinearLayout tagLayout = findViewById(R.id.tag_layout);
                for (int i = 0; i < tagLayout.getChildCount(); i++) {
                    TextView skinTypeView = (TextView) tagLayout.getChildAt(i);
                    selectedSkinTypes.add(skinTypeView.getText().toString());
                }

                List<String> filteredNames = new ArrayList<>();
                for (String name : productNames) {
                    if (name.toLowerCase().contains(searchText)) {
                        filteredNames.add(name);
                    }
                }

                // Create a new adapter with the filtered product names
                CustomAdapter newAdapter = new CustomAdapter(DictionaryActivity.this, R.layout.product_item, R.id.product_name, filteredNames, MainActivity.studentId);


                // Set the new adapter
                listView.setAdapter(newAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerviewSubcategories.setLayoutManager(layoutManager);
        categories = new HashMap<>();

        // Assign the subcategories to each category
        categories.put("전체", new HashMap<String, List<String>>() {{
            put("전체", Arrays.asList("전체"));
        }});
        categories.put("선케어", new HashMap<String, List<String>>() {{
            put("선케어", Arrays.asList("전체","기타", "선로션", "선스틱", "선스프레이", "선쿠션", "선크림", "선파우더", "스크럽", "필링"));
        }});
        categories.put("스크럽", new HashMap<String, List<String>>() {{
            put("스크럽", Arrays.asList("전체","스크럽", "필링"));
        }});
        categories.put("에센스", new HashMap<String, List<String>>() {{
            put("에센스", Arrays.asList("전체","기타", "세럼", "앰플", "에센스"));
        }});
        categories.put("크림", new HashMap<String, List<String>>() {{
            put("크림", Arrays.asList("전체","기타", "로션", "밤", "보습크림", "수딩젤", "수분크림", "스팟젤", "에멀전"));
        }});
        categories.put("클렌징", new HashMap<String, List<String>>() {{
            put("클렌징", Arrays.asList("전체","기타", "립&아이 리무버", "클렌징 밀크", "클렌징 밤", "클렌징 비누", "클렌징 오일", "클렌징 워터"
                    , "클렌징 젤", "클렌징 크림", "클렌징 티슈", "클렌징 파우더", "클렌징 패드", "클렌징 폼"));
        }});
        categories.put("토너", new HashMap<String, List<String>>() {{
            put("토너", Arrays.asList("전체","기타", "스킨", "토너"));
        }});
        categories.put("팩", new HashMap<String, List<String>>() {{
            put("팩", Arrays.asList("기타", "마스크팩", "모델링팩", "슬리핑팩", "시트팩", "워시오프팩", "코팩", "패치", "필오프팩"));
        }});


        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.custom_spinner, new ArrayList<>(categories.keySet())) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                return view;
            }
        };
        category.setAdapter(spinnerAdapter);

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = parent.getItemAtPosition(position).toString();
                tvSubcategory.setText("전체");

                // Update the subcategories recycler view
                HashMap<String, List<String>> subcategoriesMap = categories.get(selectedCategory);
                for(String key : subcategoriesMap.keySet()) {
                    List<String> subcategoriesList = subcategoriesMap.get(key);
                    recyclerviewSubcategories.setAdapter(new SubcategoriesAdapter(subcategoriesList, DictionaryActivity.this));
                    break; // Assuming there's only one entry in the map
                }

                // Filter the product list based on the selected category and update the list view
                List<String> filteredNames = filterProductNames("", selectedCategory, null, new ArrayList<>());
                updateProductList(filteredNames);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        filterButton.setBackgroundResource(R.drawable.spinner_border);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DictionaryActivity.this);
                builder.setTitle("피부 타입 및 피부 질환 선택");

                // Set up the items
                String[] skinTypes = {"OS", "OR", "NS", "NR", "DS", "DR", "OS-A", "OR-A", "NS-A", "NR-A", "DS-A", "NR-A"};
                boolean[] checkedItems = {false, false, false, false, false, false, false, false, false, false, false, false};
                builder.setMultiChoiceItems(skinTypes, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        // Store the state of the selected items
                        checkedItems[which] = isChecked;
                    }
                });

                // Set the positive/yes button click listener
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Get the LinearLayout
                        LinearLayout tagLayout = findViewById(R.id.tag_layout);

                        // Clear all previous tags
                        tagLayout.removeAllViews();

                        // Add a tag for each selected item
                        List<String> selectedSkinTypes = new ArrayList<>();
                        for (int i = 0; i < checkedItems.length; i++) {
                            if (checkedItems[i]) {
                                String skinType = skinTypes[i];
                                selectedSkinTypes.add(skinType);
                                Log.d("SelectedSkinType", skinType);

                                TextView tag = new TextView(DictionaryActivity.this);
                                tag.setText(skinType);
                                tag.setBackgroundResource(R.color.teal);
                                // Set margin to the TextView
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT, // Width of TextView
                                        LinearLayout.LayoutParams.WRAP_CONTENT); // Height of TextView

                                // Set the margins
                                int dpValue = 20;  // desired value in dp
                                float density = getResources().getDisplayMetrics().density;
                                int marginValue = (int)(dpValue * density);
                                layoutParams.setMargins(10, marginValue, 10, marginValue); // Changed the second and fourth arguments
                                tag.setLayoutParams(layoutParams);

                                tagLayout.addView(tag);
                            }
                        }

                        // Filter product list based on the selected category, subcategory, and skin types
                        String selectedCategory = category.getSelectedItem().toString();
                        String selectedSubcategory = tvSubcategory.getText().toString();
                        List<String> filteredNames = filterProductNames("", selectedCategory, selectedSubcategory, selectedSkinTypes);
                        updateProductList(filteredNames);
                    }
                });

                // Set the negative/no button click listener
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // You can do something when the user clicks the negative button
                    }
                });

                // Display the alert dialog on interface
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        Button ingredientFilterButton = findViewById(R.id.ingredient_filter_button);
        ingredientFilterButton.setBackgroundResource(R.drawable.spinner_border);
        ingredientFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIngredientFilterDialog();
            }
        });

        Button searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterLayout.setVisibility(View.GONE);

                String selectedCategory = category.getSelectedItem().toString();
                String selectedSubcategory = tvSubcategory.getText().toString();

                LinearLayout tagLayout = findViewById(R.id.tag_layout);
                List<String> selectedSkinTypes = new ArrayList<>();
                for (int i = 0; i < tagLayout.getChildCount(); i++) {
                    TextView skinTypeView = (TextView) tagLayout.getChildAt(i);
                    selectedSkinTypes.add(skinTypeView.getText().toString());
                }

                List<String> filteredNames = filterProductNames("", selectedCategory, selectedSubcategory, selectedSkinTypes);
                updateProductList(filteredNames);
            }
        });

        Button skinTypeOR = findViewById(R.id.skin_type_OR);
        skinTypeOR.setOnClickListener(v -> {
            // Change the button color when clicked
        });
    }

    private void updateProductList(List<String> filteredNames) {
        // Update the adapter with the filtered names
        adapter.clear();
        adapter.addAll(filteredNames);
        adapter.notifyDataSetChanged();
        Log.d("DEBUG", "Updating product list with: " + filteredNames);
    }

    private List<String> readCSV() {
        List<String> productNames = new ArrayList<>();
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.cosmetics);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            CSVReader csvReader = new CSVReaderBuilder(inputStreamReader)
                    .withCSVParser(new CSVParserBuilder().withSeparator(',').withQuoteChar('"').build())
                    .build();

            String[] line;
            line=csvReader.readNext();
            while ((line = csvReader.readNext()) != null) {
                if (line.length > 0) {
                    String name = line[0];
                    String category = line[2];
                    String category_detail = line[3];
                    String ingredients = line[5];
                    String skin_types = line[7];

                    String[] skinTypesArray = skin_types.split(",");
                    List<String> skinTypes = new ArrayList<>();
                    for (String skinType : skinTypesArray) {
                        skinTypes.add(skinType.trim());
                    }

                    Product product = new Product(name, category, category_detail, skin_types, ingredients);
                    productList.add(product);
                    productNames.add(name);
                }
            }

            csvReader.close();

        } catch (IOException | CsvException e) {
            Log.e("CSV", "Error reading CSV file: " + e.getMessage());
        }
        return productNames;
    }
    private void showIngredientFilterDialog() {
        // Create a dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("성분 필터");

        // Use a LinearLayout as the custom view
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText badIngredientInput = new EditText(this);
        badIngredientInput.setHint("제외할 성분을 입력하세요");
        layout.addView(badIngredientInput);

        // Add the layout to the dialog
        builder.setView(layout);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // When the OK button is clicked, filter the products
                badIngredient = badIngredientInput.getText().toString();
                List<String> filteredNames = filterProductsByIngredients(badIngredient);
                updateProductList(filteredNames);
            }
        });

        builder.setNegativeButton("Cancel", null);

        // Show the dialog
        builder.show();
    }

    public List<String> filterProductsByIngredients(String badIngredient) {
        List<Product> filteredList = new ArrayList<>();
        Log.d("DEBUG", "Filtering products by ingredients");
        Log.d("DEBUG", "Bad ingredient: " + badIngredient);

        for (Product product : productList) {
            String ingredients = product.getIngredients();
            String[] ingredientArray = ingredients.split(";"); // split the ingredient list

            boolean hasBadIngredient = false;
            for (String ingredient : ingredientArray) {
                if (ingredient.trim().equalsIgnoreCase(badIngredient)) {  // Check each ingredient individually
                    hasBadIngredient = true;
                    Log.d("DEBUG", "Found bad ingredient in product: " + product.getName());
                    break;
                }
            }

            if (!hasBadIngredient) {
                filteredList.add(product);
            }
            else {
                Log.d("DEBUG", "Excluding product: " + product.getName());
            }
        }
        this.productList = filteredList;
        List<String> productNames = new ArrayList<>();
        for (Product product : filteredList) {
            productNames.add(product.getName());
        }
        return productNames;
    }

    private List<String> filterProductNames(String searchKeyword, String category, String category_detail, List<String> skinTypes) {
        List<String> filteredNames = new ArrayList<>();

        for (Product product : productList) {
            String ingredients = product.getIngredients();
            Log.d("melon", badIngredient != null ? badIngredient : "badIngredient is null");
            if (product.getName().toLowerCase().contains(searchKeyword.toLowerCase())
                    && ("전체".equals(category) || product.getCategory().equals(category))
                    && (badIngredient == null || ingredients == null || !ingredients.contains(badIngredient))) {
                if (category_detail == null || "전체".equals(category_detail) || product.getCategoryDetail().equals(category_detail)) {
                    if(skinTypes == null || skinTypes.isEmpty() || productMatchesSkinTypes(product, skinTypes)) {
                        filteredNames.add(product.getName());
                    }
                }
            }
        }
        return filteredNames;
    }


    private boolean productMatchesSkinTypes(Product product, List<String> selectedSkinTypes) {
        List<String> productSkinTypes = Arrays.asList(product.getSkinTypes().split(";"));
        for (String selectedSkinType : selectedSkinTypes) {
            if (productSkinTypes.contains(selectedSkinType)) {
                return true;
            }
        }
        return false;
    }

    private class SubcategoriesAdapter extends RecyclerView.Adapter<SubcategoriesAdapter.SubcategoryViewHolder> {
        private Context context;
        private List<String> subcategories;

        public SubcategoriesAdapter(List<String> subcategories, Context context) {
            this.subcategories = subcategories;
            this.context = context;
        }

        @NonNull
        @Override
        public SubcategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.button, parent, false);
            return new SubcategoryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SubcategoryViewHolder holder, int position) {
            String subcategory = subcategories.get(position);
            holder.btnSubcategory.setText(subcategory);
            holder.btnSubcategory.setBackgroundResource(R.drawable.spinner_border);
            holder.btnSubcategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvSubcategory.setText(subcategory);
                    Log.d("SelectedSubcategory", subcategory);  // 로그 추가

                    // Fetch selected skin types
                    List<String> selectedSkinTypes = new ArrayList<>();
                    LinearLayout tagLayout = findViewById(R.id.tag_layout);
                    for (int i = 0; i < tagLayout.getChildCount(); i++) {
                        TextView skinTypeView = (TextView) tagLayout.getChildAt(i);
                        selectedSkinTypes.add(skinTypeView.getText().toString());
                    }

                    // Filter product list based on the selected category, subcategory, and skin types
                    String selectedCategory = category.getSelectedItem().toString();
                    List<String> filteredNames = filterProductNames("", selectedCategory, subcategory, selectedSkinTypes);
                    Log.d("DEBUG", "Filtered names in subcategory click: " + filteredNames);
                    updateProductList(filteredNames);
                }
            });
        }

        @Override
        public int getItemCount() {
            return subcategories.size();
        }

        class SubcategoryViewHolder extends RecyclerView.ViewHolder {
            Button btnSubcategory;

            public SubcategoryViewHolder(@NonNull View itemView) {
                super(itemView);
                btnSubcategory = itemView.findViewById(R.id.btn_subcategory);
            }
        }
    }
}

package com.agencytba.tba;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agencytba.tba.adapters.SubCategoriesAdapter;
import com.agencytba.tba.modals.SubCategories;
import com.agencytba.tba.server.Constants;

import java.util.ArrayList;
import java.util.List;

public class SubCategoriesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SharedPreferences mPref,Pref;
    private List<SubCategories> subcategoriesList;
    private RecyclerView.Adapter adapter;
    int doneSubCategories = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategories);
        mPref = getSharedPreferences("Constants", 0);
        Pref = getSharedPreferences("Radios", 0);
        subcategoriesList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        loadUrlData();
        Pref.edit().putString("doneStrings"+mPref.getString(Constants.MARKET_NAME, "")+"/"+mPref.getString(Constants.CATEGORY_NAME, ""),"").apply();
    }

    private void loadUrlData() {
        String[] subCategories = mPref.getString(Constants.SUBCATEGORIES, "").split(", ");
        for (String subCategory : subCategories) {
            boolean done = Pref.getBoolean(mPref.getString(Constants.MARKET_NAME, "")+"/"+mPref.getString(Constants.CATEGORY_NAME, "")+"/"+subCategory,false);
            if (getSharedPreferences("MarketAnalyze", 0).getInt(mPref.getString(Constants.MARKET_NAME, "") + "Planogram", 0) > 0 && getSharedPreferences("MarketAnalyze", 0).getInt(mPref.getString(Constants.MARKET_NAME, "") + "SOSIC", 0) > 0) {
                SubCategories ctgrs = new SubCategories(mPref.getString(Constants.CATEGORY_NAME, ""), subCategory, done, false);
                subcategoriesList.add(ctgrs);
                if (done) doneSubCategories++;
            } else if (getSharedPreferences("MarketAnalyze", 0).getInt(mPref.getString(Constants.MARKET_NAME, "") + "Planogram", 0) == 0 && getSharedPreferences("MarketAnalyze", 0).getInt(mPref.getString(Constants.MARKET_NAME, "") + "SOSIC", 0) > 0) {
                if (!subCategory.equals("Planogram")) {
                    SubCategories ctgrs = new SubCategories(mPref.getString(Constants.CATEGORY_NAME, ""), subCategory, done, false);
                    subcategoriesList.add(ctgrs);
                    if (done) doneSubCategories++;
                }
            } else if (getSharedPreferences("MarketAnalyze", 0).getInt(mPref.getString(Constants.MARKET_NAME, "") + "Planogram", 0) > 0 && getSharedPreferences("MarketAnalyze", 0).getInt(mPref.getString(Constants.MARKET_NAME, "") + "SOSIC", 0) == 0) {
                if (!subCategory.equals("SOS")) {
                    SubCategories ctgrs = new SubCategories(mPref.getString(Constants.CATEGORY_NAME, ""), subCategory, done, false);
                    subcategoriesList.add(ctgrs);
                    if (done) doneSubCategories++;
                }
            } else if (getSharedPreferences("MarketAnalyze", 0).getInt(mPref.getString(Constants.MARKET_NAME, "") + "Planogram", 0) == 0 && getSharedPreferences("MarketAnalyze", 0).getInt(mPref.getString(Constants.MARKET_NAME, "") + "SOSIC", 0) == 0) {
                if (!subCategory.equals("Planogram") && !subCategory.equals("SOS")) {
                    SubCategories ctgrs = new SubCategories(mPref.getString(Constants.CATEGORY_NAME, ""), subCategory, done, false);
                    subcategoriesList.add(ctgrs);
                    if (done) doneSubCategories++;
                }
            }
        }
        adapterSet();
    }

    public void adapterSet() {
        if (doneSubCategories == subcategoriesList.size()) Pref.edit().putBoolean(mPref.getString(Constants.MARKET_NAME, "")+"/"+mPref.getString(Constants.CATEGORY_NAME, ""),true).apply();

        adapter = new SubCategoriesAdapter(subcategoriesList, this);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                llm.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,CategoriesActivity.class);
        startActivity(intent);
    }
}

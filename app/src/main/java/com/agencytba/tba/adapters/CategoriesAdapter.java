package com.agencytba.tba.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.agencytba.tba.R;
import com.agencytba.tba.SubCategoriesActivity;
import com.agencytba.tba.modals.Categories;
import com.agencytba.tba.server.Constants;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    private SharedPreferences mPref;

    // we define a list from the DevelopersList java class

    private List<Categories> categoriesList;
    private Context context;
    public boolean isDone = false;

    public CategoriesAdapter(List<Categories> categoryList, Context context) {

        // generate constructors to initialise the List and Context objects

        this.categoriesList = categoryList;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // this method will be called whenever our ViewHolder is created
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categories, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        // this method will bind the data to the ViewHolder from whence it'll be shown to other Views
        mPref = context.getSharedPreferences("Constants", 0);
        SharedPreferences Pref = context.getSharedPreferences("Radios",0);
        SharedPreferences marketPref = context.getSharedPreferences("MarketAnalyze", 0);
        final Categories mList = categoriesList.get(position);

        if (mList.getInternet_no()) {
            holder.doneI.setVisibility(View.GONE);
            holder.halfDoneI.setVisibility(View.VISIBLE);
        } else holder.halfDoneI.setVisibility(View.GONE);
        if (mList.getDone()) {
            holder.doneI.setVisibility(View.VISIBLE);
            holder.halfDoneI.setVisibility(View.GONE);
        } else holder.doneI.setVisibility(View.GONE);

        holder.categoryN.setText(mList.getCategory_name());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mPref.edit();
                editor.putString(Constants.CATEGORY_NAME, mList.getCategory_name());
                editor.putString(Constants.SUBCATEGORIES, mList.getSubcategories());
                editor.apply();
                Intent intent = new Intent(context, SubCategoriesActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override

    public int getItemCount() {
        return categoriesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // define the View objects

        public TextView categoryN;
        public ImageView doneI,halfDoneI;
        public ViewHolder(View itemView) {
            super(itemView);

            // initialize the View objects
            categoryN = (TextView) itemView.findViewById(R.id.category_name);
            doneI = (ImageView) itemView.findViewById(R.id.done);
            halfDoneI = (ImageView) itemView.findViewById(R.id.half_done);
        }

    }
}
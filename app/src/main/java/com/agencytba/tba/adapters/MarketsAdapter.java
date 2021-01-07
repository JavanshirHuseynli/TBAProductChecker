package com.agencytba.tba.adapters;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.agencytba.tba.CategoriesActivity;
import com.agencytba.tba.MainActivity;
import com.agencytba.tba.R;
import com.agencytba.tba.modals.Markets;
import com.agencytba.tba.server.Constants;

import java.util.ArrayList;
import java.util.List;

import static com.agencytba.tba.MainActivity.countFinished;
import static com.agencytba.tba.MainActivity.countRemaining;

public class MarketsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private SharedPreferences mPref;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private List<Markets> marketsList, getMarketsListFiltered;
    private Context context;

    private AlertDialog dialog, dialogAnalyze;

    public MarketsAdapter(List<Markets> marketList, Context context) {

        // generate constructors to initialise the List and Context objects

        this.marketsList = marketList;
        this.getMarketsListFiltered = marketList;
        this.context = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // this method will be called whenever our ViewHolder is created
        if (viewType == TYPE_ITEM) {
            // Here Inflating your recyclerview item layout
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.markets, parent, false);
            return new ViewHolder(itemView);
        } else if (viewType == TYPE_HEADER) {
            // Here Inflating your header view
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_header, parent, false);
            return new HeaderViewHolder(itemView);
        }
        else return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder){
            //setheadersdata_flag = true;
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

            // You have to set your header items values with the help of model class and you can modify as per your needs
            if (((MainActivity)context).isSearchOpened()) {
                headerViewHolder.headerL.setVisibility(View.GONE);
                headerViewHolder.headerL.setLayoutParams(new ViewGroup.LayoutParams(0,0));
            }
            if (!((MainActivity)context).isSearchOpened()) {
                headerViewHolder.headerL.setVisibility(View.VISIBLE);
                headerViewHolder.headerL.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            headerViewHolder.txt_finished.setText(String.valueOf(countFinished));
            headerViewHolder.txt_remaining.setText(String.valueOf(countRemaining));

        }
        else if (holder instanceof ViewHolder){

            final ViewHolder itemViewHolder = (ViewHolder) holder;
            mPref = context.getSharedPreferences("Constants", 0);
            SharedPreferences Pref = context.getSharedPreferences("Radios", 0);
            final Markets mList = getMarketsListFiltered.get(position-1);

            if (((MainActivity)context).isSearchOpened()) {
                itemViewHolder.doneI.setVisibility(View.GONE);
                itemViewHolder.halfDoneI.setVisibility(View.GONE);
                itemViewHolder.header.setVisibility(View.GONE);
            }
            if (!((MainActivity)context).isSearchOpened()) {
                if (context.getSharedPreferences("MarketAnalyze", 0).getInt(mList.getMarket_name() + "OSA", 0) == 0 && context.getSharedPreferences("MarketAnalyze", 0).getInt(mList.getMarket_name() + "NPD", 0) == 0 && context.getSharedPreferences("MarketAnalyze", 0).getInt(mList.getMarket_name() + "Planogram", 0) == 0 && context.getSharedPreferences("MarketAnalyze", 0).getInt(mList.getMarket_name() + "SOS", 0) == 0 && context.getSharedPreferences("MarketAnalyze", 0).getInt(mList.getMarket_name() + "SOD", 0) == 0 && context.getSharedPreferences("MarketAnalyze", 0).getInt(mList.getMarket_name() + "OSAIC", 0) == 0 && context.getSharedPreferences("MarketAnalyze", 0).getInt(mList.getMarket_name() + "NPDIC", 0) == 0 && context.getSharedPreferences("MarketAnalyze", 0).getInt(mList.getMarket_name() + "PlanogramIC", 0) == 0 && context.getSharedPreferences("MarketAnalyze", 0).getInt(mList.getMarket_name() + "SOSIC", 0) == 0) {
                    itemViewHolder.doneI.setVisibility(View.VISIBLE);
                    itemViewHolder.halfDoneI.setVisibility(View.GONE);
                } else {
                    if (mList.getInternet_no()) {
                        itemViewHolder.doneI.setVisibility(View.GONE);
                        itemViewHolder.halfDoneI.setVisibility(View.VISIBLE);
                    } else {
                        if (mList.getDone()) {
                            itemViewHolder.doneI.setVisibility(View.VISIBLE);
                        } else {
                            itemViewHolder.doneI.setVisibility(View.GONE);
                        }
                        itemViewHolder.halfDoneI.setVisibility(View.GONE);
                    }
                }
                itemViewHolder.header.setText(mList.getCity());

                if (position > 1 && getMarketsListFiltered.get(position - 1).getCity().substring(0, 3).equals(mList.getCity().substring(0, 3))) {
                    itemViewHolder.header.setVisibility(View.GONE);
                } else {
                    itemViewHolder.header.setVisibility(View.VISIBLE);
                }
            }

            itemViewHolder.marketN.setText(mList.getMarket_name());
            itemViewHolder.marketA.setText(mList.getMarket_address());

            itemViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("market_company", mList.getMarket_name() + "#" + mList.getCompany());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(context, "Texts copied", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

            itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor editor = mPref.edit();
                    editor.putString(Constants.CATEGORIES_TO_CHECK, mList.getCategories_to_check());
                    editor.putString(Constants.MARKET_TYPE, mList.getMarket_type());
                    editor.putString(Constants.MARKET_CITY, mList.getCity());
                    editor.putString(Constants.MARKET_LOCATION, mList.getLocation());
                    editor.putString(Constants.MARKET_NAME, mList.getMarket_name());
                    editor.putString(Constants.MARKET_MANAGERHPCFK, mList.getManager());
                    editor.putString(Constants.MARKET_MANAGERIC, mList.getManager2());
                    editor.putString(Constants.MARKET_SUPERVISORHPCFK, mList.getSupervisor());
                    editor.putString(Constants.MARKET_SUPERVISORHPCFK2, mList.getSupervisor2());
                    editor.putString(Constants.MARKET_SUPERVISORIC, mList.getSupervisor3());
                    editor.putString(Constants.MARKET_EXPEDITORHPCFK, mList.getExpeditor());
                    editor.putString(Constants.MARKET_EXPEDITORHPCFK2, mList.getExpeditor2());
                    editor.putString(Constants.MARKET_EXPEDITORIC, mList.getExpeditor3());
                    editor.putString(Constants.MARKET_FIELD_SUPERVISOR, mList.getField_supervisor());
                    editor.apply();
                    showDialog(mList.getMarket_name());
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return getMarketsListFiltered.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // define the View objects

        public TextView marketN, marketA, header;
        public ImageView doneI,halfDoneI;

        public ViewHolder(View itemView) {
            super(itemView);

            // initialize the View objects
            marketN = (TextView) itemView.findViewById(R.id.market_name);
            marketA = (TextView) itemView.findViewById(R.id.market_address);
            header = (TextView) itemView.findViewById(R.id.header);
            doneI = (ImageView) itemView.findViewById(R.id.done);
            halfDoneI = (ImageView) itemView.findViewById(R.id.doneHalf);
        }

    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView txt_finished,txt_remaining;
        RelativeLayout headerL;
        public HeaderViewHolder(View view) {
            super(view);
            headerL = (RelativeLayout) view.findViewById(R.id.headerLayout);
            txt_remaining = (TextView) view.findViewById(R.id.remaining_count);
            txt_finished = (TextView) view.findViewById(R.id.finished_count);
        }
    }

    private void showDialog(final String market_name){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_market_ancount, null);
        final TextView tv_osa = (TextView)view.findViewById(R.id.anOSA);
        final TextView tv_npd = (TextView)view.findViewById(R.id.anNPD);
        final TextView tv_planogram = (TextView)view.findViewById(R.id.anPlanogram);
        final TextView tv_sos = (TextView)view.findViewById(R.id.anSOS);
        final TextView tv_sod = (TextView)view.findViewById(R.id.anSOD);
        final TextView tv_osaic = (TextView)view.findViewById(R.id.anOSAIC);
        final TextView tv_npdic = (TextView)view.findViewById(R.id.anNPDIC);
        final TextView tv_planogramic = (TextView)view.findViewById(R.id.anPlanogramIC);
        final TextView tv_sosic = (TextView)view.findViewById(R.id.anSOSIC);
        TextView market_info = (TextView)view.findViewById(R.id.market);
        market_info.setText(mPref.getString(Constants.MARKET_NAME,"")+" / "+mPref.getString(Constants.MARKET_CITY,"")+" / "+mPref.getString(Constants.MARKET_TYPE,""));
        AppCompatButton btn_continue = (AppCompatButton)view.findViewById(R.id.continue_btn);
        AppCompatButton btn_maps = (AppCompatButton)view.findViewById(R.id.maps_btn);
        if (mPref.getString(Constants.MARKET_LOCATION, "").equals(""))
            btn_maps.setVisibility(View.GONE);
        else
            btn_maps.setVisibility(View.VISIBLE);

        tv_osa.setText(String.valueOf(context.getSharedPreferences("MarketAnalyze", 0).getInt(market_name+"OSA",0)));
        tv_npd.setText(String.valueOf(context.getSharedPreferences("MarketAnalyze", 0).getInt(market_name+"NPD",0)));
        tv_planogram.setText(String.valueOf(context.getSharedPreferences("MarketAnalyze", 0).getInt(market_name+"Planogram",0)));
        tv_sos.setText(String.valueOf(context.getSharedPreferences("MarketAnalyze", 0).getInt(market_name+"SOS",0)));
        tv_sod.setText(String.valueOf(context.getSharedPreferences("MarketAnalyze", 0).getInt(market_name+"SOD",0)));
        tv_osaic.setText(String.valueOf(context.getSharedPreferences("MarketAnalyze", 0).getInt(market_name+"OSAIC",0)));
        tv_npdic.setText(String.valueOf(context.getSharedPreferences("MarketAnalyze", 0).getInt(market_name+"NPDIC",0)));
        tv_planogramic.setText(String.valueOf(context.getSharedPreferences("MarketAnalyze", 0).getInt(market_name+"PlanogramIC",0)));
        tv_sosic.setText(String.valueOf(context.getSharedPreferences("MarketAnalyze", 0).getInt(market_name+"SOSIC",0)));
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_osa.getText().toString().equals("0")&&tv_npd.getText().toString().equals("0")&&tv_planogram.getText().toString().equals("0")&&tv_sos.getText().toString().equals("0")&&tv_sod.getText().toString().equals("0")&&tv_osaic.getText().toString().equals("0")&&tv_npdic.getText().toString().equals("0")&&tv_planogramic.getText().toString().equals("0")&&tv_sosic.getText().toString().equals("0")) {
                    Toast.makeText(context, "Bu ay üçün siz bu marketi artıq bitirmisiniz.", Toast.LENGTH_SHORT).show();
                } else {
                    if (mPref.getString(Constants.MARKET_TYPE, "").equals("Modern Trade") && (mPref.getString(Constants.MARKET_CITY, "").equals("Bakı") || mPref.getString(Constants.MARKET_CITY, "").equals("Sumqayıt") || mPref.getString(Constants.MARKET_CITY, "").equals("Xırdalan"))) {
                        dialog.dismiss();
                        showDialogAnalyze();
                    } else {
                        Intent intent = new Intent(context, CategoriesActivity.class);
                        context.startActivity(intent);
                    }
                }
            }
        });
        btn_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlString = "http://maps.google.com/maps?q=loc:" + mPref.getString(Constants.MARKET_LOCATION, "");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.android.chrome");
                try {
                    context.startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    // Chrome browser presumably not installed so allow user to choose instead
                    intent.setPackage(null);
                    context.startActivity(intent);
                }
            }
        });
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }

    public void filterList(List<Markets> filteredList) {
        marketsList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    getMarketsListFiltered = marketsList;
                } else {
                    List<Markets> filteredList = new ArrayList<>();
                    for (Markets row : marketsList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getMarket_name().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    getMarketsListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = getMarketsListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                getMarketsListFiltered = (List<Markets>) filterResults.values;
                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }

    private void showDialogAnalyze() {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.analyze_week, null);
        final RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        final RadioButton radioOSA = view.findViewById(R.id.onlyOsa);
        final RadioButton radioALL = view.findViewById(R.id.all);
        AppCompatButton btn_continue = view.findViewById(R.id.continue_btn);
        AppCompatButton btn_close = view.findViewById(R.id.close);
        TextView market = view.findViewById(R.id.market);
        market.setText(mPref.getString(Constants.MARKET_NAME, ""));
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioALL.isChecked()) {
                    context.getSharedPreferences("MarketAnalyze", 0).edit().putInt(mPref.getString(Constants.MARKET_NAME, "")+"OSA",4).apply();
                    context.getSharedPreferences("MarketAnalyze", 0).edit().putInt(mPref.getString(Constants.MARKET_NAME, "")+"NPD",1).apply();
                    context.getSharedPreferences("MarketAnalyze", 0).edit().putInt(mPref.getString(Constants.MARKET_NAME, "")+"Planogram",1).apply();
                    context.getSharedPreferences("MarketAnalyze", 0).edit().putInt(mPref.getString(Constants.MARKET_NAME, "")+"SOS",1).apply();
                    context.getSharedPreferences("MarketAnalyze", 0).edit().putInt(mPref.getString(Constants.MARKET_NAME, "")+"SOD",1).apply();
                } else {
                    context.getSharedPreferences("MarketAnalyze", 0).edit().putInt(mPref.getString(Constants.MARKET_NAME, "")+"OSA",1).apply();
                    context.getSharedPreferences("MarketAnalyze", 0).edit().putInt(mPref.getString(Constants.MARKET_NAME, "")+"NPD",0).apply();
                    context.getSharedPreferences("MarketAnalyze", 0).edit().putInt(mPref.getString(Constants.MARKET_NAME, "")+"Planogram",0).apply();
                    context.getSharedPreferences("MarketAnalyze", 0).edit().putInt(mPref.getString(Constants.MARKET_NAME, "")+"SOS",0).apply();
                    context.getSharedPreferences("MarketAnalyze", 0).edit().putInt(mPref.getString(Constants.MARKET_NAME, "")+"SOD",0).apply();
                }
                Intent intent = new Intent(context, CategoriesActivity.class);
                context.startActivity(intent);
            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAnalyze.dismiss();
            }
        });

        builder.setView(view);
        dialogAnalyze = builder.create();
        dialogAnalyze.show();

    }
}
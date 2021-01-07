package com.agencytba.tba.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.agencytba.tba.R;
import com.agencytba.tba.SubCategoriesActivity;
import com.agencytba.tba.dbhelpers.DBHelper;
import com.agencytba.tba.modals.Tasks;
import com.agencytba.tba.server.Constants;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

import static android.view.View.VISIBLE;
import static com.agencytba.tba.TasksActivity.amountST;
import static com.agencytba.tba.TasksActivity.swipeItem;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {

    private static SharedPreferences mPref;
    private SharedPreferences Pref;

    // we define a list from the DevelopersList java class

    private List<Tasks> tasksList;
    private Context context;

    static AlertDialog dialogFinished;
    static AlertDialog dialog;

    public int count_ofRef = 0;

    private DBHelper myDB;

    public TasksAdapter(List<Tasks> tasksList, Context context) {

        // generate constructors to initialise the List and Context objects

        this.tasksList = tasksList;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // this method will be called whenever our ViewHolder is created
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tasks, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NotNull final ViewHolder holder, final int position) {

        // this method will bind the data to the ViewHolder from whence it'll be shown to other Views
        mPref = context.getSharedPreferences("Constants", 0);
        Pref = context.getSharedPreferences("Radios",0);
        SharedPreferences marketPref = context.getSharedPreferences("MarketAnalyze", 0);

        final Tasks tList = tasksList.get(position);

        holder.back.setEnabled(position != 0);
        /*Toast.makeText(context, String.valueOf(Pref.getInt("radioValueOSA" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + position, 0)), Toast.LENGTH_LONG).show();

        holder.radioAnswer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Toast.makeText(context, String.valueOf(checkedId), Toast.LENGTH_LONG).show();
            }
        });*/

        if (mPref.getString(Constants.SUBCATEGORY_NAME, "").equals("IH") || mPref.getString(Constants.SUBCATEGORY_NAME, "").equals("OH") || mPref.getString(Constants.SUBCATEGORY_NAME, "").equals("MIXED")) {
            switch (tList.getType()) {
                case "OSA":
                    holder.radioAnswer.setVisibility(View.VISIBLE);
                    holder.company_size.setVisibility(View.GONE);
                    if (Pref.getInt("radioValueOSA" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + position + "/" + count_ofRef, 0) == 0)
                        holder.radioAnswer.clearCheck();
                    else
                        holder.radioAnswer.check(Pref.getInt("radioValueOSA" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + position + "/" + count_ofRef, 0));
                    break;
                case "NPD":
                    holder.radioAnswer.setVisibility(View.VISIBLE);
                    holder.company_size.setVisibility(View.GONE);
                    if (Pref.getInt("radioValueNPD" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + position + "/" + count_ofRef, 0) == 0)
                        holder.radioAnswer.clearCheck();
                    else
                        holder.radioAnswer.check(Pref.getInt("radioValueNPD" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + position + "/" + count_ofRef, 0));
                    break;
                case "Planogram":
                    holder.radioAnswer.setVisibility(View.VISIBLE);
                    holder.company_size.setVisibility(View.GONE);
                    if (Pref.getInt("radioValuePlanogram" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + position + "/" + count_ofRef, 0) == 0)
                        holder.radioAnswer.clearCheck();
                    else
                        holder.radioAnswer.check(Pref.getInt("radioValuePlanogram" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + position + "/" + count_ofRef, 0));
                    break;
            }
        } else {
            switch (tList.getType()) {
                case "OSA":
                    holder.radioAnswer.setVisibility(View.VISIBLE);
                    holder.company_size.setVisibility(View.GONE);
                    if (Pref.getInt("radioValueOSA" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + position, 0) == 0)
                        holder.radioAnswer.clearCheck();
                    else
                        holder.radioAnswer.check(Pref.getInt("radioValueOSA" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + position, 0));
                    break;
                case "NPD":
                    holder.radioAnswer.setVisibility(View.VISIBLE);
                    holder.company_size.setVisibility(View.GONE);
                    if (Pref.getInt("radioValueNPD" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + position, 0) == 0)
                        holder.radioAnswer.clearCheck();
                    else
                        holder.radioAnswer.check(Pref.getInt("radioValueNPD" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + position, 0));
                    break;
                case "Planogram":
                    holder.radioAnswer.setVisibility(View.VISIBLE);
                    holder.company_size.setVisibility(View.GONE);
                    if (Pref.getInt("radioValuePlanogram" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + position, 0) == 0)
                        holder.radioAnswer.clearCheck();
                    else
                        holder.radioAnswer.check(Pref.getInt("radioValuePlanogram" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + position, 0));
                    break;
                case "SOS":
                    holder.company_size.setVisibility(View.VISIBLE);
                    holder.radioAnswer.setVisibility(View.GONE);
                    holder.company_size.setText(context.getSharedPreferences("shareCounts", 0).getString("etValueSOS"+mPref.getString(Constants.MARKET_NAME, "")+"/"+mPref.getString(Constants.CATEGORY_NAME,"")+mPref.getString(Constants.SUBCATEGORY_NAME,"") + position, ""));
                    break;
                case "SOD":
                    holder.company_size.setVisibility(View.VISIBLE);
                    holder.radioAnswer.setVisibility(View.GONE);
                    holder.company_size.setText(context.getSharedPreferences("shareCounts", 0).getString("etValueSOD"+mPref.getString(Constants.MARKET_NAME, "")+"/"+mPref.getString(Constants.CATEGORY_NAME,"")+mPref.getString(Constants.SUBCATEGORY_NAME,"") + position, ""));
                    break;
            }
        }

        File mFileTemp = new File(context.getFilesDir() + File.separator
                + "tba_images" + File.separator
                +"company_images",mPref.getString(Constants.COMPANY_LOGO,""));

        Picasso.get().load(mFileTemp).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).fit()
                .into(holder.company_logo);

        holder.company.setText(tList.getCompany());

        holder.type.setText(tList.getType());
        if (tList.getBrand_type().contains("#")) {
            holder.category.setText(tList.getBrand_type().split(" # ")[0]);
            switch (mPref.getString(Constants.SUBCATEGORY_NAME, "")) {
                case "IH":
                    holder.subcategory.setText("In-home");
                    break;
                case "OH":
                    holder.subcategory.setText("Impulse");
                    break;
                case "MIXED":
                    holder.subcategory.setText("Qarışıq");
                    break;
                default:
                    holder.subcategory.setText(tList.getBrand_type().split(" # ")[1]);
                    break;
            }
        } else {
            holder.category.setText(tList.getBrand_type());
            holder.subcategory.setVisibility(View.GONE);
        }

        if (tList.getProduct_name().contains("#"))
            holder.question.setText(tList.getProduct_name().split(" # ")[0]);
        else
            holder.question.setText(tList.getProduct_name());

        File mFileTempQ = new File(context.getFilesDir() + File.separator
                + "tba_images" + File.separator
                +"product_images", tList.getImage());
        Picasso.get().load(mFileTempQ).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                .into(holder.product_img);

        holder.next.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                switch (tList.getType()) {
                    case "OSA":
                        if (holder.radioAnswer.getCheckedRadioButtonId() == R.id.radioYes || holder.radioAnswer.getCheckedRadioButtonId() == R.id.radioNo) {
                            int status = 0;
                            if (holder.radioAnswer.getCheckedRadioButtonId() == R.id.radioYes)
                                status = 1;
                            if (mPref.getString(Constants.SUBCATEGORY_NAME, "").equals("IH") || mPref.getString(Constants.SUBCATEGORY_NAME, "").equals("OH") || mPref.getString(Constants.SUBCATEGORY_NAME, "").equals("MIXED")) {
                                if (!Pref.getBoolean("osaUploaded" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + tList.getProduct_name() + "/" + count_ofRef, false)) {
                                    holder.progressBar.setVisibility(VISIBLE);
                                    holder.radioAnswer.setEnabled(false);
                                    holder.next.setEnabled(false);
                                    holder.back.setEnabled(false);
                                    boolean finished;
                                    if (position + 1 < getItemCount()) {
                                        finished = !tasksList.get(position + 1).getType().equals("OSA");
                                    } else {
                                        finished = true;
                                    }
                                    insertOSA(holder.progressBar, holder.next, holder.back, holder.radioAnswer, holder.company_size, position, holder.radioAnswer.getCheckedRadioButtonId(), tasksList.get(position).getBrand_name(), tasksList.get(position).getBrand_type(), tasksList.get(position).getProduct_name(), String.valueOf(status), finished);
                                } else {
                                    if (Pref.getInt("radioValueOSA" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + position + "/" + count_ofRef, 0) == holder.radioAnswer.getCheckedRadioButtonId()) {
                                        if (position + 1 != getItemCount())
                                            swipeItem(position + 1);
                                        else {
                                            if (count_ofRef < Integer.parseInt(amountST.trim())) {
                                                Toast.makeText(context, "Siz " + (count_ofRef + 1) + "-ci dondurma dolabnının analizinə başladınız.", Toast.LENGTH_LONG).show();
                                                swipeItem(0);
                                                count_ofRef++;
                                            } else
                                                showDialogFinished(false, mPref.getString(Constants.MARKET_NAME, ""));
                                        }
                                    } else updateData(holder.progressBar, holder.next, holder.back, holder.radioAnswer, holder.company_size, position, holder.radioAnswer.getCheckedRadioButtonId(), tasksList.get(position).getBrand_name(), tasksList.get(position).getBrand_type(), tasksList.get(position).getProduct_name(), String.valueOf(status), String.valueOf(0), String.valueOf(0), String.valueOf(0), "OSA");
                                }
                            } else {
                                if (!Pref.getBoolean("osaUploaded" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + holder.question.getText().toString(), false)) {
                                    holder.progressBar.setVisibility(VISIBLE);
                                    holder.radioAnswer.setEnabled(false);
                                    holder.next.setEnabled(false);
                                    holder.back.setEnabled(false);
                                    insertOSA(holder.progressBar, holder.next, holder.back, holder.radioAnswer, holder.company_size, position, holder.radioAnswer.getCheckedRadioButtonId(), tasksList.get(position).getBrand_name(), tasksList.get(position).getBrand_type(), tasksList.get(position).getProduct_name(), String.valueOf(status), false);
                                } else {
                                    if (Pref.getInt("radioValueOSA" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + position, 0) == holder.radioAnswer.getCheckedRadioButtonId()) {
                                        if (position + 1 != getItemCount())
                                            swipeItem(position + 1);
                                        else
                                            showDialogFinished(false, mPref.getString(Constants.MARKET_NAME, ""));
                                    } else updateData(holder.progressBar, holder.next, holder.back, holder.radioAnswer, holder.company_size, position, holder.radioAnswer.getCheckedRadioButtonId(), tasksList.get(position).getBrand_name(), tasksList.get(position).getBrand_type(), tasksList.get(position).getProduct_name(), String.valueOf(status), String.valueOf(0), String.valueOf(0), String.valueOf(0), "OSA");
                                }
                            }
                        } else
                            Toast.makeText(context, "Öncə sualı cavablandırın.", Toast.LENGTH_SHORT).show();
                        break;
                    case "NPD":
                        if (holder.radioAnswer.getCheckedRadioButtonId() == R.id.radioYes || holder.radioAnswer.getCheckedRadioButtonId() == R.id.radioNo) {
                            int status = 0;
                            if (holder.radioAnswer.getCheckedRadioButtonId() == R.id.radioYes)
                                status = 1;
                            if (mPref.getString(Constants.SUBCATEGORY_NAME, "").equals("IH") || mPref.getString(Constants.SUBCATEGORY_NAME, "").equals("OH") || mPref.getString(Constants.SUBCATEGORY_NAME, "").equals("MIXED")) {
                                if (!Pref.getBoolean("npdUploaded" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + holder.question.getText().toString() + "/" + count_ofRef, false)) {
                                    holder.progressBar.setVisibility(VISIBLE);
                                    holder.radioAnswer.setEnabled(false);
                                    holder.next.setEnabled(false);
                                    holder.back.setEnabled(false);
                                    insertNPD(holder.progressBar, holder.next, holder.back, holder.radioAnswer, holder.company_size, position, holder.radioAnswer.getCheckedRadioButtonId(), tList.getBrand_name(), tList.getBrand_type(), tList.getProduct_name(), String.valueOf(status));
                                } else {
                                    if (Pref.getInt("radioValueNPD" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + position + "/" + count_ofRef, 0) == holder.radioAnswer.getCheckedRadioButtonId()) {
                                        if (position + 1 != getItemCount())
                                            swipeItem(position + 1);
                                        else {
                                            if (count_ofRef < Integer.parseInt(amountST.trim())) {
                                                Toast.makeText(context, "Siz " + (count_ofRef + 1) + "-ci dondurma dolabnının analizinə başladınız.", Toast.LENGTH_LONG).show();
                                                swipeItem(0);
                                                count_ofRef++;
                                            } else
                                                showDialogFinished(false, mPref.getString(Constants.MARKET_NAME, ""));
                                        }
                                    } else updateData(holder.progressBar, holder.next, holder.back, holder.radioAnswer, holder.company_size, position, holder.radioAnswer.getCheckedRadioButtonId(), tasksList.get(position).getBrand_name(), tasksList.get(position).getBrand_type(), tasksList.get(position).getProduct_name(), String.valueOf(status), String.valueOf(0), String.valueOf(0), String.valueOf(0), "NPD");
                                }
                            } else {
                                if (!Pref.getBoolean("npdUploaded" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + holder.question.getText().toString(), false)) {
                                    holder.progressBar.setVisibility(VISIBLE);
                                    holder.radioAnswer.setEnabled(false);
                                    holder.next.setEnabled(false);
                                    holder.back.setEnabled(false);
                                    insertNPD(holder.progressBar, holder.next, holder.back, holder.radioAnswer, holder.company_size, position, holder.radioAnswer.getCheckedRadioButtonId(), tList.getBrand_name(), tList.getBrand_type(), tList.getProduct_name(), String.valueOf(status));
                                } else {
                                    if (Pref.getInt("radioValueNPD" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + position, 0) == holder.radioAnswer.getCheckedRadioButtonId()) {
                                        if (position + 1 != getItemCount())
                                            swipeItem(position + 1);
                                        else
                                            showDialogFinished(false, mPref.getString(Constants.MARKET_NAME, ""));
                                    } else updateData(holder.progressBar, holder.next, holder.back, holder.radioAnswer, holder.company_size, position, holder.radioAnswer.getCheckedRadioButtonId(), tasksList.get(position).getBrand_name(), tasksList.get(position).getBrand_type(), tasksList.get(position).getProduct_name(), String.valueOf(status), String.valueOf(0), String.valueOf(0), String.valueOf(0), "NPD");
                                }
                            }
                        } else
                            Toast.makeText(context, "Öncə sualı cavablandırın.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Planogram":
                        if (holder.radioAnswer.getCheckedRadioButtonId() == R.id.radioYes || holder.radioAnswer.getCheckedRadioButtonId() == R.id.radioNo) {
                            int status = 0;
                            if (holder.radioAnswer.getCheckedRadioButtonId() == R.id.radioYes)
                                status = 1;
                            if (mPref.getString(Constants.SUBCATEGORY_NAME, "").equals("IH") || mPref.getString(Constants.SUBCATEGORY_NAME, "").equals("OH") || mPref.getString(Constants.SUBCATEGORY_NAME, "").equals("MIXED")) {
                                if (!Pref.getBoolean("planogramUploaded" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + holder.question.getText().toString() + "/" + count_ofRef, false)) {
                                    holder.progressBar.setVisibility(VISIBLE);
                                    holder.radioAnswer.setEnabled(false);
                                    holder.next.setEnabled(false);
                                    holder.back.setEnabled(false);
                                    insertPlanogram(holder.progressBar, holder.next, holder.back, holder.radioAnswer, holder.company_size, position, holder.radioAnswer.getCheckedRadioButtonId(), tList.getBrand_type(), tList.getProduct_name(), String.valueOf(status));
                                } else {
                                    if (Pref.getInt("radioValuePlanogram" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + position + "/" + count_ofRef, 0) == holder.radioAnswer.getCheckedRadioButtonId()) {
                                        if (position + 1 != getItemCount())
                                            swipeItem(position + 1);
                                        else {
                                            if (count_ofRef < Integer.parseInt(amountST.trim())) {
                                                Toast.makeText(context, "Siz " + (count_ofRef + 1) + "-ci dondurma dolabnının analizinə başladınız.", Toast.LENGTH_LONG).show();
                                                swipeItem(0);
                                                count_ofRef++;
                                            } else
                                                showDialogFinished(false, mPref.getString(Constants.MARKET_NAME, ""));
                                        }
                                    } else updateData(holder.progressBar, holder.next, holder.back, holder.radioAnswer, holder.company_size, position, holder.radioAnswer.getCheckedRadioButtonId(), tasksList.get(position).getBrand_name(), tasksList.get(position).getBrand_type(), tasksList.get(position).getProduct_name(), String.valueOf(status), String.valueOf(0), String.valueOf(0), String.valueOf(0), "Planogram");
                                }
                            } else {
                                if (!Pref.getBoolean("planogramUploaded" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + holder.question.getText().toString(), false)) {
                                    holder.progressBar.setVisibility(VISIBLE);
                                    holder.radioAnswer.setEnabled(false);
                                    holder.next.setEnabled(false);
                                    holder.back.setEnabled(false);
                                    insertPlanogram(holder.progressBar, holder.next, holder.back, holder.radioAnswer, holder.company_size, position, holder.radioAnswer.getCheckedRadioButtonId(), tList.getBrand_type(), tList.getProduct_name(), String.valueOf(status));
                                } else {
                                    if (Pref.getInt("radioValuePlanogram" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + position, 0) == holder.radioAnswer.getCheckedRadioButtonId()) {
                                        if (position + 1 != getItemCount())
                                            swipeItem(position + 1);
                                        else
                                            showDialogFinished(false, mPref.getString(Constants.MARKET_NAME, ""));
                                    } else updateData(holder.progressBar, holder.next, holder.back, holder.radioAnswer, holder.company_size, position, holder.radioAnswer.getCheckedRadioButtonId(), tasksList.get(position).getBrand_name(), tasksList.get(position).getBrand_type(), tasksList.get(position).getProduct_name(), String.valueOf(status), String.valueOf(0), String.valueOf(0), String.valueOf(0), "Planogram");
                                }
                            }
                        } else
                            Toast.makeText(context, "Öncə sualı cavablandırın.", Toast.LENGTH_SHORT).show();
                        break;
                    case "SOS":
                        if (!holder.company_size.getText().toString().trim().equals("")) {
                            if (Integer.parseInt(holder.company_size.getText().toString().trim()) <= Integer.parseInt(amountST.trim())) {
                                if (!Pref.getBoolean("sosUploaded" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + tList.getBrand_name(), false)) {
                                    holder.progressBar.setVisibility(VISIBLE);
                                    holder.company_size.setEnabled(false);
                                    holder.next.setEnabled(false);
                                    holder.back.setEnabled(false);
                                    insertSOS(holder.progressBar, holder.next, holder.back, holder.radioAnswer, holder.company_size, position, tList.getBrand_name(), tList.getBrand_type(), amountST.trim(), holder.company_size.getText().toString().trim(), String.valueOf((Double.parseDouble(holder.company_size.getText().toString().trim()) / Double.parseDouble(amountST.trim())) * 100));
                                } else {
                                    if (context.getSharedPreferences("shareCounts", 0).getString("etValueSOS"+mPref.getString(Constants.MARKET_NAME, "")+"/"+mPref.getString(Constants.CATEGORY_NAME,"")+mPref.getString(Constants.SUBCATEGORY_NAME,"") + position, "").equals(holder.company_size.getText().toString().trim())) {
                                        if (position + 1 != getItemCount())
                                            swipeItem(position + 1);
                                        else
                                            showDialogFinished(false, mPref.getString(Constants.MARKET_NAME, ""));
                                    } else updateData(holder.progressBar, holder.next, holder.back, holder.radioAnswer, holder.company_size, position, holder.radioAnswer.getCheckedRadioButtonId(), tasksList.get(position).getBrand_name(), tasksList.get(position).getBrand_type(), tasksList.get(position).getProduct_name(), String.valueOf(0), amountST.trim(), holder.company_size.getText().toString().trim(), String.valueOf((Double.parseDouble(holder.company_size.getText().toString().trim()) / Double.parseDouble(amountST.trim())) * 100), "SOS");
                                }
                            } else
                                Toast.makeText(context, "Şirkət məhsullarının sayı ümumi saydan çox ola bilməz.", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(context, "Öncə sualı cavablandırın.", Toast.LENGTH_SHORT).show();
                        break;
                    case "SOD":
                        if (!holder.company_size.getText().toString().trim().equals("")) {
                            if (Integer.parseInt(holder.company_size.getText().toString().trim()) <= Integer.parseInt(amountST.trim())) {
                                if (!Pref.getBoolean("sodUploaded" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + tList.getBrand_name(), false)) {
                                    holder.progressBar.setVisibility(VISIBLE);
                                    holder.company_size.setEnabled(false);
                                    holder.next.setEnabled(false);
                                    holder.back.setEnabled(false);
                                    insertSOD(holder.progressBar, holder.next, holder.back, holder.radioAnswer, holder.company_size, position, tList.getBrand_name(), tList.getBrand_type(), amountST.trim(), holder.company_size.getText().toString().trim(), String.valueOf((Double.parseDouble(holder.company_size.getText().toString().trim()) / Double.parseDouble(amountST.trim())) * 100));
                                } else {
                                    if (context.getSharedPreferences("shareCounts", 0).getString("etValueSOD"+mPref.getString(Constants.MARKET_NAME, "")+"/"+mPref.getString(Constants.CATEGORY_NAME,"")+mPref.getString(Constants.SUBCATEGORY_NAME,"") + position, "").equals(holder.company_size.getText().toString().trim())) {
                                        if (position + 1 != getItemCount())
                                            swipeItem(position + 1);
                                        else
                                            showDialogFinished(false, mPref.getString(Constants.MARKET_NAME, ""));
                                    } else updateData(holder.progressBar, holder.next, holder.back, holder.radioAnswer, holder.company_size, position, holder.radioAnswer.getCheckedRadioButtonId(), tasksList.get(position).getBrand_name(), tasksList.get(position).getBrand_type(), tasksList.get(position).getProduct_name(), String.valueOf(0), amountST.trim(), holder.company_size.getText().toString().trim(), String.valueOf((Double.parseDouble(holder.company_size.getText().toString().trim()) / Double.parseDouble(amountST.trim())) * 100), "SOD");
                                }
                            } else
                                Toast.makeText(context, "Şirkət məhsullarının sayı ümumi saydan çox ola bilməz.", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(context, "Öncə sualı cavablandırın.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        holder.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeItem(position - 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // define the View objects

        public TextView company, type, category, subcategory, question;
        public ImageView company_logo, product_img;
        public RadioGroup radioAnswer;
        public EditText company_size;
        public AppCompatButton back, next;
        public ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);

            // initialize the View objects
            company = (TextView)itemView.findViewById(R.id.company);
            type = (TextView)itemView.findViewById(R.id.type);
            category = (TextView)itemView.findViewById(R.id.category);
            subcategory = (TextView)itemView.findViewById(R.id.subcategory);
            question = (TextView)itemView.findViewById(R.id.question);
            company_logo = (ImageView)itemView.findViewById(R.id.company_logo);
            product_img = (ImageView)itemView.findViewById(R.id.product_img);
            radioAnswer = (RadioGroup)itemView.findViewById(R.id.radioAnswer);
            company_size = (EditText)itemView.findViewById(R.id.company_size);
            back = (AppCompatButton)itemView.findViewById(R.id.back);
            next = (AppCompatButton)itemView.findViewById(R.id.next);
            progressBar = (ProgressBar)itemView.findViewById(R.id.progressBar);

        }
    }

    private boolean haveNetworkConnection() {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {


            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true;
                    }  else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)){
                        return true;
                    }
                }
            } else {
                try {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                        Log.i("update_status", "Network is available : true");
                        return true;
                    }
                } catch (Exception e) {
                    Log.i("update_status", "" + e.getMessage());
                }
            }
        }
        Log.i("update_status","Network is available : FALSE ");
        return false;
    }

    public void showDialogFinished(boolean marketEnd, String market_name) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        builder.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_finished, null);
        TextView tv_message = (TextView)view.findViewById(R.id.tv_message);
        TextView redirect_to = (TextView)view.findViewById(R.id.redirect_to);
        final SharedPreferences Pref = context.getSharedPreferences("Radios",0);
        Pref.edit().putBoolean(mPref.getString(Constants.MARKET_NAME, "")+"/"+mPref.getString(Constants.CATEGORY_NAME, "")+"/"+mPref.getString(Constants.SUBCATEGORY_NAME, ""),true).apply();

        if (!marketEnd)
            redirect_to.setText(context.getResources().getString(R.string.redirect_to));
        tv_message.setText(market_name);
        Button dismiss = (Button)view.findViewById(R.id.dismiss);
        Button dismissToTasks = (Button)view.findViewById(R.id.dismissToTasks);
        if (haveNetworkConnection()) {
            SharedPreferences.Editor ed = Pref.edit();
            ed.putBoolean(mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + "/" + mPref.getString(Constants.SUBCATEGORY_NAME, ""), true);
            ed.apply();
        }
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogFinished.dismiss();
                Intent intent = new Intent(context, SubCategoriesActivity.class);
                context.startActivity(intent);
            }
        });
        dismissToTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogFinished.dismiss();
                swipeItem(0);
            }
        });
        builder.setView(view);
        dialogFinished = builder.create();
        dialogFinished.show();
    }

    public void showDialog(String previous_type, final String current_type, final int adapterPosition){

        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        builder.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_new_type, null);
        TextView tv_message = (TextView)view.findViewById(R.id.tv_message);
        TextView new_type = (TextView)view.findViewById(R.id.new_type);
        final TextView counter = (TextView)view.findViewById(R.id.countDown);
        counter.setVisibility(View.GONE);
        tv_message.setText(previous_type);
        new_type.setText(current_type);
        Button dismiss = (Button)view.findViewById(R.id.dismiss);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeItem(adapterPosition + 1);
                dialog.dismiss();
            }
        });
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void insertOSA(@NotNull final ProgressBar progressBar, @NotNull final AppCompatButton next, @NotNull final AppCompatButton back, @NotNull final RadioGroup radioAnswer, @NotNull final EditText company_size, final int adapterPosition, final int selectedId, String brand_name, String product_type, String sku_name, String status, boolean finished) {
        myDB = new DBHelper(context);
        String market_name = mPref.getString(Constants.MARKET_NAME, "");
        String company_name = mPref.getString(Constants.COMPANY, "");
        if (myDB.insertOSA(brand_name, product_type, sku_name, market_name, company_name, status, mPref.getString(Constants.MARKET_MANAGERHPCFK, ""),  mPref.getString(Constants.MARKET_MANAGERIC, ""),  mPref.getString(Constants.MARKET_SUPERVISORHPCFK, ""),  mPref.getString(Constants.MARKET_SUPERVISORHPCFK2, ""),  mPref.getString(Constants.MARKET_SUPERVISORIC, ""),  mPref.getString(Constants.MARKET_EXPEDITORHPCFK, ""),  mPref.getString(Constants.MARKET_EXPEDITORHPCFK2, ""),  mPref.getString(Constants.MARKET_EXPEDITORIC, ""),  mPref.getString(Constants.MARKET_FIELD_SUPERVISOR, ""), finished)) {
            if (!tasksList.get(adapterPosition).getCategories_to_check().equals("IC")) {
                context.getSharedPreferences("Radios", 0).edit().putInt("radioValueOSA" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + adapterPosition, selectedId).apply();
                context.getSharedPreferences("Radios", 0).edit().putBoolean("osaUploaded" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + sku_name, true).apply();

                if (adapterPosition + 1 != tasksList.size())
                    if (!tasksList.get(adapterPosition).getType().equals(tasksList.get(adapterPosition + 1).getType()))
                        showDialog(tasksList.get(adapterPosition).getType(), tasksList.get(adapterPosition + 1).getType(), adapterPosition);
                    else
                        swipeItem(adapterPosition + 1);
                else
                    showDialogFinished(false, mPref.getString(Constants.MARKET_NAME, ""));

            } else {
                context.getSharedPreferences("Radios", 0).edit().putInt("radioValueOSA" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + adapterPosition + "/" + count_ofRef, selectedId).apply();
                context.getSharedPreferences("Radios", 0).edit().putBoolean("osaUploaded" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + sku_name + "/" + count_ofRef, true).apply();
                if (adapterPosition + 1 != tasksList.size())
                    if (!tasksList.get(adapterPosition).getType().equals(tasksList.get(adapterPosition + 1).getType()))
                        showDialog(tasksList.get(adapterPosition).getType(), tasksList.get(adapterPosition + 1).getType(), adapterPosition);
                    else
                        swipeItem(adapterPosition + 1);
                else {
                    count_ofRef++;
                    if (count_ofRef <= Integer.parseInt(amountST.trim())) {
                        swipeItem(0);
                        Toast.makeText(context, "Siz " + (count_ofRef + 1) + "-ci dondurma dolabnının analizinə başladınız.", Toast.LENGTH_LONG).show();
                    } else
                        showDialogFinished(false, mPref.getString(Constants.MARKET_NAME, ""));
                }
            }
        } else {
            Toast.makeText(context, "Error here", Toast.LENGTH_LONG).show();
        }
        progressBar.setVisibility(View.GONE);
        next.setEnabled(true);
        back.setEnabled(true);
        company_size.setEnabled(true);
        radioAnswer.setEnabled(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void insertNPD(@NotNull final ProgressBar progressBar, @NotNull final AppCompatButton next, @NotNull final AppCompatButton back, @NotNull final RadioGroup radioAnswer, @NotNull final EditText company_size, final int adapterPosition, final int selectedId, String brand_name, String product_type, String sku_name, String status) {
        myDB = new DBHelper(context);
        String market_name = mPref.getString(Constants.MARKET_NAME, "");
        String company_name = mPref.getString(Constants.COMPANY, "");
        if (myDB.insertNPD(brand_name, product_type, sku_name, mPref.getString(Constants.MARKET_NAME, ""), mPref.getString(Constants.COMPANY, ""), status, mPref.getString(Constants.MARKET_MANAGERHPCFK, ""),  mPref.getString(Constants.MARKET_MANAGERIC, ""),  mPref.getString(Constants.MARKET_SUPERVISORHPCFK, ""),  mPref.getString(Constants.MARKET_SUPERVISORHPCFK2, ""),  mPref.getString(Constants.MARKET_SUPERVISORIC, ""),  mPref.getString(Constants.MARKET_EXPEDITORHPCFK, ""),  mPref.getString(Constants.MARKET_EXPEDITORHPCFK2, ""),  mPref.getString(Constants.MARKET_EXPEDITORIC, ""),  mPref.getString(Constants.MARKET_FIELD_SUPERVISOR, ""))) {
            if (!tasksList.get(adapterPosition).getCategories_to_check().equals("IC")) {
                context.getSharedPreferences("Radios", 0).edit().putInt("radioValueNPD" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + adapterPosition, selectedId).apply();
                context.getSharedPreferences("Radios", 0).edit().putBoolean("npdUploaded" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + sku_name, true).apply();

                if (adapterPosition + 1 != tasksList.size())
                    if (!tasksList.get(adapterPosition).getType().equals(tasksList.get(adapterPosition + 1).getType()))
                        showDialog(tasksList.get(adapterPosition).getType(), tasksList.get(adapterPosition + 1).getType(), adapterPosition);
                    else
                        swipeItem(adapterPosition + 1);
                else
                    showDialogFinished(false, mPref.getString(Constants.MARKET_NAME, ""));

            } else {
                context.getSharedPreferences("Radios", 0).edit().putInt("radioValueNPD" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + adapterPosition + "/" + count_ofRef, selectedId).apply();
                context.getSharedPreferences("Radios", 0).edit().putBoolean("npdUploaded" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + sku_name + "/" + count_ofRef, true).apply();
                if (adapterPosition + 1 != tasksList.size())
                    if (!tasksList.get(adapterPosition).getType().equals(tasksList.get(adapterPosition + 1).getType()))
                        showDialog(tasksList.get(adapterPosition).getType(), tasksList.get(adapterPosition + 1).getType(), adapterPosition);
                    else
                        swipeItem(adapterPosition + 1);
                else {
                    count_ofRef++;
                    if (count_ofRef <= Integer.parseInt(amountST.trim())) {
                        swipeItem(0);
                        Toast.makeText(context, "Siz " + (count_ofRef + 1) + "-ci dondurma dolabnının analizinə başladınız.", Toast.LENGTH_LONG).show();
                    } else
                        showDialogFinished(false, mPref.getString(Constants.MARKET_NAME, ""));
                }
            }
        }
        progressBar.setVisibility(View.GONE);
        next.setEnabled(true);
        back.setEnabled(true);
        company_size.setEnabled(true);
        radioAnswer.setEnabled(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void insertPlanogram(@NotNull final ProgressBar progressBar, @NotNull final AppCompatButton next, @NotNull final AppCompatButton back, @NotNull final RadioGroup radioAnswer, @NotNull final EditText company_size, final int adapterPosition, final int selectedId, String product_type, String sku_name, String status) {
        myDB = new DBHelper(context);
        String market_name = mPref.getString(Constants.MARKET_NAME, "");
        String company_name = mPref.getString(Constants.COMPANY, "");
        if (myDB.insertPlanogram(product_type, sku_name, market_name, company_name, status)) {
            if (!tasksList.get(adapterPosition).getCategories_to_check().equals("IC")) {
                context.getSharedPreferences("Radios", 0).edit().putInt("radioValuePlanogram" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + adapterPosition, selectedId).apply();
                context.getSharedPreferences("Radios", 0).edit().putBoolean("planogramUploaded" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + sku_name, true).apply();

                if (adapterPosition + 1 != tasksList.size())
                    if (!tasksList.get(adapterPosition).getType().equals(tasksList.get(adapterPosition + 1).getType()))
                        showDialog(tasksList.get(adapterPosition).getType(), tasksList.get(adapterPosition + 1).getType(), adapterPosition);
                    else
                        swipeItem(adapterPosition + 1);
                else
                    showDialogFinished(false, mPref.getString(Constants.MARKET_NAME, ""));

            } else {
                context.getSharedPreferences("Radios", 0).edit().putInt("radioValuePlanogram" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + adapterPosition + "/" + count_ofRef, selectedId).apply();
                context.getSharedPreferences("Radios", 0).edit().putBoolean("planogramUploaded" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + sku_name + "/" + count_ofRef, true).apply();
                if (adapterPosition + 1 != tasksList.size())
                    if (!tasksList.get(adapterPosition).getType().equals(tasksList.get(adapterPosition + 1).getType()))
                        showDialog(tasksList.get(adapterPosition).getType(), tasksList.get(adapterPosition + 1).getType(), adapterPosition);
                    else
                        swipeItem(adapterPosition + 1);
                else {
                    count_ofRef++;
                    if (count_ofRef <= Integer.parseInt(amountST.trim())) {
                        swipeItem(0);
                        Toast.makeText(context, "Siz " + (count_ofRef + 1) + "-ci dondurma dolabnının analizinə başladınız.", Toast.LENGTH_LONG).show();
                    } else
                        showDialogFinished(false, mPref.getString(Constants.MARKET_NAME, ""));
                }
            }
        }
        progressBar.setVisibility(View.GONE);
        next.setEnabled(true);
        back.setEnabled(true);
        company_size.setEnabled(true);
        radioAnswer.setEnabled(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void insertSOS(@NotNull final ProgressBar progressBar, @NotNull final AppCompatButton next, @NotNull final AppCompatButton back, @NotNull final RadioGroup radioAnswer, @NotNull final EditText companySize, final int adapterPosition, final String brand_name, String product_type, String total_size, final String company_size, String percentage) {
        myDB = new DBHelper(context);
        String market_name = mPref.getString(Constants.MARKET_NAME, "");
        String company_name = mPref.getString(Constants.COMPANY, "");
        if (myDB.insertSOS(brand_name, product_type, market_name, company_name, total_size, company_size, percentage)) {
            context.getSharedPreferences("shareCounts", 0).edit().putString("etValueSOS"+mPref.getString(Constants.MARKET_NAME, "")+"/"+mPref.getString(Constants.CATEGORY_NAME,"")+mPref.getString(Constants.SUBCATEGORY_NAME,"") + adapterPosition, company_size).apply();
            SharedPreferences.Editor editor = context.getSharedPreferences("Radios", 0).edit();
            editor.putBoolean("sosUploaded" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + brand_name, true);
            editor.apply();
            if (adapterPosition+1 != getItemCount())
                if (!tasksList.get(adapterPosition).getType().equals(tasksList.get(adapterPosition+1).getType()))
                    showDialog(tasksList.get(adapterPosition).getType(), tasksList.get(adapterPosition+1).getType(), adapterPosition);
                else
                    swipeItem(adapterPosition + 1);
            else
                showDialogFinished(false, mPref.getString(Constants.MARKET_NAME,""));
        }
        progressBar.setVisibility(View.GONE);
        next.setEnabled(true);
        back.setEnabled(true);
        companySize.setEnabled(true);
        radioAnswer.setEnabled(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void insertSOD(@NotNull final ProgressBar progressBar, @NotNull final AppCompatButton next, @NotNull final AppCompatButton back, @NotNull final RadioGroup radioAnswer, @NotNull final EditText companySize, final int adapterPosition, final String brand_name, String product_type, String total_size, final String company_size, String percentage) {
        myDB = new DBHelper(context);
        String market_name = mPref.getString(Constants.MARKET_NAME, "");
        String company_name = mPref.getString(Constants.COMPANY, "");
        if (myDB.insertSOD(brand_name, product_type, market_name, company_name, total_size, company_size, percentage)) {
            context.getSharedPreferences("shareCounts", 0).edit().putString("etValueSOD"+mPref.getString(Constants.MARKET_NAME, "")+"/"+mPref.getString(Constants.CATEGORY_NAME,"")+mPref.getString(Constants.SUBCATEGORY_NAME,"") + adapterPosition, company_size).apply();
            SharedPreferences.Editor editor = context.getSharedPreferences("Radios", 0).edit();
            editor.putBoolean("sodUploaded" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + brand_name, true);
            editor.apply();
            if (adapterPosition+1 != getItemCount())
                if (!tasksList.get(adapterPosition).getType().equals(tasksList.get(adapterPosition+1).getType()))
                    showDialog(tasksList.get(adapterPosition).getType(), tasksList.get(adapterPosition+1).getType(), adapterPosition);
                else
                    swipeItem(adapterPosition + 1);
            else
                showDialogFinished(false, mPref.getString(Constants.MARKET_NAME,""));
        }
        progressBar.setVisibility(View.GONE);
        next.setEnabled(true);
        back.setEnabled(true);
        companySize.setEnabled(true);
        radioAnswer.setEnabled(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateData(@NotNull final ProgressBar progressBar, @NotNull final AppCompatButton next, @NotNull final AppCompatButton back, @NotNull final RadioGroup radioAnswer, @NotNull final EditText companySize, final int adapterPosition, final int selectedId, String brand_name, String product_type, String sku_name, String status, String total_size, String company_size, String percentage, @NotNull String type) {
        myDB = new DBHelper(context);
        String market_name = mPref.getString(Constants.MARKET_NAME, "");
        String company_name = mPref.getString(Constants.COMPANY, "");
        switch (type) {
            case "OSA":
                if (myDB.updateOSA(brand_name, product_type, sku_name, market_name, company_name, status, type)) {
                    if (!tasksList.get(adapterPosition).getCategories_to_check().equals("IC")) {
                        context.getSharedPreferences("Radios", 0).edit().putInt("radioValueOSA" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + adapterPosition, selectedId).apply();
                        context.getSharedPreferences("Radios", 0).edit().putBoolean("osaUploaded" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + sku_name, true).apply();

                        if (adapterPosition + 1 != tasksList.size())
                            if (!tasksList.get(adapterPosition).getType().equals(tasksList.get(adapterPosition + 1).getType()))
                                showDialog(tasksList.get(adapterPosition).getType(), tasksList.get(adapterPosition + 1).getType(), adapterPosition);
                            else
                                swipeItem(adapterPosition + 1);
                        else
                            showDialogFinished(false, mPref.getString(Constants.MARKET_NAME, ""));

                    } else {
                        context.getSharedPreferences("Radios", 0).edit().putInt("radioValueOSA" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + adapterPosition + "/" + count_ofRef, selectedId).apply();
                        context.getSharedPreferences("Radios", 0).edit().putBoolean("osaUploaded" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + sku_name + "/" + count_ofRef, true).apply();
                        if (adapterPosition + 1 != tasksList.size())
                            if (!tasksList.get(adapterPosition).getType().equals(tasksList.get(adapterPosition + 1).getType()))
                                showDialog(tasksList.get(adapterPosition).getType(), tasksList.get(adapterPosition + 1).getType(), adapterPosition);
                            else
                                swipeItem(adapterPosition + 1);
                        else {
                            count_ofRef++;
                            if (count_ofRef <= Integer.parseInt(amountST.trim())) {
                                swipeItem(0);
                                Toast.makeText(context, "Siz " + (count_ofRef + 1) + "-ci dondurma dolabnının analizinə başladınız.", Toast.LENGTH_LONG).show();
                            } else
                                showDialogFinished(false, mPref.getString(Constants.MARKET_NAME, ""));
                        }
                    }
                }
                break;
            case "NPD":
                if (myDB.updateNPD(brand_name, product_type, sku_name, market_name, company_name, status, type)) {
                    if (!tasksList.get(adapterPosition).getCategories_to_check().equals("IC")) {
                        context.getSharedPreferences("Radios", 0).edit().putInt("radioValueNPD" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + adapterPosition, selectedId).apply();
                        context.getSharedPreferences("Radios", 0).edit().putBoolean("npdUploaded" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + sku_name, true).apply();

                        if (adapterPosition + 1 != tasksList.size())
                            if (!tasksList.get(adapterPosition).getType().equals(tasksList.get(adapterPosition + 1).getType()))
                                showDialog(tasksList.get(adapterPosition).getType(), tasksList.get(adapterPosition + 1).getType(), adapterPosition);
                            else
                                swipeItem(adapterPosition + 1);
                        else
                            showDialogFinished(false, mPref.getString(Constants.MARKET_NAME, ""));

                    } else {
                        context.getSharedPreferences("Radios", 0).edit().putInt("radioValueNPD" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + adapterPosition + "/" + count_ofRef, selectedId).apply();
                        context.getSharedPreferences("Radios", 0).edit().putBoolean("npdUploaded" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + sku_name + "/" + count_ofRef, true).apply();
                        if (adapterPosition + 1 != tasksList.size())
                            if (!tasksList.get(adapterPosition).getType().equals(tasksList.get(adapterPosition + 1).getType()))
                                showDialog(tasksList.get(adapterPosition).getType(), tasksList.get(adapterPosition + 1).getType(), adapterPosition);
                            else
                                swipeItem(adapterPosition + 1);
                        else {
                            count_ofRef++;
                            if (count_ofRef <= Integer.parseInt(amountST.trim())) {
                                swipeItem(0);
                                Toast.makeText(context, "Siz " + (count_ofRef + 1) + "-ci dondurma dolabnının analizinə başladınız.", Toast.LENGTH_LONG).show();
                            } else
                                showDialogFinished(false, mPref.getString(Constants.MARKET_NAME, ""));
                        }
                    }
                }
                break;
            case "Planogram":
                if (myDB.updatePlanogram(product_type, sku_name, market_name, company_name, status, type)) {
                    if (!tasksList.get(adapterPosition).getCategories_to_check().equals("IC")) {
                        context.getSharedPreferences("Radios", 0).edit().putInt("radioValuePlanogram" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + adapterPosition, selectedId).apply();
                        context.getSharedPreferences("Radios", 0).edit().putBoolean("planogramUploaded" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + sku_name, true).apply();

                        if (adapterPosition + 1 != tasksList.size())
                            if (!tasksList.get(adapterPosition).getType().equals(tasksList.get(adapterPosition + 1).getType()))
                                showDialog(tasksList.get(adapterPosition).getType(), tasksList.get(adapterPosition + 1).getType(), adapterPosition);
                            else
                                swipeItem(adapterPosition + 1);
                        else
                            showDialogFinished(false, mPref.getString(Constants.MARKET_NAME, ""));

                    } else {
                        context.getSharedPreferences("Radios", 0).edit().putInt("radioValuePlanogram" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + adapterPosition + "/" + count_ofRef, selectedId).apply();
                        context.getSharedPreferences("Radios", 0).edit().putBoolean("planogramUploaded" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + sku_name + "/" + count_ofRef, true).apply();
                        if (adapterPosition + 1 != tasksList.size())
                            if (!tasksList.get(adapterPosition).getType().equals(tasksList.get(adapterPosition + 1).getType()))
                                showDialog(tasksList.get(adapterPosition).getType(), tasksList.get(adapterPosition + 1).getType(), adapterPosition);
                            else
                                swipeItem(adapterPosition + 1);
                        else {
                            count_ofRef++;
                            if (count_ofRef <= Integer.parseInt(amountST.trim())) {
                                swipeItem(0);
                                Toast.makeText(context, "Siz " + (count_ofRef + 1) + "-ci dondurma dolabnının analizinə başladınız.", Toast.LENGTH_LONG).show();
                            } else
                                showDialogFinished(false, mPref.getString(Constants.MARKET_NAME, ""));
                        }
                    }
                }
                break;
            case "SOS":
                if (myDB.updateSOS(brand_name, product_type, market_name, company_name, company_size, percentage, type)) {
                    context.getSharedPreferences("shareCounts", 0).edit().putString("etValueSOS" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + adapterPosition, company_size).apply();
                    SharedPreferences.Editor editor = context.getSharedPreferences("Radios", 0).edit();
                    editor.putBoolean("sosUploaded" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + brand_name, true);
                    editor.apply();
                    if (adapterPosition + 1 != getItemCount())
                        if (!tasksList.get(adapterPosition).getType().equals(tasksList.get(adapterPosition + 1).getType()))
                            showDialog(tasksList.get(adapterPosition).getType(), tasksList.get(adapterPosition + 1).getType(), adapterPosition);
                        else
                            swipeItem(adapterPosition + 1);
                    else
                        showDialogFinished(false, mPref.getString(Constants.MARKET_NAME, ""));
                }
                break;
            case "SOD":
                if (myDB.updateSOD(brand_name, product_type, market_name, company_name, company_size, percentage, type)) {
                    context.getSharedPreferences("shareCounts", 0).edit().putString("etValueSOD" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + adapterPosition, company_size).apply();
                    SharedPreferences.Editor editor = context.getSharedPreferences("Radios", 0).edit();
                    editor.putBoolean("sodUploaded" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, "") + brand_name, true);
                    editor.apply();
                    if (adapterPosition + 1 != getItemCount())
                        if (!tasksList.get(adapterPosition).getType().equals(tasksList.get(adapterPosition + 1).getType()))
                            showDialog(tasksList.get(adapterPosition).getType(), tasksList.get(adapterPosition + 1).getType(), adapterPosition);
                        else
                            swipeItem(adapterPosition + 1);
                    else
                        showDialogFinished(false, mPref.getString(Constants.MARKET_NAME, ""));
                }
                break;
        }
        progressBar.setVisibility(View.GONE);
        next.setEnabled(true);
        back.setEnabled(true);
        companySize.setEnabled(true);
        radioAnswer.setEnabled(true);
    }

}
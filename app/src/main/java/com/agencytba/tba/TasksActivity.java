package com.agencytba.tba;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.agencytba.tba.adapters.TasksAdapter;
import com.agencytba.tba.dbhelpers.DBHelper;
import com.agencytba.tba.modals.Tasks;
import com.agencytba.tba.server.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.view.View.VISIBLE;

public class TasksActivity extends AppCompatActivity {

    private static RecyclerView recyclerView;
    private SharedPreferences mPref,Pref;
    private List<Tasks> tasksList;
    public static RecyclerView.Adapter adapter;
    private android.app.AlertDialog dialogShare, dialogIC;
    public static String amountST;
    private DBHelper myDB;
    public static int Value;
    FloatingActionButton fab;
    public static int PICK_FROM_CAMERA = 10020;
    Uri imageURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        myDB = new DBHelper(this);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras !=null) {
                Value = extras.getInt("id");
            }
        } else {
            Value = (int) savedInstanceState.getSerializable("id");
        }
        Objects.requireNonNull(getSupportActionBar()).hide();
        mPref = getSharedPreferences("Constants", 0);
        Pref = getSharedPreferences("Radios", 0);
        tasksList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        retrieveTasks(mPref.getString(Constants.CATEGORY_NAME,""), mPref.getString(Constants.SUBCATEGORY_NAME,""));
        fab = findViewById(R.id.fab);

        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        fab.setRippleColor(ColorStateList.valueOf(getResources().getColor(R.color.rippleColor)));

        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                captureImage();
            }
        });

    }

    private void retrieveTasks(final String categoryName, final String subcategoryName){
        String tks = mPref.getString("tasks", "");
        SharedPreferences marketPref = getSharedPreferences("MarketAnalyze", 0);
        try {
            JSONObject jsonObject = new JSONObject(tks);
            JSONArray array = jsonObject.getJSONArray("tasks");
            int count = 0;
            for (int i = 0; i < array.length(); i++) {
                JSONObject jo = array.getJSONObject(i);
                if (!jo.getString("type").equals("SOD")) {
                    if (jo.getString("brand_type").split(" # ")[0].equals(categoryName) && jo.getString("brand_type").split(" # ")[1].equals(subcategoryName)) {
                        if (mPref.getString(Constants.CATEGORIES_TO_CHECK, "").equals("ALL")) {
                            if (mPref.getString(Constants.MARKET_CITY,"").equals("Bakı")||mPref.getString(Constants.MARKET_CITY,"").equals("Sumqayıt")||mPref.getString(Constants.MARKET_CITY,"").equals("Xırdalan")) {
                                if (mPref.getString(Constants.MARKET_TYPE,"").equals("Modern Trade")) {
                                    if (marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "OSA", 0) > 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "NPD", 0) > 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "Planogram", 0) > 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "SOS", 0) > 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "OSAIC", 0) > 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "NPDIC", 0) > 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "PlanogramIC", 0) > 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "SOSIC", 0) > 0) {
                                        count++;
                                        Tasks tasks = new Tasks(count, jo.getString("categories_to_check"), jo.getString("brand_name"), jo.getString("brand_type"), jo.getString("product_name"), jo.getString("type"), jo.getString("image"), jo.getString("company"), jo.getString("created_at"));
                                        tasksList.add(tasks);
                                    } else if (marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "OSA", 0) > 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "NPD", 0) == 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "Planogram", 0) == 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "SOS", 0) == 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "OSAIC", 0) > 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "NPDIC", 0) > 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "PlanogramIC", 0) == 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "SOSIC", 0) == 0) {
                                        if (jo.getString("categories_to_check").equals("IC")) {
                                            if (jo.getString("type").equals("OSA") || jo.getString("type").equals("NPD")) {
                                                count++;
                                                Tasks tasks = new Tasks(count, jo.getString("categories_to_check"), jo.getString("brand_name"), jo.getString("brand_type"), jo.getString("product_name"), jo.getString("type"), jo.getString("image"), jo.getString("company"), jo.getString("created_at"));
                                                tasksList.add(tasks);
                                            }
                                        }
                                        if (jo.getString("categories_to_check").equals("HPCFK")) {
                                            if (jo.getString("type").equals("OSA")) {
                                                count++;
                                                Tasks tasks = new Tasks(count, jo.getString("categories_to_check"), jo.getString("brand_name"), jo.getString("brand_type"), jo.getString("product_name"), jo.getString("type"), jo.getString("image"), jo.getString("company"), jo.getString("created_at"));
                                                tasksList.add(tasks);
                                            }
                                        }
                                    } else if (marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "OSA", 0) > 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "NPD", 0) == 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "Planogram", 0) == 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "SOS", 0) == 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "OSAIC", 0) == 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "NPDIC", 0) == 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "PlanogramIC", 0) == 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "SOSIC", 0) == 0) {
                                        if (jo.getString("categories_to_check").equals("IC")) {
                                            if (jo.getString("type").equals("OSA")) {
                                                count++;
                                                Tasks tasks = new Tasks(count, jo.getString("categories_to_check"), jo.getString("brand_name"), jo.getString("brand_type"), jo.getString("product_name"), jo.getString("type"), jo.getString("image"), jo.getString("company"), jo.getString("created_at"));
                                                tasksList.add(tasks);
                                            }
                                        }
                                        if (jo.getString("categories_to_check").equals("HPCFK")) {
                                            if (jo.getString("type").equals("OSA")) {
                                                count++;
                                                Tasks tasks = new Tasks(count, jo.getString("categories_to_check"), jo.getString("brand_name"), jo.getString("brand_type"), jo.getString("product_name"), jo.getString("type"), jo.getString("image"), jo.getString("company"), jo.getString("created_at"));
                                                tasksList.add(tasks);
                                            }
                                        }
                                    }
                                } else if (mPref.getString(Constants.MARKET_TYPE,"").equals("Traditional Trade")) {
                                    if (marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "OSA", 0) > 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "NPD", 0) > 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "NPDIC", 0) > 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "PlanogramIC", 0) > 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "SOSIC", 0) > 0) {
                                        if (jo.getString("categories_to_check").equals("HPCFK")) {
                                            if (jo.getString("type").equals("OSA")||jo.getString("type").equals("NPD")) {
                                                count++;
                                                Tasks tasks = new Tasks(count, jo.getString("categories_to_check"), jo.getString("brand_name"), jo.getString("brand_type"), jo.getString("product_name"), jo.getString("type"), jo.getString("image"), jo.getString("company"), jo.getString("created_at"));
                                                tasksList.add(tasks);
                                            }
                                        }
                                        if (jo.getString("categories_to_check").equals("IC")) {
                                            count++;
                                            Tasks tasks = new Tasks(count, jo.getString("categories_to_check"), jo.getString("brand_name"), jo.getString("brand_type"), jo.getString("product_name"), jo.getString("type"), jo.getString("image"), jo.getString("company"), jo.getString("created_at"));
                                            tasksList.add(tasks);
                                        }
                                    } else if (marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "OSA", 0) == 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "NPD", 0) == 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "OSAIC", 0) > 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "NPDIC", 0) > 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "PlanogramIC", 0) == 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "SOSIC", 0) == 0) {
                                        if (jo.getString("type").equals("OSA") || jo.getString("type").equals("NPD")) {
                                            if (jo.getString("categories_to_check").equals("IC")) {
                                                count++;
                                                Tasks tasks = new Tasks(count, jo.getString("categories_to_check"), jo.getString("brand_name"), jo.getString("brand_type"), jo.getString("product_name"), jo.getString("type"), jo.getString("image"), jo.getString("company"), jo.getString("created_at"));
                                                tasksList.add(tasks);
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            if (jo.getString("categories_to_check").equals(mPref.getString(Constants.CATEGORIES_TO_CHECK, ""))) {
                                if (mPref.getString(Constants.MARKET_CITY,"").equals("Bakı")||mPref.getString(Constants.MARKET_CITY,"").equals("Sumqayıt")||mPref.getString(Constants.MARKET_CITY,"").equals("Xırdalan")) {
                                    if (jo.getString("categories_to_check").equals("IC")) {
                                        if (mPref.getString(Constants.MARKET_TYPE,"").equals("HORECA")) {
                                            if (marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "OSAIC", 0) > 0) {
                                                if (jo.getString("type").equals("OSA")) {
                                                    count++;
                                                    Tasks tasks = new Tasks(count, jo.getString("categories_to_check"), jo.getString("brand_name"), jo.getString("brand_type"), jo.getString("product_name"), jo.getString("type"), jo.getString("image"), jo.getString("company"), jo.getString("created_at"));
                                                    tasksList.add(tasks);
                                                }
                                            }
                                        } else {
                                            if (marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "OSAIC", 0) > 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "NPDIC", 0) > 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "PlanogramIC", 0) > 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "SOSIC", 0) > 0) {
                                                count++;
                                                Tasks tasks = new Tasks(count, jo.getString("categories_to_check"), jo.getString("brand_name"), jo.getString("brand_type"), jo.getString("product_name"), jo.getString("type"), jo.getString("image"), jo.getString("company"), jo.getString("created_at"));
                                                tasksList.add(tasks);
                                            } else if (marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "OSAIC", 0) > 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "NPDIC", 0) > 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "PlanogramIC", 0) == 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "SOSIC", 0) == 0) {
                                                if (jo.getString("type").equals("OSA")||jo.getString("type").equals("NPD")) {
                                                    count++;
                                                    Tasks tasks = new Tasks(count, jo.getString("categories_to_check"), jo.getString("brand_name"), jo.getString("brand_type"), jo.getString("product_name"), jo.getString("type"), jo.getString("image"), jo.getString("company"), jo.getString("created_at"));
                                                    tasksList.add(tasks);
                                                }
                                            }
                                        }
                                    } else if (jo.getString("categories_to_check").equals("HPCFK")) {
                                        if (mPref.getString(Constants.MARKET_TYPE,"").equals("Modern Trade")) {
                                            if (marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "OSA", 0) > 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "NPD", 0) > 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "Planogram", 0) > 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "SOS", 0) > 0) {
                                                if (!jo.getString("categories_to_check").equals("IC")) {
                                                    count++;
                                                    Tasks tasks = new Tasks(count, jo.getString("categories_to_check"), jo.getString("brand_name"), jo.getString("brand_type"), jo.getString("product_name"), jo.getString("type"), jo.getString("image"), jo.getString("company"), jo.getString("created_at"));
                                                    tasksList.add(tasks);
                                                }
                                            } else if(marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "OSA", 0) > 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "NPD", 0) > 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "Planogram", 0) > 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "SOS", 0) > 0) {
                                                if (jo.getString("type").equals("OSA")||jo.getString("type").equals("NPD")) {
                                                    count++;
                                                    Tasks tasks = new Tasks(count, jo.getString("categories_to_check"), jo.getString("brand_name"), jo.getString("brand_type"), jo.getString("product_name"), jo.getString("type"), jo.getString("image"), jo.getString("company"), jo.getString("created_at"));
                                                    tasksList.add(tasks);
                                                }
                                            }
                                        }
                                        if (mPref.getString(Constants.MARKET_TYPE,"").equals("Pharmacy") || mPref.getString(Constants.MARKET_TYPE,"").equals("Traditional Trade")) {
                                            if (marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "OSA", 0) > 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "NPD", 0) > 0) {
                                                if (jo.getString("type").equals("OSA")||jo.getString("type").equals("NPD")) {
                                                    count++;
                                                    Tasks tasks = new Tasks(count, jo.getString("categories_to_check"), jo.getString("brand_name"), jo.getString("brand_type"), jo.getString("product_name"), jo.getString("type"), jo.getString("image"), jo.getString("company"), jo.getString("created_at"));
                                                    tasksList.add(tasks);
                                                }
                                            }
                                        } else {
                                            if (marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "OSA", 0) > 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "NPD", 0) == 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "Planogram", 0) == 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "SOS", 0) == 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "OSAIC", 0) == 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "NPDIC", 0) == 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "PlanogramIC", 0) == 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "SOSIC", 0) == 0) {
                                                if (jo.getString("categories_to_check").equals("IC")) {
                                                    if (jo.getString("type").equals("OSA")) {
                                                        count++;
                                                        Tasks tasks = new Tasks(count, jo.getString("categories_to_check"), jo.getString("brand_name"), jo.getString("brand_type"), jo.getString("product_name"), jo.getString("type"), jo.getString("image"), jo.getString("company"), jo.getString("created_at"));
                                                        tasksList.add(tasks);
                                                    }
                                                }
                                                if (jo.getString("categories_to_check").equals("HPCFK")) {
                                                    if (jo.getString("type").equals("OSA")) {
                                                        count++;
                                                        Tasks tasks = new Tasks(count, jo.getString("categories_to_check"), jo.getString("brand_name"), jo.getString("brand_type"), jo.getString("product_name"), jo.getString("type"), jo.getString("image"), jo.getString("company"), jo.getString("created_at"));
                                                        tasksList.add(tasks);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    if (mPref.getString(Constants.MARKET_TYPE, "").equals("Modern Trade")) {
                                        if (marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "OSA", 0) > 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "NPD", 0) > 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "Planogram", 0) > 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "SOS", 0) > 0) {
                                            count++;
                                            Tasks tasks = new Tasks(count, jo.getString("categories_to_check"), jo.getString("brand_name"), jo.getString("brand_type"), jo.getString("product_name"), jo.getString("type"), jo.getString("image"), jo.getString("company"), jo.getString("created_at"));
                                            tasksList.add(tasks);
                                        }
                                    } else if (mPref.getString(Constants.MARKET_TYPE, "").equals("Traditional Trade")) {
                                        if (marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "OSA", 0) > 0 && marketPref.getInt(mPref.getString(Constants.MARKET_NAME, "") + "NPD", 0) > 0) {
                                            if (jo.getString("type").contains("OSA")||jo.getString("type").contains("NPD")) {
                                                count++;
                                                Tasks tasks = new Tasks(count, jo.getString("categories_to_check"), jo.getString("brand_name"), jo.getString("brand_type"), jo.getString("product_name"), jo.getString("type"), jo.getString("image"), jo.getString("company"), jo.getString("created_at"));
                                                tasksList.add(tasks);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (jo.getString("brand_type").split(" # ")[0].equals("SOD") && jo.getString("brand_type").split(" # ")[1].equals(subcategoryName)) {
                        if (mPref.getString(Constants.CATEGORIES_TO_CHECK, "").equals("ALL")) {
                            count++;
                            Tasks tasks = new Tasks(count, jo.getString("categories_to_check"), jo.getString("brand_name"), jo.getString("brand_type"), jo.getString("product_name"), jo.getString("type"), jo.getString("image"), jo.getString("company"), jo.getString("created_at"));
                            tasksList.add(tasks);
                        } else {
                            if (jo.getString("categories_to_check").equals(mPref.getString(Constants.CATEGORIES_TO_CHECK, ""))) {
                                count++;
                                Tasks tasks = new Tasks(count, jo.getString("categories_to_check"), jo.getString("brand_name"), jo.getString("brand_type"), jo.getString("product_name"), jo.getString("type"), jo.getString("image"), jo.getString("company"), jo.getString("created_at"));
                                tasksList.add(tasks);
                            }
                        }
                    }
                }
            }
            if (tasksList.size() == 0) {
                Pref.edit().putBoolean(mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + "/" + mPref.getString(Constants.SUBCATEGORY_NAME, ""), true).apply();
                Toast.makeText(TasksActivity.this, "Bu kateqoriya üzrə yoxlanmalı məhsul yoxdur.", Toast.LENGTH_LONG).show();
                startActivity(new Intent(TasksActivity.this, SubCategoriesActivity.class));
            } else
                adapterSet();

        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

    public void adapterSet() {
        if (mPref.getString(Constants.SUBCATEGORY_NAME, "").equals("IH")) {
            showDialogIC("In-home");
        }
        else if (mPref.getString(Constants.SUBCATEGORY_NAME, "").equals("OH")) {
            showDialogIC("Impulse");
        }
        else if (mPref.getString(Constants.SUBCATEGORY_NAME, "").equals("MIXED")) {
            showDialogIC("Qarışıq");
        } else
        if (tasksList.get(0).getType().equals("SOS") || tasksList.get(0).getType().equals("SOD"))
            showDialogShare();

        adapter = new TasksAdapter(tasksList, this);
        LinearLayoutManager llm = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                llm.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
    }

    public static void swipeItem(int toPosition){
        recyclerView.scrollToPosition(toPosition);
    }

    private void showDialogShare() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        if (!Pref.getBoolean((mPref.getString(Constants.MARKET_NAME, "")+"/"+mPref.getString(Constants.CATEGORY_NAME, "")+"/"+mPref.getString(Constants.SUBCATEGORY_NAME,"")),false))
            builder.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_sos, null);
        final TextView tv_message = (TextView)view.findViewById(R.id.tv_message);
        TextView subCategory = (TextView)view.findViewById(R.id.sub_category);
        TextView share_question = (TextView)view.findViewById(R.id.share_question);
        subCategory.setText(mPref.getString(Constants.CATEGORY_NAME,"") + " - " + mPref.getString(Constants.SUBCATEGORY_NAME,""));

        if (tasksList.get(0).getType().equals("SOS")){
            switch (mPref.getString(Constants.SUBCATEGORY_NAME,"")) {
                case "Shampoo & Conditioner":
                    share_question.setText("Bütün şampun/balzam üzlərinin (face) sayını daxil edin.");
                    break;
                case "Toothpaste":
                    share_question.setText("Bütün diş pastası üzlərinin (face) sayını daxil edin.");
                    break;
                case "Mouthwash":
                    share_question.setText("Bütün ağız yaxalayıcı üzlərinin (face) sayını daxil edin.");
                    break;
                case "Toothbrush":
                    share_question.setText("Bütün diş fırçası üzlərinin (face) sayını daxil edin.");
                    break;
                case "Aerosol":
                    share_question.setText("Bütün Aerosol üzlərinin (face) sayını daxil edin.");
                    break;
                case "Hand cream":
                    share_question.setText("Bütün əl və bədən tubik kremlərinin üzlərinin sayını daxil edin.");
                    break;
                case "Stick":
                    share_question.setText("Bütün Stick üzlərinin (face) sayını daxil edin.");
                    break;
                case "Rollon":
                    share_question.setText("Bütün Rollon üzlərinin (face) sayını daxil edin.");
                    break;
                case "Bleach":
                    share_question.setText("Bütün tualet təmizləyici gel/bleach üzlərinin sayını daxil edin.");
                    break;
                case "Block":
                    share_question.setText("Bütün tualet bloku üzlərinin sayını daxil edin.");
                    break;
                case "Cream":
                    if (tasksList.get(0).getBrand_type().split(" # ")[0].equals("HHC - kitchen & bath"))
                        share_question.setText("Bütün səth təmizləyici krem üzlərinin sayını daxil edin.");
                    else if (tasksList.get(0).getBrand_type().split(" # ")[0].equals("Hand & body"))
                        share_question.setText("Bütün əl və bədən tubik kremlərinin üzlərinin sayını daxil edin.");
                    break;
                case "Spray":
                    share_question.setText("Bütün mətbəx/tualet səth təmizləyici sprey üzlərinin sayını daxil edin.");
                    break;
                case "Liquid soap":
                    share_question.setText("Bütün maye sabun üzlərinin sayını daxil edin.");
                    break;
                case "Shower gel":
                    share_question.setText("Bütün duş geli üzlərinin sayını daxil edin.");
                    break;
                case "Face cream":
                    share_question.setText("Bütün üz kremi üzlərinin sayını daxil edin.");
                    break;
                case "Face cleansing":
                    share_question.setText("Bütün üz təmizləmə suyu üzlərinin sayını daxil edin.");
                    break;
                case "Ketchup":
                    share_question.setText("Bütün ketçup (doypack+bottle) üzlərinin sayını daxil edin.");
                    break;
                case "Mayo":
                    share_question.setText("Bütün doypack mayonez üzlərinin sayını daxil edin.");
                    break;
                case "Sauce":
                    share_question.setText("Bütün sous üzlərinin sayını daxil edin.");
                    break;
                case "Tea":
                    share_question.setText("Bütün paket çay üzlərinin sayını daxil edin.");
                    break;
                case "SOS":
                    share_question.setText("Bütün dondurma dolablarının sayını daxil edin.");
                    break;
            }
        }
        if (tasksList.get(0).getType().equals("SOD")){
            switch (mPref.getString(Constants.SUBCATEGORY_NAME,"")) {
                case "Hair":
                    share_question.setText("Bütün şampun/balzam displeylərinin sayını daxil edin.");
                    break;
                case "Oral":
                    share_question.setText("Bütün ağız baxım displeylərinin sayını daxil edin.");
                    break;
                case "Deo":
                    share_question.setText("Bütün deodorant displeylərinin sayını daxil edin.");
                    break;
                case "HHC - wc & universal":
                    share_question.setText("Bütün tualet təmizləyici displeylərinin sayını daxil edin. ");
                    break;
                case "HHC - kitchen & bath":
                    share_question.setText("Bütün səth təmizləyici displeylərinin sayını daxil edin. ");
                    break;
                case "Skin cleansing":
                    share_question.setText("Bütün maye sabun / duş geli displeylərinin sayını daxil edin.");
                    break;
                case "Face care":
                    share_question.setText("Bütün üz baxım displeylərinin sayını daxil edin.");
                    break;
                case "Hand & body":
                    share_question.setText("Bütün əl və bədən kremlərinin displeylərinin sayını daxil edin.");
                    break;
            }
        }
        final EditText amount = (EditText)view.findViewById(R.id.amount);
        final ProgressBar progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        final String amountSaved = getSharedPreferences("shareCounts", 0).getString("amountShare" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, ""), "");
        amount.setText(amountSaved);
        builder.setView(view);
        builder.setPositiveButton("Daxil edin", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogShare = builder.create();
        dialogShare.show();
        dialogShare.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
        dialogShare.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                amountST = amount.getText().toString();
                if(!amountST.isEmpty()){
                    if (!amountSaved.equals("")&&!amountSaved.equals("0")&&!amountST.equals(amountSaved)) {
                        progressBar.setVisibility(VISIBLE);
                        getSharedPreferences("shareCounts", 0).edit().putString("amountShare" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, ""), amountST).apply();
                        updateTotalSize(progressBar, tasksList.get(0).getType(), tasksList.get(0).getBrand_type(), amountST);
                    } else {
                        getSharedPreferences("shareCounts", 0).edit().putString("amountShare" + mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + mPref.getString(Constants.SUBCATEGORY_NAME, ""), amountST).apply();
                        if (!amountST.equals("0")) {
                            dialogShare.dismiss();
                        } else {
                            dialogShare.dismiss();
                            Pref.edit().putBoolean(mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + "/" + mPref.getString(Constants.SUBCATEGORY_NAME, ""), true).apply();
                            startActivity(new Intent(TasksActivity.this, SubCategoriesActivity.class));
                        }
                    }
                } else {
                    tv_message.setVisibility(VISIBLE);
                }
            }
        });
    }

    private void showDialogIC(String subCategory) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        if (!Pref.getBoolean((mPref.getString(Constants.MARKET_NAME, "")+"/"+mPref.getString(Constants.CATEGORY_NAME, "")+"/"+mPref.getString(Constants.SUBCATEGORY_NAME,"")),false))
            builder.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_ic, null);
        final TextView tv_message = (TextView)view.findViewById(R.id.tv_message);
        TextView ref_count = (TextView)view.findViewById(R.id.refCount);
        ref_count.setText(subCategory+getResources().getString(R.string.ref_count));
        final EditText amount = (EditText)view.findViewById(R.id.amount);
        amount.setText(Pref.getString("refCount"+mPref.getString(Constants.MARKET_NAME, "")+"/"+mPref.getString(Constants.CATEGORY_NAME,"")+mPref.getString(Constants.SUBCATEGORY_NAME,""),""));
        builder.setView(view);
        builder.setPositiveButton("Daxil edin", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogIC = builder.create();
        dialogIC.show();
        dialogIC.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
        dialogIC.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amountST = amount.getText().toString();
                Pref.edit().putString("refCount"+mPref.getString(Constants.MARKET_NAME, "")+"/"+mPref.getString(Constants.CATEGORY_NAME,"")+mPref.getString(Constants.SUBCATEGORY_NAME,""), amountST).apply();
                if(!amountST.isEmpty()){
                    if (Integer.parseInt(amountST) == 0) {
                        dialogIC.dismiss();
                        Pref.edit().putBoolean(mPref.getString(Constants.MARKET_NAME, "") + "/" + mPref.getString(Constants.CATEGORY_NAME, "") + "/" + mPref.getString(Constants.SUBCATEGORY_NAME, ""), true).apply();
                        startActivity(new Intent(TasksActivity.this, SubCategoriesActivity.class));
                    } else {
                        dialogIC.dismiss();
                    }
                } else {
                    tv_message.setVisibility(VISIBLE);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateTotalSize(@NotNull ProgressBar progressBar, @NotNull String type, String product_type, String total_size) {
        DBHelper myDB = new DBHelper(this);
        myDB.updateTotalSizeRaw(product_type, mPref.getString(Constants.MARKET_NAME, ""), mPref.getString(Constants.COMPANY, ""), total_size, type);
        dialogShare.dismiss();
        progressBar.setVisibility(View.GONE);
    }

    public void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String path = FileUtils.getDataDir(this).getAbsolutePath();

        String fileName = "wp_image.jpg";
        File file = new File(path, fileName);
        imageURI = FileProvider.getUriForFile(TasksActivity.this, "com.agencytba.tba.provider", file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == PICK_FROM_CAMERA) {
                    //Toast.makeText(TasksTestOfflineActivity.this, "asdasd", Toast.LENGTH_SHORT).show();
                    onClickWhatsApp(imageURI);

                }
            }
        } catch(Exception E) {

        }
    }

    public void onClickWhatsApp(Uri imagePath) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpeg");
        intent.setPackage("com.whatsapp");
        intent.putExtra(Intent.EXTRA_STREAM, imagePath);
        startActivity(Intent.createChooser(intent, "Share with"));

    }
}

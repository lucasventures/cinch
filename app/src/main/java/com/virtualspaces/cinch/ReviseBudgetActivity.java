package com.virtualspaces.cinch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.virtualspaces.cinch.entities.MonthlyFinances;

import java.util.ArrayList;
import java.util.List;

public class ReviseBudgetActivity extends AppCompatActivity{

    /*current values

    VALUES  | INDEX
    NAME     0
    MONTHLY  1
    EXPENSES 2
    SAVINGS  3
    BUDGET   4
     */

    private ArrayList<Double> currentValues;

    private EditText nameText,
            monthText,
            expText,
            savingsText,
            budgetText;

    private TextView title;

    private PieChart mPieChart;
    private PieChartHelper helper;
    private Button saveBtn;

    boolean nameEdited = false;
    boolean monthlyEdited = false;
    boolean expensesEdited = false;
    boolean savingsEdited = false;
    boolean budgetEdited = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revise_budget);

        Typeface typeface = Typeface.create(Constants.QUICKSAND_REG, Typeface.NORMAL);

        //init
        nameText = (EditText) findViewById(R.id.revise_name);
        nameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nameEdited = true;

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        monthText = (EditText) findViewById(R.id.revise_monthly);
        monthText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                monthlyEdited = true;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        expText = (EditText) findViewById(R.id.revise_expenses);
        expText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                expensesEdited = true;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        savingsText = (EditText) findViewById(R.id.revise_savings);
        savingsText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                savingsEdited = true;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        saveBtn = (Button) findViewById(R.id.save_btn);

        //set title typeface
        title = (TextView) findViewById(R.id.title);
        title.setTypeface(typeface);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        //init pie
        mPieChart = (PieChart) findViewById(R.id.revise_pie);
        helper = new PieChartHelper(mPieChart, this, Constants.PIE_CATEGORIES, getCurrentValues(prefs));
        helper.setUpPieChart();

        //set name from prefs
        nameText.setHint("Name: " + prefs.getString(Constants.NAME, "null"));
        //set double values to edit text hint
        monthText.setHint("Current Income: " + prefs.getString(Constants.MONTHLY, "0.0"));
        expText.setHint("Current Expenses: " + prefs.getString(Constants.FIXED, "0.0"));
        savingsText.setHint("Current Savings: " + prefs.getString(Constants.SAVINGS, "0.0"));

        String name = prefs.getString(Constants.NAME, "null");
        String inc = prefs.getString(Constants.MONTHLY, "0.0");
        String exp = prefs.getString(Constants.FIXED,"0.0");
        String sav = prefs.getString(Constants.SAVINGS,"0.0");

        MonthlyFinances mon = new MonthlyFinances(name,inc,exp,sav);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }

    private void save() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = prefs.edit();
        List<MonthlyFinances> m = MonthlyFinances.listAll(MonthlyFinances.class);

        if (nameEdited) {
            //get and save new value
            edit.putString(Constants.NAME, nameText.getText().toString());
            edit.apply();

        } else {
            //maybe do something here in the future?
        }

        if (monthlyEdited) {
            edit.putString(Constants.MONTHLY, monthText.getText().toString());
            edit.apply();

        } else {

        }
        if (expensesEdited) {
            edit.putString(Constants.FIXED, expText.getText().toString());
            edit.apply();

        } else {

        }

        if (savingsEdited) {
            edit.putString(Constants.SAVINGS, savingsText.getText().toString());
            edit.apply();

        } else {

        }


        Intent intent = new Intent(ReviseBudgetActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    private ArrayList<Double> getCurrentValues(SharedPreferences prefs) {
        ArrayList<Double> vals = new ArrayList<>(4);
        vals.add(Double.parseDouble(prefs.getString(Constants.FIXED, "0.0")));
        vals.add(Double.parseDouble(prefs.getString(Constants.BUDGET, "0.0")));
        vals.add(Double.parseDouble(prefs.getString(Constants.SAVINGS, "0.0")));

        return vals;
    }

}

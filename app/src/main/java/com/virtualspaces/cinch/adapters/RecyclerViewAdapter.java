package com.virtualspaces.cinch.adapters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.orm.SugarRecord;
import com.virtualspaces.cinch.Constants;
import com.virtualspaces.cinch.R;
import com.virtualspaces.cinch.activities.AddTransactionActivity;
import com.virtualspaces.cinch.activities.NotepadActivity;
import com.virtualspaces.cinch.activities.TransactionListActivity;
import com.virtualspaces.cinch.entities.MyTransaction;
import com.virtualspaces.cinch.entities.Note;
import com.virtualspaces.cinch.fragments.NotepadFragment;
import com.virtualspaces.cinch.viewhelpers.PieChartHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LUCASVENTURES on 5/22/2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "RecyclerAdapter";
    private int listType;
    private int[] summaryList = {3, 0, 1, 2, 4};
    private String[] calcList = {
            "Savings", "Gratuity",
            "Compound Interest",
            "Annual Percentage Rate",
            "Rent", "Mortgage",
            "Car Loan", "Custom Loan"
    };
    private int currentHolder;
    private Typeface ptSans;
    private Context context;
    private ArrayList<MyTransaction> transList;
    private ArrayList<Note> notesList;
    private MyTransaction transaction;
    //this array dictates the "Category Names"


    private ArrayList<Double> orderedDataValues;

    //constructor for summary fragment
    public RecyclerViewAdapter(int num, Context context) {
        this.context = context;
        listType = num;
        //ptSans = Typeface.createFromAsset(context.getAssets(), "fonts/pt-sans.ttf");
    }

    //constructor for transactions list
    //bool is just to avoid common erasure error in constructor
    public RecyclerViewAdapter(int num, Context context, ArrayList<MyTransaction> transactions, boolean bool) {
        this.context = context;
        listType = num;
        transList = transactions;
        //ptSans = Typeface.createFromAsset(context.getAssets(), "fonts/pt-sans.ttf");
    }

    //constructor for notes list
    public RecyclerViewAdapter(int num, Context context, ArrayList<Note> notes) {
        this.context = context;
        listType = num;
        this.notesList = notes;
        //ptSans = Typeface.createFromAsset(context.getAssets(), "fonts/pt-sans.ttf");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (listType) {

            case 0:
                //summary list
                //edit here to change card order
                switch (viewType) {
                    case 0://text
                        currentHolder = 0;
                        View v1 = inflater.inflate(R.layout.cardview_recent_transactions, parent, false);
                        return new RecentTransactionViewHolder(v1);
                    case 1://horizontal bar
                        currentHolder = 1;
                        View v2 = inflater.inflate(R.layout.cardview_goals, parent, false);
                        return new GoalProgressViewHolder(v2);
                    case 2://horizontal bar
                        currentHolder = 2;
                        View v3 = inflater.inflate(R.layout.cardview_goals, parent, false);
                        return new GoalProgressViewHolder(v3);
                    case 3://pie chart
                        currentHolder = 3;
                        View v4 = inflater.inflate(R.layout.cardview_pie, parent, false);
                        return new PieChartViewHolder(v4);
                    case 4:
                        currentHolder = 4;
                        View v5 = inflater.inflate(R.layout.cardview_summary, parent, false);
                        return new SimpleViewHolder(v5);
                }
            case 1:     //calculator cardview

                View v2 = inflater.inflate(R.layout.cardview_calc, parent, false);
                return new SimpleViewHolder(v2);

            case 2:     //notes recyclerview
                View v3 = inflater.inflate(R.layout.cardview_note, parent, false);
                return new NoteViewHolder(v3);

            case 3:     // transaction list
                View v4 = inflater.inflate(R.layout.card_view_trans_list, parent, false);
                return new TransactionListHolder(v4);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (listType) {

            //summary list
            case 0:
                Typeface typeface = Typeface.createFromAsset(context.getAssets(), Constants.QUICKSAND_REG);
                switch (currentHolder) {
                    case 0:
                        RecentTransactionViewHolder transactionViewHolder
                                = (RecentTransactionViewHolder) holder;
                        //basic view settings

                        transactionViewHolder.addTrans.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, AddTransactionActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                context.startActivity(intent);
                            }
                        });

                        transactionViewHolder.viewAll.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, TransactionListActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                context.startActivity(intent);
                            }
                        });

                        /*
                        MOST RECENT TRANSACTION SETTINGS
                         */

                        //get most recent transaction value
                        List<MyTransaction> transactions = new ArrayList<>();

                        try {
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                            int month = prefs.getInt("month",0);
                            int year = prefs.getInt("year",2016);
                            List<MyTransaction> transactz = MyTransaction.listAll(MyTransaction.class);
                            for(MyTransaction tr : transactz){
                                if(tr.getMonth() == month && tr.getYear() ==year){
                                    transactions.add(tr);
                                    Log.d(TAG, "Transactions in list: "+tr.getValue()+", type:"+tr.getCategory());

                                }else{
                                    Log.d(TAG, "Transactions not in list: "+tr.getValue()+", type:"+tr.getCategory());
                                }
                            }
                           /*
                            transactions = MyTransaction.findWithQuery(MyTransaction.class,
                                    "Select * from MyTransaction where m_month = ? and m_year = ?",""+month,""+year);
                            */
                        } catch (SQLiteException e) {
                            Log.d("RecyclerViewAdapter", "onBindViewHolder: " + e.getMessage());
                        }


                        if (transactions.size() <= 0) {
                            //db table is empty
                            Log.d("RecyclerAdapter", "onBindViewHolder: transaction object contains null values");
                            transactionViewHolder.regLayout.setVisibility(View.GONE);
                            transactionViewHolder.tapTransaction.setVisibility(View.VISIBLE);
                            transactionViewHolder.tapTransaction.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(context, AddTransactionActivity.class);
                                    context.startActivity(intent);
                                }
                            });

                        } else {
                            //get last transaction in list (most recently added)
                            MyTransaction transaction = transactions.get(transactions.size() - 1);

                            Log.d("RecyclerAdapter", "onBindViewHolder: binding most recent transaction to view holder!");
                            transactionViewHolder.tapTransaction.setVisibility(View.GONE);
                            //populate viewholder values as

                            switch (transaction.getCategory()) {
                                case Constants.TYPE_SAVINGS:
                                    transactionViewHolder.type.setTextColor(context.getResources().getColor(R.color.pie_green));
                                    transactionViewHolder.value.setTextColor(context.getResources().getColor(R.color.pie_green));

                                    break;
                                case Constants.TYPE_EXTRAS:
                                    transactionViewHolder.type.setTextColor(context.getResources().getColor(R.color.pie_gray_select));
                                    transactionViewHolder.value.setTextColor(context.getResources().getColor(R.color.pie_gray_select));

                                    break;
                                case Constants.TYPE_NECESSITY:
                                    transactionViewHolder.type.setTextColor(context.getResources().getColor(R.color.pie_blue));
                                    transactionViewHolder.value.setTextColor(context.getResources().getColor(R.color.pie_blue));
                                    break;
                                case Constants.TYPE_FIXED:
                                    transactionViewHolder.type.setTextColor(context.getResources().getColor(R.color.pie_red));
                                    transactionViewHolder.value.setTextColor(context.getResources().getColor(R.color.pie_red));
                                    break;
                            }

                            String correctStr = null;
                            transactionViewHolder.title.setText("Most Recent Transaction");
                            transactionViewHolder.title.setTypeface(typeface);
                            transactionViewHolder.type.setText(transaction.getCategory());
                            transactionViewHolder.type.setAllCaps(true);

                            String val = transaction.getValue();
                            if (val.contains(".")) {
                                //value contains decimals
                                int dot = val.indexOf(".") + 1;
                                if (val.length() - dot < 2) {
                                    //value has either a period, or period & number
                                    switch (val.length() - dot) {
                                        case 0:
                                            correctStr = val + "00";
                                            break;
                                        case 1:
                                            correctStr = val + "0";
                                            break;
                                    }

                                } else if (val.length() - dot > 2) {
                                    //value has more than two decimals
                                    correctStr = val.substring(0, val.indexOf(".") + 3);
                                } else if (val.length() - dot == 2) {
                                    //value has exactly two decimals
                                    correctStr = val;
                                }
                            } else {
                                //value was a whole number/integer
                                correctStr = val + ".00";
                            }

                            transactionViewHolder.value.setText("$" + correctStr);

                        }


                        break;
                    case 1:
                        /*
                        SAVINGS GOAL
                         */
                        GoalProgressViewHolder goals
                                = (GoalProgressViewHolder) holder;
                        //set title

                        goals.title.setText("Monthly Savings Goal");
                        goals.title.setTypeface(typeface);

                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

                        //max values, string and double instances
                        double saveMax = Double.parseDouble(preferences.getString(Constants.SAVINGS, "0.0"));
                        String maxVal = "$" + saveMax;

                        //loop to extract all relevant saving values into saveProgress var
                        double saveProgress = 0.0;
                        List<MyTransaction> transList = MyTransaction.listAll(MyTransaction.class);
                        for (MyTransaction trans : transList) {
                            if (trans.getCategory().equals("savings")) {
                                Log.d("RecyclerAdapter", "savings transaction values: " + Double.parseDouble(trans.getValue()));
                                saveProgress += Double.parseDouble(trans.getValue());
                            }
                        }

                        //progress values, String and double instances
                        //set the progress values of the progress view
                        String progVal = "";
                        if (saveProgress == 0.0) {
                            progVal = "Add a \"Savings\" transaction to begin.";
                        } else {
                            progVal = "$" + saveProgress;
                        }

                        //number that gets incremented
                        goals.progressText.setText(progVal);

                        //max save val
                        goals.goalText.setText(maxVal);
                        //setup goal progress
                        goals.bar.setProgressDrawable
                                (GoalProgressViewHolder
                                        .context
                                        .getResources()
                                        .getDrawable(R.drawable.saved_progress));

                        goals.bar.setMax(100);

                        double ratio = saveProgress / saveMax;

                        ratio = ratio * 100;



                        if(saveProgress>saveMax){
                            goals.detail.setText("+$"+(saveProgress-saveMax)+"beyond goal");
                            goals.detail.setTextColor(context.getResources().getColor(R.color.pie_green));
                        }else{
                            if(saveProgress!=0.0){
                                goals.detail.setText("$"+(saveMax-saveProgress)+" to go!");
                                goals.detail.setTextColor(context.getResources().getColor(R.color.pie_green));
                            }else{
                                goals.detail.setVisibility(View.INVISIBLE);
                            }
                        }


                        Log.d("recycleradapter", "onBindViewHolder: ratio" + ratio);

                        ObjectAnimator animation = ObjectAnimator.ofInt(goals.bar, "progress", (int) ratio);
                        animation.setDuration(1600); // 0.5 second
                        animation.setInterpolator(new DecelerateInterpolator());
                        animation.start();

                        break;
                    case 2:
                        /*
                        BUDGET
                         */
                        GoalProgressViewHolder goals2
                                = (GoalProgressViewHolder) holder;
                        //set title
                        goals2.title.setTypeface(typeface);
                        goals2.title.setText("Monthly Disposable Income");

                        //get fixed transaction entries to apply to budget

                        List<MyTransaction> fixedTrans = MyTransaction.listAll(MyTransaction.class);
                        double transEntries = 0.0;
                        for(MyTransaction trans : fixedTrans){
                            if(trans.getCategory().equals("fixed")){
                                transEntries += Double.parseDouble(trans.getValue());
                            }
                        }

                        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
                        double maxBudget = Double.parseDouble(pref.getString(Constants.BUDGET, "0.0"))-transEntries;

                        goals2.goalText.setText("$" + maxBudget);

                        /*
                        get sum of all values entered into spending extras
                         */

                        int month = pref.getInt("month",0);
                        int year = pref.getInt("year",0);

                        double sumOfBudget = 0.0;
                        List<MyTransaction> transBudget = SugarRecord.listAll(MyTransaction.class);
                        for (MyTransaction t : transBudget) {
                            if (!t.getCategory().equals("fixed")&&t.getMonth() == month &&t.getYear() == year) {
                                sumOfBudget += Double.parseDouble(t.getValue());
                            }
                        }

                        Log.d("recycleradapter", "sum of budget values: " + sumOfBudget);

                        String sum;
                        if (sumOfBudget == 0) {
                            sum = "Add a transaction to begin.";

                        } else {

                            sum = "$" + sumOfBudget;
                        }


                        double ratio2 = sumOfBudget / maxBudget;
                        ratio2 = ratio2 * 100;

                        Log.d("recycleradapter", "ratio: budget: " + ratio2);

                        goals2.progressText.setText(sum);
                        //setup charts here
                        goals2.bar.setProgressDrawable(GoalProgressViewHolder.context.getResources().getDrawable(R.drawable.budget_progress));
                        goals2.bar.setMax(100);


                        if(sumOfBudget>maxBudget){
                            goals2.detail.setText("-$"+(sumOfBudget-maxBudget));
                            goals2.detail.setTextColor(context.getResources().getColor(R.color.pie_red));
                        }else{
                            if(sumOfBudget==0.0){
                                goals2.detail.setVisibility(View.INVISIBLE);
                            }else {

                               Double d = maxBudget - sumOfBudget;

                                goals2.detail.setText("$" + (maxBudget - sumOfBudget) + " available");
                                goals2.detail.setTextColor(context.getResources().getColor(R.color.pie_blue));
                            }
                        }

                        ObjectAnimator animation2 = ObjectAnimator.ofInt(goals2.bar, "progress", (int) ratio2);
                        animation2.setDuration(1800); // 0.5 second
                        animation2.setInterpolator(new DecelerateInterpolator());
                        animation2.start();

                        break;
                    case 3:

                        /*
                        PIE CHART
                         */

                        PieChartViewHolder pieChart = (PieChartViewHolder) holder;


                        /*
                        PIE SHOULD TAKE MONTHLY INCOME, SAVINGS GOALS, AND FIXED EXPENSES INTO CONSIDERATION
                            WITH USEABLE BUDGET VALUE DEFINED AS THE REMAINDER
                         */

                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                        //monthly income
                        double monthly = Double.parseDouble(prefs.getString(Constants.MONTHLY, "null"));

                        //expenses
                        double fixed = Double.parseDouble(prefs.getString(Constants.FIXED, "null"));

                        //budget values (monthly - expenses - saving goals)
                        double disposable = Double.parseDouble(prefs.getString(Constants.BUDGET, "null"));
                        double saved = Double.parseDouble(prefs.getString(Constants.SAVINGS, "null"));

                        Log.d("RECYCLER PIE VALUE", "onBindViewHolder: fixed " + fixed);
                        Log.d("RECYCLER PIE VALUE", "onBindViewHolder: nec " + disposable);
                        Log.d("RECYCLER PIE VALUE", "onBindViewHolder: monthly " + monthly);
                        Log.d("RECYCLER PIE VALUE", "onBindViewHolder: saved " + saved);

                        orderedDataValues = new ArrayList<>();
                        //FIXED EXPENSES
                        orderedDataValues.add(fixed);
                        //NECESSITIES
                        orderedDataValues.add(disposable);
                        //EXTRAS (EXPENDABLE CASH)
                        //SAVINGS
                        orderedDataValues.add(saved);

                        pieChart.title.setText("Projected Monthly Budget");

                        pieChart.title.setTypeface(typeface);

                        //create object
                        PieChartHelper chart = new PieChartHelper(
                                pieChart.pie, //piechart
                                PieChartViewHolder.context, //context
                                Constants.PIE_CATEGORIES,//categories in specific order
                                orderedDataValues // data in respective category order
                        );
                        chart.setUpPieChart();

                        break;
                    case 4://rate in the app store

                        RecyclerViewAdapter.SimpleViewHolder rately =
                                (RecyclerViewAdapter.SimpleViewHolder) holder;
                        rately.textView.setText("");

                        //rately.textView.setTypeface(typeface);

                        break;
                }
                break;
            //calculator cardview
            case 1:
                RecyclerViewAdapter.SimpleViewHolder calcHolder
                        = (RecyclerViewAdapter.SimpleViewHolder) holder;
                String calcString = calcList[position];
                calcHolder.textView.setText(calcString);
                setClicks(SimpleViewHolder.context, calcHolder.textView);
                break;

            case 2://notepad bindings
                final RecyclerViewAdapter.NoteViewHolder noteViewHolder
                        = (RecyclerViewAdapter.NoteViewHolder) holder;

                Note noteEntry = notesList.get(position);
                noteViewHolder.title.setText(noteEntry.getTitle());
                noteViewHolder.body.setText(noteEntry.getBody());

                noteViewHolder.timestamp.setText(noteEntry.getTimestamp());
                noteViewHolder.id.setText("" + noteEntry.getId());
                noteViewHolder.card.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        final TextView text = (TextView) v.findViewById(R.id.id);

                        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View view = inflater.inflate(R.layout.custom_alert, null);
                        Button editBtn = (Button) view.findViewById(R.id.alert_edit);
                        Button deleteBtn = (Button) view.findViewById(R.id.alert_delete);

                        deleteBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(context, "Entry Deleted!", Toast.LENGTH_SHORT).show();
                                Note note = Note.findById(Note.class, Integer.parseInt(text.getText().toString()));
                                note.delete();

                                // fade card out
                                AlphaAnimation fadeAnim = new AlphaAnimation(1, 0);
                                fadeAnim.setFillAfter(false);
                                fadeAnim.setDuration(600);
                                fadeAnim.start();

                                noteViewHolder.main.startAnimation(fadeAnim);
                                //refresh content
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        NotepadFragment.setUpRecycler(
                                                (ArrayList<Note>) Note.listAll(Note.class),
                                                NotepadFragment.mRecyclerView);
                                    }
                                }, 800);
                            }
                        });

                        editBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Toast.makeText(context, "editing entry", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, NotepadActivity.class);
                                intent.putExtra("id", Integer.parseInt(text.getText().toString()));
                                context.startActivity(intent);
                            }
                        });

                        AlertDialog.Builder alert = new AlertDialog.Builder(context)
                                .setView(view)
                                .setCancelable(true)
                                .setMessage("Choose an action:");
                        AlertDialog dialog = alert.create();
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();

                        return true;
                    }
                });


                break;

            case 3: //transaction list binding'
                final RecyclerViewAdapter.TransactionListHolder transactionListHolder
                        = (RecyclerViewAdapter.TransactionListHolder) holder;

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                int month = prefs.getInt("month",0);
                int year = prefs.getInt("year",2016);

                List<MyTransaction> filteredTrans = new ArrayList<>();
                for(MyTransaction tr : transList){
                    if(tr.getMonth() == month & tr.getYear() ==year){
                        filteredTrans.add(tr);
                        Log.d(TAG, "Transactions in list: "+tr.getValue()+", type:"+tr.getMonth());

                    }else{
                        Log.d(TAG, "Transactions not in list: "+tr.getValue()+", type:"+tr.getMonth());
                    }
                }

                MyTransaction trans = filteredTrans.get(position);

                switch (trans.getCategory()) {
                    case Constants.TYPE_SAVINGS:
                        transactionListHolder.title.setTextColor(context.getResources().getColor(R.color.pie_green));
                        transactionListHolder.body.setTextColor(context.getResources().getColor(R.color.pie_green));

                        break;
                    case Constants.TYPE_EXTRAS:
                        transactionListHolder.title.setTextColor(context.getResources().getColor(R.color.pie_gray_select));
                        transactionListHolder.body.setTextColor(context.getResources().getColor(R.color.pie_gray_select));

                        break;
                    case Constants.TYPE_NECESSITY:
                        transactionListHolder.title.setTextColor(context.getResources().getColor(R.color.pie_blue));
                        transactionListHolder.body.setTextColor(context.getResources().getColor(R.color.pie_blue));
                        break;
                    case Constants.TYPE_FIXED:
                        transactionListHolder.title.setTextColor(context.getResources().getColor(R.color.pie_red));
                        transactionListHolder.body.setTextColor(context.getResources().getColor(R.color.pie_red));
                        break;
                }


                transactionListHolder.title.setText(trans.getCategory());
                transactionListHolder.body.setText("$" + trans.getValue());
                transactionListHolder.detail.setText(trans.getDetail());

                transactionListHolder.timestamp.setText(trans.getTime());
                transactionListHolder.id.setText(trans.getId() + "");
                transactionListHolder.card.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        final TextView text = (TextView) v.findViewById(R.id.id);
                        final AlertDialog dialog;

                        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View view = inflater.inflate(R.layout.custom_alert, null);
                        Button editBtn = (Button) view.findViewById(R.id.alert_edit);
                        Button deleteBtn = (Button) view.findViewById(R.id.alert_delete);

                        editBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(context, "editing entry", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, AddTransactionActivity.class);
                                intent.putExtra("id", Integer.parseInt(text.getText().toString()));
                                context.startActivity(intent);
                            }
                        });

                        deleteBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(context, "Entry Deleted!", Toast.LENGTH_SHORT).show();
                                MyTransaction trans = MyTransaction.findById(MyTransaction.class, Integer.parseInt(text.getText().toString()));
                                MyTransaction.delete(trans);
                                AlphaAnimation fadeAnim = new AlphaAnimation(1, 0);
                                fadeAnim.setFillAfter(false);
                                fadeAnim.setDuration(600);
                                fadeAnim.start();

                                transactionListHolder.main.startAnimation(fadeAnim);
                                //refresh content
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        TransactionListActivity.setUpRecycler(TransactionListActivity.mRecyclerView);
                                    }
                                }, 800);
                            }
                        });

                        AlertDialog.Builder alert = new AlertDialog.Builder(context)
                                .setView(view)
                                .setCancelable(true);

                        dialog = alert.create();
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                        return true;
                    }
                });
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {

        switch (listType) {
            case 0:
                switch (position) {
                    case 0:
                        currentHolder = 0;
                        return 0;
                    case 1:
                        currentHolder = 3;
                        return 3;
                    case 2:
                        currentHolder = 1;
                        return 1;
                    case 3:
                        currentHolder = 2;
                        return 2;
                    case 4:
                        currentHolder = 4;
                        return 4;
                }
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;

        }

        return -1;
    }

    @Override
    public int getItemCount() {
        switch (listType) {
            case 0:
                //summary list
                return summaryList.length;
            case 1:
                //calculator cardview
                return calcList.length;
            case 2:
                return notesList.size();
            case 3:
                return transList.size();
        }
        return -1;
    }

    private void setClicks(final Context context, View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        static Context context;
        TextView textView;
        CardView cardView;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            textView = (TextView) itemView.findViewById(R.id.simpleText);
            textView.setClickable(true);
            cardView = (CardView) itemView;
        }

    }

    /*
    TRANSACTIONS VIEWHOLDER
     */

    public static class RecentTransactionViewHolder extends RecyclerView.ViewHolder {
        static Context context;
        TextView title;
        TextView viewAll;
        TextView type;
        TextView value;
        TextView addTrans;
        LinearLayout regLayout;
        RelativeLayout emptyLayout;
        Button tapTransaction;

        public RecentTransactionViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            title = (TextView) itemView.findViewById(R.id.title);
            viewAll = (TextView) itemView.findViewById(R.id.all_trans);
            addTrans = (TextView) itemView.findViewById(R.id.add_trans);
            type = (TextView) itemView.findViewById(R.id.type_text);
            value = (TextView) itemView.findViewById(R.id.value_text);

            regLayout = (LinearLayout) itemView.findViewById(R.id.reg_layout);
            emptyLayout = (RelativeLayout) itemView.findViewById(R.id.empty_layout);
            tapTransaction = (Button) itemView.findViewById(R.id.empty_new_trans);
        }
    }

    /*
    PROGRESS GOAL VIEWHOLDER
     */

    public static class GoalProgressViewHolder extends RecyclerView.ViewHolder {
        static Context context;
        ProgressBar bar;
        TextView title;
        TextView goalText;
        TextView progressText;
        TextView detail;

        public GoalProgressViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            bar = (ProgressBar) itemView.findViewById(R.id.bar);
            title = (TextView) itemView.findViewById(R.id.goal_title);
            goalText = (TextView) itemView.findViewById(R.id.goal_text);
            progressText = (TextView) itemView.findViewById(R.id.goal_progress);
            detail = (TextView) itemView.findViewById(R.id.goal_detail);
        }
    }

    /*
    PIECHART VIEWHOLDER
     */

    public static class PieChartViewHolder extends RecyclerView.ViewHolder {
        static Context context;
        PieChart pie;
        TextView title;

        public PieChartViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            pie = (PieChart) itemView.findViewById(R.id.pie);
            title = (TextView) itemView.findViewById(R.id.title);
            //pie.setLogEnabled(true); for debugging use only
        }

    }


    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        static Context context;
        TextView title;
        TextView body;
        TextView timestamp;
        TextView id;
        RelativeLayout card;
        CardView main;
        Button addNote;

        public NoteViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            title = (TextView) itemView.findViewById(R.id.noteTitle);
            body = (TextView) itemView.findViewById(R.id.noteBody);
            timestamp = (TextView) itemView.findViewById(R.id.time);
            id = (TextView) itemView.findViewById(R.id.id);
            card = (RelativeLayout) itemView.findViewById(R.id.card);
            main = (CardView) itemView.findViewById(R.id.main_card);
            addNote = (Button) itemView.findViewById(R.id.addNoteButton);


        }

    }

    public static class TransactionListHolder extends RecyclerView.ViewHolder {
        static Context context;
        TextView title;
        TextView body;
        TextView detail;
        TextView timestamp;
        TextView id;
        RelativeLayout card;
        CardView main;

        public TransactionListHolder(View itemView) {
            super(itemView);

            context = itemView.getContext();
            title = (TextView) itemView.findViewById(R.id.type_text);
            body = (TextView) itemView.findViewById(R.id.value_text);
            detail = (TextView) itemView.findViewById(R.id.detail);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp);
            id = (TextView) itemView.findViewById(R.id.id);
            card = (RelativeLayout) itemView.findViewById(R.id.card);
            main = (CardView) itemView.findViewById(R.id.main_card);
        }

    }


}

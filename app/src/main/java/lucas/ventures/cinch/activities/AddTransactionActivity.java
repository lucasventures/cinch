package lucas.ventures.cinch.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

import lucas.ventures.cinch.Constants;
import lucas.ventures.cinch.R;
import lucas.ventures.cinch.entities.MyTransaction;
import lucas.ventures.cinch.viewhelpers.TypefaceSpan;

public class AddTransactionActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText enteredValue, enteredDetail;

    private Button save;
    private int chosenButton;
    private boolean typeSelected = false;
    private boolean validValue = false;
    private boolean validDetail = false;
    private boolean isUpdate = false;
    private int id;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_add_transaction);

        enteredValue = (EditText) findViewById(R.id.trans_value);
        enteredDetail = (EditText) findViewById(R.id.trans_detail);
        enteredValue.addTextChangedListener(val);
        enteredDetail.addTextChangedListener(detail);

        save = (Button) findViewById(R.id.save_trans);
        save.setOnClickListener(this);

        spinner = (Spinner) findViewById(R.id.spinner);

        List<String> transList = new ArrayList<String>();
        transList.add("Fixed Expense");
        transList.add("Necessity");
        transList.add("Add to Savings");
        transList.add("Other");

        Toolbar toolbar = (Toolbar) findViewById(R.id.add_toolbar);

        setSupportActionBar(toolbar);

        //make text have spartan typeface aas well as centered
        if (getSupportActionBar() != null) {

            //make title text a font
            SpannableString s = new SpannableString("Add Transaction");
            s.setSpan(new TypefaceSpan(this, "spartan.ttf"), 0, s.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            ActionBar actionBar = getSupportActionBar();
            View view = getLayoutInflater().inflate(R.layout.centered_title, null);
            TextView title = (TextView) view.findViewById(R.id.centered_title);
            title.setText(s);

            ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER);

            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setIcon(R.color.transparent);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setCustomView(view, params);
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, transList);


        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0://fixed
                        typeSelected = true;
                        chosenButton = position;
                        break;
                    case 1://necessity
                        typeSelected = true;
                        chosenButton = position;
                        break;
                    case 2://add to savings
                        typeSelected = true;
                        chosenButton = position;
                        break;
                    case 3://other type of transaction
                        typeSelected = true;
                        chosenButton = position;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Intent intent = getIntent();
        if (intent.getIntExtra("id", 0) != 0) {
            //flip switch
            isUpdate = true;
            //extract id
            id = intent.getIntExtra("id", 0);
            //use the id in order to

            MyTransaction trans = MyTransaction.findById(MyTransaction.class, id);
            enteredValue.setText(trans.getValue());
            enteredDetail.setText(trans.getDetail());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_trans:
                if (typeSelected & validValue & validDetail) {

                    //chosen button value indicates type ie 1 = cc, 2 = xx ;
                    String type = null;
                    switch (chosenButton) {
                        case 0:
                            type = Constants.TYPE_FIXED;
                            break;
                        case 1:
                            type = Constants.TYPE_NECESSITY;
                            break;
                        case 2:
                            type = Constants.TYPE_SAVINGS;
                            break;
                        case 3:
                            type = Constants.TYPE_EXTRAS;
                            break;
                    }

                    if (isUpdate) {
                        //is an update

                        updateTrans(type
                                , valueFilter(enteredValue.getText().toString())
                                , enteredDetail.getText().toString());
                    } else {

                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                        //month should be in 0-11 range;
                        int month = prefs.getInt("month",1);
                        int year = prefs.getInt("year",2016);
                        int day = prefs.getInt("day",1);

                        //is a new transaction
                        String transTYPE = type;
                        String detail = enteredDetail.getText().toString();

                        MyTransaction transaction = new MyTransaction(transTYPE, valueFilter(enteredValue.getText().toString()), detail,day,month,year);
                        Log.d("addtransactionactivity", "saving values: " + type + enteredValue.getText().toString() + detail);
                        MyTransaction.save(transaction);
                    }

                    AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                            .setMessage("Your Transaction has been saved. Would you like to enter another one?")
                            .setTitle("SAVED!")
                            .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    typeSelected = false;
                                    save.setEnabled(false);
                                }
                            })
                            .setNegativeButton("no", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent next = new Intent(AddTransactionActivity.this, MainActivity.class);
                                    next.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivity(next);
                                    finish();
                                }
                            });
                    dialog.create();
                    dialog.show();
                }
                break;

            default:
                Toast.makeText(
                        AddTransactionActivity.this
                        , "something went horribly wrong... :("
                        , Toast.LENGTH_SHORT).show();
        }

    }

    private TextWatcher val = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            //is string empty?
            if (s.toString().length() > 0) {
                //is it a whole number?
                if (Double.parseDouble(s.toString()) % 1 == 0) {
                    // value is a whole number
                    validValue = true;
                } else {
                    //if a decimal goes beyond the hundredths place, disable saving function and prompt warning
                    int period = s.toString().indexOf(".");
                    int places = s.toString().length() - (period + 1);
                    if (places > 2) {
                        Toast.makeText(AddTransactionActivity.this, "Please keep decimals into the hundredths place", Toast.LENGTH_LONG).show();
                        validValue = false;
                    } else {
                        validValue = true;
                    }
                }
            }

        }
    };

    private TextWatcher detail = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().length() == 0) {
                Toast.makeText(AddTransactionActivity.this, "Please add a detail about this transaction.", Toast.LENGTH_SHORT).show();
                validDetail = false;
                save.setEnabled(false);
            } else {
                save.setEnabled(true);
                validDetail = true;
            }
        }
    };


    private String valueFilter(String val) {
        String filtered = null;

        if (!val.contains(".")) {
            filtered = val + ".00";
        } else {
            return val;
        }

        return filtered;
    }

    private void updateTrans(String type, String value, String desc) {
        Log.d("addtransactionsactivity", "updateTrans: ");

        MyTransaction transaction = SugarRecord.findById(MyTransaction.class, id);
        transaction.mValue = value;
        transaction.mDetail = desc;
        transaction.mCategory = type;
        MyTransaction.save(transaction);
        Toast.makeText(AddTransactionActivity.this, "Transaction Updated", Toast.LENGTH_SHORT).show();

    }

}

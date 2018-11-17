package lucas.ventures.cinch.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ventures.cinch.R;

import lucas.ventures.cinch.Constants;

public class SimpleStartActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "SimpleStartActivity";
    // @BindView(R.id.name)
    private EditText name;
    // @BindView(R.id.monthly)
    private EditText monthly;
    private EditText expenses;
    //@BindView(R.id.savings)
    private EditText savings;
    //@BindView(R.id.weekly)
    // private EditText budget;
    //@BindView(R.id.next)
    private Button nextButton;
    private boolean nValid = false;
    private boolean mValid = false;
    private boolean eValid = false;
    private boolean sValid = false;

    private double dInc;
    private double dExp;
    private double dSav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getInt("PIN", 0) != 0) {
            //pin has already been set up, meaning so have the initial budget values
            //skip this activity
            Intent intent = new Intent(SimpleStartActivity.this, MainActivity.class);
            intent.putExtra("class", 0);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }//else pin has not been set up


        //set layout
        setContentView(R.layout.activity_simple_start);

        name = (EditText) findViewById(R.id.name);
        monthly = (EditText) findViewById(R.id.monthly);
        expenses = (EditText) findViewById(R.id.monthly_exp);
        savings = (EditText) findViewById(R.id.savings);
        nextButton = (Button) findViewById(R.id.next);

        nextButton.setOnClickListener(this);

        TextView lets = (TextView) findViewById(R.id.lets);
        lets.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/spartan.ttf"));
        lets.setTextSize(30f);

        name.addTextChangedListener(nameWatcher);
        monthly.addTextChangedListener(incWatcher);
        expenses.addTextChangedListener(expWatcher);
        savings.addTextChangedListener(saveWatcher);

    }

    @Override
    public void onClick(View v) {
        //all edit texts have a value!
        Log.d(TAG, "onClick: nValid:" + nValid);
        Log.d(TAG, "onClick: mValid:" + mValid);
        Log.d(TAG, "onClick: eValid:" + eValid);
        Log.d(TAG, "onClick: sValid:" + sValid);

        if (nValid & mValid & eValid & sValid) {

            //check if these values make sense

            double inco = Double.valueOf(valueFilter(monthly.getText().toString()));
            double expe = Double.valueOf(valueFilter(expenses.getText().toString()));
            double savi = Double.valueOf(valueFilter(savings.getText().toString()));
            Log.d(TAG, "onClick: income:" + inco + ", expenses:" + expe + ", save:" + savi);

            if (expe + savi > inco) {

                monthly.setError("Hm...");
                expenses.setError("Somethings not right here.");
                savings.setError("Somethings not right here.");

                expenses.getError();
                monthly.getError();
                savings.getError();
            } else {

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = prefs.edit();

                editor.putString(Constants.NAME, name.getText().toString()).apply();

                editor.putString(Constants.MONTHLY, valueFilter(monthly.getText().toString())).apply();

                editor.putString(Constants.FIXED, valueFilter(expenses.getText().toString())).apply();

                editor.putString(Constants.SAVINGS, valueFilter(savings.getText().toString())).apply();

                double budget = Double.parseDouble(monthly.getText().toString())
                        - Double.parseDouble(expenses.getText().toString())
                        - Double.parseDouble(savings.getText().toString());

                editor.putString(Constants.BUDGET, String.valueOf(budget)).apply();

                Intent intent = new Intent(SimpleStartActivity.this, SecurityActivity.class);
                intent.putExtra("class", 0);
                startActivity(intent);
            }

        } else {
            //at least one value is invalid. find that invalid value & "getError():" the edittext
            Toast.makeText(SimpleStartActivity.this, "Please fill out all fields realistically.", Toast.LENGTH_SHORT).show();
            if (!nValid) {
                name.setError("Missing!");
                name.getError();
            }
            if (!mValid) {
                monthly.setError("Missing!");
                monthly.getError();

            }
            if (!eValid) {
                expenses.setError("Missing!");
                expenses.getError();
            }
            if (!sValid) {
                savings.setError("Missing!");
                savings.getError();
            }
        }
    }

    SharedPreferences.Editor putDouble(final SharedPreferences.Editor edit, final String key, final double value) {
        return edit.putLong(key, Double.doubleToRawLongBits(value));
    }

    private TextWatcher nameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() >= 1) {
                nValid = true;
            } else {
                nValid = false;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher incWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            //is string empty?
            if (s.toString().length() > 0) {
                //is it a whole number?
                if (Double.parseDouble(s.toString()) % 1 == 0) {
                    // value is a whole number
                    mValid = true;
                } else {
                    //if a decimal goes beyond the hundredths place, disable saving function and prompt warning
                    int period = s.toString().indexOf(".");
                    int places = s.toString().length() - (period + 1);
                    if (places > 2) {
                        Toast.makeText(SimpleStartActivity.this, "Please keep decimals into the hundredths place", Toast.LENGTH_LONG).show();
                        mValid = false;
                    } else {
                        mValid = true;
                    }
                }
            }else{
                mValid = false;
            }


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher expWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            //is string empty?
            if (s.toString().length() > 0) {
                //is it a whole number?
                if (Double.parseDouble(s.toString()) % 1 == 0) {
                    // value is a whole number
                    eValid = true;
                } else {
                    //if a decimal goes beyond the hundredths place, disable saving function and prompt warning
                    int period = s.toString().indexOf(".");
                    int places = s.toString().length() - (period + 1);
                    if (places > 2) {
                        Toast.makeText(SimpleStartActivity.this, "Please keep decimals into the hundredths place", Toast.LENGTH_LONG).show();
                        eValid = false;
                    } else {
                        eValid = true;
                    }
                }
            }else{
                eValid = false;
            }
        }


        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher saveWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //is string empty?
            if (s.toString().length() > 0) {
                //is it a whole number?
                if (Double.parseDouble(s.toString()) % 1 == 0) {
                    // value is a whole number
                    sValid = true;
                } else {
                    //if a decimal goes beyond the hundredths place, disable saving function and prompt warning
                    int period = s.toString().indexOf(".");
                    int places = s.toString().length() - (period + 1);
                    if (places > 2) {
                        Toast.makeText(SimpleStartActivity.this, "Please keep decimals into the hundredths place", Toast.LENGTH_LONG).show();
                        sValid = false;
                    } else {
                        sValid = true;
                    }
                }
            }else{
                sValid = false;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

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

}

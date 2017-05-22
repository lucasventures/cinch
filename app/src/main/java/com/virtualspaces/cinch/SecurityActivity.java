package com.virtualspaces.cinch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SecurityActivity extends AppCompatActivity {

    private static final String TAG = "SECURITY ACTIVITY";
    private ImageView one, two, three, four, lock;
    private ImageButton enter;
    private TextView title;
    private EditText pin, pin_one;

    private int failedEntries = 0;

    private int fromInt;

    //from splash == 4 meaning "just put in pin to enter"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);

        int whichClass = getIntent().getIntExtra("class", 0);

        switch (whichClass) {
            case 0:
                fromInt = 0;
                initUI(fromInt);
            case 1:
                fromInt = whichClass;
                initUI(whichClass);
                break;
            case 2:
                fromInt = whichClass;
                initUI(whichClass);
                break;
            case 3:
                fromInt = whichClass;
                initUI(whichClass);
                break;
            case 4:
                fromInt = whichClass;
                initUI(whichClass);
                break;
            default:
                fromInt = 0;
                initUI(fromInt);
        }

        //color the status bar if >= lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    private void initUI(int i) {

        one = (ImageView) findViewById(R.id.pin_one);
        two = (ImageView) findViewById(R.id.pin_two);
        three = (ImageView) findViewById(R.id.pin_three);
        four = (ImageView) findViewById(R.id.pin_four);

        pin_one = (EditText) findViewById(R.id.enter_pin_one);

        lock = (ImageView) findViewById(R.id.lock);

        enter = (ImageButton) findViewById(R.id.enter);

        enter.setVisibility(View.INVISIBLE);

        title = (TextView) findViewById(R.id.pin_text);
        Typeface typeface1 = Typeface.createFromAsset(getAssets(), "fonts/spartan.ttf");
        title.setTypeface(typeface1);
        title.setAllCaps(true);
        pin = (EditText) findViewById(R.id.enter_pin);

        switch (i) {
            case 0:
                //pin doesn't exist
                Log.d(TAG, "initUI: pin does not yet exist");
                Log.d(TAG, "configurePinUI(); called");
                configurePinUI();

                break;
            case 1:
                //pin exists, from
                pin.addTextChangedListener(normWatcher);
                pin_one.setVisibility(View.GONE);

                Log.d(TAG, "initUI: pin dexists");
                Log.d(TAG, "norm watcher set. submit pin onClick");
                break;

            case 2:
                pin.addTextChangedListener(normWatcher);
                pin_one.setVisibility(View.GONE);


                Log.d(TAG, "initUI: pin dexists");
                Log.d(TAG, "norm watcher set. submit pin onClick");
                break;
            case 3: //this is a reset of the pin
                Log.d(TAG, "initUI: pin dexists");
                Log.d(TAG, "norm watcher set. submit pin onClick");
                changePin();

        }
    }

    private void changePin() {

        Log.d(TAG, "resetWatcher set. submit pin onClick");
        pin.addTextChangedListener(resetWatcher);
        pin_one.setVisibility(View.INVISIBLE);

    }

    private TextWatcher resetWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0) {
                lock.setVisibility(View.VISIBLE);
                one.setVisibility(View.INVISIBLE);
                two.setVisibility(View.INVISIBLE);
                three.setVisibility(View.INVISIBLE);
                four.setVisibility(View.INVISIBLE);

                enter.setVisibility(View.INVISIBLE);

            } else if (s.length() == 1) {
                lock.setVisibility(View.INVISIBLE);
                one.setVisibility(View.VISIBLE);
                two.setVisibility(View.INVISIBLE);
                three.setVisibility(View.INVISIBLE);
                four.setVisibility(View.INVISIBLE);

                enter.setVisibility(View.INVISIBLE);

            } else if (s.length() == 2) {
                lock.setVisibility(View.INVISIBLE);
                one.setVisibility(View.VISIBLE);
                two.setVisibility(View.VISIBLE);
                three.setVisibility(View.INVISIBLE);
                four.setVisibility(View.INVISIBLE);

                enter.setVisibility(View.INVISIBLE);

            } else if (s.length() == 3) {
                lock.setVisibility(View.INVISIBLE);
                one.setVisibility(View.VISIBLE);
                two.setVisibility(View.VISIBLE);
                three.setVisibility(View.VISIBLE);
                four.setVisibility(View.INVISIBLE);

                enter.setVisibility(View.INVISIBLE);

            } else if (s.length() == 4) {
                lock.setVisibility(View.INVISIBLE);
                one.setVisibility(View.VISIBLE);
                two.setVisibility(View.VISIBLE);
                three.setVisibility(View.VISIBLE);
                four.setVisibility(View.VISIBLE);

                enter.setVisibility(View.VISIBLE);

                enter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        submitPin(pin.getText().toString());

                    }
                });
            }


        }

    };

    private void configurePinUI() {
        //casn operate as recinfiguring spin
        Log.d(TAG, "configurePin() beginning.");

        title.setText("Set your new PIN");
        pin_one.setVisibility(View.VISIBLE);
        pin_one.addTextChangedListener(confWatcher);


    }


    private TextWatcher confWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0) {
                lock.setVisibility(View.VISIBLE);
                one.setVisibility(View.INVISIBLE);
                two.setVisibility(View.INVISIBLE);
                three.setVisibility(View.INVISIBLE);
                four.setVisibility(View.INVISIBLE);

                pin.setVisibility(View.INVISIBLE);

            } else if (s.length() == 1) {
                lock.setVisibility(View.INVISIBLE);
                one.setVisibility(View.VISIBLE);
                two.setVisibility(View.INVISIBLE);
                three.setVisibility(View.INVISIBLE);
                four.setVisibility(View.INVISIBLE);
                pin.setVisibility(View.INVISIBLE);

            } else if (s.length() == 2) {
                lock.setVisibility(View.INVISIBLE);
                one.setVisibility(View.VISIBLE);
                two.setVisibility(View.VISIBLE);
                three.setVisibility(View.INVISIBLE);
                four.setVisibility(View.INVISIBLE);
                pin.setVisibility(View.INVISIBLE);

            } else if (s.length() == 3) {
                lock.setVisibility(View.INVISIBLE);
                one.setVisibility(View.VISIBLE);
                two.setVisibility(View.VISIBLE);
                three.setVisibility(View.VISIBLE);
                four.setVisibility(View.INVISIBLE);
                pin.setVisibility(View.INVISIBLE);
            } else if (s.length() == 4) {
                lock.setVisibility(View.INVISIBLE);
                one.setVisibility(View.VISIBLE);
                two.setVisibility(View.VISIBLE);
                three.setVisibility(View.VISIBLE);
                four.setVisibility(View.VISIBLE);

                pin.setVisibility(View.VISIBLE);
                pin.addTextChangedListener(confWatcher2);


            }
        }
    };

    private TextWatcher confWatcher2 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0) {
                lock.setVisibility(View.VISIBLE);
                one.setVisibility(View.INVISIBLE);
                two.setVisibility(View.INVISIBLE);
                three.setVisibility(View.INVISIBLE);
                four.setVisibility(View.INVISIBLE);


            } else if (s.length() == 1) {
                lock.setVisibility(View.INVISIBLE);
                one.setVisibility(View.VISIBLE);
                two.setVisibility(View.INVISIBLE);
                three.setVisibility(View.INVISIBLE);
                four.setVisibility(View.INVISIBLE);

            } else if (s.length() == 2) {
                lock.setVisibility(View.INVISIBLE);
                one.setVisibility(View.VISIBLE);
                two.setVisibility(View.VISIBLE);
                three.setVisibility(View.INVISIBLE);
                four.setVisibility(View.INVISIBLE);

            } else if (s.length() == 3) {
                lock.setVisibility(View.INVISIBLE);
                one.setVisibility(View.VISIBLE);
                two.setVisibility(View.VISIBLE);
                three.setVisibility(View.VISIBLE);
                four.setVisibility(View.INVISIBLE);
            } else if (s.length() == 4) {
                lock.setVisibility(View.INVISIBLE);
                one.setVisibility(View.VISIBLE);
                two.setVisibility(View.VISIBLE);
                three.setVisibility(View.VISIBLE);
                four.setVisibility(View.VISIBLE);

                enter.setVisibility(View.VISIBLE);

                enter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "enter clicked");

                        if (pin.getText().toString().equals(pin_one.getText().toString())) {

                            savePin(Integer.parseInt(pin.getText().toString()));

                            Intent intent = new Intent(pin.getContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(SecurityActivity.this, "Pins do not match.", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

            }
        }
    };

    private TextWatcher normWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0) {
                lock.setVisibility(View.INVISIBLE);
                one.setVisibility(View.INVISIBLE);
                two.setVisibility(View.INVISIBLE);
                three.setVisibility(View.INVISIBLE);
                four.setVisibility(View.INVISIBLE);
                enter.setVisibility(View.INVISIBLE);


            } else if (s.length() == 1) {
                lock.setVisibility(View.VISIBLE);
                lock.setAlpha(0.25f);
                one.setVisibility(View.VISIBLE);
                two.setVisibility(View.INVISIBLE);
                three.setVisibility(View.INVISIBLE);
                four.setVisibility(View.INVISIBLE);
                enter.setVisibility(View.INVISIBLE);

            } else if (s.length() == 2) {
                lock.setVisibility(View.VISIBLE);
                lock.setAlpha(0.5f);
                one.setVisibility(View.VISIBLE);
                two.setVisibility(View.VISIBLE);
                three.setVisibility(View.INVISIBLE);
                four.setVisibility(View.INVISIBLE);
                enter.setVisibility(View.INVISIBLE);

            } else if (s.length() == 3) {
                lock.setVisibility(View.VISIBLE);
                lock.setAlpha(0.75f);
                one.setVisibility(View.VISIBLE);
                two.setVisibility(View.VISIBLE);
                three.setVisibility(View.VISIBLE);
                four.setVisibility(View.INVISIBLE);
                enter.setVisibility(View.INVISIBLE);
            } else if (s.length() == 4) {
                lock.setVisibility(View.VISIBLE);
                lock.setAlpha(1f);
                one.setVisibility(View.VISIBLE);
                two.setVisibility(View.VISIBLE);
                three.setVisibility(View.VISIBLE);
                four.setVisibility(View.VISIBLE);
                enter.setVisibility(View.VISIBLE);

                enter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        submitPin(pin.getText().toString());

                    }
                });

            }
        }
    };

    private void submitPin(String pin) {
        Log.d(TAG, "submitPin() beginning now with value: " + pin);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int savedPin = prefs.getInt("pin", 0);
        if (savedPin == Integer.parseInt(pin)) {
            switch (fromInt) {
                case 0:
                    Toast.makeText(this, "new pin", Toast.LENGTH_LONG).show();

                    break;
                case 1://from main activity
                    Log.d(TAG, "valid PIN: back to mainactivity.");
                    Log.d(TAG, "norm watcher set. submit pin onClick");
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    break;
                case 2://from transaction list
                    Log.d(TAG, "valid PIN: back to transactionlistactivity.");

                    Intent intent2 = new Intent(this, TransactionListActivity.class);
                    startActivity(intent2);
                    break;
                case 3:
                    Log.d(TAG, "submitPin: configurePinUI called. PIN RESET UNDERWAY");
                    configurePinUI();
                    break;
            }
        } else {
            failedEntries++;
            if (failedEntries == 5) {
                // TODO: VVVV
                //make app unopenable for 24hrs
                System.currentTimeMillis();
                Toast.makeText(this, "APP IS NOW LOCKED FOR 24 HOURS (not really, yet tho)", Toast.LENGTH_LONG).show();
            }
            Toast.makeText(SecurityActivity.this, "PINS DO NOT MATCH", Toast.LENGTH_SHORT).show();
            pin_one.setText("");
            this.pin.setText("");
        }
    }

    private void savePin(int pin) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Constants.PIN, pin).apply();
        Log.d(TAG, "New PIN has been saved.");

    }

}

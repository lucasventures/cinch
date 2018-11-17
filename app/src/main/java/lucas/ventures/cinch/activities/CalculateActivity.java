package lucas.ventures.cinch.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ventures.cinch.R;

public class CalculateActivity extends AppCompatActivity {

    private final int SAVINGS = 1;
    private final int GRATUITY = 2;
    private final int COMPOUND = 3;
    private final int APR = 4;
    private final int RENT = 5;
    private final int MORTGAGE = 6;
    private final int CAR = 7;
    private final int CUSTOM = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);
        //toolbar setup
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //calc chooser
        Bundle bundle = getIntent().getExtras();
        int calc = bundle.getInt("calcutor");
        switch (calc) {
            case SAVINGS:
                break;
            case GRATUITY:
                break;
            case COMPOUND:
                break;
            case APR:
                break;
            case RENT:
                break;
            case MORTGAGE:
                break;
            case CAR:
                break;
            case CUSTOM:
                break;
        }
    }
}

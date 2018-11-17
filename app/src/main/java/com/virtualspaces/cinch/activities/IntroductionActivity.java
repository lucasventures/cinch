package com.virtualspaces.cinch.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.ToxicBakery.viewpager.transforms.DepthPageTransformer;
import com.virtualspaces.cinch.R;
import com.virtualspaces.cinch.adapters.MyViewPagerAdapter;
import com.virtualspaces.cinch.fragments.FinanceEntryFragment;

import java.util.ArrayList;


public class IntroductionActivity extends AppCompatActivity {
    public static final String STRINGKEY = "titles";

    private ViewPager pager;
    private ImageView one;
    private ImageView two;
    private ImageView three;
    private ImageView four;
    //private ImageView five;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financial_calibration);

        ArrayList<FinanceEntryFragment> fragments = new ArrayList<>();

        // fragment parameters are defined in FinanceEntryFragment class
        FinanceEntryFragment cinchFrag = configureFragment(0);
        FinanceEntryFragment manageFrag = configureFragment(1);
        FinanceEntryFragment budgetFrag = configureFragment(2);
        FinanceEntryFragment saveFrag = configureFragment(3);
        //FinanceEntryFragment startFrag = configureFragment(4);

        //add fragment to fragment list
        fragments.add(cinchFrag);
        fragments.add(manageFrag);
        fragments.add(budgetFrag);
        fragments.add(saveFrag);

        //pager dots
        one = findViewById(R.id.dot_one);
        two = findViewById(R.id.dot_two);
        three = findViewById(R.id.dot_three);
        four = findViewById(R.id.dot_four);

        //setup pager
        pager = (ViewPager) findViewById(R.id.finPager);
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager(), fragments);
        pager.setAdapter(adapter);
        pager.setPageTransformer(true, new DepthPageTransformer());

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        one.setImageResource(R.drawable.pager_circle_sel);
                        two.setImageResource(R.drawable.pager_circle_not_sel);
                        break;
                    case 1:
                        one.setImageResource(R.drawable.pager_circle_not_sel);
                        two.setImageResource(R.drawable.pager_circle_sel);
                        three.setImageResource(R.drawable.pager_circle_not_sel);
                        break;
                    case 2:
                        two.setImageResource(R.drawable.pager_circle_not_sel);
                        three.setImageResource(R.drawable.pager_circle_sel);
                        four.setImageResource(R.drawable.pager_circle_not_sel);
                        break;
                    case 3:
                        three.setImageResource(R.drawable.pager_circle_not_sel);
                        four.setImageResource(R.drawable.pager_circle_sel);
                        break;
                    case 4:
                        //four.setImageResource(R.drawable.pager_circle_not_sel);
                        //five.setImageResource(R.drawable.pager_circle_sel);
                        break;
                    case 5:
                        //three.setImageResource(R.drawable.pager_circle_not_sel);
                        //four.setImageResource(R.drawable.pager_circle_sel);
                        break;
                    default:
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private FinanceEntryFragment configureFragment(int titleInt) {
        //create new fragment for page
        FinanceEntryFragment frag = new FinanceEntryFragment();
        //create bundle for args
        Bundle nameBundle = new Bundle();
        //place args into bundle
        nameBundle.putInt(STRINGKEY, titleInt);
        //set fragment with bundle that contains arguments
        frag.setArguments(nameBundle);
        return frag;
    }
}

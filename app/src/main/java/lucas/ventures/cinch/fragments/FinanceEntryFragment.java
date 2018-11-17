package lucas.ventures.cinch.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import lucas.ventures.cinch.R;
import lucas.ventures.cinch.activities.IntroductionActivity;
import lucas.ventures.cinch.activities.SimpleStartActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class FinanceEntryFragment extends Fragment {

    private RelativeLayout background;
    private Button startbutton;
    private ImageView centerImage;

    public FinanceEntryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_finance_entry, container, false);
        Bundle args = getArguments();

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/spartan.ttf");
        TextView textView = (TextView) view.findViewById(R.id.title);
        textView.setTypeface(typeface);

        centerImage = (ImageView) view.findViewById(R.id.center_art);

        startbutton = (Button) view.findViewById(R.id.start_btn);

        background = (RelativeLayout) view.findViewById(R.id.beach);
        int title = args.getInt(IntroductionActivity.STRINGKEY);
        initializeFragment(title, textView);
        return view;
    }

    private void initializeFragment(int title, TextView name) {
        switch (title) {
            case 0:
                name.setText("Easily manage your cash.");
                startbutton.setVisibility(View.GONE);
                centerImage.setImageDrawable(getResources().getDrawable(R.drawable.cashy));
                background.setBackground(getResources().getDrawable(R.drawable.intro_two));
                break;
            case 1:
                name.setText("Create a simple budget.");
                startbutton.setVisibility(View.GONE);
                centerImage.setImageDrawable(getResources().getDrawable(R.drawable.simplebudget));
                background.setBackground(getResources().getDrawable(R.drawable.intro_three));
                break;
            case 2:
                name.setText("Start saving some coin.");
                startbutton.setVisibility(View.GONE);
                centerImage.setImageDrawable(getResources().getDrawable(R.drawable.coinage));
                background.setBackground(getResources().getDrawable(R.drawable.intro_four));
                break;
            case 3:
                name.setText("cinch");
                name.setTextSize(40f);
                centerImage.setImageDrawable(getResources().getDrawable(R.drawable.cinch));
                centerImage.setPadding(0, 0, 0, 0);

                startbutton.setVisibility(View.VISIBLE);
                startbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), SimpleStartActivity.class);
                        startActivity(intent);
                    }
                });
                break;
            case 5:
                name.setText("hmm, that's odd.");
                background.setBackgroundColor(Color.RED);
                break;
            default:

        }
    }

}

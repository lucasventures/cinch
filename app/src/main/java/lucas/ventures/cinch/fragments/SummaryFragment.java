package lucas.ventures.cinch.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;

import com.ventures.cinch.R;

import lucas.ventures.cinch.activities.AddTransactionActivity;
import lucas.ventures.cinch.adapters.RecyclerViewAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class SummaryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private int type;
    private FloatingActionButton fab;
    private SwipeRefreshLayout mRefresh;
    private RecyclerView recyclerView;
    private Handler handler;

    public SummaryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_summary, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.summary_recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        // 0 value indicates that adapter is for summary fragment
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(0, getActivity());
        recyclerView.setAdapter(adapter);

        mRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        mRefresh.setOnRefreshListener(this);

        fab = (FloatingActionButton) view.findViewById(R.id.fabSummary);

        handler = new Handler();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddTransactionActivity.class);
                // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        AlphaAnimation alpha = new AlphaAnimation(0f, 1f);
        alpha.setInterpolator(new LinearInterpolator());
        alpha.setDuration(400);
        alpha.setFillAfter(true);
        alpha.start();
        recyclerView.setLayoutAnimation(new LayoutAnimationController(alpha));

        return view;
    }

    public int getType() {
        return type;
    }

    @Override
    public void onRefresh() {

        mRefresh.setRefreshing(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        // 0 parameter indicates that adapter is for summary fragment
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(0, getActivity());
        recyclerView.setAdapter(adapter);

        AlphaAnimation alpha = new AlphaAnimation(0f, 1f);
        alpha.setInterpolator(new LinearInterpolator());
        alpha.setDuration(400);
        alpha.setFillAfter(true);
        alpha.start();
        recyclerView.setLayoutAnimation(new LayoutAnimationController(alpha));

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefresh.setRefreshing(false);
            }
        }, 2000);

    }
}

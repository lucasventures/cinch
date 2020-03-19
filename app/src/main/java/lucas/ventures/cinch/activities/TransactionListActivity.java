package lucas.ventures.cinch.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lucas.ventures.cinch.R;
import lucas.ventures.cinch.adapters.RecyclerViewAdapter;
import lucas.ventures.cinch.entities.MyTransaction;

public class TransactionListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private FloatingActionButton fab;
    public static RecyclerView mRecyclerView;
    private ArrayList<MyTransaction> list;
    private SwipeRefreshLayout swipeRefresh;
    private Handler mHandler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);
        //refresh layout setup
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.trans_swipe);
        swipeRefresh.setOnRefreshListener(this);

        //recycler view setup
        mRecyclerView = (RecyclerView) findViewById(R.id.transaction_recycler);
       setUpRecycler(mRecyclerView);

        // action on fab clickz
        fab = (FloatingActionButton) findViewById(R.id.trans_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionListActivity.this, AddTransactionActivity.class);
                startActivity(intent);
            }
        });

        //for post delayed
        mHandler = new Handler();
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }


    @Override
    public void onRefresh() {
        swipeRefresh.setRefreshing(true);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(false);

            }
        }, 2000);
        TransactionListActivity.setUpRecycler(mRecyclerView);
    }

    public static void setUpRecycler(RecyclerView recyclerView) {
        ArrayList<MyTransaction> transactions = new ArrayList<>();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(recyclerView.getContext());
        int month = prefs.getInt("month",0);
        int year = prefs.getInt("year",2016);

        List<MyTransaction> filteredTrans = MyTransaction.listAll(MyTransaction.class);

        for(MyTransaction tr : filteredTrans){
            if(tr.getMonth() == month & tr.getYear() ==year){
                transactions.add(tr);
                Log.d("TransactionListActivity", "Transactions in list: "+tr.getValue()+", type:"+tr.getMonth());

            }else{
                Log.d("TransactionListActivity", "Transactions not in list: "+tr.getValue()+", type:"+tr.getMonth());
            }
        }


        //setup recyclerview + adapter here
        if(transactions.size()>0){
            Collections.reverse(transactions);
            //set layout manager
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(
                    new LinearLayoutManager(recyclerView.getContext(),LinearLayoutManager.VERTICAL,false));
            //set adapter
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(3,recyclerView.getContext(), transactions,true);
            recyclerView.setAdapter(adapter);
        }else{
            //list is empty
            recyclerView.setVisibility(View.INVISIBLE);
            Toast.makeText(recyclerView.getContext(),"Add a note!",Toast.LENGTH_SHORT).show();
        }

    }
}

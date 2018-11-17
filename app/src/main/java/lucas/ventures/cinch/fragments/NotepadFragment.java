package lucas.ventures.cinch.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.ventures.cinch.R;
import com.ventures.cinch.entities.Note;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lucas.ventures.cinch.activities.NotepadActivity;
import lucas.ventures.cinch.adapters.RecyclerViewAdapter;


/**
 * A simple {@link Fragment} subclass.
 */


public class NotepadFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private FloatingActionButton noteFab;
    public static RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Button addNoteButton;

    public NotepadFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate + init
        View view = inflater.inflate(R.layout.fragment_notepad, container, false);
        //ButterKnife.bind(this, view);
        addNoteButton = (Button) view.findViewById(R.id.addNoteButton);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            addNoteButton.setElevation(0f);
        }
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.notes_swiper);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_notepad);


        noteFab = (FloatingActionButton) view.findViewById(R.id.fabNote);

        noteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NotepadActivity.class);
                startActivity(intent);
            }
        });

        ArrayList<Note> notesList = new ArrayList<>();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int month = prefs.getInt("month",0);
        int year = prefs.getInt("year",2016);

        try {
            List<Note> notes = (ArrayList<Note>) Note.listAll(Note.class);
            for(Note note : notes){
                if(note.getMonth() == month & note.getYear() ==year)
                notesList.add(note);
            }
        } catch (SQLException e) {
            Log.d("NotePadFragment", "onCreateView: " + e.getMessage());
        }

        //if null, button view is visible
        if (notesList.size() >= 1) {

            addNoteButton.setVisibility(View.GONE);
            setUpRecycler(notesList, mRecyclerView);

        } else {
            addNoteButton.setVisibility(View.VISIBLE);
            //Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(),Constants.QUICKSAND_REG);
            addNoteButton.setTypeface(Typeface.DEFAULT_BOLD);
            addNoteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //add note!
                    Intent intent = new Intent(getActivity(), NotepadActivity.class);
                    startActivity(intent);
                }
            });
        }
        return view;
    }

    public static void setUpRecycler(ArrayList<Note> notes, RecyclerView recyclerView) {
        //setup recyclerview + adapter here
        Collections.reverse(notes);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(2, recyclerView.getContext(), notes);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());


        ArrayList<Note> notesList = new ArrayList<>();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int month = prefs.getInt("month",0);
        int year = prefs.getInt("year",2016);

        try {
            List<Note> notes = (ArrayList<Note>) Note.listAll(Note.class);
            for(Note note : notes){
                if(note.getMonth() == month & note.getYear() ==year)
                    notesList.add(note);
            }
        } catch (SQLException e) {
            Log.d("NotePadFragment", "onCreateView: " + e.getMessage());
        }


        //if null, button view is visible
        if (notesList.size() >= 1) {

            addNoteButton.setVisibility(View.GONE);
            setUpRecycler((ArrayList<Note>) notesList, mRecyclerView);
        } else {
            addNoteButton.setVisibility(View.VISIBLE);
            addNoteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //add note!
                    Intent intent = new Intent(getActivity(), NotepadActivity.class);
                    startActivity(intent);
                }
            });
            Toast.makeText(getActivity(), "Add a note!", Toast.LENGTH_LONG).show();
            mSwipeRefreshLayout.setRefreshing(false);
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }
}

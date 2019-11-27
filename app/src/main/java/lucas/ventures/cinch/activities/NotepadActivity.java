package lucas.ventures.cinch.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import lucas.ventures.cinch.R;
import lucas.ventures.cinch.entities.Note;
import lucas.ventures.cinch.viewhelpers.TypefaceSpan;

/*
this activity if for creating notepad objects
 */
public class NotepadActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private EditText title, body;
    private Button save;
    private Toolbar mToolbar;
    private boolean isUpdate = false;
    private int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        Intent intent = getIntent();

        //will be true if updating a note
        if (intent.getIntExtra("id", 0) != 0) {
            //flip switch
            isUpdate = true;
            //extract id
            id = intent.getIntExtra("id", 0);
            //use the id in order to
        }

        mToolbar = (Toolbar) findViewById(R.id.note_bar);
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {

            //make title text a font
            SpannableString s = new SpannableString("Add A Note");
            s.setSpan(new TypefaceSpan(this, "spartan.ttf"), 0, s.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            //
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

        title = (EditText) findViewById(R.id.titlenote);
        body = (EditText) findViewById(R.id.bodynote);
        title.addTextChangedListener(this);
        body.addTextChangedListener(this);

        save = (Button) findViewById(R.id.save_note);
        save.setEnabled(false);
        save.setOnClickListener(this);
        
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if ((title.getText().toString().length() > 0) & (body.getText().toString().length() > 0)) {
            save.setEnabled(true);
        } else {
            save.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {

        if (isUpdate) {
            //update note
            updateNote(title.getText().toString(),
                    body.getText().toString());

        } else {
            //is new note
            addNote(title.getText().toString(), body.getText().toString());
        }
    }

    private void addNote(String noteTitle, String noteDetail) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int day = prefs.getInt("day",1);
        int month = prefs.getInt("month",1);
        int year = prefs.getInt("year",2016);

        Note note = new Note(noteTitle, noteDetail,day,month,year);
        note.save();

        //alert for confirmation
        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setMessage("Your note has been saved. Would you like to write another one?")
                .setTitle("SAVED!")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        title.setText("");
                        body.setText("");
                        save.setEnabled(false);
                    }
                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        onBackPressed();
                        finish();
                    }
                });
        dialog.create();
        dialog.show();

    }

    private void updateNote(String title, String body) {
        Note note = Note.findById(Note.class, id);
        note.title = title;
        note.body = body;
        note.save();
        //so no funny business occurs
        isUpdate = false;
        id = 0;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

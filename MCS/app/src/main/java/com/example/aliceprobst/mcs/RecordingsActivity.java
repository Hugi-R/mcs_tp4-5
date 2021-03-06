package com.example.aliceprobst.mcs;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecordingsActivity extends AppCompatActivity implements RecordingsRecyclerViewListener {

    // DOUBLON WAVRecorder
    public static final String AUDIO_RECORDER_FOLDER = "recordsWAV";
    public static final String CSV_FOLDER = "recordsCSV";

    private RecyclerView recordingsRecyclerView;
    private RecordingAdapter recordingsAdapter;
    private CommandSuggestionAdapter commandsAdapter;
    //private RecyclerView.LayoutManager recordingsLayoutManager;

    private ArrayList<Recording> recordingsArraylist = new ArrayList<>();
    private ArrayList<Recording> recordingsAsRef = new ArrayList<>();
    private ArrayList<Recording> recordingsAsTest = new ArrayList<>();

    HashMap<String, Integer> commands;

    private Button play, stop, record;
    private WAVRecorder wavRecorder;
    private CommandNameAutocomplete record_title;

    private String selectedCommandFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        wavRecorder = new WAVRecorder();

        play = findViewById(R.id.play);
        stop = findViewById(R.id.stop);
        record = findViewById(R.id.record);
        record_title = findViewById(R.id.record_title);

        play.setVisibility(View.GONE);
        stop.setVisibility(View.GONE);
        record.setVisibility(View.GONE);
        record_title.setVisibility(View.GONE);

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                wavRecorder.startRecording();

                // EXAMPLES A CHANGER
                record.setVisibility(View.GONE);
                stop.setVisibility(View.VISIBLE);

                recordingsAdapter.setClickable(false);
            }
        });


        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                wavRecorder.stopRecording();

                // EXAMPLES A CHANGER
                record.setVisibility(View.GONE);
                stop.setVisibility(View.GONE);
                play.setVisibility(View.GONE);
                record_title.setVisibility(View.VISIBLE);
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MediaPlayer mediaPlayer = new MediaPlayer();

                try {

                    mediaPlayer.setDataSource(selectedCommandFilePath);
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                } catch (Exception e) {
                    // make something
                }
            }
        });

        FloatingActionButton addNewCommand = (FloatingActionButton) findViewById(R.id.addNewCommand);
        addNewCommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                record.setVisibility(View.VISIBLE);
                play.setVisibility(View.GONE);

            }
        });

        fetchRecordings();

        /** setting up the toolbar  **/
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Recordings");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
        setSupportActionBar(toolbar);

        /** enabling back button ***/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /** setting up recyclerView **/
        recordingsRecyclerView = findViewById(R.id.recordingsRecyclerView);
        recordingsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // specify an adapter
        recordingsAdapter = new RecordingAdapter(this, this, recordingsArraylist);
        recordingsRecyclerView.setAdapter(recordingsAdapter);


        /**
         * Setting up title autocomplete
         */

        commands = new HashMap<>();
        commands.put("Avance", 0);
        commands.put("Droite", 0);
        commands.put("Gauche", 0);
        commands.put("EtatUrgence", 0);
        commands.put("TourneDroite", 0);
        commands.put("TourneGauche", 0);
        commands.put("FaisUnFlip", 0);
        commands.put("ArreteToi", 0);
        // the add item's text
        String addNewCommandSuggestion = "+ Add new command";


        // our custom adapter with the custom footer text as last parameter
        commandsAdapter = new CommandSuggestionAdapter(
                this, android.R.layout.simple_dropdown_item_1line, commands.keySet().toArray(new String[0]), addNewCommandSuggestion);

        // bind to our custom click listener interface
        commandsAdapter.setOnAddCommandClickListener(new OnAddCommandClickListener() {
            @Override
            public void onAddCommandClicked(View view) {

                String new_command_name = record_title.getText().toString();

                commands.put(new_command_name, 0);
                commandsAdapter.setData(commands.keySet().toArray(new String[0]));
                commandsAdapter.notifyDataSetChanged();

                performCommandNameSelected(new_command_name);

            }
        });

        record_title.setThreshold(0);
        record_title.setAdapter(commandsAdapter);
        record_title.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                performCommandNameSelected(((TextView) view).getText().toString());

            }

        });

    }

    private void performCommandNameSelected(String commandName) {

        Log.d("NEW COMMAND", "Saved new " + commandName);
        wavRecorder.saveTo(commandName + commands.get(commandName) + ".wav");
        commands.replace(commandName, commands.get(commandName).intValue() + 1);
        fetchRecordings();
        recordingsAdapter.notifyDataSetChanged();
        record_title.setVisibility(View.GONE);
        play.setVisibility(View.VISIBLE);
        recordingsRecyclerView.smoothScrollToPosition(recordingsAdapter.getItemCount() - 1);
        recordingsRecyclerView.setEnabled(true);
        recordingsRecyclerView.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if(recordingsRecyclerView.findViewHolderForAdapterPosition(recordingsAdapter.getItemCount() - 1) != null )
                {
                    recordingsRecyclerView.findViewHolderForAdapterPosition(recordingsAdapter.getItemCount() - 1).itemView.performClick();
                }
            }
        },50);

        recordingsAdapter.setClickable(true);
    }

    private void fetchRecordings() {

        recordingsArraylist.clear();

        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        File directory = new File(root + "/" + AUDIO_RECORDER_FOLDER);
        Log.d("Files", "File: " + directory.getPath());
        File[] files = directory.listFiles();

        if (files != null) {

            Log.d("FILES", files.toString());

            for (int i = 0; i < files.length; i++) {

                Log.d("Files", "FileName:" + files[i].getName());
                String commandName = files[i].getName();
                String recordingUri = root + "/" + AUDIO_RECORDER_FOLDER + "/" + commandName;

                Recording recording = new Recording(commandName, recordingUri);
                recordingsArraylist.add(recording);
            }

        }


    }

    public void deleteRecording(Recording r) {
        Log.d("DELETE", "Delete : ");
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + AUDIO_RECORDER_FOLDER);

        boolean success = true;
        if (!folder.exists()) {
            Log.d("File recording", "Trying to create new folder : " + folder.getAbsolutePath());
            success = folder.mkdir();
        }

        if(success) {

            Log.d("File recording", "Successfully created new folder.");
            File tempFile = new File(r.getURI());

            if (tempFile.exists())
                tempFile.delete();

        }

        fetchRecordings();
        recordingsAdapter.notifyDataSetChanged();

    }

    @Override
    public void addAsRef(Recording r) {
        recordingsAsRef.add(r);
    }

    @Override
    public void addAsTest(Recording r) {
        recordingsAsTest.add(r);
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {

        if(recordingsAdapter.isClickable()) {
            selectedCommandFilePath = recordingsArraylist.get(position).getURI();
            play.setVisibility(View.VISIBLE);
        }
    }

}

package com.example.aliceprobst.mcs;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class RecordingsActivity extends AppCompatActivity implements RecyclerViewClickListener {


    // DOUBLON WAVRecorder
    private static final String AUDIO_RECORDER_FOLDER = "records";

    private RecyclerView recordingsRecyclerView;
    private RecyclerView.Adapter recordingsAdapter;
    //private RecyclerView.LayoutManager recordingsLayoutManager;

    private ArrayList<Recording> recordingsArraylist = new ArrayList<>();
    ;

    private Button play, stop, record;
    private WAVRecorder wavRecorder;

    private String selectedCommandFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        wavRecorder = new WAVRecorder();

        play = (Button) findViewById(R.id.play);
        stop = (Button) findViewById(R.id.stop);
        record = (Button) findViewById(R.id.record);

        play.setEnabled(false);
        stop.setEnabled(false);
        record.setEnabled(false);

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                wavRecorder.startRecording();

                // EXAMPLES A CHANGER
                record.setEnabled(false);
                stop.setEnabled(true);
            }
        });


        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                wavRecorder.stopRecording();

                // EXAMPLES A CHANGER
                record.setEnabled(true);
                stop.setEnabled(false);
                play.setEnabled(true);
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

                record.setEnabled(true);

            }
        });

        fetchRecordings();

        /** setting up the toolbar  **/
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Recordings");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
        setSupportActionBar(toolbar);

        /** enabling back button ***/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /** setting up recyclerView **/
        recordingsRecyclerView = (RecyclerView) findViewById(R.id.recordingsRecyclerView);
        recordingsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // specify an adapter
        recordingsAdapter = new RecordingAdapter(this, this, recordingsArraylist);
        recordingsRecyclerView.setAdapter(recordingsAdapter);


    }

    private void fetchRecordings() {

        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        File directory = new File(root + "/" + AUDIO_RECORDER_FOLDER);
        Log.d("Files", "File: " + directory.getPath());
        File[] files = directory.listFiles();

        if (files != null) {

            for (int i = 0; i < files.length; i++) {

                Log.d("Files", "FileName:" + files[i].getName());
                String commandName = files[i].getName();
                String recordingUri = root + "/" + AUDIO_RECORDER_FOLDER + "/" + commandName;

                Recording recording = new Recording(commandName, recordingUri);
                recordingsArraylist.add(recording);
            }

        }


    }

    @Override
    public void recyclerViewListClicked(View v, int position) {

        selectedCommandFilePath = recordingsArraylist.get(position).getURI();
    }
}

package com.example.aliceprobst.mcs;

import android.os.Environment;
import android.util.Log;

import java.io.File;

interface RecordingsRecyclerViewListener extends RecyclerViewClickListener {

    public void deleteRecording(Recording r);
    public void addAsRef(Recording r);
    public void addAsTest(Recording r);

}

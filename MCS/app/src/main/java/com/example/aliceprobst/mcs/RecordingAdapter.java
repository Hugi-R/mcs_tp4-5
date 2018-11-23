package com.example.aliceprobst.mcs;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class RecordingAdapter extends RecyclerView.Adapter<RecordingAdapter.RecordingViewHolder>{

    private ArrayList<Recording> recordingArrayList;
    private Context context;

    private static RecordingsRecyclerViewListener itemListener;

    private int selectedItem = RecyclerView.NO_POSITION;

    private boolean isClickable;

    public RecordingAdapter(Context context, RecordingsRecyclerViewListener itemListener, ArrayList<Recording> recordingArrayList){
        this.context = context;
        this.itemListener = itemListener;
        this.recordingArrayList = recordingArrayList;
    }

    @Override
    public RecordingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.recording, parent,false);
        return new RecordingViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecordingViewHolder holder, int position) {

        Recording recording = recordingArrayList.get(position);
        holder.command_name.setText(recording.getCommand_name());

        holder.itemView.setBackgroundColor(selectedItem == position ? Color.GREEN : Color.TRANSPARENT);

    }

    @Override
    public int getItemCount() {
        return recordingArrayList.size();
    }

    public boolean isClickable() {
        return isClickable;
    }

    public void setClickable(boolean clickable) {
        isClickable = clickable;
    }


    public class RecordingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Button delete;
        TextView command_name;
        CheckBox asRef, asTest;

        public RecordingViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            delete = itemView.findViewById(R.id.delete_recording);
            command_name = itemView.findViewById(R.id.command_name);
            asRef = itemView.findViewById(R.id.asRef);
            asTest = itemView.findViewById(R.id.asTest);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemListener.deleteRecording(recordingArrayList.get(getAdapterPosition()));
                }
            });

            asRef.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((CheckBox) v).isChecked()) {
                        itemListener.addAsRef(recordingArrayList.get(getAdapterPosition()));
                    }

                }
            });

            asTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((CheckBox) v).isChecked()) {
                        itemListener.addAsRef(recordingArrayList.get(getAdapterPosition()));
                    }
                }
            });

        }

        @Override
        public void onClick(View v) {
            // Below line is just like a safety check, because sometimes holder could be null,
            // in that case, getAdapterPosition() will return RecyclerView.NO_POSITION
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;

            if(isClickable) {
                Log.d("CLICKED", v.toString());

                // Updating old as well as new positions
                notifyItemChanged(selectedItem);
                selectedItem = getAdapterPosition();
                notifyItemChanged(selectedItem);

                itemListener.recyclerViewListClicked(v, this.getLayoutPosition());
            }

        }

    }



}
package com.example.aliceprobst.mcs;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class RecordingAdapter extends RecyclerView.Adapter<RecordingAdapter.RecordingViewHolder>{

    private ArrayList<Recording> recordingArrayList;
    private Context context;

    private static RecyclerViewClickListener itemListener;

    private int selectedItem = RecyclerView.NO_POSITION;

    public RecordingAdapter(Context context, RecyclerViewClickListener itemListener, ArrayList<Recording> recordingArrayList){
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


    public class RecordingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView command_name;
        CheckBox asRef, asTest;

        public RecordingViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            command_name = itemView.findViewById(R.id.command_name);
            asRef = itemView.findViewById(R.id.asRef);
            asTest = itemView.findViewById(R.id.asTest);

        }

        @Override
        public void onClick(View v) {
            // Below line is just like a safety check, because sometimes holder could be null,
            // in that case, getAdapterPosition() will return RecyclerView.NO_POSITION
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;

            // Updating old as well as new positions
            notifyItemChanged(selectedItem);
            selectedItem = getAdapterPosition();
            notifyItemChanged(selectedItem);

            itemListener.recyclerViewListClicked(v, this.getLayoutPosition());

        }

    }



}
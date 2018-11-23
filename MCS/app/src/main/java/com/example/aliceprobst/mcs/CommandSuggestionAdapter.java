package com.example.aliceprobst.mcs;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CommandSuggestionAdapter extends BaseAdapter implements Filterable {

    private List<String> commands;
    private final Object mLock = new Object();

    private int mResource;
    private int mDropDownResource;

    private ArrayList<String> originalValues;
    private ArrayFilter arrayFilter;

    private LayoutInflater layoutInflater;

    // the last item, i.e the footer
    private String addNewCommandText;

    // our listener
    private OnAddCommandClickListener onAddCommandClickListener;

    public CommandSuggestionAdapter(Context context, int resource, List<String> commands, String addNewCommandText) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResource = mDropDownResource = resource;
        this.commands = commands;
        this.addNewCommandText = addNewCommandText;
    }


    /**
     * Set listener for clicks on the footer item
     */
    public void setOnAddCommandClickListener(OnAddCommandClickListener listener) {
        onAddCommandClickListener = listener;
    }

    @Override
    public int getCount() {
        return commands.size() + 1;
    }

    @Override
    public String getItem(int position) {
        if(position == 0) {
            // last item is always the footer text
            return addNewCommandText;
        }

        // return real text
        return commands.get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, mResource);
    }

    private View createViewFromResource(int position, View convertView, ViewGroup parent,
                                        int resource) {
        View view;
        TextView text;

        if (convertView == null) {
            view = layoutInflater.inflate(resource, parent, false);
        } else {
            view = convertView;
        }

        try {
            //  If no custom field is assigned, assume the whole resource is a TextView
            text = (TextView) view;
        } catch (ClassCastException e) {
            Log.e("CustomAutoCAdapter", "Layout XML file is not a text field");
            throw new IllegalStateException("Layout XML file is not a text field", e);
        }

        text.setText(getItem(position));

        if(position == 0) {
            // it's the last item, bind click listener
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onAddCommandClickListener != null) {
                        onAddCommandClickListener.onAddCommandClicked(v);
                    }
                }
            });
        } else {
            // it's a real item, set click listener to null and reset to original state
            view.setOnClickListener(null);
            view.setClickable(false);
        }

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, mDropDownResource);
    }

    @Override
    public Filter getFilter() {
        if (arrayFilter == null) {
            arrayFilter = new ArrayFilter();
        }
        return arrayFilter;
    }

    public void setData(ArrayList<String> new_commands) {
        this.originalValues = new_commands;
    }

    /**
     * <p>An array filter constrains the content of the array adapter with
     * a prefix. Each item that does not start with the supplied prefix
     * is removed from the list.</p>
     */
    private class ArrayFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (originalValues == null) {
                synchronized (mLock) {
                    originalValues = new ArrayList<String>(commands);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                ArrayList<String> list;
                synchronized (mLock) {
                    list = new ArrayList<String>(originalValues);
                }
                results.values = list;

                // add +1 since we have a footer item which is always visible
                results.count = list.size()+1;
            } else {
                String prefixString = prefix.toString().toLowerCase();

                ArrayList<String> values;
                synchronized (mLock) {
                    values = new ArrayList<String>(originalValues);
                }

                final int count = values.size();
                final ArrayList<String> newValues = new ArrayList<String>();

                for (int i = 0; i < count; i++) {
                    final String value = values.get(i);
                    final String valueText = value.toString().toLowerCase();

                    // First match against the whole, non-splitted value
                    if (valueText.startsWith(prefixString)) {
                        newValues.add(value);
                    } else {
                        final String[] words = valueText.split(" ");
                        final int wordCount = words.length;

                        // Start at index 0, in case valueText starts with space(s)
                        for (int k = 0; k < wordCount; k++) {
                            if (words[k].startsWith(prefixString)) {
                                newValues.add(value);
                                break;
                            }
                        }
                    }
                }

                results.values = newValues;
                // add one since we always show the footer
                results.count = newValues.size() + 1;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            commands = (List<String>) results.values;
            notifyDataSetChanged();
        }
    }
}

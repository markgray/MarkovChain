package com.example.android.common;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClockDataAdapter extends RecyclerView.Adapter<ClockDataAdapter.ViewHolder> {
    public List<ClockDataItem> dataSet = new ArrayList<>(3600);

    public List<ClockDataItem> addToDataSet(ClockDataItem clockDataItem) {
        dataSet.add(clockDataItem);
        return dataSet;
    }

    public void sortList() {
        Collections.sort(dataSet);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}

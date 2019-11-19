package com.example.timely;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.net.ConnectException;
import java.util.ArrayList;

import dataStructures.Presentation;
import dataStructures.Report;

public class ReportAdapter extends BaseAdapter {
    private ArrayList<Report> data;
    private LayoutInflater inflater;

    public ReportAdapter(Context context, ArrayList<Report> listData) {
        this.data = listData;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.report_item, null);
            holder = new ViewHolder();
            holder.nameView = convertView.findViewById(R.id.report_name);
            holder.timeView = convertView.findViewById(R.id.report_time);
            holder.dateView = convertView.findViewById(R.id.report_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nameView.setText(data.get(position).date.toString());
        holder.timeView.setText(data.get(position).type.toString());
        holder.dateView.setText(Presentation.toStringTime(data.get(position).total_estimate));
        return convertView;
    }

    static class ViewHolder {
        TextView nameView;
        TextView timeView;
        TextView dateView;
    }
}

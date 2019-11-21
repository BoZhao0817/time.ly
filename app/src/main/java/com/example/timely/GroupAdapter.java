package com.example.timely;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.net.ConnectException;
import java.util.ArrayList;

import dataStructures.Presentation;
import dataStructures.Report;
import dataStructures.User;

public class GroupAdapter extends BaseAdapter {
    private ArrayList<User> data;
    private LayoutInflater inflater;
    private Context c;
    public GroupAdapter(Context context, ArrayList<User> listData) {
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

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.member_row, null);
            c = convertView.getContext();
            holder = new ViewHolder();
            holder.nameView = convertView.findViewById(R.id.member_name);
            holder.timeView = convertView.findViewById(R.id.duration);
            holder.roleView = convertView.findViewById(R.id.role);
            holder.edit = convertView.findViewById(R.id.Edit);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nameView.setText(data.get(position).name);
        holder.timeView.setText(Presentation.toStringTime(data.get(position).duration));
        holder.roleView.setText(data.get(position).role);
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(c, AddGroupMemberActivity.class);
                edit.putExtra("user", data.get(position));
                c.startActivity(edit);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView nameView;
        TextView timeView;
        TextView roleView;
        Button edit;
    }
}

package com.example.poojadeole.homework9final;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by poojadeole on 11/19/17.
 */

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableViewHolder> {

    private LayoutInflater inflater;
    List<TableRows> data = Collections.emptyList();

    public TableAdapter(Context context, List<TableRows> data){
        inflater=LayoutInflater.from(context);
        this.data = data;
    }
    @Override
    public TableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.custom_row, parent,false);
        TableViewHolder holder = new TableViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(TableViewHolder holder, int position) {

        TableRows current = data.get(position);
        if (position == 2) {
            holder.title.setText(current.title);
            if(current.title.charAt(0) != '-') {
                holder.title.setTextColor(Color.parseColor("#32CD32"));
                holder.imagetable.setImageResource(R.drawable.up);
            }
            else{
                holder.title.setTextColor(Color.parseColor("#FF0000"));
                holder.imagetable.setImageResource(R.drawable.down);
            }
            holder.header.setText(current.header);
        }
        else{
            holder.title.setText(current.title);
            holder.header.setText(current.header);
        }
        Log.d("mytable",current.title);


    }

    @Override
    public int getItemCount() {
        int dsize = data.size();
        Log.d("data_size",  Float.toString(dsize));
        return data.size();
    }

    class TableViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView header;
        ImageView imagetable;
        public TableViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.listtext);
            header = (TextView) itemView.findViewById(R.id.listhead);
            imagetable = (ImageView) itemView.findViewById(R.id.tableimage);

        }
    }
}

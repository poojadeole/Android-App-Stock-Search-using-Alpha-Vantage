package com.example.poojadeole.homework9final;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by poojadeole on 11/23/17.
 */

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.FavViewHolder> {
    private LayoutInflater inflator;
    List<FavInfo> favdata = Collections.emptyList();
    private Context context;

    public FavAdapter(Context context, List<FavInfo> favdata){
        inflator = LayoutInflater.from(context);
        this.context = context;
        this.favdata = favdata;
    }
    @Override
    public FavViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflator.inflate(R.layout.custom_fav,parent,false);
        FavViewHolder holder = new FavViewHolder(context, view);
        return holder;
    }

    @Override
    public void onBindViewHolder(FavViewHolder holder, int position) {
        final FavInfo current = favdata.get(position);
        holder.fsymbol.setText(current.favsym);
        holder.fprice.setText(current.favprice);
        holder.fchange.setText(current.favchange);
        if(current.favchange.charAt(0)!='-'){
            holder.fchange.setTextColor(Color.parseColor("#32CD32"));
                                }
                               else{
            holder.fchange.setTextColor(Color.parseColor("#FF0000"));
                                }
        holder.favRow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder((MainActivity)context);
                builder.setTitle("Remove from favorites?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        ((MainActivity)context).removeFav(current.favsym);
                        dialog.dismiss();
                        Toast.makeText(context, "Selected Item Deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();



                return false;
            }
        });
        holder.favRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(((MainActivity)context), SendString.class);
                intent.putExtra("my_data", current.favsym);
                intent.putExtra("favorite", true);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return favdata.size();
    }

    public void newFavs(List<FavInfo> newList) {
        favdata.clear();
        favdata.addAll(newList);
        notifyDataSetChanged();
    }

    public void removeFav(String str) {
        for(FavInfo fav : favdata) {
            if(fav.favsym.equals(str)) {
                favdata.remove(fav);
                notifyDataSetChanged();
                break;
            }
        }
    }

    public void clearData() {
        favdata.clear();
        notifyDataSetChanged();
    }
    public void addFav(FavInfo favInfo) {
        favdata.add(favInfo);
        notifyDataSetChanged();
    }

    public void sort(String sort, String order) {
        if(sort.equals("Symbol") && order.equals("Ascending")) {
            Collections.sort(favdata, new Comparator<FavInfo>() {
                @Override
                public int compare(FavInfo o1, FavInfo o2) {
                    return o1.favsym.compareTo(o2.favsym);
                }
            });
        } else if(sort.equals("Symbol") && order.equals("Descending")) {
            Collections.sort(favdata, new Comparator<FavInfo>() {
                @Override
                public int compare(FavInfo o1, FavInfo o2) {
                    return o2.favsym.compareTo(o1.favsym);
                }
            });
        } else if(sort.equals("Price") && order.equals("Ascending")) {
            Collections.sort(favdata, new Comparator<FavInfo>() {
                @Override
                public int compare(FavInfo o1, FavInfo o2) {
                    return Math.round(Float.parseFloat(o1.favprice)- (Float.parseFloat(o2.favprice)));
                }
            });
        } else if(sort.equals("Price") && order.equals("Descending")) {
            Collections.sort(favdata, new Comparator<FavInfo>() {
                @Override
                public int compare(FavInfo o1, FavInfo o2) {
                    return Math.round(Float.parseFloat(o2.favprice)- Float.parseFloat(o1.favprice));
                }
            });
        } else if(sort.equals("Change") && order.equals("Ascending")) {
            Collections.sort(favdata, new Comparator<FavInfo>() {
                @Override
                public int compare(FavInfo o1, FavInfo o2) {
                    String a[] = o1.favchange.split("\\(");
                    String b[] = o2.favchange.split("\\(");
                    return Math.round(Float.parseFloat(a[0]) - Float.parseFloat(b[0]));
                }
            });
        } else if(sort.equals("Change") && order.equals("Descending")) {
            Collections.sort(favdata, new Comparator<FavInfo>() {
                @Override
                public int compare(FavInfo o1, FavInfo o2) {
                    String a[] = o1.favchange.split("\\(");
                    String b[] = o2.favchange.split("\\(");
                    return Math.round(Float.parseFloat(b[0]) - Float.parseFloat(a[0]));
                }
            });
        }
        notifyDataSetChanged();
    }

    class FavViewHolder extends RecyclerView.ViewHolder{
        TextView fsymbol;
        TextView fprice;
        TextView fchange;
        LinearLayout favRow;
        public FavViewHolder(final Context context, View itemView){
            super(itemView);
            favRow = (LinearLayout)itemView.findViewById(R.id.favRow);
            fsymbol = (TextView)itemView.findViewById(R.id.favSym);
            fprice = (TextView)itemView.findViewById(R.id.favPrice);
            fchange = (TextView)itemView.findViewById(R.id.favChange);
        }
    }
}

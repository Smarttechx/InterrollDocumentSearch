package com.softdev.smarttechx.interrolldocumentsearch.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;


import com.softdev.smarttechx.interrolldocumentsearch.R;
import com.softdev.smarttechx.interrolldocumentsearch.model.InterrollDoc;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by SMARTTECHX on 11/3/2017.
 */

public class DataAdapter extends RecyclerView.Adapter<DataHolder> implements Filterable {

    Context c;
    ArrayList<InterrollDoc> searchDoc, filterList;
    DataFilter filter;
   

    private ItemClickListener Listener;
   

    public DataAdapter(Context ctx, ArrayList<InterrollDoc>searchDoc) {
        this.c = ctx;
        this.searchDoc =searchDoc;
        this.filterList =searchDoc;
    }


    @Override
    public DataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //CONVERT XML TO VIEW ONBJ
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_adapter, parent, false);

        //HOLDER
        DataHolder holder = new DataHolder(v);

        return holder;
    }

    //DATA BOUND TO VIEWS
    @Override
    public void onBindViewHolder(DataHolder holder, int position) {

        //BIND DATA
        holder.noTxt.setText(searchDoc.get(position).getNo());
        holder.part_no.setText(searchDoc.get(position).getPart_num());
        holder.part_link.setText(searchDoc.get(position).getPart_link());


        //IMPLEMENT CLICK LISTENET
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Context context = v.getContext();

            }
        });

    }

    public void clear(){
        if(getItemCount()>0){
            int size =this.searchDoc.size();
            this.searchDoc.clear();
            notifyItemRangeChanged(0,size);
        }
    }
    //GET TOTAL NUM OF PLAYERS
    @Override
    public int getItemCount() {
        return searchDoc.size();
    }


    //RETURN FILTER OBJ
    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new DataFilter(filterList, this);
        }

        return filter;
    }
}

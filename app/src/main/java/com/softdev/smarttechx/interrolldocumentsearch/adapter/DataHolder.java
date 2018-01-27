package com.softdev.smarttechx.interrolldocumentsearch.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.softdev.smarttechx.interrolldocumentsearch.R;


/**
 * Created by SMARTTECHX on 11/3/2017.
 */

public class DataHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //OUR VIEWS

    TextView noTxt, part_no,part_link;

    ItemClickListener itemClickListener;


    public DataHolder(View itemView) {
        super(itemView);
        this.noTxt =  (TextView) itemView.findViewById(R.id.no);
        this.part_no =  (TextView) itemView.findViewById(R.id.part_num);
        this.part_link= (TextView) itemView.findViewById(R.id.part_link);

        itemView.setTag(itemView);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.onItemClick(v ,getLayoutPosition());
    }

    public void setItemClickListener(ItemClickListener ic)
    {
        this.itemClickListener=ic;
    }
}

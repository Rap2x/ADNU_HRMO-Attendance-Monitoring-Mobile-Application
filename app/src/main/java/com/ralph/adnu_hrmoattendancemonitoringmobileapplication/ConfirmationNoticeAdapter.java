package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ConfirmationNoticeAdapter extends RecyclerView.Adapter<ConfirmationNoticeAdapter.ViewHolder>{
    private List<ConfirmationNoticeListItem> listItems;
    private Context context;

    public ConfirmationNoticeAdapter(List<ConfirmationNoticeListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.confirmation_notice_list_item,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ConfirmationNoticeListItem listItem = listItems.get(i);

        viewHolder.subjectCode.setText(listItem.getSubjectCode());
        viewHolder.time.setText(listItem.getTime());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView subjectCode;
        public TextView time;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            subjectCode = (TextView) itemView.findViewById(R.id.subject_code);
            time = (TextView) itemView.findViewById(R.id.time);
        }
    }
}

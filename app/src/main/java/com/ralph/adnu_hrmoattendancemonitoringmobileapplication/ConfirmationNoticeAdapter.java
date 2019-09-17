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

    private OnItemClickListener mlistener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mlistener = listener;
    }

    public ConfirmationNoticeAdapter(List<ConfirmationNoticeListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.confirmation_notice_list_item,parent,false);

        return new ViewHolder(v,mlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ConfirmationNoticeListItem listItem = listItems.get(i);

        viewHolder.subjectCode.setText(listItem.getSubjectCode());
        viewHolder.time.setText(listItem.getTime());
        viewHolder.date.setText(listItem.getDate());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public TextView subjectCode;
        public TextView time;
        public TextView date;


        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.confirmation_notice_title);
            subjectCode = (TextView) itemView.findViewById(R.id.subject_code);
            time = (TextView) itemView.findViewById(R.id.time);
            date = (TextView) itemView.findViewById(R.id.attendance_date);

            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}

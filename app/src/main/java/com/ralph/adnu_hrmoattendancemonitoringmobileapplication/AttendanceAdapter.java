package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {
    private List<AttendanceListItem> listItems;
    private Context context;

    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void setAbsent(int position);
        void setPresent(int position);
        void viewConfirmationNotice(int position);
        void onRadioButtonClicked(int position, View view);
        void viewImages(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewName;
        public TextView textViewSubjectCode;
        public TextView textViewRoomNumber;
        public TextView textViewTime;
        public TextView textViewFirst;
        public TextView textViewSecond;
        public Button buttonPresent;
        public Button buttonAbsent;
        public TextView noticeCount;
        public RadioButton radioFirst;
        public RadioButton radioSecond;
        public Button viewImagesButton1;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.fullName);
            textViewSubjectCode = (TextView) itemView.findViewById(R.id.subject);
            textViewRoomNumber = (TextView) itemView.findViewById(R.id.roomNumber);
            textViewTime = (TextView) itemView.findViewById(R.id.classTime);
            textViewFirst = (TextView) itemView.findViewById(R.id.firstTime);
            textViewSecond = (TextView) itemView.findViewById(R.id.secondTime);
            buttonAbsent = (Button) itemView.findViewById(R.id.absent);
            buttonPresent = (Button) itemView.findViewById(R.id.present);
            noticeCount = (TextView) itemView.findViewById(R.id.notice_count);
            radioFirst = (RadioButton) itemView.findViewById(R.id.firstCheck);
            radioSecond = (RadioButton) itemView.findViewById(R.id.secondCheck);
            viewImagesButton1 = (Button) itemView.findViewById(R.id.viewImageButton2);

            buttonAbsent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.setAbsent(position);
                        }
                    }
                }
            });

            buttonPresent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.setPresent(position);
                        }
                    }
                }
            });

            textViewName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.viewConfirmationNotice(position);
                        }
                    }
                }
            });

            radioFirst.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onRadioButtonClicked(position, view);
                        }
                    }
                }
            });

            radioSecond.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onRadioButtonClicked(position, view);
                        }
                    }
                }
            });

            viewImagesButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.viewImages(position);
                        }
                    }
                }
            });
        }
    }

    public AttendanceAdapter(List<AttendanceListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attendance_list_item, parent, false);

        ViewHolder evh = new ViewHolder(v, mListener);

        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        AttendanceListItem listItem = listItems.get(position);

        viewHolder.textViewName.setText(listItem.getName());
        viewHolder.textViewSubjectCode.setText(listItem.getSubjectCode());
        viewHolder.textViewRoomNumber.setText(listItem.getRoomNumber());
        viewHolder.textViewTime.setText(listItem.getClassTime());
        viewHolder.textViewFirst.setText(listItem.getFirst());
        viewHolder.textViewSecond.setText(listItem.getSecond());
        viewHolder.noticeCount.setText(listItem.getNoticeCount());

        if(!(listItem.getNoticeCount().equals("0"))){
            viewHolder.noticeCount.setTextColor(Color.RED);
        }

        if(viewHolder.textViewFirst.getText().toString().matches("")) { // if the textview is empty
            viewHolder.radioSecond.setEnabled(false);
            viewHolder.radioFirst.setEnabled(true);
        }else {
            viewHolder.radioFirst.setEnabled(false);
            viewHolder.radioSecond.setEnabled(true);
        }
        if(!viewHolder.textViewFirst.getText().toString().matches("") && !viewHolder.textViewSecond.getText().toString().matches("")){
            viewHolder.radioFirst.setEnabled(false);
            viewHolder.radioSecond.setEnabled(false);
        }

        if(listItem.getFirstImageFile() != null){
            if(!listItem.getFirstImageFile().equals("")) {
                viewHolder.textViewFirst.setTextColor(Color.RED);
                viewHolder.viewImagesButton.setEnabled(true);
            }else
                viewHolder.viewImagesButton.setEnabled(false);
        }

        if(listItem.getSecondImageFile() != null){
            if(!listItem.getSecondImageFile().equals("")){
                viewHolder.textViewSecond.setTextColor(Color.RED);
                viewHolder.viewImagesButton.setEnabled(true);
            }else
                viewHolder.viewImagesButton.setEnabled(false);
        }
    }
    @Override
    public int getItemCount() {
        return listItems.size();
    }
}

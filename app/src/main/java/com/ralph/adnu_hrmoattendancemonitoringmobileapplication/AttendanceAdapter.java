package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {
    private List<AttendanceListItem> listItems;
    private Context context;


    public AttendanceAdapter(List<AttendanceListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attendance_list_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        AttendanceListItem listItem = listItems.get(position);

        viewHolder.textViewName.setText(listItem.getName());
        viewHolder.textViewSubjectCode.setText(listItem.getSubjectCode());
        viewHolder.textViewRoomNumber.setText(listItem.getRoomNumber());
        viewHolder.textViewTime.setText(listItem.getClassTime());
        viewHolder.textViewFirst.setText(listItem.getFirst());
        viewHolder.textViewSecond.setText(listItem.getSecond());

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewName;
        public TextView textViewSubjectCode;
        public TextView textViewRoomNumber;
        public TextView textViewTime;
        public TextView textViewFirst;
        public TextView textViewSecond;
        public Button buttonPresent;
        public Button buttonAbsent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.fullName);
            textViewSubjectCode = (TextView) itemView.findViewById(R.id.subject);
            textViewRoomNumber = (TextView) itemView.findViewById(R.id.roomNumber);
            textViewTime = (TextView) itemView.findViewById(R.id.classTime);
            textViewFirst = (TextView) itemView.findViewById(R.id.firstTime);
            textViewSecond = (TextView) itemView.findViewById(R.id.secondTime);
            buttonAbsent = (Button) itemView.findViewById(R.id.absent);
            buttonPresent = (Button) itemView.findViewById(R.id.present);

            buttonAbsent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        }
    }

}

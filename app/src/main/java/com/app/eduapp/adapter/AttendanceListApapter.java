package com.app.eduapp.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.eduapp.R;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.pojo.GetMonthlyAttendance;

import java.util.List;

public class AttendanceListApapter extends RecyclerView.Adapter<AttendanceListApapter.VHItem> {
    private Context mContext;
    private int rowLayout;
    ConnectionDetector cd;
    private List<GetMonthlyAttendance.MonthlyAttendanceList> dataMembers;

    public AttendanceListApapter(Context context, int rowLayout, List<GetMonthlyAttendance.MonthlyAttendanceList> members, String flag) {
        this.mContext = context;
        this.rowLayout = rowLayout;
        this.dataMembers = members;
        cd = new ConnectionDetector(context);
    }

    @Override
    public AttendanceListApapter.VHItem onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new AttendanceListApapter.VHItem(v);
    }

    @Override
    public int getItemCount() {
        return dataMembers.size();
    }

    @Override
    public void onBindViewHolder(final VHItem viewHolder, final int position) {
        viewHolder.setIsRecyclable(false);
        try {
            //binding.tvTime.setText(Html.fromHtml("<medium><font color=\"#000000\">" + "Waiting to automatically detect an OTP sent to " + "</font></medium>" + "<medium><font color=\"#000000\">" + EdUtils.getMobilePreferences(mContext) + " </font></medium>" + " <font color=\"#FF0000\">" + i + " secs" + "</font>"));
            dataMembers.get(position);
            viewHolder.tv_date.setText(dataMembers.get(position).getFullDate());
            //viewHolder.tv_day.setText(dataMembers.get(position).getDate());
            viewHolder.tv_day.setText(Html.fromHtml("<small><font color=\"#236503\">" + "IN " + "</font></small>" + "<small><font color=\"#000000\">" + dataMembers.get(position).getInPunchTime() + " </font></small>" + "<small><font color=\"#236503\">" + " OUT " + "</font></small>" + "<small><font color=\"#000000\">" + dataMembers.get(position).getOutPunchTime() + " </font></small>"));
            if (dataMembers.get(position).getAttendance().equalsIgnoreCase("present")) {
                viewHolder.tv_attend_status.setTextColor(Color.parseColor("#147109"));
                viewHolder.tv_date.setTextColor(Color.parseColor("#147109"));
            } else if (dataMembers.get(position).getAttendance().equalsIgnoreCase("absent")) {
                viewHolder.tv_attend_status.setTextColor(Color.parseColor("#a63433"));
                viewHolder.tv_date.setTextColor(Color.parseColor("#a63433"));
            } else if (dataMembers.get(position).getAttendance().equalsIgnoreCase("holiday")) {
                viewHolder.tv_attend_status.setTextColor(Color.parseColor("#0b2c59"));
                viewHolder.tv_date.setTextColor(Color.parseColor("#0b2c59"));
            } else if (dataMembers.get(position).getAttendance().equalsIgnoreCase("n/a")) {
                viewHolder.tv_attend_status.setTextColor(Color.parseColor("#072b65"));
                viewHolder.tv_date.setTextColor(Color.parseColor("#072b65"));
            }


            viewHolder.tv_attend_status.setText(dataMembers.get(position).getAttendance());


        } catch (Exception e) {
        }
    }


    class VHItem extends RecyclerView.ViewHolder {

        TextView tv_date, tv_day, tv_attend_status;

        public VHItem(View itemView) {
            super(itemView);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_day = (TextView) itemView.findViewById(R.id.tv_day);
            tv_attend_status = (TextView) itemView.findViewById(R.id.tv_attend_status);
        }
    }


}



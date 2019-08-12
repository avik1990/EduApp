package com.app.eduapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.eduapp.LeaveAppDetails;
import com.app.eduapp.R;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.pojo.EmpLeaveApplicationList;

import java.util.List;

public class EmpLeaveListApapter extends RecyclerView.Adapter<EmpLeaveListApapter.VHItem> {
    private Context mContext;
    private int rowLayout;
    ConnectionDetector cd;
    private List<EmpLeaveApplicationList.StudentLeaveApplicationList> dataMembers;

    public EmpLeaveListApapter(Context context, int rowLayout, List<EmpLeaveApplicationList.StudentLeaveApplicationList> members, String flag) {
        this.mContext = context;
        this.rowLayout = rowLayout;
        this.dataMembers = members;
        cd = new ConnectionDetector(context);
    }

    @Override
    public EmpLeaveListApapter.VHItem onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new EmpLeaveListApapter.VHItem(v);
    }

    @Override
    public int getItemCount() {
        return dataMembers.size();
    }

    @Override
    public void onBindViewHolder(final VHItem viewHolder, final int position) {
        viewHolder.setIsRecyclable(false);
        try {
            dataMembers.get(position);
            viewHolder.tv_name.setText(dataMembers.get(position).getFullName());
            viewHolder.tv_date.setText(dataMembers.get(position).getLeaveApplicationDate());

            if (dataMembers.get(position).getIsApproved().equalsIgnoreCase("N")) {
                viewHolder.tv_appstatus.setText("New");
            } else if (dataMembers.get(position).getIsApproved().equalsIgnoreCase("A")) {
                viewHolder.tv_appstatus.setText("Approved");
            } else if (dataMembers.get(position).getIsApproved().equalsIgnoreCase("D")) {
                viewHolder.tv_appstatus.setText("Disapproved");
            }

            viewHolder.rlv_leave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, LeaveAppDetails.class);
                    i.putExtra("leave_id", dataMembers.get(position).getLeaveID());
                    mContext.startActivity(i);
                }
            });


        } catch (Exception e) {
        }
    }


    class VHItem extends RecyclerView.ViewHolder {
        TextView tv_name, tv_appstatus, tv_date;
        LinearLayout rlv_leave;

        public VHItem(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_appstatus = (TextView) itemView.findViewById(R.id.tv_appstatus);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            rlv_leave = (LinearLayout) itemView.findViewById(R.id.rlv_leave);
        }
    }
}



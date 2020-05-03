package com.app.eduapp.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.eduapp.R;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.pojo.TDiaryClass;
import com.app.eduapp.tattendance.AStudentListActivity;
import com.app.eduapp.tattendance.TakeAttendanceListActivity;

import java.util.List;

public class AClassListApapter extends RecyclerView.Adapter<AClassListApapter.VHItem> {
    private Context mContext;
    private int rowLayout;
    ConnectionDetector cd;
    private List<TDiaryClass.ClassSectionListArr> dataMembers;

    public AClassListApapter(Context context, int rowLayout, List<TDiaryClass.ClassSectionListArr> members, String flag) {
        this.mContext = context;
        this.rowLayout = rowLayout;
        this.dataMembers = members;
        cd = new ConnectionDetector(context);
    }

    @Override
    public AClassListApapter.VHItem onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new AClassListApapter.VHItem(v);
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
            viewHolder.tv_name.setText(dataMembers.get(position).getClassName());
            viewHolder.tv_appstatus.setText("Section " + dataMembers.get(position).getSectionName());

            viewHolder.v_attendance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, AStudentListActivity.class);
                    i.putExtra("ClassID", dataMembers.get(position).getClassID());
                    i.putExtra("SectionID", dataMembers.get(position).getSectionID());
                    mContext.startActivity(i);
                }
            });

            viewHolder.v_takeattendance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, TakeAttendanceListActivity.class);
                    i.putExtra("ClassID", dataMembers.get(position).getClassID());
                    i.putExtra("SectionID", dataMembers.get(position).getSectionID());
                    mContext.startActivity(i);
                }
            });


        } catch (Exception e) {
        }
    }


    class VHItem extends RecyclerView.ViewHolder {
        TextView tv_name, tv_appstatus, tv_date,v_attendance,v_takeattendance;
        LinearLayout rlv_leave;

        public VHItem(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_appstatus = (TextView) itemView.findViewById(R.id.tv_appstatus);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            rlv_leave = (LinearLayout) itemView.findViewById(R.id.rlv_leave);
            v_attendance = (TextView) itemView.findViewById(R.id.v_attendance);
            v_takeattendance = (TextView) itemView.findViewById(R.id.v_takeattendance);

        }
    }
}



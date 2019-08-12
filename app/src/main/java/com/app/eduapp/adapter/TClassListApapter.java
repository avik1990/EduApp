package com.app.eduapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.eduapp.ExamListActivity;
import com.app.eduapp.R;
import com.app.eduapp.RoutineActivity;
import com.app.eduapp.StudenSectiontListActivity;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.pojo.TDiaryClass;
import com.app.eduapp.teacher.TSubjectListActivity;

import java.util.List;

public class TClassListApapter extends RecyclerView.Adapter<TClassListApapter.VHItem> {
    private Context mContext;
    private int rowLayout;
    ConnectionDetector cd;
    private List<TDiaryClass.ClassSectionListArr> dataMembers;
    String flag;

    public TClassListApapter(Context context, int rowLayout, List<TDiaryClass.ClassSectionListArr> members, String flag) {
        this.mContext = context;
        this.rowLayout = rowLayout;
        this.dataMembers = members;
        this.flag = flag;
        cd = new ConnectionDetector(context);
    }

    @Override
    public TClassListApapter.VHItem onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new TClassListApapter.VHItem(v);
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

            if (flag.equalsIgnoreCase("exam")) {
                viewHolder.rlv_leave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(mContext, ExamListActivity.class);
                        i.putExtra("ClassID", dataMembers.get(position).getClassID());
                        i.putExtra("SectionID", dataMembers.get(position).getSectionID());
                        mContext.startActivity(i);
                    }
                });
            } else if (flag.equalsIgnoreCase("result")) {
                viewHolder.rlv_leave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(mContext, StudenSectiontListActivity.class);
                        i.putExtra("ClassID", dataMembers.get(position).getClassID());
                        i.putExtra("SectionID", dataMembers.get(position).getSectionID());
                        mContext.startActivity(i);
                    }
                });
            }else if (flag.equalsIgnoreCase("routine")) {
                viewHolder.rlv_leave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(mContext, RoutineActivity.class);
                        i.putExtra("ClassID", dataMembers.get(position).getClassID());
                        i.putExtra("SectionID", dataMembers.get(position).getSectionID());
                        mContext.startActivity(i);
                    }
                });
            }else if (flag.equalsIgnoreCase("routinetype")) {
                viewHolder.rlv_leave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(mContext, RoutineActivity.class);
                        i.putExtra("ClassID", dataMembers.get(position).getClassID());
                        i.putExtra("SectionID", dataMembers.get(position).getSectionID());
                        mContext.startActivity(i);
                    }
                });
            } else {
                viewHolder.rlv_leave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(mContext, TSubjectListActivity.class);
                        i.putExtra("ClassID", dataMembers.get(position).getClassID());
                        i.putExtra("SectionID", dataMembers.get(position).getSectionID());
                        i.putExtra("SubjectsID", "");
                        mContext.startActivity(i);
                    }
                });
            }
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



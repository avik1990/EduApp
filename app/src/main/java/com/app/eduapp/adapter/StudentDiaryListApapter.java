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
import com.app.eduapp.StudentDiaryPostList;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.pojo.StudentDiary;

import java.util.List;

public class StudentDiaryListApapter extends RecyclerView.Adapter<StudentDiaryListApapter.VHItem> {
    private Context mContext;
    private int rowLayout;
    ConnectionDetector cd;
    private List<StudentDiary.DiaryList> dataMembers;

    public StudentDiaryListApapter(Context context, int rowLayout, List<StudentDiary.DiaryList> members, String flag) {
        this.mContext = context;
        this.rowLayout = rowLayout;
        this.dataMembers = members;
        cd = new ConnectionDetector(context);
    }

    @Override
    public StudentDiaryListApapter.VHItem onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new StudentDiaryListApapter.VHItem(v);
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
            viewHolder.tv_date.setText(dataMembers.get(position).getSubjectsName());
            viewHolder.tv_appstatus.setText("Section " + dataMembers.get(position).getSectionName());

            viewHolder.rlv_leave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, StudentDiaryPostList.class);
                    i.putExtra("ClassID", dataMembers.get(position).getClassID());
                    i.putExtra("SectionID", dataMembers.get(position).getSectionID());
                    i.putExtra("SubjectsID", dataMembers.get(position).getSubjectsID());
                    i.putExtra("DiaryID", dataMembers.get(position).getDiaryId());
                    i.putExtra("classname", dataMembers.get(position).getClassName());
                    i.putExtra("subjectname", dataMembers.get(position).getSubjectsName());
                    i.putExtra("sectionname", dataMembers.get(position).getSectionName());
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



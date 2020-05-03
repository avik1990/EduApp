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
import com.app.eduapp.pojo.TDiarySubject;
import com.app.eduapp.teacher.TDiaryPostList;

import java.util.List;

public class TSubjectListApapter extends RecyclerView.Adapter<TSubjectListApapter.VHItem> {
    private Context mContext;
    private int rowLayout;
    ConnectionDetector cd;
    private List<TDiarySubject.DiaryListArr> dataMembers;

    public TSubjectListApapter(Context context, int rowLayout, List<TDiarySubject.DiaryListArr> members, String flag) {
        this.mContext = context;
        this.rowLayout = rowLayout;
        this.dataMembers = members;
        cd = new ConnectionDetector(context);
    }

    @Override
    public TSubjectListApapter.VHItem onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new TSubjectListApapter.VHItem(v);
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
                    Intent i = new Intent(mContext, TDiaryPostList.class);
                    i.putExtra("ClassID", dataMembers.get(position).getClassID());
                    i.putExtra("SectionID", dataMembers.get(position).getSectionID());
                    i.putExtra("SubjectsID", dataMembers.get(position).getSubjectsID());
                    i.putExtra("DiaryID", dataMembers.get(position).getDiaryID());

                    i.putExtra("ClassName", dataMembers.get(position).getClassName());
                    i.putExtra("SectionName", dataMembers.get(position).getSectionName());
                    i.putExtra("SubjectsName", dataMembers.get(position).getSubjectsName());



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



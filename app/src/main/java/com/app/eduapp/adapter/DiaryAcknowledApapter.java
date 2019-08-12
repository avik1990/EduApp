package com.app.eduapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.eduapp.R;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.pojo.GetOrPostDiaryAcknowledgement;
import com.app.eduapp.pojo.TDiaryClass;
import com.app.eduapp.tattendance.AStudentListActivity;
import com.app.eduapp.tattendance.TakeAttendanceListActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DiaryAcknowledApapter extends RecyclerView.Adapter<DiaryAcknowledApapter.VHItem> {
    private Context mContext;
    private int rowLayout;
    ConnectionDetector cd;
    private List<GetOrPostDiaryAcknowledgement.DiaryAcknowledgementList> dataMembers;

    public DiaryAcknowledApapter(Context context, int rowLayout, List<GetOrPostDiaryAcknowledgement.DiaryAcknowledgementList> members, String flag) {
        this.mContext = context;
        this.rowLayout = rowLayout;
        this.dataMembers = members;
        cd = new ConnectionDetector(context);
    }

    @Override
    public DiaryAcknowledApapter.VHItem onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new DiaryAcknowledApapter.VHItem(v);
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
            viewHolder.tv_date.setVisibility(View.VISIBLE);
            viewHolder.tv_studentname.setVisibility(View.VISIBLE);
            viewHolder.tv_sectionname.setVisibility(View.VISIBLE);

            viewHolder.tv_name.setText(dataMembers.get(position).getFullName());
            viewHolder.tv_date.setText(dataMembers.get(position).getDateAdded());
            viewHolder.tv_studentname.setText(dataMembers.get(position).getStudentsFullName());
            viewHolder.tv_sectionname.setText(dataMembers.get(position).getClassName() + " Sec " + dataMembers.get(position).getSectionName());
            Picasso.get()
                    .load(dataMembers.get(position).getProfilePicture())
                    .into(viewHolder.iv_view);

        } catch (Exception e) {
        }
    }


    class VHItem extends RecyclerView.ViewHolder {
        TextView tv_name, tv_date, tv_studentname, tv_sectionname;
        ImageView iv_view;

        public VHItem(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_month);
            iv_view = itemView.findViewById(R.id.iv_view);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_studentname = itemView.findViewById(R.id.tv_studentname);
            tv_sectionname = itemView.findViewById(R.id.tv_sectionname);
        }
    }
}



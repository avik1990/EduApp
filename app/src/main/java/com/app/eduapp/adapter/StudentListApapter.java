package com.app.eduapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.eduapp.AttendanceListActivity;
import com.app.eduapp.R;
import com.app.eduapp.StudentProfile;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.helper.MapUtils;
import com.app.eduapp.pojo.GetStudentsList;
import com.app.eduapp.pojo.Months.MonthsList;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentListApapter extends RecyclerView.Adapter<StudentListApapter.VHItem> {
    private Context mContext;
    private int rowLayout;
    ConnectionDetector cd;
    private List<GetStudentsList.StudentsList> dataMembers;

    public StudentListApapter(Context context, int rowLayout, List<GetStudentsList.StudentsList> members, String flag) {
        this.mContext = context;
        this.rowLayout = rowLayout;
        this.dataMembers = members;
        cd = new ConnectionDetector(context);
    }

    @Override
    public StudentListApapter.VHItem onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new StudentListApapter.VHItem(v);
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
            viewHolder.tv_monthname.setText(dataMembers.get(position).getFullName());
            //viewHolder.iv_imageview.
            Picasso.get()
                    .load(dataMembers.get(position).getProfileImage())
                   // .resize(50, 50)
                    //.centerCrop()
                    .into(viewHolder.iv_imageview);

            viewHolder.rlv_month.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(MapUtils.list_student.size()>0){
                        //EdUtils.showToastShort(mContext,""+position);
                        Intent intent = new Intent(mContext, StudentProfile.class);
                        intent.putExtra("position", String.valueOf(position));
                        mContext.startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {

        }
    }


    class VHItem extends RecyclerView.ViewHolder {
        TextView tv_monthname;
        RelativeLayout rlv_month;
        de.hdodenhof.circleimageview.CircleImageView iv_imageview;

        public VHItem(View itemView) {
            super(itemView);
            iv_imageview = (CircleImageView) itemView.findViewById(R.id.iv_view);
            tv_monthname = (TextView) itemView.findViewById(R.id.tv_month);
            rlv_month = (RelativeLayout) itemView.findViewById(R.id.rlv_month);
        }
    }


}



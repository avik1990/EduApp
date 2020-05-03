package com.app.eduapp.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.eduapp.R;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.pojo.DiaryDetails;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DiaryStudentsListApapter extends RecyclerView.Adapter<DiaryStudentsListApapter.VHItem> {

    private Context mContext;
    private int rowLayout;
    ConnectionDetector cd;
    private List<DiaryDetails.DiaryStudentsList> dataMembers;
    String classname;

    public DiaryStudentsListApapter(Context context, int rowLayout, List<DiaryDetails.DiaryStudentsList> members, String flag) {
        this.mContext = context;
        this.rowLayout = rowLayout;
        this.dataMembers = members;
        this.classname = classname;
        cd = new ConnectionDetector(context);
    }

    @Override
    public DiaryStudentsListApapter.VHItem onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new DiaryStudentsListApapter.VHItem(v);
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
            Picasso.get()
                    .load(dataMembers.get(position).getProfileImage())
                    .resize(50, 50)
                    .centerCrop()
                    .into(viewHolder.iv_imageview);

        } catch (Exception e) {
        }
    }

    class VHItem extends RecyclerView.ViewHolder {
        TextView tv_monthname;
        CircleImageView iv_imageview;

        public VHItem(View itemView) {
            super(itemView);
            iv_imageview = (CircleImageView) itemView.findViewById(R.id.iv_view);
            tv_monthname = (TextView) itemView.findViewById(R.id.tv_month);
        }
    }
}



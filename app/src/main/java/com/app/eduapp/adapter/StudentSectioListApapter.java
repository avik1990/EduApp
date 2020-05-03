package com.app.eduapp.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.eduapp.ExamsHeldListActivity;
import com.app.eduapp.R;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.pojo.StudentsListByClassSectionEMP;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentSectioListApapter extends RecyclerView.Adapter<StudentSectioListApapter.VHItem> {
    private Context mContext;
    private int rowLayout;
    ConnectionDetector cd;
    private List<StudentsListByClassSectionEMP.StudentsList> dataMembers;
    String classid = "", sectionid = "";

    public StudentSectioListApapter(Context context, int rowLayout, List<StudentsListByClassSectionEMP.StudentsList> members, String flag, String classid, String sectionid) {
        this.mContext = context;
        this.rowLayout = rowLayout;
        this.dataMembers = members;
        this.classid = classid;
        this.sectionid = sectionid;
        cd = new ConnectionDetector(context);
    }

    @Override
    public StudentSectioListApapter.VHItem onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new StudentSectioListApapter.VHItem(v);
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
                    .resize(50, 50)
                    .centerCrop()
                    .into(viewHolder.iv_imageview);

            viewHolder.rlv_month.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ExamsHeldListActivity.class);
                    intent.putExtra("sectionid", sectionid);
                    intent.putExtra("studentid", dataMembers.get(position).getStudentsID());
                    intent.putExtra("classid", classid);
                    mContext.startActivity(intent);

                }
            });
        } catch (Exception e) {

        }
    }


    class VHItem extends RecyclerView.ViewHolder {
        TextView tv_monthname;
        RelativeLayout rlv_month;
        CircleImageView iv_imageview;

        public VHItem(View itemView) {
            super(itemView);
            iv_imageview = (CircleImageView) itemView.findViewById(R.id.iv_view);
            tv_monthname = (TextView) itemView.findViewById(R.id.tv_month);
            rlv_month = (RelativeLayout) itemView.findViewById(R.id.rlv_month);
        }
    }


}



package com.app.eduapp.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.eduapp.R;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.pojo.GetOrPostNoticeAcknowledgement;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NoticeAcknowledApapter extends RecyclerView.Adapter<NoticeAcknowledApapter.VHItem> {
    private Context mContext;
    private int rowLayout;
    ConnectionDetector cd;
    private List<GetOrPostNoticeAcknowledgement.NoticeAcknowledgementList> dataMembers;

    public NoticeAcknowledApapter(Context context, int rowLayout, List<GetOrPostNoticeAcknowledgement.NoticeAcknowledgementList> members, String flag) {
        this.mContext = context;
        this.rowLayout = rowLayout;
        this.dataMembers = members;
        cd = new ConnectionDetector(context);
    }

    @Override
    public NoticeAcknowledApapter.VHItem onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new NoticeAcknowledApapter.VHItem(v);
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



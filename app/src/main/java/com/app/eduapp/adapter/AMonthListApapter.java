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
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.pojo.Months.MonthsList;
import com.app.eduapp.tattendance.TAttendanceListActivity;

import java.util.List;

public class AMonthListApapter extends RecyclerView.Adapter<AMonthListApapter.VHItem> {
    private Context mContext;
    private int rowLayout;
    ConnectionDetector cd;
    private List<MonthsList> dataMembers;
    String studentid;

    public AMonthListApapter(Context context, int rowLayout, List<MonthsList> members, String flag,String studentid) {
        this.mContext = context;
        this.rowLayout = rowLayout;
        this.dataMembers = members;
        this.studentid=studentid;
        cd = new ConnectionDetector(context);
    }

    @Override
    public AMonthListApapter.VHItem onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new AMonthListApapter.VHItem(v);
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
            viewHolder.tv_monthname.setText(dataMembers.get(position).getYearMonthFullName());


            viewHolder.rlv_month.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, TAttendanceListActivity.class);
                    intent.putExtra("position", String.valueOf(position));
                    intent.putExtra("YearMonth", dataMembers.get(position).getYearMonth());
                    intent.putExtra("studentid", studentid);
                    mContext.startActivity(intent);

                }
            });
        } catch (Exception e) {

        }
    }

    class VHItem extends RecyclerView.ViewHolder {
        TextView tv_monthname;
        RelativeLayout rlv_month;

        public VHItem(View itemView) {
            super(itemView);
            tv_monthname = (TextView) itemView.findViewById(R.id.tv_month);
            rlv_month = (RelativeLayout) itemView.findViewById(R.id.rlv_month);
        }
    }


}



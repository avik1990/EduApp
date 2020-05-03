package com.app.eduapp.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.eduapp.AttendanceListActivity;
import com.app.eduapp.R;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.pojo.Months;

import java.util.List;

public class MonthListApapter extends RecyclerView.Adapter<MonthListApapter.VHItem> {
    private Context mContext;
    private int rowLayout;
    ConnectionDetector cd;
    private List<Months.MonthsList> dataMembers;

    public MonthListApapter(Context context, int rowLayout, List<Months.MonthsList> members, String flag) {
        this.mContext = context;
        this.rowLayout = rowLayout;
        this.dataMembers = members;
        cd = new ConnectionDetector(context);
    }

    @Override
    public MonthListApapter.VHItem onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new MonthListApapter.VHItem(v);
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
                    Intent intent = new Intent(mContext, AttendanceListActivity.class);
                    intent.putExtra("position", String.valueOf(position));
                    intent.putExtra("YearMonth", dataMembers.get(position).getYearMonth());
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



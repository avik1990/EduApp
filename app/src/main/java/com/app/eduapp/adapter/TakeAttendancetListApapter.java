package com.app.eduapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.eduapp.R;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.pojo.StudentsListByClassSectionEMP;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TakeAttendancetListApapter extends RecyclerView.Adapter<TakeAttendancetListApapter.VHItem> {

    private Context mContext;
    private int rowLayout;
    ConnectionDetector cd;
    private List<StudentsListByClassSectionEMP.StudentsList> dataMembers;
    String classname;

    public TakeAttendancetListApapter(Context context, int rowLayout, List<StudentsListByClassSectionEMP.StudentsList> members, String flag, String classname) {
        this.mContext = context;
        this.rowLayout = rowLayout;
        this.dataMembers = members;
        this.classname = classname;
        cd = new ConnectionDetector(context);
    }

    @Override
    public TakeAttendancetListApapter.VHItem onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new TakeAttendancetListApapter.VHItem(v);
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
            if (!dataMembers.get(position).getSchoolRollNumber().isEmpty()) {
                viewHolder.tv_number.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tv_rollno.setVisibility(View.GONE);
            }
            viewHolder.tv_number.setText((dataMembers.get(position).getSchoolRollNumber()) + ". ");


            Picasso.get()
                    .load(dataMembers.get(position).getProfileImage())
                    .resize(50, 50)
                    .centerCrop()
                    .into(viewHolder.iv_imageview);

          /*  viewHolder.img_present.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EdUtils.showToastShort(mContext, "Present");
                }
            });

            viewHolder.img_absent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EdUtils.showToastShort(mContext, "Absent");
                }
            });*/

            if (dataMembers.get(position).getIsPresent().equalsIgnoreCase("N")) {
                viewHolder.img_absent.setVisibility(View.VISIBLE);
                viewHolder.img_present.setVisibility(View.GONE);
            } else if (dataMembers.get(position).getIsPresent().equalsIgnoreCase("Y")) {
                viewHolder.img_absent.setVisibility(View.GONE);
                viewHolder.img_present.setVisibility(View.VISIBLE);
            }


            Log.d("SlecetedValue", "" + dataMembers.get(position).getCheck_selected());
            if (dataMembers.get(position).getCheck_selected().equalsIgnoreCase("P")) {
                viewHolder.checkBox.setChecked(true);
            } else {
                viewHolder.checkBox.setChecked(false);
            }

            viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        dataMembers.get(position).setCheck_selected("P");
                    } else {
                        dataMembers.get(position).setCheck_selected("A");
                    }
                }
            });


        } catch (Exception e) {
        }
    }

    class VHItem extends RecyclerView.ViewHolder {
        TextView tv_monthname, tv_number, tv_rollno;
        RelativeLayout rlv_month;
        CircleImageView iv_imageview;
        ImageView img_present, img_absent;
        CheckBox checkBox;

        public VHItem(View itemView) {
            super(itemView);
            tv_rollno = (TextView) itemView.findViewById(R.id.tv_rollno);
            iv_imageview = (CircleImageView) itemView.findViewById(R.id.iv_view);
            tv_monthname = (TextView) itemView.findViewById(R.id.tv_month);
            rlv_month = (RelativeLayout) itemView.findViewById(R.id.rlv_month);
            tv_number = (TextView) itemView.findViewById(R.id.tv_number);
            img_present = (ImageView) itemView.findViewById(R.id.img_present);
            img_absent = (ImageView) itemView.findViewById(R.id.img_absent);
            checkBox = itemView.findViewById(R.id.checkbox);

        }
    }
}



package com.app.eduapp.adapter;

import android.content.Context;
import android.content.DialogInterface;

import androidx.percentlayout.widget.PercentRelativeLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.eduapp.NotificationListActivity;
import com.app.eduapp.R;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.pojo.ModelInbox;
import com.app.eduapp.sqlitedb.LCDatabaseHandler;

import java.util.List;

public class NotificationListApapter extends RecyclerView.Adapter<NotificationListApapter.VHItem> {
    private Context mContext;
    private int rowLayout;
    ConnectionDetector cd;
    private List<ModelInbox> dataMembers;
    LCDatabaseHandler db;

    public NotificationListApapter(Context context, int rowLayout, List<ModelInbox> members, String flag) {
        this.mContext = context;
        this.rowLayout = rowLayout;
        this.dataMembers = members;
        cd = new ConnectionDetector(context);
        db = new LCDatabaseHandler(context);
    }

    @Override
    public NotificationListApapter.VHItem onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new NotificationListApapter.VHItem(v);
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
            //EdUtils.showToastShort(mContext,""+dataMembers.get(position).getInbox_current_time());
            viewHolder.tv_date.setText(dataMembers.get(position).getInbox_current_time());
            viewHolder.tv_title.setText(dataMembers.get(position).getInbox_title());
            viewHolder.tv_msg.setText(dataMembers.get(position).getInbox_msg());


            viewHolder.v_parent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Delete");

                    //Setting message manually and performing action on button click
                    builder.setMessage("Are you sure you want to delete?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    db.deleteNote(dataMembers.get(position).getInboxId());
                                    ((NotificationListActivity) mContext).setuplistview();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
                                }
                            });
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    alert.show();
                    return false;
                }
            });


            //viewHolder.iv_imageview.
            /*Picasso.get()
                    .load(dataMembers.get(position).getProfileImage())
                    .resize(50, 50)
                    .centerCrop()
                    .into(viewHolder.iv_imageview);*/

        } catch (Exception e) {

        }
    }


    class VHItem extends RecyclerView.ViewHolder {
        TextView tv_title, tv_msg, tv_date;
        RelativeLayout rlv_month;
        PercentRelativeLayout v_parent;

        public VHItem(View itemView) {
            super(itemView);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_msg = (TextView) itemView.findViewById(R.id.tv_msg);
            v_parent = itemView.findViewById(R.id.v_parent);
        }
    }


}


